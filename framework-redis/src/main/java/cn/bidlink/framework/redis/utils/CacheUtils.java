/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>CachedUtils.java</p>
 *   
 */
package cn.bidlink.framework.redis.utils;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import cn.bidlink.framework.redis.annotation.Cache;

/**
 * 
 * @description: key.
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
public class CacheUtils {

	public static String parseKey(String template, Object[] parameters) {
		ExpressionParser parser = new SpelExpressionParser();
		ParserContext templateContext = new TemplateParserContext();
		String expressTemplate = template.replaceAll("\\$\\{([^\\$]+){1}\\}", "#{#$1}");

		Expression expression = parser.parseExpression(expressTemplate, templateContext);

		StandardEvaluationContext context = new StandardEvaluationContext();
		int i = 0;
		for (Object obj : parameters) {
			context.setVariable(String.format("p%s", i), obj);
			i++;
		}
		return expression.getValue(context, String.class);
	}

	@SuppressWarnings("deprecation")
	public static String parseKey(Cache cache, Object[] parameters) {
		String key = cache.key();
		if (cache.simple()) {
			return key;
		}

		String[] els = cache.els();
		if (els.length == 0) {
			return parseKey(key, parameters);
		}
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext teslaContext = new StandardEvaluationContext();
		teslaContext.setVariable("p", parameters);
		Object[] parsedArgs = new Object[els.length];
		int i = 0;
		for (String el : els) {
			String invention = parser.parseExpression(el).getValue(teslaContext, String.class);
			parsedArgs[i] = invention;
			i++;
		}
		return String.format(key, parsedArgs);
	}
}
