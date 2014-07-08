package cn.bidlink.framework.core.support.jta;

import java.lang.reflect.Method;


/**
 * @description: JTA事务model
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2013-9-13 上午11:21:28
 */
public class JtaModel {
	
	public JtaModel() {
	}
	
	public JtaModel(long startTime) {
		super();
		this.startTime = startTime;
	}

	public JtaModel(long startTime, Method method) {
		super();
		this.startTime = startTime;
		this.method = method;
	}

	private long startTime;

	private Method method;
 
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

    
 
}
