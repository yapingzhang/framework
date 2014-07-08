package cn.bidlink.framework.core.support;

import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @description: 远程服务注入完成事件
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-8-26 下午3:41:21
 * 
 */
@SuppressWarnings("serial")
public class BidInjectRemoteServiceFinishedEvent {
	private XmlWebApplicationContext xmlWebApplicationContext;
	private boolean isFinished;

	public BidInjectRemoteServiceFinishedEvent(
			XmlWebApplicationContext xmlWebApplicationContext,
			boolean isFinished) {
		super();
		this.xmlWebApplicationContext = xmlWebApplicationContext;
		this.isFinished = isFinished;
	}

	public XmlWebApplicationContext getXmlWebApplicationContext() {
		return xmlWebApplicationContext;
	}

	public void setXmlWebApplicationContext(
			XmlWebApplicationContext xmlWebApplicationContext) {
		this.xmlWebApplicationContext = xmlWebApplicationContext;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

}
