/**
 * Copyright (c) 2001-2010 Bidlink(Beijing) E-Biz Tech Co.,Ltd.
 * All rights reserved.
 * 必联（北京）电子商务科技有限公司 版权所有
 *
 * <p>AuthUser.java</p>
 *   
 */
package cn.bidlink.framework.redis.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @description: TODO add description
 * @version Ver 1.0
 * @author <a href="mailto:wangtao@ebnew.com">wangtao</a>
 * @Date 2012-9-12 下午04:48:09
 */
public class AuthUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userId;

	private Long spaceId;

	private String spaceName;

	private String contact;

	private String idCardNo;

	private String email;

	private String mobile;

	private String comPlace;

	private String enterpriseName;

	private String wwwStation;

	private String bossName;

	private String regNumber;

	private String dept;

	private String position;

	private String idCardUrl;

	private String positionUrl;

	private Date createDate;

	private Date updateDate;

	private Long authStatus;

	private String licenseUrl;

	private String enterpriseUrl;

	private String spacePurpose;

	private Long authType;

	private String remark;

	private String isEdit;

	private String address;

	public AuthUser(Long userId) {
		this.setUserId(userId);
		this.setAddress("大手大脚的方式是大幅度减少了抗击非典上解决阿萨德警方解救 ");
		this.setAuthStatus((long) 1);
		this.setAuthType(1L);
		this.setBossName("abcftyyyx");
		this.setComPlace("fdsafds");
		this.setContact("点发卡机反抗拉萨计算机硕士劫匪徕卡soi鄂武roeuewruoiwquro");
		this.setCreateDate(new Date());
		this.setDept("!@#$%^&*()-+`~[]{}");
		this.setEmail("fdsfds@llfds.fd");
		this.setEnterpriseName("发生的范德萨");
		this.setEnterpriseUrl("http://www.baidu.com");
		this.setIdCardNo("3");
		this.setIdCardUrl("http://www.baidu.com");
		this.setIsEdit("XZ");
		this.setWwwStation("中国");
		this.setRemark("k了范德萨就撒手的风景阿娇发酵法解放军撒房间看电视拉进风口撒娇发放独家萨克减肥打算");
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getComPlace() {
		return comPlace;
	}

	public void setComPlace(String comPlace) {
		this.comPlace = comPlace;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getWwwStation() {
		return wwwStation;
	}

	public void setWwwStation(String wwwStation) {
		this.wwwStation = wwwStation;
	}

	public String getBossName() {
		return bossName;
	}

	public void setBossName(String bossName) {
		this.bossName = bossName;
	}

	public String getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getIdCardUrl() {
		return idCardUrl;
	}

	public void setIdCardUrl(String idCardUrl) {
		this.idCardUrl = idCardUrl;
	}

	public String getPositionUrl() {
		return positionUrl;
	}

	public void setPositionUrl(String positionUrl) {
		this.positionUrl = positionUrl;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(Long authStatus) {
		this.authStatus = authStatus;
	}

	public String getLicenseUrl() {
		return licenseUrl;
	}

	public void setLicenseUrl(String licenseUrl) {
		this.licenseUrl = licenseUrl;
	}

	public String getEnterpriseUrl() {
		return enterpriseUrl;
	}

	public void setEnterpriseUrl(String enterpriseUrl) {
		this.enterpriseUrl = enterpriseUrl;
	}

	public String getSpacePurpose() {
		return spacePurpose;
	}

	public void setSpacePurpose(String spacePurpose) {
		this.spacePurpose = spacePurpose;
	}

	public Long getAuthType() {
		return authType;
	}

	public void setAuthType(Long authType) {
		this.authType = authType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
