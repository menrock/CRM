package com.niu.crm.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.form.UserFuncSearchForm;
import com.niu.crm.form.UserSearchForm;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.UserFunc;
import com.niu.crm.model.type.AclScope;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.UserFuncVO;
import com.niu.crm.vo.UserVO;

@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController{
	@Autowired
	UserService userService;
	@Autowired
	UnitService unitService;
	@Autowired
	UserFuncService userFuncService;
	
	@RequestMapping(value = "/list.do")
    public String list(ModelMap model){
		User user = this.getCurrentUser();
		
		List<Unit> companyList = null;
		UserFunc queryFunc = userFuncService.loadByCode(user.getId(), "useradmin");
		
		if( queryFunc == null){
			return "/workshop";
		}
		if( queryFunc.getAclScope() == AclScope.ALL){
			companyList = unitService.getAllCompany();
		}
		else {
			companyList = new java.util.ArrayList<Unit>();
			Unit company = unitService.load(user.getCompanyId());
			companyList.add(company);
		}
		
		model.addAttribute("companyList", companyList);
		model.addAttribute("user", user);
		
		return "user/list";
	}
	
	@RequestMapping(value = "/listData.do")
    @ResponseBody
	public Map<String, Object> listData(UserSearchForm form, Pageable pager){
		User user = this.getCurrentUser(); 
	
		if(form.getCompanyId() != null || form.getUnitId() != null){
			Long unitId = null;
			if( form.getUnitId() !=null)
				unitId = form.getUnitId();
			else
				unitId = form.getCompanyId();
			
			Unit unit = unitService.load(unitId);
			form.setCompanyId(null);
			form.setUnitId(null);
			form.setUnitCode(unit.getCode());
		}
		
		if(StringUtils.isNotEmpty(form.getPhone()))
			form.setPhone(form.getPhone().toUpperCase().trim());
		
		if( StringUtils.isNotEmpty(form.getName()) ){
			String szName = form.getName().trim();
			if(!szName.endsWith("%"))
				form.setName(szName + "%");
		}
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();
	
		int total = userService.countUser(form);
		List<UserVO> ls = userService.queryUser(form, pager);
		
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	@RequestMapping(value = "/online.do")
    @ResponseBody
	public ResponseObject<Boolean> online(){
		User user = this.getCurrentUser(); 
		userService.setOnline(user.getId());
		user.setOnline(true);
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/offline.do")
    @ResponseBody
	public ResponseObject<Boolean> offline(){
		User user = this.getCurrentUser(); 
		userService.setOffline(user.getId());
		user.setOnline(false);
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/save.do")
    @ResponseBody
	public ResponseObject<User> save(User editUser){
		editUser.setAccount( editUser.getAccount().trim() );
		
		User user = this.getCurrentUser();
		if(editUser.getId() == null)
			editUser.setCreatorId(user.getId());
		
		//检查账号是否冲突
		{
			User u = userService.loadByAccount(editUser.getAccount());
			if(u !=null){
				if( editUser.getId() == null)
					throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "账号冲突");
				else if( u.getId().longValue() != editUser.getId().longValue()){
					throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "账号冲突");
				}
			}			
		}
		
		if(editUser.getId() == null)
			userService.add(editUser);
		else
			userService.update(editUser);
		
		return new ResponseObject<User>(editUser);
	}
	
	@RequestMapping(value = "/changePwd.do")
	public String changePwd(ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("user", user);
		
		return "user/changePwd";
	}
	
	@RequestMapping(value = "/doChangePwd.do")
    @ResponseBody
	public ResponseObject<Boolean> doChangePwd(String account, String oldPasswd, String newPasswd){
		User user = this.getCurrentUser();
		User u = userService.loadByAccount(account);
				
		Boolean canChange = Boolean.TRUE;
		if( u == null){
			canChange = Boolean.FALSE;
		}
		else if( !user.getId().equals(u.getId()) ){
			//修改他人密码
			UserFunc queryFunc = userFuncService.loadByCode(user.getId(), "useradmin");
			if( queryFunc ==null){
				canChange = Boolean.FALSE;
			}
			else if(queryFunc.getAclScope() == AclScope.ALL){
				
			}
			else if(queryFunc.getAclScope() == AclScope.SELFCOMPANY){
				if( u.getCompanyId() != user.getCompanyId() )
					canChange = Boolean.FALSE;
			}	
			else {
				canChange = Boolean.FALSE;
			}
		}
	
		if(canChange){
			userService.changePwd(u.getId(), newPasswd);
			return new ResponseObject<Boolean>(Boolean.TRUE);
		}else{
			return new ResponseObject<Boolean>(Boolean.FALSE);
		}
	}
}
