package com.niu.crm.service;

import java.util.List;
import com.niu.crm.model.PaymentLine;
import com.niu.crm.vo.PaymentLineVO;
import com.niu.crm.model.User;

public interface PaymentLineService {
	
	PaymentLine load(Long id);

	List<PaymentLineVO> loadByPayId(Long payId);
	
	int add(User user, PaymentLine line);
    
    void delete(User user, Long id);

    int update(User user, PaymentLine payment);
}
