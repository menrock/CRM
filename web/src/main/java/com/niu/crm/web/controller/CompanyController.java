package com.niu.crm.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.model.Unit;
import com.niu.crm.service.UnitService;

@Controller
@RequestMapping(value = "/company")
public class CompanyController extends BaseController{
	@Autowired
	UnitService unitService;
	
	
	@RequestMapping(value = "/listData")
    @ResponseBody
    public ResponseObject<List<CompanyVO>> listData(){
		List<Unit> ls = unitService.getAllCompany();
		List<CompanyVO> lsCompany = new ArrayList<>(ls.size());
		for(Unit unit:ls){
			CompanyVO company = new CompanyVO();
			company.setId(unit.getId());
			company.setCode(unit.getCode());
			company.setAlias(unit.getAlias());
			company.setName(unit.getName());
			lsCompany.add(company);
		}
		
		return new ResponseObject<List<CompanyVO>>(lsCompany);
	}
	
	public static void main(String... args){
		Date d = new Date();
		
		d.setTime(1468166400000L);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		System.out.println("sdf=" + sdf.format(d));
		System.out.println("hello");
	}
	
	public static class CompanyVO{
		private Long id;
		private String alias;
		private String code;
		private String name;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		
		public String getAlias() {
			return alias;
		}
		public void setAlias(String alias) {
			this.alias = alias;
		}
		
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
}
