/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.redis.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cn.bidlink.framework.redis.pojo.AuthUser;

/**
 * 判断是否为空测试.
 * 
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-12-3 下午03:27:51
 */
public class UtilsTest {
	@Test
	public void testObject() throws Exception {
		Object obj = null;
		Assert.assertTrue(EmptyUtils.isEmpty(obj));
		obj = new AuthUser(1245678L);
		Assert.assertTrue(!EmptyUtils.isEmpty(obj));
		Object[] objs = {};
		Assert.assertTrue(EmptyUtils.isEmpty(objs));
	}

	@Test
	public void testString() throws Exception {
		String str = null;
		Assert.assertTrue(EmptyUtils.isEmpty(str));
		str = "  ";
		Assert.assertTrue(EmptyUtils.isEmpty(str));
		str = " x ";
		Assert.assertTrue(!EmptyUtils.isEmpty(str));
		String[] strs = {};
		Assert.assertTrue(EmptyUtils.isEmpty(strs));
	}

	@Test
	public void testList() throws Exception {
		List<String> list = null;
		Assert.assertTrue(EmptyUtils.isEmpty(list));
		list = new ArrayList<String>();
		Assert.assertTrue(EmptyUtils.isEmpty(list));
		list.add("Test");
		Assert.assertTrue(!EmptyUtils.isEmpty(list));
	}

}
