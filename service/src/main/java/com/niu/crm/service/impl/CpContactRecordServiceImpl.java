package com.niu.crm.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.dao.mapper.CpContactRecordMapper;
import com.niu.crm.dao.mapper.CpCustomerMapper;
import com.niu.crm.dao.mapper.CpStuZxgwMapper;
import com.niu.crm.dao.mapper.ZxgwCallbackRemindMapper;
import com.niu.crm.form.ContactRecordSearchForm;
import com.niu.crm.model.CpContactRecord;
import com.niu.crm.model.CpCustomer;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.User;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.CpContactRecordService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.ZxgwCallbackRemindService;

@Service
public class CpContactRecordServiceImpl extends BaseService implements CpContactRecordService {

    @Autowired
    private CpContactRecordMapper  contactRecordMapper;
    @Autowired
    private ZxgwCallbackRemindMapper  callbackRemindMapper;
    @Autowired
    private CpCustomerMapper  lxCustomerMapper;
    @Autowired
    private CpStuZxgwMapper  stuZxgwMapper;
	@Autowired
	private UserService userService;	
	@Autowired
	private ZxgwCallbackRemindService callbackRemindService;

	@Override
	public CpContactRecord load(Long id){
		return contactRecordMapper.selectById(id);
	}
	
	@Transactional
	@Override
	public int add(User user, CpContactRecord record){
		record.setCreatorId(user.getId());
		if(record.getCreatedAt() == null){
			java.util.Date now = new java.util.Date(System.currentTimeMillis());
			record.setCreatedAt(now);
			record.setUpdatedAt(now);
		}
		int count = contactRecordMapper.insert(record);
		
		this.refreshContactDate(record);
		
		return count;
	}
    
	@Transactional
	@Override
    public void delete(User user, Long id){
		CpContactRecord record = this.load(id);
    	contactRecordMapper.delete(id);
		this.refreshContactDate(record);
    }

	@Override
    public int update(User user, CpContactRecord record){
    	int count = contactRecordMapper.update(record);
		this.refreshContactDate(record);
    	
    	return count;
    }

	@Override
    public int updateAlarmed(Long id){
    	int count = contactRecordMapper.updateAlarmed(id);
    	
    	return count;
    }
	
	@Override
    public void refreshContactDate(CpContactRecord record){
		Long stuId = record.getStuId();
		Long zxgwId = record.getGwId();
		
		List<CpContactRecord> ls = contactRecordMapper.selectByStuId(stuId, null);
		Date lastDate = null;
		int contactCount = 0;
		
		if(ls.size() >0){
			lastDate = ls.get(ls.size() -1).getContactDate();
			contactCount = ls.size();
		}else{
			lastDate = null;
			contactCount = 0;
		}		
		lxCustomerMapper.updateContactDate(stuId, lastDate, contactCount);
		
		StuZxgw stuZxgw = stuZxgwMapper.selectByStuIdAndZxgw(stuId, zxgwId);
		if (stuZxgw != null) {
			ls = contactRecordMapper.selectByStuId(stuId, zxgwId);
			if (ls.size() > 0) {
				lastDate = ls.get(ls.size() - 1).getContactDate();
				contactCount = ls.size();
			} else {
				lastDate = null;
				contactCount = 0;
			}
			stuZxgwMapper.updateContactDate(stuZxgw.getId(), lastDate, contactCount);
		}
    }

	@Override
    public List<CpContactRecord> query(ContactRecordSearchForm form, Pageable page){
		List<CpContactRecord> ls = contactRecordMapper.query(form, page);
		for(CpContactRecord record:ls){
			User u = userService.load(record.getGwId());
			if(u != null)
				record.setGwName(u.getName());
		}
		
    	return ls;
    }

	/**
	 * 取最近几条联系记录
	 * @param stuId
	 * @param gwId
	 * @param count
	 * @return
	 */
	@Override
    public List<CpContactRecord> queryLast(Long stuId, Long gwId, Integer count){
		List<CpContactRecord> ls = contactRecordMapper.queryLast(stuId, gwId, count);
		for(CpContactRecord record:ls){
			User u = userService.load(record.getGwId());
			if(u != null)
				record.setGwName(u.getName());
		}
    	return ls;
    }
}
