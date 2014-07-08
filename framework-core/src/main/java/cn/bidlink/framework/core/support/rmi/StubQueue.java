package cn.bidlink.framework.core.support.rmi;

import java.io.Serializable;

import cn.bidlink.framework.core.loadbalance.BalanceType;

@SuppressWarnings("serial")
public class StubQueue implements Serializable{
	private long startTime;
	private String groupKey;
	private String host;
	private int port;
	private String basePath;
	private BalanceType balanceType;
	private int weight;

	public StubQueue(long startTime,String groupKey, String host, int port,String basePath) {
		super();
		this.startTime = startTime;
		this.groupKey = groupKey;
		this.host = host;
		this.port = port;
		this.basePath = basePath;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public StubQueue() {
	}
	
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public BalanceType getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(BalanceType balanceType) {
		this.balanceType = balanceType;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	 
	
	
}
