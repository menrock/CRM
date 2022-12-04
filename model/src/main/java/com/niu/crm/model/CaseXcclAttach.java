package com.niu.crm.model;

import com.niu.crm.model.type.XcclAttachType;

public class CaseXcclAttach extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6406268592461329488L;
	
	private Long conId;
	private XcclAttachType xcclType;
	private String attachName;
	private String attachDesc;
	private String fileExt;
	private String ossKey;
	private Long creatorId;
	
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	
	public String getXcclTypeName() {
		if(xcclType == null)
			return null;
		else
			return xcclType.getName();
	}
	public XcclAttachType getXcclType() {
		return xcclType;
	}
	public void setXcclType(XcclAttachType xcclType) {
		this.xcclType = xcclType;
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
