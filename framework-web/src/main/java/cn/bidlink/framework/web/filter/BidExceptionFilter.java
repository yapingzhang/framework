package cn.bidlink.framework.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.bidlink.framework.core.exceptions.BaseException;
import cn.bidlink.framework.core.model.ResponseEntity;
import cn.bidlink.framework.core.utils.DateUtils;
import cn.bidlink.framework.core.utils.json.JsonUtils;
import cn.bidlink.framework.core.utils.response.ResponseUtils;

/**
 * @description: 因为加了spring-security中的过滤链，所以导致springmvc HandlerExceptionResolver
 *               不能处于最外层拦截
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-9-25 上午9:20:26
 * 
 */
public class BidExceptionFilter extends OncePerRequestFilter  {
	private static Logger logger = Logger.getLogger(BidExceptionFilter.class);
//  注意这里的MessageSource 是DelegateMessageSource 不是ReloadableResourceBundleMessageSource
//	private MessageSource messageResource;

	private String errorPage;

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

 
	 
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
		     
		    super.doFilter(request, response, filterChain);
		} catch (RuntimeException e) {
			handlerException(e, request, response);
		} catch (Exception ex) {
			handlerException(ex, request, response);
		}
		
	}

	public void handlerException(Exception ex, HttpServletRequest request,
			HttpServletResponse response) {
		try {
//			setMessageResource();
	 
			String reqType = request.getHeader("X-Requested-With");
            response.reset();
			String errorMsg = getExceptionMessage(ex, request);
			request.setAttribute("errorMsg", errorMsg);
			if ("XMLHttpRequest".equals(reqType)) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				ResponseEntity re = new ResponseEntity();
				re.setResStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				re.setResMsg(errorMsg);
				JSONObject json = JsonUtils.getJsonFromBean(re,DateUtils.DATE_HH_MM, new String[] {});
//				String callback = request.getParameter("callback");
//		 		if (StringUtils.isNotEmpty(callback)) {
//		 	 		String str = callback + "("+json.toString()+")";
//		 	 		ResponseUtils.responseScript(response, str);
//		 		} else {
		 			ResponseUtils.responseJson(response, json.toString());
//		 		}
 
			} else {
				if (errorPage != null) {
					request.getRequestDispatcher(errorPage).forward(request,response);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	
	private String getExceptionMessage(Exception ex, HttpServletRequest request) {

		String message = "";
		BaseException base = null;
        Throwable throwa =  ex.getCause();
        try {
        	if (throwa instanceof BaseException) {
        	   base =  (BaseException)throwa;
        		message = base.getErrorMsg();
        	} else {
        		message = ex.getMessage();
        	}
        
        }catch (Exception e) {
        	logger.error(e.getMessage(), e);
		}
		return message;
	}
	
//	private void setMessageResource() {
//		if (messageResource != null) {
//			return;
//		}
//		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
//		messageResource = (ReloadableResourceBundleMessageSource) context.getBean("messageResource");
// 
//	}


	

}
