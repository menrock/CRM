package com.niu.crm.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.model.CustPhone;
import com.niu.crm.model.Customer;
import com.niu.crm.model.User;
import com.niu.crm.service.CustomerService;

@Controller
@RequestMapping(value = "/customer")
public class CustomerController extends BaseController{
	@Autowired
	CustomerService customerService;
	
	
	@RequestMapping(value = "/phoneData")
    @ResponseBody
    public Map<String,Object> phoneData(Long id, Boolean isMain){
		User user = this.getCurrentUser();
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		List<CustPhone> ls = null;
		
		if(id == null){
			ls = customerService.loadCustPhone(id, isMain);
		}else{
			ls = new ArrayList<CustPhone>();
		}
		map.put("total", ls.size());
		map.put("rows", ls);
		
		return map;
	}
}
