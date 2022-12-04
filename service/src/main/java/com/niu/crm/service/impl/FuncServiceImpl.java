package com.niu.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.dao.mapper.FuncMapper;
import com.niu.crm.dao.mapper.UserFuncMapper;
import com.niu.crm.model.Func;
import com.niu.crm.model.type.AclScope;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.FuncService;

@Service
public class FuncServiceImpl extends BaseService implements FuncService {
    @Autowired
    private UserFuncMapper  userFuncMapper;
    @Autowired
    private FuncMapper  funcMapper;
    
    @Override
    public AclScope[] getAllAclScope(){
		return new AclScope[]{AclScope.SELF, AclScope.SOMEUNIT, AclScope.SELFCOMPANY, AclScope.SOMECOMPANY, AclScope.ALL};
	}

	@Override
	public Func load(Long id) {
		return funcMapper.selectById(id);
	}

	@Override
	public Func loadByCode(String code) {
		return funcMapper.selectByCode(code);
	}

	@Override
	public int add(Func func) {
		return funcMapper.insert(func);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		userFuncMapper.deleteByFuncId(id);
		
		funcMapper.delete(id);
	}

	@Override
	public int update(Func func) {
		return funcMapper.update(func);
	}

	@Override
	public List<Func> list(Pageable pager) {
		return funcMapper.selectFunc(pager);
	}
}
