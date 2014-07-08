/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>Constant.java</p>
 *   
 */
package cn.bidlink.framework.log4j.utils;
/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
public class BsonColumn {
	// Main log event elements
	protected static final String KEY_TIMESTAMP = "timestamp";
	protected static final String KEY_LEVEL = "level";
	protected static final String KEY_THREAD = "thread";
	// 代码信息
	protected static final String KEY_CLASS_NAME = "className";
	protected static final String KEY_FILE_NAME = "fileName";
	protected static final String KEY_METHOD_NAME = "methodName";
	protected static final String KEY_LINE_NUMBER = "lineNumber";
	// 异常信息
	protected static final String KEY_MESSAGE = "message";
	protected static final String KEY_THROWABLES = "throwables";
	protected static final String KEY_STACK_TRACE = "stackTrace";
	// 服务其信息
	protected static final String KEY_SERVER = "serverHost";
	protected static final String KEY_SERVER_PROCESS = "process";
	protected static final String KEY_SERVER_HOST = "host";
	protected static final String KEY_SERVER_IP = "ip";
}
