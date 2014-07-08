package cn.bidlink.framework.core.utils;

public abstract class ExceptionUtils {
	
	public static String getExceptionStack(Exception exception) {
		
		if(exception == null) {
			return null;
		}
		StringBuffer msg = new StringBuffer();
		StackTraceElement[] trace = exception.getStackTrace();
		for (int i=0; i < trace.length; i++) {
			msg.append("\tat " + trace[i]);
		}
		return msg.toString();
	}
}
