/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>RedisServiceImpl.java</p>
 *   
 */
package cn.bidlink.framework.redis.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.bidlink.framework.redis.annotation.Cache;
import cn.bidlink.framework.redis.annotation.Delete;
import cn.bidlink.framework.redis.pojo.AuthUser;
import cn.bidlink.framework.redis.service.RedisService;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
@Service
public class RedisServiceImpl implements RedisService {
	transient Logger logger = Logger.getLogger(getClass());

	@Cache(key = "key_a")
	@Override
	public String a() {
		logger.info("I am doing a...");
		return "I am doing a...";
	}

	@Cache(key = "key_b_${p0.userId}", simple = false)
	@Override
	public AuthUser b(AuthUser authUser) {
		logger.info("I am doing b...");
		return authUser;
	}

	@Delete(cache = @Cache(key = "key_e_${p0}"))
	@Override
	public int c(int a, int b, int c) {
		logger.info("I am doing c...");
		return a;
	}

	/**
	 * 先执行delete 注解,在执行cache 注解.
	 */
	@Delete(cache = @Cache(key = "key_d_${p0.userId}_${p1}"))
	@Cache(key = "key_d_${p0.userId}_${p1}", simple = false)
	@Override
	public AuthUser d(AuthUser authUser, int a) {
		logger.info("I am doing d...");
		return authUser;
	}

	/**
	 * delete 注解不执行.
	 */
	@Cache(key = "key_e_${p0.userId}_${p1}", simple = false)
	@Delete(cache = @Cache(key = "key_e_${p0.userId}_${p1}"))
	@Override
	public AuthUser e(AuthUser authUser, int a) {
		logger.info("I am doing e...");
		return authUser;
	}

	@Cache(key = "key_f_%s", els = { "#p[0].userId" }, simple = false)
	@Override
	public AuthUser f(AuthUser pojo) {
		logger.info("I am doing f...");
		return pojo;
	}

	@Delete(cache = @Cache(key = "key_g_%s", els = { "#p[0]" }))
	@Override
	public int g(int a, int b, int c) {
		logger.info("I am doing g...");
		return a;
	}

	@Delete(cache = @Cache(key = "key_h_%s", els = { "#p[0]" }))
	@Override
	public int h(int a, int b, int c) {
		logger.info("I am doing h...");
		return a;
	}
}
