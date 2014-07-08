package cn.bidlink.framework.core.support.rmi;

import java.rmi.server.RemoteServer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import cn.bidlink.framework.core.exceptions.BidRmiException;
import cn.bidlink.framework.core.support.AuthInfo;
import cn.bidlink.framework.core.support.AuthType;
import cn.bidlink.framework.core.utils.DateUtils;

/**
 * 
 * @description:	IP 过滤
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012-12-5 上午11:07:19
 */
@SuppressWarnings("serial")
public class BidPerformanceMonitorInterceptor implements MethodInterceptor {

	private List<String> allowIps = new ArrayList<String>();
	private static Logger logger = Logger
			.getLogger(BidPerformanceMonitorInterceptor.class);

	/**
	 * 是否启用安全检查
	 */
	private boolean isEnabledAuth = false;
	
	/**
	 * 认证信息
	 */
	private AuthInfo authInfo;
	
	public BidPerformanceMonitorInterceptor(List<String> allowIps,boolean isEnabledAuth,AuthInfo authInfo) {
		this.allowIps = allowIps;
		this.isEnabledAuth = isEnabledAuth;
		this.authInfo = authInfo;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String remoteIp = RemoteServer.getClientHost();
		if(allowIps != null && !allowIps.isEmpty()) {
			for (String reg : allowIps) {
				Pattern p = Pattern.compile(reg);
				if(!p.matcher(remoteIp).matches()) {
					logger.error("illegal IP：" + remoteIp+"  request Time is :"+DateUtils.format(new Date(), DateUtils.DATE_HH_MM_SS));
					throw new BidRmiException("illegal IP：" + remoteIp);
				}
			}
		}
 		Authentication ac =  SecurityContextHolder.getContext().getAuthentication();
		if (isEnabledAuth) {
			if (ac == null) {
				throw new BidRmiException("Please set AuthInfo by SecurityContextHolder.getContext().setAuthentication(...)");
			}
			if (AuthType.BASIC == authInfo.getAuthType()) {
				if (StringUtils.isNotEmpty(authInfo.getUserId()) && StringUtils.isNotEmpty(authInfo.getUserPwd())) {
					if (!(authInfo.getUserId().equals(ac.getPrincipal())  && authInfo.getUserPwd().equals(ac.getCredentials()))) {
						throw new BidRmiException(" rmi basic auth fail!");
					}
				}
			} 
		}
		return invocation.proceed();
	}
 
	public static void main(String[] args) {
		String regIP = "192.168.*.*";
		Pattern p = Pattern.compile(regIP);
		String remoteIp = "192.168.10.15";
		System.out.println(p.matcher(remoteIp).matches());
		
	}
}
