package cn.bidlink.framework.core.utils.pager;

/**
 * 
 * @description 分页信息读取工具
 * @author dj
 * @since 2008-7-25上午11:07:40
 */
public class PageHelper {

	/**
	 * 默认每页记录数
	 */
	private static int DEFAULT_PAGESIZE = 10;

	/**
	 * 操作
	 */
	public static String FIRST_METHOD = "first";

	public static String PREVIOUS_METHOD = "previous";

	public static String NEXT_METHOD = "next";

	public static String LAST_METHOD = "last";

	public static String GO_METHOD = "go";

	/**
	 * 得到Pager
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param totalRows
	 *            int
	 * @param onePageSize
	 *            int
	 * @return Pager
	 */
	public static Pager getPager(PagerParam param, int totalRows,
			int onePageSize) {
		Pager pager = new Pager(totalRows, onePageSize);
		String currentPage = param.getCurrentPage();
		if (currentPage != null && !currentPage.equals("")) {
			pager.refresh(Integer.parseInt(currentPage));
		}
		String pagerMethod = param.getPagerMethod();
		if (pagerMethod != null && !"".equals(currentPage)) {
			if (pagerMethod.equals(FIRST_METHOD)) {
				pager.first();
			} else if (pagerMethod.equals(PREVIOUS_METHOD)) {
				pager.previous();
			} else if (pagerMethod.equals(NEXT_METHOD)) {
				pager.next();
			} else if (pagerMethod.equals(LAST_METHOD)) {
				pager.last();
			} else if (pagerMethod.equals(GO_METHOD)) {
				pager.go(Integer.parseInt(currentPage));
			}
		}
		return pager;
	}

	/**
	 * 得到Pager 默认的PageSize
	 * 
	 * @param param
	 *            PagerParam
	 * @param totalRows
	 *            int
	 * @return Pager
	 */
	public static Pager getPager(PagerParam param, int totalRows) {
		return getPager(param, totalRows, DEFAULT_PAGESIZE);
	}

}
