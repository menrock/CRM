package com.niu.crm.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.CountryService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.LxCustomerService;
import com.niu.crm.service.LxPreCustService;
import com.niu.crm.service.LxStuZxgwService;
import com.niu.crm.service.LxZxgwTeamService;
import com.niu.crm.service.SmsService;
import com.niu.crm.service.LxStuOperatorService;
import com.niu.crm.service.StuPlanCountryService;
import com.niu.crm.service.SysMessageService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.WechatService;
import com.niu.crm.service.ZxgwCallbackRemindService;
import com.niu.crm.form.CallbackSearchForm;
import com.niu.crm.form.CallbackSimpleSearchForm;
import com.niu.crm.form.ContactRecordSearchForm;
import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.core.error.BizException;
import com.niu.crm.dao.mapper.CustContractMapper;
import com.niu.crm.dao.mapper.LxContactRecordMapper;
import com.niu.crm.dao.mapper.LxContractMapper;
import com.niu.crm.dao.mapper.StuZxgwMapper;
import com.niu.crm.dao.mapper.UserMapper;
import com.niu.crm.dao.mapper.CustomerMapper;
import com.niu.crm.dao.mapper.LxCustomerMapper;
import com.niu.crm.dao.mapper.LxCustAssignMapper;
import com.niu.crm.dao.mapper.LxStuLevelMapper;
import com.niu.crm.dao.mapper.ZxgwCallbackRemindMapper;
import com.niu.crm.model.Country;
import com.niu.crm.model.CustomerRecommend;
import com.niu.crm.model.Dict;
import com.niu.crm.model.LxContactRecord;
import com.niu.crm.model.LxCustAssign;
import com.niu.crm.model.LxStuLevel;
import com.niu.crm.model.SmsMessage;
import com.niu.crm.model.StuPlanCountry;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.SysMessage;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.Customer;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.ZxgwCallbackRemind;
import com.niu.crm.model.ZxgwTeam;
import com.niu.crm.model.type.CallbackRemindType;
import com.niu.crm.util.CsvUtil;
import com.niu.crm.vo.LxCustomerVO;
import com.niu.crm.vo.LxCustomerZxgwVO;
import com.niu.crm.vo.LxPreCustVO;
import com.niu.crm.vo.MyCustomerVO;
import com.niu.crm.vo.RepeatCustVO;

