package cn.bidlink.framework.core.loadbalance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @remark 第三方源码
 * @author zhongfeng (mailto:djangofan@163.com)
 * @quoter dejian.liu 
 * @quoteDate 2012-12-01
 * @param <E>
 */
public abstract class LoadBalancerSupport<E> implements LoadBalancer<E> {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	private final List<E> processors = new CopyOnWriteArrayList<E>();
	
	/**
	 * 原始值
	 */
	private final List<Integer> weights = new CopyOnWriteArrayList<Integer>();

	
	public List<Integer> getWeights() {
		return weights;
	}
	public LoadBalancerSupport<E> addWeights(Integer weight) {
		weights.add(weight);
		return this;
	}
	public LoadBalancerSupport<E> removeWeight(Integer weight) {
		weights.remove(weight);
		return this;
	}
	
	public LoadBalancerSupport<E> allAllWeights(List<Integer> listWs) {
		weights.addAll(listWs);
		return this;
	}
	
	public LoadBalancerSupport<E> addProcessor(E processor) {
		processors.add(processor);
		return this;
	}

	public LoadBalancerSupport<E> addProcessors(Collection<E> processors) {
		this.processors.addAll(processors);
		return this;
	}

	public LoadBalancerSupport<E> removeProcessor(E processor) {
		processors.remove(processor);
		return this;
	}

	public List<E> getProcessors() {
		return processors;
	}

	public List<E> next() {
		if (!hasNext()) {
			return null;
		}
		return new ArrayList<E>(processors);
	}

	public boolean hasNext() {
		return processors.size() > 0;
	}

	public E select() {
		List<E> list = getProcessors();
		E processor = null;
		if (!list.isEmpty()) {
			processor = doSelect(list);
			if (processor == null) {
				throw new IllegalStateException(
						"No processors could be chosen to process ");
			}
		}
		return processor;
	}

	public E select(String key) {
		return select();
	}



	protected abstract E doSelect(List<E> processors);

}
