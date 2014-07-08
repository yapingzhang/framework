/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>ShardedJedisPool.java</p>
 *   
 */
package cn.bidlink.framework.redis.config;

import java.util.ArrayList;
import java.util.List;

import cn.bidlink.framework.redis.exception.RedisException;

import redis.clients.jedis.JedisShardInfo;

/**
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-11-21 上午09:49:23
 */
public class RedisShardInfo {

	public RedisShardInfo(String shardInfo) {
		if (shardInfo == null || "".equals(shardInfo)) {
			throw new NullPointerException("framework-redis shardInfo");
		}
		String[] shardInfos = shardInfo.split(",");
		jedisShardInfo = new ArrayList<JedisShardInfo>();
		for (String info : shardInfos) {
			if (info == null || "".equals(info)) {
				continue;
			}
			String[] infoArray = info.split(":");
			if (infoArray.length == 3) {
				setJedisShardInfo(infoArray[0], Integer.parseInt(infoArray[1]), infoArray[2]);
			} else {
				setJedisShardInfo(infoArray[0], Integer.parseInt(infoArray[1]), null);
			}
		}
	}

	private void setJedisShardInfo(String host, int port, String auth) {
		JedisShardInfo jedisInfo = new JedisShardInfo(host, port);
		if (auth != null && !"".equals(auth))
			jedisInfo.setPassword(auth);
		if (jedisShardInfo == null) {
			jedisShardInfo = new ArrayList<JedisShardInfo>();
		}
		jedisShardInfo.add(jedisInfo);
	}

	private List<JedisShardInfo> jedisShardInfo;

	public List<JedisShardInfo> getJedisShardInfo() {
		if (jedisShardInfo == null || jedisShardInfo.size() == 0) {
			throw new RedisException("framework-redis jedisShardInfo");
		}
		return jedisShardInfo;
	}

}
