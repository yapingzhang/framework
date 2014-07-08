package cn.bidlink.framework.core.support.cxf;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.spring.EndpointDefinitionParser.SpringEndpointImpl;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.springframework.web.context.support.XmlWebApplicationContext;

import cn.bidlink.framework.core.support.Publish;
import cn.bidlink.framework.core.support.cxf.security.ServerPasswordCallback;
import cn.bidlink.framework.core.utils.holder.PropertiesHolder;

/**
 * @description: TODO add description
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-8-30 上午11:20:11
 * 
 */
public class WsCxfPublish implements Publish {
	private static Logger logger = Logger.getLogger(WsCxfPublish.class);

	private XmlWebApplicationContext applicationContext = null;
 
	private static WsCxfPublish instance = null;
	
	private WsCxfPublish(){}
 
    
	public synchronized static WsCxfPublish getInstance(XmlWebApplicationContext appContext) {
		if (instance == null) {
			instance = new WsCxfPublish(); 
			instance.applicationContext = appContext;
		}
		return instance;
	}
	
	@Override
	public void publishService(Object obj, Class<?> interfaceCls) {
		try {

			String remoteServiceName = interfaceCls.getSimpleName() + "Ws";
			if (applicationContext.containsBean(remoteServiceName)) {
				return;
			}
		
			SpringBus sBus = (SpringBus) applicationContext.getBean("cxf");

			SpringEndpointImpl bidSpring = new SpringEndpointImpl(sBus, obj);
			bidSpring.setAddress("/" + interfaceCls.getSimpleName());
			logger.info("cxfAddress------------" + "/" + interfaceCls.getSimpleName());
			bidSpring.setImplementorClass(interfaceCls);

			Map<String, Object> inProps = new HashMap<String, Object>();
			inProps.put(WSHandlerConstants.ACTION,WSHandlerConstants.USERNAME_TOKEN);
			inProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
			ServerPasswordCallback serverPasswordCallback = new ServerPasswordCallback();
			serverPasswordCallback.setUsername(PropertiesHolder.getProperty("ws.cxf.server.user", "admin"));
			serverPasswordCallback.setPassword(PropertiesHolder.getProperty("ws.cxf.server.password", "admin"));
			inProps.put(WSHandlerConstants.PW_CALLBACK_REF,serverPasswordCallback);
			WSS4JInInterceptor wssin = new WSS4JInInterceptor(inProps);
			bidSpring.getInInterceptors().add(wssin);
			bidSpring.publish();
			applicationContext.getBeanFactory().registerSingleton(remoteServiceName, bidSpring);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
         
	}

}
