package cn.bidlink.framework.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import cn.bidlink.framework.core.commons.Constants;
import cn.bidlink.framework.core.exceptions.BusinessException;
import cn.bidlink.framework.core.exceptions.GeneralException;
import cn.bidlink.framework.core.exceptions.InnerErrorException;
import cn.bidlink.framework.core.exceptions.SecurityExpception;

 

public class HandlerExceptionResolverImpl implements HandlerExceptionResolver {

	/**
	 * 日志对象
	 */
	protected final Logger logger = Logger.getLogger(getClass());  

	/**
	 * 异常时返回的视图
	 */
	private String exceptionView;

	/**
	 * 放入到ModelAndView中的Expetion的名称
	 */
	private String exceptionName;

	private MessageSource messageSource;

	public void setExceptionView(String exceptionView) {
		this.exceptionView = exceptionView;
	}

	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception) {
		//判断是何种类型的异常
		Throwable finalException = (exception.getCause() != null ? exception.getCause() : null);
		if(finalException == null) {
			finalException = exception;
		}
		//先将错误信息打印出来，以便查看
		logger.error(finalException.getMessage(), finalException);
		//如果不是业务或安全异常则统一为内部服务器异常
		if (!(finalException instanceof BusinessException) && !(finalException instanceof SecurityExpception)) {
			String key = "framework.exception.inner_error_exception";
			String message = messageSource.getMessage(key, null, request.getLocale());
			finalException = new InnerErrorException(message, finalException);
		}
		//是否使用视图显示异常，还是直接抛出
		if (StringUtils.hasText(exceptionView)) {
			ModelAndView errorView = new ModelAndView(exceptionView);
			if (StringUtils.hasText(exceptionName)) {
				errorView.getModel().put(exceptionName, finalException);
			} else {
				errorView.getModel().put(Constants.WEB_ACTION_EXCEPTION_SCOPE_NAME, finalException);
			}
			return errorView;
		} else {
			//如果没配置异常提示页面将抛出显示原始异常
			if(RuntimeException.class.isAssignableFrom(finalException.getClass())) {
				throw (RuntimeException)finalException;
			} else {
				throw new GeneralException("action execute exception", finalException);
			}
		}
	}

}
