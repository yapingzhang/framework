package cn.bidlink.fileserver.decrypt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.bidlink.fileserver.annotation.Crypt;
import cn.bidlink.fileserver.annotation.PackageUtil;

/**
 * @date 2013-2-5
 * @author changzhiyuan
 * @desc 对不同解密方法的封装
 */
public class CryptContext {

	public final static String defaultKey = "default";
	private static Logger logger = Logger.getLogger(CryptContext.class);
	private static CryptContext instance = new CryptContext();

	/**
	 * 解密对象
	 */
	private Map<String, CryptToken> strategies = new HashMap<String, CryptToken>();

	/**
	 * 获取所有注解为Decrypt的类，加入到解密的队列
	 */
	private CryptContext() {

		String packageName = CryptToken.class.getPackage().getName();
		
		List<String> classNames = PackageUtil.getClassName(packageName, false);

		if (logger.isInfoEnabled()) {
			logger.info("classNames=" + classNames);
		}

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		for (String className : classNames) {
			try {
				
				Class<?> clazz = loader.loadClass(className);
				Crypt decripy = clazz.getAnnotation(Crypt.class);
				if (logger.isInfoEnabled()) {
					logger.info("decripy=" + decripy);
				}
				if (decripy != null) {
					if (logger.isInfoEnabled()) {
						logger.info("decripy=" + decripy.appid());
					}

					for (Class<?> superInterface : clazz.getInterfaces()) {
						if (superInterface.equals(CryptToken.class)) {
							String key = decripy.appid();
							strategies.put(key,
									(CryptToken) clazz.newInstance());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);

			}
		}

	}

	public static CryptContext getInstance() {
		return instance;
	}

	public void addStragy(String appid, CryptToken parseToken) {
		strategies.put(appid, parseToken);
	}

	public boolean decrypt(String appid, String token) {

		CryptToken parseToken = strategies.get(appid);
		if (parseToken == null) {
			parseToken = strategies.get(defaultKey);
		}
		return parseToken.decrypt(token);
	}
	
	public String encrypt(String appid,String text) {

		CryptToken parseToken = strategies.get(appid);
		if (parseToken == null) {
			parseToken = strategies.get(defaultKey);
		}
		return parseToken.encrypt(text);
	}

	public static void main(String args[]) {
		// String aa = DecryptToken.class.getPackage().;
		//DecryptContext.getInstance();
		String packageName = "222";
		String className = "grams/pmp-fileservice-tomcat9911/webapps/fileserver/WEB-INF/classes/cn/bidlink/fileserver/decrypt/DecryptContext";
		if(className.contains("/"))
		{
			className=className.substring(className.lastIndexOf("/")+1);
			className = packageName+"."+className;
		}
		System.out.println(className);
		
	}
}