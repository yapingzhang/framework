package cn.bidlink.framework.core.model;

import java.io.Serializable;

/**
 * @description: Model 基础类
 * @since Ver 1.0
 * @author <a href="mailto:dejian.liu@ebnew.com">dejian.liu</a>
 * @Date 2012 2012-8-3 下午2:35:43
 * 
 */
public class BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 排序字段
	 */
	private String sortColumns;

	public String getSortColumns() {
		return sortColumns;
	}

	public void setSortColumns(String sortColumns) {
		this.sortColumns = sortColumns;
	}

	 
}
