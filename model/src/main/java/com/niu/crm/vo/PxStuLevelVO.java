package com.niu.crm.vo;

import com.niu.crm.model.StuLevel;


public class PxStuLevelVO extends StuLevel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -441327854416613498L;
	
	private String levelName;
	private String creatorName;
	
	
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	
	
}
