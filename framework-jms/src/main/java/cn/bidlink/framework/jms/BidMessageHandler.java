package cn.bidlink.framework.jms;

import javax.jms.Message;

import org.apache.log4j.Logger;
 /**
 * @description:	 TODO add description
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-9-12		上午10:50:32
 *
 */
public interface BidMessageHandler   {
	Logger logger = Logger.getLogger(BidMessageHandler.class);
	
	/**
	 * 消息处理方法
	 * @param message
	 */
	public void handlerMessage(Message message);
	
	/**
	 * @description 设置接口消息时间间隔(在没有缓存MQ连接,session等情况下，为减轻服务器压力可以实现此方法 ,单位(ms))
	 * @return
	 */
	public int getTimeInterval();

}

