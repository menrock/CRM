package com.niu.crm.web.controller;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.form.AlarmSearchForm;
import com.niu.crm.model.Alarm;
import com.niu.crm.model.User;
import com.niu.crm.model.type.MessageType;
import com.niu.crm.service.AlarmService;
import com.niu.crm.service.UserService;

@Controller
@RequestMapping(value = "/alarm")
public class AlarmController extends BaseController{
	@Autowired
	AlarmService alarmService;
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/submit.do")
    @ResponseBody
	public ResponseObject<Boolean> submitMessage(HttpServletRequest request, int[] alarm_ways, Alarm alarm){
		User user = this.getCurrentUser();
		
		for(int i=0; alarm_ways !=null && i < alarm_ways.length; i++){
			if(i==0)
				alarm.setAlarmWay(alarm_ways[i]);
			else
				alarm.setAlarmWay( alarm.getAlarmWay()|alarm_ways[i]);
		}
		if( alarm.getAlarmWay() == null ){
			alarm.setAlarmWay(MessageType.SYS);
		}
		
		String alarm_time = request.getParameter("alarm_time");
		if(StringUtils.isNotBlank(alarm_time)){
			java.text.SimpleDateFormat sdf = null;
			if(alarm_time.length() == 8)
				sdf = new java.text.SimpleDateFormat("HH:mm:ss");
			else
				sdf = new java.text.SimpleDateFormat("HH:mm");
			try{
				java.sql.Time t = new java.sql.Time(sdf.parse(alarm_time).getTime());
				alarm.setAlarmTime(t); 
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(alarm.getId() == null){
			alarm.setCreatorId(user.getId());
			alarmService.add(alarm);
		}else{
			alarmService.update(alarm);
		}
		
		//立即发送
		if(alarm.getAlarmDate() == null)
			alarmService.send(alarm);
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/myAlarm.do")
	public String myAlarm(Model model){
		User user = this.getCurrentUser();
		
		return "/message/myAlarm";
	}
	
	@RequestMapping(value = "/myAlarmData.do")
    @ResponseBody
	public Map<String, Object> myAlarmData(HttpServletRequest req,AlarmSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		
		int total = alarmService.countAlarm(form);
		List<Alarm> ls = alarmService.queryAlarm(form, pager);
		
		for(Alarm vo:ls){
			vo.translateAlarmWay();
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	@RequestMapping(value = "/delete.do")
    @ResponseBody
	public int deleteAlarm(Long[] ids){
		int count = 0;
		for(int i=0; ids !=null && i < ids.length; i++){
			alarmService.delete(ids[i]);
			count ++;
		}
		
		return count;
	}
	
	@RequestMapping(value = "/listDataByStu.do")
    @ResponseBody
	public Map<String, Object> listDataByStu(HttpServletRequest req, AlarmSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		
		int total = alarmService.countAlarm(form);
		List<Alarm> ls = alarmService.queryAlarm(form, pager);
		
		for(Alarm vo:ls){
			
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
}
