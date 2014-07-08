package cn.bidlink.fileserver.bean;

import java.io.InputStream;

public class FileBean {

	private String fileName;
	private String fileError;
	/**
	 * 加密字符串
	 */
	private String uuid;
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getFileError() {
		return fileError;
	}
	public void setFileError(String fileError) {
		this.fileError = fileError;
	}
	private InputStream inputStream;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}
