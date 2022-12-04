package com.niu.crm.model;

import java.util.Date;

import com.niu.crm.model.type.AlarmRepeatType;
import com.niu.crm.model.type.MessageType;

public class Alarm extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4514699190523865877L;
	
	private Long stuId;
	private String content;
	private String url;
	private Date alarmDate;  //提醒日期
	private java.sql.Time alarmTime;  //提醒时间 null 表示立即提醒
	
	private Integer alarmWay;
	private String alarmWayNames;
	private AlarmRepeatType repeatType;
	private String repeatDays;
	private String alarmUserIds;
	private String alarmUnitIds;
	private Date alarmedTime;  //最近已提醒时间
	private Boolean enabled;   //发送之后 如无需再发送 系统自动置为false(对于立即发送的)
	private Long creatorId;
	private Date updatedAt;
	
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Date getAlarmDate() {
		return alarmDate;
	}
	public void setAlarmDate(Date alarmDate) {
		this.alarmDate = alarmDate;
	}
	
	public java.sql.Time getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(java.sql.Time alarmTime) {
		this.alarmTime = alarmTime;
	}
	
	public Integer getAlarmWay() {
		return alarmWay;
	}
	public void setAlarmWay(Integer alarmWay) {
		this.alarmWay = alarmWay;
	}

	public String getAlarmWayNames() {
		return alarmWayNames;
	}

	public AlarmRepeatType getRepeatType() {
		return repeatType;
	}
	public void setRepeatType(AlarmRepeatType repeatType) {
		this.repeatType = repeatType;
	}
	
	public String getRepeatDays() {
		return repeatDays;
	}
	public void setRepeatDays(String repeatDays) {
		this.repeatDays = repeatDays;
	}
	
	public String getAlarmUserIds() {
		return alarmUserIds;
	}
	public void setAlarmUserIds(String alarmUserIds) {
		this.alarmUserIds = alarmUserIds;
	}
	
	public String getAlarmUnitIds() {
		return alarmUnitIds;
	}
	public void setAlarmUnitIds(String alarmUnitIds) {
		this.alarmUnitIds = alarmUnitIds;
	}
	
	public Date getAlarmedTime() {
		return alarmedTime;
	}
	public void setAlarmedTime(Date alarmedTime) {
		this.alarmedTime = alarmedTime;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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
	
	public void translateAlarmWay(){
		this.alarmWayNames = null;
		if(alarmWay != null){
			int val = alarmWay&MessageType.SYS;
			if( val >0){
				if( alarmWayNames==null)
					alarmWayNames = "系统消息";
				else
					alarmWayNames += "/系统消息";
			}
			val = alarmWay&MessageType.SMS;
			if( val >0){
				if( alarmWayNames==null)
					alarmWayNames = "短信";
				else
					alarmWayNames += "/短信";
			}
			val = alarmWay&MessageType.WX;
			if( val >0){
				if( alarmWayNames==null)
					alarmWayNames = "微信";
				else
					alarmWayNames += "/微信";
			}
		}
	}
}
