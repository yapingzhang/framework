/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * TODO add description.
 * 
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-11-29 下午05:42:48
 */
public class BidRedisZsetTest extends BaseRedisTest {
	String key = "zset";

	@Test
	public void testDel() throws Exception {
		Long l = redis.del(key);
		logger.info(l);
	}

	@Test
	public void testZadd() throws Exception {
		Long l = redis.zadd(key, 0D, "bidlink");
		logger.info(l);
		Assert.assertNotSame(0L, l);
	}

	@Test
	public void testZadd2() throws Exception {
		Map<Double, Object> map = new HashMap<Double, Object>();
		map.put(0D, "ebnew");
		map.put(1D, "bidlink");
		Long l = redis.zadd(key, map);
		logger.info(l);
		Assert.assertNotSame(0L, l);
	}

	@Test
	public void testZrange() throws Exception {
		Set<Object> set = redis.zrange(key, 0, 3);
		logger.info(set);
		Assert.assertNotNull(set);
	}

	@Test
	public void testZrem() throws Exception {
		redis.zadd(key, 0D, "bidlink");
		Object obj = redis.zrem(key, "ebnew", "bidlink");
		logger.info(obj);
		Assert.assertNotNull(obj);
	}

	@Test
	public void testZincrby() throws Exception {
		redis.zadd(key, 0D, "bidlink");
		Object obj = redis.zincrby(key, 6D, "bidlink");
		logger.info(obj);
		Assert.assertNotNull(obj);
	}
}
