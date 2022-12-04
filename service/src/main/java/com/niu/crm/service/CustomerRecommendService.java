package com.niu.crm.service;

import com.niu.crm.model.CustomerRecommend;
import com.niu.crm.model.User;

public interface CustomerRecommendService {
	
	void lx2Px(User user, CustomerRecommend recommend);
	
	void lx2Cp(User user, CustomerRecommend recommend);
	
	void px2Lx(User user, CustomerRecommend recommend);
	
	void px2Cp(User user, CustomerRecommend recommend);
	
	void cp2Lx(User user, CustomerRecommend recommend);
	
	void cp2Px(User user, CustomerRecommend recommend);

}
