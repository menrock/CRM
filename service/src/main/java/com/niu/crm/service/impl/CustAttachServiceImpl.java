package com.niu.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.niu.crm.service.BaseService;
import com.niu.crm.dao.mapper.CustAttachMapper;
import com.niu.crm.form.CustAttachSearchForm;
import com.niu.crm.model.CustAttach;
import com.niu.crm.model.User;
import com.niu.crm.service.CustAttachService;

@Service
public class CustAttachServiceImpl extends BaseService implements
		CustAttachService {

	@Autowired
    private CustAttachMapper  custAttachMapper;
	
	@Override
	public CustAttach load(Long id) {
		CustAttach attach = custAttachMapper.selectByPrimaryKey(id);
		return attach;
	}

	@Override
	public void save(User user, CustAttach attach) {
		attach.setCreatorId(user.getId());
		custAttachMapper.insert(attach);
	}

	@Override
	public Boolean delete(User user, Long id) {
		custAttachMapper.delete(id);
		return Boolean.TRUE;
	}
	
	@Override
	public List<CustAttach> queryAttach(CustAttachSearchForm searchForm, Pageable pageable){
		List<CustAttach> ls = custAttachMapper.queryAttach(searchForm, pageable);
		return ls;
	}
	@Override
	public Integer countAttach(CustAttachSearchForm searchForm){
		return custAttachMapper.countAttach(searchForm);
	}

}
