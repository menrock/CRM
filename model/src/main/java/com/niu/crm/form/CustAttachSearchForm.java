package com.niu.crm.form;

import java.util.Date;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.niu.crm.util.DateUtil;

public class CustAttachSearchForm {
	private Long cstmId;
	private Long conId;
	
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
	
	
}
