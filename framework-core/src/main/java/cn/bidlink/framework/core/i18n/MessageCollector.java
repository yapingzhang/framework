//package cn.bidlink.framework.core.i18n;
//
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//import org.springframework.context.MessageSource;
//
///**
// * 消息容器 
// *
// */
//public class MessageCollector {
//
//    private MessageSource messageSource;
//
//    public void setMessageSource(MessageSource messageSource) {
//		this.messageSource = messageSource;
//	}
//
//	/**
//     * 普通消息
//     */
//    private Map<String, Object> info = new HashMap<String, Object>(0);
//
//    /**
//     * 警告消息
//     */
//    private Map<String, Object> warning = new HashMap<String, Object>(0);
//    
//    /**
//     * 错误消息
//     */
//    private Map<String, Object> error = new HashMap<String, Object>(0);
//
//    public void addInfo(String name, String messageKey, Object[] parameters, Locale locale) {
//        String message = this.messageSource.getMessage(messageKey, parameters, locale);
//        this.info.put(name, message);
//    }
//    
//    public void addInfo(String name, String message) {
//    	this.info.put(name, message);
//    }
//    
//    public void addInfoAll(Map<String, Object> infoMap) {
//    	this.info.putAll(infoMap);
//    }
//
//    public void addError(String name, String messageKey, Object[] parameters, Locale locale) {
//        String message = this.messageSource.getMessage(messageKey, parameters, locale);
//        this.error.put(name, message);
//    }
//    
//    public void addError(String name, String message) {
//    	this.error.put(name, message);
//    }
//    
//    public void addErrorAll(Map<String, Object> errorMap) {
//    	this.error.putAll(errorMap);
//    }
//
//    public void addWarning(String name, String messageKey, Object[] parameters, Locale locale) {
//        String message = this.messageSource.getMessage(messageKey, parameters, locale);
//        this.warning.put(name, message);
//    }
//    
//    public void addWarning(String name, String message) {
//    	this.warning.put(name, message);
//    }
//    
//    public void addWarningAll(Map<String, Object> warningMap) {
//    	this.warning.putAll(warningMap);
//    }
//	
//    @Deprecated
//    public Map<String, Object> getInfo() {
//    	return info;
//    }
//    
//	public Map<String, Object> getInfos() {
//		return info;
//	}
//
//	@Deprecated
//	public Map<String, Object> getWarning() {
//		return warning;
//	}
//	
//	public Map<String, Object> getWarnings() {
//		return warning;
//	}
//
//	@Deprecated
//	public Map<String, Object> getError() {
//		return error;
//	}
//	
//	public Map<String, Object> getErrors() {
//		return error;
//	}
//	
//	public Map<String, Object> getAll() {
//		Map<String, Object> messages = new HashMap<String, Object>();
//		messages.putAll(info);
//		messages.putAll(warning);
//		messages.putAll(error);
//		return messages;
//	}
//
//	public void clear() {
//		this.info.clear();
//		this.warning.clear();
//		this.error.clear();
//	}
//    
//}
