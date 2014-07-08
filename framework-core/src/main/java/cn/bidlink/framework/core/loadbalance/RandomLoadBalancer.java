package cn.bidlink.framework.core.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * @remark 第三方源码
 * @author zhongfeng (mailto:djangofan@163.com)
 * @quoter dejian.liu 
 * @quoteDate 2012-12-01
 * @param <E>
 */
public class RandomLoadBalancer<E> extends LoadBalancerSupport<E> {
	 private static final Random RANDOM = new Random();

	@Override
	protected synchronized E doSelect(List<E> processors) {
		int size = processors.size();
        if (size == 0) {
            return null;
        } else if (size == 1) {
            // there is only 1
            return processors.get(0);
        }

        // pick a random
        int index = RANDOM.nextInt(size);
        return processors.get(index);
	}

}
