package cn.bidlink.framework.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.bidlink.baseInfo.user.model.TRegUser;
import cn.bidlink.framework.core.annotation.SubmitToken;
import cn.bidlink.framework.core.commons.Constants;
import cn.bidlink.framework.core.exceptions.BusinessException;
import cn.bidlink.framework.log4j.utils.OperateMDC;
import cn.bidlink.framework.util.DateUtils;
import cn.bidlink.framework.web.utils.WebUtils;
import cn.bidlink.pmp.home.util.security.details.PremissionUserDetails;
 /**
 * @description:	 TODO add description
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-9-7		上午9:03:31
 *
 */
public class BidWebInterceptor extends HandlerInterceptorAdapter {


	private static Logger logger = Logger.getLogger(BidWebInterceptor.class);
 
	private static ThreadLocal<OperateMDC> operateMDCThreadLocal = new ThreadLocal<OperateMDC>();
 
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
	
		setCurrentUserInfo(request);
		HandlerMethod  handlerMethod = (HandlerMethod)handler;
		StringBuffer buf = new StringBuffer();
		buf.append("\r\n-------------------------------------\r\n请求URL==="+request.getRequestURL().toString());
		buf.append("\r\n调用类==="+handlerMethod.getBean().getClass().getName());
		buf.append("\r\n调用方法==="+handlerMethod.getMethod().getName());
		buf.append("\r\n-------------------------------------");
		logger.debug(buf.toString());
		checkRepeatSubmit(request, handlerMethod);
		
		OperateMDC operateMDC = new OperateMDC();
		TRegUser currentUser = getCurRegUser();
		if(StringUtils.isNotBlank(currentUser.getName())) {
			operateMDC.setUserId(currentUser.getId().toString());
			operateMDC.setUserName(currentUser.getLoginName());
			operateMDC.setCompanyId(currentUser.getCompanyId().toString());
			operateMDC.setCompanyName(currentUser.getCompanyName());
			operateMDC.setOperateTime(new Date());
			operateMDC.setClientIP(WebUtils.getIpAddr(request));
		}
		operateMDCThreadLocal.set(operateMDC);
		return true;
	}
	
	public static OperateMDC getOperateMDC() {
		return operateMDCThreadLocal.get();
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
  		operateMDCThreadLocal.remove();
	}
	
	public void setCurrentUserInfo(HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return;
		}
		Object obj = auth.getPrincipal();
		if (obj != null && obj instanceof String) {
			TRegUser user = new TRegUser();
			user.setName(obj.toString());
			user.setLoginName(obj.toString());
			request.getSession().setAttribute(Constants.CURRENT_USER, user);
		} else if (obj != null) {
			PremissionUserDetails userAuthority = (PremissionUserDetails)auth.getPrincipal();
	        TRegUser user = userAuthority.gettRegUser();
	        request.getSession().setAttribute(Constants.CURRENT_USER, user);
	        request.getSession().setAttribute(Constants.CURRENT_USER_AUTH, userAuthority.getAuthorityMap());
		}
	}
 
        

	public void checkRepeatSubmit(HttpServletRequest request,HandlerMethod  handlerMethod) {
		SubmitToken submitToken = handlerMethod.getMethodAnnotation(SubmitToken.class);
		if (submitToken != null && submitToken.validate()) {
			if(!WebUtils.isAvailableSubmitToken(request)) {
				throw new BusinessException("repeat submit",null, "framework.exception.repeated_submit",null);
			}
		} 
	}
	
	/**
	 * 
	 * @description  取得当前登录用户信息
	 * @return
	 */
	private TRegUser getCurRegUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 TRegUser user = null;
		if (auth == null) {
			logger.info("无用户信息:"+DateUtils.format(new Date(),DateUtils.DATE_HH_MM_SS));
			return new TRegUser();
		}
		Object obj = auth.getPrincipal();
		if (obj != null && obj instanceof String) {
			 user = new TRegUser();
			 user.setName(obj.toString());
			 logger.error("匿名用户:"+user.getName());
 		} else if (obj != null) {
			PremissionUserDetails userAuthority = (PremissionUserDetails)auth.getPrincipal();
	        user = userAuthority.gettRegUser();
		}
		return user;
		
	}
	 

}

