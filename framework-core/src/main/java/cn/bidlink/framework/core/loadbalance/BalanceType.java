package cn.bidlink.framework.core.loadbalance;

/**
 * @description: 负载均衡类别
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-12-2 上午11:14:05
 * 
 */
public enum BalanceType {
	/**
	 * 随机
	 */
	RANDOM,
	/**
	 * 一致性HASH
	 */
	HASH,
	/**
	 * 轮循
	 */
	ROUNDROBIN,
	/**
	 * 权重随机
	 */
	WEIGTHED_RANDOM,
	/**
	 * 权重轮循
	 */
	WEIGTHED_ROUNDROBIN
 

}
