package com.niu.crm.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
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
import com.niu.crm.service.SmsService;
import com.niu.crm.service.LxStuOperatorService;
import com.niu.crm.service.StuPlanCountryService;
import com.niu.crm.service.SysMessageService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.WechatService;
import com.niu.crm.form.PreCustSearchForm;
import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.core.error.BizException;
import com.niu.crm.dao.mapper.CustContractMapper;
import com.niu.crm.dao.mapper.LxPreCustMapper;
import com.niu.crm.dao.mapper.UserMapper;
import com.niu.crm.dao.mapper.CustomerMapper;
import com.niu.crm.model.Country;
import com.niu.crm.model.Dict;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.Customer;
import com.niu.crm.model.LxPreCust;
import com.niu.crm.model.type.PreCustStatus;
import com.niu.crm.util.CsvUtil;
import com.niu.crm.vo.LxCustomerVO;
import com.niu.crm.vo.LxPreCustVO;
import com.niu.crm.vo.RepeatCustVO;

@Service
public class LxPreCustServiceImpl extends BaseService implements
		LxPreCustService {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private LxCustomerService lxCustomerService;
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
	private LxStuOperatorService stuOperatorService;

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CustomerMapper customerMapper;
	@Autowired
	private LxPreCustMapper lxPreCustMapper;
		
	@Override
	public LxPreCust load(Long id) {
		return lxPreCustMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public LxPreCustVO loadVO(Long id) {
		LxPreCust preCust = lxPreCustMapper.selectByPrimaryKey(id);
		return loadVO(preCust);
	}
	
	@Override
	public LxPreCustVO loadVOByCstmId(Long cstmId) {
		LxPreCust preCust = lxPreCustMapper.selectByCstmId(cstmId);
		return loadVO(preCust);
	}
	
	private LxPreCustVO loadVO(LxPreCust preCust) {
		if (preCust == null) 
			return null;
		
		LxPreCustVO vo = new LxPreCustVO();
		BeanUtils.copyProperties(preCust, vo);


		Long stuFromId = vo.getStuFromId();
		if( stuFromId != null){
			Dict dict = dictService.load(stuFromId);
			if(dict !=null){
				Dict pDict = dictService.load(dict.getParentId());
				if(pDict.getDictCode().equals("stufrom"))
					vo.setStuFromName(dict.getDictName());
				else
					vo.setStuFromName(pDict.getDictName() + "-" + dict.getDictName());
			}else{
				vo.setStuFromName("lost-" + stuFromId);
			}
		}
		
		return vo;
	}

	@Transactional
	@Override
	public void insert(User user, Customer customer, LxPreCust preCust) {
		// 历史数据导入时 createdAt, updatedAt 都是已经有的
		if (preCust.getCreatedAt() == null)
			preCust.setCreatedAt(new java.util.Date(System
					.currentTimeMillis()));
		if (preCust.getUpdatedAt() == null)
			preCust.setUpdatedAt(preCust.getCreatedAt());

		if (customer.getCreatedAt() == null)
			customer.setCreatedAt(preCust.getCreatedAt());
		if (preCust.getUpdatedAt() == null)
			customer.setUpdatedAt(customer.getCreatedAt());

		if (customer.getId() == null) {
			customerService.insert(user, customer);
		} else {
			customerService.update(user, customer);
		}

		preCust.setCstmId(customer.getId());
		preCust.setCreatorId(user.getId());
		lxPreCustMapper.insert(preCust);
	}

	@Transactional
	@Override
	public void update(User user, Customer customer, LxPreCust preCust) {
		LxPreCust oldStu = lxPreCustMapper.selectByPrimaryKey(preCust.getId());
		// 更新时 允许不传cstmId, 也可以防止传了错误的cstmId
		{
			Long cstmId = oldStu.getCstmId();
			preCust.setCstmId(cstmId);
			customer.setId(cstmId);
		}

		customerService.update(user, customer);
		lxPreCustMapper.update(preCust);
		
	}
	
	@Transactional
	@Override
	public void updateByPrimaryKeySelective(User user, LxPreCust preCust) {
		lxPreCustMapper.updateByPrimaryKeySelective(preCust);
		
	}

	@Transactional
	@Override
	public int delete(User user, Long id) {
		int count = 0;
		LxPreCust preCust = lxPreCustMapper.selectByPrimaryKey(id);
		if (preCust != null) {
			lxPreCustMapper.delete(preCust.getId());
		}

		return count;
	}

	@Override
	public int countPreCust(PreCustSearchForm form) {
		return lxPreCustMapper.countPreCust(form);
	}

	@Override
	public List<LxPreCustVO> queryPreCust(PreCustSearchForm form, Pageable pageable) {
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
		List<LxPreCustVO> ls = lxPreCustMapper.queryPreCust(form, pageable);

		for(LxPreCustVO vo:ls){
			Dict dict = null;
			if(vo.getCompanyId() !=null){
				Unit unit = unitService.load(vo.getCompanyId());
				vo.setCompanyName(unit.getName());
			}
			
			Long stuFromId = vo.getStuFromId();
			if( stuFromId != null){
				dict = dictService.load(stuFromId);
				if(dict !=null){
					Dict pDict = dictService.load(dict.getParentId());
					if(pDict.getDictCode().equals("stufrom"))
						vo.setStuFromName(dict.getDictName());
					else
						vo.setStuFromName(pDict.getDictName() + "-" + dict.getDictName());
				}else{
					vo.setStuFromName("lost-" + stuFromId);
				}
			}
		}
		
		return ls;
	}

	/**
	 * 导入客户数据
	 * 
	 * @param file
	 * @return 返回(count, msg)
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
		// 其中的 0xefbb、0xfffe、0xfeff、0x5c75这些都是这个文件的前面两个字节的16进制数

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
			this.getLogger().info("fileName=" + fileName, "没有数据");
			map.put("msg", "没有数据");
			map.put("count", "0");
			return map;
		}

		// 姓名 性别 在读学校 目前学历 联系方式（手机） 备用电话 邮箱
		// 意向国 是否需要培训 资源来源 所在城市 微信 客户身份
		// 申请学历 申请专业 入学时间 基本情况 GPA 备注
		String[] flds = new String[] { "name", "gender", "currSchool",
				"currGrade", "mobile", "phone", "email", "planCountry",
				"pxRequire", "stuFrom", "stuCity", "wechat", "visitorType",
				"planXl", "hopeSpecialty", "enterYear", "basicInfo", "gpa",
				"memo" };

		StringBuffer bufMsg = new StringBuffer();
		Customer customer = null;
		LxPreCust preCust = null;
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
			preCust = new LxPreCust();
			preCust.setCompanyId(companyId);
			preCust.setStatus(PreCustStatus.INIT);

			String custName = mapVal.get("name");
			String mobile = mapVal.get("mobile");
			String gender = mapVal.get("gender");

			if (StringUtils.isEmpty(mobile))
				continue;

			// 查重
			List<RepeatCustVO> lsRepeat = lxCustomerService.queryRepeat2(null, mobile);
			if (lsRepeat.size() > 0) {
				bufMsg.append(mobile + "重复 <br/>");
				continue;
			}

			customer.setName(custName);
			if (StringUtils.isNotEmpty(gender)) {
				if (gender.equals("男") || gender.equalsIgnoreCase("M"))
					customer.setGender("M");
				else if (gender.equals("女") || gender.equalsIgnoreCase("F"))
					customer.setGender("F");
			}

			preCust.setCurrSchool(mapVal.get("currSchool"));
			preCust.setCurrGrade(mapVal.get("currGrade"));
			customer.setMobile(mobile);
			customer.setPhone(mapVal.get("phone"));
			customer.setEmail(mapVal.get("email"));
			preCust.setPlanCountry( convertPlanCountry(mapVal.get("planCountry")));
			preCust.setPxRequire(mapVal.get("pxRequire"));
			preCust.setStuFromId(stuFromId);
			preCust.setStuCity(mapVal.get("stuCity"));
			customer.setWechat(mapVal.get("wechat"));
			preCust.setVisitorType(mapVal.get("visitorType"));
			preCust.setPlanXl(mapVal.get("planXl"));
			preCust.setHopeSpecialty(mapVal.get("hopeSpecialty"));
			preCust.setBasicInfo(mapVal.get("basicInfo"));
			preCust.setGpa(mapVal.get("gpa"));

			String szMemo = mapVal.get("memo");
			if (StringUtils.isNoneEmpty(mapVal.get("enterYear"))) {
				String szEnterYear = mapVal.get("enterYear").trim();
				Pattern pattern = Pattern.compile("20[12][0-9]年[春秋]季");
				if (pattern.matcher(szEnterYear).matches()) {
					preCust.setPlanEnterYear(Integer.valueOf(szEnterYear
							.substring(0, 4)));
					preCust.setPlanEnterSeason(szEnterYear.substring(5));
				} else {
					if (szMemo.length() == 0)
						szMemo = "入学时间:" + mapVal.get("enterYear");
					else
						szMemo = "入学时间:" + mapVal.get("enterYear") + "\r\n"
								+ szMemo;
				}
			}
			preCust.setMemo(szMemo);

			this.insert(user, customer, preCust);
			
			importCount++;
		}

		map.put("count", String.valueOf(importCount));
		map.put("msg", bufMsg.toString());

		return map;
	}

	@Override
	public List<RepeatCustVO> queryRepeat(Long exclueCstmId, String... phones){
		List<RepeatCustVO> lsRepeat = new ArrayList<>();
		if(phones == null || phones.length == 0)
			return lsRepeat;
		
		//检查种子库
		List<LxPreCustVO> ls = lxPreCustMapper.queryRepeat(exclueCstmId, phones);
		for (int i = 0; ls != null && i < ls.size(); i++) {
			LxPreCustVO vo = ls.get(i);
			if (vo.getStuFromId() != null) {
				Dict dict = dictService.load(vo.getStuFromId());
				if (dict != null)
					vo.setStuFromName(dict.getDictName());
			}
			RepeatCustVO repeat = new RepeatCustVO();
			lsRepeat.add(repeat);
			
			BeanUtils.copyProperties(vo, repeat);			
			repeat.setPrecust(Boolean.TRUE);
			repeat.setStuId( vo.getId() );
			repeat.setCstmId( vo.getCstmId() );
			repeat.setCstmName( vo.getCustomer().getName() );
			repeat.setMobile( vo.getCustomer().getMobile() );
		}
		
		return lsRepeat;
	}

	private String convertPlanCountry(String planCountry) {
		if(StringUtils.isEmpty(planCountry))
			return null;
		planCountry = planCountry.trim();
		//将中文逗号 转换为西文
		String[] arr = planCountry.split("[,;，]");
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
}	
