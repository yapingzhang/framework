package cn.bidlink.framework.core.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import cn.bidlink.framework.core.annotation.PublishService;
import cn.bidlink.framework.core.enums.ServiceType;
import cn.bidlink.framework.core.exceptions.GeneralException;
import cn.bidlink.framework.core.exceptions.InnerErrorException;
import cn.bidlink.framework.core.support.cxf.WsCxfPublish;
import cn.bidlink.framework.core.support.rmi.RmiPublish;
import cn.bidlink.framework.core.support.xfile.WsXfilePublish;

public class BidRemoteServicePublisher implements BeanPostProcessor,
		ApplicationContextAware, Ordered {
 
	private XmlWebApplicationContext applicationContext = null;
	
	/**
	 * 应用上下文路径
	 */
	private String contextPath;
	
	/**
	 * only publish serviceType 只发布指定类别接口
	 */
	private ServiceType publishType;
	
	/**
	 * 合法ip
	 */
	private List<String> allowIps = new ArrayList<String>();
	
	/**
	 * 是否启用安全检查
	 */
	private boolean isEnabledAuth = false;
	
	/**
	 * 是否启用SSL
	 */
	private boolean isEnabledSsl = false;
	/**
	 * 认证信息
	 */
	private AuthInfo authInfo;
	

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {

			Class <?> tclsImpl = null;
			if (AopUtils.isAopProxy(bean) || AopUtils.isCglibProxy(bean)) {
				tclsImpl = AopUtils.getTargetClass(bean);
			} else {
				tclsImpl = bean.getClass();
			}
		    PublishService rs = AnnotationUtils.findAnnotation(tclsImpl,PublishService.class);
            if (rs == null) {
            	return bean;
            }
			Class<?>[] clses = ClassUtils.getAllInterfaces(bean);
 			Class<?> interfaceCls = null;
			if (clses == null) {
				throw new GeneralException(bean.getClass().getName()+ " is not exist interface");
			} else if (clses.length > 1) {
				boolean flag = false;
			    for (Class<?> c : clses) {
			    	if(tclsImpl.getSimpleName().contains(c.getSimpleName())) {
			    		interfaceCls = c;
			    		flag = true;
						break;
			    	}
				}
			    if (!flag) {
			    	throw new GeneralException(bean.getClass().getName()
							+ " remote service interface must only!");
			    }
			
			} else {
				 interfaceCls = clses[0];
			}
  			ServiceType[] sTypes = rs.serviceTypes();
			publishService(sTypes, bean, interfaceCls);
		return bean;
	}
	
	
	public void publishService(ServiceType[] sTypes, Object obj,
			Class<?> interfaceCls) {
		for (ServiceType sType : sTypes) {
			 if (publishType != null && publishType == sType) {
				 getPublish(sType).publishService(obj, interfaceCls);
			 } 
             if (publishType == null){//发布所有接口
				 getPublish(sType).publishService(obj, interfaceCls);
			 }
		}
	}

 
	/**
	 * 
	 * @description  取得发布嚣
	 * @param   serviceType 发布类别
	 * @return  Publish DOM对象
	 * @throws
	 */
	public Publish getPublish(ServiceType serviceType) {
		if (ServiceType.RMI == serviceType) {
             return RmiPublish.getInstance(applicationContext,contextPath,allowIps,isEnabledAuth,isEnabledSsl,authInfo);
		} else if (ServiceType.WS_CXF == serviceType) {
             return WsCxfPublish.getInstance(applicationContext);
		} else if (ServiceType.WS_XFILE == serviceType) {
			return WsXfilePublish.getInstance(applicationContext);
		} else {
			throw new InnerErrorException("system not support the serviceType!");
		}
		
	}

	
	public void setPublishType(ServiceType publishType) {
		this.publishType = publishType;
	}

	public List<String> getAllowIps() {
		return allowIps;
	}

	public void setAllowIps(List<String> allowIps) {
		this.allowIps = allowIps;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getContextPath() {
		return contextPath;
	}

	public boolean isEnabledAuth() {
		return isEnabledAuth;
	}

	public void setEnabledAuth(boolean isEnabledAuth) {
		this.isEnabledAuth = isEnabledAuth;
	}

	public AuthInfo getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(AuthInfo authInfo) {
		this.authInfo = authInfo;
	}

	public boolean isEnabledSsl() {
		return isEnabledSsl;
	}

	public void setEnabledSsl(boolean isEnabledSsl) {
		this.isEnabledSsl = isEnabledSsl;
	}

	
	@Override
	public int getOrder() {
		return Integer.MAX_VALUE-50;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = (XmlWebApplicationContext) applicationContext;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

}
