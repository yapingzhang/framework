/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.redis;

import java.util.ArrayList;
import java.util.Collection;
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
import cn.bidlink.framework.redis.utils.EmptyUtils;

/**
 * Redis Shard 实现类.
 * 
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-11-29 上午10:22:26
 */
public class BidRedisImpl implements BidRedis {
	transient Logger logger = Logger.getLogger(getClass());
	private ShardedRedisPool shardedJedisPool;

	@Override
	public Boolean exists(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return false;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis exists (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.exists(SafeEncoder.encode(key));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis exists", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis exists (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return false;
	}

	@Override
	public Long persist(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis persist (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.persist(SafeEncoder.encode(key));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis persist", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis persist (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public String type(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis type (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.type(SafeEncoder.encode(key));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis type", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis type (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Long expire(String key, int seconds) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return 0L;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis expire (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.expire(SafeEncoder.encode(key), seconds);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis expire", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis expire (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Long expire(Map<String, Integer> map) {
		if (EmptyUtils.isEmpty(map)) {
			logger.error("Map can not be empty!");
			return 0L;
		}

		Long begin = System.currentTimeMillis();
		Long sum = 0L;
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				sum += this.expire(entry.getKey(), entry.getValue());
			}
			return sum;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis expire(map)", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis expire(map) (" + map.toString() + ") took " + (System.currentTimeMillis() - begin)
					+ " ms");
		}
		return sum;
	}

	@Override
	public Long expireAt(String key, Long unixTime) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return 0L;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis expireAt (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.expireAt(SafeEncoder.encode(key), unixTime);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis expireAt", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis expireAt (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Long expireAt(Map<String, Long> map) {
		if (EmptyUtils.isEmpty(map)) {
			logger.error("Map can not be empty!");
			return 0L;
		}

		Long begin = System.currentTimeMillis();
		Long sum = 0L;
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			for (Map.Entry<String, Long> entry : map.entrySet()) {
				sum += this.expireAt(entry.getKey(), entry.getValue());
			}
			return sum;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis expireAt(map)", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis expireAt(map) (" + map.toString() + ") took " + (System.currentTimeMillis() - begin)
					+ " ms");
		}
		return sum;
	}

	@Override
	public Long ttl(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return 0L;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis ttl (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.ttl(SafeEncoder.encode(key));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis ttl", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis ttl (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Long del(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return 0L;
		}
		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis ttl (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.del(SafeEncoder.encode(key));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis del", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis del (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Long del(String... keys) {
		if (EmptyUtils.isEmpty(keys)) {
			logger.error("Keys can not be empty!");
			return 0L;
		}
		Long begin = System.currentTimeMillis();
		Long sum = 0L;
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			for (String key : keys) {
				sum += this.del(key);
			}
			return sum;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis del[]", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis del[] (" + keys.toString() + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return sum;
	}

	@Override
	public String setString(final String key, final String value) {
		return this.setString(key, -1, value);
	}

	@Override
	public String setString(final String key, final int seconds, final String value) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return "failed";
		}
		if (EmptyUtils.isEmpty(value)) {
			logger.info("Value can not be empty!");
			return "failed";
		}
		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis setString (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			if (seconds > 0) {
				return shardedJedis.setex(key, seconds, value);
			} else {
				return shardedJedis.set(key, value);
			}
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis rpush", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis rpush (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return "failed";
	}

	@Override
	public String getString(final String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return "failed";
		}
		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis getString (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.get(key);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis getString", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis getString (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public String getSetString(final String key, final String value) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return null;
		}
		if (EmptyUtils.isEmpty(value)) {
			logger.info("Value can not be empty!");
			return null;
		}
		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis setString (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.getSet(key, value);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis rpush", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis rpush (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public String setObject(final String key, final Object value) {
		return this.setObject(key, -1, value);
	}

	@Override
	public String setObject(String key, int seconds, Object value) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return "failed";
		}
		if (EmptyUtils.isEmpty(value)) {
			logger.info("Value can not be empty!");
			return "failed";
		}
		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis setObject (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			if (seconds > 0) {
				return shardedJedis.setex(SafeEncoder.encode(key), seconds, serialize(value));
			} else {
				return shardedJedis.set(SafeEncoder.encode(key), serialize(value));
			}
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis setObject", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis setObject (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return "failed";
	}

	@Override
	public Object getObject(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis getObject (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return deserialize(shardedJedis.get(SafeEncoder.encode(key)));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis getObject", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis getObject (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Object getSetObject(String key, Object value) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return null;
		}
		if (EmptyUtils.isEmpty(value)) {
			logger.info("Value can not be empty!");
			return null;
		}
		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis getSetObject (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.getSet(SafeEncoder.encode(key), serialize(value));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis getSetObject", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis getSetObject (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Long incr(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return null;
		}
		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis incr (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.incr(key);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis incr", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis incr (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Long incrBy(String key, long integer) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return null;
		}
		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis incrBy (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.incrBy(key, integer);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis incrBy", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis incrBy (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Long decr(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return null;
		}
		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis decr (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.decr(key);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis decr", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis decr (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Long decrBy(String key, long integer) {
		if (EmptyUtils.isEmpty(key)) {
			logger.error("Key can not be empty!");
			return null;
		}
		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis decrBy (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.decrBy(key, integer);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis decrBy", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis decrBy (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Long hset(final String key, final String field, final Object value) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}
		if (EmptyUtils.isEmpty(field)) {
			logger.info("Field can not be empty!");
			return null;
		}
		if (EmptyUtils.isEmpty(value)) {
			logger.info("Value can not be empty!");
			return null;
		}
		Long start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis hset (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.hset(SafeEncoder.encode(key), SafeEncoder.encode(field), serialize(value));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis hset", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis hset (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Boolean hmset(final String key, final Map<String, Object> map) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return false;
		}
		if (EmptyUtils.isEmpty(map)) {
			logger.info("Map can not be empty!");
			return false;
		}
		Long start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis hmset (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();
			for (Entry<String, Object> entry : map.entrySet()) {
				hash.put(SafeEncoder.encode(entry.getKey()), serialize(entry.getValue()));
			}
			shardedJedis.hmset(SafeEncoder.encode(key), hash);
			return true;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis hmset", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis hmset (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return false;
	}

	@Override
	public Object hget(final String key, final String field) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}
		if (EmptyUtils.isEmpty(field)) {
			logger.info("Field can not be empty!");
			return null;
		}
		Long start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis hget (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return deserialize(shardedJedis.hget(SafeEncoder.encode(key), SafeEncoder.encode(field)));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis hget", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis hget (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Map<String, Object> hgetAll(final String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}
		Long start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis hgetAll (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			Map<byte[], byte[]> bMap = shardedJedis.hgetAll(SafeEncoder.encode(key));
			if (EmptyUtils.isEmpty(bMap)) {
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
			logger.error("Redis hgetAll", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis hgetAll (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public List<Object> hmget(final String key, final String... fields) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}
		if (EmptyUtils.isEmpty(fields)) {
			logger.info("Fields can not be empty!");
			return null;
		}

		Long start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis hmget (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			final byte[][] bfields = new byte[fields.length][];
			for (int i = 0; i < bfields.length; i++) {
				bfields[i] = SafeEncoder.encode(fields[i]);
			}
			List<byte[]> bList = shardedJedis.hmget(SafeEncoder.encode(key), bfields);
			if (EmptyUtils.isEmpty(bList)) {
				return null;
			}
			List<Object> list = new ArrayList<Object>(fields.length);
			for (byte[] bts : bList) {
				list.add(deserialize(bts));
			}
			return list;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis hmget", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis hmget (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Boolean hexists(final String key, final String field) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return false;
		}
		if (EmptyUtils.isEmpty(field)) {
			logger.info("Field can not be empty!");
			return false;
		}

		Long start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis hexists (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.hexists(SafeEncoder.encode(key), SafeEncoder.encode(field));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis hexists", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis hexists (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return false;
	}

	@Override
	public Long hdel(String key, String... fields) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}
		if (EmptyUtils.isEmpty(fields)) {
			logger.info("Fields can not be empty!");
			return 0L;
		}

		Long start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis hdel (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			final byte[][] bfields = new byte[fields.length][];
			for (int i = 0; i < bfields.length; i++) {
				bfields[i] = SafeEncoder.encode(fields[i]);
			}
			return shardedJedis.hdel(SafeEncoder.encode(key), bfields);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis hdel", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis hdel (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return 0L;
	}

	@Override
	public Long hlen(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}
		Long start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis hlen (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.hlen(SafeEncoder.encode(key));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis hlen", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis hlen (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return 0L;
	}

	@Override
	public Set<String> hkeys(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis hkeys (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.hkeys(key);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis hkeys", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis hkeys (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public List<Object> hvals(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long start = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis hvals (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			Collection<byte[]> bList = shardedJedis.hvals(SafeEncoder.encode(key));

			List<Object> list = new ArrayList<Object>(bList.size());
			for (byte[] bts : bList) {
				list.add(deserialize(bts));
			}
			return list;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis hvals", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis hvals (" + key + ") took " + (System.currentTimeMillis() - start) + " ms");
		}
		return null;
	}

	@Override
	public Long rpush(String key, Object... values) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}
		if (EmptyUtils.isEmpty(values)) {
			logger.info("Values can not be empty!");
			return 0L;
		}
		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis rpush (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			final byte[][] bvalues = new byte[values.length][];
			for (int i = 0; i < bvalues.length; i++) {
				bvalues[i] = serialize(values[i]);
			}

			return shardedJedis.rpush(SafeEncoder.encode(key), bvalues);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis rpush", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis rpush (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Long lpush(String key, Object... values) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}
		if (EmptyUtils.isEmpty(values)) {
			logger.info("Values can not be empty!");
			return 0L;
		}
		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis lpush (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			final byte[][] bvalues = new byte[values.length][];
			for (int i = 0; i < bvalues.length; i++) {
				bvalues[i] = serialize(values[i]);
			}

			return shardedJedis.lpush(SafeEncoder.encode(key), bvalues);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis lpush", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis lpush (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Long llen(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis llen (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.llen(SafeEncoder.encode(key));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis llen", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis llen (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public List<Object> lrange(String key, long start, long end) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}
		if (start < 0) {
			logger.info("start is greater than or equal to zero!");
			return null;
		}
		if (end < 0) {
			logger.info("end is greater than or equal to zero!");
			return null;
		}
		if (end < start) {
			logger.info("end is greater than or equal to start!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis lrange (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			List<byte[]> bList = shardedJedis.lrange(SafeEncoder.encode(key), start, end);
			if (EmptyUtils.isEmpty(bList)) {
				return null;
			}
			List<Object> list = new ArrayList<Object>(bList.size());
			for (byte[] bts : bList) {
				list.add(deserialize(bts));
			}
			return list;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis lrange", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis lrange (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public String ltrim(final String key, final long start, final long end) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}
		if (start < 0) {
			logger.info("start is greater than or equal to zero!");
			return null;
		}
		if (end < 0) {
			logger.info("end is greater than or equal to zero!");
			return null;
		}
		if (end < start) {
			logger.info("end is greater than or equal to start!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis lrange (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.ltrim(SafeEncoder.encode(key), start, end);

		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis lrange", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis lrange (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Object lindex(String key, long index) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis lindex (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return deserialize(shardedJedis.lindex(SafeEncoder.encode(key), index));

		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis lindex", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis lindex (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public String lset(String key, long index, Object value) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis lset (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.lset(SafeEncoder.encode(key), index, serialize(value));

		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis lset", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis lset (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Long lrem(String key, long count, Object value) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}
		if (EmptyUtils.isEmpty(value)) {
			logger.info("Value can not be empty!");
			return 0L;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis lrem (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.lrem(SafeEncoder.encode(key), count, serialize(value));

		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis lrem", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis lrem (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Object lpop(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis lpop (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return deserialize(shardedJedis.lpop(SafeEncoder.encode(key)));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis lpop", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis lpop (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Object rpop(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis rpop (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return deserialize(shardedJedis.rpop(SafeEncoder.encode(key)));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis rpop", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis rpop (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Long sadd(String key, Object... members) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}
		if (EmptyUtils.isEmpty(members)) {
			logger.info("Member can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis sadd (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			final byte[][] bmembers = new byte[members.length][];
			for (int i = 0; i < members.length; i++) {
				bmembers[i] = serialize(members[i]);
			}
			return shardedJedis.sadd(SafeEncoder.encode(key), bmembers);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis sadd", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis sadd (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Set<Object> smembers(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis sadd (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			Set<byte[]> bSet = shardedJedis.smembers(SafeEncoder.encode(key));
			if (EmptyUtils.isEmpty(bSet)) {
				return null;
			}
			Set<Object> set = new HashSet<Object>(bSet.size());
			for (byte[] bts : bSet) {
				set.add(deserialize(bts));
			}
			return set;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis sadd", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis sadd (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Long srem(String key, Object... members) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}
		if (EmptyUtils.isEmpty(members)) {
			logger.info("Members can not be empty!");
			return 0L;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis srem (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			final byte[][] bmembers = new byte[members.length][];
			for (int i = 0; i < members.length; i++) {
				bmembers[i] = serialize(members[i]);
			}
			return shardedJedis.srem(SafeEncoder.encode(key), bmembers);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis srem", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis srem (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Object spop(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis spop (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return deserialize(shardedJedis.spop(SafeEncoder.encode(key)));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis spop", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis spop (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Long scard(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis scard (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.scard(SafeEncoder.encode(key));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis scard", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis scard (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Boolean sismember(String key, String member) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return false;
		}
		if (EmptyUtils.isEmpty(member)) {
			logger.info("Member can not be empty!");
			return false;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis sismember (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.sismember(SafeEncoder.encode(key), serialize(member));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis sismember", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis sismember (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return false;
	}

	@Override
	public Object srandmember(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis srandmember (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return deserialize(shardedJedis.srandmember(SafeEncoder.encode(key)));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis srandmember", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis srandmember (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Long zadd(String key, double score, Object member) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}
		if (EmptyUtils.isEmpty(member)) {
			logger.info("Member can not be empty!");
			return 0L;
		}
		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zadd (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.zadd(SafeEncoder.encode(key), score, serialize(member));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zadd", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zadd (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Long zadd(String key, Map<Double, Object> scoreMembers) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}
		if (EmptyUtils.isEmpty(scoreMembers)) {
			logger.info("ScoreMembers can not be empty!");
			return 0L;
		}
		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zadd (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			Map<Double, byte[]> bScoreMembers = new HashMap<Double, byte[]>();
			for (Entry<Double, Object> entry : scoreMembers.entrySet()) {
				bScoreMembers.put(entry.getKey(), serialize(entry.getValue()));
			}
			return shardedJedis.zadd(SafeEncoder.encode(key), bScoreMembers);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zadd", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zadd (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Long zrem(String key, Object... members) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zrem (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			final byte[][] bmembers = new byte[members.length][];
			for (int i = 0; i < bmembers.length; i++) {
				bmembers[i] = serialize(members[i]);
			}
			return shardedJedis.zrem(SafeEncoder.encode(key), bmembers);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zrem", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zrem (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Double zincrby(String key, double score, Object member) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0D;
		}
		if (EmptyUtils.isEmpty(member)) {
			logger.info("Member can not be empty!");
			return 0D;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zrem (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.zincrby(SafeEncoder.encode(key), score, serialize(member));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zrem", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zrem (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0D;
	}

	@Override
	public Long zcard(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zcard (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.zcard(SafeEncoder.encode(key));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zcard", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zcard (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Double zscore(String key, Object member) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zscore (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.zscore(SafeEncoder.encode(key), serialize(member));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zscore", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zscore (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Long zcount(String key, double min, double max) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zcount (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.zcount(SafeEncoder.encode(key), min, max);
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zcount", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zcount (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Long zrank(String key, Object member) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}
		if (EmptyUtils.isEmpty(member)) {
			logger.info("Member can not be empty!");
			return 0L;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zrank (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.zrank(SafeEncoder.encode(key), serialize(member));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zrank", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zrank (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Long zrevrank(String key, Object member) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return 0L;
		}
		if (EmptyUtils.isEmpty(member)) {
			logger.info("Member can not be empty!");
			return 0L;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zrevrank (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			return shardedJedis.zrevrank(SafeEncoder.encode(key), serialize(member));
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zrevrank", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zrevrank (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return 0L;
	}

	@Override
	public Set<Object> zrange(String key, long start, long end) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zrange (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			Set<byte[]> bSet = shardedJedis.zrange(SafeEncoder.encode(key), start, end);
			if (EmptyUtils.isEmpty(bSet)) {
				return null;
			}
			Set<Object> set = new HashSet<Object>(bSet.size());
			for (byte[] bts : bSet) {
				set.add(deserialize(bts));
			}
			return set;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zrange", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zrange (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Set<Object> zrevrange(String key, long start, long end) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zrevrange (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			Set<byte[]> bSet = shardedJedis.zrevrange(SafeEncoder.encode(key), start, end);
			if (EmptyUtils.isEmpty(bSet)) {
				return null;
			}
			Set<Object> set = new HashSet<Object>(bSet.size());
			for (byte[] bts : bSet) {
				set.add(deserialize(bts));
			}
			return set;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zrevrange", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zrevrange (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Set<Object> zrangeByScore(String key, double min, double max) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zrangeByScore (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			Set<byte[]> bSet = shardedJedis.zrangeByScore(SafeEncoder.encode(key), min, max);
			if (EmptyUtils.isEmpty(bSet)) {
				return null;
			}
			Set<Object> set = new HashSet<Object>(bSet.size());
			for (byte[] bts : bSet) {
				set.add(deserialize(bts));
			}
			return set;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zrangeByScore", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zrangeByScore (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Set<Object> zrevrangeByScore(String key, double max, double min) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zrangeByScore (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			Set<byte[]> bSet = shardedJedis.zrevrangeByScore(SafeEncoder.encode(key), max, min);
			if (EmptyUtils.isEmpty(bSet)) {
				return null;
			}
			Set<Object> set = new HashSet<Object>(bSet.size());
			for (byte[] bts : bSet) {
				set.add(deserialize(bts));
			}
			return set;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zrangeByScore", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zrangeByScore (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Set<Object> zrangeByScore(String key, double min, double max, int offset, int count) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zrangeByScore (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			Set<byte[]> bSet = shardedJedis.zrangeByScore(SafeEncoder.encode(key), min, max, offset, count);
			if (EmptyUtils.isEmpty(bSet)) {
				return null;
			}
			Set<Object> set = new HashSet<Object>(bSet.size());
			for (byte[] bts : bSet) {
				set.add(deserialize(bts));
			}
			return set;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zrangeByScore", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zrangeByScore (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public Set<Object> zrevrangeByScore(String key, double max, double min, int offset, int count) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis zrevrangeByScore (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			Set<byte[]> bSet = shardedJedis.zrevrangeByScore(SafeEncoder.encode(key), max, min, offset, count);
			if (EmptyUtils.isEmpty(bSet)) {
				return null;
			}
			Set<Object> set = new HashSet<Object>(bSet.size());
			for (byte[] bts : bSet) {
				set.add(deserialize(bts));
			}
			return set;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis zrevrangeByScore", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis zrevrangeByScore (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public List<Object> sort(String key) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis sort (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			List<byte[]> bList = shardedJedis.sort(SafeEncoder.encode(key));
			if (EmptyUtils.isEmpty(bList)) {
				return null;
			}
			List<Object> list = new ArrayList<Object>(bList.size());
			for (byte[] bts : bList) {
				list.add(deserialize(bts));
			}
			return list;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis sort", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis sort (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	@Override
	public List<Object> sort(String key, SortingParams sortingParameters) {
		if (EmptyUtils.isEmpty(key)) {
			logger.info("Key can not be empty!");
			return null;
		}

		Long begin = System.currentTimeMillis();
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = getResource();
			if (logger.isInfoEnabled()) {
				logger.info("Redis sort (" + key + "):" + shardedJedis.getShardInfo(key));
			}
			List<byte[]> bList = shardedJedis.sort(SafeEncoder.encode(key), sortingParameters);
			if (EmptyUtils.isEmpty(bList)) {
				return null;
			}
			List<Object> list = new ArrayList<Object>(bList.size());
			for (byte[] bts : bList) {
				list.add(deserialize(bts));
			}
			return list;
		} catch (JedisConnectionException jce) {
			returnBrokenResource(shardedJedis);
		} catch (Exception e) {
			logger.error("Redis sort", e);
		} finally {
			returnResource(shardedJedis);
			logger.info("Redis sort (" + key + ") took " + (System.currentTimeMillis() - begin) + " ms");
		}
		return null;
	}

	private ShardedJedis getResource() {
		try {
			return shardedJedisPool.getResource();
		} catch (Exception e) {
			logger.error("getResource", e);
			throw new JedisConnectionException(e);
		}
	}

	private void returnResource(ShardedJedis shardedJedis) {
		if (null != shardedJedis) {
			try {
				shardedJedisPool.returnResource(shardedJedis);
			} catch (Exception e) {
				logger.error("returnResource", e);
			}
		}
	}

	private void returnBrokenResource(ShardedJedis shardedJedis) {
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

	private byte[] serialize(Object obj) {
		return SerializerFactory.serialize(obj);
	}

	private Object deserialize(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return SerializerFactory.deserialize(bytes);
	}

	/**
	 * 设置连接池.
	 * 
	 * @author wangtao 2013-11-29
	 * @param shardedJedisPool
	 */
	public void setShardedJedisPool(ShardedRedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}
}
