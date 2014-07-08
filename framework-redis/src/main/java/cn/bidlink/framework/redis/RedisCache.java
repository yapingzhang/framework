/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>RedisCache.java</p>
 *   
 */
package cn.bidlink.framework.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
@Deprecated
public interface RedisCache {
	/**
	 * 取出缓存的字符串，只能与setString()配合使用。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */
	public String getString(String key);

	/**
	 * 缓存一个字符串，只能与getString()配合使用。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setString(final String key, String value);

	/**
	 * 缓存一个字符串，只能与getString()配合使用。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param seconds
	 *            过期时间，单位为秒（0和负数表示不设置过期）
	 * @param value
	 * @return
	 */
	public boolean setString(final String key, int seconds, String value);

	/**
	 * 取出缓存的对象，只能与setObject()配合使用。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */
	public Object getObject(String key);

	/**
	 * 缓存一个对象，只能与getObject()配合使用。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param value
	 *            可序列化对象（实现Serializable接口）
	 * @return
	 */
	public boolean setObject(final String key, Object value);

	/**
	 * 缓存一个对象，只能与getObject()配合使用。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param seconds
	 *            过期时间，单位为秒（0和负数表示不设置过期）
	 * @param value
	 *            可序列化对象（实现Serializable接口）
	 * @return
	 */
	public boolean setObject(final String key, int seconds, Object value);

	/**
	 * 删除某个缓存k-v对。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */
	public long del(String key);

	/**
	 * 一次删除多个k-v对。
	 * 
	 * @Description: TODO add description
	 * @param keys
	 * @return
	 */
	public long del(List<String> keys);

	/**
	 * 对key设置过期时间。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param seconds
	 * @return
	 */
	public long expire(String key, int seconds);

	/**
	 * 对key设置过期时间。
	 * 
	 * @Description: TODO add description
	 * @param map
	 *            <String, Integer>
	 * @return
	 */
	public long expire(Map<String, Integer> map);

	/**
	 * 查询某key是否存在。只要数据仍在有效期内，有将返回true。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */

	public boolean exists(String key);

	/**
	 * 对key做自增1，与decr（）配合使用。<br/>
	 * 如果key不存在，则在执行此操作前设置默认值为0。<br/>
	 * 如果key存在，且key是十进制数值的字符串表示(e.g: "-123L")，则在些数据基础上自增(e.g:return -122L)。<br/>
	 * 如果key存在，且key不是十进制数值的字符串表示，则返回null。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return 自增后的Long值，如果操作失败返回null
	 */
	public Long incr(String key);

	/**
	 * 对key做自减1，与incr（）配合使用。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */
	public Long decr(String key);

	/**
	 * 对原value后进行数据追加。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param value
	 * @return
	 */
	public Long append(String key, String value);

	/**
	 * 往map里放数据 a=new hashmap put user1 ,u fds
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public Long hset(String key, String field, Object value);

	/**
	 * 从map里取数据。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param field
	 * @return
	 */
	public Object hget(String key, String field);

	/**
	 * 获取所有map数据。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */
	public Map<String, Object> hgetAll(String key);

	/**
	 * 获取某hashmap中的items数。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */
	public Long hlen(String key);

	/**
	 * 获取某hashmap下的所有key。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key);

	/**
	 * 判断某hashmap下是否包含某field。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param field
	 * @return
	 */
	public Boolean hexists(String key, String field);

	/**
	 * 在list左端新增Object类型的item。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param item
	 * @return 操作完成后的list长度
	 */
	public Long lpush(String key, Object item);

	/**
	 * 在list右端新增Object类型的item。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param item
	 * @return 操作完成后的list长度
	 */
	public Long rpush(String key, Object item);

	/**
	 * 在list的最左端以Object的方式，取出并删除一个item。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */
	public Object lpop(String key);

	/**
	 * 在list的最右端以Object的方式，取出并删除一个item。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */
	public Object rpop(String key);

	/**
	 * 以Object的方式取出list的index（从左到右从0开始计数）位上item。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param index
	 * @return
	 */
	public Object lindex(String key, int index);

	/**
	 * 从指定的范围内返回序列的元素 (返回序列 start->end 元素)。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Object> lrange(String key, int start, int end);

	/**
	 * 修剪某个范围之外的数据 (保留 start->end 元素，其余的删除)。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public boolean ltrim(String key, int start, int end);

	/**
	 * 设置list的index位置上的值（Object类型）。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param index
	 * @param value
	 * @return
	 */
	public boolean lset(String key, int index, Object value);

	/**
	 * 获取某list的长度。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */
	public Long llen(String key);

	/**
	 * 往集合中插入member。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param members
	 * @return
	 */
	public Long sadd(String key, Object members);

	/**
	 * 删除集合中的member成员。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param members
	 * @return
	 */
	public Long srem(String key, Object members);

	/**
	 * Get the number of members in a set
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */
	public Long scard(String key);

	/**
	 * Get all the members in a set
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */
	public Set<Object> smembers(String key);

	/**
	 * 判断某个member是否在此集合中。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param member
	 * @return
	 */
	public Boolean sismemberObject(String key, Object member);

	/**
	 * Remove and return a random member from a set。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */
	public Object spop(String key);

	/**
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	public Long zadd(String key, double score, Object member);

	/**
	 * 
	 * 从有序集合中删除指定元素。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param member
	 * @return 0，未找到该元素删除不成功； 1，找到并删除成功
	 */
	public Long zrem(String key, Object member);

	/**
	 * 获取sorted set 中元素个数。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @return
	 */
	public Long zcard(String key);

	/**
	 * 返回元素在有序集合（从小到大）中的序号（从0开始）。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param member
	 * @return 元素存在时返回序号，元素不存在时返回null
	 */
	public Long zrank(String key, Object member);

	/**
	 * Sort a Set or a List。
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param patterns
	 * @return
	 */
	public List<Object> sort(String key, String... patterns);

	/**
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param sorting
	 *            排序:1=升序；2=降序
	 * @param patterns
	 * @return
	 */
	public List<Object> sort(String key, int sorting, String... patterns);

	/**
	 * 
	 * @Description: TODO add description
	 * @param key
	 * @param sorting
	 *            排序:1=升序；2=降序
	 * @param start
	 *            开始条数
	 * @param count
	 *            总条数
	 * @param patterns
	 * @return
	 */
	public List<Object> sort(String key, int sorting, int start, final int count, String... patterns);

	public String hmset(String key, Map<String, Object> hash);

	public List<Object> hmget(String key, String... fields);
}
