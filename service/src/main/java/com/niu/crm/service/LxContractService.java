package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.model.LxContract;
import com.niu.crm.dto.LxContractDTO;
import com.niu.crm.form.LxContractSearchForm;
import com.niu.crm.model.User;

public interface LxContractService {
	
	LxContract load(Long id);
	
	LxContract loadByConNo(String conNo);

	LxContractDTO loadDTO(Long id);
	
	List<LxContract> loadByCstmId(Long id);
	
	int add(User user, LxContractDTO contract);
    
    void delete(User user, Long id);

    int update(User user, LxContractDTO contract);

    int updateSk(User user, LxContract contract);

    LxContractDTO statContract(LxContractSearchForm form);
    int countContract(LxContractSearchForm form);
    List<LxContractDTO> queryContract(LxContractSearchForm form, Pageable pageable);
}
