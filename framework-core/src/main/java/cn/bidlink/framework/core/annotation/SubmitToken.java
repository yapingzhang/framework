package cn.bidlink.framework.core.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.RequestMethod;

@Target({METHOD}) 
@Retention(RUNTIME)
public @interface SubmitToken {
	
	boolean validate() default true;
	RequestMethod method() default RequestMethod.POST;
 	
}
