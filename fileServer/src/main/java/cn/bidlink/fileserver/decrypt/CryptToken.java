package cn.bidlink.fileserver.decrypt;

/**
 * @date 2013-02-04
 * @author changzhiyuan
 * @desc token解析
 */
public interface CryptToken {

	/**
	 * 根据指定的pwd解析token
	 * 
	 * @param token
	 * @param pwd
	 * @return
	 */
	boolean decrypt(String token);
	
	/**
	 * 加密信息
	 * 
	 * @param token
	 * @param pwd
	 * @return
	 */
	String encrypt(String text);
	
	
}
