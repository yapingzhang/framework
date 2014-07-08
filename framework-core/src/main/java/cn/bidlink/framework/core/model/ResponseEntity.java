package cn.bidlink.framework.core.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 响应信息
 * @since Ver 1.0
 * @author <a href="mailto:dejian.liu@ebnew.com">dejian.liu</a>
 * @Date 2012 2012-8-6 下午6:13:09
 * 
 */
@SuppressWarnings("serial")
public class ResponseEntity implements Serializable {

	/**
	 * 响应状态 HTTP 状态信息
	 */
	private int resStatus;

	/**
	 * 响应信息
	 */
	private String resMsg;

	/**
	 * 响应参数
	 */
	private Map<String, Object> params = new HashMap<String, Object>();

	public ResponseEntity() {
	}

	public ResponseEntity(int resStatus, String resMsg) {
		super();
		this.resStatus = resStatus;
		this.resMsg = resMsg;
	}

	public int getResStatus() {
		return resStatus;
	}

	public void setResStatus(int resStatus) {
		this.resStatus = resStatus;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
 
	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

}
