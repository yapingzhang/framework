package cn.bidlink.framework.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.bidlink.framework.core.enums.ServiceType;
 /**
 * @description: 自动装配远程RMI服务
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-8-25		下午8:08:12
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutowiredService {

//	boolean required() default true;
	
//	String remoteHost() default "";
//	
//	int remotePort() default 1099;
//	
//	boolean cacheStub() default true;
//	
//	boolean lookupStubOnStartup() default true;
//	
//	boolean refreshStubOnConnectFailure() default false;
	
	/**
	 * @description 远程RMI 接口服务分组
	 * @param    groupKey 组名KEY值 
	 */
	String [] groupKey() default {};
	
	/**
	 * 
	 * @description 是否本地优先
	 * @param    isLocalPrior 
	 * @throws
	 */
	boolean isLocalPrior() default true;
	
	/**
	 * 
	 * @description  远程服务类别
	 * @param  remoteType
	 * @throws
	 */
	ServiceType serviceType() default ServiceType.RMI;
	
	
	
}

