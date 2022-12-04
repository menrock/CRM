package com.niu.crm.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.dto.ProjectContractDTO;
import com.niu.crm.form.ProjectContractSearchForm;
import com.niu.crm.model.ProjectContract;

public interface ProjectContractMapper {
	
	int insert(ProjectContract contract);
	
	int update(ProjectContract contract);
	
	int updateSk(ProjectContract contract);
	
	int delete(Long id);
	
	ProjectContract selectByPrimaryKey(@Param("id")Long id);
	
	ProjectContract selectByConId(@Param("conId")Long conId);
	
	Date selectFirstSignDate(Long cstmId);
	
	int countContract(@Param("params")ProjectContractSearchForm form);
	
	List<ProjectContractDTO> queryContract(@Param("params")ProjectContractSearchForm form, @Param("pager")Pageable pageable);
	
	ProjectContractDTO statContract(@Param("params")ProjectContractSearchForm form);
}
