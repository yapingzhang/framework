/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>OperateMDC.java</p>
 *   
 */
package cn.bidlink.framework.log4j.utils;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.MDC;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
public class OperateMDC {
	public OperateMDC() {
		super();
		// MDC.clear();
	}

	/**
	 * 用户编号
	 * 
	 * @description: TODO add description
	 * @param userId
	 */
	public void setUserId(final String userId) {
		if (userId != null) {
			MDC.put("userId", userId);
		}
	}

	/**
	 * 用户名称
	 * 
	 * @description: TODO add description
	 * @param userName
	 */
	public void setUserName(final String userName) {
		if (userName != null) {
			MDC.put("userName", userName);
		}
	}

	/**
	 * 公司编号
	 * 
	 * @description: TODO add description
	 * @param companyId
	 */
	public void setCompanyId(final String companyId) {
		if (companyId != null) {
			MDC.put("companyId", companyId);
		}
	}

	/**
	 * 公司名称
	 * 
	 * @description: TODO add description
	 * @param companyName
	 */
	public void setCompanyName(final String companyName) {
		if (companyName != null) {
			MDC.put("companyName", companyName);
		}
	}

	/**
	 * 模块ID
	 * 
	 * @description: TODO add description
	 * @param modularId
	 */
	public void setModularId(final String modularId) {
		if (modularId != null) {
			MDC.put("modularId", modularId);
		}
	}

	/**
	 * 模块名称
	 * 
	 * @description: TODO add description
	 * @param modularName
	 */
	public void setModularName(final String modularName) {
		if (modularName != null) {
			MDC.put("modularName", modularName);
		}
	}

	/**
	 * 功能ID
	 * 
	 * @description: TODO add description
	 * @param functionId
	 */
	public void setFunctionId(final String functionId) {
		if (functionId != null) {
			MDC.put("functionId", functionId);
		}
	}

	/**
	 * 功能名称
	 * 
	 * @description: TODO add description
	 * @param functionName
	 */
	public void setFunctionName(final String functionName) {
		if (functionName != null) {
			MDC.put("functionName", functionName);
		}
	}

	/**
	 * 操作类型
	 * 
	 * @description: TODO add description
	 * @param operateType
	 */
	public void setOperateType(final OperateType operateType) {
		if (operateType != null) {
			MDC.put("operateType", operateType.value());
		}
	}

	/**
	 * 操作URL
	 * 
	 * @description: TODO add description
	 * @param operateUrl
	 */
	public void setOperateUrl(final String operateUrl) {
		if (operateUrl != null) {
			MDC.put("operateUrl", operateUrl);
		}
	}

	/**
	 * 操作sql
	 * 
	 * @description: TODO add description
	 * @param operateSql
	 */
	public void setOperateSql(final String operateSql) {
		if (operateSql != null) {
			MDC.put("operateSql", operateSql);
		}
	}

	/**
	 * 客户IP
	 * 
	 * @description: TODO add description
	 * @param clientIP
	 */
	public void setClientIP(final String clientIP) {
		if (clientIP != null) {
			MDC.put("clientIP", clientIP);
		}
	}

	/**
	 * 操作时间
	 * 
	 * @description: TODO add description
	 * @param createTime
	 */
	public void setOperateTime(final Date operateTime) {
		if (operateTime != null) {
			MDC.put("operateTime", operateTime);
		}
	}

	/**
	 * 自定义属性的值
	 * 
	 * @description: TODO add description
	 * @param otherProperty
	 */
	public void setOtherProperty(final Map<String, Object> otherProperty) {
		if (otherProperty != null) {
			MDC.put("otherProperty", otherProperty);
		}
	}
}
