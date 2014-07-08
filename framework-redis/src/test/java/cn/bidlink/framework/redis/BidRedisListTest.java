/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.redis;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * TODO add description.
 * 
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-11-29 下午05:42:48
 */
public class BidRedisListTest extends BaseRedisTest {
	String key = "lsit";

	@Test
	public void testDel() throws Exception {
		Long l = redis.del(key);
		logger.info(l);
	}

	@Test
	public void testRpush() throws Exception {
		Long l = redis.rpush(key, "ebnew", "bidlink");
		logger.info(l);
		Assert.assertNotSame(0L, l);
	}

	@Test
	public void testLpush() throws Exception {
		Long l = redis.lpush(key, "ebnew", "bidlink");
		logger.info(l);
		Assert.assertNotSame(0L, l);
	}

	@Test
	public void testLlen() throws Exception {
		Long l = redis.llen(key);
		logger.info(l);
		Assert.assertNotSame(0L, l);
	}

	@Test
	public void testLrange() throws Exception {
		List<Object> list = redis.lrange(key, 0, 3);
		logger.info(list);
		Assert.assertNotNull(list);
	}

	@Test
	public void testLtrim() throws Exception {
		String str = redis.ltrim(key, 0, 2);
		logger.info(str);
		Assert.assertNotNull(str);
	}

	@Test
	public void testLindex() throws Exception {
		Object str = redis.lindex(key, 2);
		logger.info(str);
		Assert.assertNotNull(str);
	}

	@Test
	public void testLset() throws Exception {
		Object str = redis.lset(key, -1, "1test");
		logger.info(str);
		Assert.assertNotNull(str);
	}

	@Test
	public void testLrem() throws Exception {
		Object str = redis.lrem(key, -1, "1test");
		logger.info(str);
		Assert.assertNotNull(str);
	}

	@Test
	public void testLpop() throws Exception {
		Object str = redis.lpop(key);
		logger.info(str);
		Assert.assertNotNull(str);
	}

	@Test
	public void testRpop() throws Exception {
		Object str = redis.rpop(key);
		logger.info(str);
		Assert.assertNotNull(str);
	}

}
