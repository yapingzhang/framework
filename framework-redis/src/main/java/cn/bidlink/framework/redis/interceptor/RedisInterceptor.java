/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>RedisInterceptor.java</p>
 *   
 */
package cn.bidlink.framework.redis.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import cn.bidlink.framework.redis.BidRedis;
import cn.bidlink.framework.redis.annotation.Cache;
import cn.bidlink.framework.redis.annotation.Delete;
import cn.bidlink.framework.redis.annotation.Listof;
import cn.bidlink.framework.redis.annotation.Writelist;
import cn.bidlink.framework.redis.annotation.Writeto;
import cn.bidlink.framework.redis.utils.CacheUtils;
import cn.bidlink.framework.redis.utils.NullObject;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
@Aspect
public class RedisInterceptor {
	transient Logger logger = Logger.getLogger(getClass());
	@Autowired
	BidRedis bidRedis;

	public void setBidRedis(BidRedis bidRedis) {
		this.bidRedis = bidRedis;
	}

	@Pointcut("@annotation(cn.bidlink.framework.redis.annotation.Cache)")
	public void cachePoint() {
	}

	@Pointcut("@annotation(cn.bidlink.framework.redis.annotation.Delete)")
	public void deletePoint() {
	}

	@Pointcut("@annotation(cn.bidlink.framework.redis.annotation.Listof)")
	public void listofPoint() {
	}

	@Pointcut("@annotation(cn.bidlink.framework.redis.annotation.Writeto)")
	public void writetoPoint() {
	}

	@Pointcut("@annotation(cn.bidlink.framework.redis.annotation.Writelist)")
	public void writelistPoint() {
	}

	@Around("cachePoint()||deletePoint()||listofPoint()||writetoPoint()||writelistPoint()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		boolean pushed;
		pushed = !(firstInvoke.get().isEmpty() || firstInvoke.get().peek());
		firstInvoke.get().push(pushed);
		try {
			Method method = getMethod(pjp);
			Annotation[] annotations = method.getAnnotations();
			Object returned = null;
			boolean invoked = false;
			for (Annotation annotation : annotations) {
				if (annotation instanceof Cache) {
					returned = this.cache(pjp, (Cache) annotation);
					invoked = true;
					break;
				} else if (annotation instanceof Listof) {
					returned = this.listof(pjp, (Listof) annotation);
					invoked = true;
					break;
				} else if (annotation instanceof Writelist) {
					returned = this.writelist(pjp, (Writelist) annotation);
					invoked = true;
					break;
				} else if (annotation instanceof Writeto) {
					returned = this.writeto(pjp, (Writeto) annotation);
					invoked = true;
					break;
				} else if (annotation instanceof Delete) {
					this.delete(pjp, (Delete) annotation);
				}
			}
			if (!invoked) {
				returned = pjp.proceed();
			}
			return returned;
		} finally {
			firstInvoke.get().pop();
		}
	}

