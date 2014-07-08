package cn.bidlink.framework.core.support.context;

/**
 * @description:节点配置信息被修改信息
 * @version Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2013-6-4 下午4:54:27
 */
public class ConfigChangedInfo {

	/**
	 * @description:节点类别
	 */
	public static enum NodeType {
		PROPERTIES, XML,LOG4J
	}

	/**
	 * @description:操作类别
	 */
	public static enum OptType {
		CREATE, DELETE, UPDATE
	}

	/**
	 * 配置节点路径
	 */
	private String nodePath;
	/**
	 * 配置节点变化后数据
	 */
	private String data;

	/**
	 * 节点类别
	 */
	private NodeType nodeType;

	/**
	 * 操作类别
	 */
	private OptType optType;

	public ConfigChangedInfo() {
	}
	
	public ConfigChangedInfo(String nodePath, String data, NodeType nodeType, OptType optType) {
		super();
		this.nodePath = nodePath;
		this.data = data;
		this.nodeType = nodeType;
		this.optType = optType;
	}

	public String getNodePath() {
		return nodePath;
	}

	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public OptType getOptType() {
		return optType;
	}

	public void setOptType(OptType optType) {
		this.optType = optType;
	}

}
