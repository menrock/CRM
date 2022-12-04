package com.niu.crm.service;


import java.util.List;

import com.niu.crm.form.UserFuncSearchForm;
import com.niu.crm.model.User;
import com.niu.crm.model.UserFunc;
import com.niu.crm.vo.UserFuncVO;

public interface UserFuncService {
	
	void setUserFunc(User user, Long userId,  List<UserFunc> lsUserFunc);
	
	int add(UserFunc userFunc);
    
    void delete(Long userId, Long id);
    
    UserFuncVO loadByCode(Long userId, String funcCode);

    List<UserFuncVO> queryUserFunc(UserFuncSearchForm form);
    
    boolean hasFunc(Long userId, String funcCode);

	void initNames(UserFuncVO uf);
	
    List<UserFuncVO> loadAll(Long userId);
    
    List<User> listUsers(String funcCode, Long companyId);
    
    boolean allowAccess(User user,UserFuncVO uf, Long companyId, Long unitId);
}
