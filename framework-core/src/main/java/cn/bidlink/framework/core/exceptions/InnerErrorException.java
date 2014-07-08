package cn.bidlink.framework.core.exceptions;

public class InnerErrorException extends BaseException {

	private static final long serialVersionUID = 1L;

	public InnerErrorException() {
		super();
	}

	public InnerErrorException(String message) {
		super(message);
	}

	public InnerErrorException(Throwable cause) {
		super(cause);
	}

	public InnerErrorException(String message, Throwable cause) {
		super(message, cause);
	}
	public InnerErrorException(String message, Throwable cause, String code,
			Object[] values) {
		 super(message, cause, code, values);
	}
	
}
