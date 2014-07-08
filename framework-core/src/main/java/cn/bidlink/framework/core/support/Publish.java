package cn.bidlink.framework.core.support;
 /**
 * @description:	 TODO add description
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-8-30		上午11:15:11
 *
 */
public interface Publish {
	
	/**
	 * 
	 * @description 业务发布
	 * @param  obj  发布对象
	 * @param  interfaceCls 接口
	 * @return  void DOM对象
	 * @throws
	 */
	public void publishService(Object obj, Class<?> interfaceCls);
	
 

}

