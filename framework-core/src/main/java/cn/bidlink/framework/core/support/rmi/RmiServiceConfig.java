package cn.bidlink.framework.core.support.rmi;

import cn.bidlink.framework.core.support.ServiceConfig;
 /**
 * @description:	 TODO add description
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-8-29		上午9:33:48
 *
 */
public class RmiServiceConfig extends ServiceConfig{

	/**
	 * @description serialVersionUID: UID
	 */
	
	private static final long serialVersionUID = -2188025223584548188L;
	
	private String remoteHost;
	
	private int remotePort;
	
	private boolean cacheStub;
	
	private boolean lookupStubOnStartup;
	
	private boolean refreshStubOnConnectFailure;
	
	private boolean isEnableSsl = false;
	
	private boolean isEnableAuth = false;
	
	private String remoteContextPath;
	
 
	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public boolean isCacheStub() {
		return cacheStub;
	}

	public void setCacheStub(boolean cacheStub) {
		this.cacheStub = cacheStub;
	}

	public boolean isLookupStubOnStartup() {
		return lookupStubOnStartup;
	}

	public void setLookupStubOnStartup(boolean lookupStubOnStartup) {
		this.lookupStubOnStartup = lookupStubOnStartup;
	}

	public boolean isRefreshStubOnConnectFailure() {
		return refreshStubOnConnectFailure;
	}

	public void setRefreshStubOnConnectFailure(boolean refreshStubOnConnectFailure) {
		this.refreshStubOnConnectFailure = refreshStubOnConnectFailure;
	}

	public String getRemoteContextPath() {
		return remoteContextPath;
	}

	public void setRemoteContextPath(String remoteContextPath) {
		this.remoteContextPath = remoteContextPath;
	}

	public boolean isEnableSsl() {
		return isEnableSsl;
	}

	public void setEnableSsl(boolean isEnableSsl) {
		this.isEnableSsl = isEnableSsl;
	}

	public boolean isEnableAuth() {
		return isEnableAuth;
	}

	public void setEnableAuth(boolean isEnableAuth) {
		this.isEnableAuth = isEnableAuth;
	}

	 

	 
 
 
}

