package cn.bidlink.framework.core.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cn.bidlink.framework.core.utils.ObjectUtils;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


public class ObjectMonitor implements MethodInterceptor{
	
	private boolean invoked = false;
	
	private boolean monitor = false;
	
	private boolean excludePrimitiveObjectMethod = true;
	
	private String[] excludedMethodNames;
	
	private Map<String, Object> excludedMethods = new HashMap<String, Object>();
	
	private static Object invokeLock = new Object();
	
	private static Object monitorLock = new Object();
	
	@SuppressWarnings("unchecked")
	public <T> T create(Class<T> clazz) {
		//初始化排除方法
		excludedMethods.clear();
		if(excludePrimitiveObjectMethod) {
			Method[] objMethods = Object.class.getDeclaredMethods();
			for(Method method : objMethods) excludedMethods.put(method.getName(), method);
		}
		
		if(!ObjectUtils.isEmpty(excludedMethodNames)) {
			for(String name : excludedMethodNames) excludedMethods.put(name, name);
		}
		//产生代理
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(this);
		return (T) enhancer.create();
	}
		
	@Override
	public Object intercept(Object thiz, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		synchronized (invokeLock) {
			if(monitor && excludedMethods.get(method.getName()) == null && !invoked) {
				invoked = true;
			}
		}
		return proxy.invokeSuper(thiz, args);
	}
	
	public void begin() {
		synchronized (monitorLock) {
			monitor = true;
		}
	}
	
	public void stop() {
		synchronized (monitorLock) {
			monitor = false;
		}
		synchronized (invokeLock) {
			invoked = false;
		}
	}

	public boolean isInvoked() {
		return invoked;
	}

	public boolean isExcludePrimitiveObjectMethod() {
		return excludePrimitiveObjectMethod;
	}

	public void setExcludePrimitiveObjectMethod(boolean excludePrimitiveObjectMethod) {
		this.excludePrimitiveObjectMethod = excludePrimitiveObjectMethod;
	}

	public String[] getExcludedMethodNames() {
		return excludedMethodNames;
	}

	public void setExcludedMethodNames(String[] excludedMethodNames) {
		this.excludedMethodNames = excludedMethodNames;
	}
}
