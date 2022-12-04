package com.niu.crm.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.LxContractTranApplySearchForm;
import com.niu.crm.model.LxContractTranApply;
import com.niu.crm.vo.LxContractTranApplyVO;

public interface LxContractTranApplyMapper {
	
	int insert(LxContractTranApply apply);
	
	int update(LxContractTranApply apply);
	
	int submit(LxContractTranApply apply);

	int approve(LxContractTranApply apply);

	int delete(Long id);
	
	LxContractTranApplyVO selectByPrimaryKey(Long id);

	List<LxContractTranApplyVO> selectByLxConId(Long lxConId);
	
	int countApply(@Param("params")LxContractTranApplySearchForm form);
	
	List<LxContractTranApplyVO> queryApply(@Param("params")LxContractTranApplySearchForm form, @Param("pager")Pageable pageable);
	
	
}
