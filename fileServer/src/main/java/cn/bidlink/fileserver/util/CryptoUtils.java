package cn.bidlink.fileserver.util;

import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;
import org.jasypt.commons.CommonUtils;
import org.jasypt.util.binary.BasicBinaryEncryptor;
import org.jasypt.util.binary.StrongBinaryEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

/**
 * @description: 对称加密(为了使并发加，解密安全，所以去掉静态方法.)
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-10-9 上午11:01:04
 * 
 */
public class CryptoUtils {

	/**
	 * MD5
	 */
	private static final String defaultPwd = "6864F6FE8A2A46CD64DF5284B1F40A14";

	/**
	 * 对文件分段加密，分段大小10(M)
	 */
	private static final int CACHE_SIZE = 10;

	/**
	 * 
	 * @description 加密
	 * @param message
	 *            待加密信息
	 * @param key
	 *            密码 default defaultPwd
	 * @return
	 */
	public String basicEncrypt(String message, String key) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(CommonUtils.isNotEmpty(key) ? key
				: defaultPwd);
		return textEncryptor.encrypt(message);
	}

	/**
	 * 
	 * @description 解密
	 * @param message
	 *            待解密信息
	 * @param key
	 *            密文
	 * @return
	 */
	public String basicDecrypt(String message, String key) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(CommonUtils.isNotEmpty(key) ? key
				: defaultPwd);
		return textEncryptor.decrypt(message);
	}

	/**
	 * @description 强加密
	 * @param message
	 *            待加密信息
	 * @param key
	 *            密码 default defaultPwd
	 * @return
	 */
	public String strongEncrypt(String message, String key) {
		StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
		textEncryptor.setPassword(CommonUtils.isNotEmpty(key) ? key
				: defaultPwd);
		return textEncryptor.encrypt(message);
	}

	/**
	 * @description 解密
	 * @param message
	 *            待解密信息
	 * @param key
	 *            密码 default defaultPwd
	 * @return
	 */
	public String strongDecrypt(String message, String key) {
		StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
		textEncryptor.setPassword(CommonUtils.isNotEmpty(key) ? key
				: defaultPwd);
		return textEncryptor.decrypt(message);
	}

	/**
	 * @description 对文件流加密
	 * @param byteFile
	 *            待加密字节数组
	 * @param key
	 *            密码 default defaultPwd
	 * @return
	 */
	public synchronized byte[] basicBinaryEncrypt(byte[] byteBuf, String key) {
		BasicBinaryEncryptor binaryEncryptor = new BasicBinaryEncryptor();
		binaryEncryptor.setPassword(CommonUtils.isNotEmpty(key) ? key
				: defaultPwd);
		return binaryEncryptor.encrypt(byteBuf);
	}

	/**
	 * @description 对文件解密
	 * @param byteBuf
	 *            待解密字节数组
	 * @param key
	 *            key 密码 default defaultPwd
	 * @return
	 */
	public synchronized byte[] basicBinaryDecrypt(byte[] byteBuf, String key) {
		BasicBinaryEncryptor binaryEncryptor = new BasicBinaryEncryptor();
		binaryEncryptor.setPassword(CommonUtils.isNotEmpty(key) ? key
				: defaultPwd);
		return binaryEncryptor.decrypt(byteBuf);
	}

	/**
	 * @description 对文件流强加密
	 * @param byteFile
	 *            待加密字节数组
	 * @param key
	 *            密码 default defaultPwd
	 * @return
	 */
	public synchronized byte[] strongBinaryEncrypt(byte[] byteBuf, String key) {
		StrongBinaryEncryptor binaryEncryptor = new StrongBinaryEncryptor();
		binaryEncryptor.setPassword(CommonUtils.isNotEmpty(key) ? key
				: defaultPwd);
		return binaryEncryptor.encrypt(byteBuf);
	}

	/**
	 * @description 对文件解密
	 * @param byteBuf
	 *            待解密字节数组
	 * @param key
	 *            key 密码 default defaultPwd
	 * @return
	 */
	public synchronized byte[] strongBinaryDecrypt(byte[] byteBuf, String key) {
		StrongBinaryEncryptor binaryEncryptor = new StrongBinaryEncryptor();
		binaryEncryptor.setPassword(CommonUtils.isNotEmpty(key) ? key
				: defaultPwd);
		return binaryEncryptor.decrypt(byteBuf);
	}







	/**
	 * 创建密钥
	 * 
	 * @return 密钥字符串
	 * @throws Exception
	 */
	public synchronized String createAESCipher() throws Exception {
		KeyGenerator gen = KeyGenerator.getInstance("AES");
		gen.init(256);
		SecureRandom rom = new SecureRandom();
		gen.init(rom);
		SecretKey key = gen.generateKey();
		byte[] buf = key.getEncoded();
		return base64EncodeString(buf);
	}




	public String base64EncodeString(byte[] text) {
		return Base64.encodeBase64String(text);
	}

	public byte[] base64Encode(byte[] text) {
		return Base64.encodeBase64(text);
	}

	public byte[] base64Decode(String text) {
		return Base64.decodeBase64(text);
	}

	public byte[] base64Decode(byte[] text) {
		return Base64.decodeBase64(text);
	}



}
