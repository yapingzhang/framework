package cn.bidlink.framework.core.support;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import cn.bidlink.framework.core.annotation.AutowiredService;
import cn.bidlink.framework.core.enums.ServiceType;
import cn.bidlink.framework.core.exceptions.InnerErrorException;
import cn.bidlink.framework.core.support.cxf.CxfAutoWire;
import cn.bidlink.framework.core.support.rmi.RmiAutowire;

/**
 * @description: TODO add description
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-12-23 下午9:16:45
 * 
 */
public class BidRemoteServiceAutowrie extends AutowiredAnnotationBeanPostProcessor implements ApplicationContextAware,
		Ordered {

	private static Logger logger = Logger.getLogger(BidRemoteServiceAutowrie.class);

	private XmlWebApplicationContext applicationContext;

	private RemoteConfig remoteConfig;
	
	private static boolean isSkipRmi = false;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (applicationContext.containsBean("remoteConfig")) {
			remoteConfig = applicationContext.getBean(RemoteConfig.class);
		}
		this.applicationContext = (XmlWebApplicationContext) applicationContext;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Class<?> cls = bean.getClass();
		if (AopUtils.isAopProxy(bean) || AopUtils.isCglibProxy(bean)) {
			cls = AopUtils.getTargetClass(bean);
		}
		if (remoteConfig == null) {
			return bean;
		}

		if (cls.getSimpleName().endsWith("ServiceImpl") || cls.getSimpleName().endsWith("Action")
				|| cls.getSimpleName().endsWith("Service") 
				|| cls.getSimpleName().endsWith("Facade")
				|| cls.getSimpleName().equals("InformationUtil")) {
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				Class<?> fieldType = field.getType();
				AutowiredService ars = field.getAnnotation(AutowiredService.class);
				
				if (ars != null) {
					ServiceType serviceType = ars.serviceType();
					if (ServiceType.RMI == serviceType && isSkipRmi) {
						continue; //跳过RMI扫描注入
					}
  					boolean isAccess = field.isAccessible();
					try {
						ReflectionUtils.makeAccessible(field);
						Object existObj = ReflectionUtils.getField(field, bean);
						if (existObj != null) {
							continue; // 已存在实例就不需要再次注入
						}
						Object localObj = null;
						if (ars.isLocalPrior()) {
							localObj = getLocalBean(field, applicationContext);
						}

						if (localObj != null) {
							ReflectionUtils.setField(field, bean, localObj);
							logger.info(bean.getClass().getName() + "【" + bean.hashCode() + "】  inject local 【"
									+ localObj.getClass().getName() + "】 object");
						} else {
							Object remoteCallObj = null;
							String remoteBeanName = field.getName() + ars.serviceType().getType();
							if (applicationContext.containsBean(remoteBeanName)) {
								remoteCallObj = applicationContext.getBean(remoteBeanName);
							} else {
								remoteCallObj = createRemoteObject(ars, fieldType);
							}

							if (remoteCallObj == null) {
								throw new InnerErrorException(cls.getName() + " nonexistent "
										+ ars.serviceType().getType() + " service!");
							}

							ReflectionUtils.setField(field, bean, remoteCallObj);
							logger.info(cls.getName() + "【" + bean.hashCode() + "】  inject remote 【"
									+ ars.serviceType().getType() + "】【" + remoteCallObj.getClass().getName()
									+ "】 object");
							if (!applicationContext.containsBean(remoteBeanName)) {
								((DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory())
										.registerSingleton(remoteBeanName, remoteCallObj);
							}

							BidInjectRemoteObjectEvent injectFinished = new BidInjectRemoteObjectEvent(
									applicationContext, bean, remoteCallObj);
							applicationContext.publishEvent(new BidApplicationEvent(injectFinished));
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					} finally {
						field.setAccessible(isAccess);
					}
				}

			}
		}

		return bean;
	}

	public void setRemoteConfig(RemoteConfig remoteConfig) {
		this.remoteConfig = remoteConfig;
	}

	/**
	 * 
	 * @description 创建远程对象
	 * @param ars
	 * @param fieldType
	 * @return
	 */
	public Object createRemoteObject(AutowiredService ars, Class<?> fieldType) {
		ServiceType serviceType = ars.serviceType();
		if (ServiceType.RMI == serviceType) {
			return RmiAutowire.getInstance().autoWireInject(ars, fieldType, remoteConfig);
		} else if (ServiceType.WS_CXF == serviceType) {
			return CxfAutoWire.getInstance().autoWireInject(ars, fieldType, remoteConfig);
		} else if (ServiceType.WS_XFILE == serviceType) {

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Object getLocalBean(Field field, ApplicationContext context) {
		Object result = null;
		try {
			Class<?> type = field.getType();
			Map<String, Object> map = (Map<String, Object>) context.getBeansOfType(type);
			Qualifier qualifier = field.getAnnotation(Qualifier.class);
			if (qualifier != null) {
				return map.get(qualifier.value());
			}
			// (如果有多个实现类)直接返回第一个实现类实例（可以通过Qualifier 指定具体那个实现类）
			if (map.size() > 0) {
				return map.values().iterator().next();
			}
		} catch (Exception e) {
			// 如果没有找到bean 直接 异常
			logger.debug(e);
		}
		return result;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE - 100;
	}

	public static boolean isSkipRmi() {
		return isSkipRmi;
	}

	public static void setSkipRmi(boolean isSkipRmi) {
		BidRemoteServiceAutowrie.isSkipRmi = isSkipRmi;
	}
	

}
