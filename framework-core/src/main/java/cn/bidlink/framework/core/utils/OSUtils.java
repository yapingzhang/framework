package cn.bidlink.framework.core.utils;

/**
 * 
 * @description:	 TODO add description
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2013-4-26 下午2:44:51
 */
public class OSUtils {
	
	/**
	 * @description 判断是否Linux系统
	 * @return
	 */
	public static boolean isLinux() {
		String osName = System.getProperty("os.name");
		if(osName.toLowerCase().startsWith("windows")) {
			return false;
		}
		return true;
	}

 
}
