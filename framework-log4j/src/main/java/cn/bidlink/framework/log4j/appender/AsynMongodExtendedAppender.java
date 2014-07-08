/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>AsynMongodExtendedAppender.java</p>
 *   
 */
package cn.bidlink.framework.log4j.appender;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
public class AsynMongodExtendedAppender extends SynMongodExtendedAppender {
	private static ExecutorService executorService;
	/**
	 * 后台写日志的线程个数
	 */
	private int nThreads = 2;

	public void activateOptions() {
		super.activateOptions();
		if(executorService==null){
			executorService = Executors.newFixedThreadPool(nThreads);
		}
	}

	@Override
	protected void append(final LoggingEvent event) {
		event.getLocationInformation();
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					appendSuper(event);
				} catch (Exception e) {
					errorHandler.error("Failed to insert document to MongoDB", e, ErrorCode.WRITE_FAILURE);
				}
			}
		});
	}

	private void appendSuper(final LoggingEvent event) {
		super.append(event);
	}

	public void setnThreads(int nThreads) {
		this.nThreads = nThreads;
	}

}
