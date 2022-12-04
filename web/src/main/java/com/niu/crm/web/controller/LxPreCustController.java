package com.niu.crm.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.niu.crm.model.Country;
import com.niu.crm.model.CustPhone;
import com.niu.crm.model.Customer;
import com.niu.crm.model.Dict;
import com.niu.crm.model.LxContactRecord;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.LxPreCust;
import com.niu.crm.model.PreContactRecord;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.UserFunc;
import com.niu.crm.model.type.CallbackType;
import com.niu.crm.model.type.PreCustStatus;
import com.niu.crm.service.CountryService;
import com.niu.crm.service.LxCustomerService;
import com.niu.crm.service.LxPreCustService;
import com.niu.crm.service.PreContactRecordService;
import com.niu.crm.service.StuPlanCountryService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.DictService;
import com.niu.crm.vo.LxPreCustVO;
import com.niu.crm.vo.RepeatCustVO;
import com.niu.crm.vo.UserFuncVO;
import com.niu.crm.form.PreCustSearchForm;
import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;

@Controller
@RequestMapping(value = "/lx/precust")
public class LxPreCustController extends BaseController {
	@Autowired
	private DictService dictService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private UserService userService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private LxCustomerService lxCustomerService;
	@Autowired
	private PreContactRecordService preContactRecordService;
	@Autowired
	private LxPreCustService lxPreCustService;
	@Autowired
	private UserFuncService userFuncService;
	
	/**
	 * 客户管理
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list.do")
	public String list(ModelMap model) {
		User user = this.getCurrentUser();
		model.addAttribute("countryList", countryService.loadAll());
		model.addAttribute("xlList", dictService.loadChildren("xl"));
		model.addAttribute("gradeList", dictService.loadChildren("grade"));
		model.addAttribute("companyList", unitService.getAllCompany());
		model.addAttribute("levelList", dictService.loadChildren("stulevel"));

		return "lx/precust/list";
	}

	/**
	 * 列出客户信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listData.do")
	@ResponseBody
	public Map<String, Object> listData(HttpServletRequest req,
			PreCustSearchForm form, Pageable pager) {
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);

		Map<String, Object> map = new java.util.HashMap<String, Object>();
		
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "lx_precust_query");
		if(func == null){
			map.put("total", 0);
			map.put("rows", new ArrayList<LxPreCustVO>() );

			return map;
		}
		

		int total = lxPreCustService.countPreCust(form);
		List<LxPreCustVO> ls = lxPreCustService.queryPreCust(form, pager);
		
		for(LxPreCustVO vo:ls){
			processVOWhenList(vo, false);
		}

		map.put("total", total);
		map.put("rows", ls);

		return map;
	}

	/**
	 * 客户信息录入表格
	 * 
	 * @return
	 */
	@RequestMapping(value = "/precust.do")
	public String detail(Long cstmId, ModelMap model) {
		LxPreCustVO lxPrecust = null;
		Customer customer = null;

		if (cstmId != null) {
			customer = customerService.load(cstmId);
			List<CustPhone> custPhones = customerService.loadCustPhone(cstmId, Boolean.FALSE);
			customer.setCustPhones(custPhones);
			
			lxPrecust = lxPreCustService.loadVOByCstmId(cstmId);
			if(lxPrecust == null){
				lxPrecust = new LxPreCustVO();
			}
		} else {
			customer = new Customer();
			lxPrecust = new LxPreCustVO();
		}

		inquireModel(model);
		
		if(lxPrecust.getStuFromId() !=null){
			Dict dict = dictService.load(lxPrecust.getStuFromId());
			if(dict !=null){
				Dict pDict = dictService.load(dict.getParentId());
				if(pDict.getDictCode().equals("stufrom"))
					lxPrecust.setStuFromName(dict.getDictName());
				else
					lxPrecust.setStuFromName(pDict.getDictName() + "-" + dict.getDictName());
			}else{
				lxPrecust.setStuFromName("lost-" + lxPrecust.getStuFromId());
			}
		}

		if (lxPrecust.getCompanyId() != null) {
			Long companyId = lxPrecust.getCompanyId();
			Unit company = unitService.load(companyId);

			lxPrecust.setCompanyName(company.getName());
		}

		List<Country> lsPlanCountry = new ArrayList<>();
		if(StringUtils.isNotEmpty(lxPrecust.getPlanCountry())){
			String[] codes = lxPrecust.getPlanCountry().split(",");
			for(int i=0; codes !=null && i < codes.length; i++){
				Country country = countryService.loadByCode(codes[i]);
				if(country !=null)
					lsPlanCountry.add(country);
			}
		}
		
		
		model.addAttribute("lsPlanCountry", lsPlanCountry);
		model.addAttribute("stu", lxPrecust);
		model.addAttribute("cust", customer);

		return "lx/precust/lxPrecust";
	}

