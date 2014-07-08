package cn.bidlink.framework.core.support.context;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.springframework.context.ApplicationContext;

import cn.bidlink.framework.core.commons.Constants;
import cn.bidlink.framework.core.support.context.ConfigChangedInfo.NodeType;
import cn.bidlink.framework.core.support.context.ConfigChangedInfo.OptType;
import cn.bidlink.framework.core.support.zookeeper.curator.CuratorZookeeperClient;

import com.netflix.curator.framework.api.CuratorWatcher;


/**
 * @description: 数据节点观察者
 * @version Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2013-6-3 下午5:49:40
 */
public class DataLog4jWatcher implements CuratorWatcher {

	private static Logger logger = Logger.getLogger(DataLog4jWatcher.class);
	
	private ConcurrentHashMap<String, byte[]> xmlLog4jMap;
	
	private CuratorZookeeperClient curatorClient;
 
	private ApplicationContext applicationContext;

	public DataLog4jWatcher(ConcurrentHashMap<String, byte[]> xmlLog4jMap,CuratorZookeeperClient curatorClient,ApplicationContext applicationContext) {
		this.curatorClient = curatorClient;
		this.applicationContext = applicationContext;
		this.xmlLog4jMap = xmlLog4jMap;
 	}

	@Override
	public void process(WatchedEvent event) throws Exception {
		//节点数据改变
		if (event.getType() != EventType.NodeDeleted) {
			try {
				synchronized (this) {
					curatorClient.getCuratorFramework().checkExists().usingWatcher(this).forPath(event.getPath());
					String path =  event.getPath();
					byte [] buf = curatorClient.getData(path);
					xmlLog4jMap.put(event.getPath(), buf);
					applicationContext.publishEvent(new ConfigChangedEvent(new ConfigChangedInfo(path, new String(buf,Constants.charset), NodeType.LOG4J, OptType.UPDATE)));
					ZooConfLoad.writeLog4jToLocal(buf);	
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		} else if(event.getType() == EventType.NodeDeleted){ //节点被删除
			synchronized (this) {
				byte [] log4jData = xmlLog4jMap.get(event.getPath());
				applicationContext.publishEvent(new ConfigChangedEvent(new ConfigChangedInfo(event.getPath(), new String(log4jData,Constants.charset), NodeType.LOG4J, OptType.DELETE)));
				xmlLog4jMap.remove(event.getPath());
				//删除不做任务log4j操作
			}
			
		}
	}
	
 
}
