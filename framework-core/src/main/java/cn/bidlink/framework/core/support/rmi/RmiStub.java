package cn.bidlink.framework.core.support.rmi;

import java.rmi.Remote;

import cn.bidlink.framework.core.loadbalance.BalanceType;
import cn.bidlink.framework.core.support.BalanceInfo;

/**
 * @description: TODO add description
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-12-2 下午4:51:43
 * 
 */
@SuppressWarnings("serial")
public class RmiStub extends BalanceInfo {
	
	private Remote stub;

	private StubStatus stubStatus;
	
	private BalanceType balanceType;
	
	private String groupKey;

	public Remote getStub() {
		return stub;
	}

	public void setStub(Remote stub) {
		this.stub = stub;
	}

	public StubStatus getStubStatus() {
		return stubStatus;
	}

	public void setStubStatus(StubStatus stubStatus) {
		this.stubStatus = stubStatus;
	}

	public BalanceType getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(BalanceType balanceType) {
		this.balanceType = balanceType;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}
	
	

}
