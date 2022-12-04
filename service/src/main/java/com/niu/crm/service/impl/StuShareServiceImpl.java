package com.niu.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.niu.crm.dao.mapper.StuShareMapper;
import com.niu.crm.form.StuShareSearchForm;
import com.niu.crm.model.StuShare;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.StuShareService;
import com.niu.crm.service.UserService;

@Service
public class StuShareServiceImpl extends BaseService implements StuShareService {

    @Autowired
    private StuShareMapper stuShareMapper;
    @Autowired
    private UserService userService;
    
	@Override
	public StuShare load(Long id) {
		return stuShareMapper.selectById(id);
	}
    
	@Override
	public int add(StuShare stuShare) {
		return stuShareMapper.insert(stuShare);
	}

	@Override
	public int delete(Long id) {
		return stuShareMapper.delete(id);
	}

	@Override
	public Integer countShare(StuShareSearchForm searchForm) {
		return stuShareMapper.countShare(searchForm);
	}

	@Override
	public List<StuShare> queryShare(StuShareSearchForm searchForm,
			Pageable pager) {
		List<StuShare> ls = stuShareMapper.queryShare(searchForm, pager);
		for(int i=0; ls !=null && i < ls.size(); i++){
			StuShare item = ls.get(i);
			
			item.setFromUserName( userService.load(item.getFromUserId()).getName() );
			item.setToUserName( userService.load(item.getToUserId()).getName() );
			item.setCreatorName( userService.load(item.getCreatorId()).getName() );
		}
		return ls;
	}

}
