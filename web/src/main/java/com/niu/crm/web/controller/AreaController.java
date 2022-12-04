package com.niu.crm.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.model.Area;
import com.niu.crm.service.AreaService;

@Controller
@RequestMapping(value = "/area")
public class AreaController extends BaseController{
	@Autowired
	AreaService areaService;
	
	@RequestMapping(value = "/refrechCache")
    @ResponseBody
    public ResponseObject<Boolean> refrechCache(){
		areaService.refrechCache();		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/{id}/children")
    @ResponseBody
    public ResponseObject<List<AreaVO>> children(@PathVariable("id") Long id){
		List<Area> ls = areaService.loadChildren(id);
		List<AreaVO> areaList = new ArrayList<AreaVO>(ls.size());
		for(Area item:ls){
			AreaVO vo = new AreaVO();
			areaList.add(vo);
			
			BeanUtils.copyProperties(item, vo);
		}
		
		return new ResponseObject<List<AreaVO>>(areaList);
	}
	
	public static class AreaVO{
		private Long id;
		private String abbr;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		
		public String getAbbr() {
			return abbr;
		}
		public void setAbbr(String abbr) {
			this.abbr = abbr;
		}
	}
}
