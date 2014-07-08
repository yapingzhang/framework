/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.SortingParams;

/**
 * Redis Shard 接口.
 * 
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-11-29 上午10:22:26
 */
public interface BidRedis {

	/**
	 * 确认key是否存在.
	 * <p>
	 * key存在,返回true;否则,返回false.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Boolean exists(String key);

	/**
	 * 撤销即将过期的key,使其能继续使用.
	 * <p>
	 * key可以继续使用,返回1L;否则,返回0L.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Long persist(String key);

	/**
	 * 返回值的类型.
	 * <p>
	 * Redis中的数据类型,如:String,Set等.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	String type(String key);

	/**
	 * 设置key的有效时间.
	 * <p>
	 * 设置成功,返回1L;设置失败,返回0L.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param seconds
	 *            过期时间,单位为秒
	 * @return
	 */
	Long expire(String key, int seconds);

	/**
	 * 设置一组key的有效时间.
	 * <p>
	 * 设置成功,返回>=1L;设置失败,返回0L.
	 * 
	 * @param map
	 *            key=>key, value=>过期时间
	 * @return
	 */
	Long expire(Map<String, Integer> map);

	/**
	 * 设置一个key的到期时间.
	 * <p>
	 * 设置成功,返回1L;设置失败,返回0L.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param unixTime
	 *            相对1970后经过的秒数
	 * @return
	 */
	Long expireAt(String key, Long unixTime);

	/**
	 * 设置一组key的到期时间.
	 * <p>
	 * 设置成功,返回>=1L;设置失败,返回0L.
	 * 
	 * @param map
	 *            key=>key, value=>到期时间,相对1970后经过的秒数
	 * @return
	 */
	Long expireAt(Map<String, Long> map);

	/**
	 * 获得一个key的有效时间.
	 * <P>
	 * 返回Key的有效时间,单位为秒.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Long ttl(String key);

	/**
	 * 删除指定的key.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Long del(String key);

	/**
	 * 删除指定的一组key.
	 * 
	 * @author wangtao 2013-11-29
	 * @param keys
	 * @return
	 */
	Long del(String... keys);

	/**
	 * 缓存一个字符串.
	 * <p>
	 * 只能与getString(key)配合使用.<br>
	 * 缓存成功后无法通过getObject(key)接口取出正确数据.<br>
	 * 若后续需要对此String进行incr/decr/incrBy/decrBy操作,则不能用setObject替换.
	 * 
	 * @see #getString(String)
	 * @see #setString(String, int, String)
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param value
	 * @return "OK" or "failed"
	 */
	String setString(String key, String value);

	/**
	 * 缓存一个字符串.
	 * <p>
	 * 只能与getString(key)配合使用.<br>
	 * 缓存成功后无法通过getObject(key)接口取出正确数据.<br>
	 * 若后续需要对此String进行incr/decr/incrBy/decrBy操作,则不能用setObject替换.
	 * 
	 * @see #getString(String)
	 * @see #setString(String, String)
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param seconds
	 * @param value
	 * @return "OK" or "failed"
	 */
	String setString(String key, int seconds, String value);

	/**
	 * 缓存一个字符串.
	 * <p>
	 * 只能与setString()配合使用.<br>
	 * 通过setObject(key,value)之类的接口缓存的对象,无法通过此接口取出正确数据.
	 * 
	 * @see #setString(String, String)
	 * @see #setString(String, int, String)
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	String getString(String key);

	/**
	 * 缓存新的字符串,并返回旧的字符串.
	 * <p>
	 * 字符串不能大于1GB.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param value
	 * @return
	 */
	String getSetString(String key, String value);

	/**
	 * 缓存一个字符串.
	 * <p>
	 * 只能与getObject()配合使用.<br>
	 * 缓存成功后无法通过getString(key)接口取出正确数据.<br>
	 * 若后续需要对此String进行incr/decr/incrBy/decrBy操作,则不能用此方法.
	 * 
	 * @see #getObject(String)
	 * @see #setObject(String, int, Object)
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param value
	 * @return "OK" or "failed"
	 */
	String setObject(String key, Object value);

	/**
	 * 缓存一个字符串.
	 * <p>
	 * 只能与getObject()配合使用.<br>
	 * 缓存成功后无法通过getString(key)接口取出正确数据.<br>
	 * 若后续需要对此String进行incr/decr/incrBy/decrBy操作,则不能用此方法.
	 * 
	 * @see #getObject(String)
	 * @see #setObject(String, Object)
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param seconds
	 * @param value
	 * @return "OK" or "failed"
	 */
	String setObject(String key, int seconds, Object value);

