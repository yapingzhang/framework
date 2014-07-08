package cn.bidlink.framework.core.loadbalance;

import java.util.List;

/**
 * @remark 第三方源码
 * @author zhongfeng (mailto:djangofan@163.com)
 * @quoter dejian.liu 
 * @quoteDate 2012-12-01
 * @param <E>
 */
public class WeightedRoundRobinLoadBalancer<E> extends WeightedLoadBalancer<E> {

	private int counter;

	public WeightedRoundRobinLoadBalancer(List<E> processors,
			List<Integer> distributionRatios) {
		super(processors, distributionRatios);
	}
 
	
	
	@Override
	protected synchronized E doSelect(List<E> processors) {
		if (isRuntimeRatiosZeroed()) {
			resetRuntimeRatios();
			counter = 0;
		}
		boolean found = false;
		while (!found) {
			if (counter >= getRuntimeRatios().size()) {
				counter = 0;
			}
			int currentRuntimeWeight = getRuntimeRatios().get(counter).getRuntimeWeight();
			if ( currentRuntimeWeight > 0) {
				getRuntimeRatios().get(counter)
						.setRuntimeWeight(currentRuntimeWeight - 1);
				found = true;
			} else {
				counter++;
			}
		}

		return processors.get(counter++);
	}

}
