//package cn.bidlink.framework.core.i18n;
//
//import java.text.MessageFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Properties;
//
//import org.springframework.context.ResourceLoaderAware;
//import org.springframework.context.support.AbstractMessageSource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.core.io.support.ResourcePatternResolver;
//
//import cn.bidlink.framework.core.utils.PropertiesHelper;
//import cn.bidlink.framework.core.utils.PropertiesUtils;
//
//public class MessageSourceImpl extends AbstractMessageSource implements
//		ResourceLoaderAware {
//	
//
//	private static final String PROPERTIES_SUFFIX = ".properties";
//
//	private ResourceLoader resourceLoader;
//	
//	private String[] basenames;
//	
//	private final Map<String, Map<Locale, List<String>>> cachedFilenames =
//			new HashMap<String, Map<Locale, List<String>>>();
//
//	private final Map<String, PropertiesHelper> cachedProperties = new HashMap<String, PropertiesHelper>();
//
//	private final Map<PropertiesHelper, Map<String,Map<Locale, MessageFormat>>> cachedMessageFormats =
//		new HashMap<PropertiesHelper, Map<String,Map<Locale, MessageFormat>>>();
//
//
//
//	@Override
//	public void setResourceLoader(ResourceLoader resourceLoader) {
//		this.resourceLoader = resourceLoader;
//	}
//
//	public void setBasenames(String[] basenames) {
//		this.basenames = basenames;
//	}
//
//	@Override
//	protected MessageFormat resolveCode(String code, Locale locale) {
//		for (String basename : this.basenames) {
//			List<String> filenames = calculateAllFilenames(basename, locale);
//			for (String filename : filenames) {
//				PropertiesHelper propHolder = getProperties(filename);
//				MessageFormat result = getMessageFormat(propHolder, code, locale);
//				if (result != null) {
//					return result;
//				}
//			}
//		}
//		return null;
//	}
//
//	protected List<String> calculateAllFilenames(String basename, Locale locale) {
//		synchronized (this.cachedFilenames) {
//			Map<Locale, List<String>> localeMap = this.cachedFilenames.get(basename);
//			if (localeMap != null) {
//				List<String> filenames = localeMap.get(locale);
//				if (filenames != null) {
//					return filenames;
//				}
//			}
//			List<String> filenames = new ArrayList<String>(7);
//			filenames.addAll(calculateFilenamesForLocale(basename, locale));
//			filenames.add(basename);
//			if (localeMap != null) {
//				localeMap.put(locale, filenames);
//			} else {
//				localeMap = new HashMap<Locale, List<String>>();
//				localeMap.put(locale, filenames);
//				this.cachedFilenames.put(basename, localeMap);
//			}
//			return filenames;
//		}
//	}
//	
//	protected List<String> calculateFilenamesForLocale(String basename, Locale locale) {
//		List<String> result = new ArrayList<String>(3);
//		String language = locale.getLanguage();
//		String country = locale.getCountry();
//		String variant = locale.getVariant();
//		StringBuilder temp = new StringBuilder(basename);
//
//		temp.append('_');
//		if (language.length() > 0) {
//			temp.append(language);
//			result.add(0, temp.toString());
//		}
//
//		temp.append('_');
//		if (country.length() > 0) {
//			temp.append(country);
//			result.add(0, temp.toString());
//		}
//
//		if (variant.length() > 0 && (language.length() > 0 || country.length() > 0)) {
//			temp.append('_').append(variant);
//			result.add(0, temp.toString());
//		}
//
//		return result;
//	}
//	
//	protected PropertiesHelper getProperties(String filename) {
//		synchronized (this.cachedProperties) {
//			PropertiesHelper propHolder = this.cachedProperties.get(filename);
//			if (propHolder != null) {
//				return propHolder;
//			}
//			return refreshProperties(filename);
//		}
//	}
//	
//	protected PropertiesHelper refreshProperties(String filename) {
//		String locationPattern = filename + PROPERTIES_SUFFIX;
//		Properties properties = PropertiesUtils.loadProperties(
//				((ResourcePatternResolver)this.resourceLoader), new String[]{locationPattern});
//		if(properties != null) {
//			PropertiesHelper propertiesHelper = new PropertiesHelper(properties);
//			this.cachedProperties.put(filename, propertiesHelper);
//			this.cachedMessageFormats.put(
//					propertiesHelper, new HashMap<String, Map<Locale, MessageFormat>>());
//			return propertiesHelper;
//		}
//		return null;
//	}
//	
//	private MessageFormat getMessageFormat(PropertiesHelper propertiesHelper, String code, Locale locale) {
//		
//		synchronized (this.cachedMessageFormats) {
//			Map<String, Map<Locale, MessageFormat>> codeMap = this.cachedMessageFormats.get(propertiesHelper);
//			if(codeMap == null) {
//				return null;
//			}
//			Map<Locale, MessageFormat> localeMap = codeMap.get(code);
//			if (localeMap != null) {
//				MessageFormat result = localeMap.get(locale);
//				if (result != null) {
//					return result;
//				}
//			}
//			String msg = propertiesHelper.getProperty(code);
//			if (msg != null) {
//				if (localeMap == null) {
//					localeMap = new HashMap<Locale, MessageFormat>();
//					codeMap.put(code, localeMap);
//				}
//				MessageFormat result = createMessageFormat(msg, locale);
//				localeMap.put(locale, result);
//				return result;
//			}
//			return null;
//		}
//	}
//}
