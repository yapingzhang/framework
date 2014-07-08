/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>ExtendedTest.java</p>
 *   
 */
package cn.bidlink.framework.log4j;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
import org.apache.log4j.Logger;

import cn.bidlink.framework.log4j.level.ExtendedLevel;

public class ExtendedTest {
	transient Logger logger = Logger.getLogger(getClass());
	// @Test
	public void testApp() {
		for (int i = 0; i < 10; i++) {
			//MDC.put("1111", new Integer(0));
			logger.log(ExtendedLevel.OPERATE_LOG_LEVEL, "ExpansionLevelTest" + i);
		}
	}
}
