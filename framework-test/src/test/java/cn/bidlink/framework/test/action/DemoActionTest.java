/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.test.action;

import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import cn.bidlink.framework.test.service.ServiceExample1;

/**
 * @description: action 测试例子.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-9-30 下午05:50:22
 */
public class DemoActionTest extends AbstractActionTests {
	@InjectMocks
	private static DemoAction demoAction;

	@Mock
	private ServiceExample1 serviceExample1;

	/**
	 * 初始化.
	 * 
	 * @author wangtao 2013-9-30
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		demoAction = new DemoAction();
	}

	@Test
	public void testSend() {
		this.initMockHttp();
		mockRequest.setParameter("test", "xxx");
		Long thenRet = 2L;
		when(serviceExample1.getLong()).thenReturn(thenRet);
		Map<String, Object> result = demoAction.send(mockRequest);
		Assert.assertEquals("xxx", result.get("test1"));
		Assert.assertNotNull(result.get("test2"));
	}

}
