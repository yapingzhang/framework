/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>RedisSerializer.java</p>
 *   
 */
package cn.bidlink.framework.redis.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

import cn.bidlink.framework.redis.exception.RedisException;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
class RedisSerializer {
	protected transient Logger logger = Logger.getLogger(getClass());
	protected static final int COMPRESSION_THRESHOLD = 16384;
	private final Charset charset;
	private final boolean packZeros;

	public RedisSerializer() {
		this(Charset.forName("UTF-8"), true);
	}

	public RedisSerializer(Charset charset, boolean packZeros) {
		this.charset = charset;
		this.packZeros = packZeros;
	}

	/**
	 * 
	 * @description: TODO add description
	 * @param in
	 * @return
	 */
	protected byte[] compress(byte[] in) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPOutputStream gz = null;
		try {
			gz = new GZIPOutputStream(bos);
			gz.write(in);
		} catch (IOException e) {
		} finally {
			RedisCloseable.close(gz);
			RedisCloseable.close(bos);
		}
		byte[] rv = bos.toByteArray();
		return rv;
	}

	/**
	 * 
	 * @description: TODO add description
	 * @param in
	 * @return
	 */
	protected byte[] decompress(byte[] in) {
		ByteArrayOutputStream bos = null;
		if (in != null) {
			ByteArrayInputStream bis = new ByteArrayInputStream(in);
			bos = new ByteArrayOutputStream();
			GZIPInputStream gis = null;
			try {
				gis = new GZIPInputStream(bis);

				byte[] buf = new byte[8192];
				int r = -1;
				while ((r = gis.read(buf)) > 0) {
					bos.write(buf, 0, r);
				}
			} catch (IOException e) {
				logger.warn("Failed to decompress data", e);
				bos = null;
			} finally {
				RedisCloseable.close(gis);
				RedisCloseable.close(bis);
				RedisCloseable.close(bos);
			}
		}
		return bos == null ? null : bos.toByteArray();
	}

	/**
	 * 
	 * @description: TODO add description
	 * @param string
	 * @return
	 * @throws RedisException
	 */
	public byte[] serializeString(String string) throws RedisException {
		return (string == null ? null : string.getBytes(charset));
	}

	/**
	 * 
	 * @description: TODO add description
	 * @param bytes
	 * @return
	 * @throws RedisException
	 */
	public String deserializeString(byte[] bytes) throws RedisException {
		return (bytes == null ? null : new String(bytes, charset));
	}

	/**
	 * 
	 * @description: TODO add description
	 * @param l
	 * @param maxBytes
	 * @return
	 */
	protected byte[] serializeNum(long l, int maxBytes) {
		byte[] rv = new byte[maxBytes];
		for (int i = 0; i < rv.length; i++) {
			int pos = rv.length - i - 1;
			rv[pos] = (byte) ((l >> (8 * i)) & 0xff);
		}
		if (packZeros) {
			int firstNon0 = 0;
			while (firstNon0 < rv.length && rv[firstNon0] == 0) {
				firstNon0++;
			}
			if (firstNon0 > 0) {
				byte[] tmp = new byte[rv.length - firstNon0];
				System.arraycopy(rv, firstNon0, tmp, 0, rv.length - firstNon0);
				rv = tmp;
			}
		}
		return rv;
	}

	public byte[] serializeLong(long l) {
		return serializeNum(l, 8);
	}

	public long deserializeLong(byte[] bytes) throws RedisException {
		long rv = 0;
		for (byte i : bytes) {
			rv = (rv << 8) | (i < 0 ? 256 + i : i);
		}
		return rv;
	}

	public byte[] serializeInt(int in) {
		return serializeNum(in, 4);
	}

	public int deserializeInt(byte[] bytes) {
		return (int) deserializeLong(bytes);
	}

	public byte[] serializeShort(short in) {
		return serializeNum(in, 2);
	}

	public short deserializeShort(byte[] bytes) {
		return (short) deserializeLong(bytes);
	}

	public byte[] serializeCharacter(char in) {
		return serializeNum(in, 2);
	}

	public char deserializeCharacter(byte[] bytes) {
		return (char) deserializeLong(bytes);
	}

	public byte[] serializeBoolean(boolean b) throws RedisException {
		byte[] rv = new byte[1];
		rv[0] = (byte) (b ? '1' : '0');
		return rv;
	}

	public boolean deserializeBoolean(byte[] bytes) throws RedisException {
		return bytes[0] == '1';
	}

	public byte[] serializeByte(byte in) throws RedisException {
		return new byte[] { in };
	}

	public byte deserializeByte(byte[] bytes) throws RedisException {
		byte rv = 0;
		if (bytes.length == 1) {
			rv = bytes[0];
		}
		return rv;
	}

	public byte[] serializeObject(Object obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Hessian2Output out = new Hessian2Output(bos);
		try {
			out.writeObject(obj);
			out.flush();
			byte[] bytes = bos.toByteArray();
			return bytes;
		} catch (Exception e) {
			throw new RedisException(e);
		} finally {
			RedisCloseable.close(bos);
		}
	}

	public Object deserializeObject(byte[] bytes) {
		ByteArrayInputStream bis = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			Hessian2Input in = new Hessian2Input(bis);
			Object o = (Object) in.readObject(Object.class);
			return o;
		} catch (Exception e) {
			throw new RedisException(e);
		} finally {
			RedisCloseable.close(bis);
		}
	}
}
