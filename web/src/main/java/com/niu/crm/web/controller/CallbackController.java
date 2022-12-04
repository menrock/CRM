package com.niu.crm.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.model.Customer;
import com.niu.crm.model.Dict;
import com.niu.crm.model.LxContactRecord;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.StuZxgwEx;
import com.niu.crm.model.SysMessage;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.ZxgwCallbackRemind;
import com.niu.crm.model.ZxgwTeam;
import com.niu.crm.model.type.AclScope;
import com.niu.crm.model.type.CallbackRemindType;
import com.niu.crm.service.CountryService;
import com.niu.crm.service.LxContactRecordService;
import com.niu.crm.service.LxZxgwTeamService;
import com.niu.crm.service.StuPlanCountryService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.ZxgwCallbackRemindService;
import com.niu.crm.util.DateUtil;
import com.niu.crm.vo.LxCustomerVO;
import com.niu.crm.vo.LxCustomerZxgwVO;
import com.niu.crm.vo.UserFuncVO;
import com.niu.crm.vo.ZxgwCallbackRemindVO;
import com.niu.crm.form.CallbackSearchForm;
import com.niu.crm.form.ContactRecordSearchForm;
import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;

@Controller
@RequestMapping(value = "/callback")
public class CallbackController extends BaseController{
	@Autowired
	private DictService dictService;	
	@Autowired
	private CountryService countryService;
	@Autowired
	private LxZxgwTeamService zxgwTeamService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private UserService userService;	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ZxgwCallbackRemindService callbackRemindService;
	@Autowired
	private LxContactRecordService contactRecordService;
	@Autowired
	private UserFuncService userFuncService;
	@Autowired
	private StuPlanCountryService stuPlanCountryService;
	
	/**
	 * 我的回访提醒管理
	 * @return
	 */
	@RequestMapping(value = "/myRemindList.do")
    public String myRemindList(ModelMap model){

		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("xlList", dictService.loadChildren("xl") );
		model.addAttribute("gradeList", dictService.loadChildren("grade") );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("levelList", dictService.loadChildren("stulevel") );
		
		return "callback/myRemindList";
	}
	
	/**
	 * 回访提醒管理
	 * @return
	 */
	@RequestMapping(value = "/remindList.do")
    public String remindList(ModelMap model){

		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("xlList", dictService.loadChildren("xl") );
		model.addAttribute("gradeList", dictService.loadChildren("grade") );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("levelList", dictService.loadChildren("stulevel") );
		
		return "callback/remindList";
	}
	
	
	/**
	 * 递归取所有子的unitId
	 * @param unitId
	 * @return
	 */
	private String childUnitIds(Long unitId){
		StringBuffer buf = new StringBuffer();
		List<Unit> ls = unitService.loadChildren(unitId);
		for(int i=0; i < ls.size(); i++){
			if(i>0) buf.append(",");
			buf.append(ls.get(i).getId());
		}
		return buf.toString();
	}
	
