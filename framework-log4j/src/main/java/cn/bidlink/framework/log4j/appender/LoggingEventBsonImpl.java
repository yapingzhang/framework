/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>LoggingEventBsonImpl.java</p>
 *   
 */
package cn.bidlink.framework.log4j.appender;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

import cn.bidlink.framework.log4j.utils.BsonColumn;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
final class LoggingEventBsonImpl extends BsonColumn implements LoggingEventBson {

	private DBObject serverInfo = new BasicDBObject();

	public LoggingEventBsonImpl() {
		setupNetworkInfo();
	}

	/**
	 * 
	 * @Description: TODO add description
	 */
	private void setupNetworkInfo() {
		serverInfo.put(KEY_SERVER_PROCESS, ManagementFactory.getRuntimeMXBean()
				.getName());
		try {
			serverInfo.put(KEY_SERVER_HOST, InetAddress.getLocalHost()
					.getHostName());
			serverInfo.put(KEY_SERVER_IP, InetAddress.getLocalHost()
					.getHostAddress());
		} catch (UnknownHostException e) {
			LogLog.warn(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public DBObject bson(final LoggingEvent event) {
		DBObject result = null;

		if (event != null) {
			result = new BasicDBObject();
			addMDCInformation(result, event.getProperties());
			addServerInformation(result);
			nullSafePut(result, KEY_TIMESTAMP, new Date(event.getTimeStamp()));
			nullSafePut(result, KEY_LEVEL, event.getLevel().toString());
			nullSafePut(result, KEY_THREAD, event.getThreadName());
			nullSafePut(result, KEY_MESSAGE, event.getMessage());
			addLocationInformation(result, event.getLocationInformation());
			addThrowableInformation(result, event.getThrowableStrRep());
		}
		return result;
	}

	/**
	 * 
	 * @Description: TODO add description
	 * @param bson
	 * @param locationInfo
	 */
	protected void addLocationInformation(final DBObject bson,
			final LocationInfo locationInfo) {
		if (locationInfo != null) {
			nullSafePut(bson, KEY_CLASS_NAME, locationInfo.getClassName());
			nullSafePut(bson, KEY_FILE_NAME, locationInfo.getFileName());
			nullSafePut(bson, KEY_METHOD_NAME, locationInfo.getMethodName());
			nullSafePut(bson, KEY_LINE_NUMBER, locationInfo.getLineNumber());
		}
	}

	/**
	 * 
	 * @Description: TODO add description
	 * @param bson
	 */
	protected void addServerInformation(final DBObject bson) {
		nullSafePut(bson, KEY_SERVER, serverInfo);

	}

	/**
	 * 
	 * @Description: TODO add description
	 * @param bson
	 * @param properties
	 */
	protected void addMDCInformation(final DBObject bson,
			final Map<Object, Object> properties) {
		if (properties != null && properties.size() > 0) {
			for (Map.Entry<Object, Object> entry : properties.entrySet()) {
				nullSafePut(bson, entry.getKey().toString(), entry.getValue());
			}
		}
	}

	/**
	 * 
	 * @description: TODO add description.
	 * @param bson
	 * @param reps
	 */
	protected void addThrowableInformation(final DBObject bson,
			final String[] reps) {
		if (reps == null)
			return;
		StringBuffer sb = new StringBuffer();
		for (String rep : reps) {
			sb.append(rep);
		}
		nullSafePut(bson, KEY_THROWABLES, sb);

	}

	/**
	 * 
	 * @description: TODO add description
	 * @param bson
	 * @param key
	 * @param value
	 */
	protected void nullSafePut(final DBObject bson, final String key,
			final Object value) {
		if (value != null) {
			if (value instanceof String) {
				String stringValue = (String) value;
				if (stringValue.trim().length() > 0) {
					bson.put(key, stringValue);
				}
			} else if (value instanceof StringBuffer) {
				String stringValue = ((StringBuffer) value).toString();
				if (stringValue.trim().length() > 0) {
					bson.put(key, stringValue);
				}
			} else {
				bson.put(key, value);
			}
		}
	}
}
