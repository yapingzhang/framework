/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>SerializerFactory.java</p>
 *   
 */
package cn.bidlink.framework.redis.serializer;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
public class SerializerFactory {
	/**
	 * 
	 * @Description: TODO add description
	 * @param obj
	 * @return
	 */
	public static byte[] serialize(Object obj) {
		if (obj == null)
			return null;
		RedisObject cachedObject = new RedisSerializingTranscoder()
				.serialize(obj);
		byte[] data = cachedObject.getData();
		int spyFlag = cachedObject.getFlags();
		byte[] ret = new byte[data.length + 1];
		System.arraycopy(data, 0, ret, 1, data.length);
		byte flag = (byte) (spyFlag >> 1);
		ret[0] = flag;
		return ret;
	}

	/**
	 * 
	 * @Description: TODO add description
	 * @param bArr
	 * @return
	 */
	public static Object deserialize(byte[] bArr) {
		if (bArr == null)
			return null;
		int flag = 0;
		flag = (bArr[0] & 0X000000FF) << 1;
		byte[] data = new byte[bArr.length - 1];
		System.arraycopy(bArr, 1, data, 0, data.length);
		RedisObject cachedData = new RedisObject(flag, data);
		Object o = new RedisSerializingTranscoder().deserialize(cachedData);
		return o;
	}
}
