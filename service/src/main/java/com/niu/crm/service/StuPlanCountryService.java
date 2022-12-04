package com.niu.crm.service;

import java.util.List;
import com.niu.crm.model.StuPlanCountry;

public interface StuPlanCountryService {
	
	int insert(StuPlanCountry stuPlanCountry);

    int deleteByStuId(Long stuId);

    List<StuPlanCountry> loadByStuId(Long stuId);
	
}
