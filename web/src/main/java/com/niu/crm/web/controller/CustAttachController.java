package com.niu.crm.web.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.dto.LxContractDTO;
import com.niu.crm.form.ContractSearchForm;
import com.niu.crm.form.CustAttachSearchForm;
import com.niu.crm.form.LxContractSearchForm;
import com.niu.crm.model.CaseXcclAttach;
import com.niu.crm.model.CustAttach;
import com.niu.crm.model.CustContract;
import com.niu.crm.model.CustContractLine;
import com.niu.crm.model.Customer;
import com.niu.crm.model.Dict;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.Payment;
import com.niu.crm.model.User;
import com.niu.crm.model.type.AclScope;
import com.niu.crm.model.type.CustAttachType;
import com.niu.crm.model.type.CustContractStatus;
import com.niu.crm.service.CustAttachService;
import com.niu.crm.service.CustContractService;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.LxContractService;
import com.niu.crm.service.LxCustomerService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.impl.OssServiceImpl;
import com.niu.crm.vo.CustContractVO;
import com.niu.crm.vo.LxCustomerVO;
import com.niu.crm.vo.PaymentVO;
import com.niu.crm.vo.UserFuncVO;

@Controller
@RequestMapping(value = "/cust/attach")
public class CustAttachController extends BaseController{
	@Autowired
	private DictService dictService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private LxCustomerService lxCustomerService;
	@Autowired
	private CustContractService contractService;
	@Autowired
	private LxContractService lxContractService;
	@Autowired
	private CustAttachService attachService;
	@Autowired
	private UserFuncService userFuncService;
	@Autowired
	private OssServiceImpl ossService;
	
	@RequestMapping(value = "/listByContract.do")
    public String listByContract(Long conId, ModelMap model){
		User user = this.getCurrentUser();
		CustContract contract = contractService.load(conId);
		model.put("conId", conId);
		model.put("contract", contract);
			
		return "cust/attach/listByContract";
	}
		
	@RequestMapping(value = "/listByCust.do")
    public String listByCust(Long id, String bizType, ModelMap model){
		User user = this.getCurrentUser();
		LxCustomer student = lxCustomerService.load(id);
		if(student != null){
			Customer cust = customerService.load(student.getCstmId());
			model.put("cstmName", cust.getName());
			
			LxContractSearchForm form = new LxContractSearchForm();
			form.setCstmId(student.getCstmId());
			//Pageable pager = new PageRequest(0,200);
			List<LxContractDTO> lsContract = lxContractService.queryContract(form, null);
			model.put("contracts", lsContract);
		}
		
		model.put("student", student);
			
		return "cust/attach/listByCust";
	}
	
	@RequestMapping(value = "/delete.do")
	@ResponseBody
    public ResponseObject<Boolean> delete(
    		HttpServletRequest request,
    		Long id){
		User user = this.getCurrentUser();
		
		
		try{
			CustAttach attach = attachService.load(id);
			if(attach == null)
				return new ResponseObject<>(Boolean.FALSE);
			
			Boolean rc = attachService.delete(user, id);
			return new ResponseObject<>(rc);
			
		}catch(Exception e){
			logger.error("",e);
			return new ResponseObject<>(Boolean.FALSE);
		}
		
	}
	
	@RequestMapping(value = "/upload.do")
	@ResponseBody
    public ResponseObject<CustAttach> upload(
    		HttpServletRequest request,
    		@RequestParam(value = "file") MultipartFile file, 
    		@RequestParam(value = "attachType") CustAttachType attachType,
    		@RequestParam(value = "cstmId") Long cstmId,
    		@RequestParam(value = "conId") Long conId,
    		@RequestParam(value = "desc") String desc){
		User user = this.getCurrentUser();
		String fileName = (file==null)?null:file.getOriginalFilename().toLowerCase();
		
    	String fileExt = ossService.getFileExt(fileName);
		CustAttach attach = new CustAttach();
		
		if(conId !=null){
			CustContract contract = contractService.load(conId);
			cstmId = contract.getCstmId();
		}
		
		try{
			String ossKey = ossService.putObject(file);
			
			attach.setAttachType(attachType);
			attach.setCstmId(cstmId);
			attach.setConId(conId);
			attach.setAttachDesc(desc);
			attach.setAttachName(fileName);
			attach.setFileExt(fileExt);
			attach.setOssKey(ossKey);
			
			attachService.save(user, attach);
			
		}catch(Exception e){
			logger.error("",e);
		}
		
		return new ResponseObject<>(attach);
	}
	
	@RequestMapping(value = "/download.do")
    public void download( HttpServletResponse response, Long id){
		User user = this.getCurrentUser();
		CustAttach attach = attachService.load(id);
		String ossKey = attach.getOssKey();
		String fileExt = attach.getFileExt()==null?"":attach.getFileExt();
		
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ attach.getAttachName() + "\"");
		//response.setContentType("application/octet-stream; charset=UTF-8");
		if(fileExt.equals(".gif"))
			response.setContentType("image/gif");
        else if( fileExt.equals(".jpg") || fileExt.equals(".jpeg") || fileExt.equals(".jpe") )
        	response.setContentType("image/jpeg");
        else if(fileExt.equals(".png"))
        	response.setContentType("image/png");
		
		try {
			java.io.OutputStream os = response.getOutputStream();
			ossService.download(ossKey, os);
		} catch (Exception e) {
			logger.error("id=" + id, e);
		}
	}
	
	@RequestMapping(value = "/custAttachData.do")
	@ResponseBody
	public Map<String, Object> custAttachData(HttpServletRequest req, CustAttachSearchForm form){
		User user = this.getCurrentUser();
		List<CustAttach> ls = null;
		
		Pageable pager = new PageRequest(0, 50000);
		
		Map<String,Object> map = new HashMap<String,Object>();	
		
		if(form.getCstmId() == null && form.getConId() ==null){
			ls = new ArrayList<>();
		}else{
			ls = attachService.queryAttach(form, pager);
		}
		
		map.put("total", ls.size());
		map.put("rows", ls);
		
		return map;
	}
	
	
	public static void main(String... args){
		System.out.println("hello");
	}
}