	private void inquireModel(ModelMap model) {
		model.addAttribute("user", this.getCurrentUser());
		model.addAttribute("companyList", unitService.getAllCompany());
		model.addAttribute("visitorTypeList",dictService.loadChildren("visitortype"));
		model.addAttribute("xlList", dictService.loadChildren("xl"));
		model.addAttribute("gradeList", dictService.loadChildren("grade"));
		model.addAttribute("contactTypeList", dictService.loadChildren("contacttype") );
		model.addAttribute("countryList", countryService.loadAll());
		model.addAttribute("stufrom", dictService.loadChildren("stufrom"));
	}

	/**
	 * 客户信息录入表格
	 * 
	 * @return
	 */
	@RequestMapping(value = "/checkRepeat.do")
	@ResponseBody
	public ResponseObject<List<RepeatCustVO>> checkRepeat(Long cstmId,
			String... phones) {

		List<RepeatCustVO> ls = lxCustomerService.queryRepeat2(cstmId, phones);

		for (RepeatCustVO vo : ls) {
			if (vo.getCompanyId() != null) {
				Unit unit = unitService.load(vo.getCompanyId());
				vo.setCompanyName(unit.getName());
			}
		}
		
		return new ResponseObject<List<RepeatCustVO>>(ls);
	}

	/**
	 * 保存客户信息到数据库
	 * 
	 * @return
	 */
	@RequestMapping(value = "/save.do")
	@ResponseBody
	public ResponseObject<LxPreCust> save(Customer customer, LxPreCust precust) {
		User user = this.getCurrentUser();
		customer.setId(precust.getCstmId());

		List<CustPhone> custPhones = customer.getCustPhones();
		if( custPhones == null )
			custPhones = new ArrayList<>();
		
		// 查重
		String[] phones = null;
		{
			List<String> lsPhone = new ArrayList<>();
			for (CustPhone custPhone : custPhones) {
				lsPhone.add(custPhone.getPhone());
			}

			phones = new String[lsPhone.size()];
			lsPhone.toArray(phones);
		}
		

		List<RepeatCustVO> lsRepeat = lxPreCustService.queryRepeat(precust.getCstmId(), phones);
		if (lsRepeat.size() > 0) {
			throw new BizException(GlobalErrorCode.INTERNAL_ERROR,
					"存在相同电话号码的客户");
		}

		if (precust.getId() == null) {
			lxPreCustService.insert(user, customer, precust);
		} else {
			lxPreCustService.update(user, customer, precust);
		}
		
		{
			LxPreCust obj = new LxPreCust();
			obj.setId(precust.getId());
			obj.setStatus(precust.getStatus());
			lxPreCustService.updateByPrimaryKeySelective(user, obj);
		}
				
		if(precust.getStatus().equals(PreCustStatus.MOVED)){
			LxCustomer student = new LxCustomer();
			student.setCstmId(customer.getId());
			student.setCompanyId(precust.getCompanyId() );
			student.setUnitId( precust.getCompanyId() );
			student.setStuFromId(precust.getStuFromId());
			student.setCurrSchool( precust.getCurrSchool() );
			student.setCurrGrade( precust.getCurrGrade() );
			student.setStuCity(precust.getStuCity());
			student.setGpa(precust.getGpa());
			
			student.setCurrentSpecialty( precust.getCurrentSpecialty());
			student.setPlanXl( precust.getPlanXl() );
			
			student.setPlanEnterYear(precust.getPlanEnterYear());
			student.setPlanEnterSeason(precust.getPlanEnterSeason());

			student.setPlanCountry(precust.getPlanCountry());
			
			student.setBasicInfo(precust.getBasicInfo());
			student.setMemo( precust.getMemo() );
			
			lxCustomerService.add(user, customer, student);
		}
		
		return new ResponseObject<LxPreCust>(precust);
	}

