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

import com.niu.crm.dao.mapper.PxContactRecordMapper;
import com.niu.crm.dao.mapper.PxCustomerMapper;
import com.niu.crm.dao.mapper.PxStuZxgwMapper;
import com.niu.crm.dao.mapper.ZxgwCallbackRemindMapper;
import com.niu.crm.form.CallbackSearchForm;
import com.niu.crm.form.ContactRecordSearchForm;
import com.niu.crm.model.PxContactRecord;
import com.niu.crm.model.PxCustomer;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.User;
import com.niu.crm.model.ZxgwCallbackRemind;
import com.niu.crm.model.type.CallbackRemindType;
import com.niu.crm.model.type.CallbackType;
import com.niu.crm.model.type.ContactStatus;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.PxContactRecordService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.ZxgwCallbackRemindService;
import com.niu.crm.vo.ZxgwCallbackRemindVO;

@Service
public class PxContactRecordServiceImpl extends BaseService implements PxContactRecordService {

    @Autowired
    private PxContactRecordMapper  contactRecordMapper;
    @Autowired
    private ZxgwCallbackRemindMapper  callbackRemindMapper;
    @Autowired
    private PxCustomerMapper  lxCustomerMapper;
    @Autowired
    private PxStuZxgwMapper  stuZxgwMapper;
	@Autowired
	private UserService userService;	
	@Autowired
	private ZxgwCallbackRemindService callbackRemindService;

	@Override
	public PxContactRecord load(Long id){
		return contactRecordMapper.selectById(id);
	}
	
	@Transactional
	@Override
	public int add(User user, PxContactRecord record){
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
		PxContactRecord record = this.load(id);
    	contactRecordMapper.delete(id);
		this.refreshContactDate(record);
    }

	@Override
    public int update(User user, PxContactRecord record){
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
    public void refreshContactDate(PxContactRecord record){
		Long stuId = record.getStuId();
		Long zxgwId = record.getGwId();
		
		List<PxContactRecord> ls = contactRecordMapper.selectByStuId(stuId, null);
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
    public List<PxContactRecord> query(ContactRecordSearchForm form, Pageable page){
		List<PxContactRecord> ls = contactRecordMapper.query(form, page);
		for(PxContactRecord record:ls){
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
    public List<PxContactRecord> queryLast(Long stuId, Long gwId, Integer count){
		List<PxContactRecord> ls = contactRecordMapper.queryLast(stuId, gwId, count);
		for(PxContactRecord record:ls){
			User u = userService.load(record.getGwId());
			if(u != null)
				record.setGwName(u.getName());
		}
    	return ls;
    }
}