	/**
	 * 列出回访提醒
	 * @return
	 */
	@RequestMapping(value = "/listMyRemindData.do")
    @ResponseBody
    public Map<String, Object> listMyRemindData(HttpServletRequest req, CallbackSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		
		String onlyShowCurrent = req.getParameter("onlyShowCurrent");
		if(StringUtils.isNotEmpty(onlyShowCurrent)){
			Date now = new Date();	
			String szNow = null;
			try{
				szNow = DateUtil.formatDate(now, "yyyy-MM-dd HH:mm");
				
				if(form.getLatest_contact_from() == null){
					form.setLatest_contact_from( szNow );
				}else if( now.getTime() > form.getLatest_contact_from().getTime() ){
					form.setLatest_contact_from( szNow );
				}
			}catch(Exception e){
				getLogger().error("",e);
			}
		}
		
		//限制为只查询自己的
		form.setZxgw_id( user.getId() );
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		
		int total = callbackRemindService.countRemind(form);
		List<ZxgwCallbackRemindVO> ls = callbackRemindService.queryRemind(form, pager);
		
		for(ZxgwCallbackRemindVO vo:ls){
			processVOWhenList(vo);
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	/**
	 * 列出回访提醒
	 * @return
	 */
	@RequestMapping(value = "/listRemindData.do")
    @ResponseBody
    public Map<String, Object> listRemindData(HttpServletRequest req, CallbackSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		if(form.getUnitId() !=null)
			form.setUnitCode( unitService.load(form.getUnitId()).getCode() );
		
		String onlyShowCurrent = req.getParameter("onlyShowCurrent");
		if(StringUtils.isNotEmpty(onlyShowCurrent)){
			Date now = new Date();	
			String szNow = null;
			try{
				szNow = DateUtil.formatDate(now, "yyyy-MM-dd HH:mm");
				
				if(form.getLatest_contact_from() == null){
					form.setLatest_contact_from( szNow );
				}else if( now.getTime() > form.getLatest_contact_from().getTime() ){
					form.setLatest_contact_from( szNow );
				}
			}catch(Exception e){
				getLogger().error("",e);
			}
		}
		
		UserFuncVO queryFunc = userFuncService.loadByCode(user.getId(), "custquery");
		
		if(user.getId().equals(form.getZxgw_id())){
			
		}
		else if( queryFunc ==null ){
			StringBuffer buf = new StringBuffer();
			buf.append(" and zxgw.zxgw_id in (" +  user.getId() );
			
			if( zxgwTeamService.isTeamLeader(user.getId())){
				List<ZxgwTeam> lsZxgw = zxgwTeamService.loadByLeaderId(user.getId());
				for(ZxgwTeam item: lsZxgw){
					buf.append("," + item.getZxgwId() );
				}
			}
			buf.append(")");
			form.setAclClause( buf.toString() );
		}
		else if( StringUtils.isNotBlank(queryFunc.getClause()) ){
			form.setAclClause( queryFunc.getClause() );
		}
		else if( queryFunc !=null && queryFunc.getAclScope() == AclScope.ALL){
			if(queryFunc.getFromIdList() !=null && queryFunc.getFromIdList().length >0){
				StringBuffer buf = new StringBuffer();
				buf.append(" and (");
				Long[] arrId = queryFunc.getFromIdList();
				for(int i=0; i < arrId.length; i++ ){
					Long id = arrId[i];
					Dict dict = dictService.load(id);
					if(i >0) buf.append(" or ");
					buf.append(" dict_code like '" + dict.getDictCode() + "%'");
				} 
				buf.append(")");
				form.setAclClause(buf.toString());
			}
		}
		else if( queryFunc !=null && queryFunc.getAclScope() == AclScope.SOMECOMPANY){
			Long[] arrFromId = queryFunc.getFromIdList();
			StringBuffer buf = new StringBuffer();
			if( arrFromId == null || arrFromId.length==0){
				buf.append(" and (a.company_id in(" + queryFunc.getCompanyIds() +") ");
			}
			else{
				buf.append(" and (( a.company_id in(" + queryFunc.getCompanyIds() +") and (");
				Long[] arrId = queryFunc.getFromIdList();
				for(int i=0; i < arrId.length; i++ ){
					Long id = arrId[i];
					Dict dict = dictService.load(id);
					if(i >0) buf.append(" or ");
					buf.append(" dict_code like '" + dict.getDictCode() + "%'");
				} 
				buf.append("))");
			}
			//buf.append(" or a.zxgw_id=" + user.getId() + " or a.creator_id=" + user.getId() );
			buf.append(")");
			form.setAclClause(buf.toString());
		}	
		else if( queryFunc !=null && queryFunc.getAclScope() == AclScope.SELFCOMPANY){
			Long[] arrFromId = queryFunc.getFromIdList();
			StringBuffer buf = new StringBuffer();
			buf.append("and (");
			buf.append(" (a.company_id =" + user.getCompanyId() + ")");
			if( arrFromId != null && arrFromId.length >0){
				buf.append(" and (");				
				for(int i=0; i < arrFromId.length; i++ ){
					Long id = arrFromId[i];
					Dict dict = dictService.load(id);
					if(i >0) buf.append(" or ");
					buf.append(" dict_code like '" + dict.getDictCode() + "%'");
				} 
				buf.append(")");
			}
			
			buf.append(")");
			form.setAclClause(buf.toString());
		}	
		else if( queryFunc !=null && queryFunc.getAclScope() == AclScope.SOMEUNIT){
			Long[] arrFromId = queryFunc.getFromIdList();
			StringBuffer buf = new StringBuffer();
			buf.append(" and (a.unit_id in(");
			
			Long[] unitIds = queryFunc.getUnitIdList();
			for(int i=0; i <unitIds.length; i++){
				if(i >0) buf.append(",");
				buf.append( unitIds[i] );
				String childIds = childUnitIds( unitIds[i] );
				if(!StringUtils.isEmpty(childIds))
					buf.append("," + childIds);
			}
			buf.append(")");
			if( arrFromId !=null && arrFromId.length >0){
				buf.append(" and (");
				for(int i=0; i < arrFromId.length; i++ ){
					Long id = arrFromId[i];
					Dict dict = dictService.load(id);
					if(i >0) buf.append(" or ");
					buf.append(" dict_code like '" + dict.getDictCode() + "%'");
				} 
				buf.append(" )");
			}
			
			if( zxgwTeamService.isTeamLeader(user.getId())){
				buf.append( " or zxgw.zxgw_id in (" +  user.getId() );
				List<ZxgwTeam> lsZxgw = zxgwTeamService.loadByLeaderId(user.getId());
				for(ZxgwTeam item: lsZxgw){
					buf.append("," + item.getZxgwId() );
				}
				buf.append(") ");
			}

			buf.append(")");	
			form.setAclClause( buf.toString() );
		}	
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		
		int total = callbackRemindService.countRemind(form);
		List<ZxgwCallbackRemindVO> ls = callbackRemindService.queryRemind(form, pager);
		
		for(ZxgwCallbackRemindVO vo:ls){
			processVOWhenList(vo);
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	private void processVOWhenList(ZxgwCallbackRemindVO vo){
		LxCustomer stu = vo.getStudent();
		StuZxgw stuZxgw = vo.getStuZxgw();
		
		if(vo.getRemindType()!=null){
			vo.setRemindTypeName(vo.getRemindType().getName());
		}
		
		if(vo.getZxgwId() !=null){
			stuZxgw.setZxgwName( userService.load(vo.getZxgwId()).getName() );
		}

		if( stuZxgw.getStuLevel() !=null){
			Dict dict = dictService.load(stuZxgw.getStuLevel());
			if(dict !=null)
				stuZxgw.setStuLevelName(dict.getDictName());			
		}
		
		if(stu.getCompanyId() !=null){
			Unit unit = unitService.load(stu.getCompanyId());
			stu.setCompanyName(unit.getName());
		}

		if( stu.getStuFromId() !=null){
			Dict dict = dictService.load(stu.getStuFromId());
			String fromName = null;
			if(dict !=null){
				fromName = dict.getDictName(); 
				int idx = dict.getDictCode().indexOf(".",8);
				if(idx >0 && dict.getParentId() !=null){
					dict = dictService.load(dict.getParentId());
					fromName = dict.getDictName() + "-" + fromName; 
				}
				stu.setStuFromName(fromName);
			}
		}
	}
	
	@RequestMapping(value = "/testAlarm.do")
    @ResponseBody
	public Boolean nextContactAlarm() {
		Pageable pageable = new PageRequest(0, 500);
		ContactRecordSearchForm form = new ContactRecordSearchForm();
		
		Calendar cal = Calendar.getInstance();
		int y = cal.get(Calendar.YEAR);
		int m = cal.get(Calendar.MONTH);
		int d = cal.get(Calendar.DATE);
		
		Calendar nextDateFrom = Calendar.getInstance();
		nextDateFrom.add(Calendar.MINUTE, -30);
		
		Calendar nextDateTo = Calendar.getInstance();
		nextDateTo.add(Calendar.MINUTE, 5);
		
		form.setNextDateFrom(nextDateFrom.getTime());
		form.setNextDateTo(nextDateTo.getTime());
		
		//已提醒的不再重复提醒
		form.setAlarmed(Boolean.FALSE);
		
		List<LxContactRecord> ls= contactRecordService.query(form, pageable);
		
		return Boolean.TRUE;

	}
	
	@RequestMapping(value = "/test.do")
    @ResponseBody
    public Boolean testRemind(String type){
		User user = this.getCurrentUser();
		if(type == null)
			type = "month";
		
		if(type.equalsIgnoreCase("month"))
			this.remindOfMonth();
		
		return Boolean.TRUE;
	}
	/*
	private void remindOfMinAndDay() {
		List<StuZxgwEx> ls = callbackRemindService.getNeedRemindForMinAndDay(2000);
		String fromCode = null;
		Calendar cal = Calendar.getInstance();
		
		for(StuZxgwEx ex:ls){

			cal.setTime(ex.getAssignDate());
			if(cal.get(Calendar.HOUR_OF_DAY) >=18){
				this.remindOfDay(ex);
				continue;
			}
			if(ex.getStuFromId() == null)
				getLogger().error("客户来源没找到  stuZxgwId=" + ex.getId() + " stuId=" + ex.getStuId());
			
			fromCode = "";
			if(ex.getStuFromId() !=null){
				Dict dict = dictService.load(ex.getStuFromId());
				fromCode = dict.getDictCode();
			}
			
			if(fromCode.equals("stufrom.14.01") || fromCode.startsWith("stufrom.14.01.")){
				//在线咨询
				this.remindOf25Min(ex);
			}else if(fromCode.equals("stufrom.14.01") || fromCode.startsWith("stufrom.14.01.")){
				//新媒体资源
				this.remindOf25Min(ex);				
			}else if(fromCode.equals("stufrom.14.04") || fromCode.equals("stufrom.14.05")){
				//前台电话、400电话
				this.remindOf25Min(ex);
			}else if(fromCode.equals("stufrom.03.01") || fromCode.startsWith("stufrom.03.01.") ){
				//50MIN回访  集团渠道资源 -第三方渠道
				this.remindOf50Min(ex);
			}else{
				this.remindOfDay(ex);
			} 
		}
	}*/
	
	public void remindOfDay(StuZxgwEx ex) {
		String remindType = CallbackRemindType.DAY1.toString();
		logger.info("--------------remindType=" + remindType +" t=" + System.currentTimeMillis() );
		
		//今天零点
		Calendar zeroTime = Calendar.getInstance(); 
				
		//前一天录入的数据， 中午12点前完成回访
		Calendar latestTime1 = Calendar.getInstance(); 
		//当天18点前录入的数据，当天完成回访
		Calendar latestTime2 = Calendar.getInstance();
		{
			Calendar cal = Calendar.getInstance();			
			
			int y = cal.get(Calendar.YEAR);
			int m = cal.get(Calendar.MONTH);
			int d = cal.get(Calendar.DATE);		
					
			zeroTime.clear();
			zeroTime.set(y,m,d);
					
			latestTime1.clear();
			latestTime1.set(y,m,d,12,0);
					
			latestTime2.clear();
			latestTime2.set(y,m,d,22,0);
		}	
		
		{
			ZxgwCallbackRemind remind = new ZxgwCallbackRemind();
			remind.setStuId(ex.getStuId());
			remind.setZxgwId(ex.getZxgwId());
			remind.setRemindType(CallbackRemindType.DAY1);
			if(ex.getAssignDate().getTime() < zeroTime.getTimeInMillis())
				remind.setLatestContactTime(latestTime1.getTime());
			else
				remind.setLatestContactTime(latestTime2.getTime());
			
			Customer cust = customerService.load(ex.getCstmId());			
			String msg = "亲!" + cust.getName() + "(" + cust.getMobile() + ") 请做日回访。";
			sendMessage(msg, ex);			
			callbackRemindService.add(remind);
		}
	}
	
	/*
	public void remindOfWeek() {
		String remindType = "WEEK";
		logger.info("--------------remindType=" + remindType +" t=" + System.currentTimeMillis() );
		
		Calendar latestTime = Calendar.getInstance();
		{
			Calendar cal = Calendar.getInstance();
			latestTime.add(Calendar.DATE, 7);
			latestTime.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			
			int y = cal.get(Calendar.YEAR);
			int m = cal.get(Calendar.MONTH);
			int d = cal.get(Calendar.DATE);
			latestTime.clear();
			latestTime.set(y,m,d,23,59,59);
		}
		
		List<StuZxgwEx> ls = callbackRemindService.getNeedRemindForWeek(2000);
		for(StuZxgwEx ex:ls){
			ZxgwCallbackRemind remind = new ZxgwCallbackRemind();
			remind.setStuId(ex.getStuId());
			remind.setZxgwId(ex.getZxgwId());
			remind.setRemindType(CallbackRemindType.WEEK);
			remind.setLatestContactTime(latestTime.getTime());
			
			Customer cust = customerService.load(ex.getCstmId());			
			String msg = "亲!" + cust.getName() + "(" + cust.getMobile() + ") 请做周回访。";
			sendMessage(msg, ex);			
			callbackRemindService.add(remind);
		}
	} */
	
	public void remindOfMonth() {
		String remindType = "MONTH";
		logger.info("--------------remindType=" + remindType +" t=" + System.currentTimeMillis() );
		
		Calendar latestTime = Calendar.getInstance();
		{
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DATE, -1);
			
			int y = cal.get(Calendar.YEAR);
			int m = cal.get(Calendar.MONTH);
			int d = cal.get(Calendar.DATE);
			latestTime.clear();
			latestTime.set(y,m,d,23,59,59);
		}
		
		List<StuZxgwEx> ls = callbackRemindService.getNeedRemindForMonth(2000);
		for(StuZxgwEx ex:ls){
			ZxgwCallbackRemind remind = new ZxgwCallbackRemind();
			remind.setStuId(ex.getStuId());
			remind.setZxgwId(ex.getZxgwId());
			remind.setRemindType(CallbackRemindType.MONTHLY);
			remind.setLatestContactTime(latestTime.getTime());
			
			Customer cust = customerService.load(ex.getCstmId());			
			String msg = "亲!" + cust.getName() + "(" + cust.getMobile() + ") 请做月度回访。";
			sendMessage(msg, ex);			
			callbackRemindService.add(remind);
		}
	}
	/*
	public void remindOf25Min(StuZxgwEx ex) {
		String remindType = CallbackRemindType.MIN25.toString();
		
		{	
			ZxgwCallbackRemind remind = new ZxgwCallbackRemind();
			remind.setStuId(ex.getStuId());
			remind.setZxgwId(ex.getZxgwId());
			remind.setRemindType(CallbackRemindType.MIN25);
			
			Calendar latestTime = Calendar.getInstance();
			latestTime.setTime(ex.getAssignDate());
			latestTime.add(Calendar.MINUTE, 30);
			remind.setLatestContactTime(latestTime.getTime());
			
			Customer cust = customerService.load(ex.getCstmId());			
			String msg = "亲!" + cust.getName() + "(" + cust.getMobile() + ") 需要你关心下，做个回访吧。";
			sendMessage(msg, ex);			
			callbackRemindService.add(remind);
		}
	}
	
	
	private void remindOf50Min(StuZxgwEx ex) {
		String remindType = CallbackRemindType.MIN50.toString();
		{
			ZxgwCallbackRemind remind = new ZxgwCallbackRemind();
			remind.setStuId(ex.getStuId());
			remind.setZxgwId(ex.getZxgwId());
			remind.setRemindType(CallbackRemindType.MIN50);
			
			Calendar latestTime = Calendar.getInstance();
			latestTime.setTime(ex.getAssignDate());
			latestTime.add(Calendar.MINUTE, 60);
			remind.setLatestContactTime(latestTime.getTime());
			
			Customer cust = customerService.load(ex.getCstmId());			
			String msg = "亲!" + cust.getName() + "(" + cust.getMobile() + ") 请回访。";
			sendMessage(msg, ex);			
			callbackRemindService.add(remind);
		}
	} */
	
	private void sendMessage(String msg, StuZxgwEx stuZxgw){
		boolean bSuccess = false;
		Long toUserId = stuZxgw.getZxgwId();
		User u = userService.load(toUserId);
		Long sysUserId = 2L;
		
		//发送系统消息
		try{
			SysMessage sysMsg = new SysMessage();
			sysMsg.setContent(msg);
			sysMsg.setOwnerId(toUserId);
			sysMsg.setSenderId(sysUserId);
			sysMsg.setUrl("/lx/student/main.do?id=" + stuZxgw.getStuId() );
			
			System.out.println("toUserId=" + toUserId + " msg=" + msg);
		}
		catch(Exception e){
			getLogger().error("发送系统消息失败 toUser=" + toUserId, e);
		}
	}
}