	/**
	 * 缓存一个字符串.
	 * <p>
	 * 只能与setObject()配合使用.<br>
	 * 通过setString(key,value)之类的接口缓存的对象,无法通过此接口取出正确数据.
	 * 
	 * @see #setObject(String, Object)
	 * @see #setObject(String, int, Object)
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Object getObject(String key);

	/**
	 * 缓存新的对象,并返回旧的对象.
	 * <p>
	 * 对象不能大于1GB.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param value
	 * @return
	 */
	Object getSetObject(String key, Object value);

	/**
	 * 对key做自增1.
	 * <p>
	 * 如果key不存在,则在执行此操作前设置默认值为0.<br>
	 * 如果key存在,且key是十进制数值的字符串表示(e.g: "-123L"),则在此数据基础上自增(e.g:return -122L).<br>
	 * 如果key存在,且key不是十进制数值的字符串表示,则返回null.
	 * 
	 * @see #decr(String)
	 * @see #incrBy(String, long)
	 * @see #decrBy(String, long)
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Long incr(String key);

	/**
	 * 对key做自增N.
	 * 
	 * @see #decrBy(String, long)
	 * @see #decr(String)
	 * @see #incr(String)
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param integer
	 * @return
	 */
	Long incrBy(String key, long integer);

	/**
	 * 对key做自减1.
	 * <p>
	 * 如果key不存在,则在执行此操作前设置默认值为0.<br>
	 * 如果key存在,且key是十进制数值的字符串表示(e.g: "-123L"),则在此数据基础上自增(e.g:return -124L).<br>
	 * 如果key存在,且key不是十进制数值的字符串表示,则返回null.
	 * 
	 * @see #incr(String)
	 * @see #incrBy(String, long)
	 * @see #decrBy(String, long)
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Long decr(String key);

	/**
	 * 对key做自减N.
	 * 
	 * @see #incrBy(String, long)
	 * @see #decr(String)
	 * @see #incr(String)
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param integer
	 * @return
	 */
	Long decrBy(String key, long integer);

	/**
	 * 向名称为key的hash中添加元素field<—>value.
	 * <p>
	 * 如果字段已经存在,则更新field值,返回0L;<br>
	 * 否则创建一新的field,返回1L.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	Long hset(String key, String field, Object value);

	/**
	 * 向名称为key的hash中添加元素field i<—>value i.
	 * <p>
	 * 操作成功,返回true;<br>
	 * 否则,返回false.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param hash
	 * @return
	 */
	Boolean hmset(String key, Map<String, Object> hash);

	/**
	 * 返回名称为key的hash中field对应的value.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param field
	 * @return
	 */
	Object hget(String key, String field);

	/**
	 * 返回名称为key的hash中所有的键(field)及其对应的value.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Map<String, Object> hgetAll(String key);

	/**
	 * 返回名称为key的hash中field i对应的value.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param fields
	 * @return
	 */
	List<Object> hmget(String key, String... fields);

	/**
	 * 名称为key的hash中是否存在键为field的域.
	 * <p>
	 * 存在,返回true;<br>
	 * 否则,返回false.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param field
	 * @return
	 */
	Boolean hexists(String key, String field);

	/**
	 * 删除名称为key的hash中键为field i的域.
	 * <p>
	 * 如果存在,删除,返回1L;<br>
	 * 否则,返回0L,不执行操作.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param fields
	 * @return
	 */
	Long hdel(final String key, final String... fields);

	/**
	 * 返回名称为key的hash中元素个数.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Long hlen(String key);

	/**
	 * 返回名称为key的hash中所有键.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Set<String> hkeys(String key);

	/**
	 * 返回名称为key的hash中所有键对应的value.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	List<Object> hvals(String key);

	/**
	 * 在名称为key的list尾添加一个值为value的元素.
	 * <p>
	 * 操作完成后的list长度.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param values
	 * @return
	 */
	Long rpush(String key, Object... values);

	/**
	 * 在名称为key的list头添加一个值为value的 元素.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param values
	 * @return
	 */
	Long lpush(String key, Object... values);

	/**
	 * 返回名称为key的list的长度.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Long llen(String key);

	/**
	 * 返回名称为key的list中start至end之间的元素(下标从0开始).
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	List<Object> lrange(String key, long start, long end);

	/**
	 * 截取名称为key的list,保留start至end之间的元素.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	String ltrim(String key, long start, long end);

	/**
	 * 返回名称为key的list中index位置的元素.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param index
	 * @return
	 */
	Object lindex(String key, long index);

	/**
	 * 给名称为key的list中index位置的元素赋值为value.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param index
	 * @param value
	 * @return
	 */
	String lset(String key, long index, Object value);

