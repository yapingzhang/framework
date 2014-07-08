/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>ExtendedPatternParser.java</p>
 *   
 */
package cn.bidlink.framework.log4j.varia;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
public class ExtendedFilter extends Filter {
	boolean acceptOnMatch = false;
	int levelMin;
	int levelMax;

	public int getLevelMin() {
		return levelMin;
	}

	public void setLevelMin(final int levelMin) {
		this.levelMin = levelMin;
	}

	public int getLevelMax() {
		return levelMax;
	}

	public void setLevelMax(final int levelMax) {
		this.levelMax = levelMax;
	}

	@Override
	public int decide(final LoggingEvent lgEvent) {

		int inputLevel = lgEvent.getLevel().toInt();
		if (inputLevel < levelMin) {
			return Filter.DENY;
		}

		if (inputLevel > levelMax) {
			return Filter.DENY;
		}

		if (acceptOnMatch) {
			return Filter.ACCEPT;
		} else {
			return Filter.NEUTRAL;
		}
	}

}
