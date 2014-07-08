package cn.bidlink.framework.core.exceptions;


public class NoAuthorityException extends SecurityExpception {
	private static final long serialVersionUID = 1L;

	public NoAuthorityException() {
		super();
	}

	public NoAuthorityException(String message) {
		super(message);
	}

	public NoAuthorityException(Throwable cause) {
		super(cause);
	}

	public NoAuthorityException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoAuthorityException(String message, Throwable cause, String code,
			Object[] values) {
		 super(message, cause, code, values);
	}
}
