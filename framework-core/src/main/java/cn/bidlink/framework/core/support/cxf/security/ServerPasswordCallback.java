package cn.bidlink.framework.core.support.cxf.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

/**
 * webservice server端密码校验回调
 * @author wubin
 * Dec 21, 2011
 */
public class ServerPasswordCallback implements CallbackHandler {
	private String username;
	private String password;
	
	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
		if (pc.getIdentifier().equals(this.getUsername())) {
			pc.setPassword(this.getPassword());
		}
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

}
