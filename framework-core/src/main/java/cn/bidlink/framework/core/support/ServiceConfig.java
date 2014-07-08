package cn.bidlink.framework.core.support;

import java.io.Serializable;

/**
 * @description: TODO add description
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-8-30 下午2:32:39
 * 
 */
public class ServiceConfig{

	/**
	 * 是否本地优先
	 */
	private boolean isLocalPrior;

	/**
	 * 分组key
	 */
	private String groupKey;
	
	/**
	 * 负载均衡
	 */
	private RemoteBalance remoteBalance;
	
	public boolean isLocalPrior() {
		return isLocalPrior;
	}

	public void setLocalPrior(boolean isLocalPrior) {
		this.isLocalPrior = isLocalPrior;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	public RemoteBalance getRemoteBalance() {
		return remoteBalance;
	}

	public void setRemoteBalance(RemoteBalance remoteBalance) {
		this.remoteBalance = remoteBalance;
	}

	
}
