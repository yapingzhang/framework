package cn.bidlink.framework.core.support.zookeeper.curator;

import cn.bidlink.framework.core.support.zookeeper.ZookeeperTransporter;


/** 
 * @description:	 TODO add description
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2013-5-20 下午6:09:38
 */
public class CuratorZookeeperTransporter implements ZookeeperTransporter {

	@Override
	public CuratorZookeeperClient connect(String connections) {
		return new CuratorZookeeperClientImpl(connections,null);
	}

	@Override
	public CuratorZookeeperClient connect(String connections, byte[] auths) {
		return new CuratorZookeeperClientImpl(connections,auths);
	}

 

}
