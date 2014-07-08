package cn.bidlink.framework.core.commons;

import java.io.Serializable;
import java.util.List;

/**
 * refactor paging for xfire
 * 
 * @author wangjinsi
 *
 */
public class PagingDto extends Paging implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public List<?> getResult() {
		return super.getResult();
	}

	public final void setPageTotal(int pageTotal) {
	}

	public final void setFirstIndex(int firstIndex) {
	}

	public final void setLastPage(int lastPage) {
	}

	public final void setNextPage(int nextPage) {
	}

	public final void setFirstPage(int firstPage) {
	}

	public final void setEndPage(int endPage) {
	}

	public final void setUsed(boolean used) {
	}

}
