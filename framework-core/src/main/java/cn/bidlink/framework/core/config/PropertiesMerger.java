package cn.bidlink.framework.core.config;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bidlink.framework.core.utils.StringUtils;

 

public class PropertiesMerger {
	
	private Pattern placeholderPattern = Pattern.compile(".*(\\$\\{.+\\}).*");
	
	public void mergeProperties(Properties original, List<Properties> mergedProperties) {
		for(Properties properties : mergedProperties) {
			Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<Object, Object> entry = iterator.next();
				String finalValue = null;
				String originalValue = (String) original.get(entry.getKey());
				String mergedValue = (String) entry.getValue();
				if(StringUtils.hasLength(mergedValue)) {
					if(!StringUtils.hasLength(originalValue)) {
						originalValue = "";
					}
					if(mergedValue.startsWith("append:")) {
						finalValue = originalValue + mergedValue.substring("append:".length()); 
					} else if(mergedValue.startsWith("first:")) {
						//TODO
						//finalValue = mergedValue.substring("first:".length()) + originalValue; 
					} else {
						finalValue = mergedValue;
					}
				} else {
					finalValue = originalValue;
				}
				if(finalValue == null) {
					finalValue = "";
				}
				if(entry.getKey() != null) {
					original.put(entry.getKey(), finalValue);
				}
			}
		}
		//解析相互关联的引用值
		parse(original);
	}
	
	public void parse(Properties properties) {
		Iterator<Entry<Object, Object>> iterator = properties.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<Object, Object> entry = iterator.next();
			String key = (String) entry.getKey();
			String originalValue = (String) entry.getValue();
			String associatedValue = getAssociatedValue(properties, originalValue);
			if(!originalValue.equals(associatedValue)) {
				properties.put(key, associatedValue);
			}
			
		}
	}
	
	
	public String getAssociatedValue(Properties properties, String value) {
		String oringnalValue = value;
		String placeholder = parsePlaceholder(oringnalValue);
		if(!StringUtils.hasLength(placeholder)) {
			return oringnalValue;
		}
		String subKey = placeholder.substring(2, placeholder.length() - 1);
		String subValue = properties.getProperty(subKey);
		if(!StringUtils.hasLength(subValue)) {
			subValue = "";
		}
		String subPlaceholder = parsePlaceholder(subValue);
		if(StringUtils.hasLength(subPlaceholder)) {
			subValue = getAssociatedValue(properties, subValue);
		}
		while(oringnalValue.indexOf("${" + subKey + "}") > -1) {
			oringnalValue = oringnalValue.replace("${" + subKey + "}", subValue);
		}
		return StringUtils.hasLength(parsePlaceholder(oringnalValue)) ? getAssociatedValue(properties, oringnalValue) : oringnalValue;
	}
	
	public String parsePlaceholder(String text) {
		if (text != null) {
			Matcher matcher = placeholderPattern.matcher(text);
			if (matcher.matches()) {
				return matcher.group(1);
			}
		}
		return null;
	}
	
}
