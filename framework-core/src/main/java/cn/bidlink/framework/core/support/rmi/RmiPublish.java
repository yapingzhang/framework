package cn.bidlink.framework.core.support.rmi;

import java.rmi.RemoteException;
import java.util.List;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.web.context.support.XmlWebApplicationContext;

import cn.bidlink.framework.core.exceptions.InnerErrorException;
import cn.bidlink.framework.core.support.AuthInfo;
import cn.bidlink.framework.core.support.Publish;
import cn.bidlink.framework.core.utils.holder.PropertiesHolder;

 /**
 * @description:	Rmi 发布适配
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-8-30		上午11:16:56
 *
 */
public class RmiPublish implements Publish {
	private static Logger logger = Logger.getLogger(RmiPublish.class);
	
	private static RmiPublish instance = null;
	
	private RmiPublish(){}
	
	private  XmlWebApplicationContext applicationContext = null;
	
	private String contextPath = "";
    
	private  List<String> allowIps;
	
	/**
	 * 是否启用安全检查
	 */
	private boolean isEnabledAuth = false;
	/**
	 * 是否启用SSL
	 */
	private boolean isEnabledSsl = false;
	/**
	 * 认证信息
	 */
	private AuthInfo authInfo;
	
	public synchronized static RmiPublish getInstance(XmlWebApplicationContext appContext,
			String contextPath,List<String> allowIps,
			boolean isEnabledAuth,
			boolean isEnabledSsl,
			AuthInfo authInfo) {
		if (instance == null) {
			instance = new RmiPublish(); 
			instance.applicationContext = appContext;
			instance.allowIps = allowIps;
			if(StringUtils.isNotEmpty(contextPath) && contextPath.length() > 1) {
				instance.contextPath = contextPath + "/";	
			}
			instance.isEnabledAuth = isEnabledAuth;
			instance.authInfo = authInfo;
			instance.isEnabledSsl = isEnabledSsl;
		}
		return instance;
	}
      
 
	@SuppressWarnings("static-access")
	@Override
	public void publishService(Object obj, Class<?> interfaceCls) {
		try {
			String remoteServiceName = interfaceCls.getSimpleName() + "Rmi";
			if (applicationContext.containsBean(remoteServiceName)) {
				return;
			}
	
			RmiServiceExporter rmiServiceExporter = BeanUtils.instantiateClass(RmiServiceExporter.class);
			rmiServiceExporter.setService(obj);
			rmiServiceExporter.setServiceName(contextPath+interfaceCls.getSimpleName());			
			rmiServiceExporter.setServiceInterface(interfaceCls);
			Integer port = 1199;
			String host = "";
			if(applicationContext.containsBeanDefinition("propertiesHolder")) {
				PropertiesHolder property = (PropertiesHolder) applicationContext.getBean("propertiesHolder");
				String portStr = property.getProperty("rmi.remote.port");
				host = property.getProperty("rmi.remote.host");
				if (StringUtils.isNotEmpty(portStr) && NumberUtils.isDigits(portStr)) {
					port = Integer.parseInt(portStr);
				}
				if (StringUtils.isEmpty(host)) {
					throw new InnerErrorException("rmi host cannot null!");
				}
			}
			 System.setProperty("java.rmi.server.hostname" ,host);
			logger.info("rmiAddress------------" + logRmiAddress(contextPath,host,port, interfaceCls));
//			rmiServiceExporter.setRegistryHost(host);
			if (isEnabledSsl) {
//				System.setProperty("javax.net.ssl.trustStore","G:/bid.store");
//				System.setProperty("javax.net.ssl.keyStore","G:/bid.store");
//				System.setProperty("javax.net.ssl.keyStorePassword","123456");
				
				SslRMIClientSocketFactory sslClientSocketFactory = new SslRMIClientSocketFactory();
				SslRMIServerSocketFactory sslServerSocketFactory = new SslRMIServerSocketFactory(new String[]{"SSL_DH_anon_WITH_RC4_128_MD5"},new String[]{"TLSv1"},false);
				rmiServiceExporter.setServerSocketFactory(sslServerSocketFactory);
				rmiServiceExporter.setClientSocketFactory(sslClientSocketFactory);
			}
 
			rmiServiceExporter.setInterceptors(new Object[]{
					new BidPerformanceMonitorInterceptor(allowIps,isEnabledAuth,authInfo)
			});
			
 			rmiServiceExporter.setRegistryPort(port);
			rmiServiceExporter.setReplaceExistingBinding(false);
			rmiServiceExporter.prepare();
			
			applicationContext.getBeanFactory().registerSingleton(remoteServiceName, rmiServiceExporter);
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private String logRmiAddress(String contextPath,String host,Integer port,Class<?> interfaceCls) {
		StringBuffer buf = new StringBuffer();
		try {
//			InetAddress addr = InetAddress.getLocalHost();
			buf.append("rmi://").append(host).append(":")
			.append(port).append("/").append(contextPath).append("")
			.append(interfaceCls.getSimpleName());
		} catch (Exception e) {
		     logger.error(e.getMessage(),e);
		}
		return buf.toString();
	}

 
	
}

