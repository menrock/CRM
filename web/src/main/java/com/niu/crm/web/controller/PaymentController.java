package com.niu.crm.web.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.form.ContractSearchForm;
import com.niu.crm.form.PaymentSearchForm;
import com.niu.crm.form.SkSearchForm;
import com.niu.crm.model.CustContract;
import com.niu.crm.model.Customer;
import com.niu.crm.model.Dict;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.Payment;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.type.AclScope;
import com.niu.crm.model.type.PaymentStatus;
import com.niu.crm.service.CustContractService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.LxCustomerService;
import com.niu.crm.service.PaymentService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.util.UniqueNoUtils;
import com.niu.crm.vo.PaymentLineVO;
import com.niu.crm.vo.PaymentVO;
import com.niu.crm.vo.UserFuncVO;

@Controller
@RequestMapping(value = "/payment")
public class PaymentController extends BaseController{
	@Autowired
	DictService dictService;	
	@Autowired
	CustContractService contractService;
	@Autowired
	LxCustomerService lxCustomerService;
	@Autowired
	CustomerService customerService;
	@Autowired
	PaymentService paymentService;
	@Autowired
	UserService userService;
	@Autowired
	UserFuncService userFuncService;
	@Autowired
	UnitService unitService;
	
	@RequestMapping(value = "/stuPayments.do")
    public String stuPayments(ModelMap model, Long stuId){
		LxCustomer student = lxCustomerService.load(stuId);
		Customer customer = customerService.load(student.getCstmId());
		
		model.addAttribute("cstmName",customer.getName());
		model.addAttribute("student",student);
		{
			List<Dict> lsFeeItem = new ArrayList<Dict>();
			List<Dict> feeItems = dictService.loadChildren("feeitem");
			for(Dict item:feeItems){
				//退费 不再允许录入
				if(item.getId() == 488)
					continue;
				
				List<Dict> lsChild = dictService.loadChildren(item.getId());
				for(Dict a:lsChild)
					a.setDictName( item.getDictName() + "-" + a.getDictName());
				
				if(lsChild.size() ==0)
					lsFeeItem.add(item);
				else
					lsFeeItem.addAll(lsChild);
			}
			model.addAttribute("feeItemList",  lsFeeItem);
		}
		
		model.addAttribute("paytypeList", dictService.loadChildren("paytype") );
		model.addAttribute("paybankList", dictService.loadChildren("paybank") );
		return "payment/stuPayments";
	}
	
	@RequestMapping(value = "/payments.do")
    public String payments(ModelMap model){

		List<Unit> companyList = new ArrayList<>();
		List<Unit> lsCompany = unitService.getAllCompany();
		for(Unit unit:lsCompany){
			String code = unit.getCode();
			if(code.equals("zjb") || code.equals("cwb"))
				continue;
			companyList.add(unit);
		}
		model.addAttribute("companyList", unitService.getAllCompany() );
		
		model.addAttribute("feeItemList", dictService.loadChildren("feeitem") );
		model.addAttribute("paytypeList", dictService.loadChildren("paytype") );
		model.addAttribute("paybankList", dictService.loadChildren("paybank") );
		
		return "payment/payments";
	}

