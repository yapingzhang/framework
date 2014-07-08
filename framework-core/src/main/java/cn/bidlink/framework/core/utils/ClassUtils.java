package cn.bidlink.framework.core.utils;

import cn.bidlink.framework.core.exceptions.GeneralException;


public abstract class ClassUtils extends org.springframework.util.ClassUtils{
	
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String className) {
		try {
			return (T)newInstance(Class.forName(className));
		} catch (ClassNotFoundException e) {
			throw new GeneralException(e);
		}
	}
	
	public static <T> T newInstance(Class<T> clazz) {
		try {
			return (T) clazz.newInstance();
		} catch (Exception exception) {
			throw new GeneralException(exception);
		}
	}
	
}
