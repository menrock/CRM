package com.niu.crm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.service.BaseService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.WechatService;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.core.error.BizException;
import com.niu.crm.dao.mapper.LxCustomerMapper;
import com.niu.crm.dao.mapper.LxPreCustMapper;
import com.niu.crm.dao.mapper.UnitMapper;
import com.niu.crm.model.Unit;

@Service
public class UnitServiceImpl extends BaseService implements UnitService {
	private Map<Long, Unit> cacheById = null;
	private Map<String, Unit> cacheByCode = null;

	@Autowired
	private UnitMapper unitMapper;

	@Autowired
	private LxCustomerMapper lxCustomerMapper;
	@Autowired
	private LxPreCustMapper lxPreCustMapper;
	
	@Autowired
	private WechatService wechatService;

	@Override
	public void refrechCache() {
		cacheById = null;
		cacheByCode = null;
	}

	private void refreshCache(Long id, String code) {
		if (cacheById == null) {
			cacheById = new HashMap<Long, Unit>();
			cacheByCode = new HashMap<String, Unit>();
		}

		Unit unit = null;
		if (null != id)
			unit = unitMapper.selectById(id);
		else
			unit = unitMapper.selectByCode(code);

		if (unit == null) {
			getLogger().error("unit not found id=" + id + " code=" + code);
		} else {
			cacheById.put(unit.getId(), unit);
			cacheByCode.put(unit.getCode(), unit);
		}
	}

	@Override
	public Unit load(Long id) {
		return load(id, null);
	}

	@Override
	public Unit loadByCode(String code) {
		return load(null, code);
	}

	private Unit load(Long id, String code) {
		Unit unit = null;
		if (null != cacheById) {
			if (null != id)
				unit = cacheById.get(id);
			else
				unit = cacheByCode.get(code);
		}

		if (unit == null) {
			refreshCache(id, code);
			if (null != id)
				unit = cacheById.get(id);
			else
				unit = cacheByCode.get(code);
		}
		if (unit.getParentId() == null) {
			unit.setCompanyId(null);
		} else {
			Unit pUnit = load(unit.getParentId());
			if (pUnit.getParentId() == null)
				unit.setCompanyId(unit.getId());
		}

		return unit;
	}

	@Override
	public int add(Unit unit) {
		Unit pUnit = unitMapper.selectById(unit.getParentId());
		int count = unitMapper.insert(unit);

		unit.setCode(pUnit.getCode() + "." + unit.getAlias() );
		if (pUnit.getParentId() == null)
			unit.setCompanyId(unit.getId());
		else
			unit.setCompanyId(pUnit.getCompanyId());

		update(unit);

		return count;
	}

	@Override
	public void delete(Long id) {
		Unit unit = this.load(id);
		List<Unit> ls = this.loadChildren(id);
		if(ls.size() == 0){
			unitMapper.delete(id);
			wechatService.deleteUnit(unit);
		}
	}

	@Override
	public int update(Unit unit) {
		Unit oldUnit = unitMapper.selectById(unit.getId());
		Unit pUnit = unitMapper.selectById(unit.getParentId());
		
		unit.setCode(pUnit.getCode() + "." + unit.getAlias() );
		unit.setWxUnitId(oldUnit.getWxUnitId());
		
		if (pUnit.getParentId() == null)
			unit.setCompanyId(unit.getId());
		else
			unit.setCompanyId(pUnit.getCompanyId());

		int count = unitMapper.update(unit);

		refreshCache(unit.getId(), null);
		
		if(unit.getWxUnitId() !=null)
			wechatService.updateUnit(unit);
		else
			wechatService.createUnit(unit);
		
		return count;
	}

	@Override
	public List<Unit> loadChildren(Long parentId) {
		return unitMapper.selectChildren(parentId);
	}

	@Override
	public List<Unit> getAllCompany() {
		Unit root = this.loadByCode("root");
		return unitMapper.selectChildren(root.getId());
	}

	@Override
	public JSONArray getJSONTree(String parentCode) {
		/*
		 * if(tree == null) loadJSONTree();
		 * 
		 * return tree;
		 */
		return loadJSONTree(parentCode);
	}

	@Override
	public JSONArray loadJSONTree(String parentCode) {
		Long parentId = null;
		if (!StringUtils.isEmpty(parentCode)) {
			Unit unit = this.loadByCode(parentCode);
			parentId = unit.getId();
		}
		return childTree(loadChildren(parentId), true);
	}

	/**
	 * @param children
	 * @param topLevel
	 *            是否是tree第一层
	 * @return
	 */
	private JSONArray childTree(List<Unit> children, boolean topLevel) {
		JSONArray jTree = new JSONArray();
		if (children == null || children.size() == 0)
			return jTree;

		for (int i = 0; i < children.size(); i++) {
			Unit unit = children.get(i);
			JSONObject node = new JSONObject();
			node.put("id", unit.getId());
			node.put("text", unit.getName());

			jTree.add(node);

			List<Unit> ls = loadChildren(unit.getId());
			if (ls.size() > 0) {
				node.put("state", "closed");
				node.put("children", childTree(ls, false));
			}
		}

		return jTree;
	}
	
	@Override
	public String getName(Long id) {
		Unit unit = this.load(id);
		if (null == unit)
			return null;
		else
			return unit.getName();
	}

	@Override
	public String getFullName(Long id) {
		Unit unit = this.load(id);
		if (null == unit)
			return null;
		if (null == unit.getParentId())
			return unit.getName();

		String szName = unit.getName();
		while (unit.getParentId() != null) {
			unit = this.load(unit.getParentId());
			if (unit.getParentId() == null)
				break;
			else
				szName = unit.getName() + "-" + szName;
		}
		return szName;
	}
	

	@Override
	public boolean isAncestorUnit(Unit ancestorUnit, Unit unit){	
		if(unit.getParentId() == null)
			return false;
		
		Long id = ancestorUnit.getId();		
		Unit pUnit = this.load(unit.getParentId());
		while(pUnit !=null){
			if(pUnit.getId().equals(id))
				return true;
			
			if(pUnit.getParentId() == null)
				break;
			pUnit = this.load(pUnit.getParentId());
		}
		return false;
	}

	@Transactional
	@Override
	public void mergeTo(Long fromUnitId, Long toUnitId){
		
	}
	
	@Transactional
	@Override
	public void moveTo(Long fromUnitId, Long toUnitId){
		if(fromUnitId == null || toUnitId == null)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "参数不能为空");
		if(fromUnitId.equals(toUnitId))
			return;
				
		Unit fromUnit = load(fromUnitId);
		Unit toUnit = load(toUnitId);
		
		if(isAncestorUnit(fromUnit, toUnit))
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "上级部门不能迁移到下级部门");
		
		Long oldCompanyId = fromUnit.getCompanyId();
		Long newCompanyId = null;
		
		fromUnit.setParentId(toUnitId);
		this.update(fromUnit);
		newCompanyId = fromUnit.getCompanyId();
		
		if(!newCompanyId.equals(oldCompanyId)){
			lxCustomerMapper.changeCompany(oldCompanyId, newCompanyId);
			lxPreCustMapper.changeCompany(oldCompanyId, newCompanyId);
		}
		updateChildCode(fromUnit);
	}
	
	/**
	 * 根据父级code更新code
	 * @param pUnit
	 */
	private void updateChildCode(Unit pUnit){
		List<Unit> ls = this.loadChildren(pUnit.getId());
		for(Unit unit:ls){
			this.update(unit);
			updateChildCode(unit);
		}
	}
}
