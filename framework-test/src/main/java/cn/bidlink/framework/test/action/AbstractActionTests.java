/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *   
 */
package cn.bidlink.framework.test.action;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import cn.bidlink.framework.test.AbstractPowerMockTests;

/**
 * @description: Junit测试基类(action),不依赖spring.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-9-30 下午04:40:55
 */
public abstract class AbstractActionTests extends AbstractPowerMockTests {
	protected MockHttpServletRequest mockRequest;
	protected MockHttpServletResponse mockResponse;
	protected MockHttpSession mockSession;

	/**
	 * 初始化MockHttp,测试方法执行时收到调用.
	 * 
	 * @author wangtao 2013-9-30
	 */
	protected void initMockHttp() {
		mockRequest = new MockHttpServletRequest();
		mockResponse = new MockHttpServletResponse();
		mockSession = new MockHttpSession();
	}
}