	@RequestMapping(value = "/paymentsData.do")
	@ResponseBody
    public Map<String, Object> paymentsData(HttpServletRequest req, PaymentSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		if(StringUtils.isNotBlank(form.getPayType())){
			form.setPayType("%" + form.getPayType() + "%");
		}
		
		if(form.getFuzzySearch() !=null && form.getFuzzySearch() ){
			if( StringUtils.isNotBlank(form.getCreatorName()) )
				form.setCreatorName("%" + form.getCreatorName().trim() + "%");
		}
		
		Map<String,Object> map = new HashMap<String,Object>();	
		this.setListClause(user, null, form);
		
		int total = paymentService.queryCount(form);
		List<PaymentVO> ls = paymentService.query(form, pager);
		for(PaymentVO vo:ls){
			processWhenList(vo);
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	private void setListClause(User user, String funcCode, PaymentSearchForm form){
		UserFuncVO queryFunc = null;
		
		if(StringUtils.isEmpty(funcCode) || funcCode.equals("sk_fz_confirm"))
			queryFunc = userFuncService.loadByCode(user.getId(), "sk_fz_confirm");
		if( queryFunc == null){
			if(StringUtils.isEmpty(funcCode) || funcCode.equals("sk_confirm"))
				queryFunc = userFuncService.loadByCode(user.getId(), "sk_confirm");
		}
		
		AclScope aclScope = (queryFunc==null?null:queryFunc.getAclScope());
		if(queryFunc == null){
			form.setAclClause(" and (1=2)" );
		}
		else if( queryFunc.getAclScope() == AclScope.ALL){
			//不需要增加
		}else if( aclScope == AclScope.SOMECOMPANY || aclScope == AclScope.SELFCOMPANY){
			String companyIds = String.valueOf( user.getCompanyId() );
			if(aclScope == AclScope.SOMECOMPANY)
				companyIds = queryFunc.getCompanyIds();
			
			StringBuffer buf = new StringBuffer(64);
			buf.append(" and p.company_id in(" + companyIds +") ");
			form.setAclClause( buf.toString() );
		}else{
			form.setAclClause(" and (1=2)");
		}
	}
	
	@RequestMapping(value = "/stuPaymentData.do")
	@ResponseBody
    public Map<String, Object> stuPaymentData(HttpServletRequest req, @RequestParam("cstmId")Long cstmId){
		User user = this.getCurrentUser();
		
		Pageable pager = new PageRequest(0, 50000);
		PaymentSearchForm form = new PaymentSearchForm();
		form.setCstmId(cstmId);
		
		Map<String,Object> map = new HashMap<String,Object>();	
		
		List<PaymentVO> ls = paymentService.query(form, pager);
		for(PaymentVO vo:ls){
			
		}
		
		map.put("total", ls.size());
		map.put("rows", ls);
		
		return map;
	}
	
	@RequestMapping(value = "/payments4FzConfirm.do")
    public String payments4FzConfirm(ModelMap model){
		return payments4Confirm(model, "FZ_CONFIRM");
	}
	
	@RequestMapping(value = "/payments4FinanceConfirm.do")
    public String payments4FinanceConfirm(ModelMap model){
		return payments4Confirm(model, "CONFIRM");		
	}
	
	/**
	 * @param model
	 * @param confirmNode 确认环节 (FZ_CONFIRM/CONFIRM)
	 * @return
	 */
	private String payments4Confirm(ModelMap model, String confirmNode){
		User user = this.getCurrentUser();
		
		List<Unit> companyList = new ArrayList<>();
		
		if( confirmNode.equals("FZ_CONFIRM") ){
			UserFuncVO fzConfirmFunc = userFuncService.loadByCode(user.getId(),
					"sk_fz_confirm");
			if(fzConfirmFunc.getAclScope() == AclScope.SELFCOMPANY){
				Unit company = unitService.load(user.getCompanyId());
				companyList.add(company);
			}
				
		}else{
			UserFuncVO confirmFunc = userFuncService.loadByCode(user.getId(),
					"sk_confirm");
			if(confirmFunc.getAclScope() == AclScope.SELFCOMPANY){
				Unit company = unitService.load(user.getCompanyId());
				companyList.add(company);
			}
		}
		if(companyList.size() ==0){
			List<Unit> lsCompany = unitService.getAllCompany();
			for(Unit unit:lsCompany){
				String code = unit.getCode();
				if(code.equals("zjb") || code.equals("cwb"))
					continue;
				companyList.add(unit);
			}
		}
		
		model.addAttribute("confirmNode", confirmNode);
		model.addAttribute("companyList", companyList );
		model.addAttribute("feeItemList", dictService.loadChildren("feeitem") );
		model.addAttribute("paytypeList", dictService.loadChildren("paytype") );
		model.addAttribute("paybankList", dictService.loadChildren("paybank") );
		
		return "payment/payments4Confirm";
	}
	
	@RequestMapping(value = "/payments4ConfirmData.do")
	@ResponseBody
    public Map<String, Object> payments4ConfirmData(HttpServletRequest req, PaymentSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		String confirmNode = req.getParameter("confirmNode");
		
		pager = convertPager(req, pager);
		if(StringUtils.isNotBlank(form.getPayType())){
			form.setPayType("%" + form.getPayType() + "%");
		}
		
		if(form.getStatusList() == null)
			form.setStatusList( new ArrayList<PaymentStatus>() );
		
		List<PaymentStatus> statusList = form.getStatusList();
		if (statusList.size() == 0) {
			UserFuncVO fzConfirmFunc = userFuncService.loadByCode(user.getId(),
					"sk_fz_confirm");
			UserFuncVO confirmFunc = userFuncService.loadByCode(user.getId(),
					"sk_confirm");
			
			if (fzConfirmFunc != null) {
				statusList.add(PaymentStatus.SUBMIT);
				statusList.add(PaymentStatus.FZ_CONFIRMED);
				statusList.add(PaymentStatus.CONFIRMED);
			}else if ( confirmFunc != null) {
				statusList.add(PaymentStatus.FZ_CONFIRMED);
				statusList.add(PaymentStatus.CONFIRMED);
			}
		}
		if( "FZ_CONFIRM".equals(confirmNode))
			this.setListClause(user, "sk_fz_confirm", form);
		else
			this.setListClause(user, "sk_confirm", form);
			
		
		Map<String,Object> map = new HashMap<String,Object>();	
		
		int total = paymentService.queryCount(form);
		List<PaymentVO> ls = paymentService.query(form, pager);
		for(PaymentVO vo:ls){
			processWhenList(vo);
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	@RequestMapping(value = "/add.do")
    public String add(HttpServletRequest req){
		User user = this.getCurrentUser();
		return "payment/detail";
	}
	
	@RequestMapping(value = "/delete.do")
	@ResponseBody
    public ResponseObject<Integer> delete(Long[] ids){
		User user = this.getCurrentUser();
		long userId = user.getId().longValue();
		UserFuncVO adminFunc = userFuncService.loadByCode(user.getId(), "admin");
		
		int deletedCount = 0;
		for(int i=0; ids !=null && i < ids.length; i++){
			Payment payment = paymentService.load(ids[i]);
			long creatorId = payment.getCreatorId().longValue();
			if(payment.getStatus() == PaymentStatus.CONFIRMED)
				continue;
			
			if(userId == creatorId || adminFunc !=null){
				getLogger().info(userId + " 删除 payId=" + payment.getId() + " cstmId=" + payment.getCstmId());
				
				paymentService.delete(user, payment.getId());
				deletedCount ++;
			}
		}
		return new ResponseObject<Integer>(deletedCount);
	}
	
	@RequestMapping(value = "/save.do")
	@ResponseBody
    public ResponseObject<Payment> save(HttpServletRequest req, PaymentVO payment){
		User user = this.getCurrentUser();
		
		if(StringUtils.isBlank(payment.getPayerName()))
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "付款人不能为空");		

		if(payment.getId() !=null){
			Payment oldPay = paymentService.load(payment.getId());
			if(oldPay.getStatus() == PaymentStatus.CONFIRMED)
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "已确认支付信息 不能重复确认");
		}
		if(payment.getStatus() == null || payment.getStatus() == PaymentStatus.DRAFT)
			payment.setStatus(PaymentStatus.SUBMIT);
		
		Long cstmId = payment.getCstmId();
		
		for(PaymentLineVO line:payment.getLines()){
			String conNo = line.getConNo();
			if(StringUtils.isNotBlank(conNo)){
				conNo = conNo.trim().toUpperCase();
				line.setConNo(conNo);
				
				CustContract contract = contractService.loadByNo(conNo);
				if(contract == null)
					throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "合同号错误,合同没找到 conNo=" + conNo);
				
				if(contract.getCstmId().longValue() != cstmId.longValue())
					throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "合同号和客户信息不一致");
				line.setConId(contract.getId());
			}else{
				line.setConId(null);
			}
		}
		
		try{
			if(payment.getId() == null){
				String payNo = UniqueNoUtils.next(UniqueNoUtils.UniqueNoType.P);
				payment.setPayNo(payNo);
				paymentService.add(user, payment);
			}else{
				paymentService.update(user, payment);
			}
			
		}catch(Exception e){
			this.getLogger().error("", e);
			throw new BizException(GlobalErrorCode.INTERNAL_ERROR, "");
		}
		
		return new ResponseObject<Payment>(payment);
	}
	
