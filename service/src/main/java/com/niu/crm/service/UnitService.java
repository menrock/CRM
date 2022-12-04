package com.niu.crm.service;


import java.util.List;

import com.niu.crm.model.Unit;
import com.alibaba.fastjson.JSONArray;

public interface UnitService {

	void refrechCache();
	
	Unit load(Long id);
	
	Unit loadByCode(String code);
	
	int add(Unit unit);
    
    void delete(Long id);

    int update(Unit unit);

    List<Unit> loadChildren(Long parentId);
    
    List<Unit> getAllCompany();
    
    JSONArray loadJSONTree(String parentCode);
    
    JSONArray getJSONTree(String parentCode);
    
    String getName(Long id);

    String getFullName(Long id);
    
    /**
     * 判断 ancestorUnit 是否是unit的上级部门
     * @param ancestorUnit
     * @param unit
     * @return
     */
    boolean isAncestorUnit(Unit ancestorUnit, Unit unit);
    
    /**
     * 部门合并(fromUnit 合并到 toUnit)
     * @param fromUnitId
     * @param toUnitId
     */
    void mergeTo(Long fromUnitId, Long toUnitId);
	
    /**
     * 部门迁移(fromUnit 迁移到 toUnit下)
     * @param fromUnitId
     * @param toUnitId
     */
	void moveTo(Long fromUnitId, Long toUnitId);
}
