/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>ExtendedPatternParser.java</p>
 *   
 */
package cn.bidlink.framework.log4j.layout;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-10-14 下午03:00:20
 */
public class ExtendedPatternParser extends PatternParser {

	static final char HOST_NAME = 'H';
	static final char VM_NAME = 'V';
	static final char IP_ADDRESS = 'I';
	static final Map<String, PatternConverter> converters;
	static {
		Map<String, PatternConverter> tmp = new HashMap<String, PatternConverter>();
		tmp.put(String.valueOf(HOST_NAME), new HostPatternConverter());
		tmp.put(String.valueOf(VM_NAME), new VMNamePatternConverter());
		tmp.put(String.valueOf(IP_ADDRESS), new IPAddressPatternConverter());
		converters = Collections.unmodifiableMap(tmp);
	}

	public ExtendedPatternParser(final String pattern) {
		super(pattern);
	}

	public void finalizeConverter(final char formatChar) {
		PatternConverter pc = null;
		switch (formatChar) {
		case HOST_NAME:
			pc = ExtendedPatternParser.converters.get(String.valueOf(HOST_NAME));
			currentLiteral.setLength(0);
			addConverter(pc);
			break;
		case VM_NAME:
			pc = ExtendedPatternParser.converters.get(String.valueOf(VM_NAME));
			currentLiteral.setLength(0);
			addConverter(pc);
			break;
		case IP_ADDRESS:
			pc = ExtendedPatternParser.converters.get(String.valueOf(IP_ADDRESS));
			currentLiteral.setLength(0);
			addConverter(pc);
			break;
		default:
			super.finalizeConverter(formatChar);
		}
	}

	/**
	 * host name
	 */
	private static class HostPatternConverter extends PatternConverter {
		private String hostname = "";

		HostPatternConverter() {
			super();
			try {
				hostname = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				LogLog.warn(e.getMessage());
			}
		}

		public String convert(final LoggingEvent event) {
			return hostname;
		}
	}

	/**
	 * usually formatted as pid@host
	 */
	private static class VMNamePatternConverter extends PatternConverter {
		private String process = "";

		VMNamePatternConverter() {
			super();
			process = ManagementFactory.getRuntimeMXBean().getName();
		}

		public String convert(final LoggingEvent event) {
			return process;
		}
	}

	/**
	 * ip address
	 */
	private static class IPAddressPatternConverter extends PatternConverter {
		private String ipaddress = "";

		IPAddressPatternConverter() {
			super();
			try {
				ipaddress = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				LogLog.warn(e.getMessage());
			}
		}

		public String convert(final LoggingEvent event) {
			return ipaddress;
		}
	}
}
