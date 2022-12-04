package com.niu.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.niu.crm.dao.mapper.LxVisitInviteMapper;
import com.niu.crm.form.VisitInviteSearchForm;
import com.niu.crm.model.LxVisitInvite;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.LxVisitInviteService;
import com.niu.crm.vo.LxVisitInviteVO;

@Service
public class LxVisitInviteServiceImpl extends BaseService implements
		LxVisitInviteService {
	
	@Autowired
    private LxVisitInviteMapper visitInviteMapper;

	@Override
	public int add(LxVisitInvite invite) {
		return visitInviteMapper.insert(invite);
	}

	@Override
	public LxVisitInviteVO load(Long id) {
		return visitInviteMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<LxVisitInviteVO> loadByStuId(Long stuId) {
		return visitInviteMapper.selectByStuId(stuId);
	}

	@Override
	public int delete(Long id) {
		return visitInviteMapper.delete(id);
	}

	@Override
	public int updateInvite(LxVisitInvite invite) {
		return visitInviteMapper.updateInvite(invite);
	}

	@Override
	public int updateVisit(LxVisitInvite invite) {
		return visitInviteMapper.updateVisit(invite);
	}

	@Override
	public int countInvite(VisitInviteSearchForm form) {
		return visitInviteMapper.countInvite(form);
	}

	@Override
	public List<LxVisitInviteVO> queryInvite(VisitInviteSearchForm form,
			Pageable pager) {
		return visitInviteMapper.queryInvite(form, pager);
	}

}
