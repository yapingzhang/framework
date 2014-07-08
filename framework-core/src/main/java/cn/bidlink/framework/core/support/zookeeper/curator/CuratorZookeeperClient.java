package cn.bidlink.framework.core.support.zookeeper.curator;

import com.netflix.curator.framework.CuratorFramework;

import cn.bidlink.framework.core.support.zookeeper.ZookeeperClient;

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
