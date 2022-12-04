package com.niu.crm.dao.mapper;


import com.niu.crm.model.CustomerRecommend;

public interface CustomerRecommendMapper {
	
	int insert(CustomerRecommend recommed);
	
	CustomerRecommend selectByPrimaryKey(Long id);
}
