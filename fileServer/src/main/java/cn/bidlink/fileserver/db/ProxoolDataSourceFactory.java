package cn.bidlink.fileserver.db;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.logicalcobwebs.proxool.ProxoolDataSource;

/**
 * 
 * @author changzhiyuan
 * @date 2013-2-28
 * @desc proxool配置
 * 
 */
public class ProxoolDataSourceFactory implements DataSourceFactory {

	private ProxoolDataSource dataSource = null;

	public ProxoolDataSourceFactory() {
		this.dataSource = new ProxoolDataSource();
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setProperties(Properties properties) {

		dataSource.setDriver((String) properties.get("driver"));
		dataSource.setDriverUrl((String) properties.get("driverUrl"));
		dataSource.setUser((String) properties.get("user"));
		dataSource.setPassword((String) properties.get("password"));
		dataSource.setAlias("alias");

		if (properties.containsKey("house-keeping-sleep-time")) {
			dataSource.setHouseKeepingSleepTime(Integer.parseInt(properties
					.get("house-keeping-sleep-time").toString()));
		}
		if (properties.containsKey("house-keeping-test-sql")) {
			dataSource.setHouseKeepingTestSql(properties.get(
					"house-keeping-test-sql").toString());
		}
		// 所以你需要设置这个时间大于预计最慢响应的时间(默认时间是5分钟)。
		if (properties.containsKey("maximum-active-time")) {
			dataSource.setMaximumActiveTime(Integer.parseInt(properties.get(
					"maximum-active-time").toString()));
		}
		// 数据库最大连接数（默认值为15）
		if (properties.containsKey("maximum-connection-count")) {
			dataSource.setMaximumConnectionCount(Integer.parseInt(properties
					.get("maximum-connection-count").toString()));
		}
		// 一个连接存在的最长保持活动的时间。默认值是4小时，单位是毫秒。
		if (properties.containsKey("maximum-connection-lifetime")) {
			dataSource.setMaximumConnectionLifetime(Integer.parseInt(properties
					.get("maximum-connection-lifetime").toString()));
		}
		// 最小连接保持打开的个数，不管是否需要，默认值是5个。
		if (properties.containsKey("minimum-connection-count")) {
			dataSource.setMaximumConnectionLifetime(Integer.parseInt(properties
					.get("minimum-connection-count").toString()));
		}
		// 这个帮助我们确定连接池的状态。如果在这个时间阀值内（单位为毫秒）拒绝了一个连接，就认为是过载了。默认时间60秒。
		if (properties.containsKey("overload-without-refusal-lifetime")) {
			dataSource.setMaximumConnectionLifetime(Integer.parseInt(properties
					.get("overload-without-refusal-lifetime").toString()));
		}
	}
}