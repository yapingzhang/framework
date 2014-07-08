package cn.bidlink.fileserver.bean;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Accessory {

	private String accessoryId;

	private String filedName;

	/**
	 * 文件原名称
	 */
	private String fileName;
	/**
	 * 文件新名称
	 */
	private String fileNewName;
	/**
	 * 上传文件名
	 */
	private String accessoryName;
	/**
	 * 远端上传PC IPIP
	 */
	private String remoteIP;

	/**
	 * 原来绝对路径
	 */
	private String oldFullPath;
	private String accessoryURL;
	/**
	 * 文件大小byte
	 */
	private long fileSize;

	public String getAccessoryId() {
		return accessoryId;
	}

	public void setAccessoryId(String accessoryId) {
		this.accessoryId = accessoryId;
	}

	public String getAccessoryName() {
		return accessoryName;
	}

	public void setAccessoryName(String accessoryName) {
		this.accessoryName = accessoryName;
	}

	public String getAccessoryURL() {
		return accessoryURL;
	}

	public void setAccessoryURL(String accessoryURL) {
		this.accessoryURL = accessoryURL;
	}

	public String getFiledName() {
		return filedName;
	}

	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}

	public String getOldFullPath() {
		return oldFullPath;
	}

	public void setOldFullPath(String oldFullPath) {
		this.oldFullPath = oldFullPath;
	}

	public String getRemoteIP() {
		return remoteIP;
	}

	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getFileName() {
		if (StringUtils.isNotEmpty(oldFullPath)
				&& this.oldFullPath.indexOf("/") != -1) {
			fileName = StringUtils.substringAfterLast(oldFullPath, "/");
		} else {
			fileName = this.oldFullPath;
		}
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileNewName() {

		return fileNewName;
	}

	public void setFileNewName(String fileNewName) {
		this.fileNewName = fileNewName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

}
