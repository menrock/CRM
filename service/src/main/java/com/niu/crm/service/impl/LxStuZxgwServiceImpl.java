package com.niu.crm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.niu.crm.dao.mapper.StuZxgwMapper;
import com.niu.crm.model.Dict;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.LxStuZxgwService;
import com.niu.crm.service.UserService;

@Service
public class LxStuZxgwServiceImpl extends BaseService implements
		LxStuZxgwService {
	@Autowired
	private UserService userService;
	@Autowired
	private DictService dictService;
	@Autowired
	private StuZxgwMapper stuZxgwMapper;

	@Override
	public void add(StuZxgw stuZxgw) {
		stuZxgwMapper.insert(stuZxgw);
	}

	@Override
	public StuZxgw load(Long id) {
		return stuZxgwMapper.selectByPrimaryKey(id);
	}

	@Override
	public StuZxgw load(Long stuId, Long zxgwId) {
		return stuZxgwMapper.selectByStuIdAndZxgw(stuId, zxgwId);
	}

	@Override
	public List<StuZxgw> listByStuId(Long stuId) {
		List<StuZxgw> ls = stuZxgwMapper.selectByStuId(stuId);
		
		for(StuZxgw zxgw:ls)
			initZxgw(zxgw);
		
		return ls;
	}

	private void initZxgw(StuZxgw zxgw){
		if(zxgw ==null)
			return ;
		
			
		String uName = userService.load(zxgw.getZxgwId()).getName();
		zxgw.setZxgwName(uName);
			
		if(zxgw.getStuLevel() !=null){
			Dict dict = dictService.load(zxgw.getStuLevel());
			if(dict !=null)
				zxgw.setStuLevelName(dict.getDictName());
			else
				zxgw.setStuLevelName(zxgw.getStuLevel().toString());
		}
	}
}
