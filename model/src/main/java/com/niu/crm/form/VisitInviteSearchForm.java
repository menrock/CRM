package com.niu.crm.form;

import java.util.Date;

public class VisitInviteSearchForm {
	
	private Long companyId;
	private String cstmName;
	private String gwName;
	private String mobile;
	private Date appointmentFrom;
	private Date appointmentTo;
	private Date visitFrom;
	private Date visitTo;
	private String aclClause;
	

	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public String getCstmName() {
		return cstmName;
	}
	public void setCstmName(String cstmName) {
		this.cstmName = cstmName;
	}

	public String getGwName() {
		return gwName;
	}
	public void setGwName(String gwName) {
		this.gwName = gwName;
	}
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Date getAppointmentFrom() {
		return appointmentFrom;
	}
	public void setAppointmentFrom(Date appointmentFrom) {
		this.appointmentFrom = appointmentFrom;
	}
	
	public Date getAppointmentTo() {
		return appointmentTo;
	}
	public void setAppointmentTo(Date appointmentTo) {
		this.appointmentTo = appointmentTo;
	}
	
	public Date getVisitFrom() {
		return visitFrom;
	}
	public void setVisitFrom(Date visitFrom) {
		this.visitFrom = visitFrom;
	}
	
	public Date getVisitTo() {
		return visitTo;
	}
	public void setVisitTo(Date visitTo) {
		this.visitTo = visitTo;
	}
	
	public String getAclClause() {
		return aclClause;
	}
	public void setAclClause(String aclClause) {
		this.aclClause = aclClause;
	}
}
