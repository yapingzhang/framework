package cn.bidlink.framework.core.support.context;

import org.springframework.context.ApplicationEvent;

/**
 * @description:节点配置信息被修改事件
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2013-6-4 下午5:07:05
 */
public class ConfigChangedEvent extends ApplicationEvent{

	private static final long serialVersionUID = 7530993205830517196L;

	
	@Override
	public ConfigChangedInfo getSource() {
 		Object obj =  super.getSource();
 		if(obj != null) {
 			return (ConfigChangedInfo)obj;
 		}
 		return new ConfigChangedInfo();
	}
	public ConfigChangedEvent(Object source) {
		super(source);
	}
}
