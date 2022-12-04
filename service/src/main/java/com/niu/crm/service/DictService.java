package com.niu.crm.service;

import java.util.List;

import com.niu.crm.model.Dict;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.vo.DictVO;
import com.alibaba.fastjson.JSONArray;

public interface DictService {

	void refrechCache();
	
	DictVO dict2VO(Dict dict);
	
	Dict load(Long id);
	
	Dict loadByCode(String code);
	
	int add(User user, DictVO dict);
    
    void delete(User user, Long id);

    int update(User user, DictVO dict);

    List<Dict> loadChildren(Long parentId);
    
    List<Dict> loadChildren(String parentCode);
    
    List<Dict> selectByName(String codePrefix, String dictName);
    
    JSONArray loadJSONTree(Long parentId);
    
    JSONArray getJSONTree(Long parentId);
    
    /**
     * 判断 ancestorDict 是否是dict的上级
     * @param ancestorDict
     * @param dict
     * @return
     */
    boolean isAncestorDict(Dict ancestorDict, Dict dict);
    
    
    /**
     * 部门合并(fromId 合并到 toId)
     * @param fromId
     * @param toId
     */
    void stuFromMergeTo(User user, Long fromId, Long toId);
	
    /**
     * 客户来源迁移(fromId 迁移到 toId下)
     * @param fromId
     * @param toId
     */
	void stuFromMoveTo(User user, Long fromId, Long toId);
}
