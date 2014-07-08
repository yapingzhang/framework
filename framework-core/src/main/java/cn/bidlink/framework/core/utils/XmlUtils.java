package cn.bidlink.framework.core.utils;

import org.apache.log4j.Logger;
import org.dom4j.Node;

public abstract class XmlUtils {
	
	public static final Logger logger = Logger.getLogger(XmlUtils.class); 

	@SuppressWarnings("unchecked")
	public static <T> T safeValue(Node node, Class<T> clazz) {
		if(node == null || clazz == null)
			return null;
		String value = node.getText();
		try {
			if(clazz.equals(Integer.class)) {
				return (T)new Integer(value);
			} else if(clazz.equals(Long.class)) {
				return (T)new Long(value);
			} else if(clazz.equals(String.class)) {
				return (T)value;
			} else if(clazz.equals(Boolean.class)) {
				return (T)Boolean.valueOf(value);
			} else if(clazz.equals(Class.class)) {
				return (T)Class.forName(value);
			} 
		} catch (NullPointerException e) {
			logger.error(e);
			return null;
		} catch (ClassNotFoundException e) {
			logger.error(e);
			return null;
		}
		return null;
	}
}
