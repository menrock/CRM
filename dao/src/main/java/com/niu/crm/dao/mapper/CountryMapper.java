package com.niu.crm.dao.mapper;

import java.util.List;


import com.niu.crm.model.Country;

public interface CountryMapper {
	
	int insert(Country country);
	
	int update(Country country);
	
	Country selectByCode(String code);

	List<Country> selectAll();

}
