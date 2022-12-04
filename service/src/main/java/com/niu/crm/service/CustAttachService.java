package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.CustAttachSearchForm;
import com.niu.crm.model.CustAttach;
import com.niu.crm.model.User;

public interface CustAttachService {

	CustAttach load(Long id);

	void save(User user, CustAttach attach);

	Boolean delete(User user, Long id);

	List<CustAttach> queryAttach(CustAttachSearchForm searchForm,
			Pageable pageable);

	Integer countAttach(CustAttachSearchForm searchForm);
}
