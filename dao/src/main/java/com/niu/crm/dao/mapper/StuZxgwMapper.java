package com.niu.crm.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.vo.LxCustomerVO;
import com.niu.crm.vo.LxCustomerZxgwVO;

public interface StuZxgwMapper {
	
	StuZxgw selectByPrimaryKey(Long id);
	
	StuZxgw selectByStuIdAndZxgw(@Param("stuId")Long stuId, @Param("zxgwId")Long zxgwId);
	
	List<StuZxgw> selectByStuId(Long stuId);
	
	int insert(StuZxgw record);
	
	void update(StuZxgw record);
	
	void deleteByStuId(Long stuId);
	
	int delete(Long id);
	
	int updateContactDate(@Param("id")Long id, @Param("lastContactDate")Date lastContactDate, @Param("contactCount") Integer contactCount);
	
	int countStuZxgw(@Param("params") StudentSearchForm form);
	
	List<LxCustomerZxgwVO> queryStuZxgw(@Param("params") StudentSearchForm form, @Param("pager")Pageable pageable);
}
