package cn.bidlink.framework.core.support.zookeeper;

/**
 * 
 * @description:	 TODO add description
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2013-6-3 下午2:33:09
 */
public interface StateListener {

	int DISCONNECTED = 0;

	int CONNECTED = 1;

	int RECONNECTED = 2;

	void stateChanged(int connected);

}
