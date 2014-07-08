package cn.bidlink.framework.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

import cn.bidlink.framework.core.enums.ServiceType;

/**
 * @description: RMI 远程接口发布注解
 * @since Ver 1.0
 * @author  <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-8-25 下午4:16:39
 * 
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface PublishService {

	String value() default "";

//	String registryHost() default "";
//	int registryPort() default 1099;
//	Class<?> serviceInterface() default Object.class;
//	String serviceName() default "";
	
	ServiceType [] serviceTypes() default {ServiceType.RMI}; 
	
	//@description 兼容dubbo
	String [] protocol() default {};

}
