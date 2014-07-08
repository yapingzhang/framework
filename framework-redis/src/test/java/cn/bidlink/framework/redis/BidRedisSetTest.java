/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.redis;

import org.junit.Assert;
import org.junit.Test;

/**
 * TODO add description.
 * 
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-11-29 下午05:42:48
 */
public class BidRedisSetTest extends BaseRedisTest {

	String key = "set";

	@Test
	public void testDel() throws Exception {
		Long l = redis.del(key);
		logger.info(l);
	}

	@Test
	public void testSadd() throws Exception {
		Long l = redis.sadd(key, "ebnew", "bidlink");
		logger.info(l);
		Assert.assertNotSame(0L, l);
	}

	@Test
	public void testSmembers() throws Exception {
		redis.sadd(key, "ebnew", "bidlink");
		Object obj = redis.smembers(key);
		logger.info(obj);
		Assert.assertNotNull(obj);
	}

	@Test
	public void testSrem() throws Exception {
		redis.sadd(key, "ebnew", "bidlink");
		Object obj = redis.srem(key, "ebnew", "bidlink");
		logger.info(obj);
		Assert.assertNotNull(obj);
	}

	@Test
	public void testSpop() throws Exception {
		redis.sadd(key, "ebnew", "bidlink");
		Object obj = redis.spop(key);
		logger.info(obj);
		Assert.assertNotNull(obj);
	}

	@Test
	public void testScard() throws Exception {
		redis.sadd(key, "ebnew", "bidlink");
		Object obj = redis.scard(key);
		logger.info(obj);
		Assert.assertNotNull(obj);
	}

	@Test
	public void testSismember() throws Exception {
		redis.sadd(key, "ebnew", "bidlink");
		Object obj = redis.sismember(key, "ebnew");
		logger.info(obj);
		Assert.assertNotNull(obj);
	}

	@Test
	public void testSrandmember() throws Exception {
		redis.sadd(key, "ebnew", "bidlink");
		Object obj = redis.srandmember(key);
		logger.info(obj);
		Assert.assertNotNull(obj);
	}
}
