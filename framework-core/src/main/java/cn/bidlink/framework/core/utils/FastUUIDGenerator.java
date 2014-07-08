package cn.bidlink.framework.core.utils;

import java.util.UUID;
 /**
 * @description:	UUID  
 * @since    Ver 1.0
 * @author   <a href="mailto:dejian.liu@ebnew.com">dejian.liu</a>
 * @Date	 2012	2012-8-6		下午3:10:11
 *
 */
public class FastUUIDGenerator {
	public static String genUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
		
	}

}

