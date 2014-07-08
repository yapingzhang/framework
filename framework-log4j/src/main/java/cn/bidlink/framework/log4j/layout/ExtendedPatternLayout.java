/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>ExtendedPatternLayout.java</p>
 *   
 */
package cn.bidlink.framework.log4j.layout;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternParser;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
public class ExtendedPatternLayout extends PatternLayout {

	public ExtendedPatternLayout() {
		this(DEFAULT_CONVERSION_PATTERN);
	}

	public ExtendedPatternLayout(final String pattern) {
		super(pattern);
	}

	@Override
	public PatternParser createPatternParser(final String pattern) {
		PatternParser parser;
		if (pattern == null) {
			parser = new ExtendedPatternParser(DEFAULT_CONVERSION_PATTERN);
		} else {
			parser = new ExtendedPatternParser(pattern);
		}
		return parser;
	}
}