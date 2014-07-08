/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>MongodExtendedAppender.java</p>
 *   
 */
package cn.bidlink.framework.log4j.appender;

import org.apache.log4j.spi.LoggingEvent;

import cn.bidlink.framework.log4j.monitor.JvmMonitor;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
public class SynMongodExtendedAppender extends MongodExtendedAppender {
	private String jvmMonitor = "false";
	private int periodSeconds = 60;

	public void setJvmMonitor(final String jvmMonitor) {
		this.jvmMonitor = jvmMonitor;
	}

	public void setPeriodSeconds(final int periodSeconds) {
		this.periodSeconds = periodSeconds;
	}

	public void activateOptions() {
		super.activateOptions();
		initJvmMonitor();
	}

	private void initJvmMonitor() {

		if (!jvmMonitor.equals("true")) {
			return;
		}
		JvmMonitor.getInstance(periodSeconds);
	}

	@Override
	protected void append(final LoggingEvent event) {
		super.append(event);
	}

}
