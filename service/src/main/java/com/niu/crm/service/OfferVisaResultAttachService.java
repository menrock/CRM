package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.CustAttachSearchForm;
import com.niu.crm.model.OfferVisaResultAttach;
import com.niu.crm.model.User;

public interface OfferVisaResultAttachService {

	OfferVisaResultAttach load(Long id);

	void save(User user, OfferVisaResultAttach attach);

	Boolean delete(User user, Long id);

	List<OfferVisaResultAttach> queryAttach(CustAttachSearchForm searchForm,
			Pageable pageable);

	Integer countAttach(CustAttachSearchForm searchForm);
}
