package cn.bidlink.fileserver.bean;


/**
 * 通过反射保存上传文件的公用信息类
 *
 */
public abstract class FormBaseBean {
	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	/**
	 * 文件存放路径（文件存放的文件夹路径 ）,通过前台传入
	 */
	private String savePath;

	/**
	 * 存放上传的文件公用信息
	 */
	private String applicationId;
	private String currentDate;
	private String basePath;
	private String Filename;
	private String extend;// 扩展字段，用户扩展信息
	private String prasethMethod;
	private String fileFoldPath;
	private String code;
	private String companyId;
	private String fileSize;


	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * 上传错误提示信息;
	 */
	private String errorMsg;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}



	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}



	public String getFilename() {
		return Filename;
	}

	public void setFilename(String filename) {
		Filename = filename;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public String getPrasethMethod() {
		return prasethMethod;
	}

	public void setPrasethMethod(String prasethMethod) {
		this.prasethMethod = prasethMethod;
	}

	public String getFileFoldPath() {
		return fileFoldPath;
	}

	public void setFileFoldPath(String fileFoldPath) {
		this.fileFoldPath = fileFoldPath;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
