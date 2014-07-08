package cn.bidlink.framework.jms.listener;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import cn.bidlink.framework.jms.BidMessageHandler;

/**
 * @description:  消息处理默认实现
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-9-12 上午10:54:08
 * 
 */
public   class DefaultBidMessageHandler implements BidMessageHandler {
 

	@Override
	public synchronized void handlerMessage(Message message) {
		try {
	
			logger.info("receiveMsg:depiveryMode"+message.getJMSDeliveryMode()+"\r\nmessageId:"+message.getJMSMessageID()+"\r\ntimestamp"+message.getJMSTimestamp()+"\r\n=================");
			if (message instanceof TextMessage) {
				handlerTextMessage((TextMessage) message);
			} else if (message instanceof ObjectMessage) {
				handlerObjectMessage((ObjectMessage) message);
			} else if (message instanceof MapMessage) {
				handlerMapMessage((MapMessage) message);
			} else if (message instanceof StreamMessage) {
				handlerStreamMessage((StreamMessage) message);
			} else if (message instanceof BytesMessage) {
				handlerBytesMessage((BytesMessage) message);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	
	protected void handlerTextMessage(TextMessage textMessage) throws JMSException {
 		logger.info(textMessage);
	}

	protected void handlerObjectMessage(ObjectMessage objectMessage) throws JMSException {
		logger.info(objectMessage);
	}

	protected void handlerMapMessage(MapMessage mapMessage) throws JMSException {
		logger.info(mapMessage);
	}

	protected void handlerStreamMessage(StreamMessage streamMessage) throws JMSException {
		logger.info(streamMessage);
	}

	protected void handlerBytesMessage(BytesMessage bytesMessage) throws JMSException {
		logger.info(bytesMessage);
	}


	@Override
	public int getTimeInterval() {
		return 0;
	}

}
