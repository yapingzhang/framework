package cn.bidlink.framework.jms.listener;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.apache.log4j.Logger;

/**
 * 
 * @description: 异常监控
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012-9-12 上午10:43:59
 */
public class BidJmsExceptionListener  implements ExceptionListener {

	private static Logger logger = Logger.getLogger(BidJmsExceptionListener.class);
 
	public void onException(JMSException paramJMSException) {
 		logger.error(paramJMSException.getMessage(),paramJMSException);
	}

}
