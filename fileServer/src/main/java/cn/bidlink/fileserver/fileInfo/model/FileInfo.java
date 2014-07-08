/*
 * TregFile.java 2012-09-03
 * Copyright  © 2001-2012 必联网
 * 京ICP备09004729号京公网安备110108008196号
 */
package cn.bidlink.fileserver.fileInfo.model;

public class FileInfo {

	/**
	 * 描述:id 字段: ID int(10)
	 */
	private java.lang.Integer id;

	/**
	 * 描述:fileName 字段: FILE_NAME varchar(256)
	 */
	private java.lang.String fileName;

	/**
	 * 描述:applicationId 字段: APPLICATION_ID varchar(64)
	 */
	private java.lang.String applicationId;

	/**
	 * 描述:code 字段: CODE varchar(64)
	 */
	private java.lang.String code;

	/**
	 * 描述:uploadDate 字段: UPLOAD_DATE varchar(256)
	 */
	private java.lang.String uploadDate;
	
	
	/**
	 * 加密字符串
	 */
	private String uuid ;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public FileInfo() {
	}

	public FileInfo(java.lang.Integer id) {
		this.id = id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setFileName(java.lang.String fileName) {
		this.fileName = fileName;
	}

	public java.lang.String getFileName() {
		return this.fileName;
	}

	public void setApplicationId(java.lang.String applicationId) {
		this.applicationId = applicationId;
	}

	public java.lang.String getApplicationId() {
		return this.applicationId;
	}

	public void setCode(java.lang.String code) {
		this.code = code;
	}

	public java.lang.String getCode() {
		return this.code;
	}

	public void setUploadDate(java.lang.String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public java.lang.String getUploadDate() {
		return this.uploadDate;
	}

}
