package com.niu.crm.form;

import java.util.Date;


public class AlarmSearchForm {
	private Long stuId;
	private Boolean enabled;
	private Date alarmTimeFrom;
	private Date alarmTimeTo;
	private Long creatorId;

	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public Date getAlarmTimeFrom() {
		return alarmTimeFrom;
	}
	public void setAlarmTimeFrom(Date alarmTimeFrom) {
		this.alarmTimeFrom = alarmTimeFrom;
	}
	
	public Date getAlarmTimeTo() {
		return alarmTimeTo;
	}
	public void setAlarmTimeTo(Date alarmTimeTo) {
		this.alarmTimeTo = alarmTimeTo;
	}
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
}
