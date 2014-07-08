package cn.bidlink.zk.manager.utils.zookeeper;

import cn.bidlink.zk.manager.utils.zookeeper.curator.CuratorZookeeperClient;


/**
 * 
 * @description:
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:28:30
 */
public interface ZookeeperTransporter {


	CuratorZookeeperClient connect(String connections);

	CuratorZookeeperClient connect(String connections,byte [] auths);
}
