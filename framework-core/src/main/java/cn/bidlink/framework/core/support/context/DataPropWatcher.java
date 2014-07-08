package cn.bidlink.framework.core.support.context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.springframework.context.ApplicationContext;

import cn.bidlink.framework.core.commons.Constants;
import cn.bidlink.framework.core.support.context.ConfigChangedInfo.NodeType;
import cn.bidlink.framework.core.support.context.ConfigChangedInfo.OptType;
import cn.bidlink.framework.core.support.zookeeper.curator.CuratorZookeeperClient;
import cn.bidlink.framework.core.utils.holder.PropertiesHolder;

import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.netflix.curator.framework.api.CuratorWatcher;


/**
 * @description: 数据节点观察者
 * @version Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2013-6-3 下午5:49:40
 */
public class DataPropWatcher implements CuratorWatcher {

	private static Logger logger = Logger.getLogger(DataPropWatcher.class);
	
	private  ConcurrentHashMap<String, Properties> curMap;
	
	private CuratorZookeeperClient curatorClient;
 
	private ApplicationContext applicationContext;

	public DataPropWatcher(ConcurrentHashMap<String, Properties> curMap, CuratorZookeeperClient curatorClient,ApplicationContext applicationContext) {
		this.curMap = curMap;
		this.curatorClient = curatorClient;
		this.applicationContext = applicationContext;
 	}

	@Override
	public void process(WatchedEvent event) throws Exception {
		//节点数据改变
		if (event.getType() != EventType.NodeDeleted) {
			try {
				curatorClient.getCuratorFramework().checkExists().usingWatcher(this).forPath(event.getPath());
				String path =  event.getPath();
				byte [] buf = curatorClient.getData(path);
				Properties tmp = new Properties();
				tmp.load(new ByteArrayInputStream(buf));
				//先删除该节点properties数据，再重新装载数据
				synchronized (this) {
					//【■】单个配置文件修改将以覆盖的方式，替换相同key的值
					PropertiesHolder.addProperties(tmp);
					//与老系统集成framework-pmp需要用到
					cn.bidlink.framework.util.holder.PropertiesHolder.addProperties(tmp);
					//与dubbo集成
					ConfigUtils.addProperties(tmp); 
					//与系统监控集成
					ZooConfLoad.addMonitorClientSupport(tmp, path);

					//缓存快照
					curMap.put(path, tmp);
					applicationContext.publishEvent(new ConfigChangedEvent(new ConfigChangedInfo(path, new String(buf,Constants.charset), NodeType.PROPERTIES, OptType.UPDATE)));

				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		} else if(event.getType() == EventType.NodeDeleted){ //节点被删除
			synchronized (this) {
  				//【■】删除本地缓存Properties相同的key对应的值配置
				removePartFromTotal(PropertiesHolder.getProperties(), curMap.get(event.getPath()));
				//与老系统集成framework-pmp需要用到
				removePartFromTotal(cn.bidlink.framework.util.holder.PropertiesHolder.getProperties(), curMap.get(event.getPath()));
				//与dubbo集成
				removePartFromTotal(ConfigUtils.getProperties(), curMap.get(event.getPath()));
				//与系统监控集成（删除操作对系统监控不起作用）
				Properties propDel = curMap.get(event.getPath());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				propDel.store(out, event.getPath());
				applicationContext.publishEvent(new ConfigChangedEvent(new ConfigChangedInfo(event.getPath(), new String(out.toByteArray(),Constants.charset), NodeType.PROPERTIES, OptType.DELETE)));
				out.close();
				//删除节点，将删除该节点对应的所有properties文件数据
				curMap.remove(event.getPath());
			}
			
		}
	}
	
	
	/**
	 * @description  将用户删除的prop文件从本地缓存中删除
	 * @param total
	 * @param part
	 */
	private void removePartFromTotal(Properties total,Properties part) {
		if(part == null) {
			return;
		}
		Set<Map.Entry<Object,Object>> sets = part.entrySet();
		for(Iterator<Map.Entry<Object,Object>> it = sets.iterator();it.hasNext();) {
			Map.Entry<Object,Object> entry = it.next();
			total.remove(entry.getKey());
		}
	}
	
	
}
