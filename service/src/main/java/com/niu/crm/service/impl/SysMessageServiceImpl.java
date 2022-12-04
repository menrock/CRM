package com.niu.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.niu.crm.dao.mapper.SysMessageMapper;
import com.niu.crm.form.SysMessageSearchForm;
import com.niu.crm.model.SysMessage;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.SysMessageService;
import com.niu.crm.service.UserService;

@Service
public class SysMessageServiceImpl extends BaseService implements SysMessageService {
    @Autowired
    private SysMessageMapper  sysMessageMapper;
    @Autowired
    private UserService  userService;

	@Override
	public int add(SysMessage msg) {
		return sysMessageMapper.insert(msg);
	}

	@Override
	public int update(SysMessage msg) {
		return sysMessageMapper.update(msg);
	}

	@Override
	public int delete(Long id) {
		return sysMessageMapper.delete(id);
	}

	@Override
	public SysMessage load(Long id) {
		return sysMessageMapper.selectById(id);
	}

	@Override
	public List<SysMessage> queryMessage(SysMessageSearchForm form, Pageable pageable) {
		List<SysMessage> ls = sysMessageMapper.queryMessage(form, pageable);

		for(SysMessage msg:ls){
			if(msg.getSenderId() !=null)
				msg.setSenderName( userService.load(msg.getSenderId()).getName() );
		}
		return ls;
	}

	@Override
	public Integer countMessage(SysMessageSearchForm form) {
		return sysMessageMapper.countMessage(form);
	}

}