	/**
	 * 删除客户信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/delete.do")
	@ResponseBody
	public ResponseObject<Integer> delete(Long id) {
		User user = this.getCurrentUser();

		int count = 0;
		UserFunc queryFunc = userFuncService.loadByCode(user.getId(), "admin");
		if (queryFunc != null && id != null)
			count = lxPreCustService.delete(user, id);

		return new ResponseObject<Integer>(count);
	}

	/**
	 * 导入客户信息--界面
	 * @return
	 */
	@RequestMapping(value = "/importCust.do")
	public String importCust(ModelMap model) {
		User user = this.getCurrentUser();
		model.addAttribute("user", user);
		model.addAttribute("companyList", unitService.getAllCompany());

		return "lx/precust/importCust";
	}

	/**
	 * 录入客户信息到数据库
	 * 
	 * @return
	 */
	@RequestMapping(value = "/doImportCust.do")
	public String doImportCust(
			@RequestParam(value = "file") MultipartFile file,
			@RequestParam(value = "companyId") Long companyId,
			@RequestParam(value = "unitId") Long unitId,
			@RequestParam(value = "stuFromId") Long stuFromId,
			HttpServletRequest request, ModelMap model) {
		User user = this.getCurrentUser();
		model.addAttribute("user", user);
		model.addAttribute("companyList", unitService.getAllCompany());

		String fileName = (file == null) ? null : file.getOriginalFilename()
				.toLowerCase();

		if (fileName == null || !fileName.endsWith(".csv")) {
			model.addAttribute("msg", "只支持csv文件导入");
		} else {
			// 回传count/msg
			Map<String, String> map = null;
			try {
				if (null == unitId)
					unitId = companyId;
				map = lxPreCustService.importCustomer(user, unitId, stuFromId, file);

			} catch (BizException e) {
				getLogger().error("", e);
				throw e;
			} catch (Exception e) {
				getLogger().error("", e);
				throw new BizException(GlobalErrorCode.INTERNAL_ERROR,
						e.getMessage());
			}
			if (map == null) {
				model.addAttribute("count", "0");
				model.addAttribute("msg", "系统异常");
			} else {
				model.addAttribute("count", map.get("count"));
				String msg = map.get("msg");
				if (StringUtils.isEmpty(msg))
					model.addAttribute("msg", "导入成功");
				else
					model.addAttribute("msg", msg);
			}
		}

		return "lx/precust/importCust";
	}
	
	private void processVOWhenList(LxPreCustVO vo, boolean bExport){
		Long cstmId = vo.getCstmId();
		//屏蔽mobile/phone/email
		if( !bExport ){
			vo.getCustomer().setMobile("");
			vo.getCustomer().setPhone("");
			vo.getCustomer().setEmail("");
		}
		
		List<PreContactRecord> lsRecords = preContactRecordService.queryLast(cstmId, null, 3);
		for(PreContactRecord record:lsRecords){
			User u = userService.load(record.getGwId());
			if(u != null)
				record.setGwName(u.getName());
		}
		vo.setLastContactRecords(lsRecords);
		
		vo.setPlanCountryName( translateCountry(vo.getId(),vo.getPlanCountry()) );

		if( vo.getCreatorId() !=null)
			vo.setCreatorName( userService.load(vo.getCreatorId()).getName() );
	}

	private String translateCountry(Long stuId, String countryCodes) {
		if (StringUtils.isEmpty(countryCodes))
			return "";

		StringBuffer buf = new StringBuffer();
		String[] codes = countryCodes.split(",");
		for (int i = 0; codes != null && i < codes.length; i++) {
			if (i > 0)
				buf.append(",");

			Country country = countryService.loadByCode(codes[i]);

			String s = country == null ? codes[i] : country.getNameAbbr();
			buf.append(s);
		}
		return buf.toString();
	}
}
