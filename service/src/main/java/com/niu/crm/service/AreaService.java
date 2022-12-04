package com.niu.crm.service;


import java.util.List;

import com.niu.crm.model.Area;

public interface AreaService {

	void refrechCache();
	
	Area load(Long id);
	
    List<Area> loadChildren(Long parentId);
    
}
