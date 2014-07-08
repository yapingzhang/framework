package cn.bidlink.framework.core.support;

import java.io.Serializable;

/**
 * @description: TODO add description
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-12-2 下午4:33:47
 * 
 */
@SuppressWarnings("serial")
public class BalanceInfoBase implements Serializable {
	
	private String protocol;

	private String host;

	private int port;

	private String basePath;

	private String username;

	private String password;
	


	public BalanceInfoBase() {
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
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
 

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	 
}
