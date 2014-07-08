package cn.bidlink.framework.web.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.StringUtils;

import cn.bidlink.framework.core.commons.Constants;
import cn.bidlink.framework.core.exceptions.GeneralException;
import cn.bidlink.framework.core.utils.ObjectUtils;
import cn.bidlink.framework.core.utils.ValidateCodeUtils;
import cn.bidlink.framework.core.utils.ValidateCodeUtils.ValidateCodeConstraints;
import cn.bidlink.framework.core.utils.ValidateCodeUtils.ValidateCodeEntry;
import cn.bidlink.framework.core.utils.holder.PropertiesHolder;

 

public abstract class WebUtils extends org.springframework.web.util.WebUtils{

	private static  Logger logger = Logger.getLogger(WebUtils.class);
	
	public static String generateSubmitToken(HttpServletRequest request) {
		String uuid = (String) request.getSession().getAttribute(Constants.WEB_SUBMIT_TOKEN); 
		if(!StringUtils.hasLength(uuid)) {
			uuid = UUID.randomUUID().toString();
			request.getSession().setAttribute(Constants.WEB_SUBMIT_TOKEN ,uuid);
		}
		return uuid;
	}
	/**
	 * 扩展自定义token生成方案,separator--分隔符  flowUniqueId 记录工作流实例ID
	 * @description  TODO add zhoujun
	 * @param request
	 * @return
	 */
	public static String generateSubmitToken(HttpServletRequest request,String separator,String flowUniqueId) {
		String uuid = (String) request.getSession().getAttribute(Constants.WEB_SUBMIT_TOKEN); 
		if(!StringUtils.hasLength(uuid)) {
			uuid = UUID.randomUUID().toString()+separator+flowUniqueId;
			request.getSession().setAttribute(Constants.WEB_SUBMIT_TOKEN ,uuid);
		}
		return uuid;
	}
	/**
	 * 扩展自定义token校验方案,separator--分隔符  flowUniqueId 记录工作流实例ID
	 * @description  TODO add zhoujun
	 * @param request
	 * @return
	 */
	public static boolean isAvailableSubmitToken(HttpServletRequest request,String separator,String flowUniqueId) {
		String sessionToken  = (String) request.getSession().getAttribute(
				Constants.WEB_SUBMIT_TOKEN);
		String requestToken  = request.getParameter(Constants.WEB_SUBMIT_TOKEN);
		request.getSession().removeAttribute(Constants.WEB_SUBMIT_TOKEN);
		boolean flag = false;
		try{
		   if(requestToken!=null){
			if(requestToken.indexOf(separator)>0){
				String[] strs= requestToken.split(separator);
				
				if(org.apache.commons.lang.StringUtils.isNotEmpty(flowUniqueId) && flowUniqueId.equals(strs[1])){
					flag=true;
				}
			}
		  }
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return (StringUtils.hasLength(sessionToken) 
				&& StringUtils.hasLength(requestToken) 
				&& sessionToken.equals(requestToken)
				&& flag);
	}
	
	public static boolean isAvailableSubmitToken(HttpServletRequest request) {
		String sessionToken  = (String) request.getSession().getAttribute(
				Constants.WEB_SUBMIT_TOKEN);
		String requestToken  = request.getParameter(Constants.WEB_SUBMIT_TOKEN);
		request.getSession().removeAttribute(Constants.WEB_SUBMIT_TOKEN);
		return (StringUtils.hasLength(sessionToken) 
				&& StringUtils.hasLength(requestToken) 
				&& sessionToken.equals(requestToken));
	}
	
	public static ValidateCodeEntry generateValidateCode(
			HttpServletRequest request, ValidateCodeConstraints constraints) {
		ValidateCodeEntry validateCodeEntry = ValidateCodeUtils.generateValidateCode(constraints);
		request.getSession().setAttribute(Constants.WEB_VALIDATE_CODE ,validateCodeEntry);
		return validateCodeEntry;
	}

	public static boolean checkValidateCode(HttpServletRequest request, boolean isRemove) {
		String validateCodeParameter = request.getParameter(Constants.WEB_VALIDATE_CODE);
		ValidateCodeEntry validateCodeEntry = 
			(ValidateCodeEntry) request.getSession().getAttribute(Constants.WEB_VALIDATE_CODE);
		boolean isAvailable = (validateCodeEntry != null && validateCodeEntry.getCode().equalsIgnoreCase(validateCodeParameter));
		if(isRemove) {
			request.getSession().removeAttribute(Constants.WEB_VALIDATE_CODE);
		}
		return isAvailable;
	}
	
	@SuppressWarnings({"unchecked" })
	public static void exposeDefaultData(Map<String, Object> model, HttpServletRequest request) {
		if(model == null) {
			return;
		}
		//application properties
		model.put("PropertiesHolder", PropertiesHolder.class);
		
		//request response
		//TODO
		model.put(Constants.WEB_REQUEST_SCOPE_NAME, request);
		model.put(Constants.WEB_SESSION_SCOPE_NAME, request.getSession());
		model.put(Constants.WEB_APPLICATION_SCOPE_NAME, request.getSession().getServletContext());
		//contextPath
		String contextPath = request.getContextPath();
		if(!StringUtils.hasLength(contextPath)) {
			contextPath = "";
		}
		model.put(Constants.WEB_CONTEXT_PATH, contextPath);
		//parameters
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		Enumeration<String> parameterNames = request.getParameterNames();
		while(parameterNames.hasMoreElements()) {
			String parameterName = parameterNames.nextElement();
			String[] parameterValues = request.getParameterValues(parameterName);
			if(parameterValues != null && parameterValues.length > 0) {
				if(parameterValues.length == 1) {
					parameterMap.put(parameterName.replaceAll("\\.", "_"), request.getParameter(parameterName));
				} else {
					parameterMap.put(parameterName.replaceAll("\\.", "_"), parameterValues);
					
				}
			}
		}
		model.put(Constants.WEB_REQUEST_PARAMETER_MAP_NAME, parameterMap);
		//headers
		Enumeration<String> headerNames = request.getHeaderNames();
		Map<String, String> headerMap = new HashMap<String, String>();
		while(headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			headerMap.put(headerName.replaceAll("\\.", "_"), request.getHeader(headerName));
		}
		model.put(Constants.WEB_REQUEST_HEADER_MAP_NAME, headerMap);
	}
	
	public static String[] getParameterArrayFromQueryString(HttpServletRequest request, String name, String...enc) {
		List<String> values = new ArrayList<String>();
		String queryString = request.getQueryString();
		String[] parameterPairs = queryString.split("&");
		for(String parameterPair : parameterPairs) {
			String[] parameterItem = parameterPair.split("=");
			if(StringUtils.hasLength(parameterItem[0]) 
			   && parameterItem[0].equals(name)) {
				if(ObjectUtils.isEmpty(enc)) {
					values.add(urlDecode(parameterItem[1], "UTF-8"));
				} else {
					values.add(urlDecode(parameterItem[1], enc[0]));
				}
			}
		}
		return values.toArray(new String[values.size()]);
	}
	
	public static String getParameterFromQueryString(HttpServletRequest request, String name, String...enc) {
		String[] parameterArray = getParameterArrayFromQueryString(request, name, enc);
		if(!ObjectUtils.isEmpty(parameterArray)) {
			return parameterArray[0];
		}
		return null;
	}
	
	/**
	 * url字符串编码
	 * 
	 * @param s
	 * @param enc
	 * @return
	 */
	public static String urlEncode(String s, String... enc) {
		if (!StringUtils.hasLength(s)) {
			return s;
		}
		String encodedString = null;
		try {
			if (ObjectUtils.isEmpty(enc)) {
				encodedString = URLEncoder.encode(s, "UTF-8");
			} else {
				encodedString = URLEncoder.encode(s, enc[0]);
			}
		} catch (UnsupportedEncodingException e) {
			throw new GeneralException("encode string [" + s + "] error", e);
		}
		return encodedString;
	}
	
	/**
	 * url字符串解码
	 * 
	 * @param s
	 * @param enc
	 * @return
	 */
	public static String urlDecode(String s, String... enc) {
		if (!StringUtils.hasLength(s)) {
			return s;
		}
		String decodedString = null;
		try {
			if (ObjectUtils.isEmpty(enc)) {
				decodedString = URLDecoder.decode(s, "UTF-8");
			} else {
				decodedString = URLDecoder.decode(s, enc[0]);
			}
		} catch (UnsupportedEncodingException e) {
			throw new GeneralException("decode string [" + s + "] error", e);
		}
		return decodedString;
	}

	/**
	 * 
	 * @description: 获取客户端ip.
	 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	public static void main(String[] args) {
		String queryString = "name=a&name=b&password=32432";//&nike=张三&nike=李四";
		queryString += "&nike=" + urlEncode("张三", "utf-8");
		queryString += "&nike=" + urlEncode("李四", "utf-8");
		//queryString = URLEncoder.encode(queryString, "UTF-8");
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setQueryString(queryString);
		String[] parmeterArray = getParameterArrayFromQueryString(request, "nike");
		System.out.println(String.valueOf(parmeterArray));
	}
}
