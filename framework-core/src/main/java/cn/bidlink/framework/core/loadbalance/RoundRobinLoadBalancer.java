package cn.bidlink.framework.core.loadbalance;

import java.util.List;

/**
 * @remark 第三方源码
 * @author zhongfeng (mailto:djangofan@163.com)
 * @quoter dejian.liu 
 * @quoteDate 2012-12-01
 * @param <E>
 */
public class RoundRobinLoadBalancer<E> extends LoadBalancerSupport<E> {
	private int counter = -1;

	@Override
	protected synchronized E doSelect(List<E> processors) {
		 int size = processors.size();
	        if (++counter >= size) {
	            counter = 0;
	        }
	        return processors.get(counter);
	}

}
