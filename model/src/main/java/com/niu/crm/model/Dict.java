package com.niu.crm.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Dict extends BaseModel{
	private static final long serialVersionUID = 8254338342058057593L;
	
	private Long showIndex;
	private String dictCode;
	private String dictName;
	private String dictDesc;
	private String keywords;
	private Long parentId;
	private Long creatorId;
	private Date updatedAt;

	public Long getShowIndex() {
		return showIndex;
	}
	public void setShowIndex(Long showIndex) {
		this.showIndex = showIndex;
	}
	
	public String getDictCode() {
		return dictCode;
	}
	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	
	public String getDictName() {
		return dictName;
	}
	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
	
	public String getDictDesc() {
		return dictDesc;
	}
	public void setDictDesc(String dictDesc) {
		this.dictDesc = dictDesc;
	}
	
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
	
	public int getLayer(){
		if(StringUtils.isEmpty(dictCode))
			return 0;
		
		int layer =1;
		String szCode = dictCode;
		while( szCode.indexOf(".") >0){
			int idx = szCode.indexOf(".");
			szCode = szCode.substring(idx +1);
			layer ++;
		}
		return layer;
		
	}
}