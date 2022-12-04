package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.PaymentSearchForm;
import com.niu.crm.model.Payment;
import com.niu.crm.vo.PaymentVO;

public interface PaymentMapper {
	
	int insert(Payment payment);
	
	int update(Payment payment);
	
	int delete(Long id);
	
	int fzConfirm(Payment payment);
	
	int confirm(Payment payment);

	int fzRevoke(Payment payment);
	int revoke(Payment payment);
	
	PaymentVO selectById(Long id);
	
	List<PaymentVO> selectByCstmId(@Param("cstmId")Long cstmId);
	
	int queryCount(@Param("params")PaymentSearchForm form);
	
	List<PaymentVO> query(@Param("params")PaymentSearchForm form, @Param("pager")Pageable pageable);
}
