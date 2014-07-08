/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.redis.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import cn.bidlink.framework.redis.BaseRedisTest;

/**
 * TODO add description.
 * 
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-12-3 上午09:27:30
 */
public class AnnotationTest {
	transient Logger logger = Logger.getLogger(BaseRedisTest.class);
	AnnotationTest anno;

	@Delete(cache = @Cache(key = "ann_m1", simple = true))
	@Cache(key = "ann_m1", simple = true)
	public void m1() {
	}

	@Cache(key = "ann_m2", simple = true)
	@Delete(cache = @Cache(key = "ann_m2", simple = true))
	public void m2() {
	}

	@Before
	public void onSet() {
		anno = new AnnotationTest();
	}

	@Test
	public void testAnnotation() throws SecurityException, NoSuchMethodException {
		Method m1 = anno.getClass().getMethod("m1");
		Method m2 = anno.getClass().getMethod("m2");
		for (Annotation a : m1.getAnnotations()) {
			logger.info(a.toString());
		}
		logger.info("-------------------");
		for (Annotation a : m2.getAnnotations()) {
			logger.info(a.toString());
		}
	}
}
