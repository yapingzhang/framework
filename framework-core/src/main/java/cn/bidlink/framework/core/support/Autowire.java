package cn.bidlink.framework.core.support;

import cn.bidlink.framework.core.annotation.AutowiredService;
 /**
 * @description:	 TODO add description
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-8-30		下午2:13:58
 *
 */
public interface Autowire {

	/**
	 * 
	 * @description 适配注入
	 * @param    Autowire设定文件
	 * @param interfaces 接口
	 * @param remoteConfig
	 * @return  Object DOM对象
	 * @throws
	 */
	public Object autoWireInject(AutowiredService ars,Class<?> interfaces,RemoteConfig remoteConfig); 
	
}

