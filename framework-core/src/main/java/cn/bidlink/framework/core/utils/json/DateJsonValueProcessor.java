package cn.bidlink.framework.core.utils.json;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcessor implements JsonValueProcessor {
	public  static String formatPattern = "yyyy-MM-dd";


	private SimpleDateFormat simpleDateFormat = null;
	
	public DateJsonValueProcessor() {
         this(formatPattern);
	}

	public DateJsonValueProcessor(String datePattern) {
			if (datePattern != null && !"".equals(datePattern)) {
				synchronized (formatPattern) {
					formatPattern = datePattern;
				}
			}
			simpleDateFormat = new SimpleDateFormat(formatPattern);
	
	}

	public Object processArrayValue(Object paramObject,
			JsonConfig paramJsonConfig) {

		if (paramObject != null
				&& paramObject.getClass() == java.util.Date.class) {
			return simpleDateFormat.format((Date) paramObject);
		}
		return "";
	}

	public Object processObjectValue(String paramString, Object paramObject,
			JsonConfig paramJsonConfig) {
		if (paramObject != null
				&& paramObject.getClass() == java.util.Date.class) {
			return simpleDateFormat.format((Date) paramObject);
		}
		return "";
	}
}
