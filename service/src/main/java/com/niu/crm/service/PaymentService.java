package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.PaymentSearchForm;
import com.niu.crm.model.Payment;
import com.niu.crm.model.User;
import com.niu.crm.vo.PaymentVO;

public interface PaymentService {
	
	Payment load(Long id);
	
	PaymentVO loadVO(Long id);

	List<PaymentVO> loadByCstmId(Long cstmId);
	
	int add(User user, PaymentVO payment);
    
    void delete(User user, Long id);

    int update(User user, PaymentVO payment);

    int confirm(User user, PaymentVO payment);
    
    int revoke(User user, PaymentVO payment);
    
    int queryCount(PaymentSearchForm form);
	
    List<PaymentVO> query(PaymentSearchForm form, Pageable pageable);
}
