package com.niu.crm.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.dao.mapper.CustContractMapper;
import com.niu.crm.dao.mapper.CustContractSkMapper;
import com.niu.crm.dao.mapper.CustomerMapper;
import com.niu.crm.dao.mapper.LxCustomerMapper;
import com.niu.crm.dao.mapper.PaymentLineMapper;
import com.niu.crm.dao.mapper.PaymentMapper;
import com.niu.crm.form.PaymentSearchForm;
import com.niu.crm.model.CustContractSk;
import com.niu.crm.model.Customer;
import com.niu.crm.model.Dict;
import com.niu.crm.model.Payment;
import com.niu.crm.model.PaymentLine;
import com.niu.crm.model.User;
import com.niu.crm.model.type.PaymentStatus;
import com.niu.crm.model.type.SkStatus;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.CustContractSkService;
import com.niu.crm.service.PaymentService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.PaymentLineVO;
import com.niu.crm.vo.PaymentVO;

@Service
public class PaymentServiceImpl extends BaseService implements PaymentService {

	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private LxCustomerMapper lxCustomerMapper;
	@Autowired
	private CustContractMapper contractMapper;
	@Autowired
	private PaymentMapper paymentMapper;
	@Autowired
	private PaymentLineMapper paymentLineMapper;
	@Autowired
	private CustContractSkMapper skMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private DictService dictService;
	@Autowired
	private UnitService unitService;

	@Autowired
	private CustContractSkService skService;

	@Override
	public Payment load(Long id) {
		return paymentMapper.selectById(id);
	}
	
	@Override
	public PaymentVO loadVO(Long id) {
		PaymentVO payment = paymentMapper.selectById(id);
		
		if (payment == null) {
			return null;
		}
		

		if( payment.getPayType() !=null)
			payment.setPayTypeName( dictService.load(payment.getPayType()).getDictName() );
		
		Customer cust = customerMapper.selectById(payment.getCstmId());
		payment.setCstmName( cust.getName() );
			
		List<PaymentLineVO> lines = paymentLineMapper.selectByPayId(id);
		payment.setLines(lines);
			
		Map<Long,String> map = new HashMap<Long,String>();
		for(int i=0; lines !=null && i < lines.size(); i++){
			PaymentLineVO line = lines.get(i);
			
			Dict skItem = dictService.load(line.getItemId());
			String itemName = skItem.getDictName();
			line.setItemName(itemName);
			line.setCompanyName( unitService.load(line.getCompanyId()).getName() );
				
			if(line.getConId() !=null){
				String conNo = map.get(line.getConId());
				if(conNo == null){
					conNo = contractMapper.selectByIdOrNo(line.getConId(), null).getConNo();
				}
				line.setConNo(conNo);
			}
		}		
		return payment;
	}

	@Override
	public List<PaymentVO> loadByCstmId(Long cstmId) {
		return paymentMapper.selectByCstmId(cstmId);
	}

	@Transactional
	@Override
	public void delete(User user, Long id) {
		Payment payment = this.load(id);
		if(payment.getStatus() == PaymentStatus.CONFIRMED)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "付款信息已确认,不能删除");
		
