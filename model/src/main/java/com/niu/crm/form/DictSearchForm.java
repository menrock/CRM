package com.niu.crm.form;

import java.util.Date;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.niu.crm.util.DateUtil;

public class DictSearchForm {
	private Long parentId;
	private String keywords;
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		if(StringUtils.isEmpty(keywords) )
			this.keywords = null;
		else
			this.keywords = keywords.trim();
	}
}
