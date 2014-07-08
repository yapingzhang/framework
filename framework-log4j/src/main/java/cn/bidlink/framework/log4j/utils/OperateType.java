/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>LoggingEventBson.java</p>
 *   
 */
package cn.bidlink.framework.log4j.utils;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
public enum OperateType {
	ADD(1), DELTE(2), UPDATE(3), QUERY(4);

	private Integer value;

	OperateType(final Integer value) {
		this.value = value;
	}

	public Integer value() {
		return value;
	}

}
