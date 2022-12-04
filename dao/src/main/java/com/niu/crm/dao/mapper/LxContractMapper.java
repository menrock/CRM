package com.niu.crm.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.dto.LxContractDTO;
import com.niu.crm.form.LxContractSearchForm;
import com.niu.crm.model.LxContract;

public interface LxContractMapper {
	
	int insert(LxContract contract);
	
	int update(LxContract contract);
	
	int updateSk(@Param("conId")Long conId, @Param("contract")LxContract contract);
	
	//转后期(第二阶段)
	int updateTran2Second(@Param("params")LxContract contract);
	
    /**
     * 更新顾问信息
     * @param contract
     * @return
     */
	int updateGuwen(@Param("params")LxContract contract);
	
	int delete(Long id);
	
	LxContract selectByPrimaryKey(@Param("id")Long id);
	
	LxContract selectByConId(@Param("conId")Long conId);
	
	LxContract selectByConNo(@Param("conNo")String conNo);
	
	List<LxContract> selectByCstmId(@Param("cstmId")Long cstmId);
	
	Date selectCustFirstSignDate(@Param("cstmId")Long cstmId);
	
	int countContract(@Param("params")LxContractSearchForm form);
	
	List<LxContractDTO> queryContract(@Param("params")LxContractSearchForm form, @Param("pager")Pageable pageable);
	
	LxContractDTO statContract(@Param("params")LxContractSearchForm form);
}
