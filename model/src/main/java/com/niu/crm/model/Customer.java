package com.niu.crm.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer extends BaseModel{
	private static final long serialVersionUID = 4131473937718464827L;
	
	private String name;
	private String gender;  //性别
	private String mobile;  //手机
	private String phone;  //备用电话
	private String email;  //email
	private String wechat;  //微信
	private Boolean lxCust;  //留学客户
	private Boolean pxCust;  //培训客户
	private Boolean cpCust;  //产品客户
	private String QQ;  //QQ
	private String idCertType;  //证件类型
	private String idCertNo;    //证件号
	private String marriage;
	private Long creatorId;
	private Date updatedAt;
	private Boolean archive;
	
	private List<CustPhone> custPhones;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	} 
	public void setGender(String gender) {
		if(gender ==null)
			this.gender = gender;
		else
			this.gender = gender.toUpperCase();		
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public List<CustPhone> getCustPhones() {
		return custPhones;
	}
	public void setCustPhones(List<CustPhone> custPhones) {
		this.custPhones = custPhones;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getIdCertType() {
		return idCertType;
	}
	public void setIdCertType(String idCertType) {
		this.idCertType = idCertType;
	}
	public String getIdCertNo() {
		return idCertNo;
	}
	public void setIdCertNo(String idCertNo) {
		this.idCertNo = idCertNo;
	}
	
	public String getMarriage(){
		return this.marriage;
	}
	
	public void setMarriage(String marriage){
		this.marriage =marriage;
	}

	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Boolean getArchive() {
		return archive;
	}
	public void setArchive(Boolean archive) {
		this.archive = archive;
	}
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	public String getQQ() {
		return QQ;
	}
	public void setQQ(String QQ) {
		this.QQ = QQ;
	}

	public Boolean getLxCust() {
		return lxCust;
	}
	public void setLxCust(Boolean lxCust) {
		this.lxCust = lxCust;
	}
	
	public Boolean getPxCust() {
		return pxCust;
	}
	public void setPxCust(Boolean pxCust) {
		this.pxCust = pxCust;
	}
	
	public Boolean getCpCust() {
		return cpCust;
	}
	public void setCpCust(Boolean cpCust) {
		this.cpCust = cpCust;
	}
}