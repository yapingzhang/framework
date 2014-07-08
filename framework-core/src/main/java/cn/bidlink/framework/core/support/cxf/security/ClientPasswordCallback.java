package cn.bidlink.framework.core.support.cxf.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

/**
 * webservice client密码校验回调
 * @author wubin
 * Dec 21, 2011
 */
public class ClientPasswordCallback implements CallbackHandler{
	
	private String password;
	
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
		pc.setPassword(this.getPassword());
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
}