	/**
	 * 简单缓存.
	 * 
	 * @author wangtao 2013-12-2
	 * @param pjp
	 * @param cache
	 * @return
	 * @throws Throwable
	 */
	protected Object cache(ProceedingJoinPoint pjp, final Cache cache) throws Throwable {
		final String key = CacheUtils.parseKey(cache, pjp.getArgs());
		Object result = null;
		try {
			result = bidRedis.getObject(key);
		} catch (Exception e) {
			logger.error("cache", e);
		}
		if (result == null) {
			result = pjp.proceed();
			this.setObject(key, cache.expire(), result, cache.empty());
		}
		if (result instanceof NullObject) {
			result = null;
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object listof(ProceedingJoinPoint pjp, final Listof cache) throws Throwable {
		final String key = CacheUtils.parseKey(cache.cache(), pjp.getArgs());
		Integer start = (Integer) getParameterValue(cache.start(), pjp.getArgs());
		Integer limit = (Integer) getParameterValue(cache.limit(), pjp.getArgs());
		List<?> result = null;
		try {
			result = bidRedis.lrange(key, start, limit);
		} catch (Exception e) {
			logger.error("listof", e);
		}
		if (result == null) {
			return pjp.proceed();
		}
		if (result.size() == limit) {
			return result;
		}
		pjp.getArgs()[cache.start()] = (start + result.size());
		pjp.getArgs()[cache.limit()] = (limit - result.size());
		return result.addAll((List) pjp.proceed());
	}

	/**
	 * 
	 * @Description: TODO add description
	 * @param pjp
	 * @param cache
	 * @return
	 * @throws Throwable
	 */
	protected Object writeto(ProceedingJoinPoint pjp, final Writeto cache) throws Throwable {
		final String key = CacheUtils.parseKey(cache.cache(), pjp.getArgs());
		Object result = pjp.proceed();
		if (cache.parameter()) {
			Object parameter = getParameterValue(cache.index(), pjp.getArgs());
			setObject(key, cache.cache().expire(), parameter, cache.cache().empty());
		} else {
			setObject(key, cache.cache().expire(), result, cache.cache().empty());
		}
		return result;
	}

	private void setObject(String key, int expire, Object value, boolean empty) {
		try {
			if (value != null) {
				bidRedis.setObject(key, expire, value);
			}
			if (empty && value == null) {
				bidRedis.setObject(key, expire, new NullObject());
			}
		} catch (Exception e) {
			logger.error("setObject", e);
		}
	}

	/**
	 * 
	 * @Description: TODO add description
	 * @param pjp
	 * @param cache
	 * @return
	 * @throws Throwable
	 */
	protected Object writelist(ProceedingJoinPoint pjp, final Writelist cache) throws Throwable {
		final String key = CacheUtils.parseKey(cache.cache(), pjp.getArgs());
		Object result = pjp.proceed();
		try {
			if (cache.parameter()) {
				Object parameter = getParameterValue(cache.index(), pjp.getArgs());
				if (parameter != null) {
					this.writelist(key, parameter, cache.rpush(), cache.length());
				}
			} else {
				if (result != null) {
					this.writelist(key, result, cache.rpush(), cache.length());
				}
			}
		} catch (Exception e) {
			logger.error("writelist", e);
		}
		return result;
	}

	/**
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param value
	 * @param append
	 * @param max
	 */
	protected void writelist(String key, Object value, boolean append, int max) {
		long curNum = 0;
		if (value instanceof Iterable) {
			for (Object obj : (Iterable<?>) value) {
				if (append) {
					curNum = bidRedis.rpush(key, obj);
				} else {
					bidRedis.lpush(key, obj);
				}
			}
		} else {
			if (append) {
				curNum = bidRedis.rpush(key, value);
			} else {
				bidRedis.lpush(key, value);
			}
		}
		if (append) {
			if (curNum > max)
				bidRedis.ltrim(key, (int) (curNum - max), (int) curNum);
		} else {
			bidRedis.ltrim(key, 0, max);
		}
	}

	/**
	 * 删除缓存的key.
	 * 
	 * @author wangtao 2013-12-2
	 * @param pjp
	 * @param cache
	 * @return
	 * @throws Throwable
	 */
	protected void delete(ProceedingJoinPoint pjp, final Delete cache) throws Throwable {
		final String key = CacheUtils.parseKey(cache.cache(), pjp.getArgs());
		try {
			if (cache.markAsNull()) {
				this.setObject(key, cache.cache().expire(), null, true);
			} else {
				bidRedis.del(key);
			}
		} catch (Exception e) {
			logger.error("delete", e);
		}
	}

	private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
		Signature sig = jp.getSignature();
		MethodSignature msig = (MethodSignature) sig;
		return getClass(jp).getMethod(msig.getName(), msig.getParameterTypes());
	}

	private Class<? extends Object> getClass(JoinPoint jp) throws NoSuchMethodException {
		return jp.getTarget().getClass();
	}

	protected Object getParameterValue(int index, Object[] parameters) {
		return parameters[index];
	}

	private static final ThreadLocal<Stack<Boolean>> firstInvoke = new ThreadLocal<Stack<Boolean>>() {
		@Override
		protected Stack<Boolean> initialValue() {
			return new Stack<Boolean>();
		}
	};
}
