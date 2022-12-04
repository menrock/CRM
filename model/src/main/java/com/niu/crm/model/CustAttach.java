package com.niu.crm.model;

import com.niu.crm.model.type.CustAttachType;

public class CustAttach extends BaseModel {
	private static final long serialVersionUID = -7016425126097249724L;
	
	private Long cstmId;
	private Long conId;
	private CustAttachType attachType;
	private String attachName;
	private String attachDesc;
	private String fileExt;
	private String ossKey;
	private Long creatorId;
	

	public Long getCstmId() {
		return cstmId;
	}
	public void setCstmId(Long cstmId) {
		this.cstmId = cstmId;
	}
	
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	
	public String getAttachTypeName() {
		if(attachType == null)
			return null;
		else
			return attachType.getName();
	}
	public CustAttachType getAttachType() {
		return attachType;
	}
	public void setAttachType(CustAttachType attachType) {
		this.attachType = attachType;
	}
	
	public String getAttachName() {
		return attachName;
	}
	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}
	
	public String getAttachDesc() {
		return attachDesc;
	}
	public void setAttachDesc(String attachDesc) {
		this.attachDesc = attachDesc;
	}
	
	public String getFileExt() {
		return fileExt;
	}
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	
	public String getOssKey() {
		return ossKey;
	}
	public void setOssKey(String ossKey) {
		this.ossKey = ossKey;
	}
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
}
