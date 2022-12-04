package com.niu.crm.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niu.crm.model.Customer;
import com.niu.crm.model.User;
import com.niu.crm.model.UserFunc;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.vo.UserFuncVO;

@Controller
public class WorkshopController extends BaseController{
	@Autowired
	private CustomerService customerService;
	@Autowired
	private UserFuncService userFuncService;
	
	@RequestMapping({"/","/workshop"})
    public String workshop(HttpServletRequest request, ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("user", user);
		
		List<UserFuncVO> lsFunc = userFuncService.loadAll(user.getId());
		model.addAttribute("lsFunc", lsFunc);
		
		return "workshop";
	}
	
	@RequestMapping({"/workshop2"})
    public String workshop2(HttpServletRequest request, ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("user", user);
		
		//List<UserFuncVO> lsFunc = userFuncService.loadAll(user.getId());
		//model.addAttribute("lsFunc", lsFunc);
		
		return "workshop2";
	}
	
	@RequestMapping(value = "/navigatorTree.do")
    public String navigator(HttpServletRequest request, ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("user", user);
		
		List<UserFuncVO> lsFunc = userFuncService.loadAll(user.getId());
		model.addAttribute("lsFunc", lsFunc);
		
		return "navigatorTree";
	}
	
	@RequestMapping(value = "/top.do")
    public String top(HttpServletRequest request, ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("user", user);
		
		return "top";
	}
}
