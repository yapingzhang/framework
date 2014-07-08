package cn.bidlink.framework.core.loadbalance;

import java.util.Collection;

/**
 * @remark 第三方源码
 * @author zhongfeng (mailto:djangofan@163.com)
 * @quoter dejian.liu 
 * @quoteDate 2012-12-01
 * @param <E>
 */
public class GCDAlg {
	public static int gcd(int n, int m) {
		int x;
		while (m % n != 0) {
			x = m % n;
			m = n;
			n = x;
		}
		return n;
	}

	public static int gcdN(Integer[] digits, int length) {
		if (digits == null || digits.length == 0) {
			return 0;
		}
		if (1 == length)
			return digits[0];
		else
			return gcd(digits[length - 1], gcdN(digits, length - 1));
	}

	public static int gcdN(Collection<Integer> digits, int length) {
		Integer[] di = new Integer[digits.size()];
		digits.toArray(di);
		return gcdN(di, length);
	}
}
