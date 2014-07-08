/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>RedisCacheFactory.java</p>
 *   
 */
package cn.bidlink.framework.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import redis.clients.jedis.JedisPoolConfig;
import cn.bidlink.framework.redis.config.RedisShardInfo;
import cn.bidlink.framework.redis.config.ShardedRedisPool;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
@Deprecated
public class RedisCacheFactory {
	static transient Logger logger = Logger.getLogger(RedisCacheFactory.class);
	private final static String REDIS_FILE = "framework.redis.properties";// 默认配置文件
	private final static String REDIS_CONF = "framework.redis.conf";// 连接字符串
	private final static String REDIS_TOOK = "framework.redis.took";// 连接字符串
	static RedisCache redisCache;

	/**
	 * 默认构造函数
	 */
	static {
		InputStream inputStream = null;
		String shardInfo = null;
		boolean redisTook = false;
		try {
			// 读取配置文件
			inputStream = RedisCacheFactory.class.getClassLoader().getResourceAsStream(REDIS_FILE);
			Properties properties = new Properties();
			properties.load(inputStream);
			shardInfo = properties.getProperty(REDIS_CONF);
			logger.info("shardInfo:"+shardInfo);
			redisTook = (properties.getProperty(REDIS_TOOK) != null && "true".equals(properties.getProperty(REDIS_TOOK)));

		} catch (IOException e) {
			logger.error("read jedisb.properties error ", e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
			}
		}
		RedisShardInfo redisShardInfo = new RedisShardInfo(shardInfo);
		// 配置连接池
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(500);
		config.setMaxIdle(50);
		config.setMaxWait(3000L);
		config.setTestOnBorrow(false);
		config.setTestWhileIdle(true);
		ShardedRedisPool shardedJedisPool = new ShardedRedisPool(config, redisShardInfo);
		RedisFactory redisCacheImpl = new RedisFactory();
		redisCacheImpl.setShardedJedisPool(shardedJedisPool);
		redisCacheImpl.setTookTime(redisTook);
		redisCache = redisCacheImpl;
	}

	public static RedisCache getRedisCache() {
		return redisCache;
	}

}
