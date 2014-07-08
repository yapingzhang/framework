package cn.bidlink.framework.core.exceptions;


public class NoSuchConfigItemException extends GeneralException {

	private static final long serialVersionUID = 2539484905331343607L;

	public NoSuchConfigItemException() {
		super();
	}

	public NoSuchConfigItemException(String message) {
		super(message);
	}

	public NoSuchConfigItemException(Throwable cause) {
		super(cause);
	}

	public NoSuchConfigItemException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoSuchConfigItemException(String message, Throwable cause, String code,
			Object[] values) {
		 super(message, cause, code, values);
	}
}
