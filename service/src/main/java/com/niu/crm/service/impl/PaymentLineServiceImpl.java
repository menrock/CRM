package com.niu.crm.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.dao.mapper.PaymentLineMapper;
import com.niu.crm.model.PaymentLine;
import com.niu.crm.model.User;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.PaymentLineService;
import com.niu.crm.service.DictService;
import com.niu.crm.vo.PaymentLineVO;
import com.niu.crm.vo.PaymentVO;

@Service
public class PaymentLineServiceImpl extends BaseService implements PaymentLineService {

	@Autowired
	PaymentLineMapper paymentLineMapper;
	@Autowired
	DictService dictService;

	@Override
	public PaymentLine load(Long id) {
		return paymentLineMapper.selectById(id);
	}

	@Override
	public List<PaymentLineVO> loadByPayId(Long payId) {
		return paymentLineMapper.selectByPayId(payId);
	}

	@Transactional
	@Override
	public int add(User user, PaymentLine line) {
		line.setCreatorId(user.getId());
		int rc = paymentLineMapper.insert(line);
	
		return rc;
	}

	@Transactional
	@Override
	public void delete(User user, Long id) {
		paymentLineMapper.delete(id);
	}

	@Transactional
	@Override
	public int update(User user, PaymentLine line) {
		int rc= paymentLineMapper.update(line);
		
		return rc;
	}
}
