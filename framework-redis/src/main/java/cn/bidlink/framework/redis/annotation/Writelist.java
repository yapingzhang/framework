/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有 
 */
package cn.bidlink.framework.redis.annotation;

/**
 * 写入列表类型的缓存.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-11-28 下午05:58:32
 */
public @interface Writelist {
	Cache cache();

	int index() default 0;

	boolean parameter() default false;

	boolean rpush() default false;

	int length() default 1000;

}
