package com.niu.crm.web.controller;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.form.SmsSearchForm;
import com.niu.crm.form.SysMessageSearchForm;
import com.niu.crm.model.Customer;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.SmsMessage;
import com.niu.crm.model.SysMessage;
import com.niu.crm.model.User;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.LxCustomerService;
import com.niu.crm.service.SmsService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.UserFuncVO;

@Controller
@RequestMapping(value = "/sms")
public class SmsController extends BaseController{
	@Autowired
	private SmsService smsService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private LxCustomerService lxCustomerService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserFuncService userFuncService;
	
	@RequestMapping(value = "/common")
    public String commonTemplate(){
		return "sms/smsTemplate";
	}
	
	@RequestMapping(value = "/student")
    public String studentTempalte(ModelMap model, Long id){
		LxCustomer stu = lxCustomerService.load(id);
		Customer cust = customerService.load(stu.getCstmId());
		
		model.put("stuId", id);
		model.put("customer", cust);
		return "sms/smsTemplate";
	}

	@RequestMapping(value = "/submit")
    @ResponseBody
	public ResponseObject<Boolean> submitMessage(SmsMessage message){
		User user = this.getCurrentUser();
		
		message.setCreatorId(user.getId());
		smsService.send(message);
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/list.do")
    public String list(ModelMap model){
		String smsCapacity = smsService.queryCapacity();
		model.addAttribute("smsCapacity", smsCapacity);
		
		return "sms/list";
	}

	@RequestMapping(value = "/listData.do")
    @ResponseBody
	public Map<String, Object> listData(HttpServletRequest req,SmsSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);

		UserFuncVO adminFunc = userFuncService.loadByCode(user.getId(), "admin");
		
		if(adminFunc ==null)
			form.setCreatorId(user.getId());	
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		
		int total = smsService.countMessage(form);
		List<SmsMessage> ls = smsService.queryMessage(form, pager);
		
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	public static void main(String... args){
		System.out.println("hello");
	}
}
