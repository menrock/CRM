package com.niu.crm.web.controller;

import java.util.ArrayList;
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
import com.niu.crm.dto.LxContractDTO;
import com.niu.crm.form.LxContractTranApplySearchForm;
import com.niu.crm.model.Customer;
import com.niu.crm.model.LxContract;
import com.niu.crm.model.LxContractTranApply;
import com.niu.crm.model.User;
import com.niu.crm.model.type.AclScope;
import com.niu.crm.model.type.ApproveStatus;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.LxContractService;
import com.niu.crm.service.LxContractTranApplyService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.LxContractTranApplyVO;
import com.niu.crm.vo.UserFuncVO;

@Controller
@RequestMapping(value = "/lx/contract/tranApply")
public class LxContractTranApplyController extends BaseController {
	@Autowired
	private LxContractTranApplyService tranApplyService;
	@Autowired
	private LxContractService lxContractService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserFuncService userFuncService;
	
	@RequestMapping(value = "/auditList.do")
    public String auditList(ModelMap model){
		User user = this.getCurrentUser();
		
		model.addAttribute("companyList", unitService.getAllCompany() );
		
		return "lx/contract/tranAuditList";
	}
	
	@RequestMapping(value = "/auditListData.do")
	@ResponseBody
    public Map<String, Object> auditListData(HttpServletRequest req, LxContractTranApplySearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		Map<String,Object> map = new HashMap<String,Object>();
		
		form.setApproveQuery(Boolean.TRUE);
		UserFuncVO auditFunc = userFuncService.loadByCode(user.getId(), "tran_audit");
		
		AclScope aclScope = (auditFunc==null?null:auditFunc.getAclScope());
		if(auditFunc == null){
			map.put("total", 0);
			map.put("rows", new ArrayList<LxContractDTO>());
			return map;
		}
		
		StringBuffer buf = new StringBuffer(128);
		pager = convertPager(req, pager);
		if( auditFunc.getAclScope() == AclScope.ALL || aclScope == AclScope.SOMECOMPANY || aclScope == AclScope.SELFCOMPANY){
			
			if( auditFunc.getAclScope() == AclScope.ALL ){
				//不需要增加
			}
			else{
				String companyIds = String.valueOf( user.getCompanyId() );
				if(aclScope == AclScope.SOMECOMPANY)
					companyIds = auditFunc.getCompanyIds();
				buf.append(" and (");
				buf.append("  d.company_id in(" + companyIds +") ");
				buf.append(" )");
			}
			
		}else{
			buf.append(" and (1=2)");
		}

		form.setAclClause( buf.toString() );
		List<LxContractTranApplyVO> ls = null;
		int total = tranApplyService.countApply(form);
		ls = tranApplyService.queryApply(form, pager);
		
		for(LxContractTranApplyVO vo:ls){
			Customer customer = customerService.load(vo.getCstmId());
			
			if(vo.getCreatorId() !=null)
				vo.setCreatorName( userService.getUserName(vo.getCreatorId()) );
			if(vo.getApproverId() !=null)
				vo.setApproverName( userService.getUserName(vo.getApproverId()) );
			vo.setCstmName( customer.getName() );	
			vo.setCompanyName( unitService.getName(vo.getCompanyId()) );
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	@RequestMapping(value = "/newApply.do")
    @ResponseBody
	public ResponseObject<LxContractTranApplyVO> newApply(Long lxConId){
		User user = this.getCurrentUser();
		
		LxContract contract = lxContractService.load(lxConId);
		LxContractTranApplyVO vo = new LxContractTranApplyVO();
		vo.setLxConId(lxConId);
		vo.setStatus(ApproveStatus.DRAFT);
		vo.setCreatorId(user.getId());
		
		vo.setFromApplyGwId(contract.getApplyGwId());
		vo.setToApplyGwId(contract.getApplyGwId());
		
		vo.setFromPlanGwId(contract.getPlanGwId());
		vo.setToPlanGwId(contract.getPlanGwId());
		
		vo.setFromWriteGwId(contract.getWriteGwId());
		vo.setToWriteGwId(contract.getWriteGwId());
		
		vo.setFromServiceGwId(contract.getServiceGwId());
		vo.setToServiceGwId(contract.getServiceGwId());
		
		initGwNames(vo);
		return new ResponseObject<>(vo);
	}
	
	@RequestMapping(value = "/applyInfo.do")
    @ResponseBody
	public ResponseObject<LxContractTranApplyVO> applyInfo(Long id){
		User user = this.getCurrentUser();
		
		LxContractTranApplyVO vo = tranApplyService.load(id);
		Customer customer = customerService.load(vo.getCstmId());		

		if(vo.getCreatorId() !=null)
			vo.setCreatorName( userService.getUserName(vo.getCreatorId()) );
		if(vo.getApproverId() !=null)
			vo.setApproverName( userService.getUserName(vo.getApproverId()) );
		
		vo.setCstmName( customer.getName() );
		initGwNames(vo);		
		return new ResponseObject<>(vo);
	}
	
	@RequestMapping(value = "/save.do")
    @ResponseBody
	public ResponseObject<Boolean> save(LxContractTranApply apply){
		User user = this.getCurrentUser();
		
		if(apply.getId() == null){
			List<ApproveStatus> statusList = new ArrayList<>();
			statusList.add(ApproveStatus.DRAFT);
			statusList.add(ApproveStatus.SUBMIT);
			statusList.add(ApproveStatus.REJECTED);
			
			LxContractTranApplySearchForm form = new LxContractTranApplySearchForm();
			form.setLxConId(apply.getLxConId());
			form.setStatusList(statusList);
			int count = tranApplyService.countApply(form);
			if(count >0)
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "存在非通过的申请");
			
			
			if(apply.getTranType() ==2){
				form.setStatusList(null);
				form.setTranType(2);
			}
			count = tranApplyService.countApply(form);
			if(count >0)
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "转后期申请只允许一次");
		}
		
		LxContract contract = lxContractService.load(apply.getLxConId());
		apply.setFromPlanGwId( contract.getPlanGwId() );
		apply.setFromApplyGwId( contract.getApplyGwId() );
		apply.setFromWriteGwId( contract.getWriteGwId() );
		apply.setFromServiceGwId( contract.getServiceGwId() );
		
		if(apply.getId() == null){
			apply.setStatus(ApproveStatus.DRAFT);
			apply.setCreatorId(user.getId());
			tranApplyService.add(apply);
		}else{
			tranApplyService.update(apply);
		}
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/submit.do")
    @ResponseBody
	public ResponseObject<Boolean> submit(LxContractTranApply apply){
		User user = this.getCurrentUser();
		if(apply.getId() == null){
			apply.setStatus(ApproveStatus.DRAFT);
			apply.setCreatorId(user.getId());
			tranApplyService.add(apply);
		}
			
		tranApplyService.submit(user, apply);
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/approve.do")
    @ResponseBody
	public ResponseObject<Boolean> approve(Long id, Boolean agreeFlag){
		User user = this.getCurrentUser();			
		LxContractTranApply apply = tranApplyService.load(id);
		if(apply.getStatus() == ApproveStatus.PASS || apply.getStatus() == ApproveStatus.REJECTED)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "不能重复审批 ，该申请已经审批");
		else if(apply.getStatus() != ApproveStatus.SUBMIT )
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "不能审批");
		
		if(agreeFlag)
			apply.setStatus(ApproveStatus.PASS);
		else
			apply.setStatus(ApproveStatus.REJECTED);

		tranApplyService.approve(user, apply);
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/listByLxConId.do")
    @ResponseBody
	public Map<String,Object> listByLxConId(Long lxConId){
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		List<LxContractTranApplyVO> ls = tranApplyService.loadByLxConId(lxConId);
		for(LxContractTranApplyVO vo:ls){
			if(vo.getCreatorId() !=null)
				vo.setCreatorName( userService.getUserName(vo.getCreatorId()) );
			if(vo.getApproverId() !=null)
				vo.setApproverName( userService.getUserName(vo.getApproverId()) );
		}
		
		map.put("rows", ls);
		return map;
	}
	
	private void initGwNames(LxContractTranApplyVO vo){
		if(vo.getCreatorId() !=null)
			vo.setCreatorName( userService.getUserName(vo.getCreatorId()) );
		
		if(vo.getFromApplyGwId() !=null)
			vo.setFromApplyGwName( userService.getUserName(vo.getFromApplyGwId()) );
		if(vo.getToApplyGwId() !=null)
			vo.setToApplyGwName( userService.getUserName(vo.getToApplyGwId()) );
		
		if(vo.getFromPlanGwId() !=null)
			vo.setFromPlanGwName( userService.getUserName(vo.getFromPlanGwId()) );
		if(vo.getToPlanGwId() !=null)
			vo.setToPlanGwName( userService.getUserName(vo.getToPlanGwId()) );
		

		if(vo.getFromWriteGwId() !=null)
			vo.setFromWriteGwName( userService.getUserName(vo.getFromWriteGwId()) );
		if(vo.getToWriteGwId() !=null)
			vo.setToWriteGwName( userService.getUserName(vo.getToWriteGwId()) );
		
		if(vo.getFromServiceGwId() !=null)
			vo.setFromServiceGwName( userService.getUserName(vo.getFromServiceGwId()) );
		if(vo.getToServiceGwId() !=null)
			vo.setToServiceGwName( userService.getUserName(vo.getToServiceGwId()) );
	}
}
