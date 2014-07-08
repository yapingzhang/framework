package cn.bidlink.framework.core.support.jmx;

import org.springframework.stereotype.Component;

/**
 * 
 * @description:系统内存状态
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2013-1-7 下午4:31:42
 */
@Component
public class SystemMBeanStatus {

	private long getMax() {
		return Runtime.getRuntime().maxMemory() >> 20;
	}

	private long getTotal() {
		return Runtime.getRuntime().totalMemory() >> 20;
	}

	private long getFree() {
		return Runtime.getRuntime().freeMemory() >> 20;
	}

	private long getUsed() {
		return getTotal() - getFree();
	}


    public void gc() {
    	 Runtime.getRuntime().gc();
    }
	public String getMaxMemory() {
		return getMax() + " M";
	}

	public String getTotalMemory() {
		return getTotal() + " M";
	}

	public String getUsedMemory() {
		return getUsed() + " M";
	}

	public void shutdown() {
		Runtime.getRuntime().exit(0);
	}

}
