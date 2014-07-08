package cn.bidlink.framework.core.support;

import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @description: 远程服务注入完成事件
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-8-26 下午3:41:21
 * 
 */
public class BidInjectRemoteObjectEvent {
	private XmlWebApplicationContext xmlWebApplicationContext;
	private Object sourceObj;
	private Object injectObj;
 

	public BidInjectRemoteObjectEvent(
			XmlWebApplicationContext xmlWebApplicationContext,
			Object sourceObj,
			Object injectObj) {
		super();
		this.xmlWebApplicationContext = xmlWebApplicationContext;
		this.sourceObj = sourceObj;
		this.injectObj = injectObj;
 
	}

	public XmlWebApplicationContext getXmlWebApplicationContext() {
		return xmlWebApplicationContext;
	}

	public void setXmlWebApplicationContext(
			XmlWebApplicationContext xmlWebApplicationContext) {
		this.xmlWebApplicationContext = xmlWebApplicationContext;
	}

	public Object getSourceObj() {
		return sourceObj;
	}

	public void setSourceObj(Object sourceObj) {
		this.sourceObj = sourceObj;
	}

	public Object getInjectObj() {
		return injectObj;
	}

	public void setInjectObj(Object injectObj) {
		this.injectObj = injectObj;
	}

    

}
