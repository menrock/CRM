package com.niu.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.niu.crm.service.BaseService;
import com.niu.crm.dao.mapper.CaseXcclAttachMapper;
import com.niu.crm.form.CustAttachSearchForm;
import com.niu.crm.model.CaseXcclAttach;
import com.niu.crm.model.User;
import com.niu.crm.service.CaseXcclAttachService;

@Service
public class CaseXcclAttachServiceImpl extends BaseService implements
		CaseXcclAttachService {

	@Autowired
	private CaseXcclAttachMapper attachMapper;

	@Override
	public CaseXcclAttach load(Long id) {
		CaseXcclAttach attach = attachMapper.selectByPrimaryKey(id);
		return attach;
	}

	@Override
	public void save(User user, CaseXcclAttach attach) {
		attach.setCreatorId(user.getId());
		attachMapper.insert(attach);
	}

	@Override
	public Boolean delete(User user, Long id) {
		attachMapper.delete(id);
		
		return Boolean.TRUE;
	}

	@Override
	public List<CaseXcclAttach> queryAttach(CustAttachSearchForm searchForm,
			Pageable pageable) {
		List<CaseXcclAttach> ls = attachMapper
				.queryAttach(searchForm, pageable);
		return ls;
	}

	@Override
	public Integer countAttach(CustAttachSearchForm searchForm) {
		return attachMapper.countAttach(searchForm);
	}

}
