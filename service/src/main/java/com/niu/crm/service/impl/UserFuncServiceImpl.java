package com.niu.crm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.dao.mapper.UserFuncMapper;
import com.niu.crm.form.UserFuncSearchForm;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.UserFunc;
import com.niu.crm.model.type.AclScope;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.UserFuncVO;

@Service
public class UserFuncServiceImpl extends BaseService implements UserFuncService {
    @Autowired
    private UserFuncMapper  userFuncMapper;
    @Autowired
    private UnitService unitService;
    @Autowired
    private UserService userService;
    @Autowired
    private DictService dictService;

    @Transactional
    @Override
    /**
     * @Param user 操作人
     * @userId 被设置人
     */
	public void setUserFunc(User user, Long userId, List<UserFunc> lsUserFunc) {
    	userFuncMapper.deleteByUserId(userId);
    	for(int i=0; lsUserFunc !=null && i < lsUserFunc.size(); i++){
    		UserFunc userFunc = lsUserFunc.get(i);
    		userFunc.setUserId(userId);
    		userFunc.setCreatorId(user.getId());
    		
    		userFuncMapper.insert(userFunc);
    	}
	}
    
	@Override
	public int add(UserFunc userFunc) {
		return userFuncMapper.insert(userFunc);
	}

	@Override
	public void delete(Long userId, Long id) {
		userFuncMapper.delete(userId, id);
	}

	@Override
	public List<UserFuncVO> queryUserFunc(UserFuncSearchForm form) {
		return userFuncMapper.queryUserFunc(form);
	}
	
	@Override
	public UserFuncVO loadByCode(Long userId, String funcCode){
		UserFuncSearchForm form =new UserFuncSearchForm();
		form.setUserId(userId);
		form.setFuncCode(funcCode);
		
		List<UserFuncVO> ls = userFuncMapper.queryUserFunc(form);
		if(ls == null || ls.size() ==0)
			return null;
		else
			return ls.get(0);
		
	}
	
	@Override
	public boolean hasFunc(Long userId, String funcCode){
		UserFuncVO vo = loadByCode(userId, funcCode);
		
		if(vo == null )
			return false;
		else
			return true;
	}
	
	@Override
	public void initNames(UserFuncVO uf) {
		if(null == uf)
			return ;
		StringBuffer buf = new StringBuffer();
		Long[] arrId = null;
		if(uf.getAclScope() == AclScope.SOMEUNIT)
		{
			buf.setLength(0);
			arrId = uf.getUnitIdList();
			for(int i=0; arrId !=null && i < arrId.length; i++){
				if(i >0) buf.append(",");
				buf.append(unitService.load(arrId[i]).getName());
			}
			uf.setUnitNames(buf.toString());
		}
		if(uf.getAclScope() == AclScope.SOMECOMPANY)
		{
			buf.setLength(0);
			arrId = uf.getCompanyIdList();
			for(int i=0; arrId !=null && i < arrId.length; i++){
				if(i >0) buf.append(",");
				buf.append(unitService.load(arrId[i]).getName());
			}
			uf.setCompanyNames(buf.toString());
		}
		if(uf.getAclScope() == AclScope.SOMECOMPANY)
		{
			buf.setLength(0);
			arrId = uf.getFromIdList();
			for(int i=0; arrId !=null && i < arrId.length; i++){
				if(i >0) buf.append(",");
				buf.append(dictService.load(arrId[i]).getDictName());
			}
			uf.setFromNames(buf.toString());
		}
	}

	@Override
	public List<UserFuncVO> loadAll(Long userId) {
		return userFuncMapper.selectAll(userId);
	}
	
	//列出对某个公司有相关权限的人
	@Override
	public List<User> listUsers(String funcCode, Long companyId){
		UserFuncSearchForm form = new UserFuncSearchForm();
		form.setFuncCode(funcCode);
		
		List<User> lsUser = new ArrayList<>();
		List<UserFuncVO> ls = userFuncMapper.queryUserFunc(form);
		for(UserFuncVO vo:ls){
			User u = userService.load( vo.getUserId() );
			if(allowAccess(u, vo, companyId, null))
				lsUser.add(u);
		}
		return lsUser;
	}
	
	@Override
	public boolean allowAccess(User user,UserFuncVO uf, Long companyId, Long unitId){
		Long uCompanyId = user.getCompanyId();
		Long uUnitId = user.getUnitId();
		if(unitId == null){
			if(uf.getAclScope() == AclScope.ALL)
				return true;
			if(uf.getAclScope() == AclScope.SELFCOMPANY && uCompanyId.equals(companyId))
				return true;
			if(uf.getAclScope() == AclScope.SOMECOMPANY && inScope(uCompanyId,uf.getCompanyIds()))
				return true;
		}else{
			if(uf.getAclScope() == AclScope.ALL)
				return true;
			if(uf.getAclScope() == AclScope.SOMEUNIT && unitInScope(uUnitId,uf.getUnitIds()))
				return true;
		}
		return false;
	}
	
	private boolean inScope(Long id, String scopeStr){
		return inScope(id.toString(), scopeStr);
	}
	
	private boolean inScope(String id, String scopeStr){
		if(StringUtils.isEmpty(id) || StringUtils.isEmpty(scopeStr))
			return false;
		
		String[] arr = scopeStr.split(",");
		for(int i=0; i < arr.length; i++){
			if(arr[i].equals(id))
				return true;
		}
		return false;
	}
	
	private boolean unitInScope(Long id, String scopeStr){
		if(id == null || StringUtils.isEmpty(scopeStr))
			return false;
		
		Unit unit = unitService.load(id);
		
		String[] arr = scopeStr.split(",");
		for(int i=0; i < arr.length; i++){
			Unit pUnit = unitService.load(Long.parseLong(arr[i]));
			if(pUnit.getId().equals(unit.getId()) )
				return true;
			if(unitService.isAncestorUnit(pUnit, unit))
				return true;
		}
		return false;
	}

}
