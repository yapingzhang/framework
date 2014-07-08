/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.redis;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.bidlink.framework.redis.pojo.AuthUser;

/**
 * BidRedis 测试基类.
 * 
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-11-29 下午05:42:48
 */
public class BaseRedisTest {
	protected transient Logger logger = Logger.getLogger(BaseRedisTest.class);
	protected BidRedis redis;
	protected AuthUser authUser;

	@Before
	public void onSet() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-redis-test.xml");
		redis = (BidRedis) context.getBean("bidRedis");
		authUser = new AuthUser(1245678L);
	}
}
