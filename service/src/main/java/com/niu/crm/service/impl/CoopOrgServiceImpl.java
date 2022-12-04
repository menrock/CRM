package com.niu.crm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.service.BaseService;
import com.niu.crm.service.CoopOrgService;
import com.niu.crm.vo.DictVO;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.dao.mapper.CoopOrgMapper;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.CoopOrg;

@Service
public class CoopOrgServiceImpl extends BaseService implements CoopOrgService {
	
    @Autowired
    private CoopOrgMapper  orgMapper;
    
    @Override
	public CoopOrg load(Long id){
    	return orgMapper.selectByPrimaryKey(id);
    }
    
    @Override
	public int add(User user, CoopOrg org){
    	org.setCreatorId(user.getId());
    	
    	
		return orgMapper.insert(org);
	}
    
    @Override
	public void delete(User user, Long id){
    	orgMapper.delete(id);
	}
    
    @Override
	public int update(User user, CoopOrg org){
		return orgMapper.update(org);
	}
    
    @Override
    public int queryCount(String keywords){
    	return orgMapper.queryCount(keywords);
    }
    
    @Override
    public List<CoopOrg> queryOrg(String keywords){
    	return orgMapper.queryOrg(keywords);
    }
}
