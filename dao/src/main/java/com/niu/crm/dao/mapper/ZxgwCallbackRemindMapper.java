package com.niu.crm.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.niu.crm.form.CallbackCheckForm;
import com.niu.crm.form.CallbackSearchForm;
import com.niu.crm.form.CallbackSimpleSearchForm;
import com.niu.crm.model.CallbackCheckDetail;
import com.niu.crm.model.CallbackCheckLine;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.StuZxgwEx;
import com.niu.crm.model.ZxgwCallbackRemind;
import com.niu.crm.vo.LxCustomerVO;
import com.niu.crm.vo.LxCustomerZxgwVO;
import com.niu.crm.vo.ZxgwCallbackRemindVO;

public interface ZxgwCallbackRemindMapper {
	
	int insert(ZxgwCallbackRemind record);
	
	int updateContactTime(ZxgwCallbackRemind record);
	
	ZxgwCallbackRemind selectById(Long id);

	int deleteByPrimaryKey(Long id);

	int delete(@Param("callbackType")String callbackType, Date endDate);
	
	/**
	 * 供删除咨询顾问时 删除 相关回访提醒
	 * @param zxgwId
	 * @return
	 */
	int deleteBySourceZxgw(@Param("stuId")Long stuId, @Param("sourceZxgwId")Long sourceZxgwId);
	
	Integer simpleCount(@Param("params") CallbackSimpleSearchForm form, @Param("pager")Pageable pageable);
	List<ZxgwCallbackRemind> simpleList(@Param("params") CallbackSimpleSearchForm form, @Param("pager")Pageable pageable);
	
	List<ZxgwCallbackRemindVO> queryRemind(@Param("params") CallbackSearchForm form, @Param("pager")Pageable pageable);
	Integer countRemind(@Param("params") CallbackSearchForm form);
	
	List<StuZxgwEx> queryStudentForRemind(@Param("params") CallbackSearchForm form, @Param("maxRows") Integer maxRows);
	
	List<CallbackCheckLine> checkReportStat(@Param("params") CallbackCheckForm form);
	
	List<CallbackCheckDetail> checkReportDetail(@Param("params") CallbackCheckForm form, @Param("pager")Pageable pageable);
}
