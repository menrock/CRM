package com.niu.crm.dao.mapper;

import java.util.List;
import com.niu.crm.model.StuPlanCountry;

public interface StuPlanCountryMapper {
	
	int insert(StuPlanCountry country);
	
	int deleteByStuId(Long stuId);

	List<StuPlanCountry> selectByStuId(Long stuId);

}
