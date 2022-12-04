package com.niu.crm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.niu.crm.dao.mapper.StuPlanCountryMapper;
import com.niu.crm.model.StuPlanCountry;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.StuPlanCountryService;

@Service
public class StuPlanCountryServiceImpl extends BaseService implements StuPlanCountryService {
    @Autowired
    private StuPlanCountryMapper  planCountryMapper;
    
	@Override
	public int insert(StuPlanCountry planCountry) {
		int count = planCountryMapper.insert(planCountry);
		return count;
	}

	@Override
	public int deleteByStuId(Long stuId) {
		int count = planCountryMapper.deleteByStuId(stuId);
		return count;
	}

	@Override
	public List<StuPlanCountry> loadByStuId(Long stuId) {
		return planCountryMapper.selectByStuId(stuId);
	}
}
