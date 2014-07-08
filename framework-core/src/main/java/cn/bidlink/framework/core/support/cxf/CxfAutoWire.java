package cn.bidlink.framework.core.support.cxf;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.frontend.ClientFactoryBean;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.springframework.beans.BeanUtils;

import cn.bidlink.framework.core.annotation.AutowiredService;
import cn.bidlink.framework.core.commons.Constants;
import cn.bidlink.framework.core.exceptions.InnerErrorException;
import cn.bidlink.framework.core.support.Autowire;
import cn.bidlink.framework.core.support.RemoteConfig;
 /**
 * @description:	 TODO add description
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-8-30		下午4:10:12
 *
 */
public class CxfAutoWire implements Autowire {
	
	public static  CxfAutoWire instance = null; 
	
	public static Map<String, JaxWsProxyFactoryBean> cacheMap = new HashMap<String, JaxWsProxyFactoryBean>();
	public static Map<String, ClientProxyFactoryBean> cacheClientMap = new HashMap<String, ClientProxyFactoryBean>();
	
	private CxfAutoWire(){}
	
	public synchronized static CxfAutoWire getInstance() {
		if (instance == null) {
			instance = new CxfAutoWire();
			cacheMap.clear();
		}
		return instance;
	}

	@Override
	public Object autoWireInject(AutowiredService ars, Class<?> interfaces,
			RemoteConfig remoteConfig) {
		String [] groupKeys = ars.groupKey();
		String groupKey = "";
		if (!ArrayUtils.isEmpty(groupKeys)) {
			groupKey = groupKeys[0]; //默认取第一个key
		} else {
			groupKey = remoteConfig.getDefaultWsGroupKey();
		}
		if (StringUtils.isEmpty(groupKey)) {
			return null;
		}
		
		Object obj = remoteConfig.getGroupConfigMap().get(groupKey);
		if (obj == null) {
			throw new InnerErrorException(interfaces.getName() +"AutowiredService groupkey ["+groupKey+"] is not existing!");
		}
		Object objRes = null;
		
		CxfServiceConfig cxfConfig = (CxfServiceConfig)obj;
		
		if (cxfConfig.getBaseUrl().endsWith("wsdl")) {
			ClientProxyFactoryBean proxyFactory = createClientProxyFactoryBean(cxfConfig, groupKey);
			proxyFactory.setAddress(getRemoteUrl(cxfConfig, interfaces));
			return proxyFactory.create(interfaces);
		} else {
			JaxWsProxyFactoryBean proxyFactory = createJaxWsProxyFactory(cxfConfig, groupKey);
			proxyFactory.setAddress(getRemoteUrl(cxfConfig, interfaces));
			objRes =  proxyFactory.create(interfaces);
		}
		return objRes;
	}
	
	
	/**
	 * 
	 * @description 与原来 老系统集成
	 * @param    CxfAutoWire设定文件
	 * @return  ClientProxyFactoryBean DOM对象
	 * @throws
	 */
	private ClientProxyFactoryBean createClientProxyFactoryBean(CxfServiceConfig cxfConfig,String groupKey) {
			if (cacheClientMap.containsKey(groupKey)) {
				return cacheClientMap.get(groupKey);
			}
			ClientProxyFactoryBean proxyFactory = new ClientProxyFactoryBean(
	        		new ClientFactoryBean(new CXFServiceFactoryBean(Constants.CXF_NAME_SPACE))
	        );
		    Map<String,Object> outProps = new HashMap<String,Object>();
		    if (StringUtils.isEmpty(cxfConfig.getAction())) {
		    	 outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);  
		    } else {
		    	 outProps.put(WSHandlerConstants.ACTION, cxfConfig.getAction());  
		    }
		    if (StringUtils.isEmpty(cxfConfig.getPasswordType())) {
		    	outProps.put(WSHandlerConstants.PASSWORD_TYPE,WSConstants.PW_TEXT);//服务器端配置的用户名  
		    } else {
		    	outProps.put(WSHandlerConstants.PASSWORD_TYPE, cxfConfig.getPasswordType());
		    }
	        outProps.put(WSHandlerConstants.USER, cxfConfig.getUser());
	        outProps.put(WSHandlerConstants.PW_CALLBACK_REF, cxfConfig.getPasswordCallbackHandler()); 
	        proxyFactory.getInInterceptors().add(new LoggingInInterceptor());
	        proxyFactory.getOutInterceptors().add(new LoggingOutInterceptor());
	        proxyFactory.getOutInterceptors().add(new WSS4JOutInterceptor(outProps));
	        cacheClientMap.put(groupKey, proxyFactory);
	    	return  proxyFactory;
	        
	}
	
	
	private JaxWsProxyFactoryBean createJaxWsProxyFactory(CxfServiceConfig cxfConfig,String groupKey) {
		if (cacheMap.containsKey(groupKey)) {
			return cacheMap.get(groupKey);
		}
	    JaxWsProxyFactoryBean proxyFactory = BeanUtils.instantiateClass(JaxWsProxyFactoryBean.class);
	    Map<String,Object> outProps = new HashMap<String,Object>();
	    if (StringUtils.isEmpty(cxfConfig.getAction())) {
	    	 outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);  
	    } else {
	    	 outProps.put(WSHandlerConstants.ACTION, cxfConfig.getAction());  
	    }
	    if (StringUtils.isEmpty(cxfConfig.getPasswordType())) {
	    	outProps.put(WSHandlerConstants.PASSWORD_TYPE,WSConstants.PW_TEXT);//服务器端配置的用户名  
	    } else {
	    	outProps.put(WSHandlerConstants.PASSWORD_TYPE, cxfConfig.getPasswordType());
	    }
        outProps.put(WSHandlerConstants.USER, cxfConfig.getUser());
        outProps.put(WSHandlerConstants.PW_CALLBACK_REF, cxfConfig.getPasswordCallbackHandler()); 
        proxyFactory.getInInterceptors().add(new LoggingInInterceptor());
        proxyFactory.getOutInterceptors().add(new LoggingOutInterceptor());
        proxyFactory.getOutInterceptors().add(new WSS4JOutInterceptor(outProps));
        cacheMap.put(groupKey, proxyFactory);
        
        
		return proxyFactory;
	}
	
	/**
	 * @description 取得远程URL
	 * @param rsc  配置文件
	 * @param fieldType 接口
	 * @return  String DOM对象
	 * @throws
	 */
	private String getRemoteUrl(CxfServiceConfig cxfConfig,Class<?> fieldType) {
		StringBuffer buf = new StringBuffer();
		buf.append(cxfConfig.getBaseUrl());
		if (cxfConfig.getBaseUrl().endsWith("wsdl")) {
			return buf.toString();
		}
		if (cxfConfig.getBaseUrl().endsWith("/")) {
			buf.append(fieldType.getSimpleName());
		} else {
             buf.append("/").append(fieldType.getSimpleName());
		}
		return buf.toString();
	}
 

 

}

