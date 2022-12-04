package com.niu.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public abstract class BaseService {
	@Value("${SYS_USER_ID}")
	private Long SYS_USER_ID;
	
	@Value("${profiles.active}")
	private String PROFILES_ACTIVE;
		
	public Logger getLogger() {
		return LoggerFactory.getLogger(this.getClass());
	}
	
	public Long getSysUserId(){
		return this.SYS_USER_ID;
	}
	
	public String getProfiles(){
		return PROFILES_ACTIVE;
	}
	
	public boolean isDevProfile(){
		if(PROFILES_ACTIVE !=null && PROFILES_ACTIVE.equalsIgnoreCase("dev"))
			return true;
		else
			return false;
	}

	/**
	 * 是否是可以在测试环境发微信，短信， 邮件的用户
	 * @param userId
	 * @return
	 */
	public boolean isCanTestUser(long userId){
		if(userId ==1 || userId == 50 || userId ==56)
			return true;
		else
			return false;
	}
}
