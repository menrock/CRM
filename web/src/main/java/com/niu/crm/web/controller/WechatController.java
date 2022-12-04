package com.niu.crm.web.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.form.UserSearchForm;
import com.niu.crm.model.SmsMessage;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.UserFunc;
import com.niu.crm.model.type.AclScope;
import com.niu.crm.service.SmsService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.WechatService;
import com.niu.crm.util.WechatUtil;
import com.niu.crm.vo.UserVO;

@Controller
@RequestMapping(value = "/wechat")
public class WechatController extends BaseController{
	@Autowired
	private UserService userService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private WechatService wechatService;
	@Autowired
	private SmsService smsService;
	
	@RequestMapping(value = "/getUser.do")
    @ResponseBody
    public ResponseObject<JSONObject> getUser(Long id){
		User user = this.getCurrentUser();
		JSONObject json = null;
		try{
			json = wechatService.getUser(id);
		}
		catch(Exception e){
			logger.error("", e);
			throw new BizException(GlobalErrorCode.INTERNAL_ERROR, e.getMessage());
		}
		
		return new ResponseObject<JSONObject>(json);
	}
	
	@RequestMapping(value = "/createUser.do")
    @ResponseBody
    public ResponseObject<JSONObject> createUser(Long id){
		User user = this.getCurrentUser();
		JSONObject json = null;
		
		try{
			json = wechatService.createUser(id);
		}
		catch(Exception e){
			logger.error("", e);
		}
		
		return new ResponseObject<JSONObject>(json);
	}
	
	@RequestMapping(value = "/createUnit.do")
    @ResponseBody
    public ResponseObject<Boolean> createUnit(Long id){
		User user = this.getCurrentUser();
		
		try{
			Unit unit = unitService.load(id);
			wechatService.createUnit(unit);
		}
		catch(Exception e){
			logger.error("", e);
			return new ResponseObject<Boolean>(Boolean.FALSE);
		}
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/department/list.do")
    @ResponseBody
    public ResponseObject<JSONObject> listDepartment(@RequestParam(value="unitId",required =false)Long unitId, @RequestParam(value="wxUnitId",required =false)Long wxUnitId){
		User user = this.getCurrentUser();
		JSONObject json = null;
		
		try{
			json = wechatService.listDepartment(unitId, wxUnitId);
			//invalid access_token
			if(json.getIntValue("errcode") == 40014){
				WechatUtil.getInstance().createAccess_token();
				json = wechatService.listDepartment(unitId, wxUnitId);
			}
		}
		catch(Exception e){
			logger.error("", e);
		}
		
		return new ResponseObject<JSONObject>(json);
	}
	
	@RequestMapping(value = "/user/list.do")
    @ResponseBody
    public ResponseObject<JSONObject> listUser(Long unitId, Boolean fetchChild,Integer status){
		User user = this.getCurrentUser();
		JSONObject json = null;
		
		try{
			json = wechatService.listUser(unitId, fetchChild, status);
			//invalid access_token
			if(json.getIntValue("errcode") == 40014){
				WechatUtil.getInstance().createAccess_token();
				json = wechatService.listUser(unitId, fetchChild, status);
			}
		}
		catch(Exception e){
			logger.error("", e);
		}
		
		return new ResponseObject<JSONObject>(json);
	}
	
	@RequestMapping(value = "/message/send")
    @ResponseBody
    public ResponseObject<JSONObject> sendTxtMessage(String msg, Long userId){
		User user = this.getCurrentUser();
		JSONObject json = null;
		
		try{
			json = wechatService.sendTxtMessage(msg, null, new Long[]{userId});
		}
		catch(Exception e){
			logger.error("", e);
		}
		
		return new ResponseObject<JSONObject>(json);
	}
}
