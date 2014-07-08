package cn.bidlink.framework.core.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2013-4-8 下午4:34:04
 */
public final class Pid {

	private Pid() {
		super();
	}

	static int pid = -1;

	public static int getJvmPid() {
		if (pid < 0)
			try {
				RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
				String name = runtime.getName();
 				pid = Integer.parseInt(name.substring(0, name.indexOf(64)));
			} catch (Throwable e) {
				pid = 0;
			}
		return pid;
	}

	public static void main(String[] args) {
		System.out.println(getJvmPid());
	}
}
