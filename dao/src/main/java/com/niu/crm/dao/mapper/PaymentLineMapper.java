package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.niu.crm.model.PaymentLine;
import com.niu.crm.vo.PaymentLineVO;

public interface PaymentLineMapper {
	
	int insert(PaymentLine line);
	
	int update(PaymentLine line);
	
	int deleteByPayId(@Param("payId")Long payId);
	int delete(Long id);
	
	PaymentLine selectById(Long id);
	
	List<PaymentLineVO> selectByPayId(@Param("payId")Long payId);
}
