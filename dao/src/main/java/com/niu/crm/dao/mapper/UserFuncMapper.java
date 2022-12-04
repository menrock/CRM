package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.form.UserFuncSearchForm;
import com.niu.crm.model.UserFunc;
import com.niu.crm.vo.UserFuncVO;

public interface UserFuncMapper {
	
	int insert(UserFunc userFunc);
	
	int delete(@Param("userId")Long userId, @Param("id")Long id);
	
	int deleteByUserId(@Param("userId")Long userId);
	
	List<UserFuncVO> queryUserFunc(@Param("params")UserFuncSearchForm form);

	int deleteByFuncId(@Param("funcId")Long funcId);
	
	List<UserFuncVO> selectAll(@Param("userId")Long userId);
}
