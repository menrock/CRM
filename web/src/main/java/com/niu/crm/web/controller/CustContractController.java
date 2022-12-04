package com.niu.crm.web.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.model.CustContractArchive;
import com.niu.crm.model.User;
import com.niu.crm.service.CustContractService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;

@Controller
@RequestMapping(value = "/contract")
public class CustContractController extends BaseController{
	@Autowired
	private CustContractService archiveService;
	@Autowired
	private UserFuncService userFuncService;
	@Autowired
	private UnitService unitService;
	
	/**
	 * 合同归档
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/archive")
    @ResponseBody
    public ResponseObject<Boolean> archive(Long id){
		User user = this.getCurrentUser();
		CustContractArchive archive = archiveService.loadArchive(id);
		if(archive !=null)
			return new ResponseObject<>(Boolean.FALSE);
				
		archive = new CustContractArchive();
		archive.setConId(id);
		archive.setArchiveDate(new Date() );
		archiveService.archive(user, archive);
		
		return new ResponseObject<>(Boolean.TRUE);
	}
}
