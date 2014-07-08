/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>ExtendedLevel.java</p>
 *   
 */
package cn.bidlink.framework.log4j.level;

import org.apache.log4j.Level;
import org.apache.log4j.net.SyslogAppender;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
public class ExtendedLevel {
	protected static final class Extended extends Level {
		private static final long serialVersionUID = 1L;

		private Extended(final int level, final String levelName, final int syslogEquivalent) {
			super(level, levelName, syslogEquivalent);
		}
	}

	public static final Level OPERATE_LOG_LEVEL = new Extended(20050, "OPERATE", SyslogAppender.LOG_LOCAL0);

}
