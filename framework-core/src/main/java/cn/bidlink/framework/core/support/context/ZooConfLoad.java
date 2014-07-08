package cn.bidlink.framework.core.support.context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooDefs.Ids;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import cn.bidlink.framework.core.commons.Constants;
import cn.bidlink.framework.core.support.context.ConfigChangedInfo.NodeType;
import cn.bidlink.framework.core.support.context.ConfigChangedInfo.OptType;
import cn.bidlink.framework.core.support.zookeeper.ChildListener;
import cn.bidlink.framework.core.support.zookeeper.curator.CuratorZookeeperClient;
import cn.bidlink.framework.core.utils.NetUtils;
import cn.bidlink.framework.core.utils.OSUtils;
import cn.bidlink.framework.core.utils.Pid;
import cn.bidlink.framework.core.utils.crypto.MD5;
import cn.bidlink.framework.core.utils.holder.PropertiesHolder;

import com.alibaba.dubbo.common.utils.ConfigUtils;

/**
 * 
 * @description: zookeeper 配置文件加载
 * @version Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2013-6-3 下午3:31:10
 */
public class ZooConfLoad {

	private static Logger logger = Logger.getLogger(ZooConfLoad.class);

	private static ApplicationContext applicationContext;
	
	
	/**
	 * 配置信息
	 */
	public static ConcurrentHashMap<String, Properties> curPropMap = new ConcurrentHashMap<String, Properties>();
 
	/**
	 * xml 目前不支持reload 因为如果大量xml 配置修改会影响到系统的稳定性
	 */
	public static ConcurrentHashMap<String, byte[]> xmlMap = new ConcurrentHashMap<String, byte[]>();
	
	/**
	 * log4j 配置信息
	 */
	public static ConcurrentHashMap<String, byte[]> xmlLog4jMap = new ConcurrentHashMap<String, byte[]>();
	
	/**
	 * @description 初始化上下文
	 * @param applicationContext
	 */
	public static void initContext(ApplicationContext applicationContext) {
		ZooConfLoad.applicationContext = applicationContext;
	}
	
