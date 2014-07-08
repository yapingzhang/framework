package cn.bidlink.framework.core.loadbalance;

import java.util.Collection;
import java.util.List;

/**
 * @remark 第三方源码
 * @author zhongfeng (mailto:djangofan@163.com)
 * @quoter dejian.liu 
 * @quoteDate 2012-12-01
 * @param <E>
 */
public interface LoadBalancer<E> {
	/**
	 * Adds a new processor to the load balancer
	 * 
	 * @param processor
	 *            the processor to be added to the load balancer
	 */
	LoadBalancer<E> addProcessor(E processor);
	
	public LoadBalancer<E> addProcessors(Collection<E> processors) ;

	/**
	 * Removes the given processor from the load balancer
	 * 
	 * @param processor
	 *            the processor to be removed from the load balancer
	 */
	LoadBalancer<E> removeProcessor(E processor);

	/**
	 * Returns the current processors available to this load balancer
	 * 
	 * @return the processors available
	 */
	List<E> getProcessors();

	/**
	 * Returns the selected processor based on load balance algorithm
	 * 
	 * @return the processors
	 */
	E select();
	
	/**
	 * Returns the selected processor based on load balance algorithm
	 * Used by ConsistHash
	 * 
	 * @return the processors
	 */
	E select(String key);
}
