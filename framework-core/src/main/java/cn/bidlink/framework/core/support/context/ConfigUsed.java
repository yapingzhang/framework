package cn.bidlink.framework.core.support.context;

import java.io.Serializable;

/**
 * @description: 配置文件使用记录
 * @version Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2013-6-13 上午9:33:02
 */
public class ConfigUsed implements Serializable {

	private static final long serialVersionUID = -7800368711834489760L;

	/**
	 * 配置路径
	 */
	private String configPath;

	/**
	 * 应用路径
	 */
	private String appPath;
	
	/**
	 * 进程ID
	 */
	private String processId;

	/**
	 * 应用名称
	 */
	private String appName;

	/**
	 * 应用服务IP
	 */
	private String serverIp;

	/**
	 * 应用服务端口
	 */
	private String serverPort;

	/**
	 * 应用配置信息
	 */
	private String desc;

	/**
	 * 创建时间
	 */
	private long createTime;

	/**
	 * 更新时间
	 */
	private long updateTime;

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public String getAppPath() {
		return appPath;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

}
