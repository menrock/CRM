package com.niu.crm.web.controller;

import java.io.IOException;
import java.util.HashMap;
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
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.form.StuShareSearchForm;
import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.model.StuShare;
import com.niu.crm.model.StuSingleShare;
import com.niu.crm.model.User;
import com.niu.crm.service.StuShareService;
import com.niu.crm.service.StuSingleShareService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.vo.UserFuncVO;

@Controller
@RequestMapping(value = "/stushare")
public class StuShareController extends BaseController{
	@Autowired
	StuShareService stuShareService;
	@Autowired
	StuSingleShareService singleShareService;
	@Autowired
	UserFuncService userFuncService;
	@Autowired
	UnitService unitService;
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/myList.do")
    public String myList(ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("user", user );
		model.addAttribute("companyList", unitService.getAllCompany() );		
		return "stushare/myList";
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/myListData.do")
    @ResponseBody
    public Map<String, Object> myListData(HttpServletRequest req){
		User user = this.getCurrentUser();
		StuShareSearchForm searchForm = new StuShareSearchForm();
		searchForm.setFromUserId(user.getId());
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<StuShare> ls = stuShareService.queryShare(searchForm, null);
		
		map.put("total", ls.size());
		map.put("rows", ls);
		
		return map;
	}	
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/list.do")
    public String list(ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("user", user );
		model.addAttribute("companyList", unitService.getAllCompany() );
		return "stushare/list";
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/listData.do")
    @ResponseBody
    public Map<String, Object> listData(HttpServletRequest req,StuShareSearchForm searchForm, Pageable pager){
		User user = this.getCurrentUser();
		searchForm.setFromUserId(user.getId());
		Map<String, Object> map = new HashMap<String, Object>();
		List<StuShare> ls = stuShareService.queryShare(searchForm, pager);
		int count = stuShareService.countShare(searchForm);
		
		map.put("total", count);
		map.put("rows", ls);
		
		return map;
	}

	@RequestMapping(value = "/add")
    @ResponseBody
    public ResponseObject<StuShare> add(StuShare stuShare) throws IOException{
		User user = this.getCurrentUser();
		Long fromUserId = stuShare.getFromUserId();
		if(fromUserId.compareTo(user.getId()) !=0 ){
			UserFuncVO adminFunc = userFuncService.loadByCode(user.getId(), "admin");
			if(null == adminFunc)
				throw new BizException(GlobalErrorCode.UNAUTHORIZED,"权限不足");
		}		
		stuShare.setCreatorId(user.getId());
		stuShareService.add(stuShare);
		
		return new ResponseObject<StuShare>(stuShare);
	}
	
	@RequestMapping(value = "/addSingle")
    @ResponseBody
    public ResponseObject<StuSingleShare> addSingle(StuSingleShare stuShare) throws IOException{
		User user = this.getCurrentUser();
		Long fromUserId = stuShare.getFromUserId();
		if(fromUserId.compareTo(user.getId()) !=0 ){
			UserFuncVO adminFunc = userFuncService.loadByCode(user.getId(), "admin");
			if(null == adminFunc)
				throw new BizException(GlobalErrorCode.UNAUTHORIZED,"权限不足");
		}		
		stuShare.setCreatorId(user.getId());
		singleShareService.add(stuShare);
		
		return new ResponseObject<StuSingleShare>(stuShare);
	}
	
	@RequestMapping(value = "/cancel")
    @ResponseBody
    public ResponseObject<Boolean> cancel(Long id) throws IOException{
		User user = this.getCurrentUser();
		StuShare stuShare = stuShareService.load(id);
		if(null == stuShare)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"数据没找到");
		
		Long fromUserId = stuShare.getFromUserId();
		if(fromUserId.compareTo(user.getId()) !=0 ){
			UserFuncVO adminFunc = userFuncService.loadByCode(user.getId(), "admin");
			if(null == adminFunc)
				throw new BizException(GlobalErrorCode.UNAUTHORIZED,"权限不足");
		}		
		stuShareService.delete(id);
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/cancelSingle")
    @ResponseBody
    public ResponseObject<Boolean> cancelSingle(Long id) throws IOException{
		User user = this.getCurrentUser();
		StuSingleShare stuShare = singleShareService.load(id);
		if(null == stuShare)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"数据没找到");
		
		Long fromUserId = stuShare.getFromUserId();
		if(fromUserId.compareTo(user.getId()) !=0 ){
			UserFuncVO adminFunc = userFuncService.loadByCode(user.getId(), "admin");
			if(null == adminFunc)
				throw new BizException(GlobalErrorCode.UNAUTHORIZED,"权限不足");
		}		
		stuShare.setCreatorId(user.getId());
		singleShareService.delete(stuShare.getId());
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
}
