package cn.bidlink.framework.core.support.cxf;

import javax.security.auth.callback.CallbackHandler;

import cn.bidlink.framework.core.support.ServiceConfig;
/**
 * @description:	 TODO add description
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-8-30		下午1:18:47
 *
 */
@SuppressWarnings("serial")
public class CxfServiceConfig extends ServiceConfig {
	
	private String baseUrl;
	
	private String user;
	
	private String password;
	
	private String passwordType;
	
	private String action;
	
	
	private int receiveTimeOut;
	
	private CallbackHandler passwordCallbackHandler;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordType() {
		return passwordType;
	}

	public void setPasswordType(String passwordType) {
		this.passwordType = passwordType;
	}

	public int getReceiveTimeOut() {
		return receiveTimeOut;
	}

	public void setReceiveTimeOut(int receiveTimeOut) {
		this.receiveTimeOut = receiveTimeOut;
	}

	public CallbackHandler getPasswordCallbackHandler() {
		return passwordCallbackHandler;
	}

	public void setPasswordCallbackHandler(CallbackHandler passwordCallbackHandler) {
		this.passwordCallbackHandler = passwordCallbackHandler;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	
	
	

}

