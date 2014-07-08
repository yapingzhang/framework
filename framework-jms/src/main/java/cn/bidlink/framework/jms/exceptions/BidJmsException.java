package cn.bidlink.framework.jms.exceptions;

import cn.bidlink.framework.core.exceptions.BaseException;

 /**
 * @description:	JMS 组件业务异常
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-9-12		上午10:42:08
 */
public class BidJmsException extends BaseException{
	private static final long serialVersionUID = 1L;

	public BidJmsException() {
		super();
	}

	public BidJmsException(String message) {
		super(message);
	}

	public BidJmsException(Throwable cause) {
		super(cause);
	}

	public BidJmsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BidJmsException(String message, Throwable cause, String code,
			Object[] values) {
		 super(message, cause, code, values);
	}

}

