/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>RedisException.java</p>
 *   
 */
package cn.bidlink.framework.redis.exception;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
public class RedisException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RedisException() {
		super();
	}

	public RedisException(String message) {
		super(message);
	}

	public RedisException(Throwable cause) {
		super(cause);
	}

	public RedisException(String message, Throwable cause) {
		super(message, cause);
	}
}
