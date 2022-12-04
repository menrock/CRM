package com.niu.crm.service;

import java.util.List;
import com.niu.crm.model.Country;

public interface CountryService {
    	
	void refrechCache();
	
	Country loadByCode(String code);
	
	int add(Country country);

    int update(Country country);

    List<Country> loadAll();
	
}
