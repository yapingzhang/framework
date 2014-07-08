/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>LoggingEventBson.java</p>
 *   
 */
package cn.bidlink.framework.log4j.appender;

import org.apache.log4j.spi.LoggingEvent;

import com.mongodb.DBObject;
/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
interface LoggingEventBson {
	/**
	 * 
	 * @Description: TODO add description
	 * @param loggingEvent
	 * @return
	 */
	DBObject bson(LoggingEvent event);
}
