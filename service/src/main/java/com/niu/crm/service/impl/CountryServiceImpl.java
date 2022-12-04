package com.niu.crm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.niu.crm.dao.mapper.CountryMapper;
import com.niu.crm.model.Country;
import com.niu.crm.model.Unit;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.CountryService;

@Service
public class CountryServiceImpl extends BaseService implements CountryService {
	private Map<String, Country> cacheByCode = null;
	
    @Autowired
    private CountryMapper  countryMapper;
    
    @Override
	public void refrechCache() {
		cacheByCode = null;
	}

	private void refreshCache(String code) { 
		if (cacheByCode == null) {
			cacheByCode = new HashMap<String, Country>();
		}
		else{
			cacheByCode.remove(code);
		}

		Country country = countryMapper.selectByCode(code);

		if (country == null) {
			getLogger().error("country not found code=" + code);
		} else {
			cacheByCode.put(country.getCode(), country);
		}
	}
    

	@Override
	public Country loadByCode(String code) {
		if(code ==null)
			return null;
		
		code = code.toUpperCase();
		Country country = null;
		if (null != cacheByCode) {
			country = cacheByCode.get(code);
		}
		if (country == null) {
			refreshCache(code);
			country = cacheByCode.get(code);
		}
		
		return country;
	}

	@Override
	public int add(Country country) {
		int count = countryMapper.insert(country);
		refreshCache(country.getCode());
		return count;
	}

	@Override
	public int update(Country country) {
		int count = countryMapper.update(country);
		refreshCache(country.getCode());
		return count;
	}

	@Override
	public List<Country> loadAll() {
		List<Country> ls = countryMapper.selectAll();
		return ls;
	}
}
