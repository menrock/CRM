package com.niu.crm.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.niu.crm.dao.mapper.LxPreCustMapper;
import com.niu.crm.dao.mapper.PreContactRecordMapper;
import com.niu.crm.form.PreContactRecordSearchForm;
import com.niu.crm.model.LxContactRecord;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.PreContactRecord;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.User;
import com.niu.crm.model.type.CallbackType;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.PreContactRecordService;

@Service
public class PreContactRecordServiceImpl extends BaseService implements
		PreContactRecordService {
	@Autowired
    private PreContactRecordMapper  contactRecordMapper;
	@Autowired
    private LxPreCustMapper  lxPreCustMapper;

	@Override
	public PreContactRecord load(Long id) {
		return contactRecordMapper.selectByPrimaryKey(id);
	}

	@Override
	public int add(User user, PreContactRecord record) {
		record.setCreatorId(user.getId());
		record.setCreatedAt(new Date() );
		int count = contactRecordMapper.insert(record);
		
		refreshContactDate(record);
		
		return count;
	}

	@Override
	public void delete(User user, Long id) {
		contactRecordMapper.delete(id);
	}

	@Override
	public int update(User user, PreContactRecord record) {
		return contactRecordMapper.update(record);
	}

	@Override
	public int updateAlarmed(Long id) {
		int count = contactRecordMapper.updateAlarmed(id);
    	
    	return count;
	}

	@Override
	public void refreshContactDate(PreContactRecord record) {
		Long cstmId = record.getCstmId();
		
		List<PreContactRecord> ls = contactRecordMapper.selectByCstmId(cstmId, null);
		Date lastDate = null;
		int contactCount = ls.size();
		
		for(int i=0; i < ls.size(); i++){
			PreContactRecord item = ls.get(i);
			if(lastDate == null || item.getContactDate().getTime() > lastDate.getTime() )
				lastDate = item.getContactDate();
		}
		
		lxPreCustMapper.updateContactDate(cstmId, lastDate, contactCount);
	}

	@Override
	public List<PreContactRecord> query(PreContactRecordSearchForm form,
			Pageable page) {
		List<PreContactRecord> ls = contactRecordMapper.query(form, page);
		
		return ls;
	}

	@Override
	public List<PreContactRecord> queryLast(Long stuId, Long gwId, Integer count) {
		List<PreContactRecord> ls = contactRecordMapper.queryLast(stuId, gwId, count);
		return ls;
	}

}
