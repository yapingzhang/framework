package cn.bidlink.framework.core.exceptions;

public class InvalidBusinessArgumentException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public InvalidBusinessArgumentException() {
		super();
	}

	public InvalidBusinessArgumentException(String message) {
		super(message);
	}

	public InvalidBusinessArgumentException(Throwable cause) {
		super(cause);
	}

	public InvalidBusinessArgumentException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidBusinessArgumentException(String message, Throwable cause, String code,
			Object[] values) {
		 super(message, cause, code, values);
	}
}
