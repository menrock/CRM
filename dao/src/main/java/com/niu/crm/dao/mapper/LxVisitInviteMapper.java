package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.VisitInviteSearchForm;
import com.niu.crm.model.LxVisitInvite;
import com.niu.crm.vo.LxVisitInviteVO;

public interface LxVisitInviteMapper {
	
	LxVisitInviteVO selectByPrimaryKey(Long id);
	
	List<LxVisitInviteVO> selectByStuId(Long stuId);

	int delete(Long id);
	
	int insert(LxVisitInvite invite);
	
	int updateInvite(LxVisitInvite invite);
	
	int updateVisit(LxVisitInvite invite);
	
	int countInvite(@Param("params")VisitInviteSearchForm form);
	
	List<LxVisitInviteVO> queryInvite(@Param("params")VisitInviteSearchForm form, @Param("pager")Pageable pager);
}
