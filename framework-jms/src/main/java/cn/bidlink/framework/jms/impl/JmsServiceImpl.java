package cn.bidlink.framework.jms.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.Topic;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.ProducerCallback;

import cn.bidlink.framework.core.support.jta.JtaContext;
import cn.bidlink.framework.core.utils.NetUtils;
import cn.bidlink.framework.jms.JmsService;
import cn.bidlink.framework.jms.enums.DestinationType;

/**
 * @description: JMS 消息生产者
 * @version Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2010-06-12 上午10:09:51
 */
// @Service
public class JmsServiceImpl  implements JmsService {

	private static Logger log = Logger.getLogger(JmsServiceImpl.class);

	// @Autowired
	private JmsTemplate jmsTemplate;

	/**
	 * @param message
	 *            待发送的消息(消息destination 从jmsTemplate 取一个默认的消息目的地)
	 * @throws Exception
	 */
	public void sendMsg(Message message) throws Exception {
		sendMsg(jmsTemplate.getDefaultDestination(), message,
				Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
				Message.DEFAULT_TIME_TO_LIVE);
	}

	/**
	 * 
	 * @param destination
	 *            消息待传送到的目的地
	 * @param message
	 *            待发送的消息
	 * @throws Exception
	 */
	public void sendMsg(Destination destination, Message message)
			throws Exception {
		sendMsg(destination, message, Message.DEFAULT_DELIVERY_MODE,
				Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
	}

	/**
	 * @param message
	 *            待发送的消息 (消息destination 从jmsTemplate 取一个默认的消息目的地)
	 * @param deliveryMode
	 *            传送模式 默认 PERSISTENT(DeliveryMode)
	 * @param priority
	 *            消息优先级 默认的优先级是4。(0是最低优先级，9是最高优先级.客户端应当认为优先级0-4是渐变的普通优先级，优先级5-9是渐变的加急优先级。 )
	 * @param timeToLive
	 *            消息的存活周期（以毫秒为单位） 默认的超时时间是没有限制的；消息永不过期。(0表示永不过期)
	 * @throws Exception
	 */
	public void sendMsg(Message message, int deliveryMode, int priority,
			long timeToLive) throws Exception {
		sendMsg(jmsTemplate.getDefaultDestination(), message, deliveryMode,
				priority, timeToLive);
	}

	/**
	 * @param destination
	 *            消息目的地址
	 * @param message
	 *            待发送的消息
	 * @param deliveryMode
	 *            传送模式 默认 PERSISTENT(DeliveryMode)
	 * @param priority
	 *            消息优先级 默认的优先级是4。
	 * @param timeToLive
	 *            消息的存活周期（以毫秒为单位） 默认的超时时间是没有限制的；消息永不过期。(0表示永不过期)
	 * @throws Exception
	 */
	public synchronized void sendMsg(final Destination destination,
			final Message message, final int deliveryMode, final int priority,
			final long timeToLive) throws Exception {
        //全局事务
		if(JtaContext.TRANSACTION_LOCAL.get() != null) {
			Session session = JtaContext.JMS_SESSION_LOCAL.get();
			if(session == null) {
				Connection con = JtaContext.JMS_CONNECTION_LOCAL.get();
				if(con == null) {
					con = getConnection();
					JtaContext.JMS_CONNECTION_LOCAL.set(con);
				}
				session = con.createSession(true, Session.AUTO_ACKNOWLEDGE);
				JtaContext.JMS_SESSION_LOCAL.set(session);
			}
			
			MessageProducer producer = null;
			try {
				producer = session.createProducer(destination);
				producer.send(destination, message, deliveryMode,priority, timeToLive);
			}finally {
				if(producer != null) {
					producer.close();
				}
			}
		} else {
			jmsTemplate.execute(new ProducerCallback<Object>() {
				public Object doInJms(Session session,
						MessageProducer messageProducer) throws JMSException {
					messageProducer.send(destination, message, deliveryMode,
							priority, timeToLive);
					messageProducer.close();
					session.close();
					return null;
				}
			});
		}
 
	}

	/**
	 * @param destination
	 *            消息目的地址
	 * @param objMsg
	 *            待发送的对象消息
	 * @param deliveryMode
	 *            传送模式 默认 PERSISTENT(DeliveryMode)
	 * @param priority
	 *            消息优先级 默认的优先级是4。
	 * @param timeToLive
	 *            消息的存活周期（以毫秒为单位） 默认的超时时间是没有限制的；消息永不过期。(0表示永不过期)
	 * @throws Exception
	 */
	@Override
	public synchronized void sendMsg(final Destination destination, final Serializable objMsg,
			final int deliveryMode, final int priority, final long timeToLive) throws Exception {
 
		        //全局事务
				if(JtaContext.TRANSACTION_LOCAL.get() != null) {
					Session session = JtaContext.JMS_SESSION_LOCAL.get();
					if(session == null) {
						Connection con = JtaContext.JMS_CONNECTION_LOCAL.get();
						if(con == null) {
							con = getConnection();
							JtaContext.JMS_CONNECTION_LOCAL.set(con);
						}
						session = con.createSession(true, Session.AUTO_ACKNOWLEDGE);
						JtaContext.JMS_SESSION_LOCAL.set(session);
					}
					
					MessageProducer producer = null;
					try {
						producer = session.createProducer(destination);
						ObjectMessage objectMessage = session.createObjectMessage(objMsg);
						objectMessage.setJMSMessageID(NetUtils.getLocalIp()+"_"+System.currentTimeMillis()+"_"+System.nanoTime());
 						producer.send(destination, objectMessage,deliveryMode, priority, timeToLive);
					}finally {
						if(producer != null) {
							producer.close();
						}
					}
				} else {
		 			jmsTemplate.execute(new ProducerCallback<Object>() {
						public Object doInJms(Session session,
								MessageProducer messageProducer) throws JMSException {
							ObjectMessage objectMessage = session.createObjectMessage(objMsg);
							objectMessage.setJMSMessageID(NetUtils.getLocalIp()+"_"+System.currentTimeMillis()+"_"+System.nanoTime());
							messageProducer.send(destination, objectMessage,deliveryMode, priority, timeToLive);
							return null;
						}
					});
				}

	}

	@Override
	public  synchronized void sendMsg(final Destination destination, final String strMsg,
			final int deliveryMode, final int priority, final long timeToLive) throws Exception {
	      //全局事务
			if(JtaContext.TRANSACTION_LOCAL.get() != null) {
				Session session = JtaContext.JMS_SESSION_LOCAL.get();
				if(session == null) {
					Connection con = JtaContext.JMS_CONNECTION_LOCAL.get();
					if(con == null) {
						con = getConnection();
						JtaContext.JMS_CONNECTION_LOCAL.set(con);
					}
					session = con.createSession(true, Session.AUTO_ACKNOWLEDGE);
					JtaContext.JMS_SESSION_LOCAL.set(session);
				}
				
				MessageProducer producer = null;
				try {
					producer = session.createProducer(destination);
 					producer.send(destination,session.createTextMessage(strMsg), deliveryMode,priority, timeToLive);
				}finally {
					if(producer != null) {
						producer.close();
					}
				}
			} else {
				jmsTemplate.execute(new ProducerCallback<Object>() {
					public Object doInJms(Session session,
							MessageProducer messageProducer) throws JMSException {
						messageProducer.send(destination,
								session.createTextMessage(strMsg), deliveryMode,
								priority, timeToLive);
						return null;
					}
				});
			}
			
		 
	}

	public  synchronized void sendMsg(final Destination destination, final byte[] byteMsg,
			final int deliveryMode, final int priority, final long timeToLive) throws Exception {
	 
		//全局事务
		if(JtaContext.TRANSACTION_LOCAL.get() != null) {
			Session session = JtaContext.JMS_SESSION_LOCAL.get();
			if(session == null) {
				Connection con = JtaContext.JMS_CONNECTION_LOCAL.get();
				if(con == null) {
					con = getConnection();
					JtaContext.JMS_CONNECTION_LOCAL.set(con);
				}
				session = con.createSession(true, Session.AUTO_ACKNOWLEDGE);
				JtaContext.JMS_SESSION_LOCAL.set(session);
			}
			
			MessageProducer producer = null;
			try {
				producer = session.createProducer(destination);
				BytesMessage bytesMessage = session.createBytesMessage();
				bytesMessage.writeBytes(byteMsg);
				producer.send(destination, bytesMessage,deliveryMode, priority, timeToLive);
			}finally {
				if(producer != null) {
					producer.close();
				}
			}
		} else {
			jmsTemplate.execute(new ProducerCallback<Object>() {
				public Object doInJms(Session session,
						MessageProducer messageProducer) throws JMSException {
					BytesMessage bytesMessage = session.createBytesMessage();
					bytesMessage.writeBytes(byteMsg);
					messageProducer.send(destination, bytesMessage,deliveryMode, priority, timeToLive);
					return null;
				}
			});
		}
			
	}

	public synchronized void sendMsg(final Destination destination,
			final Map<String, Object> mapMsg, final int deliveryMode,
			final int priority, final long timeToLive)
			throws Exception {
		//全局事务
		if(JtaContext.TRANSACTION_LOCAL.get() != null) {
			Session session = JtaContext.JMS_SESSION_LOCAL.get();
			if(session == null) {
				Connection con = JtaContext.JMS_CONNECTION_LOCAL.get();
				if(con == null) {
					con = getConnection();
					JtaContext.JMS_CONNECTION_LOCAL.set(con);
				}
				session = con.createSession(true, Session.AUTO_ACKNOWLEDGE);
				JtaContext.JMS_SESSION_LOCAL.set(session);
			}
			
			MessageProducer producer = null;
			try {
				producer = session.createProducer(destination);
 				MapMessage mapMessage = session.createMapMessage();
				if (mapMsg != null) {
					for (Iterator<Map.Entry<String, Object>> it = mapMsg
							.entrySet().iterator(); it.hasNext();) {
						Map.Entry<String, Object> entry = it.next();
						mapMessage.setObject(entry.getKey(),
								entry.getValue());
					}
				}
				producer.send(destination, mapMessage, deliveryMode,priority, timeToLive);
			}finally {
				if(producer != null) {
					producer.close();
				}
			}
		} else {
			jmsTemplate.execute(new ProducerCallback<Object>() {
				public Object doInJms(Session session,
						MessageProducer messageProducer) throws JMSException {
					MapMessage mapMessage = session.createMapMessage();
					if (mapMsg != null) {
						for (Iterator<Map.Entry<String, Object>> it = mapMsg
								.entrySet().iterator(); it.hasNext();) {
							Map.Entry<String, Object> entry = it.next();
							mapMessage.setObject(entry.getKey(),
									entry.getValue());
						}
					}
					messageProducer.send(destination, mapMessage, deliveryMode,
							priority, timeToLive);
					return null;
				}
			});
		}

		 
	}

	public synchronized  void sendMsg(final Destination destination,
			final InputStream streamMsg, final int deliveryMode,
			final int priority, final long timeToLive)
			throws Exception {
		        //全局事务
				if(JtaContext.TRANSACTION_LOCAL.get() != null) {
					Session session = JtaContext.JMS_SESSION_LOCAL.get();
					if(session == null) {
						Connection con = JtaContext.JMS_CONNECTION_LOCAL.get();
						if(con == null) {
							con = getConnection();
							JtaContext.JMS_CONNECTION_LOCAL.set(con);
						}
						session = con.createSession(true, Session.AUTO_ACKNOWLEDGE);
						JtaContext.JMS_SESSION_LOCAL.set(session);
					}
					
					MessageProducer producer = null;
					try {
						producer = session.createProducer(destination);
						StreamMessage streamMessage = session.createStreamMessage();
						byte[] bytes = new byte[streamMsg.available()];
						streamMsg.read(bytes);
						streamMsg.close();
						streamMessage.writeBytes(bytes);
						producer.send(destination, streamMessage,deliveryMode, priority, timeToLive);
					}finally {
						if(producer != null) {
							producer.close();
						}
					}
				} else {
					jmsTemplate.execute(new ProducerCallback<Object>() {
						public Object doInJms(Session session,
								MessageProducer messageProducer) throws JMSException {
							StreamMessage streamMessage = session.createStreamMessage();
							try {
								byte[] bytes = new byte[streamMsg.available()];
								streamMsg.read(bytes);
								streamMsg.close();
								streamMessage.writeBytes(bytes);
								messageProducer.send(destination, streamMessage,
										deliveryMode, priority, timeToLive);
							} catch (IOException e) {
								log.error(e.getMessage(), e);
							}
							return null;
						}
					});
				}
			
	}
 
	@Override
	public void sendTextMsg(String destinationName, String msg,
			DestinationType destType, int deliveryMode, int priority,
			long timeToLive) throws Exception {
		ActiveMQTextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText(msg);
		Destination destination = null;
		if (destType == DestinationType.QUEUE) {
			destination = new ActiveMQQueue(destinationName);
		} else if (destType == DestinationType.TOPIC) {
			destination = new ActiveMQTopic(destinationName);
		}
	    sendMsg(destination,textMessage, deliveryMode, priority, timeToLive);
	}

	@Override
	public void sendObjectMsg(String destinationName, Serializable objMsg,
			DestinationType destType, int deliveryMode, int priority,
			long timeToLive) throws Exception {
		Destination destination = null;
		if (destType == DestinationType.QUEUE) {
			destination = new ActiveMQQueue(destinationName);
		} else if (destType == DestinationType.TOPIC) {
			destination = new ActiveMQTopic(destinationName);
		}
	    sendMsg(destination,objMsg, deliveryMode, priority, timeToLive);
	}

	@Override
	public void sendBytesMsg(String destinationName, byte[] byteMsg,
			DestinationType destType, int deliveryMode, int priority,
			long timeToLive) throws Exception {
		Destination destination = null;
		if (destType == DestinationType.QUEUE) {
			destination = new ActiveMQQueue(destinationName);
		} else if (destType == DestinationType.TOPIC) {
			destination = new ActiveMQTopic(destinationName);
		}
	    sendMsg(destination,byteMsg,deliveryMode, priority, timeToLive);
	}

	@Override
	public void sendMapMsg(String destinationName, Map<String, Object> mapMsg,
			DestinationType destType, int deliveryMode, int priority,
			long timeToLive) throws Exception {
		Destination destination = null;
		if (destType == DestinationType.QUEUE) {
			destination = new ActiveMQQueue(destinationName);
		} else if (destType == DestinationType.TOPIC) {
			destination = new ActiveMQTopic(destinationName);
		}
	    sendMsg(destination,mapMsg,deliveryMode, priority, timeToLive);
	}
	
	@Override
	public void unSubscriber(Session session, String subscriberName) {
		try {
			session.unsubscribe(subscriberName);
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void createSubscriber(Session session, Topic topic,
			String subscriberName) {
		try {
 			session.createConsumer(topic, subscriberName);
		} catch (JMSException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	@Override
	public JmsTemplate getCurJmsTemplate() {
		return this.jmsTemplate;
	}
	
	/**
	 * @description 创建新的连接
	 * @return
	 * @throws JMSException
	 */
	public Connection getConnection() throws JMSException {
		  ConnectionFactory cf = jmsTemplate.getConnectionFactory();
  	  	  return cf.createConnection();  
	}


}
