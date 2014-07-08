package cn.bidlink.framework.core.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

import cn.bidlink.framework.core.exceptions.GeneralException;
import cn.bidlink.framework.core.utils.ResourceUtils.ResourceComparator;

public abstract class PropertiesUtils {
	
	public static Properties loadProperties(String[] patterns) {
		return loadProperties(new ServletContextResourcePatternResolver(new DefaultResourceLoader()), patterns, null, null);
	}
	
	public static Properties loadProperties(String[] patterns, Properties[] properties) {
		return loadProperties(new ServletContextResourcePatternResolver(new DefaultResourceLoader()), patterns, null, properties);
	}
	
	public static Properties loadProperties(String[] patterns, Resource[] locations) {
		return loadProperties(new ServletContextResourcePatternResolver(new DefaultResourceLoader()), patterns, locations, null);
	}
	
	public static Properties loadProperties(Resource[] locations) {
		return loadProperties(new ServletContextResourcePatternResolver(new DefaultResourceLoader()), null, locations, null);
	}
	
	public static Properties loadProperties(Resource[] locations, Properties[] properties ) {
		return loadProperties(new ServletContextResourcePatternResolver(new DefaultResourceLoader()), null, locations, properties);
	}
	
	public static Properties loadProperties(String[] patterns, Resource[] locations, Properties[] properties) {
		return loadProperties(new ServletContextResourcePatternResolver(new DefaultResourceLoader()), patterns, null, null);
	}
	
	public static Properties loadProperties(ResourcePatternResolver resourcePatternResolver,String[] patterns) {
		return loadProperties(resourcePatternResolver, patterns, null, null);
	}
	
	public static Properties loadProperties(ResourcePatternResolver resourcePatternResolver,String[] patterns, Properties[] properties) {
		return loadProperties(resourcePatternResolver, patterns, null, properties);
	}
	
	public static Properties loadProperties(ResourcePatternResolver resourcePatternResolver,String[] patterns, Resource[] locations) {
		return loadProperties(resourcePatternResolver, patterns, locations, null);
	}
	
	public static Properties loadProperties(ResourcePatternResolver resourcePatternResolver,Resource[] locations) {
		return loadProperties(resourcePatternResolver, null, locations, null);
	}
	
	public static Properties loadProperties(ResourcePatternResolver resourcePatternResolver,Resource[] locations, Properties[] properties ) {
		return loadProperties(resourcePatternResolver, null, locations, properties);
	}
	
	@SuppressWarnings("unchecked")
	public static Properties loadProperties(ResourcePatternResolver resourcePatternResolver,String[] patterns, Resource[] locations, Properties[] properties) {
		//parse patterns
		List<Resource> resourceList = new ArrayList<Resource>();
		try {
			if(!ObjectUtils.isEmpty(patterns) && resourcePatternResolver != null) {
				for(String pattern : patterns) {
					List<Resource> perResourceList = new ArrayList<Resource>();
					Resource[] resources = resourcePatternResolver.getResources(pattern);
					if(!ObjectUtils.isEmpty(resources)) {
						CollectionUtils.mergeArrayIntoCollection(resources, perResourceList);
					}
					Collections.sort(perResourceList, new ResourceComparator());
					resourceList.addAll(perResourceList);
				}
			}
		} catch (Exception e) {
			throw new GeneralException("parse resource error",e);
		}
		///////
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setIgnoreResourceNotFound(true);
		propertiesFactoryBean.setLocalOverride(true);
		propertiesFactoryBean.setFileEncoding("UTF-8");
		propertiesFactoryBean.setSingleton(true);
		if(!ObjectUtils.isEmpty(locations)) {
			resourceList.addAll(CollectionUtils.arrayToList(locations));
		}
		if(properties != null) {
			propertiesFactoryBean.setPropertiesArray(properties);
		}
		propertiesFactoryBean.setLocations(resourceList.toArray(new Resource[resourceList.size()]));
		try {
			propertiesFactoryBean.afterPropertiesSet();
			return propertiesFactoryBean.getObject();
		} catch (IOException exception) {
			throw new GeneralException("load properties error", exception);
		}
	}
}
