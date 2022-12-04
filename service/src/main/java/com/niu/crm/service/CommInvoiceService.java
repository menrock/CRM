package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.CommInvoiceSearchForm;
import com.niu.crm.model.CommInvoice;
import com.niu.crm.model.CommInvoiceLine;

public interface CommInvoiceService {
	
	CommInvoice load(Long id);
	
	List<CommInvoiceLine> loadLines(Long invId);
	
	void add(CommInvoice invoice);
	
	void update(CommInvoice invoice);
	
	void sent(CommInvoice invoice);
	
	void received(CommInvoice invoice);
	
	void delete(Long id);
	
	Integer countInvoice(CommInvoiceSearchForm form);
	
	List<CommInvoice> queryInvoice(CommInvoiceSearchForm form, Pageable pager);
}
