package cn.bidlink.framework.core.enums;

/**
 * @description: 服务类别
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-8-29 上午9:47:14
 */
public enum ServiceType {
	RMI("rmi"), WS_XFILE("xfile"), WS_CXF("cxf");
	
	private String type;

	private ServiceType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static ServiceType parseToEnum(String type) {
		ServiceType[] values = ServiceType.values();
		for (ServiceType afe : values) {
			if (afe.type.equals(type)) {
				return afe;
			}
		}
		return null;
	}
	
}
