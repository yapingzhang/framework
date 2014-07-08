package cn.bidlink.framework.dao.ibatis.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.bidlink.framework.core.annotation.Dao;
import cn.bidlink.framework.dao.dataSource.DynamicDataSource;
 /**
 * @description:	 TODO add description
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-8-24		下午10:51:54
 */
@Aspect
@Component
@Order(1)
public class DaoInterceptor {
	 
	@Around(value = "execution(* *cn.bidlink..dao..impl.*DaoImpl.* (..))", argNames = "pjp")
	public Object doException(ProceedingJoinPoint pjp) {
		Object obj = null;
		try {
			Object target = pjp.getTarget();
			Dao dao = target.getClass().getAnnotation(Dao.class);
			String [] dses = dao.dsKey();
			if (dses != null && dses.length > 0) {
				String curDs = dses[0];
				DynamicDataSource.CONTEXT_HOLDER.set(curDs);
			}
			obj = pjp.proceed();
			
		} catch (Throwable e) {
		    throw new RuntimeException(e);
		} finally {
			DynamicDataSource.CONTEXT_HOLDER.remove();
		}
		return obj;
	} 
}
