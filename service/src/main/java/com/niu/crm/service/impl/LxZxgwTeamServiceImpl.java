package com.niu.crm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.niu.crm.dao.mapper.LxZxgwTeamMapper;
import com.niu.crm.model.User;
import com.niu.crm.model.ZxgwTeam;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.LxZxgwTeamService;
import com.niu.crm.service.UserService;

@Service
public class LxZxgwTeamServiceImpl extends BaseService implements LxZxgwTeamService {
	
	
    @Autowired
    private LxZxgwTeamMapper  zxgwTeamMapper;

	
    @Autowired
    private UserService  userService;
    

    @Override
	public int add(ZxgwTeam zxgwTeam){
		return zxgwTeamMapper.insert(zxgwTeam);
	}

    @Override
	public int deleteByLeaderId(Long leaderId){
		return zxgwTeamMapper.deleteByLeaderId(leaderId);
	}
    @Override
	public ZxgwTeam loadByZxgwId(Long zxgwId){
		ZxgwTeam zxgwTeam = zxgwTeamMapper.selectByZxgwId(zxgwId);
		return zxgwTeam;
	}
    
    @Override
	public List<ZxgwTeam> loadByLeaderId(Long leaderId){
		List<ZxgwTeam> ls = zxgwTeamMapper.selectByLeaderId(leaderId);
		if(ls == null)
			ls = new ArrayList<>();
		
		return ls;
	}
    
    @Override
	public boolean isTeamLeader(long userId){
    	List<ZxgwTeam> ls = zxgwTeamMapper.selectByLeaderId(userId);
    	if(ls == null || ls.size() ==0)
    		return false;
    	else
    		return true;
	}
    
    @Override
	public List<User> listTeamLeader(){
		List<Long> ls = zxgwTeamMapper.selectLeaderId(); 
		List<User> lsUser = new ArrayList<>();
		for(Long id:ls){
			lsUser.add( userService.load(id) );
		}
		return lsUser;
	}
}
