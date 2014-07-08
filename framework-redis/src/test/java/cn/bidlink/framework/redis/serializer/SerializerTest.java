/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>SerializerTest.java</p>
 *   
 */
package cn.bidlink.framework.redis.serializer;

import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import cn.bidlink.framework.redis.pojo.AuthUser;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
public class SerializerTest {
	Logger logger = Logger.getLogger(this.getClass());
	private AuthUser authUser;
	byte[] object;

	@Before
	public void loadContext() {
		authUser = new AuthUser(1245678L);
	}

	@Test
	public void testString() throws Exception {
		object = SerializerFactory.serialize("abcdeft");
		logger.info((String) SerializerFactory.deserialize(object));
	}

	@Test
	public void testObject() throws Exception {
		object = SerializerFactory.serialize(new AuthUser(1245678L));
		logger.info(((AuthUser) SerializerFactory.deserialize(object)).getAddress());
	}

	@Test
	public void testCharacter() throws Exception {
		object = SerializerFactory.serialize('a');
		logger.info(String.valueOf(SerializerFactory.deserialize(object)));
	}

	@Test
	public void testCcompare() {
		logger.info("boolean:" + SerializerFactory.serialize(true).length);
		logger.info("byte:" + SerializerFactory.serialize((byte) 123).length);
		logger.info("short:" + SerializerFactory.serialize((short) 1234).length);
		logger.info("char:" + SerializerFactory.serialize('a').length);
		logger.info("int(123):" + SerializerFactory.serialize(123).length);
		logger.info("int(123456789):" + SerializerFactory.serialize(123456789).length);
		logger.info("long(123):" + SerializerFactory.serialize(123L).length);
		logger.info("long(1234567890):" + SerializerFactory.serialize(1234567890L).length);
		logger.info("float:" + SerializerFactory.serialize(123.0f).length);
		logger.info("doublue:" + SerializerFactory.serialize(new Double(123.000)).length);
		logger.info("string.length=6:" + SerializerFactory.serialize("abcdef").length);
		logger.info("date:" + SerializerFactory.serialize(new Date()).length);
		logger.info("object:" + SerializerFactory.serialize(authUser).length);
	}
}
