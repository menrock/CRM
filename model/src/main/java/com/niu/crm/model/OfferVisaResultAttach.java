package com.niu.crm.model;

public class OfferVisaResultAttach extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3493033821201525724L;
	
	private Long conId;
	private Boolean offer;  //offer or visa
	private Boolean refuse; //是否拒绝
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
	
	public Boolean getOffer() {
		return offer;
	}
	public void setOffer(Boolean offer) {
		this.offer = offer;
	}
	
	public Boolean getRefuse() {
		return refuse;
	}
	public void setRefuse(Boolean refuse) {
		this.refuse = refuse;
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