		paymentLineMapper.deleteByPayId(id);
		paymentMapper.delete(id);
	}

	@Transactional
	@Override
	public int add(User user, PaymentVO payment) {
		checkPayment(payment);

		Long companyId = lxCustomerMapper.selectByCstmId(payment.getCstmId()).getCompanyId();
		payment.setCompanyId(companyId);
		payment.setCreatorId(user.getId());
		if(payment.getPayType() !=null){
			payment.setPayTypeName( dictService.load(payment.getPayType()).getDictName());
		}
		
		int rc = paymentMapper.insert(payment);
		Long payId = payment.getId();
		
		for(PaymentLine line:payment.getLines()){
			//退费 不再允许录入
			if(line.getItemId() ==488)
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"不允许直接录入退费  itemId=" + line.getItemId());
				
			
			Dict skItem = dictService.load(line.getItemId());
			List<Dict> lsChild = dictService.loadChildren(skItem.getId());
			if(lsChild.size() >0)
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"费用项目错误  id=" + skItem.getId());
			
			line.setPayId(payId);
			line.setCompanyId(companyId);
			line.setCreatorId(user.getId());
			paymentLineMapper.insert(line);
		}
	
		return rc;
	}

	@Transactional
	@Override
	public int update(User user, PaymentVO payment) {
		checkPayment(payment);
		
		Long companyId = lxCustomerMapper.selectByCstmId(payment.getCstmId()).getCompanyId();
		Long payId = payment.getId();
		Payment oldPayment = this.load(payId);
		if(oldPayment.getStatus() == PaymentStatus.CONFIRMED)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "付款信息已确认,不能删除");
		
		if(payment.getPayType() !=null){
			payment.setPayTypeName( dictService.load(payment.getPayType()).getDictName());
		}
		int rc= paymentMapper.update(payment);

		paymentLineMapper.deleteByPayId(payment.getId());
		for(PaymentLine line:payment.getLines()){
			Dict skItem = dictService.load(line.getItemId());
			List<Dict> lsChild = dictService.loadChildren(skItem.getId());
			if(lsChild.size() >0)
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"费用项目错误  id=" + skItem.getId());
			
			line.setPayId(payId);
			line.setCompanyId(companyId);
			line.setCreatorId(user.getId());
			paymentLineMapper.insert(line);
		}
		
		return rc;
	}

	@Transactional
	@Override
	public int confirm(User user, PaymentVO payment){
		Payment oldPay = paymentMapper.selectById(payment.getId());
		if(oldPay.getStatus() == PaymentStatus.CONFIRMED)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"不能重复确认");
		else if(oldPay.getStatus() == PaymentStatus.DRAFT)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"请先提交");
		
		if(oldPay.getStatus() == PaymentStatus.SUBMIT){
			payment.setFzConfirmerId(user.getId());
			payment.setStatus(PaymentStatus.FZ_CONFIRMED);
			int rc = paymentMapper.fzConfirm(payment);
			return rc;
		}
		
		payment.setConfirmerId(user.getId());
		payment.setStatus(PaymentStatus.CONFIRMED);
		int rc = paymentMapper.confirm(payment);
		
		
		Set<Long> setConId = new HashSet<Long>(); 
		
		CustContractSk skRecord = null;
		for(PaymentLineVO line:payment.getLines()){
			skRecord = new CustContractSk();
			skRecord.setPayLineId(line.getId());
			skRecord.setCstmId(payment.getCstmId());
			skRecord.setPayType(payment.getPayType());
			skRecord.setCompanyId(line.getCompanyId());
			skRecord.setItemId(line.getItemId());
			skRecord.setConId(line.getConId());
			skRecord.setSkValue(line.getItemValue());
			skRecord.setKcgwName(line.getKcgwName());
			skRecord.setMemo(line.getMemo());
			skRecord.setSkDate(payment.getPaidAt());
			skRecord.setStatus(SkStatus.SUBMIT);
			skRecord.setCreatorId(user.getId());
			
			if(line.getConId() !=null)
				setConId.add(line.getConId());
			
			skService.add(user, skRecord);
		}
		
		return rc;
	}
	
	@Transactional
	@Override
	public int revoke(User user, PaymentVO payment){
		int rc = 0;
				
		if(payment.getStatus() == PaymentStatus.FZ_CONFIRMED){
			payment.setStatus(PaymentStatus.SUBMIT);
			paymentMapper.fzRevoke(payment);
		}
		
		if(payment.getStatus() == PaymentStatus.CONFIRMED){
			if(payment.getFzConfirmerId() == null)
				payment.setStatus(PaymentStatus.SUBMIT);
			else
				payment.setStatus(PaymentStatus.FZ_CONFIRMED);
			
			rc = paymentMapper.revoke(payment);
			
			for(PaymentLineVO line:payment.getLines()){
				CustContractSk skRecord = skMapper.selectByPayLineId(line.getId());
				
				skService.delete(user, skRecord.getId());
			}
		}
		
		return rc;
	}
	

	@Override
	public int queryCount(PaymentSearchForm form){
		return paymentMapper.queryCount(form);
	}
	
	@Override
	public List<PaymentVO> query(PaymentSearchForm form, Pageable pageable){
		List<PaymentVO> ls = paymentMapper.query(form, pageable);
		return ls;
	}
	
	private void checkPayment(PaymentVO vo){
		BigDecimal lineSum = BigDecimal.ZERO;
		for(PaymentLine line:vo.getLines()){
			lineSum = lineSum.add(line.getItemValue());
			
			List<Dict> lsChild = dictService.loadChildren(line.getItemId());
			if(lsChild.size() >0)
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "费用科目只能选择叶子科目");
		}
		if(lineSum.compareTo(vo.getInvMoney()) !=0)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "行合计金额和付款金额不一致");
	}

}
