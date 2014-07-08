package cn.bidlink.framework.core.support;


/**
 * @description: 认证检查信息
 * @version Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012-12-10 上午10:30:25
 */
public class AuthInfo {

	/**
	 * 认证类别
	 */
	private AuthType authType;

	private String userId;

	private String userPwd;

	public AuthType getAuthType() {
		return authType;
	}

	public void setAuthType(AuthType authType) {
		this.authType = authType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

}
