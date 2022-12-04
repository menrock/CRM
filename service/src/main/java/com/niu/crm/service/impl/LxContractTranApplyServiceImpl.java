package com.niu.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.dao.mapper.LxContractMapper;
import com.niu.crm.dao.mapper.LxContractTranApplyMapper;
import com.niu.crm.form.LxContractTranApplySearchForm;
import com.niu.crm.model.LxContract;
import com.niu.crm.model.LxContractTranApply;
import com.niu.crm.model.User;
import com.niu.crm.model.UserFunc;
import com.niu.crm.model.type.ApproveStatus;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.LxContractTranApplyService;
import com.niu.crm.vo.LxContractTranApplyVO;

@Service
public class LxContractTranApplyServiceImpl extends BaseService implements
		LxContractTranApplyService {
	
	@Autowired
	private LxContractTranApplyMapper tranApplyMapper;

	@Autowired
	private LxContractMapper lxContractMapper;

	@Override
	public LxContractTranApplyVO load(Long id) {
		return tranApplyMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<LxContractTranApplyVO> loadByLxConId(Long lxConId) {
		List<LxContractTranApplyVO> ls = tranApplyMapper.selectByLxConId(lxConId);
		return ls;
	}

	@Override
	public int add(LxContractTranApply apply) {
		return tranApplyMapper.insert(apply);
	}

	@Override
	public int update(LxContractTranApply apply) {
		return tranApplyMapper.update(apply);
	}
	
	@Override
	public int submit(User user, LxContractTranApply apply){
		apply.setStatus(ApproveStatus.SUBMIT);
		return tranApplyMapper.submit(apply);
	}
	
	@Transactional
	@Override
	public int approve(User user, LxContractTranApply apply){
		LxContract contract = new LxContract();
		contract.setApplyGwId(apply.getToApplyGwId());
		contract.setPlanGwId(apply.getToPlanGwId());
		contract.setWriteGwId(apply.getToWriteGwId());
		contract.setServiceGwId(apply.getToServiceGwId());
		
		if(apply.getStatus() == ApproveStatus.PASS){
			if(apply.getTranType() ==2)
				lxContractMapper.updateGuwen(contract);
			else
				lxContractMapper.updateTran2Second(contract);
		}
		
		apply.setApproverId(user.getId());
		return tranApplyMapper.approve(apply);
	}

	@Override
	public int delete(Long id) {
		return tranApplyMapper.delete(id);
	}

	@Override
	public int countApply(LxContractTranApplySearchForm form) {
		return tranApplyMapper.countApply(form);
	}

	@Override
	public List<LxContractTranApplyVO> queryApply(
			LxContractTranApplySearchForm form, Pageable pageable) {
		return tranApplyMapper.queryApply(form, pageable);
	}

}
