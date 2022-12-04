package com.niu.crm.model;


public class StuLevel extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1672942007727663635L;
	
	private Long stuId;
	private Long levelId;
	private Long creatorId;
	
	
	public Long getStuId() {
		return stuId;
	}
	public void setStuId(Long stuId) {
		this.stuId = stuId;
	}
	public Long getLevelId() {
		return levelId;
	}
	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	
}
