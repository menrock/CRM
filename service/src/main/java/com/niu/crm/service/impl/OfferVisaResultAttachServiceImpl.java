package com.niu.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.niu.crm.service.BaseService;
import com.niu.crm.dao.mapper.OfferVisaResultAttachMapper;
import com.niu.crm.form.CustAttachSearchForm;
import com.niu.crm.model.OfferVisaResultAttach;
import com.niu.crm.model.User;
import com.niu.crm.service.OfferVisaResultAttachService;

@Service
public class OfferVisaResultAttachServiceImpl extends BaseService implements
		OfferVisaResultAttachService {

	@Autowired
	private OfferVisaResultAttachMapper attachMapper;

	@Override
	public OfferVisaResultAttach load(Long id) {
		OfferVisaResultAttach attach = attachMapper.selectByPrimaryKey(id);
		return attach;
	}

	@Override
	public void save(User user, OfferVisaResultAttach attach) {
		attach.setCreatorId(user.getId());
		attachMapper.insert(attach);
	}

	@Override
	public Boolean delete(User user, Long id) {
		attachMapper.delete(id);		
		return Boolean.TRUE;
	}

	@Override
	public List<OfferVisaResultAttach> queryAttach(CustAttachSearchForm searchForm,
			Pageable pageable) {
		List<OfferVisaResultAttach> ls = attachMapper
				.queryAttach(searchForm, pageable);
		return ls;
	}

	@Override
	public Integer countAttach(CustAttachSearchForm searchForm) {
		return attachMapper.countAttach(searchForm);
	}

}
