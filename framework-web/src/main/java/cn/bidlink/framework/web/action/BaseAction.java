package cn.bidlink.framework.web.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.bidlink.baseInfo.user.model.TRegUser;
import cn.bidlink.framework.log4j.utils.OperateMDC;
import cn.bidlink.framework.util.DateUtils;
import cn.bidlink.framework.web.BidWebInterceptor;
import cn.bidlink.pmp.home.util.security.details.PremissionUserDetails;

/**
 * @description: 所有 Action基类
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-8-24 下午12:12:53
 * 
 */
public abstract class BaseAction<T>{
	/**
	 * 日志对象
	 */
	public final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private MessageSource messageResource;
	
	/**
	 * 
	 * @description 从messageResource 中读取数据
	 * @param key key
	 * @param params 参数
	 * @param locale 请求国别
	 * @return
	 */
	public String getValueByMessageResource(String key,String [] params,Locale locale) {
		return messageResource.getMessage(key, params, Locale.CHINA);
	}
	
	public String getValueByMessageResource(String key,String [] params) {
		return messageResource.getMessage(key, params, Locale.CHINA);
	}
	
	public String getValueByMessageResource(String key) {
		return messageResource.getMessage(key, null, Locale.CHINA);
	}

	/**
	 * @description 进入该页面时调用ActionURL
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @param reqObj
	 *            请求对象
	 * @return String DOM对象
	 * @throws
	 */
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public abstract ModelAndView index(HttpServletRequest request,HttpServletResponse response, T reqObj);

	/**
	 * 
	 * @description  取得当前登录用户信息
	 * @return
	 */
	public TRegUser getCurRegUser() {
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

 
	
	/**
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @description  设置MDC属性
	 */
	protected void putMDCProperty(Map<String, String> logProperties) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		OperateMDC operateMDC = BidWebInterceptor.getOperateMDC();
		for(Entry<String, String> entry : logProperties.entrySet()) {
			PropertyUtils.setProperty(operateMDC, entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * @description  设置其他日志数据
	 * @param otherProperty
	 */
	protected void putOtherProperty(Map<String, Object> otherProperty) {
		OperateMDC operateMDC = BidWebInterceptor.getOperateMDC();
		operateMDC.setOtherProperty(otherProperty);
	}

}
