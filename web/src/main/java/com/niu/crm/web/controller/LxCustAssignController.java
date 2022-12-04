package com.niu.crm.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.io.IOUtils;

import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.User;
import com.niu.crm.model.UserFunc;
import com.niu.crm.service.LxCustAssignService;
import com.niu.crm.service.LxCustomerService;
import com.niu.crm.service.LxStuZxgwService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.LxCustAssignVO;
import com.niu.crm.vo.UserFuncVO;
import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;


@Controller
@RequestMapping(value = "/lx/custAssign")
public class LxCustAssignController extends BaseController{
	@Autowired
	private UserService userService;
	
	@Autowired
	private LxCustAssignService custAssignService;	
	@Autowired
	private LxStuZxgwService stuZxgwService;	
	@Autowired
	private LxCustomerService lxCustomerService;
	@Autowired
	private UserFuncService userFuncService;	
	
	/**
	 * 列出客户分配信息
	 * @return
	 */
	@RequestMapping(value = "/listByStu.do")
    @ResponseBody
    public ResponseObject<List<LxCustAssignVO>> listByStu(Long stuId){
		User user = this.getCurrentUser();

		List<LxCustAssignVO> ls = custAssignService.selectByStuId(stuId);
		
		return new ResponseObject<List<LxCustAssignVO>>(ls);
	}
	

	
	/**
	 * 客户分配
	 * @return
	 */
	@RequestMapping(value = "allocate.do")
    @ResponseBody                                             
    public ResponseObject<Boolean> assign(HttpServletRequest req,Long[] stuIds, @RequestParam("toZxgwId")Long toZxgwId){
		User user = this.getCurrentUser();

		UserFuncVO queryFunc = userFuncService.loadByCode(user.getId(), "lx_cust_assign");
		if(queryFunc == null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED, "权限不够");
		
		if(stuIds == null || toZxgwId == null)
			return new ResponseObject<Boolean>(Boolean.FALSE);
		
		for(Long stuId:stuIds){
			lxCustomerService.assign2Zxgw(user, stuId, toZxgwId);
		}
				
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	/**
	 * 撤销分配
	 * @return
	 */
	@RequestMapping(value = "revoke.do")
    @ResponseBody
    public ResponseObject<Boolean> revokeAssign(Long[] stuZxgwIds){
		User user = this.getCurrentUser();
		
		UserFuncVO queryFunc = userFuncService.loadByCode(user.getId(), "lx_cust_assign");
		if(queryFunc == null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED, "权限不够");
		
		for(Long id:stuZxgwIds){
			StuZxgw stuZxgw = stuZxgwService.load(id);
			Long stuId = stuZxgw.getStuId();
			
			lxCustomerService.revokeAssignFromZxgw(user, stuId, stuZxgw.getZxgwId());
		}

		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	/**
	 * 转分配
	 * @return
	 */
	@RequestMapping(value = "tranAssign.do")
    @ResponseBody
    public ResponseObject<Boolean> tranAssign(Long[] stuZxgwIds, Long toZxgwId){
		if(stuZxgwIds == null || toZxgwId == null)
			return new ResponseObject<Boolean>(Boolean.FALSE);
				
		User user = this.getCurrentUser();
		for(Long id:stuZxgwIds){
			StuZxgw stuZxgw = stuZxgwService.load(id);
			Long stuId = stuZxgw.getStuId();
			
			lxCustomerService.revokeAssignFromZxgw(user, stuId, stuZxgw.getZxgwId());
			lxCustomerService.assign2Zxgw(user, stuId, toZxgwId);
		}

		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
}
