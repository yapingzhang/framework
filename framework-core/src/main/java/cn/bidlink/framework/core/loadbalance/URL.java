package cn.bidlink.framework.core.loadbalance;

import java.io.Serializable;

/**
 * 
 * @description:	URL
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012-12-2 上午11:50:13
 */
public final class URL implements Serializable {
    
	private static final long serialVersionUID = 1L;

	private String protocol;
	
	private String host;

	private int port;

	private String path;

	public URL() {
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

 

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	 
}