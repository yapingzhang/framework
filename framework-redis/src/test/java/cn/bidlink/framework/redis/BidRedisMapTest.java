/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.redis;

import java.util.List;
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
public class BidRedisMapTest extends BaseRedisTest {
	String key = "website";

	@Test
	public void testDel() throws Exception {
		Long l = redis.del(key);
		logger.info(l);
	}

	@Test
	public void testHset() throws Exception {
		redis.hset(key, "ebnew", "www.ebnew.com");
		Object obj = redis.hget(key, "ebnew");
		logger.info(obj);
		Assert.assertEquals("www.ebnew.com", obj);
	}

	@Test
	public void testHget() throws Exception {
		redis.hset(key, "ebnew", "www.ebnew.com");
		Object obj = redis.hget(key, "ebnew");
		logger.info(obj);
		Assert.assertEquals("www.ebnew.com", obj);
	}

	@Test
	public void testHgetAll() throws Exception {
		redis.hset(key, "ebnew", "www.ebnew.com");
		Map<String, Object> map = redis.hgetAll(key);
		logger.info(map);
		Assert.assertNotNull(map);
	}

	@Test
	public void testHmget() throws Exception {
		redis.hset(key, "ebnew", "www.ebnew.com");
		redis.hset(key, "bidlink", "www.bidlink.cn");
		List<Object> list = redis.hmget(key, "ebnew", "bidlink");
		logger.info(list);
		Assert.assertNotNull(list);
	}

	@Test
	public void testHexists() throws Exception {
		redis.hset(key, "ebnew", "www.ebnew.com");
		redis.hset(key, "bidlink", "www.bidlink.cn");
		Boolean b = redis.hexists(key, "ebnew");
		logger.info(b);
		Assert.assertTrue(b);
	}

	@Test
	public void testHdel() throws Exception {
		redis.hset(key, "ebnew", "www.ebnew.com");
		redis.hset(key, "bidlink", "www.bidlink.cn");
		Long l = redis.hdel(key, "ebnew", "bidlink");
		logger.info(l);
		Assert.assertNotSame(0L, l);
	}

	@Test
	public void testHlen() throws Exception {
		redis.hset(key, "ebnew", "www.ebnew.com");
		redis.hset(key, "bidlink", "www.bidlink.cn");
		Long l = redis.hlen(key);
		logger.info(l);
		Assert.assertNotSame(0L, l);
	}

	@Test
	public void testHkeys() throws Exception {
		redis.hset(key, "ebnew", "www.ebnew.com");
		redis.hset(key, "bidlink", "www.bidlink.cn");
		Set<String> set = redis.hkeys(key);
		logger.info(set);
		Assert.assertNotNull(set);
	}

	@Test
	public void testHvals() throws Exception {
		redis.hset(key, "ebnew", "www.ebnew.com");
		redis.hset(key, "bidlink", "www.bidlink.cn");
		List<Object> l = redis.hvals(key);
		logger.info(l);
		Assert.assertNotNull(l);
	}

}
