package cn.bidlink.framework.core.support.zookeeper;

import java.util.List;

/**
 * 
 * @description:	 TODO add description
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2013-6-3 下午2:33:14
 */
public interface ChildListener {

	void childChanged(String path, List<String> children);

}
