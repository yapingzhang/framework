package cn.bidlink.framework.core.support;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import cn.bidlink.framework.core.annotation.AutowiredService;
import cn.bidlink.framework.core.enums.ServiceType;
import cn.bidlink.framework.core.exceptions.InnerErrorException;
import cn.bidlink.framework.core.support.cxf.CxfAutoWire;
import cn.bidlink.framework.core.support.rmi.RmiAutowire;
 /**
 * @description: 远程服务自动适配
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-8-25		下午8:22:10
 */
//@Component
@Deprecated
public class RemoteServiceAutowrie implements ApplicationListener<ContextRefreshedEvent>,Ordered {
	private static Logger logger = Logger.getLogger(RemoteServiceAutowrie.class);
 
//	 @Autowired
	 private RemoteConfig remoteConfig;
 
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info(event.getClass().getName()+"---------bean load finished................"+event.getSource().getClass().getName());
		if (!(event.getSource() instanceof XmlWebApplicationContext)) {
			return;
		}
		XmlWebApplicationContext context = (XmlWebApplicationContext) event.getSource();
		String [] beanNames = context.getBeanDefinitionNames();
		for (String beanName : beanNames) {
			Object obj = context.getBean(beanName);
			Class<?> cls = obj.getClass();
			if(AopUtils.isAopProxy(obj) || AopUtils.isCglibProxy(obj)) {
				cls = AopUtils.getTargetClass(obj);
			}
			if(cls.getSimpleName().endsWith("ServiceImpl") 
					|| cls.getSimpleName().endsWith("Action")
					|| cls.getSimpleName().endsWith("Service")
					|| cls.getSimpleName().endsWith("Facade")
	            	|| cls.getSimpleName().equals("InformationUtil")) {
				Field [] fields = cls.getDeclaredFields();
				for (Field field : fields) {
					Class<?> fieldType = field.getType();
					AutowiredService ars = field.getAnnotation(AutowiredService.class);
					if (ars != null) {
						boolean isAccess = field.isAccessible();
						try {
							ReflectionUtils.makeAccessible(field);
							Object existObj = ReflectionUtils.getField(field, obj);
							if (existObj != null) {
								continue; //已存在实例就不需要再次注入
							}
							Object localObj = null;
							if (ars.isLocalPrior()) {
								localObj = getLocalBean(field, context);
							}
 
	                        if (localObj != null) {
	                        	ReflectionUtils.setField(field, obj, localObj);
	                        	logger.info(obj.getClass().getName()+"【"+obj.hashCode()+"】  inject local 【"+localObj.getClass().getName() +"】 object");
	                        } else {
	                        	Object remoteCallObj = null;
	                         
	                        	String remoteBeanName = field.getName()+ars.serviceType().getType();
	                        	if (context.containsBean(remoteBeanName)) {
	                        		remoteCallObj = context.getBean(remoteBeanName);
	                        	} else {
	                        		remoteCallObj = createRemoteObject(ars,fieldType);
	                        	}
	                        	
	                        	if (remoteCallObj == null) {
	                        		throw new InnerErrorException(cls.getName()+" nonexistent "+ars.serviceType().getType()+ " service!");
	                        	}
	                        	
	                        	ReflectionUtils.setField(field, obj, remoteCallObj);
	                        	logger.info(cls.getName()+"【"+obj.hashCode()+"】  inject remote 【"+ars.serviceType().getType()+"】【"+remoteCallObj.getClass().getName() +"】 object");
	                        	if (!context.containsBean(remoteBeanName)) {
		                        	context.getBeanFactory().registerSingleton(remoteBeanName, remoteCallObj);
	                        	}
	                        }
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						}  finally {
							field.setAccessible(isAccess);
						}
					} 	
				}
			}	
		}
		
		BidInjectRemoteServiceFinishedEvent injectFinished = new BidInjectRemoteServiceFinishedEvent(context, true);
		context.publishEvent(new BidApplicationEvent(injectFinished));
		
	}
	
	
	/**
	 * 
	 * @description 创建远程对象
	 * @param ars
	 * @param fieldType
	 * @return
	 */
	public Object createRemoteObject(AutowiredService ars,Class<?> fieldType) {
		 ServiceType serviceType =  ars.serviceType();
		 if (ServiceType.RMI == serviceType) {
			return RmiAutowire.getInstance().autoWireInject(ars, fieldType,remoteConfig);
		 } else if (ServiceType.WS_CXF == serviceType) {
			return CxfAutoWire.getInstance().autoWireInject(ars, fieldType,remoteConfig);
		 } else if (ServiceType.WS_XFILE == serviceType) {
			 
		 }
		return null;
	}

	
	@SuppressWarnings("unchecked")
	public Object getLocalBean(Field field,ApplicationContext context) {
		Object result = null;
		try {
			 Class<?>type = field.getType();
			 Map<String, Object> map = (Map<String, Object>) context.getBeansOfType(type);
			 Qualifier qualifier = field.getAnnotation(Qualifier.class);
			 if (qualifier != null) {
				 return map.get(qualifier.value());
			 }
			 //(如果有多个实现类)直接返回第一个实现类实例（可以通过Qualifier 指定具体那个实现类）
			 if (map.size() > 0) {
			     return map.values().iterator().next();
			 }
		}catch (Exception e) {
			//如果没有找到bean 直接 异常
		    logger.debug(e);
		}
		 return result;
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE-1;
	}



	public void setRemoteConfig(RemoteConfig remoteConfig) {
		this.remoteConfig = remoteConfig;
	}


	
}

