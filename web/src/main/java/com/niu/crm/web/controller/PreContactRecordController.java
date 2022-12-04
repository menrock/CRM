package com.niu.crm.web.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.form.PreContactRecordSearchForm;
import com.niu.crm.model.PreContactRecord;
import com.niu.crm.model.User;
import com.niu.crm.service.LxPreCustService;
import com.niu.crm.service.PreContactRecordService;
import com.niu.crm.service.UserService;

@Controller
@RequestMapping(value = "/pre/contactRecord")
public class PreContactRecordController extends BaseController {
	@Autowired
	private PreContactRecordService contactRecordService;
	
	@Autowired
	private LxPreCustService lxPreCustService;

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/listByCstm")
    @ResponseBody
    public List<PreContactRecord> listByCstm(Long cstmId) {
		PreContactRecordSearchForm form = new PreContactRecordSearchForm();
		form.setCstmId(cstmId);
		Sort sort = new Sort(Sort.Direction.DESC, "contact_date", "created_at");
		Pageable pager = new PageRequest(0, 200, sort);
		
		List<PreContactRecord> ls = contactRecordService.query(form, pager);
		for(PreContactRecord record:ls){
			if(record.getGwId() == null)
				continue;
			record.setGwName( userService.load(record.getGwId()).getName() );
		}
		return ls;
	}
	
	@RequestMapping(value = "/save")
    @ResponseBody
    public ResponseObject<PreContactRecord> save(HttpServletRequest request, PreContactRecord record){
		User user = this.getCurrentUser();
		if( StringUtils.isEmpty(record.getContactType()) )
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"联系方式必须录入");
		
		String szNextDate = request.getParameter("nextDate");
		if(StringUtils.isNotBlank(szNextDate)){
			SimpleDateFormat df = null;
		
			if(szNextDate.length() ==10)
				df = new SimpleDateFormat("yyyy-MM-dd");
			else
				df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			try{
				record.setNextDate(df.parse(szNextDate));
			}catch(Exception e){
				
			}
		}
		
		if(record.getId() == null){
			if(record.getContactDate() == null)
				record.setContactDate(new java.util.Date());
			contactRecordService.add(user, record);
					
		}else{
			contactRecordService.update(user, record);
		}
		
		return new ResponseObject<>(record);
	}
}
