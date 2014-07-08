package cn.bidlink.framework.jms.listener;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;

import cn.bidlink.framework.core.utils.holder.PropertiesHolder;
import cn.bidlink.framework.jms.BidMessageHandler;
import cn.bidlink.framework.jms.exceptions.BidJmsException;

/**
 * @description: TODO add description
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-10-15 下午3:19:42
 * 
 */
public class BidTopicListeners implements ApplicationContextAware,
		InitializingBean, Ordered {

	private static Logger logger = Logger.getLogger(BidTopicListeners.class);

	private ConnectionFactory connectionFactory;

	private Topic destination;

	private Connection connection = null;

	private boolean isAutoStart = true;
	
	private int sessionAcknowledgeMode = Session.AUTO_ACKNOWLEDGE;
	
	private String clientId = "clientId-"+System.currentTimeMillis();

	/**
	 * 订阅者名称
	 */
	private String durableSubscriptionName = "";
	
	/**
	 * 是否持久订阅者
	 */
	private boolean isDurableSubscriber = false;
	
	private ApplicationContext applicationContext = null;

	private static BidMessageHandler bidMessageHandler;
	
	public void init() throws JMSException {
		Session session = connection.createSession(false,sessionAcknowledgeMode);
	    MessageConsumer messageConsumer = null;
		if (isDurableSubscriber) {
			messageConsumer = session.createDurableSubscriber(destination,durableSubscriptionName); 
	    } else {
	    	messageConsumer = session.createConsumer(destination);
	    }
		messageConsumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				messageHandler(message);
				
			}
		});
		
	}
	
	/**
	 * @description 消息处理
	 * @param message
	 */
	private void messageHandler(Message message) {
		 
		try {
			 
			if (bidMessageHandler != null) {
				bidMessageHandler.handlerMessage(message);
			} else {
				String lisImpl = PropertiesHolder.getProperty("mq.topic.listener.impl.class");
				if (StringUtils.isEmpty(lisImpl)) {
 					bidMessageHandler = getBidMessageHandlerFromContext(applicationContext, DefaultBidMessageHandler.class);
					logger.debug("init queue message listener!");
				} else {
					Class<?> clsIml = Class.forName(lisImpl);
					bidMessageHandler = getBidMessageHandlerFromContext(applicationContext, clsIml);
				}
				bidMessageHandler.handlerMessage(message);
			}
//			message.acknowledge();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		connection = connectionFactory.createConnection();
		if(connection != null) {
			connection.setClientID(clientId);
		}
		if (isAutoStart) {
			init();
			connection.start();
		}
	}

	private static String firstWordToLowerCase(String word) {
		if (word.length() < 2) {
			throw new BidJmsException(
					"class name length must greater than one!");
		}
		String firstWord = word.substring(0, 1).toLowerCase();
		String afterWork = word.substring(1, word.length());
		return firstWord + afterWork;
	}

	/**
	 * 
	 * @description 从容器中获取bean
	 * @param applicationContext
	 * @param clsIml
	 * @return
	 */
	public static BidMessageHandler getBidMessageHandlerFromContext(
			ApplicationContext applicationContext, Class<?> clsIml) {
		try {

			String beanName = firstWordToLowerCase(clsIml.getSimpleName());

			if (applicationContext.containsBean(beanName)) {
				return (BidMessageHandler) applicationContext.getBean(beanName,
						clsIml);
			} else {
				return (BidMessageHandler) applicationContext.getBean(clsIml);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE - 3;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void setAutoStart(boolean isAutoStart) {
		this.isAutoStart = isAutoStart;
	}
 
	public void setSessionAcknowledgeMode(int sessionAcknowledgeMode) {
		this.sessionAcknowledgeMode = sessionAcknowledgeMode;
	}

 

	public void setDestination(Topic destination) {
		this.destination = destination;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setDurableSubscriptionName(String durableSubscriptionName) {
		this.durableSubscriptionName = durableSubscriptionName;
	}

	public void setDurableSubscriber(boolean isDurableSubscriber) {
		this.isDurableSubscriber = isDurableSubscriber;
	}

	 
    
	
}
