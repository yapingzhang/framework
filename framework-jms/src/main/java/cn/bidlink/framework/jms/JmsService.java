package cn.bidlink.framework.jms;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Topic;

import org.springframework.jms.core.JmsTemplate;

import cn.bidlink.framework.jms.enums.DestinationType;



/**
 * @description:	JMS 消息操作类
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2010-06-12 上午10:09:51
 */
public interface JmsService {
 

	/**
	 * 
	 * @param message 待发送的消息(消息destination 从jmsTemplate 取一个默认的消息目的地)
	 * @throws Exception
	 */
	 public void sendMsg(Message message) throws Exception;
	 
	 /**
	  * 
	  * @param destination 消息待传送到的目的地
	  * @param message 待发送的消息
	  * @throws Exception
	  */
	 public void sendMsg(Destination destination,Message message)throws Exception;
	 
	 /**
	  * @param message  待发送的消息
	  * @param deliveryMode 传送模式  默认 PERSISTENT(DeliveryMode)
	  * @param priority  消息优先级     默认的优先级是4。
	  * @param timeToLive 消息的存活周期（以毫秒为单位）  默认的超时时间是没有限制的；消息永不过期。(0表示永不过期)
	  * @throws Exception
	  */
	 public void sendMsg(Message message,int deliveryMode,int priority,long timeToLive)throws Exception;
	

	 
	 /**
	  * @param destination 消息目的地址
	  * @param message  待发送的消息
	  * @param deliveryMode 传送模式  默认 PERSISTENT(DeliveryMode)
	  * @param priority  消息优先级     默认的优先级是4。
	  * @param timeToLive 消息的存活周期（以毫秒为单位）  默认的超时时间是没有限制的；消息永不过期。(0表示永不过期)
	  * @throws Exception
	  */
	 public void sendMsg(Destination destination,Message message,int deliveryMode,int priority,long timeToLive)throws Exception;
 
	 /**
	  * @param destination 消息目的地址
	  * @param strMsg  待发送的文本消息
	  * @param deliveryMode 传送模式  默认 PERSISTENT(DeliveryMode)
	  * @param priority  消息优先级     默认的优先级是4。
	  * @param timeToLive 消息的存活周期（以毫秒为单位）  默认的超时时间是没有限制的；消息永不过期。(0表示永不过期)
	  * @throws Exception
	  */
	 public void sendMsg(Destination destination,String strMsg,int deliveryMode,int priority,long timeToLive)throws Exception;
	 
	 /**
	  * @description  
	  * @param destinationName 消息目的地名称
	  * @param msg 待发送消息
	  * @param destType 消息类别
	  * @param deliveryMode 传送模式  默认 PERSISTENT(DeliveryMode)
	  * @param priority  消息优先级     默认的优先级是4。
	  * @param timeToLive 消息的存活周期（以毫秒为单位）  默认的超时时间是没有限制的；消息永不过期。(0表示永不过期)
	  * @throws Exception
	  */
	 public void sendTextMsg(String destinationName,String msg,DestinationType destType,int deliveryMode,int priority,long timeToLive) throws Exception;
	 
	 
	 /**
	  * @param destination 消息目的地址
	  * @param objMsg  待发送的对象消息
	  * @param deliveryMode 传送模式  默认 PERSISTENT(DeliveryMode)
	  * @param priority  消息优先级     默认的优先级是4。
	  * @param timeToLive 消息的存活周期（以毫秒为单位）  默认的超时时间是没有限制的；消息永不过期。(0表示永不过期)
	  * @throws Exception
	  */
	 public void sendMsg(Destination destination,Serializable objMsg,int deliveryMode,int priority,long timeToLive)throws Exception;
	 

