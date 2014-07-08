/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>LoggingEventBson.java</p>
 *   
 */
package cn.bidlink.framework.log4j.mongod;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.helpers.OnlyOnceErrorHandler;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.ErrorHandler;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
class MongodConnection {
	static transient ErrorHandler errorHandler = new OnlyOnceErrorHandler();
	private String hostnamePort;
	private String databaseName;
	private String collectionName;
	private String userName;
	private String password;
	private Mongo mongo;
	private DBCollection collection = null;

	public MongodConnection(final String hostnamePort, final String databaseName, final String collectionName, final String userName, final String password) {
		super();
		this.hostnamePort = hostnamePort;
		this.databaseName = databaseName;
		this.collectionName = collectionName;
		this.userName = userName;
		this.password = password;
		connect();
	}

	public DBCollection getCollection() {
		return collection;
	}

	private void setCollection(final DBCollection collection) {
		this.collection = collection;
	}

	private List<ServerAddress> getServerAddresses(final String hostnamePort) {
		List<ServerAddress> address = new ArrayList<ServerAddress>();
		String[] hostnamePorts = hostnamePort.split(",");
		for (String port : hostnamePorts) {
			if (port != null && !"".equals(port.trim())) {
				String[] ports = port.split(":");
				if (ports.length == 2) {
					try {
						address.add(new ServerAddress(ports[0].trim(), Integer.valueOf(ports[1].trim())));
					} catch (NumberFormatException nfe) {
						errorHandler.error("MongoDB manager can't parse a port property value into an integer", nfe, ErrorCode.ADDRESS_PARSE_FAILURE);
					} catch (UnknownHostException uhe) {
						errorHandler.error("MongoDB manager hostname property contains unknown host", uhe, ErrorCode.ADDRESS_PARSE_FAILURE);
					}
				}
			}
		}
		return address;
	}

	private Mongo getMongo(final List<ServerAddress> addresses) {
		if (addresses == null) {
			throw new RuntimeException("Addresses can't be empty");
		} else if (addresses.size() == 1) {
			return new Mongo(addresses.get(0));
		} else {
			return new Mongo(addresses);
		}
	}

	private void connect() {
		try {
			List<ServerAddress> addresses = getServerAddresses(hostnamePort);
			mongo = getMongo(addresses);
			if (databaseName != null && !"".equals(databaseName.trim())) {
				DB db = mongo.getDB(databaseName);
				if (!db.authenticate(userName, password.toCharArray())) {
					throw new RuntimeException("Unable to authenticate with MongoDB server.");
				}
				if (collectionName == null || "".equals(collectionName)) {
					throw new RuntimeException("Unable to collection with MongoDB server.");
				}
				setCollection(db.getCollection(collectionName));
			}
		} catch (Exception e) {
			errorHandler.error("Unexpected exception while initialising MongoDbAppender.", e, ErrorCode.GENERIC_FAILURE);
		}
	}

}
