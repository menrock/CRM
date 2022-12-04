package com.niu.crm.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.alibaba.fastjson.JSONArray;
import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.model.CoopOrg;
import com.niu.crm.model.User;
import com.niu.crm.service.CoopOrgService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.vo.UserFuncVO;

@Controller
@RequestMapping(value = "/coopOrg")
public class CoopOrgController extends BaseController{
	@Autowired
	private CoopOrgService coopOrgService;
	
	@Autowired
	private UserFuncService userFuncService;
	
	
	@RequestMapping(value = "/listPage.do")
    public String list(HttpServletResponse response) throws IOException{
		return "coop-org/list";
	}
	
	@RequestMapping(value = "/listData.do")
    @ResponseBody
    public Map<String, Object> listData(String keywords){
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		Integer count = coopOrgService.queryCount(keywords);
		List<CoopOrg> list = coopOrgService.queryOrg(keywords);
		
		map.put("total", count);
		map.put("rows", list);
		
		return map;
	}
	
	@RequestMapping(value = "/save.do")
    @ResponseBody
    public ResponseObject<CoopOrg> save(CoopOrg org){
		User user = this.getCurrentUser();
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "admin");
		if( func == null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED,"权限不够");
		
		if(org.getId() == null)
			coopOrgService.add(user, org);
		else
			coopOrgService.update(user, org);
		
		return new ResponseObject<CoopOrg>(org);
	}
	
	@RequestMapping(value = "/delete.do")
    @ResponseBody
    public ResponseObject<Boolean> delete(Long id){
		User user = this.getCurrentUser();
		
		coopOrgService.delete(user, id);		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/orgInfo")
    @ResponseBody
    public ResponseObject<CoopOrg> info(Long id){
		User user = this.getCurrentUser();
		
		CoopOrg org = coopOrgService.load(id);		
		return new ResponseObject<CoopOrg>(org);
	}
	
	public static void main(String... args){
		System.out.println("hello");
	}
}
