package cn.bidlink.framework.jms.listener;

import java.util.concurrent.TimeUnit;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;

import cn.bidlink.framework.core.utils.holder.PropertiesHolder;
import cn.bidlink.framework.jms.BidMessageHandler;
import cn.bidlink.framework.jms.exceptions.BidJmsException;

/**
 * @description: 异步消息监听
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-9-12 上午10:48:32
 * 
 */
public class BidTopicAsynMsgListener implements MessageListener,
		ApplicationContextAware, InitializingBean,Ordered {
	private static Logger logger = Logger.getLogger(BidTopicAsynMsgListener.class);

	private static BidMessageHandler bidMessageHandler;

	private ApplicationContext applicationContext = null;

	@Override
	public void onMessage(Message message) {
		try {

			if (bidMessageHandler != null) {
				bidMessageHandler.handlerMessage(message);
			} else {
				String lisImpl = PropertiesHolder.getProperty("mq.topic.listener.impl.class");
				if (StringUtils.isEmpty(lisImpl)) {
					bidMessageHandler = BeanUtils.instantiateClass(DefaultBidMessageHandler.class);
					logger.debug("init topic message listener!");
				} else {
					Class<?> clsIml = Class.forName(lisImpl);
					bidMessageHandler = getBidMessageHandlerFromContext(applicationContext, clsIml);
				}
				bidMessageHandler.handlerMessage(message);
			}
			
			//sleep
			if(bidMessageHandler.getTimeInterval() > 0) {
	            TimeUnit.MILLISECONDS.sleep(bidMessageHandler.getTimeInterval());
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
	
	private static String firstWordToLowerCase(String word) {
		if (word.length()<2) {
			throw new BidJmsException("class name length must greater than one!");
		}
		String firstWord = word.substring(0,1).toLowerCase();
		String afterWork = word.substring(1,word.length());
	    return firstWord+afterWork;
	}
	
	/**
	 * 
	 * @description  从容器中获取bean 
	 * @param applicationContext
	 * @param clsIml
	 * @return
	 */
	public static BidMessageHandler getBidMessageHandlerFromContext(ApplicationContext applicationContext,Class<?> clsIml) {
		try {
			String beanName = firstWordToLowerCase(clsIml.getSimpleName());
			
			if(applicationContext.containsBean(beanName)) {
				return (BidMessageHandler) applicationContext.getBean(beanName, clsIml);
			} else {
			    return 	(BidMessageHandler) applicationContext.getBean(clsIml);
			}
		}catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE - 2;
	}

	public static void setBidMessageHandler(BidMessageHandler bidMessageHandler) {
		BidTopicAsynMsgListener.bidMessageHandler = bidMessageHandler;
	}
	
	

 
}
