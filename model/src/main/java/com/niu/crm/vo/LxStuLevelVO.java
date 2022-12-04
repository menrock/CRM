package com.niu.crm.vo;

import com.niu.crm.model.LxStuLevel;


public class LxStuLevelVO extends LxStuLevel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1682682626596956757L;
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
