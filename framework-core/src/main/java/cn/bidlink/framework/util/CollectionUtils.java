package cn.bidlink.framework.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import cn.bidlink.framework.core.exceptions.GeneralException;

/**
 * Spring工具类CollectionUtils的扩展类.
 * 
 * @author jinshiwang
 */
public abstract class CollectionUtils extends org.springframework.util.CollectionUtils{

	
	/**
	 * Check whether the given Iterator contains the given element.
	 * @param iterator the Iterator to check
	 * @param element the element to look for
	 * @return <code>true</code> if found, <code>false</code> else
	 */
	public static <K, V> boolean containsKey(Map<K, V> map, K key) {
		Iterator<K> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			Object candidate = iterator.next();
			if (ObjectUtils.nullSafeEquals(candidate, key)) {
				return true;
			}
		}
		return false;
	}
	
	public static <K, V> boolean containsValue(Map<K, V> map, K value) {
		Iterator<V> iterator = map.values().iterator();
		while (iterator.hasNext()) {
			Object candidate = iterator.next();
			if (ObjectUtils.nullSafeEquals(candidate, value)) {
				return true;
			}
		}
		return false;
	}
		
	public static <K, V> List<K> findKeysOfValue(Map<K, V> map, V value) {
		if (isEmpty(map)) {
			return null;
		}
		List<K> keys = new ArrayList<K>();
		Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<K, V> entry = iterator.next();
			if(entry.getValue() != null && value.equals(entry.getValue())) {
				keys.add(entry.getKey());
			}
		}
		return keys;
	}
	
	public static void mergePropertiesIntoMap(Properties props, Map<String, Object> map) {
		if (map == null) {
			throw new IllegalArgumentException("Map must not be null");
		}
		if (props != null) {
			for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements();) {
				String key = (String) en.nextElement();
				String value = (String)props.getProperty(key);
				if (value == null) {
					// Potentially a non-String value...
					value = (String) props.get(key);
				}
				String existentValue = (String)map.get(key);
				if(StringUtils.hasLength(existentValue) 
				   && StringUtils.hasLength(value) 
				   && value.startsWith("append:")) {
						value += existentValue;
				}
				map.put(key, value);
			}
		}
	}
	
	public static <T> List<T> getPropertyValues(List<?> list, String propertyName) {
		List<T> values = new ArrayList<T>();
		if(isEmpty(list) || !StringUtils.hasLength(propertyName)) {
			return values;
		}
		Method getMethod = null;
		for(Object obj : list) {
			try {
				if(getMethod == null) {
					getMethod = obj.getClass().getDeclaredMethod("get" + String.valueOf(propertyName.charAt(0)).toUpperCase() + propertyName.substring(1));
				}
				if(getMethod != null) {
					values.add((T)getMethod.invoke(obj));
				}
			} catch (Exception e) {
				throw new GeneralException(e);
			}
		}
		return values;
	}
	
}
