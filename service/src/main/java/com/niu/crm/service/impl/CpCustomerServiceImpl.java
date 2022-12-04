package com.niu.crm.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.CountryService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.CpCustomerService;
import com.niu.crm.service.SmsService;
import com.niu.crm.service.CpStuOperatorService;
import com.niu.crm.service.StuPlanCountryService;
import com.niu.crm.service.SysMessageService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.WechatService;
import com.niu.crm.service.ZxgwCallbackRemindService;
import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.core.error.BizException;
import com.niu.crm.dao.mapper.CustContractMapper;
import com.niu.crm.dao.mapper.CpContactRecordMapper;
import com.niu.crm.dao.mapper.CpContractMapper;
import com.niu.crm.dao.mapper.CpStuZxgwMapper;
import com.niu.crm.dao.mapper.UserMapper;
import com.niu.crm.dao.mapper.CustomerMapper;
import com.niu.crm.dao.mapper.CpCustomerMapper;
import com.niu.crm.dao.mapper.CpCustAssignMapper;
import com.niu.crm.dao.mapper.CpStuLevelMapper;
import com.niu.crm.model.Country;
import com.niu.crm.model.CustAssign;
import com.niu.crm.model.Dict;
import com.niu.crm.model.CpContactRecord;
import com.niu.crm.model.StuLevel;
import com.niu.crm.model.SmsMessage;
import com.niu.crm.model.StuPlanCountry;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.SysMessage;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.Customer;
import com.niu.crm.model.CpCustomer;
import com.niu.crm.model.ZxgwCallbackRemind;
import com.niu.crm.model.type.CallbackRemindType;
import com.niu.crm.vo.CpCustomerVO;
import com.niu.crm.vo.CpCustomerZxgwVO;
import com.niu.crm.vo.RepeatCustVO;

