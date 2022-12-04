package com.niu.crm.dao.mapper;

import java.util.List;
import com.niu.crm.model.LxContractCountry;

public interface LxContractCountryMapper {
	
	int insert(LxContractCountry contractCountry);
	
	int deleteByLxConId(Long conId);

	List<LxContractCountry> selectByLxConId(Long conId);

}
