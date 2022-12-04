package com.niu.crm.web.controller;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.form.SysMessageSearchForm;
import com.niu.crm.model.SysMessage;
import com.niu.crm.model.User;
import com.niu.crm.service.SysMessageService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.LxCustomerVO;

@Controller
@RequestMapping(value = "/sysMessage")
public class SysMessageController extends BaseController{
	@Autowired
	private SysMessageService sysMessageService;
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value = "/delete.do")
    @ResponseBody
	public int deleteAlarm(Long[] ids){
		int count = 0;
		for(int i=0; ids !=null && i < ids.length; i++){
			sysMessageService.delete(ids[i]);
			count ++;
		}
		return count;
	}
	
	@RequestMapping(value = "/myMessage.do")
    public String myMessages(ModelMap model){
		
		return "message/myMessage";
	}

	@RequestMapping(value = "/myMessageData.do")
    @ResponseBody
	public Map<String, Object> myMessageData(HttpServletRequest req,SysMessageSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		
		
		form.setOwnerId(user.getId());		
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		
		int total = sysMessageService.countMessage(form);
		List<SysMessage> ls = sysMessageService.queryMessage(form, pager);
		
		for(SysMessage msg:ls){
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	@RequestMapping(value = "/myPromptData.do")
    @ResponseBody
	public List<SysMessage> myPromptData(HttpServletRequest req){
		User user = this.getCurrentUser();		
		Pageable pager = new PageRequest(0, 3);
		
		SysMessageSearchForm form = new SysMessageSearchForm();
		form.setOwnerId(user.getId());
		form.setStatus(0);
		form.setStatusOp("=");
		
		List<SysMessage> ls = sysMessageService.queryMessage(form, pager);
		for(SysMessage msg:ls){
			msg.setStatus( msg.getStatus()|0x1);
			sysMessageService.update(msg);
		}
		
		return ls;
	}
	
	public static void main(String... args){
		System.out.println("hello");
	}
}
