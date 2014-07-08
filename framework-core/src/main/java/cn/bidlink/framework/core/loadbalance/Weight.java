package cn.bidlink.framework.core.loadbalance;

/**
 * @remark 第三方源码
 * @author zhongfeng (mailto:djangofan@163.com)
 * @quoter dejian.liu 
 * @quoteDate 2012-12-01
 * @param <E>
 */
public interface Weight {
	/**
	 * @return
	 */
	int getWeight();
}
