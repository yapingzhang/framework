package cn.bidlink.framework.core.exceptions;

public class BidRmiException extends BaseException {

	private static final long serialVersionUID = 1L;

	public BidRmiException() {
		super();
	}

	public BidRmiException(String message) {
		super(message);
	}

	public BidRmiException(Throwable cause) {
		super(cause);
	}

	public BidRmiException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BidRmiException(String message, Throwable cause, String code,
			Object[] values) {
		 super(message, cause, code, values);
	}

}
