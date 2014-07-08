/*
 * Copyright (c) 2001-2013 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 */
package cn.bidlink.framework.test;

import static org.mockito.Matchers.any;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

/**
 * @description: 静态方法例子.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-9-30 下午12:58:39
 */
@PrepareForTest({ StaticClass.class })
public class StaticClassTest extends AbstractPowerMockTests {
	/**
	 * 模拟 private的方法.
	 * 
	 * @author wangtao 2013-10-8
	 * @throws Exception
	 */
	@Test
	public void testPrivateMethod() throws Exception {
		StaticClass spy = PowerMockito.spy(new StaticClass());
		PowerMockito.doReturn(3).when(spy, "privateMethod", 1);
		Assert.assertEquals(3, spy.testPrivateMethod(1));
		PowerMockito.verifyPrivate(spy, Mockito.times(1)).invoke("privateMethod", 1);
	}

	/**
	 * 模拟 静态有返回值的方法.
	 * 
	 * @author wangtao 2013-10-8
	 * @throws Exception
	 */
	@Test
	public void testStaticReturnMethod() throws Exception {
		PowerMockito.mockStatic(StaticClass.class);
		Mockito.when(StaticClass.staticReturnMethod()).thenReturn(2);
		Assert.assertEquals(2, StaticClass.staticReturnMethod());
	}

	/**
	 * 模拟 不执行void的方法.
	 * 
	 * @author wangtao 2013-10-8
	 * @throws Exception
	 */
	@Test
	public void testVoidMethod() throws Exception {
		StaticClass spy = PowerMockito.spy(new StaticClass());
		PowerMockito.doNothing().when(spy).voidMethod();
		spy.voidMethod();
	}

	/**
	 * 模拟 不执行没参数的静态void的方法.
	 * 
	 * @author wangtao 2013-10-8
	 * @throws Exception
	 */
	@Test
	public void testStaticMethod1() throws Exception {
		PowerMockito.mockStatic(StaticClass.class);
		PowerMockito.doNothing().when(StaticClass.class, "staticVoidMethod");
		StaticClass.staticVoidMethod();
	}

	/**
	 * 模拟 不执行带参数的静态void的方法.
	 * 
	 * @author wangtao 2013-10-8
	 * @throws Exception
	 */
	@Test
	public void testStaticMethod2() throws Exception {
		PowerMockito.mockStatic(StaticClass.class);
		PowerMockito.doNothing().when(StaticClass.class, "staticMethod", "123");
		StaticClass.staticMethod("123");
		PowerMockito.doNothing().when(StaticClass.class, "staticMethod", Mockito.anyString());
		StaticClass.staticMethod("456");
	}

	@Test
	public void testBooleanX() throws Exception {
		StaticClass spy = PowerMockito.spy(new StaticClass());
		PowerMockito.doReturn(true).when(spy, "booleanX", any(Boolean.class));
		Assert.assertTrue(spy.getBooleanX());

	}
}

/**
 * @description: 测试类.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2013-10-8 下午01:34:22
 */
class StaticClass {

	private final int privateMethod(int a) {
		return a;
	}

	public int testPrivateMethod(int a) {
		return privateMethod(a);
	}

	public static int staticReturnMethod() {
		return 1;
	}

	void voidMethod() {
		throw new IllegalStateException("should not go here");
	}

	public static void staticVoidMethod() {
		throw new IllegalStateException("should not go here");
	}

	public static void staticMethod(String msg) {
		throw new IllegalStateException(msg);
	}

	Boolean booleanX(Boolean b) {
		return b;
	}

	public Boolean getBooleanX() {
		return booleanX(false);
	}
}