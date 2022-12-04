package com.niu.crm.model;


import org.apache.commons.lang3.StringUtils;

public class Area extends BaseModel{
	private static final long serialVersionUID = 8254338342058057593L;
	
	private Integer showIndex;
	private String name;
	private String abbr;
	private String zipCode;
	private Long parentId;
	private String zoneTag;
	private Long creatorId;
	

	public Integer getShowIndex() {
		return showIndex;
	}
	public void setShowIndex(Integer showIndex) {
		this.showIndex = showIndex;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAbbr() {
		if(StringUtils.isEmpty(abbr))
			return name;
		else
			return abbr;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public String getZoneTag() {
		return zoneTag;
	}
	public void zoneTag(String zoneTag) {
		this.zoneTag = zoneTag;
	}

	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
}