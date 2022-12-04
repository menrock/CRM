package com.niu.crm.dao.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.SkSearchForm;
import com.niu.crm.model.CustContractSk;
import com.niu.crm.vo.CustContractSkVO;
import com.niu.crm.vo.CustContractVO;

public interface CustContractSkMapper {
	
	int insert(CustContractSk line);
	
	int update(CustContractSk line);
	
	int updateAchivement(CustContractSk line);
	
	int delete(Long id);
	
	CustContractSk selectById(Long id);
	
	CustContractSk selectByPayLineId(Long payLineId);
	
	List<CustContractSk> selectByPayId(Long payId);
	
	List<CustContractSkVO> selectByConId(Long conId);

	//合同首次收款日期
	Date statContractFirstSkDate(@Param("conId")Long conId);
	
	//合同收款总金额
	BigDecimal statContractSkAmount(@Param("conId")Long conId);

	int countSk(@Param("params")SkSearchForm form);
	
	List<CustContractSkVO> querySk(@Param("params")SkSearchForm form, @Param("pager")Pageable pageable);
	
	CustContractSkVO queryStat(@Param("params")SkSearchForm form);
}
