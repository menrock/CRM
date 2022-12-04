package com.niu.crm.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.dao.mapper.CommInvoiceLineMapper;
import com.niu.crm.dao.mapper.CommInvoiceMapper;
import com.niu.crm.form.CommInvoiceSearchForm;
import com.niu.crm.model.CommInvoice;
import com.niu.crm.model.CommInvoiceLine;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.CommInvoiceService;

@Service
public class CommInvoiceServiceImpl extends BaseService implements
		CommInvoiceService {
	@Autowired
	private CommInvoiceMapper invoiceMapper;
	@Autowired
	private CommInvoiceLineMapper invoiceLineMapper;

	@Override
	public CommInvoice load(Long id) {
		return invoiceMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<CommInvoiceLine> loadLines(Long invId) {
		return invoiceLineMapper.selectByInvId(invId);
	}
	
	@Transactional
	@Override
	public void add(CommInvoice invoice) {
		List<CommInvoiceLine> lines = invoice.getLines();
		
		//计算账单金额
		BigDecimal invAmount = BigDecimal.ZERO;
		for(int i=0; lines !=null && i < lines.size(); i++){
			CommInvoiceLine invLine = lines.get(i);
			invAmount = invAmount.add(invLine.getCommAmount());
		}
		invoice.setInvAmount(invAmount);
		invoiceMapper.insert(invoice);
		
		for(int i=0; lines !=null && i < lines.size(); i++){
			CommInvoiceLine invLine = lines.get(i);
			invLine.setInvId(invoice.getId());
			
			invoiceLineMapper.insert(invLine);
		}
	}

	@Override
	public void update(CommInvoice invoice) {
		invoiceMapper.update(invoice);
		invoiceLineMapper.deleteByInvId( invoice.getId() );
		
		List<CommInvoiceLine> lines = invoice.getLines();
		
		//计算账单金额
		BigDecimal invAmount = BigDecimal.ZERO;
		for(int i=0; lines !=null && i < lines.size(); i++){
			CommInvoiceLine invLine = lines.get(i);
			invAmount = invAmount.add(invLine.getCommAmount());
		}
		invoice.setInvAmount(invAmount);
		
		for(int i=0; lines !=null && i < lines.size(); i++){
			CommInvoiceLine invLine = lines.get(i);
			invLine.setInvId(invoice.getId());
			
			invoiceLineMapper.insert(invLine);
		}
	}

	@Override
	public void sent(CommInvoice invoice) {
		invoiceMapper.sent(invoice);
	}
	
	@Override
	public void received(CommInvoice invoice) {
		invoiceMapper.received(invoice);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		invoiceLineMapper.deleteByInvId(id);
		invoiceMapper.delete(id);
	}

	@Override
	public Integer countInvoice(CommInvoiceSearchForm form) {
		return invoiceMapper.countInvoice(form);
	}

	@Override
	public List<CommInvoice> queryInvoice(CommInvoiceSearchForm form,
			Pageable pager) {
		return invoiceMapper.queryInvoice(form, pager);
	}

}
