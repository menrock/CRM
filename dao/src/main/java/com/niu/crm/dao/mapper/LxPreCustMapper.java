package com.niu.crm.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.PreCustSearchForm;
import com.niu.crm.model.LxPreCust;
import com.niu.crm.vo.LxPreCustVO;

public interface LxPreCustMapper {
	
	int insert(LxPreCust preCust);
	
	int update(LxPreCust preCust);
	
	int updateByPrimaryKeySelective(LxPreCust preCust);
	
	int updateContactDate(@Param("cstmId")Long cstmId, @Param("lastContactDate")Date lastContactDate, @Param("contactCount") Integer contactCount);
		
	int delete(Long id);
	
	Long selectCstmIdById(Long id);
	
	LxPreCust selectByPrimaryKey(Long id);
	LxPreCust selectByCstmId(@Param("cstmId")Long cstmId);
	
	int countPreCust(@Param("params") PreCustSearchForm form);
	
	List<LxPreCustVO> queryPreCust(@Param("params") PreCustSearchForm form, @Param("pager")Pageable pageable);
	
	List<LxPreCustVO> queryRepeat(@Param("excludeCstmId")Long excludeCstmId, @Param("phones")String[] phones);
	
	Integer changeCompany(@Param("fromCompanyId")Long fromCompanyId, @Param("toCompanyId")Long toCompanyId);
	
	Integer changeStufrom(@Param("fromId")Long fromId, @Param("toId")Long toId);
}
