package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.UserSearchForm;
import com.niu.crm.model.User;

public interface UserMapper {
	
	int insert(User user);
	
	int update(User user);
	
	int updateOnline(@Param("id") Long userId, @Param("online") Boolean onlineFlag);
	
	int changePwd(@Param("id") Long userId, @Param("password") String password);
	
	int delete(User user);
	
	User selectById(Long id);

	User selectByAccount(String account);

	List<User> selectByName(String username);

    int countRegistered(String account);
	
	int queryCount(@Param("params") UserSearchForm form);
	
	List<User> query(@Param("params") UserSearchForm form, @Param("pager")Pageable pageable);

	int addLoginLog(@Param("userId") Long userId, @Param("userName") String userName, @Param("fromIP") String fromIP);

}
