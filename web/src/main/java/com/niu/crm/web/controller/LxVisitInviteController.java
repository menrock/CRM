package com.niu.crm.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.niu.crm.form.VisitInviteSearchForm;
import com.niu.crm.model.Customer;
import com.niu.crm.model.Dict;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.LxVisitInvite;
import com.niu.crm.model.User;
import com.niu.crm.model.UserFunc;
import com.niu.crm.model.type.AclScope;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.LxCustomerService;
import com.niu.crm.service.LxVisitInviteService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.LxVisitInviteVO;
import com.niu.crm.vo.UserFuncVO;


@Controller
@RequestMapping(value = "/lx/visitInvite")
public class LxVisitInviteController extends BaseController {
	@Autowired
	private LxVisitInviteService visitInviteService;
	@Autowired
	private LxCustomerService lxCustomerService;
	@Autowired
	private CustomerService customerService;

	@Autowired
	private UnitService unitService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserFuncService userFuncService;
	
	@RequestMapping(value = "/list.do")
    public String list(ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("companyList", unitService.getAllCompany() );
		return "lx/visitInviteList";
	}
	
	@RequestMapping(value = "/listData.do")
    @ResponseBody
    public Map<String, Object> listData(HttpServletRequest req, VisitInviteSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		Map<String,Object> map = new HashMap<String,Object>();
		pager = convertPager(req, pager);
		UserFuncVO queryFunc = userFuncService.loadByCode(user.getId(), "lx_invite_visit_manager");
		if( queryFunc == null){
			map.put("rows", new ArrayList<LxVisitInviteVO>() );
			map.put("total", 0);
			
			return map;
		}
		
		if(StringUtils.isEmpty(form.getCstmName())){
			form.setCstmName(null);
		}else{
			form.setCstmName( "%" + form.getCstmName() + "%" );
		}
		
		if(StringUtils.isEmpty(form.getGwName())){
			form.setGwName(null);
		}else{
			form.setGwName( "%" + form.getGwName() + "%" );
		}

		if(form.getAppointmentTo() !=null)
			form.setAppointmentTo( toDateLastMillis(form.getAppointmentTo()) );
		if(form.getVisitTo() !=null)
			form.setVisitTo( toDateLastMillis(form.getVisitTo()) );
		
		
		/***/
		
		if( queryFunc.getAclScope() == AclScope.ALL){
		}
		else if( queryFunc.getAclScope() == AclScope.SOMECOMPANY){
			form.setAclClause(" and b.company_id in(" + queryFunc.getCompanyIds() +") ");
		}	
		else if( queryFunc.getAclScope() == AclScope.SELFCOMPANY){
			form.setAclClause(" and b.company_id =" + user.getCompanyId() );
		}else{
			form.setAclClause(" and 1=2 ");
		}
		
		/***/
					
		
		List<LxVisitInviteVO> ls = visitInviteService.queryInvite(form, pager);
		int count = visitInviteService.countInvite(form);
		
		for(LxVisitInviteVO vo:ls){
			vo.setCompanyName(unitService.getName(vo.getCompanyId()));
			vo.setGwName(userService.getUserName(vo.getGwId()));
		}
		
		map.put("rows", ls);
		map.put("total", count);
		
		return map;
	}
	
	@RequestMapping(value = "/stuList.do")
    public String stuList(ModelMap model, Long stuId){
		User user = this.getCurrentUser();
		
		LxCustomer stu = lxCustomerService.load(stuId);
		Customer cust = customerService.load(stu.getCstmId());
		
		model.addAttribute("stuId", stuId );
		model.addAttribute("cust", cust );
		model.addAttribute("user", user );
		
		return "lx/stuVisitInviteList";
	}
	
	@RequestMapping(value = "/stuListData.do")
    @ResponseBody
    public Map<String, Object> stuListData(HttpServletRequest req, Long stuId){
		User user = this.getCurrentUser();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		
		List<LxVisitInviteVO> ls = visitInviteService.loadByStuId(stuId);
		int count = ls.size();
		
		for(LxVisitInviteVO vo:ls){
			vo.setGwName(userService.getUserName(vo.getGwId()));
		}
		
		map.put("rows", ls);
		map.put("total", count);
		
		return map;
	}
	
	@RequestMapping(value = "/saveInvite.do")
    @ResponseBody
    public ResponseObject<LxVisitInvite> saveInvite(HttpServletRequest req, LxVisitInvite invite){
		String szApptTime = req.getParameter("appointmentTime");
		try{
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
			invite.setAppointmentTime(sdf.parse(szApptTime));
		}catch(Exception e){
			getLogger().error("szApptTime=" + szApptTime, e);
		}
		
		User user = this.getCurrentUser();
		invite.setCreatorId(user.getId());
		if(invite.getGwId() == null)
			invite.setGwId(user.getId());
		
		if(invite.getId()==null)
			visitInviteService.add(invite);
		else
			visitInviteService.updateInvite(invite);
		
		return new ResponseObject<>(invite);
	}
	
	@RequestMapping(value = "/saveVisit.do")
    @ResponseBody
    public ResponseObject<LxVisitInvite> saveVisit(HttpServletRequest req, LxVisitInvite visit){
		User user = this.getCurrentUser();
		
		String szVisitTime = req.getParameter("visitTime");
		try{
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
			visit.setVisitTime(sdf.parse(szVisitTime));
		}catch(Exception e){
			getLogger().error("szVisitTime=" + szVisitTime, e);
		}
		
		visit.setVisitCreator(user.getId());
		
		visitInviteService.updateVisit(visit);
		return new ResponseObject<>(visit);
	}
	
	@RequestMapping(value = "/delete.do")
    @ResponseBody
    public ResponseObject<Boolean> delete(Long id){
		User user = this.getCurrentUser();
		Long userId = user.getId();
		LxVisitInvite visit = visitInviteService.load(id);
		if(visit == null)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "邀约信息不存在");
			
		
		UserFunc uf = userFuncService.loadByCode(userId, "admin");
		if( uf == null && !visit.getCreatorId().equals(user.getId()) )
			throw new BizException(GlobalErrorCode.UNAUTHORIZED, "录入人自己才能删除");
		
		if(visit.getVisitTime() !=null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED, "已到访信息 不能删除");
		
		visitInviteService.delete(id);
		return new ResponseObject<>(Boolean.TRUE);
	}
}
