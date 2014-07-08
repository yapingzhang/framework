package cn.bidlink.framework.core.support.context;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.context.support.XmlWebApplicationContext;

import cn.bidlink.framework.core.commons.Constants;
import cn.bidlink.framework.core.support.zookeeper.ZookeeperTransporter;
import cn.bidlink.framework.core.support.zookeeper.curator.CuratorZookeeperClient;
import cn.bidlink.framework.core.support.zookeeper.curator.CuratorZookeeperTransporter;
import cn.bidlink.framework.core.utils.holder.PropertiesHolder;

/**
 * @description: spring context容器对象
 * @version Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2013-5-10 上午10:13:27
 */
public class BidFrameworkSpringContext extends XmlWebApplicationContext {

	private static Logger logger = Logger.getLogger(BidFrameworkSpringContext.class);
	
	private ZookeeperTransporter zookeeperTransporter;
	
	private CuratorZookeeperClient zookeeperClient;

	private String connections = null;
	
	public BidFrameworkSpringContext() {
		try {
			ZooConfLoad.initContext(this);
			connections = Constants.getConfigZooConnectUrl();
			if(StringUtils.isNotEmpty(connections)) {
				zookeeperTransporter = new CuratorZookeeperTransporter();
				if(StringUtils.isNotEmpty(Constants.ZOO_DIGEST_SECRET)) {
					zookeeperClient = zookeeperTransporter.connect(connections,Constants.ZOO_DIGEST_SECRET.getBytes());
				} else {
					zookeeperClient = zookeeperTransporter.connect(connections);
				}
				
				ZooConfLoad.loadProperties(zookeeperClient);
				ZooConfLoad.loadLog4jXml(zookeeperClient);
				ZooConfLoad.loadXml(zookeeperClient);
				ZooConfLoad.loadLocalApp(zookeeperClient);
			}
		}catch (Exception e) {
 			logger.error(e.getMessage(),e);
		}
	}
	

	@Override
	protected void initBeanDefinitionReader(
			XmlBeanDefinitionReader beanDefinitionReader) {
		try {
			if(StringUtils.isNotEmpty(connections)) {
				ConcurrentHashMap<String,byte[]> xmlMap = ZooConfLoad.getXmlMap();
				if(!xmlMap.isEmpty()) {
					for(Iterator<Map.Entry<String, byte[]>> it = xmlMap.entrySet().iterator();it.hasNext();) {
						Map.Entry<String, byte[]> entry = it.next(); 
						ByteArrayResource byteArrayResource = new ByteArrayResource(entry.getValue(),entry.getKey());
						beanDefinitionReader.loadBeanDefinitions(byteArrayResource);
					}
				}	
			}
		} catch (Exception e) {
 			logger.error(e.getMessage(),e);
		}
 	}
	
	@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
			throws BeansException, IOException {
		Properties prop = PropertiesHolder.getProperties();
		PropertiesPropertySource propertySource = new PropertiesPropertySource("remoteProperties", prop);
		this.getEnvironment().getPropertySources().addFirst(propertySource);
		super.loadBeanDefinitions(beanFactory);
	}
	

}
