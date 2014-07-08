/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有 
 */
package cn.bidlink.framework.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 最常用的使用方式,被注解的方法在被调用时,首先尝试从缓存中加载数据:<br>
 * 如果缓存命中,则直接返回.<br>
 * 缓存中没有,则会调用方法本身,并在调用成功后将结果写入缓存.
 * 
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Cache {
	/**
	 * spring el表达式格式的字符串,通过 ${p0} ${p1} 来获取方法的参数,0,1代表的是在方法参数.
	 */
	String key();

	/** 已过期，无需使用 */
	@Deprecated
	String[] els() default {};

	/** key 的类型.ture:简单字符串;false:执行字符串替换. */
	boolean simple() default false;

	/** 缓存过期时间，秒为单位 */
	int expire() default 0;

	/** 对象为null时,是否缓存.ture:缓存;false:不缓存. */
	boolean empty() default false;
}
