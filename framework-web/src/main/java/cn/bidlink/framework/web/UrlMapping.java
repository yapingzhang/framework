package cn.bidlink.framework.web;

import org.springframework.beans.factory.config.BeanDefinition;

public interface UrlMapping {
	
	public boolean isSupport(String beanName, BeanDefinition beanDefinition) throws Exception;

	public String[] generateUrls(String beanName, BeanDefinition beanDefinition) throws Exception;

}
