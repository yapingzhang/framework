/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>LoggingEventBson.java</p>
 *   
 */
package cn.bidlink.framework.log4j.mongod;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.DBCollection;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
public class MongodFactory {
	static Map<String, DBCollection> collection = new HashMap<String, DBCollection>();

	/**
	 * 
	 * @Description: TODO add description
	 * @param hostnamePort
	 * @param databaseName
	 * @param collectionName
	 * @param userName
	 * @param password
	 * @return
	 */
	public static synchronized DBCollection getCollection(final String hostnamePort, final String databaseName, final String collectionName, final String userName, final String password) {
		if (hostnamePort == null || "".equals(hostnamePort)) {
			throw new NullPointerException("hostnamePort can't be null");
		}
		if (databaseName == null || "".equals(databaseName)) {
			throw new NullPointerException("databaseName can't be null");
		}
		if (collectionName == null || "".equals(collectionName)) {
			throw new NullPointerException("collectionName can't be null");
		}
		if (userName == null || password == null) {
			throw new NullPointerException("username can't be null");
		}
		String key = key(hostnamePort, databaseName, collectionName);
		DBCollection dbc = collection.get(key);
		if (dbc == null) {
			dbc = new MongodConnection(hostnamePort, databaseName, collectionName, userName, password).getCollection();
		}
		return dbc;
	}

	/**
	 * 
	 * @Description: TODO add description
	 * @param hostnamePort
	 * @param databaseName
	 * @param collectionName
	 * @return
	 */
	static String key(final String hostnamePort, final String databaseName, final String collectionName) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = (hostnamePort + databaseName + collectionName).getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}