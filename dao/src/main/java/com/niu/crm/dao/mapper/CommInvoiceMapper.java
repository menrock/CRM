package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.CommInvoiceSearchForm;
import com.niu.crm.model.CommInvoice;

public interface CommInvoiceMapper {
	
	CommInvoice selectByPrimaryKey(Long id);
	int delete(Long id);
	
	void insert(CommInvoice invoice);
	void update(CommInvoice invoice);
	

	void sent(CommInvoice invoice);
	void received(CommInvoice invoice);
	
	
	int countInvoice(@Param("params")CommInvoiceSearchForm form);
	
	List<CommInvoice>  queryInvoice(@Param("params")CommInvoiceSearchForm form, @Param("pager")Pageable pageable);

}