	 /**
	  * @param destination 消息目的地址名称
	  * @param objMsg  待发送的对象消息
	  * @param destType 消息类别
	  * @param deliveryMode 传送模式  默认 PERSISTENT(DeliveryMode)
	  * @param priority  消息优先级     默认的优先级是4。
	  * @param timeToLive 消息的存活周期（以毫秒为单位）  默认的超时时间是没有限制的；消息永不过期。(0表示永不过期)
	  * @throws Exception
	  */
	 public void sendObjectMsg(String destinationName,Serializable objMsg,DestinationType destType,int deliveryMode,int priority,long timeToLive)throws Exception;
	 
	 
	 /**
	  * @param destination 消息目的地址
	  * @param byteMsg  待发送的字节消息
	  * @param deliveryMode 传送模式  默认 PERSISTENT(DeliveryMode)
	  * @param priority  消息优先级     默认的优先级是4。
	  * @param timeToLive 消息的存活周期（以毫秒为单位）  默认的超时时间是没有限制的；消息永不过期。(0表示永不过期)
	  * @throws Exception
	  */
	 public void sendMsg(Destination destination,byte [] byteMsg,int deliveryMode,int priority,long timeToLive)throws Exception;
	 
	 /**
	  * 
	  * @description  
	  * @param destinationName 消息目的名称
	  * @param byteMsg  待发送的字节消息
	  * @param destType 消息类别
	  * @param deliveryMode  传送模式  默认 PERSISTENT(DeliveryMode)
	  * @param priority   消息优先级     默认的优先级是4。
	  * @param timeToLive 消息的存活周期（以毫秒为单位）  默认的超时时间是没有限制的；消息永不过期。(0表示永不过期)
	  * @throws Exception
	  */
	 public void sendBytesMsg(String destinationName,byte [] byteMsg,DestinationType destType,int deliveryMode,int priority,long timeToLive)throws Exception;
	 
	 
	 /**
	  * @param destination 消息目的地址
	  * @param mapMsg  待发送的Map消息
	  * @param deliveryMode 传送模式  默认 PERSISTENT(DeliveryMode)
	  * @param priority  消息优先级     默认的优先级是4。
	  * @param timeToLive 消息的存活周期（以毫秒为单位）  默认的超时时间是没有限制的；消息永不过期。(0表示永不过期)
	  * @throws Exception
	  */
	 public void sendMsg(Destination destination,Map<String, Object> mapMsg, int deliveryMode,int priority,long timeToLive)throws Exception;
	 
	 /**
	  * @param destination 消息目的地址
	  * @param mapMsg  待发送的Map消息
	  * @param deliveryMode 传送模式  默认 PERSISTENT(DeliveryMode)
	  * @param priority  消息优先级     默认的优先级是4。
	  * @param timeToLive 消息的存活周期（以毫秒为单位）  默认的超时时间是没有限制的；消息永不过期。(0表示永不过期)
	  * @throws Exception
	  */
	 public void sendMapMsg(String destinationName,Map<String, Object> mapMsg, DestinationType destType,int deliveryMode,int priority,long timeToLive)throws Exception;
	 
	 
	 /**
	  * @param destination 消息目的地址
	  * @param streamMsg  待发送的流消息
	  * @param deliveryMode 传送模式  默认 PERSISTENT(DeliveryMode)
	  * @param priority  消息优先级     默认的优先级是4。
	  * @param timeToLive 消息的存活周期（以毫秒为单位）  默认的超时时间是没有限制的；消息永不过期。(0表示永不过期)
	  * @throws Exception
	  */
	 public void sendMsg(Destination destination,InputStream streamMsg,int deliveryMode,int priority,long timeToLive)throws Exception;
	 
	 
	 /**
	  * 
	  * @description 取消持久订阅者
	  * @param session JMS会话
	  * @param subscriberName 持久订阅者名称
	  */
	 public void unSubscriber(Session session,String subscriberName);
	 
	 /**
	  * 
	  * @description 创建持久订阅者
	  * @param session JMS会话
	  * @param topic 消息目的地
	  * @param subscriberName 持久订阅者名称
	  */
	 public void createSubscriber(Session session,Topic topic,String subscriberName);
	 
	 /**
	  * 
	  * @description  返回JMS template
	  * @return
	  */
	 public  JmsTemplate getCurJmsTemplate();
	 
 
}
