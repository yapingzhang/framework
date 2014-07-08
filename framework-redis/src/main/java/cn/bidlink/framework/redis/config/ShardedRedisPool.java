/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>ShardedRedisPool.java</p>
 *   
 */
package cn.bidlink.framework.redis.config;

import org.apache.commons.pool.impl.GenericObjectPool.Config;

import redis.clients.jedis.ShardedJedisPool;

/**
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-11-21 上午10:49:53
 */
public class ShardedRedisPool extends ShardedJedisPool {

	public ShardedRedisPool(Config poolConfig, RedisShardInfo shardInfo) {
		super(poolConfig, shardInfo.getJedisShardInfo());
	}

}
