package cn.bidlink.framework.core.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
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

/**
 * @description: add description
 * @since Ver 1.0
 * @author <a href="mailto:dejian.liu@ebnew.com">dejian.liu</a>
 * @Date 2012 2012-8-25 下午4:41:33
 * 
 */
@Deprecated
public class RemoteServicePublisher implements InitializingBean, Ordered,
		ApplicationContextAware {

	private static Logger logger = Logger.getLogger(RemoteServicePublisher.class);

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
	public void afterPropertiesSet() throws Exception {
		Map<String, Object> map = applicationContext.getBeansWithAnnotation(PublishService.class);
		for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, Object> entry = it.next();
			// String name = entry.getKey();
			Object obj = entry.getValue();
			Class<?>[] clses = ClassUtils.getAllInterfaces(obj);
 
			Class <?> tclsImpl = null;
			if (AopUtils.isAopProxy(obj) || AopUtils.isCglibProxy(obj)) {
				tclsImpl = AopUtils.getTargetClass(obj);
			} else {
				tclsImpl = obj.getClass();
			}
			
 			Class<?> interfaceCls = null;
			if (clses == null) {
				throw new GeneralException(obj.getClass().getName()+ " is not exist interface");
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
			    	throw new GeneralException(obj.getClass().getName()
							+ " remote service interface must only!");
			    }
			
			} else {
				 interfaceCls = clses[0];
			}
			Class<?> targetCls = obj.getClass();
			PublishService rs = AnnotationUtils.findAnnotation(targetCls,PublishService.class);
			ServiceType[] sTypes = rs.serviceTypes();
			publishService(sTypes, obj, interfaceCls);
		}
		

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

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE - 2;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = (XmlWebApplicationContext) applicationContext;
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

    
    
	
}
