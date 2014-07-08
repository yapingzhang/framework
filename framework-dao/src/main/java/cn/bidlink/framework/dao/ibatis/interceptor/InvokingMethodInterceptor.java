package cn.bidlink.framework.dao.ibatis.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import cn.bidlink.framework.dao.ibatis.impl.IbatisCommonDaoImpl;


public class InvokingMethodInterceptor implements MethodInterceptor{
	
	protected IbatisCommonDaoImpl commonDao;
	


	public IbatisCommonDaoImpl getCommonDao() {
		return commonDao;
	}
 
	public void setCommonDao(IbatisCommonDaoImpl commonDao) {
		this.commonDao = commonDao;
	}




	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		Object target = invocation.getThis();
		if(method!=null){
			if(commonDao!=null && commonDao instanceof IbatisCommonDaoImpl){
				((IbatisCommonDaoImpl)commonDao).setTarget(target);
				((IbatisCommonDaoImpl)commonDao).setInvokingMethod(method);
			}
		}
		Object result = invocation.proceed();
		if(method!=null){
			if(commonDao!=null && commonDao instanceof IbatisCommonDaoImpl){
				((IbatisCommonDaoImpl)commonDao).setTarget(null);
				((IbatisCommonDaoImpl)commonDao).setInvokingMethod(null);
			}
		}
		return result;
	}

}
