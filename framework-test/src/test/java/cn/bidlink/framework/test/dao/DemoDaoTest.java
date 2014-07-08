/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *   
 */
package cn.bidlink.framework.test.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.bidlink.framework.test.model.Demo;

/**
 * @description: Demo Dao测试.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-9-30 下午12:58:39
 */
public class DemoDaoTest extends AbstractDaoTests {

	@Autowired
	protected DemoDao demoDao;

	@Test
	public void testSave() throws Exception {
		Demo demo = new Demo();
		demo.setName("name-test");
		demo.setPhone("phone-test");
		demo.setAddress("address-test");
		demo.setReview(false);
		int ret = this.demoDao.save(demo);
		assertEquals(1, ret);

	}

	@Override
	protected String[] getDbUnitDataFiles() {
		return new String[] { "classpath:data/demo_testdata.xml" };
	}

}