	/**
	 * 删除count个名称为key的list中值为value的元素.
	 * <p>
	 * count=0,删除所有值为value的元素;<br>
	 * count>0,从头至尾删除count个值为value的元素;<br>
	 * count<0,从尾到头删除count个值为value的元素.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param count
	 * @param value
	 * @return
	 */
	Long lrem(String key, long count, Object value);

	/**
	 * 返回并删除名称为key的list中的首元素.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Object lpop(String key);

	/**
	 * 返回并删除名称为key的list中的尾元素.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Object rpop(String key);

	/**
	 * 向名称为key的set中添加元素member i.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param members
	 * @return
	 */
	Long sadd(String key, Object... members);

	/**
	 * 返回名称为key的set的所有元素.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Set<Object> smembers(String key);

	/**
	 * 删除名称为key的set中的元素member i.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param members
	 * @return
	 */
	Long srem(String key, Object... members);

	/**
	 * 随机返回并删除名称为key的set中一个元素.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Object spop(String key);

	/**
	 * 返回名称为key的set的基数.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Long scard(String key);

	/**
	 * 测试member是否是名称为key的set的元素.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param member
	 * @return
	 */
	Boolean sismember(String key, String member);

	/**
	 * 随机返回名称为key的set的一个元素.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Object srandmember(String key);

	/**
	 * 向名称为key的zset中添加元素member,score用于排序.
	 * <p>
	 * 如果该元素已经存在,则根据score更新该元素的顺序.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	Long zadd(String key, double score, Object member);

	/**
	 * 向名称为key的zset中添加元素member i,score i用于排序.
	 * <p>
	 * 如果该元素已经存在,则根据score更新该元素的顺序.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param scoreMembers
	 * @return
	 */
	Long zadd(String key, Map<Double, Object> scoreMembers);

	/**
	 * 删除名称为key的zset中的元素member i.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param members
	 * @return
	 */
	Long zrem(String key, Object... members);

	/**
	 * 如果在名称为key的zset中已经存在元素member,则该元素的score增加increment;<br>
	 * 否则向集合中添加该元素,其score的值为increment.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	Double zincrby(String key, double score, Object member);

	/**
	 * 返回名称为key的zset的基数.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	Long zcard(String key);

	/**
	 * 返回名称为key的zset中元素element的score.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param member
	 * @return
	 */
	Double zscore(String key, Object member);

	/**
	 * 返回名称为key的zset中score >= min且score <= max的元素个数.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */

	Long zcount(String key, double min, double max);

	/**
	 * 返回名称为key的zset(元素已按score从小到大排序)中member元素的rank(即index,从0开始),<br>
	 * 若没有member元素,返回 "nil".
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param member
	 * @return
	 */
	Long zrank(String key, Object member);

	/**
	 * 返回名称为key的zset(元素已按score从大到小排序)中member元素的rank(即index,从0开始),<br>
	 * 若没有member元素,返回 "nil".
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param member
	 * @return
	 */
	Long zrevrank(String key, Object member);

	/**
	 * 返回名称为key的zset(元素已按score从小到大排序)中的index从start到end的所有元素.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	Set<Object> zrange(String key, long start, long end);

	/**
	 * 返回名称为key的zset(元素已按score从大到小排序)中的index从start到end的所有元素.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	Set<Object> zrevrange(String key, long start, long end);

	/**
	 * 返回名称为key的zset中score >= min且score <= max的所有元素(元素已按score从小到大排序).
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	Set<Object> zrangeByScore(String key, double min, double max);

	/**
	 * 返回名称为key的zset中score >= min且score <= max的所有元素(元素已按score从大到小排序).
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param max
	 * @param min
	 * @return
	 */
	Set<Object> zrevrangeByScore(String key, double max, double min);

	/**
	 * 返回名称为key的zset中score >= min且score <= max的count个元素(元素已按score从小到大排序).
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 * @param count
	 * @return
	 */
	Set<Object> zrangeByScore(String key, double min, double max, int offset, int count);

	/**
	 * 返回名称为key的zset中score >= min且score <= max的count个元素(元素已按score从大到小排序).
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param min
	 * @param max
	 * @param offset
	 * @param count
	 * @return
	 */
	Set<Object> zrevrangeByScore(String key, double max, double min, int offset, int count);

	/**
	 * 排序.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @return
	 */
	List<Object> sort(String key);

	/**
	 * 排序.
	 * 
	 * @author wangtao 2013-11-29
	 * @param key
	 * @param sortingParameters
	 * @return
	 */
	List<Object> sort(String key, SortingParams sortingParameters);
}
