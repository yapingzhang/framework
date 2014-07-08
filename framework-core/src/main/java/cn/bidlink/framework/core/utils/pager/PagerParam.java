package cn.bidlink.framework.core.utils.pager;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @description 从Request中获取分页参数
 * @author dj
 * @since 2008-7-25上午11:19:31
 */
public class PagerParam implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3244699759102854782L;

	/**
	 * 页号
	 */
	private String currentPage;

	/**
	 * 操作
	 */
	private String pagerMethod;
	
	/**
	 * 开始页
	 */
	private Integer start;
	
	/**
	 * 限制条数
	 */
	private Integer limit;

	/**
	 * 构造函数
	 * 
	 * @param request
	 *            HttpServletRequest
	 */
	public PagerParam(HttpServletRequest request) {
		pagerMethod = request.getParameter("pagerMethod");
		currentPage = request.getParameter("currentPage");
		start = Integer.valueOf(request.getParameter("start") == null ? "0" : request.getParameter("start"));
		limit = Integer.valueOf(request.getParameter("limit") == null ? String.valueOf(Integer.MAX_VALUE) : request.getParameter("limit"));
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public String getPagerMethod() {
		return pagerMethod;
	}
	
	public void setCurrentPage(String currentPage){
		this.currentPage = currentPage;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	
	
}
