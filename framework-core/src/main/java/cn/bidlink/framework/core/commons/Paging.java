/*
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>Paging.java</p>
 *   
 * Created on 2010-11-11 by Administrator
 *
 * Revision 1.3  2010/11/11 03:58:16  jinsiwang
 * 添加分页信息
 *
 * Revision 1.2  2010/11/11 03:20:43  jinsiwang
 * 提交CommonDao的Hibernate实现代码
 *
 * Revision 1.1  2010/11/11 00:11:42  jinsiwang
 * 添加公共代码
 *
 */

package cn.bidlink.framework.core.commons;

import java.io.Serializable;
import java.util.List;

/**
 * 存放分页信息
 * 
 */
public class Paging implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	
	private final static int DEFAULT_PAGE_SIZE = 20;

	/**
	 *  当前页号
	 */
	private int currentPageNum = 0;

	/**
	 *  每页的最大记录数
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/**
	 *  总记录数
	 */
	private int recordTotal = 0;
	
	/**
	 * 是否计算总记录数，默认不计算
	 */
	private boolean autoCalculateTotal = true;
	
	private Object calculateTotalHandler;
	
	/**
	 * 分页查询后的结果信息
	 */
	private List<?> result;
	
	public Paging(){}
	
	public Paging(int currentPageNum, int pageSize){
		this.currentPageNum = currentPageNum;
		this.pageSize = pageSize;
	}
	
	/*
	 * 返回当前页号
	 */
	public int getCurrentPageNum() {
		return currentPageNum;
	}

	public void setCurrentPageNum(int currentPageNum) {
		if(currentPageNum < 1) {
			this.currentPageNum = 1;
		} else {
			this.currentPageNum = currentPageNum;
		}
	}

	public int getPageSize() {
		return pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 总记录数
	 * 
	 * @return
	 */
	
	public int getRecordTotal() {
		// 总页数如果小于1则当前页数直接为0
		if (getPageTotal() < 1) {
			currentPageNum = 0;
		} else {
			if (currentPageNum < 1) {
				// 如果当前页号小于1，置为1
				currentPageNum = 1;
			} else if (currentPageNum > getPageTotal()) {
				currentPageNum = getPageTotal();
			}
		}
		return recordTotal;
	}
	
	public void setRecordTotal(int totalRecord) {
		this.recordTotal = totalRecord;
		if(getPageTotal() > 0) {
			if(getCurrentPageNum() > getPageTotal()) {
				currentPageNum = getPageTotal();
			} else if(getCurrentPageNum() < 1) {
				currentPageNum = 1;
			}
			
		} else {
			currentPageNum = 0;
		}
	}

	/**
	 * 总页数
	 * 
	 * @return
	 */
	public int getPageTotal() {
		int pageTotal = (recordTotal + getPageSize() - 1) / getPageSize();
		return pageTotal > 0 ? pageTotal : 0;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getResult() {
		return (List<T>) this.result;
	}

	public <T> void setResult(List<T> result) {
		this.result = result;
	}

	/**
	 * 取得第一条记录的位置
	 * 
	 * @return
	 */
	public int getFirstIndex() {
		return getCurrentPageNum() > 0 ? ((currentPageNum - 1)) * getPageSize() : 0;
	}
	
	/**
	 * 返回第一页页号
	 * 
	 * @return
	 */
	public int getFirstPage(){
		return hasFirstPage() ? 1 : 0;
	}
	
	/**
	 * 返回上一页页号
	 * 
	 * @return
	 */
	public int getLastPage(){
		return hasLastPage() ? currentPageNum - 1 : 0;
	}
	
	/**
	 * 返回上下一页页号
	 * 
	 * @return
	 */
	public int getNextPage(){
		return hasNextPage() ? currentPageNum + 1 : 0;
	}
	
	/**
	 * 返回上末页页号
	 * 
	 * @return
	 */
	public int getEndPage(){
		return hasEndPage() ? getPageTotal() : 0;
	}
	
	/**
	 * 是否有首页
	 * 
	 * @return
	 */
	public boolean hasFirstPage() {
		return currentPageNum > 1;
	}
	
	/**
	 * 是否有上一页
	 * @return
	 */
	public boolean hasLastPage() {
		return hasFirstPage();
	}
	
	/**
	 * 是否有下一页
	 * @return
	 */
	public boolean hasNextPage() {
		return currentPageNum < getPageTotal();
	}
	
	/**
	 * 是否有下一页
	 * @return
	 */
	public boolean hasEndPage() {
		return hasNextPage();
	}
	
	/**
	 * 是否能跳转页
	 * @return
	 */
	public boolean canGo() {
		return getPageTotal() > 1;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append(" currentPageNum=" + this.currentPageNum);
		str.append(" pageSize=" + this.pageSize);
		str.append(" recordTotal=" + this.recordTotal);
		str.append(" pageTotal=" + this.getPageTotal());
		str.append(" firstIndex=" + this.getFirstIndex());
		str.append(" ResultSize=" + (this.getResult() != null ? this.getResult().size() : 0));
		return str.toString();
	}

	public boolean isAutoCalculateTotal() {
		return autoCalculateTotal;
	}

	public void setAutoCalculateTotal(boolean autoCalculateTotal) {
		this.autoCalculateTotal = autoCalculateTotal;
	}

	public Object getCalculateTotalHandler() {
		return calculateTotalHandler;
	}

	public void setCalculateTotalHandler(Object calculateTotalHandler) {
		this.calculateTotalHandler = calculateTotalHandler;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
}
