package com.niu.crm.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.form.ContactRecordSearchForm;
import com.niu.crm.model.User;
import com.niu.crm.model.LxContactRecord;
import com.niu.crm.model.type.CallbackType;
import com.niu.crm.model.type.ContactStatus;
import com.niu.crm.service.LxContactRecordService;
import com.niu.crm.service.LxCustomerService;
import com.niu.crm.service.UserService;

@Controller
@RequestMapping(value = "/lx/contactRecord")
public class LxContactRecordController extends BaseController{
	@Autowired
	private LxContactRecordService contactRecordService;
	@Autowired
	private LxCustomerService lxCustomerService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/listByStu")
    @ResponseBody
    public List<LxContactRecord> listByStu(Long stuId) {
		ContactRecordSearchForm form = new ContactRecordSearchForm();
		form.setStuId(stuId);
		Sort sort = new Sort(Sort.Direction.DESC, "contact_date", "created_at");
		Pageable pager = new PageRequest(0, 200, sort);
		
		List<LxContactRecord> ls = contactRecordService.query(form, pager);
		for(LxContactRecord record:ls){
			if(record.getGwId() == null)
				continue;
			record.setGwName( userService.load(record.getGwId()).getName() );
		}
		return ls;
	}
	
	@RequestMapping(value = "/save")
    @ResponseBody
    public ResponseObject<LxContactRecord> save(HttpServletRequest request, LxContactRecord record){
		User user = this.getCurrentUser();
		if( StringUtils.isEmpty(record.getContactType()) )
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"联系方式必须录入");
		if(record.getCallbackType() == null)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"回访类型必须录入");
		
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
			
			String stuLevel = request.getParameter("stuLevel");
			if(StringUtils.isNotBlank(stuLevel)){
				Long levelId = Long.parseLong(stuLevel);
				lxCustomerService.updateStuZxgwLevel(user, record.getGwId(), record.getStuId(), levelId);
			}			
		}else{
			contactRecordService.update(user, record);
		}
		
		return new ResponseObject<LxContactRecord>(record);
	}
	
	@RequestMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseObject<Boolean> delete(@PathVariable("id") Long id){
		User user = this.getCurrentUser();
		contactRecordService.delete(user, id);
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	public static void main(String... args){
		System.out.println("hello");
	}
}
