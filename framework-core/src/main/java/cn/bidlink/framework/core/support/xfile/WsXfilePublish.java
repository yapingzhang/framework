package cn.bidlink.framework.core.support.xfile;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.XmlWebApplicationContext;

import cn.bidlink.framework.core.support.Publish;
 /**
 * @description:	 TODO add description
 * @since    Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2012	2012-8-30		上午11:23:07
 *
 */
public class WsXfilePublish implements Publish {
	
	private static Logger logger = Logger.getLogger(WsXfilePublish.class);
  

	private XmlWebApplicationContext applicationContext = null;
	 
	private static WsXfilePublish instance = null;
	
	private WsXfilePublish(){}
 
    
	public synchronized static WsXfilePublish getInstance(XmlWebApplicationContext appContext) {
		if (instance == null) {
			instance = new WsXfilePublish(); 
			instance.applicationContext = appContext;
		}
		return instance;
	}

	@Override
	public void publishService(Object obj, Class<?> interfaceCls) {

        logger.error("Waiting for the realization");

	}

}

