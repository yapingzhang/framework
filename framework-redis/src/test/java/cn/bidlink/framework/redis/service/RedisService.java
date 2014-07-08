/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>RedisService.java</p>
 *   
 */
package cn.bidlink.framework.redis.service;

import cn.bidlink.framework.redis.pojo.AuthUser;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
public interface RedisService {

	String a();

	AuthUser b(AuthUser authUser);

	int c(int a, int b, int c);

	AuthUser d(AuthUser authUser, int a);

	AuthUser e(AuthUser authUser, int a);

	AuthUser f(AuthUser pojo);

	int g(int a, int b, int c);

	int h(int a, int b, int c);
}
