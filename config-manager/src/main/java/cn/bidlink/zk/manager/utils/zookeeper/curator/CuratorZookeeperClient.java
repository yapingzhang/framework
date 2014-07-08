package cn.bidlink.zk.manager.utils.zookeeper.curator;

import cn.bidlink.zk.manager.utils.zookeeper.ZookeeperClient;

import com.netflix.curator.framework.CuratorFramework;

/**
 * 
 * @author dejianliu
 *
 */
public interface CuratorZookeeperClient extends ZookeeperClient{
	
	/**
	 * @description curator实现时使用
	 * @return
	 */
	CuratorFramework getCuratorFramework();

}
