package cn.bidlink.framework.jms.listener;

import java.io.IOException;

import org.apache.activemq.transport.TransportListener;
import org.apache.log4j.Logger;

/**
 * @description:	消息传输监听
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012-9-12 上午10:40:09
 */
public class BidJmsTransportListener implements TransportListener {
	private static Logger logger = Logger.getLogger(BidJmsTransportListener.class);

	@Override
	public void onCommand(Object paramObject) {
		logger.debug(paramObject);
	}

	@Override
	public void onException(IOException iOException) {
		logger.debug("异常:"+iOException);
		logger.error(iOException.getMessage(),iOException);
	}

	@Override
	public void transportInterupted() {
		 
	}

	@Override
	public void transportResumed() {
		 
		
	}

 
}
