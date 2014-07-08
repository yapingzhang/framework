package cn.bidlink.framework.core.utils;

import java.util.regex.Pattern;

import org.springframework.util.ObjectUtils;

import cn.bidlink.framework.core.exceptions.GeneralException;

/**
 * 字符串处理类
 */
public abstract class StringUtils extends org.springframework.util.StringUtils {
	
	/**
	 * 替换第几个字符串
	 * 
	 * @param source
	 * @param target
	 * @param replacement
	 * @param sequence 从1开始
	 * @return
	 */
	public static String replace(String source, String target, String replacement, int sequence) {
		int tempSequence = 0;
		int i = 0;
		int iterateIndex = -1;
		StringBuffer replacedString = new StringBuffer();
		while((iterateIndex = source.indexOf(target, i)) > 0) {
			tempSequence++;
			if(sequence == tempSequence) {
				replacedString.append(source.substring(0, iterateIndex));
				replacedString.append(replacement);
				replacedString.append(source.substring(iterateIndex + target.length()));
				break;
			}
			i = iterateIndex + target.length();
		}
		return replacedString.toString();
	}
	
	/**
	 * 二进制字节数组 -转- 16进制字符串
	 * @param bytes 二进制字节数组
	 * @return 16进制字符串
	 */
	public static String bytesToHexes(byte[] bytes) {
		StringBuffer hex = new StringBuffer();
		String perByteHex = null;
		for (int n = 0; n < bytes.length; n++) {
			perByteHex = (Integer.toHexString(bytes[n] & 0XFF));
			if (perByteHex.length() == 1)
				hex.append("0" + perByteHex);
			else
				hex.append(perByteHex);
		}
		return hex.toString().toUpperCase();
	}

	/**
	 * 16进制字符串 -转- 二进制字节数组
	 * @param hexes 16进制字符串
	 * @return 二进制字节数组
	 */
	public static byte[] hexesToBytes(String hexes) {
		return hexesToBytes(hexes.getBytes());
	}

	/**
	 * 16进制的字符串对应的字节数组 -转- 二进制字节数组
	 * @param hexBytes 16进制的字符串对应的字节数组
	 * @return 二进制字节数组
	 */
	public static byte[] hexesToBytes(byte[] hexBytes) {
		if ((hexBytes.length % 2) != 0) {
			throw new GeneralException();
		}
		byte[] bytes = new byte[hexBytes.length / 2];
		for (int n = 0; n < hexBytes.length; n += 2) {
			String item = new String(hexBytes, n, 2);
			bytes[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return bytes;
	}
	
	/**
	 * str是否存在strChar字符
	 * @param str 字符串
	 * @param strChar 匹配字符
	 * @return 不存在,返回-1；存在，返回位置；
	 */
    public static int safeIndexOf(String str, String strChar) {
    	if(!hasLength(str)) {
    		return -1;
    	}
        return str.indexOf(strChar);
    }

    /**
     * 去除字符串空格
     * @param str 字符串
     * @return 新字符串
     */
    public static String safeTrim(String str) {
    	if(!hasLength(str)) {
    		return str;
    	}
        return str.trim();
    }
    /**
     * 字符串的字节数组长度
     * @param str 字符串
     * @return 字节数组长度
     */
    public static int getByteSize(String str) {
        if (str != null) {
            return str.getBytes().length;
        }
        return 0;
    }
	
    /**
     * 判断是否为0-9的数字字符串, null和""都为false
     * 
     * @param str 字符串
     * @return true=是；false=否；
     */
    public static boolean isNumeric(String str) {
        if(!hasLength(str)) {
        	return false;
        }
        return Pattern.matches("[0-9]+", str);
    }
    /**
     * 格式化HTTP站点地址字符串，去掉http://
     * @param websit HTTP站点地址字符串
     * @return 格式化后站点地址
     */
	public static String websitFormat(String websit) {
		
		if(!hasLength(websit)) {
			return null;
		} 
		
		return websit.replaceAll("http://", "").trim()
			    .replaceAll("https://", "").trim();
	}
    
	/**
	 * 半角转全角
	 * @param input 待处理字符串
	 * @return 全角字符串
	 */
	public static String toSBC(String input) {
		if(!hasLength(input)) {
			return null;
		}
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000'; 
			} else if (c[i] < '\177') { 
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * @param input 待处理字符串
	 * @return 半角字符串
	 */
	public static String toDBC(String input) {
		if(!hasLength(input)) {
			return null;
		}
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String returnString = new String(c);
		return returnString;
	}
	
	public static String fill(String str, int width, String...placeholder) {
		StringBuffer filled = new StringBuffer();
		int zeroCount = (str == null ? width : width - str.length());
		String finalPlaceholder = "0";
		if(!ObjectUtils.isEmpty(placeholder)) {
			finalPlaceholder = placeholder[0];
		}
		for(int i = 0 ; i < zeroCount; i++) {
			filled.append(finalPlaceholder);
		}
		filled.append(str);
		return filled.toString();
	}

}
