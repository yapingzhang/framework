package cn.bidlink.fileserver.util;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 工具类-用于时间日期处理
 */
public final class TimeUtil {
	/**
	 * 私有构造函数，防止被创建实例
	 */
	private TimeUtil() {

	}

	/**
	 * 以时间为基础创建ID
	 * 
	 * @param name
	 * @return
	 */
	public static synchronized String getIdByTime(String name) {
		return name + "-" + getCurrDate() + "-" + getCurrTime()
				+ getRandomNumber();
	}

	/**
	 * 以时间为基础创建ID
	 * 
	 * @param name
	 * @return
	 */
	public static synchronized String getIdByTime() {
		return getCurrDate() + "-" + getCurrTime() + "-" + getRandomNumber();
	}

	/**
	 * 取得字符串格式的当前日期
	 * 
	 * @return
	 */
	public static String getCurrDate() {
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy.MM.dd");
		return formatter.format(date);
	}

	/**
	 * 取得字符串格式的当前时间
	 * 
	 * @return
	 */
	public static String getCurrTime() {
		Date date = new Date();
		Format formatter = new SimpleDateFormat("HH.mm.ss");
		return formatter.format(date);
	}

	public static String getRandomNumber() {
		Random random = new Random();
		String retval = String.valueOf(random.nextInt(100));
		return retval;
	}

}