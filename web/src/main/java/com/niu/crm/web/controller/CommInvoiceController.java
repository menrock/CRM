package com.niu.crm.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.form.CommInvoiceSearchForm;
import com.niu.crm.model.CommInvoice;
import com.niu.crm.model.User;
import com.niu.crm.model.UserFunc;
import com.niu.crm.model.type.CommInvoiceStatus;
import com.niu.crm.service.CommInvoiceService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.UserFuncService;

@Controller
@RequestMapping(value = "/commission")
public class CommInvoiceController extends BaseController {
	@Autowired
	private DictService dictService;
	@Autowired
	private CommInvoiceService invoiceService;
	@Autowired
	private UserFuncService userFuncService;
	
	@RequestMapping(value = "/invList.do")
    public String invList(Boolean singlePage, ModelMap model) throws IOException{
		User user = this.getCurrentUser();
		
		if (singlePage == null || singlePage == false)
			model.put("singlePage", Boolean.FALSE);
		else
			model.put("singlePage", Boolean.TRUE);
		

		UserFunc inputFunc = userFuncService.loadByCode(user.getId(), "comm_inv_input");
		if(inputFunc !=null)
			model.put("canInputInvoice", Boolean.TRUE);
		else
			model.put("canInputInvoice", Boolean.FALSE);		
		
		model.put("statusList", CommInvoiceStatus.values());
		
		return "commission/invList";
	}
	
	@RequestMapping(value = "/invListData.do")
    @ResponseBody
    public Map<String, Object> invListData(HttpServletRequest req, CommInvoiceSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		
		if(StringUtils.isEmpty(form.getObjectName()))
			form.setObjectName(null);
		else
			form.setObjectName("%" + form.getObjectName() + "%");

		Map<String,Object> map = new HashMap<String,Object>();	
		
		int total = invoiceService.countInvoice(form);
		List<CommInvoice> ls = invoiceService.queryInvoice(form, pager);
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	@RequestMapping(value = "/saveInvoice.do")
    @ResponseBody
    public ResponseObject<Boolean> saveInvoice(CommInvoice invoice){
		User user = this.getCurrentUser();
		if(invoice.getId() !=null){
			CommInvoice oldInv = invoiceService.load(invoice.getId());
		}
		
		if(invoice.getId() !=null){
			invoiceService.update(invoice);
		}else{
			invoice.setStatus(CommInvoiceStatus.DRAFT);
			invoice.setCreatorId(user.getId());
			invoiceService.add(invoice);
		}
		return new ResponseObject<>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/invoiceInfo.do")
    @ResponseBody
    public ResponseObject<CommInvoice> invoiceInfo(Long id){
		CommInvoice invoice = invoiceService.load(id);
		
		return new ResponseObject<>(invoice);
	}
}
