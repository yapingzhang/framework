/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>LoggingEventBson.java</p>
 *   
 */
package cn.bidlink.framework.log4j.appender;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

import cn.bidlink.framework.log4j.mongod.MongodFactory;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
class MongodExtendedAppender extends AppenderSkeleton {
	private LoggingEventBson eventBson = new LoggingEventBsonImpl();
	private String hostnamePort = "127.0.0.1:27017";
	private String databaseName = "logmongod";
	private String collectionName = "usetest";
	private String userName = null;
	private String password = null;
	private String appCode = null;
	private DBCollection collection;
	private boolean initialized = false;

	public void setEventBson(final LoggingEventBson eventBson) {
		this.eventBson = eventBson;
	}

	public void setHostnamePort(final String hostnamePort) {
		if (hostnamePort == null || hostnamePort.trim().length() == 0) {
			throw new NullPointerException("hostnamePort can't be null");
		}
		this.hostnamePort = hostnamePort;
	}

	public void setDatabaseName(final String databaseName) {
		if (databaseName == null || databaseName.trim().length() == 0) {
			throw new NullPointerException("databaseName can't be null");
		}
		this.databaseName = databaseName;
	}

	public void setCollectionName(final String collectionName) {
		if (collectionName == null || collectionName.trim().length() == 0) {
			throw new NullPointerException("collectionName can't be null");
		}
		this.collectionName = collectionName;
	}

	public void setUserName(final String userName) {
		if (userName == null || userName.trim().length() == 0) {
			throw new NullPointerException("userName can't be null");
		}
		this.userName = userName;
	}

	public void setPassword(final String password) {
		if (password == null || password.trim().length() == 0) {
			throw new NullPointerException("appCode can't be null");
		}
		this.password = password;
	}

	public void setAppCode(final String appCode) {
		if (appCode == null || appCode.trim().length() == 0) {
			throw new NullPointerException("appCode can't be null");
		}
		this.appCode = appCode;
	}

	public void setCollection(final DBCollection collection) {
		if (collection == null) {
			throw new NullPointerException("collection can't be null");
		}
		this.collection = collection;
	}

	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * 
	 * @Description: TODO add description
	 * @param bson
	 */
	protected void append(final DBObject bson) {
		if (initialized && bson != null) {
			try {
				if (appCode != null && appCode.trim().length() > 0) {
					bson.put("appCode", appCode);
				}
				collection.insert(bson, collection.getWriteConcern());
			} catch (MongoException e) {
				errorHandler.error("Failed to insert document to MongoDB", e, ErrorCode.WRITE_FAILURE);
			}
		}
	}

	@Override
	public void activateOptions() {
		try {
			setCollection(MongodFactory.getCollection(hostnamePort, databaseName, collectionName, userName, password));
			initialized = true;
		} catch (Exception e) {
			errorHandler.error("Unexpected exception while initialising MongoDbAppender.", e, ErrorCode.GENERIC_FAILURE);
		}
	}

	@Override
	protected void append(final LoggingEvent event) {
		DBObject bson = eventBson.bson(event);
		append(bson);
	}

	@Override
	public void close() {
		collection = null;
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

}
