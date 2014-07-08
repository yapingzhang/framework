package cn.bidlink.framework.core.support;

import java.util.ArrayList;
import java.util.List;

import cn.bidlink.framework.core.loadbalance.BalanceType;

/**
 * @description: 是否设置balance
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-12-2 上午11:12:02
 * 
 */
public class RemoteBalance {

	/**
	 * 类别
	 */
	private BalanceType balanceType;
	
	/**
	 * 远程URL
	 */
	private List<BalanceInfo> balanceInfos = new ArrayList<BalanceInfo>();

	/**
	 * 如果服务端 启用了ssl 这里必需设置为true
	 */
	private boolean isEnableSsl = false;
	
	public RemoteBalance() {
	}
	
	public BalanceType getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(BalanceType balanceType) {
		this.balanceType = balanceType;
	}
	
	
 
	public List<BalanceInfo> getBalanceInfos() {
		return balanceInfos;
	}

	public void setBalanceInfos(List<BalanceInfo> balanceInfos) {
		this.balanceInfos = balanceInfos;
	}

	public boolean isEnableSsl() {
		return isEnableSsl;
	}

	public void setEnableSsl(boolean isEnableSsl) {
		this.isEnableSsl = isEnableSsl;
	}

 


}
