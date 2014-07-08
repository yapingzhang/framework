package cn.bidlink.framework.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PropertiesPersister;


/**
 * 可以支持表达式的PropertiesFactoryBean
 * 
 * @author jinsiwang
 * @author pengtaoli
 *
 */
public class PropertiesFactoryBean extends
		org.springframework.beans.factory.config.PropertiesFactoryBean {
	
	private PropertiesMerger propertiesMerger = new PropertiesMerger();
	
	@SuppressWarnings("unchecked")
	@Override
	protected Properties mergeProperties() throws IOException {
		//是否覆盖本地的Properties
		boolean localOverride = getLocalOverride();
		//加载locations指定的Properties
		Properties[] localProperties = getLocalProperties();
		//获取所有的Properties
		List<Properties> allProperties = new ArrayList<Properties>();
		List<Properties> propertiesList = loadProperties();
		if(!CollectionUtils.isEmpty(propertiesList)) {
			allProperties.addAll(propertiesList);
		}
		////
		if (localOverride) {
			allProperties.addAll(CollectionUtils.arrayToList(localProperties));
		}

		//最终的返回Properties
		Properties result = new Properties();
		//合并properties
		if(!CollectionUtils.isEmpty(allProperties)) {
			propertiesMerger.mergeProperties(result, allProperties);
		}
		return result;
	}
	

	/**
	 * 加载所有的Properties并以List返回
	 * 
	 * @return
	 * @throws IOException
	 */
	protected List<Properties> loadProperties() throws IOException {
		Resource[] locations = getLocations();
		PropertiesPersister propertiesPersister = getPropertiesPersister();
		String fileEncoding = getFileEncoding();
		boolean ignoreResourceNotFound = getIgnoreResourceNotFound();
		
		List<Properties> propertiesList = new ArrayList<Properties>();
		if (!ObjectUtils.isEmpty(locations)) {
			Properties tempProperties;
			for (Resource location : locations) {
				tempProperties = new Properties();
				if (logger.isInfoEnabled()) {
					logger.info("Loading properties file from " + location);
				}
				InputStream is = null;
				try {
					is = location.getInputStream();

					String filename = null;
					try {
						filename = location.getFilename();
					} catch (IllegalStateException ex) {
						// resource is not file-based. See SPR-7552.
					}

					if (filename != null && filename.endsWith(XML_FILE_EXTENSION)) {
						propertiesPersister.loadFromXml(tempProperties, is);
					}
					else {
						if (fileEncoding != null) {
							propertiesPersister.load(tempProperties, new InputStreamReader(is, fileEncoding));
						}
						else {
							propertiesPersister.load(tempProperties, is);
						}
					}
					propertiesList.add(tempProperties);
				}
				catch (IOException ex) {
					if (ignoreResourceNotFound) {
						if (logger.isWarnEnabled()) {
							logger.warn("Could not load properties from " + location + ": " + ex.getMessage());
						}
					}
					else {
						throw ex;
					}
					
				}
				finally {
					if (is != null) {
						is.close();
					}
				}
			}
		}
		return propertiesList;
	}
	
	public boolean getLocalOverride() {
		return (Boolean)ReflectionTestUtils.getField(this, "localOverride");
	}
	
	public Properties[] getLocalProperties() {
		return (Properties[])ReflectionTestUtils.getField(this, "localProperties");
	}
	
	public Resource[] getLocations(){
		
		return (Resource[])ReflectionTestUtils.getField(this, "locations");
	}
	
	
	public PropertiesPersister getPropertiesPersister(){
		
		return (PropertiesPersister)ReflectionTestUtils.getField(this, "propertiesPersister");
	}
	
	public String getFileEncoding(){
		
		return (String)ReflectionTestUtils.getField(this, "fileEncoding");
	}
	
	public boolean getIgnoreResourceNotFound(){
		
		return (Boolean)ReflectionTestUtils.getField(this, "ignoreResourceNotFound");
	}
}
