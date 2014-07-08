package cn.bidlink.fileserver.util;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.FileUtils;

/**
 * MD5加密对象
 * 
 * @author wangjinsi
 *
 */
public abstract class MD5 {
	
	private final static String DM5 = "MD5";
	
	/**
	 * MD5加密
	 * @param password 密码串（未加密）
	 * @return 密码串（已加密）
	 */
    public static String encrypt(String password) {
        try {
            MessageDigest alg = MessageDigest.getInstance(DM5);
            alg.update(password.getBytes());
            byte[] digesta = alg.digest();
            return byte2hex(digesta);
        } catch (NoSuchAlgorithmException NsEx) {
            return null;
        }
    }
    
    /**
	 * MD5加密
	 * @param password 密码串（未加密）
	 * @return 密码串（已加密）
	 */
    public static String encrypt(byte [] data) {
        try {
            MessageDigest alg = MessageDigest.getInstance(DM5);
            alg.update(data);
            byte[] digesta = alg.digest();
            return byte2hex(digesta);
        } catch (NoSuchAlgorithmException NsEx) {
            return null;
        }
    }
    
    private static String byte2hex(byte[] bstr) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < bstr.length; n++) {
            stmp = (java.lang.Integer.toHexString(bstr[n] & 0XFF));
            if (stmp.length() == 1){
            		hs.append("0");
            		hs.append(stmp);
            }else{
                hs.append(stmp);
            }    
        }
        return hs.toString();
    }
  
    public static void main(String[] args) throws Exception {
//		System.out.println(MD5.encrypt("admin"));
		byte [] buf = FileUtils.readFileToByteArray(new File("G:/电子书/数据库/MySQL技术内幕InnoDB存储引擎.pdf"));
		System.out.println(encrypt(buf));
		
	} 
}