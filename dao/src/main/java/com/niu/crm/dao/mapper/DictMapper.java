package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.DictSearchForm;
import com.niu.crm.model.Dict;

public interface DictMapper {
	
	int insert(Dict dict);
	
	int update(Dict dict);
	
	int delete(Long id);
	
	Dict selectById(Long id);
	
	Dict selectByCode(String code);

	List<Dict> selectByName(@Param("codePrefix")String codePrefix, @Param("dictName")String dictName);

	List<Dict> selectChildren(@Param("parentId") Long parentId);

	List<Dict> selectChildrenByCode(@Param("parentCode") String parentCode);
	
	int queryCount(@Param("params")DictSearchForm form);
	
	List<Dict> query(@Param("params")DictSearchForm form, @Param("pager")Pageable pageable);

}
