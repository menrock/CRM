package com.niu.crm.model;

public class StuPlanCountry extends BaseModel{
	private static final long serialVersionUID = 5197247787877565585L;
	
	private Long stuId;
	private String countryCode;
	private String countryName;
	
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
}
