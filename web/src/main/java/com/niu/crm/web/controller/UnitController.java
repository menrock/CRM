package com.niu.crm.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.alibaba.fastjson.JSONArray;
import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.vo.UserFuncVO;

@Controller
@RequestMapping(value = "/unit")
public class UnitController extends BaseController{
	@Autowired
	private UnitService unitService;
	
	@Autowired
	private UserFuncService userFuncService;
	
	@RequestMapping(value = "/refrechCache")
    @ResponseBody
    public ResponseObject<Boolean> refrechCache(){
		unitService.refrechCache();		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/frame")
    public String unitFrame(){
		return "unit/unitFrame";
	}
	
	@RequestMapping(value = "/tree")
    public String unitTree(Model model){
		return "unit/unitTree";
	}
	
	@RequestMapping(value = "/treeData")
    public void treeData(String rootCode, Long rootId, HttpServletResponse response) throws IOException{
		response.setContentType("text/json");
		response.setCharacterEncoding("gb2312");
		
		if(null != rootId){
			rootCode = unitService.load(rootId).getCode();
		}		
		
		JSONArray tree = unitService.getJSONTree(rootCode);
		String szTxt = tree.toString();
		response.getWriter().write( szTxt );
	}
	
	@RequestMapping(value = "/edit")
    public String unitEdit(Long id, Model model){
		Unit unit = unitService.load(id);
		model.addAttribute("unit", unit);
		
		if (unit.getParentId() != null) {
			Unit pUnit = unitService.load(unit.getParentId());
			model.addAttribute("pUnit", pUnit);
		}
		
		return "unit/unitDetail";
	}
	
	@RequestMapping(value = "/create")
    public String unitCreate(Long parentId, Model model){
		
		Unit pUnit = unitService.load(parentId);
		model.addAttribute("pUnit", pUnit);
		
		return "unit/unitDetail";
	}
	
	@RequestMapping(value = "/{id}")
    @ResponseBody
    public ResponseObject<Unit> getUnit(@PathVariable("id") Long id){
		Unit unit = unitService.load(id);
		if(unit == null)
			throw new BizException(GlobalErrorCode.UNIT_NOT_EXIST, "部门没找到");
		
		return new ResponseObject<Unit>(unit);
	}
	
	@RequestMapping(value = "/save")
    @ResponseBody
    public ResponseObject<Unit> save(Unit unit){
		User user = this.getCurrentUser();
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "admin");
		if( func == null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED,"权限不够");
		
		//部门代码检查
		if(StringUtils.isEmpty(unit.getAlias()))
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"部门代码不能为空");
		if( !Pattern.matches("^[a-zA-z][a-zA-Z0-9-_]{1,14}$", unit.getAlias())){
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"部门代码只能由字母数字及-_组成,且必须由字母开头");
		}
		
		if(StringUtils.isEmpty(unit.getName()))
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"部门名称不能为空");
		
		if(unit.getParentId() !=null){
			Unit pUnit = unitService.load(unit.getParentId());
			unit.setCode(pUnit.getCode() + "." + unit.getAlias() );
		}
		
		if(unit.getId() == null)
			unitService.add(unit);
		else
			unitService.update(unit);
		
		return new ResponseObject<Unit>(unit);
	}
	
	@RequestMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseObject<Boolean> delete(@PathVariable("id") Long id){
		User user = this.getCurrentUser();
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "admin");
		if( func == null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED,"权限不够");
		
		unitService.delete(id);
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/moveTo")
    @ResponseBody
    public ResponseObject<Boolean> moveTo(@RequestParam("fromUnitId")Long fromUnitId, @RequestParam("toUnitId")Long toUnitId){
		User user = this.getCurrentUser();
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "admin");
		if( func == null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED,"权限不够");
		
		unitService.moveTo(fromUnitId, toUnitId);
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	public static void main(String... args){
		Date d = new Date();
		
		d.setTime(1468166400000L);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		System.out.println("sdf=" + sdf.format(d));
		System.out.println("hello");
	}
}
