/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.bidlink.framework.test.service.ServiceExample1;
import cn.bidlink.framework.test.service.ServiceExample2;

/**
 * @description: 测试.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-9-30 下午12:58:39
 */
@Service
public class ServiceExample1Impl implements ServiceExample1 {
	@Autowired
	private ServiceExample2 serviceExample2;

	@Override
	public Long getLong() {
		return serviceExample2.getLong(null);
	}
}
