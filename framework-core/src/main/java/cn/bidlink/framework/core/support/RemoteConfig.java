package cn.bidlink.framework.core.support;

import java.util.HashMap;
import java.util.Map;
 /**
 * @description:	 TODO add description
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-8-29		上午9:45:31
 *
 */
public class RemoteConfig {
 
	private String defaultRmiGroupKey = "";
	
	private String defaultWsGroupKey = "";
	
	private Map<String, Object> groupConfigMap = new HashMap<String, Object>();
 
	public String getDefaultRmiGroupKey() {
		return defaultRmiGroupKey;
	}

	public void setDefaultRmiGroupKey(String defaultRmiGroupKey) {
		this.defaultRmiGroupKey = defaultRmiGroupKey;
	}

	public String getDefaultWsGroupKey() {
		return defaultWsGroupKey;
	}

	public void setDefaultWsGroupKey(String defaultWsGroupKey) {
		this.defaultWsGroupKey = defaultWsGroupKey;
	}

	public Map<String, Object> getGroupConfigMap() {
		return groupConfigMap;
	}

	public void setGroupConfigMap(Map<String, Object> groupConfigMap) {
		this.groupConfigMap = groupConfigMap;
	}

     
 
	 
 
}

  