	@RequestMapping(value = "/detailData.do")
	@ResponseBody
    public ResponseObject<PaymentVO> detailData(Long id){
		User user = this.getCurrentUser();
		PaymentVO vo = paymentService.loadVO(id);
		
		if(vo.getStatus() == PaymentStatus.CONFIRMED){
			
		}
		
		return new ResponseObject<PaymentVO>(vo);
	}
	
	@RequestMapping(value = "/confirm.do")
	@ResponseBody
    public ResponseObject<Payment> confirm(HttpServletRequest req, PaymentVO payment){
		if(StringUtils.isBlank(payment.getPayerName())){
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "付款人不能为空");
		}
		PaymentVO oldPay = paymentService.loadVO(payment.getId());

		User user = this.getCurrentUser();
		if(oldPay.getStatus() == PaymentStatus.SUBMIT){
			UserFuncVO confirmFunc = userFuncService.loadByCode(user.getId(), "sk_fz_confirm");
			if(confirmFunc == null)
				throw new BizException(GlobalErrorCode.UNAUTHORIZED, "权限不够 需要sk_fz_confirm权限");
		}else if(oldPay.getStatus() == PaymentStatus.FZ_CONFIRMED){
			UserFuncVO confirmFunc = userFuncService.loadByCode(user.getId(), "sk_confirm");
			if(confirmFunc == null)
				throw new BizException(GlobalErrorCode.UNAUTHORIZED, "权限不够");
		}else if(oldPay.getStatus() == PaymentStatus.CONFIRMED){
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "已确认支付信息 不能重复确认");
		}else{
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "提交后才能确认");
		}
			
		oldPay.setMemo(payment.getMemo());
		
		try{
			paymentService.confirm(user, oldPay);
		}catch(Exception e){
			this.getLogger().error("", e);
			throw new BizException(GlobalErrorCode.INTERNAL_ERROR, "");
		}
		
		return new ResponseObject<Payment>(payment);
	}
	
	/**
	 * 撤销确认
	 * @param req
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/revoke.do")
	@ResponseBody
    public ResponseObject<Payment> revoke(HttpServletRequest req, Long id){
		User user = this.getCurrentUser();
		
		PaymentVO payment = paymentService.loadVO(id);
		if(payment.getStatus() == PaymentStatus.FZ_CONFIRMED){
			if( !user.getId().equals(payment.getFzConfirmerId()) )
				throw new BizException(GlobalErrorCode.UNAUTHORIZED, "只有审批人才能撤回");
		}
		else if(payment.getStatus() == PaymentStatus.CONFIRMED){
			if( ! user.getId().equals(payment.getConfirmerId()) )
				throw new BizException(GlobalErrorCode.UNAUTHORIZED, "只有审批人才能撤回");
			
		}else{
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "此状态不能撤回");
		}
		
		try{
			paymentService.revoke(user, payment);
			this.getLogger().info(user.getAccount() + "(" + user.getId() + ")撤回付款确认 id=" + id);
			
		}catch(Exception e){
			this.getLogger().error("", e);
			throw new BizException(GlobalErrorCode.INTERNAL_ERROR, "");
		}
		
		return new ResponseObject<Payment>(payment);
	}
	
	private void processWhenList(PaymentVO vo){
		if( vo.getPayType() !=null)
			vo.setPayTypeName( dictService.load(vo.getPayType()).getDictName() );
		if(vo.getCreatorId() !=null)
			vo.setCreatorName( userService.load(vo.getCreatorId()).getName() );
		if(vo.getFzConfirmerId() !=null)
			vo.setFzConfirmerName( userService.load(vo.getFzConfirmerId()).getName() );
		if(vo.getConfirmerId() !=null)
			vo.setConfirmerName( userService.load(vo.getConfirmerId()).getName() );
		
		vo.setCompanyName( unitService.getFullName(vo.getCompanyId()) );
	}
}
