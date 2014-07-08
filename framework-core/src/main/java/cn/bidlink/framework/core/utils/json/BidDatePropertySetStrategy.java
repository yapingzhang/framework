package cn.bidlink.framework.core.utils.json;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertySetStrategy;
 /**
 * @description:	 TODO add description
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-9-15		下午4:16:48
 *
 */
public class BidDatePropertySetStrategy extends PropertySetStrategy {
   
	private JsonConfig jsonConfig;
	
	public BidDatePropertySetStrategy(JsonConfig jsonConfig) {
       this.jsonConfig = jsonConfig;
	}
	
	private static Logger logger = Logger.getLogger(BidDatePropertySetStrategy.class);
	@Override
	public void setProperty(Object bean, String key, Object value)
			throws JSONException {
		try {
			  super.setProperty(bean, key, value,jsonConfig);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
 
	}

}

