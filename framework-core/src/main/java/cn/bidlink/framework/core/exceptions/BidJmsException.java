package cn.bidlink.framework.core.exceptions;

public class BidJmsException extends BaseException {

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
