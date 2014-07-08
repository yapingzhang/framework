/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.test.dao;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @description: Dao测试基类,配置文件以"-test.xml"结尾.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-9-30 下午04:40:55
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:*-test.xml")
public abstract class AbstractDaoTests {

	@Autowired
	private DataSource dataSource;

	@Before
	public void onSetUp() throws Exception {
		String[] files = getDbUnitDataFiles();
		if (files != null && files.length > 0) {
			FixturesHelper.reloadAllTable(dataSource, files);
		}
	}

	/**
	 * 该类提供了单元测试需要加载的数据文件的路径,可以执行多个数据文件.
	 * 
	 * @author wangtao 2013-10-8
	 * @return
	 */
	protected abstract String[] getDbUnitDataFiles();

	/**
	 * 设置数据源.
	 * 
	 * @author wangtao 2013-9-30
	 * @param dataSource
	 *            数据源
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
