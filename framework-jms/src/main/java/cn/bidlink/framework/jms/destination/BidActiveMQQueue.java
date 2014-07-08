package cn.bidlink.framework.jms.destination;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang.StringUtils;

import cn.bidlink.framework.jms.exceptions.BidJmsException;
 /**
 * @description:	消息队列
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-9-13		下午1:09:00
 *
 */
public class BidActiveMQQueue extends ActiveMQDestination implements Queue {
	
	public static final byte DATA_STRUCTURE_TYPE = 100;
	
	private String desitions;
	
	private boolean isReceiveDlq = true;
	
	private List<ActiveMQQueue> queueLists = new ArrayList<ActiveMQQueue>();
 
	@Override
	public byte getDataStructureType() {
		return DATA_STRUCTURE_TYPE;
	}

	@Override
	public String getQueueName() throws JMSException {
		return  getPhysicalName();
	}

 
	@Override
	public byte getDestinationType() {
		 return 1;
	}

	@Override
	protected String getQualifiedPrefix() {
		 return "queue://";
	}

	public String getDesitions() {
		return desitions;
	}

	public void setDesitions(String desitions) {
		if (StringUtils.isEmpty(desitions)) {
			throw new BidJmsException("desition can't null!");
		}
		this.desitions = desitions;
	
		if (isReceiveDlq()) {
			queueLists.add(new ActiveMQQueue("ActiveMQ.DLQ"));
		}
		if (desitions.indexOf(",") !=-1) {
			String [] ary = desitions.split(",");
			for (String queueName : ary) {
				if (StringUtils.isNotEmpty(queueName.trim())) {
					queueLists.add(new ActiveMQQueue(queueName));
				}       
			}
		} else {
			queueLists.add(new ActiveMQQueue(desitions));
		}
		this.setCompositeDestinations(queueLists.toArray(new ActiveMQQueue[queueLists.size()]));
	}

	public boolean isReceiveDlq() {
		return isReceiveDlq;
	}

	public void setReceiveDlq(boolean isReceiveDlq) {
		this.isReceiveDlq = isReceiveDlq;
	}

	public List<ActiveMQQueue> getQueueLists() {
		return queueLists;
	}
	
	
}

