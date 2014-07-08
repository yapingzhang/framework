package cn.bidlink.framework.core.support.zookeeper;

import cn.bidlink.framework.core.support.zookeeper.curator.CuratorZookeeperClient;


/**
 * @description:	 TODO add description
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2013-5-20 下午6:10:10
 */
public interface ZookeeperTransporter {


	CuratorZookeeperClient connect(String connections);

	CuratorZookeeperClient connect(String connections,byte [] auths);
}
