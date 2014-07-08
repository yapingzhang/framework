/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有 
 */
package cn.bidlink.framework.redis.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 用来缓存列表类型的数据.<br>
 * 比如查询 start=10,limit=10的数据,<br>
 * 将首先尝试从缓存中加载,如果缓存中仅有18条数据,则首先从缓存中加载 start=10,limit=8的数据,<br>
 * 然后将修改传递给方法调用的参数为 start=18,limit =2,将剩余的数据补充上.
 * 
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Listof {
	Cache cache();

	int start() default 1;

	int limit() default 2;
}
