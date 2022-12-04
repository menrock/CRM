package com.niu.crm.model;

import java.util.Date;

public abstract class BaseModel implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6837166796762692803L;
	

	private Long id;
	private Date createdAt;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
