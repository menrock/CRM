package com.niu.crm.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.niu.crm.dao.mapper.LxContactRecordMapper;
import com.niu.crm.dao.mapper.LxCustAssignMapper;
import com.niu.crm.dao.mapper.ZxgwCallbackRemindMapper;
import com.niu.crm.form.CallbackSearchForm;
import com.niu.crm.model.StuZxgwEx;
import com.niu.crm.model.ZxgwCallbackRemind;
import com.niu.crm.model.type.CallbackRemindType;
import com.niu.crm.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.niu.crm.service.ZxgwCallbackRemindService;
import com.niu.crm.vo.LxCustomerZxgwVO;
import com.niu.crm.vo.ZxgwCallbackRemindVO;

@Service
public class ZxgwCallbackRemindServiceImpl extends BaseService implements ZxgwCallbackRemindService {
    @Autowired
    private LxCustAssignMapper  assignMapper;
    @Autowired
    private ZxgwCallbackRemindMapper  remindMapper;
    @Autowired
    private LxContactRecordMapper  contactMapper;

	@Override
	public ZxgwCallbackRemind load(Long id) {
		return remindMapper.selectById(id);
	}
	
	@Override
	public Integer countRemind(CallbackSearchForm searchForm){
		return remindMapper.countRemind(searchForm);
	}

	@Override
	public List<ZxgwCallbackRemindVO> queryRemind(CallbackSearchForm searchForm, Pageable pageable){
		return remindMapper.queryRemind(searchForm, pageable);
	}
	
	/* 
	 * 【紧急】+【重要】+【未接听】(35,29,350)
	 */
	@Override
	public List<StuZxgwEx> getNeedRemindForWeek(Integer maxRows) {
		List<StuZxgwEx> lsRemind = new ArrayList<StuZxgwEx>();
		
		Date firstDay = getFirstDayOfWeek(null);
				
		CallbackSearchForm searchForm = new CallbackSearchForm();
		searchForm.setCallbackType(CallbackRemindType.WEEKLY);
		searchForm.setSignFlag(Boolean.FALSE);
		
		try{
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
						
			searchForm.setStu_level(new Long[]{29L,35L,350L});
			searchForm.setAssign_to(sdf.format(firstDay) );		
				
		}
		catch(Exception e){
			getLogger().error("", e);
		}
		
		List<StuZxgwEx> ls = remindMapper.queryStudentForRemind(searchForm, maxRows);
		for(StuZxgwEx ex:ls){
			//已签约或者无效的资源顾问无需再回访
			if(ex.getStuLevel() >29 && ex.getStuLevel() < 34 )
				continue;
			
			lsRemind.add(ex);
		}
		
		return lsRemind;
	}
	
	/**
	 * 月度回访：【犹豫待定】+【长期潜在】(309,311)
	 */
	@Override
	public List<StuZxgwEx> getNeedRemindForMonth(Integer maxRows) {
		Date firstOfThis = getFirstDayOfMonth(null);
				
		CallbackSearchForm searchForm = new CallbackSearchForm();
		searchForm.setCallbackType(CallbackRemindType.MONTHLY);
		searchForm.setSignFlag(Boolean.FALSE);
		//普通
		searchForm.setStu_level(new Long[]{309L, 311L});
		
		
		try{
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
						
			//searchForm.setAssign_from(sdf.format(firstOfLast));
			searchForm.setAssign_to(sdf.format(firstOfThis) );		
		}
		catch(Exception e){
			getLogger().error("", e);
		}
		
		List<StuZxgwEx> ls = remindMapper.queryStudentForRemind(searchForm, maxRows);
		
		return ls;
	}

	@Override
	public int add(ZxgwCallbackRemind record) {
		if(record.getSourceZxgwId() == null)
			record.setSourceZxgwId(record.getZxgwId());
		
		return remindMapper.insert(record);
	}

	@Override
	public int delete(String remindType, Date endDate) {
		return remindMapper.delete(remindType, endDate);
	}
	
	private static Date getFirstDayOfWeek(Date d){
		if(d ==null)
			d = new java.util.Date();
		java.util.Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		try{
			d = sdf.parse( sdf.format(cal.getTime()) );
			cal.setTimeInMillis(d.getTime());
		}catch(Exception e){
			e.printStackTrace();
			cal = null;
		}
		return cal.getTime();
	}
	
	private static Date getFirstDayOfMonth(Date d){
		if(d ==null)
			d = new java.util.Date();
		java.util.Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.DAY_OF_MONTH, 1);
	
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		
		try{
			d = sdf.parse( sdf.format(cal.getTime()) );
			cal.setTimeInMillis(d.getTime());
		}catch(Exception e){
			e.printStackTrace();
			cal = null;
		}
		return cal.getTime();
	}
}