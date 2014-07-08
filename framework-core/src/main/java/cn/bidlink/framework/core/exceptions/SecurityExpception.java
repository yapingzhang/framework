package cn.bidlink.framework.core.exceptions;

public class SecurityExpception extends BaseException {
	private static final long serialVersionUID = 1L;

	public SecurityExpception() {
		super();
	}

	public SecurityExpception(String message) {
		super(message);
	}

	public SecurityExpception(Throwable cause) {
		super(cause);
	}

	public SecurityExpception(String message, Throwable cause) {
		super(message, cause);
	}
	public SecurityExpception(String message, Throwable cause, String code,
			Object[] values) {
		 super(message, cause, code, values);
	}
}
