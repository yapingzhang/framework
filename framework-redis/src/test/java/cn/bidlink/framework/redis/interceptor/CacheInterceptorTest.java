/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>Delete.java</p>
 *   
 */
package cn.bidlink.framework.redis.interceptor;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.bidlink.framework.redis.pojo.AuthUser;
import cn.bidlink.framework.redis.service.RedisService;

/**
 * 
 * @description: 测试拦截器.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
public class CacheInterceptorTest {

	RedisService redisService;
	private AuthUser authUser;

	@Before
	public void onSet() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-test.xml");
		redisService = (RedisService) context.getBean("redisService");
		authUser = new AuthUser(1245678L);
	}

	@Test
	public void testA() throws Exception {
		redisService.a();
	}

	@Test
	public void testB() throws Exception {
		redisService.b(authUser);
	}

	@Test
	public void testC() throws Exception {
		redisService.c(1, 2, 3);

	}

	@Test
	public void testD() throws Exception {
		redisService.d(authUser, 2);

	}

	@Test
	public void testE() throws Exception {
		redisService.e(authUser, 2);

	}

	@Test
	public void testF() throws Exception {
		redisService.f(authUser);

	}

	@Test
	public void testG() throws Exception {
		redisService.g(1, 2, 3);

	}

	@Test
	public void testH() throws Exception {
		redisService.h(1, 2, 3);

	}
}
