package com.niu.crm.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.model.CpCustomer;
import com.niu.crm.vo.CpCustomerVO;

public interface CpCustomerMapper {
	int insert(CpCustomer customer);
	
	int update(CpCustomer customer);
	
	int updateByPrimaryKeySelective(CpCustomer customer);
	
	int updateSignDate(CpCustomer customer);
	
	int delete(Long id);
	
	int updateContactDate(@Param("id")Long id, @Param("lastContactDate")Date lastContactDate, @Param("contactCount") Integer contactCount);
	
	Long selectCstmIdById(Long id);
	
	CpCustomer selectById(Long id);
	CpCustomer selectByCstmId(@Param("cstmId")Long cstmId);
	
	int countStudents(@Param("params") StudentSearchForm form);
	
	List<CpCustomerVO> queryStudents(@Param("params") StudentSearchForm form, @Param("pager")Pageable pageable);
	
	List<CpCustomerVO> queryRepeat(@Param("excludeCstmId")Long excludeCstmId, @Param("phones")String[] phones);
	
	Integer changeCompany(@Param("fromCompanyId")Long fromCompanyId, @Param("toCompanyId")Long toCompanyId);
	
	Integer changeUnit(@Param("fromUnitId")Long fromUnitId, @Param("toUnitId")Long toUnitId);
	
	Integer changeStufrom(@Param("fromId")Long fromId, @Param("toId")Long toId);
}
