package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.model.CustPhone;

public interface CustPhoneMapper {
	
	int insert(CustPhone custPhone);
	
	int update(CustPhone custPhone);
	
	int delete(@Param("id")Long id);
	
	int deleteByCstmId(@Param("cstmId")Long cstmId);
		
	List<CustPhone> selectByCstmId(@Param("cstmId")Long cstmId, @Param("isMain")Boolean isMain);
}