@Service
public class LxCustomerServiceImpl extends BaseService implements
		LxCustomerService {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private LxStuZxgwService stuZxgwService;
	@Autowired
	private LxPreCustService lxPreCustService;
	
	@Autowired
	private DictService dictService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private UserService userService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private LxZxgwTeamService zxgwTeamService;
	@Autowired
	private StuPlanCountryService planCountryService;
	@Autowired
	private WechatService wechatService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private SysMessageService sysMessageService;
	@Autowired
	private LxStuOperatorService stuOperatorService;
	@Autowired
	private ZxgwCallbackRemindService callbackRemindService;

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private LxCustomerMapper lxCustomerMapper;
	@Autowired
	private LxCustAssignMapper lxCustAssignMapper;
	@Autowired
	private StuZxgwMapper stuZxgwMapper;
	@Autowired
	private LxContactRecordMapper lxContactRecordMapper;
	@Autowired
	private LxStuLevelMapper lxStuLevelMapper;
	@Autowired
	private LxContractMapper lxContractMapper;	
	@Autowired
	private ZxgwCallbackRemindMapper callbackRemindMapper;
		
	@Override
	public LxCustomer load(Long id) {
		return load(id, null);
	}
	
	@Override
	public LxCustomer loadByCstmId(Long cstmId) {
		return load(null, cstmId);
	}
	private LxCustomer load(Long id, Long cstmId) {
		LxCustomer stu;
		if(id == null)
			stu = lxCustomerMapper.selectByCstmId(cstmId);
		else
			stu = lxCustomerMapper.selectById(id);
			
		return stu;
	}
	
	@Override
	public LxCustomerVO loadVO(Long id, Long cstmId) {
		LxCustomer stu = load(id, cstmId);
		LxCustomerVO vo = new LxCustomerVO();
		BeanUtils.copyProperties(stu, vo);
		
		vo.setZxgwList( stuZxgwService.listByStuId(stu.getId()) );
		
		return vo;
	}

	@Transactional
	@Override
	public void add(User user, Customer customer, LxCustomer lxCustomer) {
		// ????????????????????? createdAt, updatedAt ??????????????????
		if (lxCustomer.getCreatedAt() == null)
			lxCustomer.setCreatedAt(new java.util.Date(System
					.currentTimeMillis()));
		if (lxCustomer.getUpdatedAt() == null)
			lxCustomer.setUpdatedAt(lxCustomer.getCreatedAt());

		if (customer.getCreatedAt() == null)
			customer.setCreatedAt(lxCustomer.getCreatedAt());
		if (lxCustomer.getUpdatedAt() == null)
			customer.setUpdatedAt(customer.getCreatedAt());

		if (customer.getId() == null) {
			customerService.insert(user, customer);
		} else {
			customerService.update(user, customer);
		}

		lxCustomer.setCstmId(customer.getId());
		lxCustomer.setCreatorId(user.getId());
		lxCustomerMapper.insert(lxCustomer);
		savePlanCountry(lxCustomer);

		stuOperatorService.fix(lxCustomer.getId());
	}

	@Transactional
	@Override
	public void update(User user, Customer customer, LxCustomer lxCustomer) {
		LxCustomer oldStu = lxCustomerMapper.selectById(lxCustomer.getId());
		// ????????? ????????????cstmId, ??????????????????????????????cstmId
		{
			Long cstmId = oldStu.getCstmId();
			lxCustomer.setCstmId(cstmId);
			customer.setId(cstmId);
		}

		customerService.update(user, customer);
		lxCustomerMapper.update(lxCustomer);
		savePlanCountry(lxCustomer);
		
		stuOperatorService.fix(lxCustomer.getId());
	}
	
	@Transactional
	private void savePlanCountry(LxCustomer lxCustomer) {
		Long stuId = lxCustomer.getId();
		planCountryService.deleteByStuId(stuId);
		
		String[] codes = null;
		if( lxCustomer.getPlanCountry() !=null)
			codes = lxCustomer.getPlanCountry().split("\\,");
		for(int i=0; codes !=null && i < codes.length; i++){
			if(StringUtils.isEmpty(codes[i]))
				continue;
			StuPlanCountry planCountry = new StuPlanCountry();
			planCountry.setCountryCode(codes[i]);
			planCountry.setStuId(stuId);
			planCountryService.insert(planCountry);
		}
	}

	@Transactional
	@Override
	public int delete(User user, Long id) {
		int count = 0;
		LxCustomer lxCustomer = lxCustomerMapper.selectById(id);
		if (lxCustomer != null) {
			stuOperatorService.deleteByStuId(id);
			
			stuZxgwMapper.deleteByStuId(id);
			lxCustAssignMapper.deleteByStuId(id);
			lxContactRecordMapper.deleteByStuId(id);
			count = lxCustomerMapper.delete(id);
			customerMapper.delete(lxCustomer.getCstmId());
		}

		return count;
	}

	@Override
	public int countStudents(StudentSearchForm form) {
		return lxCustomerMapper.countStudents(form);
	}

	@Override
	public List<LxCustomerVO> queryStudents(StudentSearchForm form, Pageable pageable) {
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
		List<LxCustomerVO> ls = lxCustomerMapper.queryStudents(form, pageable);

		for(LxCustomerVO vo:ls){
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
		
		List<LxCustomerVO> ls = lxCustomerMapper.queryRepeat(excludeCstmId, phones);
		for(int i=0; ls !=null && i < ls.size(); i++){
			LxCustomerVO vo = ls.get(i);
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
	
	/**
	 * ????????????????????????????????????????????????
	 * @param excludeCstmId
	 * @param phones
	 * @return
	 */
	@Override
	public List<RepeatCustVO> queryRepeat2(Long excludeCstmId, String... phones){
		List<RepeatCustVO> lsRepeat = queryRepeat(excludeCstmId, phones);
		
		List<RepeatCustVO> ls2 = lxPreCustService.queryRepeat(excludeCstmId, phones);
		if(ls2.size() >0)
			lsRepeat.addAll( ls2 );
		
		return lsRepeat;
	}

	@Transactional
	@Override
	public void updateStuZxgwLevel(User user, Long zxgwId, Long stuId, Long levelId) {
		if(levelId == null)
			return;
		
		//?????????  ??????????????????
		if( (levelId > 29 && levelId <34) || levelId == 497){
			CallbackSimpleSearchForm form = new CallbackSimpleSearchForm();
			form.setSource_zxgw_id(zxgwId);
			form.setStuId(stuId);
			List<ZxgwCallbackRemind> ls = callbackRemindMapper.simpleList(form, null);
			for(ZxgwCallbackRemind item:ls){
				if(item.getContactId() ==null){
					callbackRemindMapper.deleteByPrimaryKey(item.getId());
				}
			}
		}
				
		LxStuLevel stuLevel = new LxStuLevel();
		stuLevel.setStuId(stuId);
		stuLevel.setLevelId(levelId);
		stuLevel.setCreatorId(user.getId());

		StuZxgw zxgw = stuZxgwMapper.selectByStuIdAndZxgw(stuId, zxgwId);
		if(zxgw ==null || levelId.equals(zxgw.getStuLevel()) ){
			return;
		}else{
			zxgw.setStuLevel(levelId);
			stuZxgwMapper.update(zxgw);
			lxStuLevelMapper.insert(stuLevel); 
			stuOperatorService.fix(stuId);
			
			String levelCode = dictService.load(levelId).getDictCode();
			if(levelCode.endsWith(".invalid") || levelCode.endsWith(".discard")){
				fixZxgwNames(stuId);
			}
		}
		
		//??????????????????????????????
		this.fixStuLevel(stuId);
	}
	
	/**
	 * ??????????????????????????????
	 * @param stuId ??????id
	 */
	@Override
	public void fixStuLevel(Long stuId) {
		// ??????????????????????????????
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
			LxCustomer lxCust = new LxCustomer();
			lxCust.setId(stuId);
			lxCust.setStuLevel(topLevel.getId());
			lxCustomerMapper.updateByPrimaryKeySelective(lxCust);
		}
	}

	/**
	 * zxgwId null ????????????
	 */
	@Transactional
	@Override
	public void assign2Zxgw(User user, Long stuId, Long zxgwId) {
		StuZxgw stuZxgw = stuZxgwMapper.selectByStuIdAndZxgw(stuId, zxgwId);
		if(stuZxgw !=null)
			return;
		
		LxCustAssign custAssign = new LxCustAssign();
		custAssign.setStuId(stuId);
		custAssign.setZxgwId(zxgwId);
		custAssign.setOpType( LxCustAssign.OP_ASSIGN );
		custAssign.setCreatorId(user.getId());
		
		StringBuffer buf = new StringBuffer();
		buf.append( user.getName() + "?????????1???????????????" );
		
		stuZxgw = new StuZxgw();
		stuZxgw.setStuId(stuId);
		stuZxgw.setZxgwId(zxgwId);
		stuZxgw.setAssignDate( new Date() );
		stuZxgw.setCreatorId(user.getId());
		
		LxCustomer student = lxCustomerMapper.selectById(stuId);
		Customer cust = customerMapper.selectById(student.getCstmId());
		buf.append("\r\n");
		buf.append(" ??????:" + cust.getName() + " ??????:" + cust.getMobile() );
		
		stuZxgwMapper.insert(stuZxgw);
		
		if(student.getFirstAssignDate() ==null){
			LxCustomer lxCust = new LxCustomer();
			lxCust.setId(stuId);
			lxCust.setFirstAssignDate( new Date() );
			
			lxCustomerMapper.updateByPrimaryKeySelective(lxCust);
		}
		
		//????????????
		lxCustAssignMapper.insert(custAssign);
		stuOperatorService.fix(stuId);
		fixZxgwNames(stuId);
		
		//?????????????????????
		if (!user.getId().equals(zxgwId) ) {
			sendMessage(user.getId(), buf.toString(), stuZxgw);
		}
		
		Calendar cal = java.util.Calendar.getInstance();
		//??????????????? ??????????????????
		//if(zxgwId !=null && student.getCompanyId().intValue() == 4)
		{
			int y = cal.get(Calendar.YEAR);
			int m = cal.get(Calendar.MONTH);
			int d = cal.get(Calendar.DATE);
			Date today = null;
			{
				Calendar cal2 = java.util.Calendar.getInstance();
				cal2.clear();
				cal2.set(y, m, d);
				today = cal2.getTime();
			}
			
			ZxgwCallbackRemind remind = null;
			//????????????
			remind = new ZxgwCallbackRemind();
			remind.setStuId(stuId);
			remind.setSourceZxgwId(zxgwId);
			remind.setZxgwId(zxgwId);
			remind.setRemindType(CallbackRemindType.DAY1);
			if(cal.get(Calendar.HOUR_OF_DAY) < 18){
				remind.setLatestContactTime( DateUtils.addHours(today,23) );
			}else{
				remind.setLatestContactTime( DateUtils.addHours(today,35) );
			}
			callbackRemindService.add(remind);
			
			//???????????????
			remind = new ZxgwCallbackRemind();
			remind.setStuId(stuId);
			remind.setSourceZxgwId(zxgwId);
			remind.setZxgwId(zxgwId);
			remind.setRemindType(CallbackRemindType.DAY3);
			remind.setLatestContactTime( DateUtils.addHours(today,24*3 -1) );
			callbackRemindService.add(remind);
			//?????????5?????????
			Long teamLeaderId = getTeamLeader(zxgwId);
			if (teamLeaderId != null) {
				remind = new ZxgwCallbackRemind();
				remind.setStuId(stuId);
				remind.setSourceZxgwId(zxgwId);
				remind.setZxgwId(teamLeaderId);
				remind.setRemindType(CallbackRemindType.DAY5);
				remind.setLatestContactTime(DateUtils.addHours(today, 24 * 5 -1));
				callbackRemindService.add(remind);
			}
			//???7?????????
			remind = new ZxgwCallbackRemind();
			remind.setStuId(stuId);
			remind.setSourceZxgwId(zxgwId);
			remind.setZxgwId(zxgwId);
			remind.setRemindType(CallbackRemindType.DAY7);
			remind.setLatestContactTime( DateUtils.addHours(today,24*7 -1) );
			callbackRemindService.add(remind);
		}
	}

	/**
	 * ???????????????????????? ?????????
	 */
	@Override
	public void revokeAssignFromZxgw(User user, Long stuId, Long zxgwId) {
		StuZxgw stuZxgw = stuZxgwMapper.selectByStuIdAndZxgw(stuId, zxgwId);
		if(stuZxgw ==null)
			return;
			
		LxCustAssign custAssign = new LxCustAssign();
		custAssign.setStuId(stuId);
		custAssign.setZxgwId(zxgwId);
		custAssign.setOpType( LxCustAssign.OP_REVOKE );
		custAssign.setCreatorId(user.getId());
						
		stuZxgwMapper.delete(stuZxgw.getId());
		
		
		callbackRemindMapper.deleteBySourceZxgw(stuId, zxgwId);
			
		//??????????????????
		lxCustAssignMapper.insert(custAssign);
		stuOperatorService.fix(stuId);
		fixZxgwNames(stuId);
		
		//????????????????????? ????????????
		this.fixStuLevel(stuId);
	}
	
	private void fixZxgwNames(Long stuId){
		List<StuZxgw> zxgwList = stuZxgwService.listByStuId(stuId);
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
		LxCustomer student = new LxCustomer();
		student.setId(stuId);
		student.setZxgwNames(buf.toString());
		lxCustomerMapper.updateByPrimaryKeySelective(student);
	}

	/**
	 * ??????????????????
	 * 
	 * @param file
	 * @return ??????(count, msg)
	 * @throws IOException
	 * @throws Exception
	 */
	@Transactional
	@Override
	public Map<String, String> importCustomer(User user, Long unitId, Long stuFromId,
			MultipartFile file) throws IOException, Exception {
		Map<String, String> map = new java.util.HashMap<String, String>();
		String fileName = file.getName().toLowerCase();

		Long companyId = unitService.load(unitId).getCompanyId();

		byte[] head = file.getBytes();
		String charset = "GBK";
		// ????????? 0xefbb???0xfffe???0xfeff???0x5c75????????????????????????????????????????????????16?????????

		if (head[0] == -1 && head[1] == -2)
			charset = "UTF-16";
		else if (head[0] == -2 && head[1] == -1)
			charset = "Unicode";
		else if (head[0] == -17 && head[1] == -69 && head[2] == -65)
			charset = "UTF-8";

		InputStream is = file.getInputStream();
		InputStreamReader reader = new InputStreamReader(is, charset);
		List<List<String>> rowArray = CsvUtil.parse(reader);
		if (rowArray == null || rowArray.size() < 2) {
			this.getLogger().info("fileName=" + fileName, "????????????");
			map.put("msg", "????????????");
			map.put("count", "0");
			return map;
		}

		// ?????? ?????? ???????????? ???????????? ???????????????????????? ???????????? ??????
		// ????????? ?????????????????? ???????????? ???????????? ?????? ????????????
		// ???????????? ???????????? ???????????? ???????????? GPA ??????
		String[] flds = new String[] { "name", "gender", "currSchool",
				"currGrade", "mobile", "phone", "email", "planCountry",
				"pxRequire", "stuFrom", "stuCity", "wechat", "visitorType",
				"planXl", "hopeSpecialty", "enterYear", "basicInfo", "gpa",
				"memo" };


		StringBuffer bufMsg = new StringBuffer();
		Customer customer = null;
		LxCustomer lxCustomer = null;
		int importCount = 0;
		for (int idx = 1; idx < rowArray.size(); idx++) {
			List<String> row = rowArray.get(idx);

			if (row.size() < flds.length)
				continue;

			Map<String, String> mapVal = new HashMap<String, String>();
			for (int loop = 0; loop < flds.length; loop++) {
				mapVal.put(flds[loop], row.get(loop));
			}

			customer = new Customer();
			lxCustomer = new LxCustomer();
			lxCustomer.setCompanyId(companyId);
			lxCustomer.setUnitId(unitId);

			String custName = mapVal.get("name");
			String mobile = mapVal.get("mobile");
			String gender = mapVal.get("gender");

			if (StringUtils.isEmpty(mobile))
				continue;

			// ??????
			List<RepeatCustVO> ls = queryRepeat2(null, mobile);
			if (ls.size() > 0) {
				bufMsg.append(mobile + "?????? <br/>");
				continue;
			}

			customer.setName(custName);
			if (StringUtils.isNotEmpty(gender)) {
				if (gender.equals("???") || gender.equalsIgnoreCase("M"))
					customer.setGender("M");
				else if (gender.equals("???") || gender.equalsIgnoreCase("F"))
					customer.setGender("F");
			}

			lxCustomer.setCurrSchool(mapVal.get("currSchool"));
			lxCustomer.setCurrGrade(mapVal.get("currGrade"));
			customer.setMobile(mobile);
			customer.setPhone(mapVal.get("phone"));
			customer.setEmail(mapVal.get("email"));
			lxCustomer.setPlanCountry( convertPlanCountry(mapVal.get("planCountry")));
			lxCustomer.setPxRequire(mapVal.get("pxRequire"));
			lxCustomer.setStuFromId(stuFromId);
			lxCustomer.setStuCity(mapVal.get("stuCity"));
			customer.setWechat(mapVal.get("wechat"));
			lxCustomer.setVisitorType(mapVal.get("visitorType"));
			lxCustomer.setPlanXl(mapVal.get("planXl"));
			lxCustomer.setHopeSpecialty(mapVal.get("hopeSpecialty"));
			lxCustomer.setBasicInfo(mapVal.get("basicInfo"));
			lxCustomer.setGpa(mapVal.get("gpa"));

			String szMemo = mapVal.get("memo");
			if (StringUtils.isNoneEmpty(mapVal.get("enterYear"))) {
				String szEnterYear = mapVal.get("enterYear").trim();
				Pattern pattern = Pattern.compile("20[12][0-9]???[??????]???");
				if (pattern.matcher(szEnterYear).matches()) {
					lxCustomer.setPlanEnterYear(Integer.valueOf(szEnterYear
							.substring(0, 4)));
					lxCustomer.setPlanEnterSeason(szEnterYear.substring(5));
				} else {
					if (szMemo.length() == 0)
						szMemo = "????????????:" + mapVal.get("enterYear");
					else
						szMemo = "????????????:" + mapVal.get("enterYear") + "\r\n"
								+ szMemo;
				}
			}
			lxCustomer.setMemo(szMemo);

			this.add(user, customer, lxCustomer);
			
			importCount++;
		}

		map.put("count", String.valueOf(importCount));
		map.put("msg", bufMsg.toString());

		return map;
	}
	
	private String convertPlanCountry(String planCountry) {
		if(StringUtils.isEmpty(planCountry))
			return null;
		planCountry = planCountry.trim();
		//??????????????? ???????????????
		String[] arr = planCountry.split("[,;???]");
		List<Country> ls = countryService.loadAll();
		
		StringBuffer buf = new StringBuffer();
		for(String s:arr){
			s = s.toUpperCase().trim();
			if(StringUtils.isEmpty(s))
				continue;
			
			for(Country country:ls){
				if(s.equals(country.getCode()) )
					buf.append(country.getCode());
				else if(s.equals(country.getName()) )
					buf.append(country.getCode());
				else if(s.equals(country.getNameAbbr()) )
					buf.append(country.getCode());
			}
		}
		return buf.toString();
	}
	
	private void sendMessage(Long userId, String msg, StuZxgw stuZxgw){
		boolean bSuccess = false;
		Long toUserId = stuZxgw.getZxgwId();
		User u = userService.load(toUserId);
		
		//??????????????????
		try{
			SysMessage sysMsg = new SysMessage();
			sysMsg.setContent(msg);
			sysMsg.setOwnerId(toUserId);
			sysMsg.setSenderId(userId);
			sysMsg.setUrl("/lx/student/main.do?id=" + stuZxgw.getStuId() );
					
			sysMessageService.add(sysMsg);
		}
		catch(Exception e){
			getLogger().error("???????????????????????? toUser=" + toUserId, e);
		}
		
		//?????????????????????
		if (!isDevProfile() || isCanTestUser(toUserId)) {
			try {
				JSONObject json = wechatService.sendTxtMessage(msg, null,
						new Long[] { toUserId });
				if (json.getIntValue("errcode") == 0) {
					bSuccess = true;
				} else {
					getLogger().error("???????????????????????? toUser=" + toUserId);
				}
			} catch (Exception e) {
				getLogger().error("???????????????????????? toUser=" + toUserId, e);
			}
				
			// ???????????????????????? ???????????????
			if (!bSuccess && StringUtils.isNoneEmpty(u.getPhone())) {
				SmsMessage smsMessage = new SmsMessage();
				smsMessage.setContent(msg);
				smsMessage.setMobile(u.getPhone());
				smsMessage.setCreatorId(userId);

				smsService.send(smsMessage);
			}
		}
	}

    //??????????????????	
	public void updateSignDate(Long cstmId){
		Date firstDate = lxContractMapper.selectCustFirstSignDate(cstmId);
		LxCustomer lxCustomer = new LxCustomer();
		lxCustomer.setCstmId(cstmId);
		lxCustomer.setSignDate(firstDate);
		
		lxCustomerMapper.updateSignDate(lxCustomer);
	}

	@Override
	public List<LxStuLevel> loadStuLevelByStuId(Long stuId){
		return lxStuLevelMapper.selectByStuId(stuId);	
	}

	@Override
	public List<LxCustAssign> loadCustAssignByStuId(Long stuId){
		return lxCustAssignMapper.selectByStuId(stuId);	
	}
	
	/*
	private Dict getInitLevel(){
		return dictService.loadByCode("stulevel.10");
	} */
	
	@Override
	public int countStuZxgw(StudentSearchForm form) {
		return stuZxgwMapper.countStuZxgw(form);
	}

	@Override
	public List<LxCustomerZxgwVO> queryStuZxgw(StudentSearchForm form, Pageable pageable) {
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
		List<LxCustomerZxgwVO> ls = stuZxgwMapper.queryStuZxgw(form, pageable);
		for(LxCustomerZxgwVO vo:ls){
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
	
	//???????????????
	private Long getTeamLeader(Long zxgwId){
		ZxgwTeam zxgwTeam = zxgwTeamService.loadByZxgwId(zxgwId);
		if(zxgwTeam == null)
			return null;
		else
			return zxgwTeam.getLeaderId();
	}
}	
