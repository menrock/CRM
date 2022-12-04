package com.niu.crm.dao.mapper;

import java.util.List;

import com.niu.crm.model.CommInvoiceLine;



public interface CommInvoiceLineMapper {
	
	CommInvoiceLine selectByPrimaryKey(Long id);
	void insert(CommInvoiceLine invLine);
	void update(CommInvoiceLine invLine);
	
	List<CommInvoiceLine> selectByInvId(Long invId);
	
	int deleteByPrimaryKey(Long id);
	int deleteByInvId(Long invId);

}
