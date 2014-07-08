package cn.bidlink.framework.core.support;

import org.springframework.context.ApplicationEvent;
 /**
 * @description: 
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-8-26		下午3:36:14
 *
 */
@SuppressWarnings("serial")
public class BidApplicationEvent extends ApplicationEvent {
	
	public BidApplicationEvent(Object source) {
		 super(source);         
	}
}

