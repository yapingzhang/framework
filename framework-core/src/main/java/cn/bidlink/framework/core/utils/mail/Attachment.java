package cn.bidlink.framework.core.utils.mail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @description:附件
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2007-10-30 上午10:45:04
 */
public class Attachment {
	private Map<String, byte[]> mapAttachMent = null;

	public Attachment() {
		mapAttachMent = new HashMap<String, byte[]>();
	}

	public Map<String, byte[]> getMapAttachMent() {
		return mapAttachMent;
	}

	public void setMapAttachMent(Map<String, byte[]> mapAttachMent) {
		this.mapAttachMent = mapAttachMent;
	}

	public void addAttachment(File file) throws IOException {
		mapAttachMent.put(file.getName(), FileUtils.readFileToByteArray(file));
	}

	public void addAttachment(String fileName, byte[] bufFile)
			throws IOException {
		mapAttachMent.put(fileName, bufFile);
	}
}
