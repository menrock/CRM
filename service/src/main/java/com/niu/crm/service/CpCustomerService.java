package com.niu.crm.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.model.Customer;
import com.niu.crm.model.CustAssign;
import com.niu.crm.model.CpCustomer;
import com.niu.crm.model.StuLevel;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.User;
import com.niu.crm.vo.CpCustomerVO;
import com.niu.crm.vo.CpCustomerZxgwVO;
import com.niu.crm.vo.RepeatCustVO;

public interface CpCustomerService {
	
	CpCustomer load(Long id);
	
	CpCustomer loadByCstmId(Long cstmId);
	
	CpCustomerVO loadVO(Long id, Long cstmId);
	
	void add(User user, Customer customer, CpCustomer lxCustomer);
	
	void update(User user, Customer customer, CpCustomer lxCustomer);
	
	int delete(User user, Long id);
    
    int countStudents(StudentSearchForm form);
	
    List<CpCustomerVO> queryStudents(StudentSearchForm form, Pageable pageable);
    
    /**
     * 培训库中 重复客户查询
     * @param exclueCstmId  不检查cstmId=此值的客户 
     * @param phones
     * @return
     */
    List<RepeatCustVO> queryRepeat(Long excludeCstmId,	String... phones);
    
    //顾问评级
    void updateStuZxgwLevel(User user, Long zxgwId, Long stuId, Long levelId);
    
    /**
	 * 修复该客户的最高评级
	 * @param stuId 学生id
	 */
    void fixStuLevel(Long stuId);
    
    //分配给咨询顾问
    void assign2Zxgw(User user, Long stuId, Long zxgwId);

    //咨询顾问退回
    void revokeAssignFromZxgw(User user, Long stuId, Long zxgwId);
    
    //更新签约日期	
	void updateSignDate(Long cstmId) ;

	List<StuZxgw> loadStuZxgwByStuId(Long stuId);
	StuZxgw loadStuZxgw(Long stuId, Long zxgwId);
	
	List<StuLevel> loadStuLevelByStuId(Long stuId);	

	List<CustAssign> loadCustAssignByStuId(Long stuId);
	
	int countStuZxgw(StudentSearchForm form) ;

	List<CpCustomerZxgwVO> queryStuZxgw(StudentSearchForm form, Pageable pageable);
}
