package cn.bidlink.fileserver.decrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import cn.bidlink.fileserver.annotation.Crypt;
import cn.bidlink.fileserver.util.Constants;
import cn.bidlink.fileserver.util.CryptoUtils;
import cn.bidlink.framework.redis.RedisCache;
import cn.bidlink.framework.redis.RedisCacheFactory;

/**
 * 
 * @date 2013-2-4
 * @author changzhiyuan
 * @desc 默认token解析
 * 用生产者消费者模式，token产生放入redis中，消费从redis中取
 */
@Crypt(appid = "default")
public class DafaultCryptToken implements CryptToken {

	// token有效时间
	private static final int validseconds = 10 * 60;
	private static Logger logger = Logger.getLogger(DafaultCryptToken.class);
	private static final int tokenUsedLimit = 2;

	// redis缓冲
	private RedisCache redisCache = RedisCacheFactory.getRedisCache();

	public DafaultCryptToken() {

	}

	/**
	 * @token
	 * @pwd
	 * @desc 解析方式time_appid_cmpid
	 * @desc 先解base64，在解密
	 */
	public boolean decrypt(String token) {
		if (token == null) {
			return false;
		}
		String trimToken = token.trim();
		Object otokenRemainTimes = redisCache.getObject(Constants.TOKEN_PREFIX
				+ trimToken);
		if(logger.isInfoEnabled())
		{
			logger.info("otokenRemainTimes="+otokenRemainTimes);
		}
		if (otokenRemainTimes == null) {
			return false;
		}

		int itokenRemainTimes = Integer.parseInt(otokenRemainTimes.toString());
		itokenRemainTimes--;
		if (itokenRemainTimes == 0) {
			redisCache.del(Constants.TOKEN_PREFIX + trimToken);
		} else {
			redisCache.setObject(Constants.TOKEN_PREFIX + trimToken,
					validseconds, String.valueOf(itokenRemainTimes));
		}

		return true;
	}

	@Override
	public String encrypt(String text) {
		CryptoUtils cryto = new CryptoUtils();
		long current = System.currentTimeMillis();

		String key = cryto.basicEncrypt(String.valueOf(current), null);
		key = cryto.base64EncodeString(key.getBytes()).trim();
		redisCache.setObject(Constants.TOKEN_PREFIX + key, validseconds,
				String.valueOf(tokenUsedLimit));
		if (logger.isInfoEnabled()) {
			logger.info("#key=" + key);
		}
		return key;
	}

	public static void main(String args[]) {
		CryptoUtils cryptoUtil = new CryptoUtils();
		String key = cryptoUtil.basicEncrypt(
				String.valueOf(System.currentTimeMillis()), null);
		System.out.println(key);

		String aa = "6lKI8Rjua8DTshn1RKuJc+Rtx/RP/NfV";
		String cc = cryptoUtil.base64EncodeString(aa.getBytes()).trim();
		System.out.println("@@@" + cc);

		byte[] dd = Base64.decodeBase64(cc);
		System.out.println("@@@" + new String(dd));

		System.out.println(cryptoUtil.basicDecrypt(aa, null));

		// QXF5cnRaZ1ZsUktybEt5OC9zU3ZKNW5JeWZZeldocHM=

		DafaultCryptToken d = new DafaultCryptToken();
		System.out.println(d
				.decrypt("QXF5cnRaZ1ZsUktybEt5OC9zU3ZKNW5JeWZZeldocHM="));
	}

}
