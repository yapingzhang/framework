package cn.bidlink.framework.core.support.jmx.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import cn.bidlink.framework.core.support.jmx.FrameworkBeanStatus;
import cn.bidlink.framework.core.support.rmi.BidRmiProxyFactoryBean;

/**
 * 
 * @description:	通用管理类
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2013-1-8 上午11:48:56
 */
//暂时注释掉。方便测试，以后使用自己配置在xml中
//@Component
public class FrameworkBeanStatusImpl implements ApplicationContextAware,FrameworkBeanStatus{

	private XmlWebApplicationContext applicationContext;
	
	public int getTotalBean() {
		return applicationContext.getBeanDefinitionCount();
	}
	
	public String [] getBeanDefinitions() {
		return applicationContext.getBeanDefinitionNames();
	}
	
	/**
	 * 
	 * @description  【注】返回对象必需实现序列化
	 * @param beanName
	 * @return
	 */
	public Object getBean(String beanName) {
		if (applicationContext.containsBean(beanName)) {
			return applicationContext.getBean(beanName);
		}
		return null;
	}
	
	
	public int getRmiWireErrorStubCount() {
	     return BidRmiProxyFactoryBean.getCacheErrorStub().size();
	}
	
	public int getRmiWireCacheAllStubCount() {
	     return BidRmiProxyFactoryBean.getCacheAllStubMap().size();
	}
	
    
	public void deploySingleBean(String beanName) {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getBeanFactory();
		if(beanFactory.containsSingleton(beanName)) {
			beanFactory.destroySingleton(beanName);
		}
	}
	
	public void deployScopedBean(String beanDefineName) {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getBeanFactory();
		if(beanFactory.containsBeanDefinition(beanDefineName)) {
			beanFactory.destroyScopedBean(beanDefineName);
		}
	}
	
	public void registerSingleton(String beanName,Object singletonObject) {
		if(!applicationContext.containsBean(beanName)) {
			applicationContext.getBeanFactory().registerSingleton(beanName, singletonObject);
		}
 
	}
	
	public void registerBeanDefined(String beanName,BeanDefinition beanDefinition) {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)applicationContext.getBeanFactory();
		if (!applicationContext.containsBeanDefinition(beanName)) {
			beanFactory.registerBeanDefinition(beanName, beanDefinition);
		}
	}
	
 
	public Object invokerMethod(String beanName,String methodName,Class<?>[] paramterType,Object [] params) throws Exception {
            if(applicationContext.containsBean(beanName)) {
            	Object obj = applicationContext.getBean(beanName);
            	Method m = ReflectionUtils.findMethod(obj.getClass(), methodName, paramterType);
            	if (m != null) {
            		m.setAccessible(true);
            		return m.invoke(obj, params);
            	}
            }
            return null;
	}
	
	public Object getFieldValue(String beanName,String fieldName) throws Exception {
		 if(applicationContext.containsBean(beanName)) {
         	Object obj = applicationContext.getBean(beanName);
         	Field field = ReflectionUtils.findField(obj.getClass(), fieldName);
         	if (field != null) {
         		field.setAccessible(true);
         		return field.get(obj);
         	}
         }
         return null;
	}
 
	
 
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		 this.applicationContext = (XmlWebApplicationContext)applicationContext;
	}
}

 
 