	/**
	 * @description 加载远程配置文件
	 * @param curatorClient
	 * @param basePath
	 * @return
	 */
	public static void loadProperties(final CuratorZookeeperClient curatorClient) {
		try {
			 String basePath = Constants.getRemoteConfPath();
			if (StringUtils.isNotEmpty(basePath)) {
				if (basePath.endsWith("/")) {
					basePath = basePath + "prop";
				} else {
					basePath = basePath + "/prop";
				}
			}
			/**
			 * 【1】取得当前节点子节点变化情况(增加/删除子节点通知)
			 */
			List<String> listChilds = curatorClient.addChildListener(basePath, new ChildListener() {
						@Override
						public void childChanged(String path,List<String> children) {
							if(children != null) {
								for (String c : children) {
									String fullPath = path + "/" + c;
 									try {
 										//添加节点时，增加properties.删除操作不在些做，在每个DataPropWatcher里处理
										if(!curPropMap.containsKey(fullPath)) {
											byte [] datas = curatorClient.getData(fullPath);
											Properties tmpP = new Properties();
											tmpP.load(new ByteArrayInputStream(datas));
 											curPropMap.put(fullPath, tmpP);
 											//【■】添加单个配置文件
											PropertiesHolder.addProperties(tmpP);
											//与老系统集成framework-pmp需要用到
											cn.bidlink.framework.util.holder.PropertiesHolder.addProperties(tmpP);
											//与dubbo集成配置文件
											ConfigUtils.addProperties(tmpP);
											//与系统监控集成
											addMonitorClientSupport(tmpP, fullPath);
											applicationContext.publishEvent(new ConfigChangedEvent(new ConfigChangedInfo(fullPath, new String(datas,Constants.charset), NodeType.PROPERTIES, OptType.CREATE)));
											curatorClient.getCuratorFramework().checkExists().usingWatcher(new DataPropWatcher(curPropMap,curatorClient,applicationContext)).forPath(fullPath);
										}
									} catch (Exception e) {
										logger.error(e.getMessage(),e);
									}
								}
							}
							
						}
					});
			
	 
			/**
			 * 【2】增加各子节点数据通知(数据修改，与节点删除)
			 */
			if (listChilds != null) {
				for (String child : listChilds) {
					String fullChildPath = basePath + "/" + child;
					byte [] datas = curatorClient.getData(fullChildPath);
					Properties tmpP = new Properties();
					tmpP.load(new ByteArrayInputStream(datas));
  					curPropMap.put(fullChildPath, tmpP);
					curatorClient.getCuratorFramework().checkExists().usingWatcher(new DataPropWatcher(curPropMap,curatorClient,applicationContext)).forPath(fullChildPath);
 				}
				reloadProp();
			
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
 	}

	/**
	 * @description  重新加载prop文件
	 */
    public static void reloadProp() {
       	 for(Iterator<Map.Entry<String, Properties>> it = curPropMap.entrySet().iterator();it.hasNext();) {
       		Map.Entry<String, Properties> entry = it.next();
       		//【■】将远程配置信息缓存至本地 PropertiesHolder 工具类，以方便应用程序使用
			PropertiesHolder.addProperties(entry.getValue());
			//与老系统集成framework-pmp需要用到
			cn.bidlink.framework.util.holder.PropertiesHolder.addProperties(entry.getValue());
			//与dubbo集成配置文件
			//第一次加载dubbo配置，让本地覆盖远程
			Properties propDub = entry.getValue();
			propDub.putAll(ConfigUtils.getProperties());
			ConfigUtils.setProperties(propDub);
			//与系统监控集成
			addMonitorClientSupport(entry.getValue(), entry.getKey());
       	 }	
    }
    
    
    /**
     * @description 加载远程xml文件
     * @param curatorClient
     */
    public static void loadXml(final CuratorZookeeperClient curatorClient) {
    	try {
    		 String basePath = Constants.getRemoteConfPath();
 			if (StringUtils.isNotEmpty(basePath)) {
 				if (basePath.endsWith("/")) {
 					basePath = basePath + "xml";
 				} else {
 					basePath = basePath + "/xml";
 				}
 			}
 			
 			List<String> listChilds = curatorClient.addChildListener(basePath, new ChildListener() {
				@Override
				public void childChanged(String path, List<String> children) {
 					
				}
 			});
    		
 			/**
			 * 【2】增加各子节点数据通知(数据修改，与节点删除)
			 */
			if (listChilds != null) {
				for (String child : listChilds) {
					String fullChildPath = basePath + "/" + child;
					byte [] datas = curatorClient.getData(fullChildPath);
					xmlMap.put(fullChildPath, datas);
 				}
 			}
    	}catch (Exception e) {
    		logger.error(e.getMessage(), e);
		}
    }
    
    /**
     * 
     * @description 加载log4j日志配置
     * @param curatorClient
     */
    public static void loadLog4jXml(final CuratorZookeeperClient curatorClient) {
    	try {
   		 String basePath = Constants.getRemoteConfPath();
			if (StringUtils.isNotEmpty(basePath)) {
				if (basePath.endsWith("/")) {
					basePath = basePath + "log4j";
				} else {
					basePath = basePath + "/log4j";
				}
			}
			
			List<String> listChilds = curatorClient.addChildListener(basePath, new ChildListener() {
				@Override
				public void childChanged(String path, List<String> children) {
					if(children != null) {
						for (String c : children) {
							String fullPath = path + "/" + c;
								try {
									//添加节点时，增加log4j.删除操作不在些做，在每个DataLog4jWatcher里处理
								if(!xmlLog4jMap.containsKey(fullPath)) {
									byte [] datas = curatorClient.getData(fullPath);
									xmlLog4jMap.put(fullPath, datas);
									writeLog4jToLocal(datas);
									applicationContext.publishEvent(new ConfigChangedEvent(new ConfigChangedInfo(fullPath, new String(datas,Constants.charset), NodeType.LOG4J, OptType.CREATE)));
									curatorClient.getCuratorFramework().checkExists().usingWatcher(new DataLog4jWatcher(xmlLog4jMap,curatorClient,applicationContext)).forPath(fullPath);
								}
							} catch (Exception e) {
								logger.error(e.getMessage(),e);
							}
						}
					}
				}
			});
   		
			/**
			 * 【2】增加各子节点数据通知(数据修改，与节点删除)
			 */
			if (listChilds != null) {
				for (String child : listChilds) {
					String fullChildPath = basePath + "/" + child;
					byte [] datas = curatorClient.getData(fullChildPath);
					xmlLog4jMap.put(fullChildPath, datas);
					curatorClient.getCuratorFramework().checkExists().usingWatcher(new DataLog4jWatcher(xmlLog4jMap,curatorClient,applicationContext)).forPath(fullChildPath);
					writeLog4jToLocal(datas);
 				}
			}
   	}catch (Exception e) {
   		logger.error(e.getMessage(), e);
	 }
    }
 
	public static ConcurrentHashMap<String, byte[]> getXmlMap() {
		return xmlMap;
	}

	public static void setXmlMap(ConcurrentHashMap<String, byte[]> xmlMap) {
		ZooConfLoad.xmlMap = xmlMap;
	}

	/**
	 * @throws IOException 
	 * @description 加载本地app配置信息
	 */
	public static void loadLocalApp(final CuratorZookeeperClient curatorClient)  {
		try {
			String appPath = System.getProperty("user.dir");
	        String pid = String.valueOf(Pid.getJvmPid());
			Properties p = new Properties();
			p.put("appPath",appPath);
			p.put("configPath",Constants.getRemoteConfPath());
			p.put("processId",pid);
			p.put("appName",Constants.getAppName());
			p.put("serverIp",NetUtils.getLocalIp());
	 		p.put("createTime",String.valueOf(System.currentTimeMillis()));
	 		p.put("startConf",getRuntimeConf(pid));
	        ByteArrayOutputStream bout = new ByteArrayOutputStream();
	        System.getProperties().store(bout, "system");
			bout.close();
			p.put("systemConf",new String(bout.toByteArray()));
			
			ByteArrayOutputStream resOut = new ByteArrayOutputStream();
			p.store(resOut, "系统配置信息");
			resOut.close();
			String nodeId = MD5.encrypt(appPath);
			String path =Constants.ZOO_CONF_DATA+"/"+nodeId;
			if(!curatorClient.isExist(path)) {
				curatorClient.create(path, false, false, resOut.toByteArray(), Ids.CREATOR_ALL_ACL);
			} else {
				try {
				     curatorClient.delete(path); //确保删除操作不影响使用 ,处理session过期临界点时。
				}catch (Exception e) { 
					 logger.error(e.getMessage(),e);
				}
				curatorClient.create(path, false, false, resOut.toByteArray(), Ids.CREATOR_ALL_ACL);
 			}
		}catch (Exception e) {
			 logger.error(e.getMessage(),e);
		}
	}
	
	public static String getRuntimeConf(String pid) {
 		String command = " jps -lmvV ";
		try {
			InputStream input = null;
			if (OSUtils.isLinux()) {
				input = execute(" " + command);
			} else if (!OSUtils.isLinux()) {
				input = execute("cmd.exe /c " + command);
			}
			List<String> listRes = IOUtils.readLines(input, "UTF-8");
			for (String res : listRes) {
				if(res.startsWith(pid)) {
					return res;
				}		       
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
 	}
	
	public static InputStream execute(String command) throws IOException {
		Process process = Runtime.getRuntime().exec(command);
		return process.getInputStream();
	}
	
	
    public static void writeLog4jToLocal(byte [] buf) {
    	try {
    		String path = BidFrameworkSpringContext.class.getResource("/").getFile();
    		File fileDir = new File(path);
    		if(fileDir.exists()) {
    			File log4jFile = new File(fileDir,"log4j.xml");
     		    FileUtils.writeByteArrayToFile(log4jFile, buf);	
    		}
    	}catch (Exception e) {
			 logger.error(e.getMessage(),e);
		}
    }
 
    /**
     * @description 增加对系统监控的配置动态支持
     * @param propByte
     */
    public static void addMonitorClientSupport(Properties prop,String path) {
    	try {
    		//为了避免大范围加载不需要的配置文档，这里限定必须为 monitor-conf.properties 文件才做修改
    		if(!path.endsWith("monitor-conf.properties")) {
    			return;
    		}
    		
    		String clsStr = "cn.bidlink.monitor.core.utils.MonitorConfigUtils";
    		Class<?> cls = Class.forName(clsStr);
    		Method mclear = ReflectionUtils.findMethod(cls, "clear");
    		if(mclear != null) { //先清空再加载
    			mclear.setAccessible(true);
    			mclear.invoke(null);
     		}
    		Method method = ReflectionUtils.findMethod(cls, "load", new Class<?>[]{Properties.class});
    		if(method != null) {
    			method.setAccessible(true);
    			method.invoke(null, prop);
     		}
    		
    	}catch (Exception e) {}
    }

	
}
