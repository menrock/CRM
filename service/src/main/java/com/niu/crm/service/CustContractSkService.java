package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.ContractSearchForm;
import com.niu.crm.form.SkSearchForm;
import com.niu.crm.model.CustContractSk;
import com.niu.crm.model.User;
import com.niu.crm.vo.CustContractSkVO;
import com.niu.crm.vo.CustContractVO;

public interface CustContractSkService {
	
	CustContractSk load(Long id);

	List<CustContractSkVO> loadByConId(Long conId);
	
	int batchAdd(User user, List<CustContractSk> skRecords);
	
	int add(User user, CustContractSk skRecord);
    
    void delete(User user, Long id);

    int update(User user, CustContractSk skRecord);
    
    int fixAchivement(CustContractSk skRecord);
    
    /**
     * 更新留学合同的 收款金额及首次收款时间
     * @param conId  t_lx_contract.con_id
     */
    void refreshLxContractSk(Long conId);
    
    int countSk(SkSearchForm form);
    
    CustContractSkVO queryStat(SkSearchForm form);
	
    List<CustContractSkVO> querySk(SkSearchForm form, Pageable pageable);
}
