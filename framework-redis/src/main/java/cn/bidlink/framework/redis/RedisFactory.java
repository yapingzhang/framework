/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>RedisCache.java</p>
 *   
 */
package cn.bidlink.framework.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.SafeEncoder;
import cn.bidlink.framework.redis.config.ShardedRedisPool;
import cn.bidlink.framework.redis.serializer.SerializerFactory;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
@Deprecated
public class RedisFactory implements RedisCache {
	transient Logger logger = Logger.getLogger(getClass());
	private ShardedRedisPool shardedJedisPool;
	private boolean tookTime;

	public void setShardedJedisPool(ShardedRedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}

	public void setTookTime(boolean tookTime) {
		this.tookTime = tookTime;
	}

	public ShardedJedis getResource() {
		try {
			return shardedJedisPool.getResource();
		} catch (Exception e) {
			logger.error("getResource", e);
			throw new JedisConnectionException(e);
		}
	}

	public void returnResource(ShardedJedis shardedJedis) {
		if (null != shardedJedis) {
			try {
				shardedJedisPool.returnResource(shardedJedis);
			} catch (Exception e) {
				logger.error("returnResource", e);
			}
		}
	}

	public void returnBrokenResource(ShardedJedis shardedJedis) {
		if (null != shardedJedis) {
			try {
				shardedJedisPool.returnBrokenResource(shardedJedis);
			} catch (Exception e) {
				logger.error("returnBrokenResource", e);
			} finally {
				shardedJedis = null;
			}
		}
	}

	/**
	 * 
	 * @Description: TODO add description
	 * @param obj
	 * @return
	 */
	public byte[] serialize(Object obj) {
		return SerializerFactory.serialize(obj);
	}

	/**
	 * 
	 * @Description: TODO add description
	 * @param bytes
	 * @return
	 */
	public Object deserialize(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return SerializerFactory.deserialize(bytes);
	}

