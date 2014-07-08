package cn.bidlink.framework.core.exceptions;

public class BusinessException extends BaseException {

	private static final long serialVersionUID = 1L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BusinessException(String message, Throwable cause, String code,
			Object[] values) {
		 super(message, cause, code, values);
	}

}
