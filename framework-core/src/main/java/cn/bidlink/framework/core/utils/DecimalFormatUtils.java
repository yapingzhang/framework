package cn.bidlink.framework.core.utils;

import java.text.DecimalFormat;

/**
 * 数据格式化
 */
public class DecimalFormatUtils {

	private static final String outFormat = "###########0.0#####";

	/**
	 * 格式化long数据
	 * @param number long数据
	 * @return 数据
	 */
	public static String format(long number) {
		DecimalFormat format = new DecimalFormat(outFormat);
		return format.format(number);
	}

	/**
	 * 格式化float数据
	 * @param number float数据
	 * @return 数据
	 */
	public static String format(float number) {
		DecimalFormat format = new DecimalFormat(outFormat);
		return format.format(number);
	}

	/**
	 * 格式化double数据
	 * @param number double数据
	 * @return 数据
	 */
	public static String format(double number) {
		DecimalFormat format = new DecimalFormat(outFormat);
		return format.format(number);
	}
}
