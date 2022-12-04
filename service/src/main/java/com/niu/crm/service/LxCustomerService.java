package com.niu.crm.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.model.Customer;
import com.niu.crm.model.CustomerRecommend;
import com.niu.crm.model.LxCustAssign;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.LxStuLevel;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.User;
import com.niu.crm.util.CsvUtil;
import com.niu.crm.vo.LxCustomerVO;
import com.niu.crm.vo.LxCustomerZxgwVO;
import com.niu.crm.vo.MyCustomerVO;
import com.niu.crm.vo.RepeatCustVO;

public interface LxCustomerService {
	
	LxCustomer load(Long id);
	
	LxCustomer loadByCstmId(Long cstmId);
	
	LxCustomerVO loadVO(Long id, Long cstmId);
	
	
	void add(User user, Customer customer, LxCustomer lxCustomer);
	
	void update(User user, Customer customer, LxCustomer lxCustomer);
	
	int delete(User user, Long id);
    
    int countStudents(StudentSearchForm form);
	
    List<LxCustomerVO> queryStudents(StudentSearchForm form, Pageable pageable);
    
    /**
     * 留学库中 重复客户查询
     * @param exclueCstmId  不检查cstmId=此值的客户 
     * @param phones
     * @return
     */
    List<RepeatCustVO> queryRepeat(Long excludeCstmId,	String... phones);
    
    /**
     * 留学库和种子库中 重复客户查询
     * @param exclueCstmId  不检查cstmId=此值的客户 
     * @param phones
     * @return
     */
    List<RepeatCustVO> queryRepeat2(Long excludeCstmId,	String... phones);

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
    
    //导入客户数据(返回count, msg)	
	Map<String,String> importCustomer(User user, Long unitId, Long stuFromId, MultipartFile file) throws IOException,Exception;
	
    //更新签约日期	
	void updateSignDate(Long cstmId) ;
	
	List<LxStuLevel> loadStuLevelByStuId(Long stuId);	

	List<LxCustAssign> loadCustAssignByStuId(Long stuId);
	
	int countStuZxgw(StudentSearchForm form) ;

	List<LxCustomerZxgwVO> queryStuZxgw(StudentSearchForm form, Pageable pageable);
}
