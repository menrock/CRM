package com.niu.crm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.service.BaseService;
import com.niu.crm.service.DictService;
import com.niu.crm.vo.DictVO;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.dao.mapper.DictMapper;
import com.niu.crm.dao.mapper.LxCustomerMapper;
import com.niu.crm.dao.mapper.LxPreCustMapper;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.Dict;

@Service
public class DictServiceImpl extends BaseService implements DictService {
	private Map<Long,Dict> cacheById = null;
	private Map<String,Dict> cacheByCode = null;
	
    @Autowired
    private DictMapper  dictMapper;
    @Autowired
    private LxCustomerMapper  lxCustomerMapper;
    @Autowired
    private LxPreCustMapper  lxPreCustMapper;
	
    @Override
   	public void refrechCache(){
    	cacheById = null;
    	cacheByCode = null;
    }
    
    private void refreshCache(Long id, String code){
    	if( cacheById == null){
    		cacheById = new HashMap<Long, Dict>();
    		cacheByCode = new HashMap<String, Dict>();
    	}
    	if(id==null && StringUtils.isBlank(code))
    		return;
    	
    	Dict dict = null;
    	if(null != id )
    		dict = dictMapper.selectById(id);
    	else 
    		dict = dictMapper.selectByCode(code);
    	
    	if(dict == null)
    		this.getLogger().error("dict not found id=" + id + " code=" + code);
    	
    	if( dict != null){
    		cacheById.put(dict.getId(), dict);
    		cacheByCode.put(dict.getDictCode(), dict);
    	}
    }
    
    @Override
	public DictVO dict2VO(Dict dict){
		DictVO vo = new DictVO();
		BeanUtils.copyProperties(dict, vo);
		String alias = getAliasFromCode( dict.getDictCode() );
		vo.setAlias( alias );
		
		return vo;
	}
    
    @Override
	public Dict load(Long id){
		return load(id,null);
	}
    
    @Override
	public Dict loadByCode(String code){
    	return load(null, code);
	}
    
    private Dict load(Long id, String code){
    	Dict dict = null;
    	if( null != cacheById ){
    		if(null != id )
        		dict = cacheById.get(id);
        	else 
        		dict = cacheByCode.get(code); 
    	}
    	
    	if(dict == null){
    		refreshCache(id, code);
    		if(null != id )
        		dict = cacheById.get(id);
        	else 
        		dict = cacheByCode.get(code);  
    	}
    	
    	return dict;
    }
	
    private void autoAlias(DictVO dict){     	
    	Dict pDict = load(dict.getParentId());
    	List<Dict> ls = this.loadChildren(dict.getParentId());
    	java.util.Set<String> set = new java.util.HashSet<String>();
    	for(Dict item:ls){
    		if(dict.getId() !=null && dict.getId() == item.getId())
    			continue;
    		
    		set.add(item.getDictCode());
    	}
    	java.text.DecimalFormat df = new java.text.DecimalFormat("00");
    	for(int i=1; i < 1000; i++){
    		String s = pDict.getDictCode() + "." + df.format(i);
    		if(!set.contains(s)){
    			dict.setAlias(df.format(i));
    			break;
    		}
    		
    	}
    }
    
    @Override
	public int add(User user, DictVO dict){
    	dict.setCreatorId(user.getId());
    	
    	if( StringUtils.isEmpty(dict.getAlias()) ){
    		autoAlias(dict);
    	}
		Dict pDict = load(dict.getParentId());			
		dict.setDictCode(pDict.getDictCode() + "." + dict.getAlias() );
		
    	if(dict.getShowIndex() == null)
    		dict.setShowIndex(99L);
    	
		return dictMapper.insert(dict);
	}
    
    @Override
	public void delete(User user, Long id){
		dictMapper.delete(id);
	}
    
    @Override
	public int update(User user, DictVO dict){
    	String oldCode = load(dict.getId()).getDictCode();
    	
    	if( StringUtils.isEmpty(dict.getAlias()) ){
    		autoAlias(dict);
    	}
    	if(dict.getParentId() == null){
    		dict.setDictCode( dict.getAlias() );
    	}
    	else
		{
			Dict pDict = load(dict.getParentId());
			dict.setDictCode(pDict.getDictCode() + "." + dict.getAlias());
		}
		int count = dictMapper.update(dict);		
		refreshCache(dict.getId(),null);
		
		//如果修改了code 需要更新子类的code
		if(!dict.getDictCode().equals( oldCode))
			updateChildCode(user, dict);
		
		return count;
	}

    @Override
    public List<Dict> loadChildren(String parentCode){
    	Dict pDict = loadByCode(parentCode.toLowerCase()); 
    	if(pDict == null)
    		getLogger().error("dict not exists code=" + parentCode);
    	
    	return loadChildren(pDict.getId());
    }

