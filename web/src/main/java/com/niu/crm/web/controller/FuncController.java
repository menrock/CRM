package com.niu.crm.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.niu.crm.form.UserFuncSearchForm;
import com.niu.crm.model.Func;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.UserFunc;
import com.niu.crm.model.type.AclScope;
import com.niu.crm.service.FuncService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.UserFuncVO;
import com.niu.crm.vo.UserVO;

@Controller
@RequestMapping(value = "/func")
public class FuncController extends BaseController{
	@Autowired
	private FuncService funcService;
	@Autowired
	private UserFuncService userFuncService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/list")
    public String list(){
		return "func/list";
	}
	
	@RequestMapping(value = "/listData")
    @ResponseBody
    public Map<String, Object> listData(HttpServletRequest request, Pageable pager) throws IOException{
		Map<String,Object> map = new java.util.HashMap<String, Object>();		
		List<Func> ls = funcService.list(pager);
		
		map.put("total", ls.size() );
		map.put("rows", ls);
		
		return map;
	}

	@RequestMapping(value = "/users")
    public String users(Long funcId,Model model){
		Func func = funcService.load(funcId);
		model.addAttribute("func", func);
		return "func/users";
	}
	
	@RequestMapping(value = "/usersData")
    @ResponseBody
    public Map<String, Object> usersData(Long funcId) throws IOException{
		UserFuncSearchForm form = new UserFuncSearchForm();
		form.setFuncId(funcId);
		List<UserFuncVO> ls = userFuncService.queryUserFunc(form);
		for(int i=0; ls !=null && i < ls.size(); i++){
			UserFuncVO uf = ls.get(i);
			
			User u = userService.load( uf.getUserId() );
			Unit unit = unitService.load(u.getCompanyId());
			
			UserVO userVO = new UserVO();
			userVO.setName( u.getName() );
			userVO.setAccount( u.getAccount() );
			userVO.setUnitId( u.getUnitId() );
			userVO.setCompanyId( u.getCompanyId() );
			userVO.setCompanyName(unit.getName());
			
			uf.setUser(userVO);
		}
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();		
		map.put("total", ls.size() );
		map.put("rows", ls);
		
		return map;
	}
	
	@RequestMapping(value = "/save")
    @ResponseBody
    public ResponseObject<Func> save(Func func){
		
		if(func.getId() == null)
			funcService.add(func);
		else
			funcService.update(func);
		
		return new ResponseObject<Func>(func);
	}
	
	@RequestMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseObject<Boolean> delete(@PathVariable("id") Long id){
		funcService.delete(id);
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/userFuncSet.do")
	public String funcSet(Long userId, Model model){
		User user = this.getCurrentUser();
		UserFunc myFunc = userFuncService.loadByCode(user.getId(), "useradmin");
		if(myFunc == null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED, "没有此操作权限");

		User u = userService.load(userId);
		AclScope aclScope = myFunc.getAclScope();
		if(aclScope == AclScope.SELFCOMPANY){
			if(user.getCompanyId() != u.getCompanyId())
				throw new BizException(GlobalErrorCode.UNAUTHORIZED, "没有此操作权限");
		}
		List<Func> lsFunc = funcService.list(null);
		List<UserFuncVO> lsUserFunc = userFuncService.loadAll(userId);
		
		model.addAttribute("u", u);
		model.addAttribute("lsFunc", lsFunc);
		model.addAttribute("lsUserFunc", lsUserFunc);
		model.addAttribute("userId", userId);
		model.addAttribute("lsAclScope", funcService.getAllAclScope());
		
		return "func/userFuncSet";	
	}

    @ResponseBody
	@RequestMapping(value = "/userFuncSetData.do")
	public Map<String, Object> funcSetData(Long userId){
		User user = this.getCurrentUser();
		
		UserFunc userFunc = userFuncService.loadByCode(user.getId(), "useradmin");
		if(userFunc == null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED, "没有此操作权限");
		
		AclScope aclScope = userFunc.getAclScope();
		if(aclScope == AclScope.SELFCOMPANY){
			User u = userService.load(userId);
			if(user.getCompanyId() != u.getCompanyId())
				throw new BizException(GlobalErrorCode.UNAUTHORIZED, "没有此操作权限");
		}
		
		List<Func> lsFunc = funcService.list(null);
		List<UserFuncVO> lsUserFunc = userFuncService.loadAll(userId);
		Map<String, UserFunc> mapUserFunc = new HashMap<String, UserFunc>();
		for(UserFunc uf:lsUserFunc){
			mapUserFunc.put(uf.getFuncCode(),uf);
		}
		
		
		List<Map<String,Object>> ls = new ArrayList<Map<String, Object>>();
		for(Func func:lsFunc){
			Map<String,Object> row = new HashMap<String, Object>();
			UserFunc uf = mapUserFunc.get(func.getCode());
						
			row.put("code", func.getCode());
			row.put("name", func.getName());
			
			String[] arrScopes = func.getAclScopes().split(",");
			List<Map<String,Object>> lsScope = new ArrayList<Map<String,Object>>();
			for(int i=0; i < arrScopes.length; i++){
				AclScope obj = AclScope.valueOf(arrScopes[i]);
				Map<String,Object> mapScope = new HashMap<String, Object>();
				mapScope.put("value", obj.name() );
				mapScope.put("name", obj.getName() );
				
				if(uf !=null && uf.getAclScope() == obj)
					mapScope.put("selected", Boolean.TRUE);
				else
					mapScope.put("selected", Boolean.FALSE);
				
				lsScope.add(mapScope);
			}			
			row.put("aclScopes",  lsScope);
						
			ls.add(row);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("total", ls.size());
		map.put("rows", ls);
		
		return map;	
	}
	
	@RequestMapping(value = "/doUserFuncSet.do")
    @ResponseBody
	public ResponseObject<Boolean> doFuncSet(HttpServletRequest request, Long userId){
		User user = this.getCurrentUser();
		
		UserFunc myFunc = userFuncService.loadByCode(user.getId(), "useradmin");
		if( myFunc == null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED, "没有此操作权限");
		
		String[] funcCode   = request.getParameterValues("funcCode");
		String[] aclScope   = request.getParameterValues("aclScope");
		String[] companyIds = request.getParameterValues("companyIds");
		String[] fromIds = request.getParameterValues("fromIds");
		String[] clauses = request.getParameterValues("clause");
		
		List<UserFunc> lsUserFunc = new java.util.ArrayList<UserFunc>();
		for(int i=0; funcCode !=null && i < funcCode.length; i++){
			UserFunc userFunc = new UserFunc();

			Func func = funcService.loadByCode(funcCode[i]);
			
			userFunc.setFuncId(func.getId());
			userFunc.setFuncCode(funcCode[i]);
			userFunc.setAclScope( AclScope.valueOf(aclScope[i]) );
			userFunc.setClause(clauses[i]);
			AclScope ufAclScope = userFunc.getAclScope();
			
			if( ufAclScope == AclScope.SOMEUNIT )
				userFunc.setUnitIds(companyIds[i]);
			else if( ufAclScope == AclScope.SOMECOMPANY)
				userFunc.setCompanyIds(companyIds[i]);
			
			userFunc.setFromIds(fromIds[i]);
			userFunc.setUserId(userId);
			lsUserFunc.add(userFunc);
		}
		
		userFuncService.setUserFunc(user, userId, lsUserFunc);
	
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	public static void main(String... args){
		System.out.println("hello");
	}
}
