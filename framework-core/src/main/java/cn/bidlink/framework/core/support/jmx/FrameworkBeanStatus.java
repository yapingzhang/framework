package cn.bidlink.framework.core.support.jmx;

import org.springframework.beans.factory.config.BeanDefinition;


public interface FrameworkBeanStatus {

	public int getTotalBean();
	
	public String [] getBeanDefinitions();
	
	public Object getBean(String beanName);
	
	public void deploySingleBean(String beanName);
	
	public void deployScopedBean(String beanDefineName);
	
	public void registerSingleton(String beanName,Object singletonObject);
	
	public void registerBeanDefined(String beanName,BeanDefinition beanDefinition);
	
	public Object invokerMethod(String beanName,String methodName,Class<?>[] paramterType,Object [] params) throws Exception;
	
	public Object getFieldValue(String beanName,String fieldName) throws Exception;
	
	
}