    @Override
    public List<Dict> loadChildren(Long parentId){
    	List<Dict> ls = dictMapper.selectChildren(parentId);
    	
    	return ls;
    }
    
    @Override
    public List<Dict> selectByName(String codePrefix, String dictName){
    	return dictMapper.selectByName(codePrefix, dictName);
    }

    
    @Override
    public JSONArray getJSONTree(Long parentId){
    	/*
    	if(tree == null)
    		loadJSONTree();
    	
    	return tree; */
    	return loadJSONTree(parentId);
    }
    
    @Override
    public JSONArray loadJSONTree(Long parentId){
    	List<Dict> children = loadChildren(parentId);
    	
    	return childTree(children, true);
    }
    
    @Override
	public boolean isAncestorDict(Dict ancestorDict, Dict dict){	
		if(dict.getParentId() == null)
			return false;
		
		Long id = ancestorDict.getId();		
		Dict pDict = this.load(dict.getParentId());
		while(pDict !=null){
			if(pDict.getId().equals(id))
				return true;
			
			if(pDict.getParentId() == null)
				break;
			pDict = this.load(pDict.getParentId());
		}
		return false;
	}
    
    /**
     * 部门合并(fromId 合并到 toId)
     * @param fromId
     * @param toId
     */
    @Transactional
    @Override
    public void stuFromMergeTo(User user, Long fromId, Long toId){		
		Dict fromDict = load(fromId);
		Dict toDict = load(toId);
		Dict rootDict = loadByCode("stufrom");
				
		if( !isAncestorDict(rootDict, fromDict))
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "只允许对学生来源进行操作");
		if( !isAncestorDict(rootDict, toDict))
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "只允许对学生来源进行操作");
		
		List<Dict> children = loadChildren(fromId);
		for(Dict dict:children){
			this.stuFromMoveTo(user, dict.getId(), toId);
		}
		
		//更新客户来源
		lxCustomerMapper.changeStufrom(fromId, toId);
		lxPreCustMapper.changeStufrom(fromId, toId);
		
		this.delete(user, fromId);    	
    }
	
    /**
     * 客户来源迁移(fromId 迁移到 toId下)
     * @param fromId
     * @param toId
     */
    @Transactional
    @Override
    public void stuFromMoveTo(User user, Long fromId, Long toId){				
		Dict fromDict = load(fromId);
		Dict toDict = load(toId);
		Dict rootDict = loadByCode("stufrom");
				
		if( !isAncestorDict(rootDict, fromDict))
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "只允许对学生来源进行操作");
		if( !isAncestorDict(rootDict, toDict))
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "只允许对学生来源进行操作");

		this.moveTo(user, fromId, toId);
	}
    
    /**
     * 通用字典节点移动
     * @param user
     * @param fromId
     * @param toId
     */
    private void moveTo(User user, Long fromId, Long toId){
    	if(fromId == null || toId == null)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "参数不能为空");
		if(fromId.equals(toId))
			return;
				
		Dict fromDict = load(fromId);
		Dict toDict = load(toId);
			
		if(isAncestorDict(fromDict, toDict))
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "上级不能迁移到下级字典");
		
		DictVO fromVO = this.dict2VO(fromDict); 
		fromVO.setParentId(toId);
		fromVO.setAlias(null);
		
		this.update(user,fromVO);
	}
	
	/**
	 * 根据父级code更新code
	 * @param pUnit
	 */
	private void updateChildCode(User user, Dict pDict){
		List<Dict> ls = this.loadChildren(pDict.getId());
		for(Dict dict:ls){
			DictVO vo = this.dict2VO(dict);
			
			this.update(user, vo);
			
			
			updateChildCode(user,dict);
		}
	}
    
    /**
     * @param children
     * @param topLevel  是否是tree第一层
     * @return
     */
    private JSONArray childTree(List<Dict> children, boolean topLevel){
    	JSONArray jTree = new JSONArray();
    	if(children == null || children.size() ==0)
    		return jTree;
    	    	
    	for(int i=0; i < children.size(); i++){
    		Dict dict = children.get(i);
    		JSONObject node = new JSONObject();
    		node.put("id", dict.getId() );
    		node.put("code", dict.getDictCode() );
    		node.put("text", dict.getDictName() );
    		
    		
        	jTree.add(node);
        	
        	List<Dict> ls = loadChildren(dict.getId());
        	if(ls.size() >0){
        		node.put("state", "closed" );	
        		node.put("children", childTree(ls, false));
        	}
    	}    
    	return jTree;
    }
    
    private String getAliasFromCode(String dictCode){
    	if(StringUtils.isEmpty(dictCode))
    		return null;
    	
    	int idx = dictCode.lastIndexOf(".");
    	if(idx <0)
    		return dictCode;
    	else
    		return dictCode.substring(idx +1);
    	
    }
}
