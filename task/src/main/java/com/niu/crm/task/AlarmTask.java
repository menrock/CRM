package com.niu.crm.task;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.niu.crm.form.AlarmSearchForm;
import com.niu.crm.form.ContactRecordSearchForm;
import com.niu.crm.model.Alarm;
import com.niu.crm.model.LxContactRecord;
import com.niu.crm.model.SmsMessage;
import com.niu.crm.model.SysMessage;
import com.niu.crm.model.User;
import com.niu.crm.model.type.AlarmRepeatType;
import com.niu.crm.service.LxContactRecordService;
import com.niu.crm.service.SmsService;
import com.niu.crm.service.SysMessageService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.WechatService;
import com.niu.crm.service.AlarmService;

@Component
public class AlarmTask extends BaseTask{
	@Autowired
	private AlarmService alarmService;
	@Autowired
	private UserService userService;
	
	
	private static final long ONE_HOUR = 60*60000L; 
	
	/**
	 * 每隔1分钟 扫描提醒
	 */
	@Scheduled(cron = "0 0/1 * * * ?")
	public void sendMessage() {
		Pageable pageable = new PageRequest(0, 500);
		AlarmSearchForm form = new AlarmSearchForm();
		
		form.setEnabled(Boolean.TRUE);
		form.setAlarmTimeTo( new java.util.Date() );
		
		List<Alarm> ls = alarmService.queryAlarm(form, pageable);
		
		for(Alarm alarm :ls){
			sendMessage(alarm);
		}
	}
	
	private void sendMessage(Alarm alarm){
		//暂时只支持发送一次的提醒
		if(alarm.getRepeatType() == AlarmRepeatType.ONLY ){
			Calendar cal = java.util.Calendar.getInstance();
			cal.clear();
			cal.setTime(alarm.getAlarmDate());
			
			{
				java.util.Calendar t = java.util.Calendar.getInstance();
				t.clear();
				t.setTime(alarm.getAlarmTime());
				cal.set(Calendar.HOUR_OF_DAY, t.get(Calendar.HOUR_OF_DAY));
				cal.set(Calendar.MINUTE, t.get(Calendar.MINUTE));
				cal.set(Calendar.SECOND, t.get(Calendar.SECOND));
			}
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					
			long now = System.currentTimeMillis();
			
			//逾期1小时的不发
			if(cal.getTimeInMillis() <= now && cal.getTimeInMillis() > now - ONE_HOUR)
				alarmService.send(alarm);
		}
	}
}
