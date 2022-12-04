package com.niu.crm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.niu.crm.dao.mapper.LxCustAssignMapper;
import com.niu.crm.model.LxCustAssign;
import com.niu.crm.model.User;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.LxCustAssignService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.LxCustAssignVO;

@Service
public class LxCustAssignServiceImpl extends BaseService implements LxCustAssignService {
	@Autowired
	LxCustAssignMapper lxCustAssignMapper;
	@Autowired
	UserService userService;

	@Override
	public LxCustAssign load(Long id) {
		return lxCustAssignMapper.selectById(id);
	}

	@Override
	public List<LxCustAssignVO> selectByStuId(Long stuId) {
		List<LxCustAssign> ls = lxCustAssignMapper.selectByStuId(stuId);
		
		List<LxCustAssignVO> list = new ArrayList<LxCustAssignVO>();
		for(LxCustAssign item:ls){
			LxCustAssignVO vo = new LxCustAssignVO();
			BeanUtils.copyProperties(item, vo);
			
			if(vo.getZxgwId() !=null)
				vo.setZxgwName( userService.load(vo.getZxgwId()).getName() );
			if(vo.getCreatorId() !=null)
				vo.setCreatorName( userService.load(vo.getCreatorId()).getName());
			
			list.add(vo);
		}
		
		return list;
	}

}