@Service
public class CpCustomerServiceImpl extends BaseService implements
		CpCustomerService {
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private DictService dictService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private UserService userService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private StuPlanCountryService planCountryService;
	@Autowired
	private WechatService wechatService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private SysMessageService sysMessageService;
	@Autowired
	private CpStuOperatorService stuOperatorService;
	@Autowired
	private ZxgwCallbackRemindService callbackRemindService;

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private CpCustomerMapper cpCustomerMapper;
	@Autowired
	private CpCustAssignMapper cpCustAssignMapper;
	@Autowired
	private CpStuZxgwMapper stuZxgwMapper;
	@Autowired
	private CpContactRecordMapper cpContactRecordMapper;
	@Autowired
	private CpStuLevelMapper cpStuLevelMapper;
	@Autowired
	private CpContractMapper cpContractMapper;	
		
	@Override
	public CpCustomer load(Long id) {
		return load(id, null);
	}
	
	@Override
	public CpCustomer loadByCstmId(Long cstmId) {
		return load(null, cstmId);
	}
	private CpCustomer load(Long id, Long cstmId) {
		CpCustomer stu;
		if(id == null)
			stu = cpCustomerMapper.selectByCstmId(cstmId);
		else
			stu = cpCustomerMapper.selectById(id);
			
		return stu;
	}
	
	@Override
	public CpCustomerVO loadVO(Long id, Long cstmId) {
		CpCustomer stu = load(id, cstmId);
		CpCustomerVO vo = new CpCustomerVO();
		BeanUtils.copyProperties(stu, vo);
		
		vo.setZxgwList( stuZxgwMapper.selectByStuId(stu.getId()) );
		for(StuZxgw item:vo.getZxgwList()){
			item.setZxgwName( userService.getUserName(item.getZxgwId()) );
		}
		
		return vo;
	}

	@Transactional
	@Override
	public void add(User user, Customer customer, CpCustomer cpCustomer) {
		// 历史数据导入时 createdAt, updatedAt 都是已经有的
		if (cpCustomer.getCreatedAt() == null)
			cpCustomer.setCreatedAt(new java.util.Date(System
					.currentTimeMillis()));
		if (cpCustomer.getUpdatedAt() == null)
			cpCustomer.setUpdatedAt(cpCustomer.getCreatedAt());

		if (customer.getCreatedAt() == null)
			customer.setCreatedAt(cpCustomer.getCreatedAt());
		if (cpCustomer.getUpdatedAt() == null)
			customer.setUpdatedAt(customer.getCreatedAt());

		if (customer.getId() == null) {
			customerService.insert(user, customer);
		} else {
			customerService.update(user, customer);
		}

		cpCustomer.setCstmId(customer.getId());
		cpCustomer.setCreatorId(user.getId());
		cpCustomerMapper.insert(cpCustomer);

		stuOperatorService.fix(cpCustomer.getId());
	}

	@Transactional
	@Override
	public void update(User user, Customer customer, CpCustomer cpCustomer) {
		CpCustomer oldStu = cpCustomerMapper.selectById(cpCustomer.getId());
		// 更新时 允许不传cstmId, 也可以防止传了错误的cstmId
		{
			Long cstmId = oldStu.getCstmId();
			cpCustomer.setCstmId(cstmId);
			customer.setId(cstmId);
		}

		customerService.update(user, customer);
		cpCustomerMapper.update(cpCustomer);
		
		stuOperatorService.fix(cpCustomer.getId());
	}

	@Transactional
	@Override
	public int delete(User user, Long id) {
		int count = 0;
		CpCustomer lxCustomer = cpCustomerMapper.selectById(id);
		if (lxCustomer != null) {
			stuOperatorService.deleteByStuId(id);
			
			stuZxgwMapper.deleteByStuId(id);
			cpCustAssignMapper.deleteByStuId(id);
			cpContactRecordMapper.deleteByStuId(id);
			count = cpCustomerMapper.delete(id);
			customerMapper.delete(lxCustomer.getCstmId());
		}

		return count;
	}

	@Override
	public int countStudents(StudentSearchForm form) {
		return cpCustomerMapper.countStudents(form);
	}

	@Override
	public List<CpCustomerVO> queryStudents(StudentSearchForm form, Pageable pageable) {
		if (form.getFuzzySearch()) {
			if (StringUtils.isNoneEmpty(form.getName())) {
				if (!form.getName().startsWith("%"))
					form.setName("%" + form.getName());
				if (!form.getName().endsWith("%"))
					form.setName(form.getName() + "%");
			}
			if (StringUtils.isNoneEmpty(form.getPhone())) {
				if (!form.getPhone().startsWith("%"))
					form.setPhone("%" + form.getPhone());
				if (!form.getPhone().endsWith("%"))
					form.setPhone(form.getPhone() + "%");
			}
		}
		List<CpCustomerVO> ls = cpCustomerMapper.queryStudents(form, pageable);

		for(CpCustomerVO vo:ls){
			Dict dict = null;
			if( vo.getStuLevel() !=null){
				dict = dictService.load(vo.getStuLevel());
				vo.setStuLevelName(dict.getDictName());
			}
			if(vo.getCompanyId() !=null){
				Unit unit = unitService.load(vo.getCompanyId());
				vo.setCompanyName(unit.getName());
			}
		}
		
		return ls;
	}


	@Override
	public List<RepeatCustVO> queryRepeat(Long excludeCstmId, String... phones){
		List<RepeatCustVO> lsRepeat = new ArrayList<>();
		if(phones == null || phones.length == 0)
			return lsRepeat;
		
		List<CpCustomerVO> ls = cpCustomerMapper.queryRepeat(excludeCstmId, phones);
		for(int i=0; ls !=null && i < ls.size(); i++){
			CpCustomerVO vo = ls.get(i);
			if (vo.getStuFromId() != null) {
				Dict dict = dictService.load(vo.getStuFromId());
				if (dict != null)
					vo.setStuFromName(dict.getDictName());
			}
			RepeatCustVO repeat = new RepeatCustVO();
			lsRepeat.add(repeat);
			
			BeanUtils.copyProperties(vo, repeat);			
			repeat.setPrecust(Boolean.FALSE);
			repeat.setStuId( vo.getId() );
			repeat.setCstmId( vo.getCstmId() );
			repeat.setCstmName( vo.getCustomer().getName() );
			repeat.setMobile( vo.getCustomer().getMobile() );
		}
		
		return lsRepeat;
	}

	@Transactional
	@Override
	public void updateStuZxgwLevel(User user, Long zxgwId, Long stuId, Long levelId) {
		if(levelId == null)
			return;
		
		StuLevel stuLevel = new StuLevel();
		stuLevel.setStuId(stuId);
		stuLevel.setLevelId(levelId);
		stuLevel.setCreatorId(user.getId());

		StuZxgw zxgw = stuZxgwMapper.selectByStuIdAndZxgw(stuId, zxgwId);
		if(zxgw ==null || levelId.equals(zxgw.getStuLevel()) ){
			return;
		}else{
			zxgw.setStuLevel(levelId);
			stuZxgwMapper.update(zxgw);
			cpStuLevelMapper.insert(stuLevel); 
			stuOperatorService.fix(stuId);
			
			String levelCode = dictService.load(levelId).getDictCode();
			if(levelCode.endsWith(".invalid") || levelCode.endsWith(".discard")){
				fixZxgwNames(stuId);
			}
		}
		
		//设置该客户的最高评级
		this.fixStuLevel(stuId);
	}
	
	/**
	 * 修复该客户的最高评级
	 * @param stuId 学生id
	 */
	@Override
	public void fixStuLevel(Long stuId) {
		// 设置该客户的最高评级
		List<StuZxgw> lsZxgw = stuZxgwMapper.selectByStuId(stuId);
		Dict topLevel = null;
		for (StuZxgw item : lsZxgw) {
			if (item.getStuLevel() != null) {
				Dict dict = dictService.load(item.getStuLevel());
				if (topLevel == null
						|| topLevel.getShowIndex() > dict.getShowIndex())
					topLevel = dict;
			}
		}
		if (topLevel != null) {
			CpCustomer cpCust = new CpCustomer();
			cpCust.setId(stuId);
			cpCust.setStuLevel(topLevel.getId());
			cpCustomerMapper.updateByPrimaryKeySelective(cpCust);
		}
	}

	/**
	 * zxgwId null 表示收回
	 */
	@Transactional
	@Override
	public void assign2Zxgw(User user, Long stuId, Long zxgwId) {
		StuZxgw stuZxgw = stuZxgwMapper.selectByStuIdAndZxgw(stuId, zxgwId);
		if(stuZxgw !=null)
			return;
		
		CustAssign custAssign = new CustAssign();
		custAssign.setStuId(stuId);
		custAssign.setZxgwId(zxgwId);
		custAssign.setOpType( CustAssign.OP_ASSIGN );
		custAssign.setCreatorId(user.getId());
		
		StringBuffer buf = new StringBuffer();
		buf.append( user.getName() + "分配了1个客户给你" );
		
		stuZxgw = new StuZxgw();
		stuZxgw.setStuId(stuId);
		stuZxgw.setZxgwId(zxgwId);
		stuZxgw.setAssignDate( new Date() );
		stuZxgw.setCreatorId(user.getId());
		
		CpCustomer student = cpCustomerMapper.selectById(stuId);
		Customer cust = customerMapper.selectById(student.getCstmId());
		buf.append("\r\n");
		buf.append(" 姓名:" + cust.getName() + " 电话:" + cust.getMobile() );
		
		stuZxgwMapper.insert(stuZxgw);
		
		if(student.getFirstAssignDate() ==null){
			CpCustomer pxCust = new CpCustomer();
			pxCust.setId(stuId);
			pxCust.setFirstAssignDate( new Date() );
			
			cpCustomerMapper.updateByPrimaryKeySelective(pxCust);
		}
		
		//分配日志
		cpCustAssignMapper.insert(custAssign);
		stuOperatorService.fix(stuId);
		fixZxgwNames(stuId);
		
		//回收资源不提醒
		if (!user.getId().equals(zxgwId) ) {
			sendMessage(user.getId(), buf.toString(), stuZxgw);
		}
	}

	/**
	 * 顾问把客户退回到 客户池
	 */
	@Override
	public void revokeAssignFromZxgw(User user, Long stuId, Long zxgwId) {
		StuZxgw stuZxgw = stuZxgwMapper.selectByStuIdAndZxgw(stuId, zxgwId);
		if(stuZxgw ==null)
			return;
			
		CustAssign custAssign = new CustAssign();
		custAssign.setStuId(stuId);
		custAssign.setZxgwId(zxgwId);
		custAssign.setOpType( CustAssign.OP_REVOKE );
		custAssign.setCreatorId(user.getId());
						
		stuZxgwMapper.delete(stuZxgw.getId());
			
		//回收分配日志
		cpCustAssignMapper.insert(custAssign);
		stuOperatorService.fix(stuId);
		fixZxgwNames(stuId);
		
		//重新设置学生的 顾问评级
		this.fixStuLevel(stuId);
	}
	
	private void fixZxgwNames(Long stuId){
		List<StuZxgw> zxgwList = loadStuZxgwByStuId(stuId);
		List<StuZxgw> ls = new ArrayList<>();
		for(StuZxgw zxgw:zxgwList){
			if( zxgw.getStuLevel() !=null){
				String levelCode = dictService.load(zxgw.getStuLevel()).getDictCode();
				if(levelCode.endsWith(".invalid") || levelCode.endsWith(".discard")){
					continue;
				}
			}
			ls.add(zxgw);
		}
		
		StringBuffer buf = new StringBuffer();
		for(int i=0; i < ls.size(); i++){
			if(i >0)
				buf.append(",");
			buf.append(ls.get(i).getZxgwName());
		}
		CpCustomer student = new CpCustomer();
		student.setId(stuId);
		student.setZxgwNames(buf.toString());
		cpCustomerMapper.updateByPrimaryKeySelective(student);
	}
	
	private void sendMessage(Long userId, String msg, StuZxgw stuZxgw){
		boolean bSuccess = false;
		Long toUserId = stuZxgw.getZxgwId();
		User u = userService.load(toUserId);
		
		//发送系统消息
		try{
			SysMessage sysMsg = new SysMessage();
			sysMsg.setContent(msg);
			sysMsg.setOwnerId(toUserId);
			sysMsg.setSenderId(userId);
			sysMsg.setUrl("/lx/student/main.do?id=" + stuZxgw.getStuId() );
					
			sysMessageService.add(sysMsg);
		}
		catch(Exception e){
			getLogger().error("发送系统消息失败 toUser=" + toUserId, e);
		}
		
		//先发送微信消息
		if (!isDevProfile() || isCanTestUser(toUserId)) {
			try {
				JSONObject json = wechatService.sendTxtMessage(msg, null,
						new Long[] { toUserId });
				if (json.getIntValue("errcode") == 0) {
					bSuccess = true;
				} else {
					getLogger().error("发送微信消息失败 toUser=" + toUserId);
				}
			} catch (Exception e) {
				getLogger().error("发送微信消息失败 toUser=" + toUserId, e);
			}
				
			// 如果微信发送失败 再发送短信
			if (!bSuccess && StringUtils.isNoneEmpty(u.getPhone())) {
				SmsMessage smsMessage = new SmsMessage();
				smsMessage.setContent(msg);
				smsMessage.setMobile(u.getPhone());
				smsMessage.setCreatorId(userId);

				smsService.send(smsMessage);
			}
		}
	}

    //更新签约日期	
	public void updateSignDate(Long cstmId){
		Date firstDate = cpContractMapper.selectFirstSignDate(cstmId);
		CpCustomer lxCustomer = new CpCustomer();
		lxCustomer.setCstmId(cstmId);
		lxCustomer.setSignDate(firstDate);
		
		cpCustomerMapper.updateSignDate(lxCustomer);
	}
	
	@Override
	public List<StuZxgw> loadStuZxgwByStuId(Long stuId){
		List<StuZxgw> ls = stuZxgwMapper.selectByStuId(stuId);
		for(StuZxgw zxgw:ls){
			initZxgw(zxgw);
		}
		return ls;
	}
	
	@Override
	public StuZxgw loadStuZxgw(Long stuId, Long zxgwId){
		StuZxgw zxgw = stuZxgwMapper.selectByStuIdAndZxgw(stuId, zxgwId);
		initZxgw(zxgw);
		return zxgw;
	}
	
	private void initZxgw(StuZxgw zxgw){
		if(zxgw ==null)
			return;
		
		String uName = userService.load(zxgw.getZxgwId()).getName();
		zxgw.setZxgwName(uName);
			
		if(zxgw.getStuLevel() !=null){
			Dict dict = dictService.load(zxgw.getStuLevel());
			if(dict !=null)
				zxgw.setStuLevelName(dict.getDictName());
			else
				zxgw.setStuLevelName(zxgw.getStuLevel().toString());
		}
	}
	

	@Override
	public List<StuLevel> loadStuLevelByStuId(Long stuId){
		return cpStuLevelMapper.selectByStuId(stuId);	
	}

	@Override
	public List<CustAssign> loadCustAssignByStuId(Long stuId){
		return cpCustAssignMapper.selectByStuId(stuId);	
	}
	
	
	@Override
	public int countStuZxgw(StudentSearchForm form) {
		return stuZxgwMapper.countStuZxgw(form);
	}

	@Override
	public List<CpCustomerZxgwVO> queryStuZxgw(StudentSearchForm form, Pageable pageable) {
		if (form.getFuzzySearch()) {
			if (StringUtils.isNoneEmpty(form.getName())) {
				if (!form.getName().startsWith("%"))
					form.setName("%" + form.getName());
				if (!form.getName().endsWith("%"))
					form.setName(form.getName() + "%");
			}
			if (StringUtils.isNoneEmpty(form.getPhone())) {
				if (!form.getPhone().startsWith("%"))
					form.setPhone("%" + form.getPhone());
				if (!form.getPhone().endsWith("%"))
					form.setPhone(form.getPhone() + "%");
			}
		}
		Dict dict = null;
		List<CpCustomerZxgwVO> ls = stuZxgwMapper.queryStuZxgw(form, pageable);
		for(CpCustomerZxgwVO vo:ls){
			User u = userService.load(vo.getZxgwId());
			vo.setZxgwName(u.getName());
			
			if(vo.getStuLevel() !=null){
				dict = dictService.load(vo.getStuLevel());
				if(dict == null)
					vo.setStuLevelName(vo.getStuLevel().toString());
				else
					vo.setStuLevelName(dict.getDictName());
			}
		}
		
		return ls;
	}
	
	
	//取咨询组长
	private Long getTeamLeader(Long zxgwId){
		String uName = userService.load(zxgwId).getName();

		if(uName.equals("刘元媛") || uName.equals("许悦") || uName.equals("梁潇") ||uName.equals("杨柳") || 
				uName.equals("李晓涵") || uName.equals("姜霁钊") ||	uName.equals("刘琳")){
			return userService.loadByAccount("liuyuanyuan").getId();
		}
		if(uName.equals("郑佳南") || uName.equals("谢露") || uName.equals("张嘉书") ||uName.equals("王琦")){
			return userService.loadByAccount("zhengjianan").getId();
		}

		if(uName.equals("卢晓鹏") || uName.equals("王煦")){
			return userService.loadByAccount("luxiaopeng").getId();
		}
		if(uName.equals("闫宁") || uName.equals("董吴晗") || uName.equals("王鑫")){
			return zxgwId; //yanning
		}
		if(uName.equals("陈言言") || uName.equals("姜静") || uName.equals("刘彦君")){
			return 468L; //陈言言
		}
		
		return null;
	}
}	
