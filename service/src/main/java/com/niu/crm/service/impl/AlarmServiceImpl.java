package com.niu.crm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.dao.mapper.AlarmMapper;
import com.niu.crm.form.AlarmSearchForm;
import com.niu.crm.model.Alarm;
import com.niu.crm.model.SmsMessage;
import com.niu.crm.model.SysMessage;
import com.niu.crm.model.type.AlarmRepeatType;
import com.niu.crm.model.type.MessageType;
import com.niu.crm.service.AlarmService;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.SmsService;
import com.niu.crm.service.SysMessageService;
import com.niu.crm.service.WechatService;

@Service
public class AlarmServiceImpl extends BaseService implements AlarmService {
	@Autowired
    private AlarmMapper alarmMapper;
	@Autowired
	private WechatService wechatService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private SysMessageService sysMessageService;
    
	@Override
	public int add(Alarm alarm) {
		if(alarm.getRepeatType() == AlarmRepeatType.ONLY){
			if(alarm.getAlarmDate() !=null && alarm.getAlarmTime() == null)
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"提醒日期和时间填写不正确(必须都填或都不填)");
			if(alarm.getAlarmDate() ==null && alarm.getAlarmTime() != null)
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"提醒日期和时间填写不正确(必须都填或都不填)");
		}
		
		if(StringUtils.isEmpty(alarm.getAlarmUnitIds()) && StringUtils.isEmpty(alarm.getAlarmUserIds()) )
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"提醒对象不能为空");
		
		return alarmMapper.insert(alarm);
	}

	@Override
	public int update(Alarm alarm) {
		return alarmMapper.update(alarm);
	}

	@Override
	public int delete(Long id) {
		return alarmMapper.delete(id);
	}

	@Override
	public Alarm load(Long id) {
		return alarmMapper.selectById(id);
	}

	@Transactional
	@Override
	public int send(Alarm alarm) {
		Long[] toUnitIds = null;
		Long[] toUserIds = null;
		
		if(StringUtils.isNotEmpty(alarm.getAlarmUnitIds())){
			String[] arr = alarm.getAlarmUnitIds().split(",");
			toUnitIds = new Long[arr.length];
			for(int i=0; i < arr.length; i++)
				toUnitIds[i] = Long.parseLong(arr[i]);			
		}
		if(StringUtils.isNotEmpty(alarm.getAlarmUserIds())){
			String[] arr = alarm.getAlarmUserIds().split(",");
			toUserIds = new Long[arr.length];
			for(int i=0; i < arr.length; i++)
				toUserIds[i] = Long.parseLong(arr[i]);
		}
		
		if( (alarm.getAlarmWay() & MessageType.SYS) >0){
			//按部门提醒
			for(int i=0; toUnitIds !=null && i < toUnitIds.length; i++){
			}
			
			//按用户提醒
			for(int i=0; toUserIds !=null && i < toUserIds.length; i++){
				Long toUserId = toUserIds[i];
				SysMessage msg = new SysMessage();
				msg.setContent(alarm.getContent());
				msg.setUrl(alarm.getUrl());
				msg.setSenderId(alarm.getCreatorId());
				msg.setOwnerId(toUserId);
				msg.setStuId(alarm.getStuId());
					
				sysMessageService.add(msg);
			}
		}
		
		if( (alarm.getAlarmWay() & MessageType.WX)>0){
			try{
				wechatService.sendTxtMessage(alarm.getContent(), toUserIds, toUserIds);
			}catch(Exception e){
				getLogger().error("id=" + alarm.getId(), e);
			}
		}
			
		if( (alarm.getAlarmWay() & MessageType.SMS)>0){
			String content = alarm.getContent();
			//按部门提醒
			for(int i=0; toUnitIds !=null && i < toUnitIds.length; i++){
				smsService.send2Unit(content, toUnitIds[i]);
			}
			
			//按用户提醒
			for(int i=0; toUserIds !=null && i < toUserIds.length; i++){
				smsService.send2User(content, toUserIds[i]);
			}
		}	
		
		if(alarm.getRepeatType() == AlarmRepeatType.ONLY)
			alarm.setEnabled(Boolean.FALSE);
		
		alarm.setAlarmedTime( new Date() );
		return alarmMapper.updateAfterSend(alarm);
	}

	@Override
	public List<Alarm> queryAlarm(AlarmSearchForm form, Pageable pageable) {		
		return alarmMapper.queryAlarm(form, pageable);
	}

	@Override
	public Integer countAlarm(AlarmSearchForm form) {
		return alarmMapper.countAlarm(form);
	}

}
