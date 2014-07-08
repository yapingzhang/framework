package cn.bidlink.framework.core.loadbalance;

import java.io.UnsupportedEncodingException;

/**
 * @remark 第三方源码
 * @author zhongfeng (mailto:djangofan@163.com)
 * @quoter dejian.liu 
 * @quoteDate 2012-12-01
 * @param <E>
 */
public class SafeEncoder {
	public final static String DEFAULT_CHARSET = "UTF-8";

	public static byte[] encode(String str) {
		try {
			if (str == null) {
				str = "DEFAULT";
			}
			return str.getBytes(DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {

		}
		return encode(null);
	}
}
