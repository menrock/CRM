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

import com.niu.crm.dao.mapper.LxContactRecordMapper;
import com.niu.crm.dao.mapper.LxCustomerMapper;
import com.niu.crm.dao.mapper.StuZxgwMapper;
import com.niu.crm.dao.mapper.ZxgwCallbackRemindMapper;
import com.niu.crm.form.CallbackSearchForm;
import com.niu.crm.form.CallbackSimpleSearchForm;
import com.niu.crm.form.ContactRecordSearchForm;
import com.niu.crm.model.LxContactRecord;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.User;
import com.niu.crm.model.ZxgwCallbackRemind;
import com.niu.crm.model.type.CallbackRemindType;
import com.niu.crm.model.type.CallbackType;
import com.niu.crm.model.type.ContactStatus;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.LxContactRecordService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.ZxgwCallbackRemindService;
import com.niu.crm.vo.ZxgwCallbackRemindVO;

@Service
public class LxContactRecordServiceImpl extends BaseService implements LxContactRecordService {

    @Autowired
    private LxContactRecordMapper  contactRecordMapper;
    @Autowired
    private ZxgwCallbackRemindMapper  callbackRemindMapper;
    @Autowired
    private LxCustomerMapper  lxCustomerMapper;
    @Autowired
    private StuZxgwMapper  stuZxgwMapper;
	@Autowired
	private UserService userService;	
	@Autowired
	private ZxgwCallbackRemindService callbackRemindService;

	@Override
	public LxContactRecord load(Long id){
		return contactRecordMapper.selectById(id);
	}
	
	@Transactional
	@Override
	public int add(User user, LxContactRecord record){
		record.setCreatorId(user.getId());
		if(record.getCreatedAt() == null){
			java.util.Date now = new java.util.Date(System.currentTimeMillis());
			record.setCreatedAt(now);
			record.setUpdatedAt(now);
		}
		int count = contactRecordMapper.insert(record);
		
		//首次回访时 没接听 需要第二天回访
		if(record.getCallbackType() == CallbackType.DAY1 && record.getContactStatus() !=ContactStatus.Y){
			LxCustomer student = lxCustomerMapper.selectById(record.getStuId());
			
			if( student.getCompanyId().intValue() == 4){
				Calendar cal = Calendar.getInstance();
				int y = cal.get(Calendar.YEAR);
				int m = cal.get(Calendar.MONTH);
				int d = cal.get(Calendar.DATE);
				Date today = null;
				{
					Calendar cal2 = java.util.Calendar.getInstance();
					cal2.clear();
					cal2.set(y, m, d);
					today = cal2.getTime();
				}
				
				ZxgwCallbackRemind remind = null;
				//当日回访
				remind = new ZxgwCallbackRemind();
				remind.setStuId(record.getStuId());
				remind.setZxgwId(record.getGwId());
				remind.setRemindType(CallbackRemindType.DAY2);
				remind.setLatestContactTime( DateUtils.addHours(today,23) );
				callbackRemindService.add(remind);
			}
		}
		
		//更新回访提醒中的 回访时间
		CallbackType callbackType = record.getCallbackType();
		{
			Pageable pager = new PageRequest(0, 100);
			CallbackSimpleSearchForm form = new CallbackSimpleSearchForm();
			form.setStuId(record.getStuId());
			form.setZxgwId(record.getGwId());
			form.setCallbackType( CallbackRemindType.valueOf( record.getCallbackType().name() ) );
			
			List<ZxgwCallbackRemind> ls = callbackRemindMapper.simpleList(form, null);
			for(int i=0; ls !=null && i < ls.size(); i++){
				ZxgwCallbackRemind item = ls.get(i);
				ZxgwCallbackRemind callbackRemind = new ZxgwCallbackRemind();
				callbackRemind.setId(item.getId());
				callbackRemind.setContactId(record.getId() );
				callbackRemind.setContactTime(record.getCreatedAt());		
				callbackRemindMapper.updateContactTime(callbackRemind);
			}	
		}
		
		
		this.refreshContactDate(record);
		
		return count;
	}
    
	@Transactional
	@Override
    public void delete(User user, Long id){
		LxContactRecord record = this.load(id);
    	contactRecordMapper.delete(id);
		this.refreshContactDate(record);
    }

	@Override
    public int update(User user, LxContactRecord record){
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
    public void refreshContactDate(LxContactRecord record){
		Long stuId = record.getStuId();
		Long zxgwId = record.getGwId();
		
		List<LxContactRecord> ls = contactRecordMapper.selectByStuId(stuId, null);
		Date kfLastDate=null, gwLastDate=null, leaderLastDate=null, lastDate = null;
		int contactCount = ls.size();
		
		for(int i=0; i < ls.size(); i++){
			LxContactRecord item = ls.get(i);
			lastDate = item.getContactDate();
			CallbackType callType = record.getCallbackType();
			if( callType == null){
			}
			else if( callType == CallbackType.KF){
				kfLastDate = item.getContactDate();
			}else if( callType == CallbackType.DAY5){
				leaderLastDate = item.getContactDate();
			}else{
				gwLastDate = item.getContactDate();
			}
		}
		
		
		LxCustomer stu = new LxCustomer();
		stu.setId(stuId);
		stu.setContactCount(contactCount);
		stu.setKfLastContactDate(kfLastDate);
		stu.setGwLastContactDate(gwLastDate);
		stu.setLeaderLastContactDate(leaderLastDate);
		stu.setLastContactDate(lastDate);
		lxCustomerMapper.updateContactDate(stu);
		
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
    public List<LxContactRecord> query(ContactRecordSearchForm form, Pageable page){
		List<LxContactRecord> ls = contactRecordMapper.query(form, page);
		for(LxContactRecord record:ls){
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
    public List<LxContactRecord> queryLast(Long stuId, Long gwId, Integer count){
		List<LxContactRecord> ls = contactRecordMapper.queryLast(stuId, gwId, count);
		for(LxContactRecord record:ls){
			User u = userService.load(record.getGwId());
			if(u != null)
				record.setGwName(u.getName());
		}
    	return ls;
    }
}
