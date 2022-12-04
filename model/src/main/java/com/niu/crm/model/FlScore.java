package com.niu.crm.model;

public class FlScore extends BaseModel {
	private static final long serialVersionUID = -22627369410796757L;
	
	private Integer showIndex;
	private Long stuId;
	private Long langId;
	private String langName;
	private String langScore;
	
	
	public Integer getShowIndex() {
		return showIndex;
	}
	public void setShowIndex(Integer showIndex) {
		this.showIndex = showIndex;
	}
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	public Long getLangId() {
		return langId;
	}
	public void setLangId(Long langId) {
		this.langId = langId;
	}
	public String getLangName() {
		return langName;
	}
	public void setLangName(String langName) {
		this.langName = langName;
	}
	public String getLangScore() {
		return langScore;
	}
	public void setLangScore(String langScore) {
		this.langScore = langScore;
	}
}
