package cn.bidlink.fileserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @date 2012-2-5
 * @author changzhiyuan
 * @desc 解密实现类注解，实现该类自动加入到执行环境中
 * @desc 根据appid进行hash 
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Crypt {
	String appid();
}
