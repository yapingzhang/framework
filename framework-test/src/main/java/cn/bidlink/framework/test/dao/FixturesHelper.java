/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.test.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @description: 基于DBUnit初始化测试数据到Mysql数据库的工具类.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-9-30 下午04:37:58
 */
public class FixturesHelper {

	protected static Logger logger = Logger.getLogger(FixturesHelper.class);
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();

	/**
	 * 
	 */
	private FixturesHelper() {
	}

	/**
	 * 先删除数据库中所有表的数据,再插入XML文件中的数据到数据库.
	 * 
	 * @author wangtao 2013-9-30
	 * @param dataSource
	 *            数据源
	 * @param xmlFilePaths
	 *            符合Spring Resource路径格式的文件路径列表
	 * @throws Exception
	 */
	public static void reloadAllTable(DataSource dataSource, String... xmlFilePaths) throws Exception {
		deleteAllTable(dataSource);
		loadData(dataSource, xmlFilePaths);
	}

	/**
	 * 插入XML文件中的数据到数据库.
	 * 
	 * @author wangtao 2013-9-30
	 * @param dataSource
	 *            数据源
	 * @param xmlFilePaths
	 *            符合Spring Resource路径格式的文件路径列表
	 * @throws Exception
	 */
	public static void loadData(DataSource dataSource, String... xmlFilePaths) throws Exception {
		execute(DatabaseOperation.INSERT, dataSource, xmlFilePaths);
	}

	/**
	 * 先删除XML数据文件中涉及的表的数据, 再插入XML文件中的数据到数据库.<br>
	 * 在更新全部表时速度没有reloadAllTable快且容易产生锁死,适合只更新小部分表的数据的情况.
	 * 
	 * @author wangtao 2013-9-30
	 * @param dataSource
	 *            数据源
	 * @param xmlFilePaths
	 *            符合Spring Resource路径格式的文件路径列表
	 * @throws Exception
	 */
	public static void reloadData(DataSource dataSource, String... xmlFilePaths) throws Exception {
		execute(DatabaseOperation.CLEAN_INSERT, dataSource, xmlFilePaths);
	}

	/**
	 * 在数据库中删除XML文件中涉及的表的数据.
	 * 
	 * @author wangtao 2013-9-30
	 * @param dataSource
	 *            数据源
	 * @param xmlFilePaths
	 *            符合Spring Resource路径格式的文件路径列表
	 * @throws Exception
	 */
	public static void deleteData(DataSource dataSource, String... xmlFilePaths) throws Exception {
		execute(DatabaseOperation.DELETE_ALL, dataSource, xmlFilePaths);
	}

	/**
	 * 对XML文件中的数据在数据库中执行Operation.
	 * 
	 * @author wangtao 2013-9-30
	 * @param operation
	 *            操作类型
	 * @param dataSource
	 *            数据源
	 * @param xmlFilePaths
	 *            符合Spring Resource路径格式的文件路径列表
	 * @throws DatabaseUnitException
	 * @throws SQLException
	 */
	private static void execute(DatabaseOperation operation, DataSource dataSource, String... xmlFilePaths)
			throws DatabaseUnitException, SQLException {
		IDatabaseConnection connection = new DatabaseConnection(dataSource.getConnection(), null);
		for (String xmlPath : xmlFilePaths) {
			try {
				InputStream input = resourceLoader.getResource(xmlPath).getInputStream();
				IDataSet dataSet = new FlatXmlDataSetBuilder().setColumnSensing(true).build(input);
				operation.execute(connection, dataSet);
			} catch (IOException e) {
				logger.warn(xmlPath + " file not found", e);
			}
		}
	}

	/**
	 * 删除所有的表,excludeTables除外.<br>
	 * 在删除期间disable外键检查.
	 * 
	 * @author wangtao 2013-9-30
	 * @param dataSource
	 *            数据源
	 * @param excludeTables
	 *            例外的表
	 */
	public static void deleteAllTable(DataSource dataSource, String... excludeTables) {

		List<String> tableNames = new ArrayList<String>();
		try {
			ResultSet rs = dataSource.getConnection().getMetaData()
					.getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				if (Arrays.binarySearch(excludeTables, tableName) < 0) {
					tableNames.add(tableName);
				}
			}

			deleteTable(dataSource, tableNames.toArray(new String[tableNames.size()]));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 删除指定的表, 在删除期间disable外键的检查.
	 * 
	 * @author wangtao 2013-9-30
	 * @param dataSource
	 *            数据源
	 * @param tableNames
	 *            删除的表
	 */
	public static void deleteTable(DataSource dataSource, String... tableNames) {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		for (String tableName : tableNames) {
			template.update("DELETE FROM " + tableName);
		}
	}
}
