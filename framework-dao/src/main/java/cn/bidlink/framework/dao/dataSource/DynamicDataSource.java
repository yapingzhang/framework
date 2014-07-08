package cn.bidlink.framework.dao.dataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;



/**
 * @description: 动态数据源
 * @since Ver 1.0
 * @author <a href="mailto:dejian.liu@ebnew.com">dejian.liu</a>
 * @Date 2012 2012-8-24 下午2:31:52
 * 
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	public static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>();

	@Override
	protected Object determineCurrentLookupKey() {
		return (String) CONTEXT_HOLDER.get();
	}
	
 
}
