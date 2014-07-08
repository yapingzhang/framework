/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *   
 */
package cn.bidlink.framework.test;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @description: Junit测试基类,不依赖spring.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-9-30 下午04:40:55
 */
@RunWith(PowerMockRunner.class)
public abstract class AbstractPowerMockTests {
	protected final Logger logger = Logger.getLogger(AbstractPowerMockTests.class);

	/**
	 * 初始化.
	 * 
	 * @author wangtao 2013-9-30
	 */
	@Before
	public void setUpBefore() {
		MockitoAnnotations.initMocks(this);
	}
}
