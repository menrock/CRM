package com.niu.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;

public interface WechatService {
	
	JSONObject listDepartment(Long unitId, Long wxUnitId);
	
	JSONObject listUser(Long unitId, Boolean fetchChild, Integer status);
	
	boolean createUnit(Unit unit);
	void deleteUnit(Unit unit);
	void updateUnit(Unit unit);
	

	JSONObject getUser(Long userId) throws Exception ;
	
	/**
	 * 同步用户信息到 微信
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	JSONObject createUser(Long userId) throws Exception ;
	
	JSONObject deleteUser(Long userId) throws Exception ;
	JSONObject updateUser(User user) throws Exception ;
	
	JSONObject sendTxtMessage(String msg, Long[] toUnitId, Long[] toUserId) throws Exception;
}
