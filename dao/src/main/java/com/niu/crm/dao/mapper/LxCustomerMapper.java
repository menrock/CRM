package com.niu.crm.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.vo.LxCustomerVO;

public interface LxCustomerMapper {
	int insert(LxCustomer customer);
	
	int update(LxCustomer customer);
	
	int updateByPrimaryKeySelective(LxCustomer customer);
	
	int updateSignDate(LxCustomer customer);
	
	int delete(Long id);
	
	int updateContactDate(LxCustomer customer);
	
	Long selectCstmIdById(Long id);
	
	LxCustomer selectById(Long id);
	LxCustomer selectByCstmId(@Param("cstmId")Long cstmId);
	
	int countStudents(@Param("params") StudentSearchForm form);
	
	List<LxCustomerVO> queryStudents(@Param("params") StudentSearchForm form, @Param("pager")Pageable pageable);
	
	List<LxCustomerVO> queryRepeat(@Param("excludeCstmId")Long excludeCstmId, @Param("phones")String[] phones);
	
	Integer changeCompany(@Param("fromCompanyId")Long fromCompanyId, @Param("toCompanyId")Long toCompanyId);
	
	Integer changeUnit(@Param("fromUnitId")Long fromUnitId, @Param("toUnitId")Long toUnitId);
	
	Integer changeStufrom(@Param("fromId")Long fromId, @Param("toId")Long toId);
}
