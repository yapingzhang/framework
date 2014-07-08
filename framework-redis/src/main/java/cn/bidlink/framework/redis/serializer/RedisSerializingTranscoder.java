/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>RedisSerializingTranscoder.java</p>
 *   
 */
package cn.bidlink.framework.redis.serializer;

import java.util.Date;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
public class RedisSerializingTranscoder extends RedisSerializer {
	static final int COMPRESSED = (1 << 1);
	static final int SPECIAL_STRING = (2 << 2);
	static final int SPECIAL_STRINGBUFFER = (3 << 2);
	static final int SPECIAL_STRINGBUILDER = (4 << 2);
	static final int SPECIAL_LONG = (5 << 2);
	static final int SPECIAL_INT = (6 << 2);
	static final int SPECIAL_SHORT = (7 << 2);
	static final int SPECIAL_BOOLEAN = (8 << 2);
	static final int SPECIAL_DATE = (9 << 2);
	static final int SPECIAL_BYTE = (10 << 2);
	static final int SPECIAL_FLOAT = (11 << 2);
	static final int SPECIAL_DOUBLE = (12 << 2);
	static final int SPECIAL_BYTEARRAY = (13 << 2);
	static final int SPECIAL_CHARACTER = (14 << 2);
	static final int SERIALI_OBJECT = (15 << 2);

	public RedisObject serialize(Object obj) {
		byte[] b = null;
		int flags = 0;
		if (obj instanceof String) {
			b = serializeString(String.valueOf(obj));
			flags |= SPECIAL_STRING;
			if (StringUtils.isJsonObject((String) obj)) {
				return new RedisObject(flags, b);
			}
		} else if (obj instanceof StringBuffer) {
			b = serializeString(String.valueOf(obj));
			flags |= SPECIAL_STRINGBUFFER;
		} else if (obj instanceof StringBuilder) {
			b = serializeString(String.valueOf(obj));
			flags |= SPECIAL_STRINGBUILDER;
		} else if (obj instanceof Long) {
			b = serializeLong((Long) obj);
			flags |= SPECIAL_LONG;
		} else if (obj instanceof Integer) {
			b = serializeInt((Integer) obj);
			flags |= SPECIAL_INT;
		} else if (obj instanceof Short) {
			b = serializeShort((Short) obj);
			flags |= SPECIAL_SHORT;
		} else if (obj instanceof Boolean) {
			b = serializeBoolean((Boolean) obj);
			flags |= SPECIAL_BOOLEAN;
		} else if (obj instanceof Date) {
			b = serializeLong(((Date) obj).getTime());
			flags |= SPECIAL_DATE;
		} else if (obj instanceof Byte) {
			b = serializeByte((Byte) obj);
			flags |= SPECIAL_BYTE;
		} else if (obj instanceof Float) {
			b = serializeInt(Float.floatToRawIntBits((Float) obj));
			flags |= SPECIAL_FLOAT;
		} else if (obj instanceof Double) {
			b = serializeLong(Double.doubleToRawLongBits((Double) obj));
			flags |= SPECIAL_DOUBLE;
		} else if (obj instanceof byte[]) {
			b = (byte[]) obj;
			flags |= SPECIAL_BYTEARRAY;
		} else if (obj instanceof Character) {
			b = serializeCharacter((Character) obj);
			flags |= SPECIAL_CHARACTER;
		} else {
			b = serializeObject(obj);
			flags |= SERIALI_OBJECT;
		}
		if (b.length > COMPRESSION_THRESHOLD) {
			byte[] compressed = compress(b);
			if (compressed.length < b.length) {
				b = compressed;
				flags |= COMPRESSED;
			} else {
			}
		}
		return new RedisObject(flags, b);
	}

	public Object deserialize(RedisObject obj) {
		byte[] bytes = obj.getData();
		Object rv = null;
		if ((obj.getFlags() & COMPRESSED) != 0) {
			bytes = decompress(obj.getData());
		}

		int flags = obj.getFlags() & ~COMPRESSED;
		switch (flags) {
		case SPECIAL_STRING:
			rv = deserializeString(bytes);
			break;
		case SPECIAL_STRINGBUFFER:
			rv = new StringBuffer(deserializeString(bytes));
			break;
		case SPECIAL_STRINGBUILDER:
			rv = new StringBuilder(deserializeString(bytes));
			break;
		case SPECIAL_LONG:
			rv = deserializeLong(bytes);
			break;
		case SPECIAL_INT:
			rv = deserializeInt(bytes);
			break;
		case SPECIAL_SHORT:
			rv = Short.valueOf((short) deserializeInt(bytes));
			break;
		case SPECIAL_BOOLEAN:
			rv = deserializeBoolean(bytes);
			break;
		case SPECIAL_DATE:
			rv = new Date(deserializeLong(bytes));
			break;
		case SPECIAL_BYTE:
			rv = deserializeByte(bytes);
			break;
		case SPECIAL_FLOAT:
			rv = Float.intBitsToFloat(deserializeInt(bytes));
			break;
		case SPECIAL_DOUBLE:
			rv = Double.longBitsToDouble(deserializeLong(bytes));
			break;
		case SPECIAL_BYTEARRAY:
			rv = bytes;
			break;
		case SPECIAL_CHARACTER:
			rv = deserializeCharacter(bytes);
			break;
		case SERIALI_OBJECT:
			rv = deserializeObject(bytes);
			break;
		default:
			logger.warn("Undeserialize with flags [" + flags + "]");
		}
		return rv;
	}
}
