package cn.bidlink.framework.util;

import java.io.UnsupportedEncodingException;

import sun.nio.cs.ext.EUC_CN;

public abstract class CharsetUtils {

	@SuppressWarnings("restriction")
	public static String getSimplifiedChineseCharacter() {
		EUC_CN gb2312 = new EUC_CN();
		return gb2312.encode("啊").toString();
		
//		String[] content = gb2312.getDecoderIndex2();
//		StringBuilder simplifiedChinese = new StringBuilder();
//		simplifiedChinese.append(content[0].substring(content[0].indexOf('啊')));
//		for (int i = 1; i < content.length; i++) {
//			simplifiedChinese.append(content[i]);
//		}
//		return simplifiedChinese.toString();
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(getSimplifiedChineseCharacter());
	}

}
