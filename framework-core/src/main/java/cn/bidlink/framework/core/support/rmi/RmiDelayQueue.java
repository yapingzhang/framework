package cn.bidlink.framework.core.support.rmi;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

 /**
 * @description:	 TODO add description
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-10-30		上午9:14:54
 *
 */
public class RmiDelayQueue implements Delayed{
	private long debut;
	private long delay;
	private Class<?> classType;
	private RmiStub curRmiStub;
	
	
	
	public RmiDelayQueue(long delay,Class<?> classType,RmiStub curRmiStub) {
		this.debut = System.currentTimeMillis();
		this.delay = delay;
		this.classType = classType;
		this.curRmiStub = curRmiStub;
	}

	@Override
	public int compareTo(Delayed o) {
		if (o instanceof RmiDelayQueue) {
			RmiDelayQueue timeout = (RmiDelayQueue) o;
			if (this.debut + this.delay == timeout.debut + timeout.delay) {
				return 0;
			} else if (this.debut + this.delay > timeout.debut + timeout.delay) {
				return 1;
			} else {
				return -1;
			}
		}
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return debut + delay - System.currentTimeMillis();
	}

	public Class<?> getClassType() {
		return classType;
	}

	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}

	public RmiStub getCurRmiStub() {
		return curRmiStub;
	}

	public void setCurRmiStub(RmiStub curRmiStub) {
		this.curRmiStub = curRmiStub;
	}

	
 
}

