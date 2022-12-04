package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.LxContractTranApplySearchForm;
import com.niu.crm.model.LxContractTranApply;
import com.niu.crm.model.User;
import com.niu.crm.vo.LxContractTranApplyVO;

public interface LxContractTranApplyService {
	
	LxContractTranApplyVO load(Long id);
	
	List<LxContractTranApplyVO> loadByLxConId(Long lxConId);
	
	int add(LxContractTranApply apply);
	
	int update(LxContractTranApply apply);
	
	int submit(User user, LxContractTranApply apply);
	
	int approve(User user, LxContractTranApply apply);
	
	int delete(Long id);
	
	int countApply(LxContractTranApplySearchForm form);
	
	List<LxContractTranApplyVO> queryApply(LxContractTranApplySearchForm form, Pageable pageable);
}
