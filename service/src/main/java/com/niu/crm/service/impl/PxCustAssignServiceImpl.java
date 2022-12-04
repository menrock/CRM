package com.niu.crm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.niu.crm.dao.mapper.PxCustAssignMapper;
import com.niu.crm.model.CustAssign;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.PxCustAssignService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.PxCustAssignVO;

@Service
public class PxCustAssignServiceImpl extends BaseService implements PxCustAssignService {
	@Autowired
	private PxCustAssignMapper pxCustAssignMapper;
	@Autowired
	private UserService userService;

	@Override
	public CustAssign load(Long id) {
		return pxCustAssignMapper.selectById(id);
	}

	@Override
	public List<PxCustAssignVO> selectByStuId(Long stuId) {
		List<CustAssign> ls = pxCustAssignMapper.selectByStuId(stuId);
		
		List<PxCustAssignVO> list = new ArrayList<>();
		for(CustAssign item:ls){
			PxCustAssignVO vo = new PxCustAssignVO();
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
