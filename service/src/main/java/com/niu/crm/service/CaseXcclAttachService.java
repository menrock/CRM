package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.CustAttachSearchForm;
import com.niu.crm.model.CaseXcclAttach;
import com.niu.crm.model.User;

public interface CaseXcclAttachService {

	CaseXcclAttach load(Long id);

	void save(User user, CaseXcclAttach attach);

	Boolean delete(User user, Long id);

	List<CaseXcclAttach> queryAttach(CustAttachSearchForm searchForm,
			Pageable pageable);

	Integer countAttach(CustAttachSearchForm searchForm);
}
