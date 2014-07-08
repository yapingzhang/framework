/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>RedisObject.java</p>
 *   
 */
package cn.bidlink.framework.redis.serializer;

import java.util.Arrays;

import cn.bidlink.framework.redis.exception.RedisException;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
public class RedisObject {
	public final int MAX_SIZE = 20 * 1024 * 1024;

	private final int flags;
	private final byte[] data;

	public byte[] getData() {
		return data;
	}

	public int getFlags() {
		return flags;
	}

	public RedisObject(final int flags, final byte[] data) {
		if (data.length > MAX_SIZE) {
			throw new RedisException("Stored data amount is too large [" + data.length + "]");
		}
		this.flags = flags;
		this.data = data;
	}

	@Override
	public String toString() {
		return "RedisObject [MAX_SIZE=" + MAX_SIZE + ", flags=" + flags + ", data=" + Arrays.toString(data) + "]";
	}

}
