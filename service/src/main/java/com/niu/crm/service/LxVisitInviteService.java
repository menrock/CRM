package com.niu.crm.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.VisitInviteSearchForm;
import com.niu.crm.model.LxVisitInvite;
import com.niu.crm.vo.LxVisitInviteVO;

public interface LxVisitInviteService {
	
	int add(LxVisitInvite invite);
	
	LxVisitInviteVO load(Long id);
	
	List<LxVisitInviteVO> loadByStuId(Long stuId);

	int delete(Long id);
	
	int updateInvite(LxVisitInvite invite);
	
	int updateVisit(LxVisitInvite visit);
	
	int countInvite(VisitInviteSearchForm form);
	
	List<LxVisitInviteVO> queryInvite(VisitInviteSearchForm form, Pageable pager);
}
