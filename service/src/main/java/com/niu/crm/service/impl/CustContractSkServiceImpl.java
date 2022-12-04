package com.niu.crm.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.dao.mapper.CustContractLineMapper;
import com.niu.crm.dao.mapper.CustContractMapper;
import com.niu.crm.dao.mapper.CustContractSkMapper;
import com.niu.crm.dao.mapper.LxContractMapper;
import com.niu.crm.form.SkSearchForm;
import com.niu.crm.model.CustContract;
import com.niu.crm.model.CustContractSk;
import com.niu.crm.model.Dict;
import com.niu.crm.model.LxContract;
import com.niu.crm.model.User;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.CustContractSkService;
import com.niu.crm.service.DictService;
import com.niu.crm.vo.CustContractSkVO;

@Service
public class CustContractSkServiceImpl extends BaseService implements CustContractSkService {

	@Autowired
	private LxContractMapper lxContractMapper;
	@Autowired
	private CustContractSkMapper skMapper;
	@Autowired
	private DictService dictService;

	@Override
	public CustContractSk load(Long id) {
		return skMapper.selectById(id);
	}

	@Override
	public List<CustContractSkVO> loadByConId(Long conId) {
		return skMapper.selectByConId(conId);
	}

	@Transactional
	@Override
	public int batchAdd(User user, List<CustContractSk> skRecords) {
		for(int i=0; i < skRecords.size(); i++){
			add(user, skRecords.get(i));
		}
		return skRecords.size();
	}

	@Transactional
	@Override
	public int add(User user, CustContractSk skRecord) {
		canSave(skRecord);
		
		skRecord.setCreatorId(user.getId());
		calAchivement(skRecord);
		
		int rc = skMapper.insert(skRecord);
		refreshLxContractSk(skRecord.getConId());
		return rc;
	}

	@Transactional
	@Override
	public void delete(User user, Long id) {
		CustContractSk skRecord = this.load(id);
		skMapper.delete(id);
		
		refreshLxContractSk(skRecord.getConId());
	}

	@Transactional
	@Override
	public int update(User user, CustContractSk skRecord) {
		canSave(skRecord);
		
		calAchivement(skRecord);
		
		int rc= skMapper.update(skRecord);
		refreshLxContractSk(skRecord.getConId());
		return rc;
	}
	
	@Override
	public int fixAchivement(CustContractSk skRecord){
		calAchivement(skRecord);
		return skMapper.updateAchivement(skRecord);
	}
	
	@Override
	public void refreshLxContractSk(Long conId){
		Date minSkDate = skMapper.statContractFirstSkDate(conId);
		BigDecimal skAmount = skMapper.statContractSkAmount(conId);
		
		LxContract contract = new LxContract();
		contract.setFirstSkDate(minSkDate);
		contract.setSkValue(skAmount);
		
		lxContractMapper.updateSk(conId, contract);
	}
	

	@Override
	public int countSk(SkSearchForm form){
		return skMapper.countSk(form);
	}
    
	@Override
	public CustContractSkVO queryStat(SkSearchForm form){
		return skMapper.queryStat(form);
	}
	
	@Override
	public List<CustContractSkVO> querySk(SkSearchForm form, Pageable pageable){
		List<CustContractSkVO> ls = skMapper.querySk(form, pageable); 
		for(CustContractSkVO vo:ls){
			if(vo.getConType() !=null){
				String conTypeName = dictService.load(vo.getConType()).getDictName();
				vo.setConTypeName(conTypeName);
			}
		}
		return ls;
	}
	
	private void calAchivement(CustContractSk skRecord){
		//项目款 业绩 *0.45
		BigDecimal xmkRatio = new BigDecimal("0.45");
				
		//项目款业绩需要乘系数
		if(skRecord.getItemId() == 394)
			skRecord.setAchivement( skRecord.getSkValue().multiply(xmkRatio).setScale(2, BigDecimal.ROUND_HALF_UP) );
		else
			skRecord.setAchivement( skRecord.getSkValue() );
	}
	
	private void canSave(CustContractSk skRecord){
		List<Dict> lsChild = dictService.loadChildren(skRecord.getItemId());
		if(lsChild.size() >0)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "费用科目只能选择叶子科目");
			
		
		//检查当前科目下合计是否为负
		if(skRecord.getSkValue().compareTo(BigDecimal.ZERO) <0){
			SkSearchForm form = new SkSearchForm();
			form.setCstmId(skRecord.getCstmId());
			form.setConId(skRecord.getConId());
			form.setItemId(skRecord.getItemId());
			
			Pageable pager = new PageRequest(0, 10000);
			BigDecimal sumSkVal = BigDecimal.ZERO;
			List<CustContractSkVO> ls = skMapper.querySk(form, pager); 
			for(CustContractSkVO item:ls){
				if(item.getId().equals(skRecord.getId()) )
					continue;
				sumSkVal = sumSkVal.add(item.getSkValue());
			}
			if(sumSkVal.add(skRecord.getSkValue()).compareTo(BigDecimal.ZERO) <0)
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "科目合计金额不能为负");
		}		
	}
}
