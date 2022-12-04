package com.niu.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.dao.mapper.StuSingleShareMapper;
import com.niu.crm.form.StuShareSearchForm;
import com.niu.crm.model.StuShare;
import com.niu.crm.model.StuSingleShare;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.LxStuOperatorService;
import com.niu.crm.service.StuSingleShareService;
import com.niu.crm.service.UserService;

@Service
public class StuSingleShareServiceImpl extends BaseService implements
		StuSingleShareService {

	@Autowired
	private StuSingleShareMapper stuShareMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private LxStuOperatorService stuOperatorService;

	@Override
	public StuSingleShare load(Long id) {
		return stuShareMapper.selectById(id);
	}

	@Transactional
	@Override
	public int add(StuSingleShare stuShare) {
		int count = stuShareMapper.insert(stuShare);
		stuOperatorService.fix(stuShare.getStuId());

		return count;
	}

	@Transactional
	@Override
	public int delete(Long id) {
		StuSingleShare stuShare = stuShareMapper.selectById(id);
		if (stuShare != null) {
			int count = stuShareMapper.delete(id);
			stuOperatorService.fix(stuShare.getStuId());
			return count;
		} else {
			return 0;
		}
	}

	@Override
	public Integer countShare(StuShareSearchForm searchForm) {
		return stuShareMapper.countShare(searchForm);
	}

	@Override
	public List<StuSingleShare> queryShare(StuShareSearchForm searchForm,
			Pageable pager) {
		List<StuSingleShare> ls = stuShareMapper.queryShare(searchForm, pager);

		for (int i = 0; ls != null && i < ls.size(); i++) {
			StuShare item = ls.get(i);

			item.setFromUserName(userService.load(item.getFromUserId())
					.getName());
			item.setToUserName(userService.load(item.getToUserId()).getName());
			item.setCreatorName(userService.load(item.getCreatorId()).getName());
		}
		return ls;
	}

}
