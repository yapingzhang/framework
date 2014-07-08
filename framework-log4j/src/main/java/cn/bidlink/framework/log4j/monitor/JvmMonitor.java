/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>JvmMonitor.java</p>
 *   
 */
package cn.bidlink.framework.log4j.monitor;

import java.lang.management.RuntimeMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午04:26:56
 */
@SuppressWarnings("restriction")
public final class JvmMonitor {
	private static JvmMonitor uniqueInstance = null;
	private static Logger logger = Logger.getLogger(JvmMonitor.class);
	private long lastProcessCpuTime = 0;
	private long lastUptime = 0;
	public static final int DEFAULT_REFRESH_SECONDS = 60;

	public static synchronized JvmMonitor getInstance(final int periodSeconds) {
		if (uniqueInstance == null) {
			uniqueInstance = new JvmMonitor(periodSeconds);
		}
		return uniqueInstance;
	}

	public static synchronized JvmMonitor getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new JvmMonitor();
		}
		return uniqueInstance;
	}

	private JvmMonitor() {
		this(DEFAULT_REFRESH_SECONDS);
	}

	private JvmMonitor(final int periodSeconds) {
		logger.info("jvm monitor start periodSeconds....");
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
		executorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				record();
			}
		}, periodSeconds, periodSeconds, TimeUnit.SECONDS);
	}

	public void record() {
		String message = "memoryUsed=" + getMemoryUsed() + "k " + " cpuUsed=" + getCpu() + " threadCount=" + getThreadCount();
		logger.info(message);
	}

	protected int getThreadCount() {
		return sun.management.ManagementFactory.getThreadMXBean().getThreadCount();
	}

	protected long getMemoryUsed() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024);
	}

	protected double getCpu() {
		com.sun.management.OperatingSystemMXBean osbean = (com.sun.management.OperatingSystemMXBean) sun.management.ManagementFactory.getOperatingSystemMXBean();
		RuntimeMXBean runbean = java.lang.management.ManagementFactory.getRuntimeMXBean();
		long uptime = runbean.getUptime();
		long processCpuTime = osbean.getProcessCpuTime();
		int processors = osbean.getAvailableProcessors();
		double cpu = (processCpuTime - lastProcessCpuTime) / ((uptime - lastUptime) * 10000f * processors);
		lastProcessCpuTime = processCpuTime;
		lastUptime = uptime;
		return (int) cpu;
	}
}