	@Override
	public String getString(final String key) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.get(key);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis getString (" + key + ") took " + (System.currentTimeMillis() - start.longValue())
						+ " ms");
		}
		return null;
	}

	@Override
	public boolean setString(final String key, final String value) {
		return setString(key, -1, value);
	}

	@Override
	public boolean setString(final String key, final int seconds, final String value) {
		if (key == null || value == null) {
			logger.debug("Key=[" + key + "] and Value=[" + value + "] can not be empty!");
			return false;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (seconds > 0) {
				shardedJedis.setex(key, seconds, value);
			} else {
				shardedJedis.set(key, value);
			}
			return true;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis setString (" + key + ") took " + (System.currentTimeMillis() - start.longValue())
						+ " ms");
		}
		return false;
	}

	@Override
	public Object getObject(String key) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return deserialize(shardedJedis.get(SafeEncoder.encode(key)));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis getObject (" + key + ") took " + (System.currentTimeMillis() - start.longValue())
						+ " ms");
		}
		return null;
	}

	@Override
	public boolean setObject(String key, Object value) {
		return setObject(key, -1, value);
	}

	@Override
	public boolean setObject(String key, int seconds, Object value) {
		if (key == null || value == null) {
			logger.debug("Key=[" + key + "] and Value=[" + value + "] can not be empty!");
			return false;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		boolean result = set(SafeEncoder.encode(key), seconds, serialize(value));
		if (tookTime)
			logger.info("Redis setObject (" + key + ") Took " + (System.currentTimeMillis() - start) + " ms");
		return result;
	}

	protected boolean set(byte[] key, int seconds, byte[] value) {
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (seconds > 0) {
				shardedJedis.setex(key, seconds, value);
			} else {
				shardedJedis.set(key, value);
			}
			return true;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
		}
		return false;
	}

	@Override
	public long del(String key) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return 0L;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.del(key);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis del (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return 0L;
	}

	@Override
	public long del(List<String> keys) {
		if (keys == null) {
			logger.debug("Keys can not be empty!");
			return 0L;
		}
		Long start = null, sum = 0L;
		if (tookTime)
			start = System.currentTimeMillis();
		for (String key : keys) {
			sum += del(key);
		}
		if (tookTime)
			logger.info("Redis del(list) (" + keys.toString() + ") took " + (System.currentTimeMillis() - start)
					+ " ms");
		return sum;
	}

	@Override
	public long expire(String key, int seconds) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return 0L;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.expire(key, seconds);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis expire (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return 0;
	}

	@Override
	public long expire(Map<String, Integer> map) {
		if (map == null) {
			logger.debug("Map can not be empty!");
			return 0L;
		}
		Long start = null, sum = 0L;
		if (tookTime)
			start = System.currentTimeMillis();
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			sum += expire(entry.getKey(), entry.getValue());
		}
		if (tookTime)
			logger.info("Redis expire(map) (" + map.toString() + ") took " + (System.currentTimeMillis() - start)
					+ " ms");
		return sum;
	}

	@Override
	public boolean exists(String key) {
		if (key == null) {
			logger.error("Key can not be empty!");
			return false;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.exists(key);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis exists (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return false;
	}

	@Override
	public Long incr(String key) {
		if (key == null) {
			logger.error("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.incr(key);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis incr (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Long decr(String key) {
		if (key == null) {
			logger.error("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.decr(key);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis decr (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Long append(String key, String value) {
		if (key == null || value == null) {
			logger.debug("Key=[" + key + "] and Value=[" + value + "] can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.append(key, value);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis append (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Long hset(String key, String field, Object value) {
		if (key == null || field == null) {
			logger.debug("Key=[" + key + "] , Field=[" + field + "] and Value=[" + value + "] can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.hset(SafeEncoder.encode(key), SafeEncoder.encode(field), serialize(value));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis hset (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Object hget(String key, String field) {
		if (key == null || field == null) {
			logger.debug("Key=[" + key + "] and Field=[" + field + "] can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return deserialize(shardedJedis.hget(SafeEncoder.encode(key), SafeEncoder.encode(field)));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis hget (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Map<String, Object> hgetAll(String key) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			System.out.println(shardedJedis.getShardInfo(key).getHost());

			Map<byte[], byte[]> bMap = shardedJedis.hgetAll(SafeEncoder.encode(key));
			if (bMap == null || bMap.size() == 0) {
				return null;
			}
			Map<String, Object> map = new HashMap<String, Object>(bMap.size());
			for (Entry<byte[], byte[]> entry : bMap.entrySet()) {
				map.put(SafeEncoder.encode(entry.getKey()), deserialize(entry.getValue()));
			}
			return map;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis hgetAll (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Long hlen(String key) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.hlen(key);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis hlen (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Set<String> hkeys(String key) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.hkeys(key);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis hkeys (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Boolean hexists(String key, String field) {
		if (key == null || field == null) {
			logger.debug("Key=[" + key + "] and Field=[" + field + "] can not be empty!");
			return false;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.hexists(SafeEncoder.encode(key), SafeEncoder.encode(field));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis hexists (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return false;
	}

	@Override
	public Long lpush(String key, Object item) {
		if (key == null || item == null) {
			logger.debug("Key=[" + key + "] and Item=[" + item + "] can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.lpush(SafeEncoder.encode(key), serialize(item));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis lpush (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Long rpush(String key, Object item) {
		if (key == null || item == null) {
			logger.debug("Key=[" + key + "] and Item=[" + item + "] can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.rpush(SafeEncoder.encode(key), serialize(item));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis rpush (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Object lpop(String key) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return deserialize(shardedJedis.lpop(SafeEncoder.encode(key)));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis lpop (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Object rpop(String key) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return deserialize(shardedJedis.rpop(SafeEncoder.encode(key)));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis rpop (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Object lindex(String key, int index) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return deserialize(shardedJedis.lindex(SafeEncoder.encode(key), index));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis lindex (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public List<Object> lrange(String key, int start, int end) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long begin = null;
		if (tookTime)
			begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			List<byte[]> sb = shardedJedis.lrange(SafeEncoder.encode(key), start, end);

			List<Object> ret = new ArrayList<Object>();
			for (byte[] b : sb) {
				if (b != null) {
					ret.add(deserialize(b));
				}
			}
			return ret;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis lrange (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public boolean ltrim(String key, int start, int end) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return false;
		}
		Long begin = null;
		if (tookTime)
			begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			shardedJedis.ltrim(SafeEncoder.encode(key), start, end);
			return true;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis ltrim (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return false;

	}

	@Override
	public boolean lset(String key, int index, Object value) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return false;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			shardedJedis.lset(SafeEncoder.encode(key), index, serialize(value));
			return true;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis lset (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return false;
	}

	@Override
	public Long llen(String key) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.llen(key);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis llen (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Long sadd(String key, Object members) {
		if (key == null || members == null) {
			logger.debug("Key=[" + key + "] and Members=[" + members + "] can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.sadd(SafeEncoder.encode(key), serialize(members));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis sadd (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Long srem(String key, Object members) {
		if (key == null || members == null) {
			logger.debug("Key=[" + key + "] and Members=[" + members + "] can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.srem(SafeEncoder.encode(key), serialize(members));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis srem (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Long scard(String key) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.scard(key);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis scard (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Set<Object> smembers(String key) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			Set<byte[]> sb = shardedJedis.smembers(SafeEncoder.encode(key));
			Set<Object> ret = new HashSet<Object>();
			for (byte[] b : sb) {
				if (b != null) {
					ret.add(deserialize(b));
				}
			}
			return ret;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis smembers (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Boolean sismemberObject(String key, Object member) {
		if (key == null || member == null) {
			logger.debug("Key=[" + key + "] and Member=[" + member + "] can not be empty!");
			return false;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.sismember(SafeEncoder.encode(key), serialize(member));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis sismemberObject took " + (System.currentTimeMillis() - start) + " ms");
		}
		return false;
	}

	@Override
	public Object spop(String key) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.spop(key);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis spop (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Long zadd(String key, double score, Object member) {
		if (key == null || member == null) {
			logger.debug("Key=[" + key + "] and Member=[" + member + "] can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.zadd(SafeEncoder.encode(key), score, serialize(member));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis zadd (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Long zrem(String key, Object member) {
		if (key == null || member == null) {
			logger.debug("Key=[" + key + "] and Member=[" + member + "] can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.zrem(SafeEncoder.encode(key), serialize(member));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis zrem (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Long zcard(String key) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.zcard(SafeEncoder.encode(key));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis zcard (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Long zrank(String key, Object member) {
		if (key == null || member == null) {
			logger.debug("Key=[" + key + "] and Member=[" + member + "] can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			return shardedJedis.zrank(SafeEncoder.encode(key), serialize(member));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis zrank (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public List<Object> sort(String key, String... patterns) {
		return sort(key, 0, 0, 0, patterns);
	}

	@Override
	public List<Object> sort(String key, int sorting, String... patterns) {
		return sort(key, sorting, -1, -1, patterns);
	}

	@Override
	public List<Object> sort(String key, int sorting, final int starts, final int count, String... patterns) {
		if (key == null) {
			logger.debug("Key can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		SortingParams sortingParams = null;
		if (sorting == 1) {
			if (sortingParams == null) {
				sortingParams = new SortingParams();
			}
			sortingParams.asc();
		} else if (sorting == -1) {
			if (sortingParams == null) {
				sortingParams = new SortingParams();
			}
			sortingParams.desc();
		}
		if (patterns != null) {
			if (sortingParams == null) {
				sortingParams = new SortingParams();
			}
			sortingParams.get(patterns);
		}
		if (starts >= 0 && count >= 1) {
			if (sortingParams == null) {
				sortingParams = new SortingParams();
			}
			sortingParams.limit(starts, count);
		}
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			List<byte[]> sb = null;
			if (sortingParams == null) {
				sb = shardedJedis.sort(SafeEncoder.encode(key));
				shardedJedis.lrange(SafeEncoder.encode(key), starts, 1);
			} else {
				sb = shardedJedis.sort(SafeEncoder.encode(key), sortingParams);
			}
			List<Object> ret = new ArrayList<Object>();
			for (byte[] b : sb) {
				if (b != null) {
					ret.add(deserialize(b));
				}
			}
			return ret;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis sort (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public String hmset(String key, Map<String, Object> map) {
		if (key == null || map == null || map.isEmpty()) {
			logger.debug("Key=[" + key + "] and Map=[" + map + "] can not be empty!");
			return null;
		}
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();
			for (Entry<String, Object> entry : map.entrySet()) {
				hash.put(SafeEncoder.encode(entry.getKey()), serialize(entry.getValue()));
			}
			return shardedJedis.hmset(SafeEncoder.encode(key), hash);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis hmset (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public List<Object> hmget(String key, String... fields) {
		if (key == null || fields == null) {
			logger.debug("Key=[" + key + "] and Fields=[" + fields + "] can not be empty!");
			return null;
		}
		List<Object> list = new ArrayList<Object>();
		Long start = null;
		if (tookTime)
			start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			List<byte[]> bytes = null;
			for (String field : fields) {
				bytes = shardedJedis.hmget(SafeEncoder.encode(key), SafeEncoder.encode(field));
				if (bytes != null && bytes.size() == 1) {
					list.add(deserialize(bytes.get(0)));
				}
			}
			return list;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(shardedJedis);
			if (tookTime)
				logger.info("Redis hget (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}
}
