/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>StringUtils.java</p>
 *   
 */
package cn.bidlink.framework.redis.serializer;

import java.util.Collection;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
final class StringUtils {

	public static String join(Collection<String> keys, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (String key : keys) {
			sb.append(key);
			sb.append(delimiter);
		}
		return sb.toString();
	}

	public static boolean isJsonObject(String s) {
		if (s.startsWith("{") || s.startsWith("[") || s.equals("true") || s.equals("false") || s.equals("null")) {
			return true;
		}
		try {
			new Integer(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}