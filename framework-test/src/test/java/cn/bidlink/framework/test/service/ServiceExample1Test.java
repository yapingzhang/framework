/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.test.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import cn.bidlink.framework.test.AbstractPowerMockTests;
import cn.bidlink.framework.test.service.impl.ServiceExample1Impl;

/**
 * @description: Service 测试 例子.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-9-30 下午12:56:47
 */
public class ServiceExample1Test extends AbstractPowerMockTests {
	@InjectMocks
	private static ServiceExample1 serviceExample1;

	@Mock
	private ServiceExample2 serviceExample2;

	/**
	 * 初始化.
	 * 
	 * @author wangtao 2013-9-30
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		serviceExample1 = new ServiceExample1Impl();
	}

	@Test
	public void getLongTest() {
		Long thenRet = 2L;
		when(serviceExample2.getLong(any(Long.class))).thenReturn(thenRet);
		Long result = serviceExample1.getLong();
		assertNotNull(result);
	}

}
