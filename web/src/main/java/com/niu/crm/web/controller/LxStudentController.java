package com.niu.crm.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.niu.crm.model.Country;
import com.niu.crm.model.CustPhone;
import com.niu.crm.model.Customer;
import com.niu.crm.model.Dict;
import com.niu.crm.model.LxContactRecord;
import com.niu.crm.model.LxCustAssign;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.LxStuLevel;
import com.niu.crm.model.StuPlanCountry;
import com.niu.crm.model.StuSingleShare;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.UserFunc;
import com.niu.crm.model.type.AclScope;
import com.niu.crm.model.type.CallbackRemindType;
import com.niu.crm.model.type.CallbackType;
import com.niu.crm.service.CountryService;
import com.niu.crm.service.LxContactRecordService;
import com.niu.crm.service.LxStuOperatorService;
import com.niu.crm.service.LxStuZxgwService;
import com.niu.crm.service.StuPlanCountryService;
import com.niu.crm.service.StuSingleShareService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.LxCustomerService;
import com.niu.crm.vo.LxCustAssignVO;
import com.niu.crm.vo.LxCustomerVO;
import com.niu.crm.vo.LxCustomerZxgwVO;
import com.niu.crm.vo.LxStuLevelVO;
import com.niu.crm.vo.MyCustomerVO;
import com.niu.crm.vo.RepeatCustVO;
import com.niu.crm.vo.UserFuncVO;
import com.niu.crm.form.StuShareSearchForm;
import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;


@Controller
@RequestMapping(value = "/lx/student")
public class LxStudentController extends BaseController{
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
	private LxStuZxgwService stuZxgwService;
	@Autowired
	private LxContactRecordService contactRecordService;
	@Autowired
	private UserFuncService userFuncService;
	@Autowired
	private StuPlanCountryService stuPlanCountryService;
	@Autowired
	private StuSingleShareService singleShareService;
	@Autowired
	private LxStuOperatorService stuOperatorService;

	/**
	 * ?????????
	 * @return
	 */
	@RequestMapping(value = "/pool.do")
    public String pool(ModelMap model){
		
		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("xlList", dictService.loadChildren("xl") );
		model.addAttribute("gradeList", dictService.loadChildren("grade") );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("levelList", dictService.loadChildren("stulevel") );
		
		return "lx/student/pool";
	}
	
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping(value = "/youngList.do")
    public String youngList(ModelMap model){

		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("xlList", dictService.loadChildren("xl") );
		model.addAttribute("gradeList", dictService.loadChildren("grade") );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("levelList", dictService.loadChildren("stulevel") );
		
		return "lx/student/youngList";
	}
	
	
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping(value = "/list.do")
    public String list(ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("xlList", dictService.loadChildren("xl") );
		model.addAttribute("gradeList", dictService.loadChildren("grade") );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("levelList", dictService.loadChildren("stulevel") );
		
		model.addAttribute("canExport", canExport(user) );
		
		return "lx/student/list";
	}
	
	private boolean canExport(User user){
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "custexport");
		if(func !=null)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}
	
	/**
	 * ???????????????
	 * @return
	 */
	@RequestMapping(value = "/myStudents.do")
    public String myStudents(ModelMap model){
		
		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("xlList", dictService.loadChildren("xl") );
		model.addAttribute("gradeList", dictService.loadChildren("grade") );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("levelList", dictService.loadChildren("stulevel") );
		
		return "lx/student/myStudents";
	}
	
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping(value = "/myStudentsData.do")
    @ResponseBody
    public Map<String, Object> myStudentsData(HttpServletRequest req,StudentSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		form.setZxgw_id(user.getId());
		if(ArrayUtils.isEmpty(form.getStu_level() ) )
			form.setExclude_stu_level(new String[]{"30","31"});	
		
		pager = this.convertPager(req, pager);
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		
		int total = lxCustomerService.countStuZxgw(form);
		List<LxCustomerZxgwVO> ls = lxCustomerService.queryStuZxgw(form, pager);
		
		for(LxCustomerZxgwVO vo:ls){
			processVOWhenList(vo, false);
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping(value = "/myCreatedStudents.do")
    public String myCreatedStudents(ModelMap model){
		
		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("xlList", dictService.loadChildren("xl") );
		model.addAttribute("gradeList", dictService.loadChildren("grade") );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("levelList", dictService.loadChildren("stulevel") );
		
		return "lx/student/myCreatedStudents";
	}
	
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping(value = "/myCreatedStudentsData.do")
    @ResponseBody
    public Map<String, Object> myCreatedStudentsData(HttpServletRequest req,StudentSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		form.setCreator_id(user.getId());
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		
		int total = lxCustomerService.countStudents(form);
		List<LxCustomerVO> ls = lxCustomerService.queryStudents(form, pager);
		
		for(LxCustomerVO vo:ls){
			processVOWhenList(vo, false);
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	/**
	 * ?????????????????????unitId
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
	 * ??????????????????
	 * @return
	 */
	@RequestMapping(value = "/listData.do")
    @ResponseBody
    public Map<String, Object> listData(HttpServletRequest req,StudentSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		
		setListClause(user, form);
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		
		int total = lxCustomerService.countStudents(form);
		List<LxCustomerVO> ls = lxCustomerService.queryStudents(form, pager);
		
		for(LxCustomerVO vo:ls){
			processVOWhenList(vo, false);
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping(value = "/exportData.do")
    @ResponseBody
    public void exportData(HttpServletRequest request, HttpServletResponse response,StudentSearchForm form){
		User user = this.getCurrentUser();
		Pageable pager = null;
		
		//fld-code-->fld-title
		List<String[]> lsFld = new ArrayList<String[]>();
		//List< Map(fld-code, fld-value)>
		List<Map<String,Object>> lsRows = new ArrayList<Map<String,Object>>();
		
		Sort.Direction dir = Sort.Direction.ASC;
		String szSort  = request.getParameter("sort");
		String szOrder = request.getParameter("order");
		if("desc".equalsIgnoreCase(szOrder))
			dir = Sort.Direction.DESC;
		
		if(StringUtils.isNoneBlank(szSort))
			pager = new PageRequest(0, 50000, dir, szSort);
		else
			pager = new PageRequest(0, 50000);
		
		setListClause(user, form);
		
		lsFld.add( new String[]{"companyName","??????"} );
		lsFld.add( new String[]{"cstmName","??????"} );
		lsFld.add( new String[]{"mobile","??????"} );
		lsFld.add( new String[]{"phone","????????????"} );
		lsFld.add( new String[]{"wechat", "??????"} );
		lsFld.add( new String[]{"qq", "QQ"} );
		lsFld.add( new String[]{"email", "Email"} );
		lsFld.add( new String[]{"stuCity", "????????????"} );
		lsFld.add( new String[]{"planCountry", "????????????"} );
		
		lsFld.add( new String[]{"currSchool", "????????????/????????????"});
		lsFld.add( new String[]{"currGrade", "????????????"});
		lsFld.add( new String[]{"planXl", "????????????"});
		lsFld.add( new String[]{"currentSpecialty", "????????????"});
		lsFld.add( new String[]{"hopeSpecialty", "????????????"});
		lsFld.add( new String[]{"enterTime", "????????????"});
		lsFld.add( new String[]{"stuLevelName", "????????????"});
		lsFld.add( new String[]{"zxgwNames", "????????????"});
		lsFld.add( new String[]{"contactRecords", "????????????"});
		lsFld.add( new String[]{"stuFromName1", "????????????1"});
		lsFld.add( new String[]{"stuFromName", "????????????"});
		lsFld.add( new String[]{"toCity", "????????????"});
		lsFld.add( new String[]{"lastContactDate", "??????????????????"});
		lsFld.add( new String[]{"createdAt", "????????????"});
		lsFld.add( new String[]{"creatorName", "?????????"});
		lsFld.add( new String[]{"memo", "??????"});
		
		List<LxCustomerVO> ls = null;
		if( canExport(user) )
			ls = lxCustomerService.queryStudents(form, pager);
		else
			ls = new ArrayList<LxCustomerVO>();
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;

		// ??????Excel?????????????????????Sheet???
		sheet = workbook.createSheet("sheet1");
		
		int rowNo =0;
		row = sheet.createRow(rowNo ++); 

		for(int i=0; i < lsFld.size(); i++){
			String[] pair = lsFld.get(i);
			
			cell = row.createCell(i); 
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(pair[1]));
		}
		
		for(LxCustomerVO vo:ls){
			processVOWhenList(vo,true);
			
			Map<String,Object> mapVal = new HashMap<String,Object>();
			lsRows.add(mapVal);
			
			StringBuffer contactRecords = new StringBuffer();
			List<LxContactRecord> lsContacts = vo.getLastContactRecords();
			for(int idx=0; lsContacts !=null && idx < lsContacts.size(); idx++){
				LxContactRecord record = lsContacts.get(idx);
				if(idx >0)
					contactRecords.append("\r\n");
				
				contactRecords.append("[" + record.getGwName() + "]" + record.getContactText());
			}
			
			Customer cust = vo.getCustomer();
			mapVal.put("companyName", vo.getCompanyName() );
			mapVal.put("cstmName", cust.getName() );
			mapVal.put("mobile", cust.getMobile());
			mapVal.put("phone", cust.getPhone() );
			mapVal.put("wechat", cust.getWechat() );
			mapVal.put("qq", cust.getQQ() );
			mapVal.put("email", cust.getEmail() );
			mapVal.put("stuCity", vo.getStuCity());
			mapVal.put("planCountry", translateCountry(vo.getId(),vo.getPlanCountry()) );
			
			mapVal.put("currSchool", vo.getCurrSchool());
			mapVal.put("currGrade", vo.getCurrGrade());
			mapVal.put("planXl", vo.getPlanXl());
			mapVal.put("currentSpecialty", vo.getCurrentSpecialty());
			mapVal.put("hopeSpecialty", vo.getHopeSpecialty());
			
			String enterTime = null;
			if(vo.getPlanEnterYear() !=null)
				enterTime = vo.getPlanEnterYear() + vo.getPlanEnterSeason();
			
			mapVal.put("enterTime", enterTime==null?"":enterTime);
			
			mapVal.put("stuLevelName", vo.getStuLevelName());
			mapVal.put("zxgwNames", vo.getZxgwNames()==null?"":vo.getZxgwNames());
			mapVal.put("contactRecords", contactRecords.toString());
	
			mapVal.put("stuFromName1", vo.getStuFromName1() );
			mapVal.put("stuFromName", vo.getStuFromName());
			
			mapVal.put("toCity", vo.getToCity());
			mapVal.put("lastContactDate", vo.getLastContactDate() );
			mapVal.put("createdAt", vo.getCreatedAt());
			
			mapVal.put("creatorName", vo.getCreatorName() );
			mapVal.put("memo", vo.getMemo());
		}
		
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		for(Map<String,Object> rowVal: lsRows){
			row = sheet.createRow(rowNo ++);
			
			for(int i=0; i < lsFld.size(); i++){
				String fldCode = lsFld.get(i)[0];
				
				cell = row.createCell(i); 
				
				Object valueObj = rowVal.get(fldCode);
				if(valueObj instanceof Integer){
					Integer value = (Integer)valueObj;
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					if(value != null)
						cell.setCellValue(value.intValue());
				} else if(valueObj instanceof Long){
					Long value = (Long)valueObj;
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					if(value != null)
						cell.setCellValue(value.longValue());
				} else if(valueObj instanceof BigDecimal){
					BigDecimal value = (BigDecimal)valueObj;
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					if(value != null)
						cell.setCellValue(value.doubleValue());
				} else if(valueObj instanceof java.util.Date){
					java.util.Date value = (java.util.Date)valueObj;
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if(value != null)
						cell.setCellValue(sdf.format(value));
				}else{
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if(valueObj == null)
						cell.setCellValue(new HSSFRichTextString(""));
					else
						cell.setCellValue(new HSSFRichTextString(valueObj.toString()));
				}
			}
		}
				
		String fileName = "????????????" + System.currentTimeMillis() + ".xls";		
		try{
			fileName = new String(fileName.getBytes("utf-8"),"iso8859-1");
			
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ fileName + "\"");
			response.setContentType("application/octet-stream; charset=UTF-8");
			
			java.io.OutputStream os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
			
			workbook.close();
		}catch(Exception e){
			logger.error("", e);
		}		
	}
	
	private void setListClause(User user, StudentSearchForm form){
		UserFuncVO queryFunc = userFuncService.loadByCode(user.getId(), "custquery");
		
		/**
		 * //??????  ????????????
		if(queryFunc !=null && 1==2 && "houyue".equalsIgnoreCase(user.getAccount())){
			StringBuffer buf = new StringBuffer();
			Long unitId = 45L;
			buf.append(" and (a.unit_id in(" + unitId);
			
			String childIds = childUnitIds( unitId );
			if(!StringUtils.isEmpty(childIds))
					buf.append("," + childIds);
			buf.append(")");
			
			buf.append(" or (a.company_id=4 and exists (");
			buf.append("select * from t_stu_plan_country where stu_id=a.id and country_code !='US'");
			buf.append("))");

			buf.append(")");	
			form.setAclClause( buf.toString() );
		}else 
		 */
		
		if( queryFunc ==null ){
			form.setAclClause(" and 1=2");
		}
		else if( StringUtils.isNotBlank(queryFunc.getClause()) ){
			form.setAclClause( queryFunc.getClause() );
		}
		else if( queryFunc.getAclScope() == AclScope.ALL){
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
		else if( queryFunc.getAclScope() == AclScope.SOMECOMPANY){
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
		else if( queryFunc.getAclScope() == AclScope.SELFCOMPANY){
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
			StringBuffer idBuf = new StringBuffer();
			Long[] unitIds = queryFunc.getUnitIdList();
			for(int i=0; i <unitIds.length; i++){
				if(i >0) idBuf.append(",");
				idBuf.append( unitIds[i] );
				String childIds = childUnitIds( unitIds[i] );
				if(!StringUtils.isEmpty(childIds))
					idBuf.append("," + childIds);
			}
			
			Long[] arrFromId = queryFunc.getFromIdList();
			StringBuffer buf = new StringBuffer();
			buf.append(" and (");
			buf.append("  a.unit_id in(" + idBuf.toString() + ")");
			
			buf.append(" or exists( ");
			buf.append(" select * from t_stu_zxgw zxgw, t_user user where zxgw_id=user.id ");
			buf.append("    and zxgw.stu_id=a.id and user.unit_id in (" + idBuf.toString() + ")");
			buf.append(" ) ");
			
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
	
			form.setAclClause( buf.toString() );
		}	
		else {
			//form.setAclClause(" and (a.zxgw_id=" + user.getId() + " or a.creator_id=" + user.getId() + ")" );
			form.setAclClause(" and 1=2");
		}
	}
	
	/**
	 * ????????????????????????
	 * @return
	 */
	@RequestMapping(value = "/listYoungData.do")
    @ResponseBody
    public Map<String, Object> listYoungData(HttpServletRequest req,StudentSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		
		UserFuncVO queryFunc = userFuncService.loadByCode(user.getId(), "youngquery");

		Map<String,Object> map = new java.util.HashMap<String, Object>();
		if(queryFunc == null){
			List<LxCustomerVO> ls = new ArrayList<LxCustomerVO>();
					
			map.put("total", 0);
			map.put("rows", ls);
			return map;
		}
			
 
		StringBuffer buf = new StringBuffer();
		buf.append(" and ( plan_xl in ('??????','??????','??????') or plan_xl like '%??????')");
		if( queryFunc !=null && queryFunc.getAclScope() == AclScope.ALL){
		}
		else if( queryFunc !=null && queryFunc.getAclScope() == AclScope.SOMECOMPANY){
			buf.append(" and a.company_id in(" + queryFunc.getCompanyIds() +") ");
		}	
		else {
			buf.append(" and a.company_id =" + user.getCompanyId() );
		}	
		form.setAclClause(buf.toString());
		
		this.getLogger().debug("pageNo" + pager.getPageNumber() + " pageSize=" + pager.getPageSize() );
		int total = lxCustomerService.countStudents(form);
		List<LxCustomerVO> ls = lxCustomerService.queryStudents(form, pager);
		
		for(LxCustomerVO vo:ls){
			processVOWhenList(vo,false);
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	private void processVOWhenList(LxCustomerVO vo, boolean bExport){
		Long stuId = vo.getId();
		//??????mobile/phone/email
		if( !bExport ){
			vo.getCustomer().setMobile("");
			vo.getCustomer().setPhone("");
			vo.getCustomer().setEmail("");
		}
		
		Dict dict = null;
		if( vo.getStuFromId() !=null){
			dict = dictService.load(vo.getStuFromId());
			String fromName = null;
			if(dict !=null){
				fromName = dict.getDictName(); 
				int idx = dict.getDictCode().indexOf(".",8);
				if(idx >0 && dict.getParentId() !=null){
					dict = dictService.load(dict.getParentId());
					fromName = dict.getDictName() + "-" + fromName; 
				}
				vo.setStuFromName(fromName);
				
				Dict pDict = dict;
				while(pDict.getLayer() >2)
					pDict = dictService.load( pDict.getParentId() );
				vo.setStuFromName1(pDict.getDictName() );
			}
		}
		vo.setPlanCountryName( translateCountry(vo.getId(),vo.getPlanCountry()) );

		if( vo.getOwnerId() !=null)
			vo.setOwnerName( userService.load(vo.getOwnerId()).getName() );
		if( vo.getCreatorId() !=null)
			vo.setCreatorName( userService.load(vo.getCreatorId()).getName() );
		
		//??????????????????????????????
		int showCount=3;
		//?????????1w???
		List<LxContactRecord> kfLastRecords = new ArrayList<>();
		List<LxContactRecord> gwLastRecords = new ArrayList<>();
		List<LxContactRecord> leaderLastRecords = new ArrayList<>(); 
		List<LxContactRecord> lastContactRecords = new ArrayList<>(); 
		List<LxContactRecord> lsRecords = contactRecordService.queryLast(stuId, null, 10000);
		for(int i=0; lsRecords !=null && i < lsRecords.size(); i++){
			LxContactRecord item= lsRecords.get(i);
			
			if(lastContactRecords.size() <showCount)
				lastContactRecords.add(item);
			
			CallbackType callType = item.getCallbackType();
			if(callType == null){				
			}else if( callType == CallbackType.KF){
				if(kfLastRecords.size() <showCount)
					kfLastRecords.add(item);
			}else if( callType == CallbackType.DAY5){
				if(leaderLastRecords.size() <showCount)
					leaderLastRecords.add(item);
			}else{
				if(gwLastRecords.size() <showCount)
					gwLastRecords.add(item);				
			}
		}

		vo.setKfLastContactRecords(kfLastRecords);
		vo.setGwLastContactRecords(gwLastRecords);
		vo.setLeaderLastContactRecords(leaderLastRecords);
		vo.setLastContactRecords(lastContactRecords);
	}
	
	private void processVOWhenList(LxCustomerZxgwVO vo, boolean bExport){
		Long stuId = vo.getStuId();
		//??????mobile/phone/email
		
		Customer customer = vo.getCustomer();
		if( !bExport ){
			customer.setMobile("");
			customer.setPhone("");
			customer.setEmail("");
		}
		
		LxCustomer student = vo.getStudent();
		
		if(student.getCompanyId() !=null){
			Unit unit = unitService.load(student.getCompanyId());
			student.setCompanyName(unit.getName());
		}

		if( student.getStuFromId() !=null){
			Dict dict = dictService.load(student.getStuFromId());
			String fromName = null;
			if(dict !=null){
				fromName = dict.getDictName(); 
				int idx = dict.getDictCode().indexOf(".",8);
				if(idx >0 && dict.getParentId() !=null){
					dict = dictService.load(dict.getParentId());
					fromName = dict.getDictName() + "-" + fromName; 
				}
				student.setStuFromName(fromName);
			}
		}
		student.setPlanCountryName( translateCountry(student.getId(), student.getPlanCountry()) );

		if( student.getOwnerId() !=null)
			student.setOwnerName( userService.load(student.getOwnerId()).getName() );
		
		//??????????????????????????????
		int showCount=3;
		List<LxContactRecord> lastContactRecords = contactRecordService.queryLast(stuId, vo.getZxgwId(), showCount);
		
		vo.setLastContactRecords(lastContactRecords);
	}
	
	/**
	 * ????????????????????????
	 * @return
	 */
	@RequestMapping(value ="/main.do")
    public String studentMain(Long id, ModelMap model){
		model.addAttribute("id", id );
		model.addAttribute("user", this.getCurrentUser() );
		return "lx/student/student";
	}
	
	private boolean canModifyStuFrom(User user, Long companyId){
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "modify_stufrom");
		if(func == null)
			return false;
		
		if( func.getAclScope() == AclScope.ALL)
			return true;
		if(func.getAclScope() == AclScope.SELF ){
			if(companyId.longValue() == user.getCompanyId().longValue())
				return true;
			else
				return false;
		}else{
			Long[] arrIds = func.getCompanyIdList();
			for(int i=0; arrIds !=null && i < arrIds.length; i++){
				if( arrIds[i].longValue() == companyId.longValue())
					return true;
			}
			return false;
		}
	}
	
	/**
	 * ????????????????????????
	 * @return
	 */
	@RequestMapping(value ="/inquireRecord.do")
    public String inquireRecord(Long id, ModelMap model){
		User user = this.getCurrentUser();
		Customer customer = null;
		
		LxCustomerVO lxCustomer = lxCustomerService.loadVO(id, null);
		
		customer = customerService.load(lxCustomer.getCstmId());
		List<CustPhone> custPhones = customerService.loadCustPhone(lxCustomer.getCstmId(), Boolean.FALSE);
		customer.setCustPhones(custPhones);
		/*
		{
			//???????????????
			StuShareSearchForm searchForm = new StuShareSearchForm();
			searchForm.setStuId(lxCustomer.getId());
			List<StuSingleShare> lsShare = singleShareService.queryShare(searchForm, null);
			model.addAttribute("shareList", lsShare);
		}*/
		inquireModel(model);
		
		if(lxCustomer.getUnitId() !=null){ 
			Long companyId = lxCustomer.getCompanyId();
			Unit company = unitService.load(companyId);
			String szUnitName = unitService.getFullName(lxCustomer.getUnitId());
			
			lxCustomer.setUnitName(szUnitName);
			lxCustomer.setCompanyName(company.getName());
		}
		
		
		if(lxCustomer.getStuFromId() !=null){
			Dict dict = dictService.load(lxCustomer.getStuFromId());
			String stuFromName = dict.getDictName();
			if(dict.getParentId() !=null){
				dict = dictService.load(dict.getParentId());
				stuFromName = dict.getDictName() + "-" + stuFromName;
			}
			lxCustomer.setStuFromName(stuFromName);
		}
		
		if(lxCustomer.getOwnerId() !=null){ 
			User creator = userService.load(lxCustomer.getOwnerId());
			lxCustomer.setOwnerName(creator.getName());
		}
		
		if(lxCustomer.getCreatorId() !=null){ 
			User creator = userService.load(lxCustomer.getCreatorId());
			lxCustomer.setCreatorName(creator.getName());
		}
		
		List<StuPlanCountry> lsPlanCountry = stuPlanCountryService.loadByStuId(id);
		model.addAttribute("lsPlanCountry", lsPlanCountry);
		model.addAttribute("stu", lxCustomer);
		model.addAttribute("cust", customer);
		
		model.addAttribute("isZxgw", this.isZxgw(lxCustomer));
		model.addAttribute("canModifyStuFrom", canModifyStuFrom(user, lxCustomer.getCompanyId()) );
		
		return "lx/student/inquireRecord";
	}
	
	/**
	 * ????????????????????????
	 * @return
	 */
	@RequestMapping(value = "/add.do")
    public String add(Long cstmId, ModelMap model){
		LxCustomerVO lxCustomer = null;
		Customer customer = null;

		lxCustomer = new LxCustomerVO();
		if(cstmId != null){
			customer = customerService.load(lxCustomer.getCstmId());
			lxCustomer.setCstmId(cstmId);
		}
		else{
			customer = new Customer();
		}
		inquireModel(model);
		
		List<StuPlanCountry> lsPlanCountry = new ArrayList<StuPlanCountry>();
		model.addAttribute("lsPlanCountry", lsPlanCountry);
		model.addAttribute("stu", lxCustomer);
		model.addAttribute("cust", customer);
		
		model.addAttribute("isZxgw", Boolean.FALSE);
		model.addAttribute("canModifyStuFrom", Boolean.TRUE );
		return "lx/student/inquireRecord";
	}
	
	private void inquireModel(ModelMap model){
		model.addAttribute("user", this.getCurrentUser() );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("visitorTypeList", dictService.loadChildren("visitortype") );		
		model.addAttribute("xlList", dictService.loadChildren("xl") );
		model.addAttribute("gradeList", dictService.loadChildren("grade") );
		model.addAttribute("levelList", dictService.loadChildren("stulevel") );
		model.addAttribute("contactTypeList", dictService.loadChildren("contacttype") );

		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("ccLevelList", dictService.loadChildren("cclevel") );
		model.addAttribute("stufrom", dictService.loadChildren("stufrom"));
		model.addAttribute("callbackTypes", CallbackType.values());
	}
	
	/**
	 * ????????????????????????
	 * @return
	 */
	@RequestMapping(value = "/checkRepeat.do")
    @ResponseBody
    public ResponseObject<List<RepeatCustVO>> checkRepeat(Long cstmId, String... phones){

    	List<RepeatCustVO> ls = lxCustomerService.queryRepeat2(cstmId, phones);
    	
	    for(RepeatCustVO vo:ls){
	    	if(vo.getCompanyId() !=null){
	    		Unit unit = unitService.load(vo.getCompanyId());
	    		vo.setCompanyName(unit.getName());
	    	}
	    }
    	
    	return new ResponseObject<List<RepeatCustVO>>(ls);
	}
	
	/**
	 * ??????????????????????????????
	 * @return
	 */
	@RequestMapping(value = "/save.do")
    @ResponseBody
    public ResponseObject<LxCustomer> save(Customer customer, LxCustomer lxCustomer){
		User user = this.getCurrentUser();
		Long cstmId = lxCustomer.getCstmId();
    	customer.setId( cstmId );
    	
		//??????
    	List<String> lsPhone = new ArrayList<>();
    	lsPhone.add(customer.getMobile());
		if (customer.getCustPhones() != null) {
			for (CustPhone custPhone : customer.getCustPhones()) {
				String szPhone = custPhone.getPhone();
				if(StringUtils.isEmpty(szPhone))
					szPhone = custPhone.getShowPhone();
				
				if (StringUtils.isNotEmpty(szPhone))
					lsPhone.add(szPhone);
			}
		}
    	String[] phones = new String[lsPhone.size()];
    	lsPhone.toArray(phones);
    	List<RepeatCustVO> lsRepeat =lxCustomerService.queryRepeat2(cstmId, phones);
    	if(lsRepeat.size() >0){
    		throw new BizException(GlobalErrorCode.INTERNAL_ERROR,"?????????????????????????????????");
    	}
    	
    	Unit unit = unitService.load(lxCustomer.getUnitId());
    	if (unit == null) {
    		String errMsg = "unit not found id=" + lxCustomer.getUnitId();
    		errMsg += " stu_id=" + lxCustomer.getId() + " mobile=" + customer.getMobile();
			getLogger().error(errMsg);
    	}
    	
    	lxCustomer.setCompanyId(unit.getCompanyId());
    	    					
		if(lxCustomer.getId() == null){
			lxCustomerService.add(user, customer, lxCustomer);
		}else{
			lxCustomerService.update(user, customer, lxCustomer);
		}
		return new ResponseObject<LxCustomer>(lxCustomer);
	}
	
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping(value = "/updateLevel.do")
    @ResponseBody
    public ResponseObject<Boolean> updateLevel(Long stuId, Long levelId){
		User user = this.getCurrentUser();
		Long zxgwId = user.getId();
		
		lxCustomerService.updateStuZxgwLevel(user, zxgwId, stuId, levelId);		
		
		return new ResponseObject<Boolean>(Boolean.TRUE);		
	}
	
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping(value = "/delete.do")
    @ResponseBody
    public ResponseObject<Integer> delete(Long id){
		User user = this.getCurrentUser();
		
		int count = 0;
		UserFunc queryFunc = userFuncService.loadByCode(user.getId(), "admin");
		if( queryFunc != null && id != null)
			count = lxCustomerService.delete(user, id);
		
		return new ResponseObject<Integer>(count);
	}
	
	/**
	 * ??????????????????--??????
	 * @return
	 */
	@RequestMapping(value = "/importCust.do")
    public String importCust(ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("user", user);
		model.addAttribute("companyList", unitService.getAllCompany() );
		
		return "lx/student/importCust";
	}
	
	/**
	 * ??????????????????????????????
	 * @return
	 */
	@RequestMapping(value = "/doImportCust.do")
    public String doImportCust(@RequestParam(value = "file") MultipartFile file, 
    		@RequestParam(value = "companyId") Long companyId, 
    		@RequestParam(value = "unitId") Long unitId, 
			@RequestParam(value = "stuFromId") Long stuFromId,
    		HttpServletRequest request, ModelMap model){
		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.YEAR) >2017){
			model.addAttribute("count", "0");
			model.addAttribute("msg", "???????????????");
			return "lx/student/importCust";			
		}
		
		User user = this.getCurrentUser();
		model.addAttribute("user", user);
		model.addAttribute("companyList", unitService.getAllCompany() );
		
		String fileName = (file==null)?null:file.getOriginalFilename().toLowerCase();
		
		if (fileName ==null || !fileName.endsWith(".csv")) {
			model.addAttribute("msg", "?????????csv????????????");
		}
		else{
			//??????count/msg
			Map<String,String> map = null;
			try {
				if(null==unitId)
					unitId = companyId;
				map = lxCustomerService.importCustomer(user, unitId, stuFromId, file);
				
			} catch (BizException e) {
				getLogger().error("", e);
				throw e;
			} catch (Exception e) {
				getLogger().error("", e);
				throw new BizException(GlobalErrorCode.INTERNAL_ERROR,
						e.getMessage());
			}
			if(map == null){
				model.addAttribute("count", "0");
				model.addAttribute("msg", "????????????");
			}
			else{
				model.addAttribute("count", map.get("count"));
				String msg = map.get("msg");
				if(StringUtils.isEmpty(msg))
					model.addAttribute("msg", "????????????");
				else
					model.addAttribute("msg", msg);
			}
		}
		
		
		return "lx/student/importCust";
	}
	
	/**
	 * ??????????????????????????????
	 * @return
	 */
	@RequestMapping(value = "/stuLevelLog.do")
    @ResponseBody
    public List<LxStuLevelVO> stuLevelLog(HttpServletRequest req, Long stuId){
		List<LxStuLevel> ls = lxCustomerService.loadStuLevelByStuId(stuId);
		List<LxStuLevelVO> lsVO = new ArrayList<>();
		for(LxStuLevel item:ls){
			LxStuLevelVO vo = new LxStuLevelVO();
			lsVO.add(vo);
			
			BeanUtils.copyProperties(item, vo);
			vo.setCreatorName( userService.load(vo.getCreatorId()).getName() );
			if( vo.getLevelId() !=null)
				vo.setLevelName( dictService.load(vo.getLevelId()).getDictName() );
		}
		return lsVO;
	}
	
	/**
	 * ????????????????????????
	 * @return
	 */
	@RequestMapping(value = "/listStuZxgw.do")
    @ResponseBody
    public Map<String, Object> listStuZxgw(Long stuId){
		User user = this.getCurrentUser();
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();		
		List<StuZxgw> ls = stuZxgwService.listByStuId(stuId);
				
		map.put("total", ls.size());
		map.put("rows", ls);
		
		return map;
	}
	
	/**
	 * ????????????  ???????????? inquireRecord.do ?????????????????????
	 * @return
	 */
	@RequestMapping(value = "assign/revoke.do")
    @ResponseBody
    public ResponseObject<Boolean> revokeAssign(Long stuId, Long[] zxgwIds){
		User user = this.getCurrentUser();
		for(Long zxgwId:zxgwIds){
			lxCustomerService.revokeAssignFromZxgw(user, stuId, zxgwId);
		}

		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	/**
	 * ??????????????????????????????
	 * @return
	 */
	@RequestMapping(value = "/stuAssignLog.do")
    @ResponseBody
    public List<LxCustAssignVO> stuAssignLog(HttpServletRequest req, Long stuId){
		User user = this.getCurrentUser();
		UserFuncVO adminFunc = userFuncService.loadByCode(user.getId(), "admin");
		
		List<LxCustAssign> ls = lxCustomerService.loadCustAssignByStuId(stuId);
		List<LxCustAssignVO> lsVO = new ArrayList<>();
		for(LxCustAssign item:ls){
			LxCustAssignVO vo = new LxCustAssignVO();
			lsVO.add(vo);
			
			BeanUtils.copyProperties(item, vo);
			if( adminFunc !=null )
				vo.setCreatorName( userService.load(vo.getCreatorId()).getName() );
			else
				vo.setCreatorId(null);
			
			if( vo.getZxgwId() !=null)
				vo.setZxgwName( userService.load(vo.getZxgwId()).getName() );
		}
		
		return lsVO;
	}
	
	/**
	 * ?????????????????????????????????
	 * @return
	 */
	@RequestMapping(value = "/fixOperator1.do")
    @ResponseBody
    public ResponseObject<Boolean> fixOperator(Long id){
		stuOperatorService.fix(id);
    	return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	/**
	 * ?????????????????????????????????
	 * @return
	 */
	@RequestMapping(value = "/fixOperator.do")
    @ResponseBody
    public ResponseObject<Boolean> fixOperator(){
		StudentSearchForm form = new StudentSearchForm();
		int total = lxCustomerService.countStudents(form);
		getLogger().info("total=" + total);
				
		int pageNo =0;
		while(true){
			PageRequest pager = new PageRequest(pageNo++, 200);
			List<LxCustomerVO> ls = lxCustomerService.queryStudents(form, pager);
			for(LxCustomerVO item:ls){
				stuOperatorService.fix(item.getId());
			}
			if(ls.size() < pager.getPageSize() ) break;
		}
    	return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	
	private String translateCountry(Long stuId, String countryCodes){
		if(StringUtils.isEmpty(countryCodes))
			return "";
		
		StringBuffer buf = new StringBuffer();
		String[] codes = countryCodes.split(",");
		for(int i=0; codes !=null && i < codes.length; i++){
			if(i>0)
				buf.append(",");
			
			Country country = countryService.loadByCode(codes[i]);
			
			String s = country==null?codes[i]:country.getNameAbbr();
			buf.append(s);
		}
		return buf.toString();
	}
	
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping(value = "/stuZxgwList.do")
    public String stuZxgwList(ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("xlList", dictService.loadChildren("xl") );
		model.addAttribute("gradeList", dictService.loadChildren("grade") );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("levelList", dictService.loadChildren("stulevel") );
		
		model.addAttribute("canExport", canExport(user) );
		
		return "lx/student/stuZxgwList";
	}
	
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping(value = "/stuZxgwData.do")
    @ResponseBody
    public Map<String, Object> stuZxgwData(HttpServletRequest req,StudentSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		
		String unitId = req.getParameter("unitId");
		if(StringUtils.isNotEmpty(unitId)){
			String unitCode = unitService.load(Long.parseLong(unitId)).getCode();
			form.setUnitCode(unitCode);
		}
		
		setListClause(user, form);
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		
		this.getLogger().debug("pageNo" + pager.getPageNumber() + " pageSize=" + pager.getPageSize() );
		int total = lxCustomerService.countStuZxgw(form);
		List<LxCustomerZxgwVO> ls = lxCustomerService.queryStuZxgw(form, pager);
		
		for(LxCustomerZxgwVO vo:ls){
			processVOWhenList(vo, false);
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping(value = "/exportStuZxgw.do")
    @ResponseBody
    public void exportStuZxgwData(HttpServletRequest request, HttpServletResponse response, StudentSearchForm form){
		User user = this.getCurrentUser();
		Pageable pager = null;
		
		//fld-code-->fld-title
		List<String[]> lsFld = new ArrayList<String[]>();
		//List< Map(fld-code, fld-value)>
		List<Map<String,Object>> lsRows = new ArrayList<Map<String,Object>>();
		
		Sort.Direction dir = Sort.Direction.ASC;
		String szSort  = request.getParameter("sort");
		String szOrder = request.getParameter("order");
		if("desc".equalsIgnoreCase(szOrder))
			dir = Sort.Direction.DESC;
		
		if(StringUtils.isNoneBlank(szSort))
			pager = new PageRequest(0, 50000, dir, szSort);
		else
			pager = new PageRequest(0, 50000);
		
		setListClause(user, form);
		

		lsFld.add( new String[]{"companyName","??????"} );
		lsFld.add( new String[]{"cstmName","??????"} );
		lsFld.add( new String[]{"mobile","??????"} );
		lsFld.add( new String[]{"phone","????????????"} );
		lsFld.add( new String[]{"wechat", "??????"} );
		lsFld.add( new String[]{"qq", "QQ"} );
		lsFld.add( new String[]{"email", "Email"} );
		lsFld.add( new String[]{"stuCity", "????????????"} );
		lsFld.add( new String[]{"planCountry", "????????????"} );
		
		lsFld.add( new String[]{"currSchool", "????????????/????????????"});
		lsFld.add( new String[]{"currGrade", "????????????"});
		lsFld.add( new String[]{"planXl", "????????????"});
		lsFld.add( new String[]{"currentSpecialty", "????????????"});
		lsFld.add( new String[]{"hopeSpecialty", "????????????"});
		lsFld.add( new String[]{"enterTime", "????????????"});
		lsFld.add( new String[]{"stuLevelName", "????????????"});
		lsFld.add( new String[]{"zxgwName", "????????????"});
		lsFld.add( new String[]{"contactRecords", "????????????"});
		lsFld.add( new String[]{"stuFromName", "????????????"});
		lsFld.add( new String[]{"memo", "??????"});		
		
		List<LxCustomerZxgwVO> ls = null;
		if( canExport(user) )
			ls = lxCustomerService.queryStuZxgw(form, pager);
		else
			ls = new ArrayList<LxCustomerZxgwVO>();
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;

		// ??????Excel?????????????????????Sheet???
		sheet = workbook.createSheet("sheet1");
		
		int rowNo =0;
		row = sheet.createRow(rowNo ++); 

		for(int i=0; i < lsFld.size(); i++){
			String[] pair = lsFld.get(i);
			
			cell = row.createCell(i); 
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(pair[1]));
		}
		
		for(LxCustomerZxgwVO vo:ls){
			processVOWhenList(vo, true);
			
			Map<String,Object> mapVal = new HashMap<String,Object>();
			lsRows.add(mapVal);
			
			StringBuffer contactRecords = new StringBuffer();
			List<LxContactRecord> lsContacts = vo.getLastContactRecords();
			for(int idx=0; lsContacts !=null && idx < lsContacts.size(); idx++){
				LxContactRecord record = lsContacts.get(idx);
				if(idx >0)
					contactRecords.append("\r\n");
				
				contactRecords.append("[" + record.getGwName() + "]" + record.getContactText());
			}
			
			Customer cust = vo.getCustomer();
			LxCustomer stu = vo.getStudent();
			
			mapVal.put("companyName", stu.getCompanyName() );
			mapVal.put("cstmName", cust.getName() );
			mapVal.put("mobile", cust.getMobile());
			mapVal.put("phone", cust.getPhone() );
			mapVal.put("wechat", cust.getWechat() );
			mapVal.put("qq", cust.getQQ() );
			mapVal.put("email", cust.getEmail() );
			mapVal.put("stuCity", vo.getStudent().getStuCity());
			mapVal.put("planCountry", translateCountry(vo.getId(),vo.getStudent().getPlanCountry()) );
			
			mapVal.put("currSchool", stu.getCurrSchool());
			mapVal.put("currGrade", stu.getCurrGrade() );
			mapVal.put("planXl", stu.getPlanXl());
			mapVal.put("currentSpecialty", stu.getCurrentSpecialty());
			mapVal.put("hopeSpecialty", stu.getHopeSpecialty());
			
			String enterTime = null;
			if(stu.getPlanEnterYear() !=null)
				enterTime = stu.getPlanEnterYear() + stu.getPlanEnterSeason();
			
			mapVal.put("enterTime", enterTime==null?"":enterTime);
			
			mapVal.put("stuLevelName", vo.getStuLevelName());
			mapVal.put("zxgwName",vo.getZxgwName()==null?"":vo.getZxgwName());
			mapVal.put("contactRecords", contactRecords.toString());
	
			mapVal.put("stuFromName", stu.getStuFromName());
			mapVal.put("memo", stu.getMemo());
		}
		
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		for(Map<String,Object> rowVal: lsRows){
			row = sheet.createRow(rowNo ++);
			
			for(int i=0; i < lsFld.size(); i++){
				String fldCode = lsFld.get(i)[0];
				
				cell = row.createCell(i); 
				
				Object valueObj = rowVal.get(fldCode);
				if(valueObj instanceof Integer){
					Integer value = (Integer)valueObj;
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					if(value != null)
						cell.setCellValue(value.intValue());
				} else if(valueObj instanceof Long){
					Long value = (Long)valueObj;
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					if(value != null)
						cell.setCellValue(value.longValue());
				} else if(valueObj instanceof BigDecimal){
					BigDecimal value = (BigDecimal)valueObj;
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					if(value != null)
						cell.setCellValue(value.doubleValue());
				} else if(valueObj instanceof java.util.Date){
					java.util.Date value = (java.util.Date)valueObj;
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if(value != null)
						cell.setCellValue(sdf.format(value));
				}else{
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if(valueObj == null)
						cell.setCellValue(new HSSFRichTextString(""));
					else
						cell.setCellValue(new HSSFRichTextString(valueObj.toString()));
				}
			}
		}
		
		String fileName = "??????????????????" + System.currentTimeMillis() + ".xls";
		try{
			fileName = new String(fileName.getBytes("utf-8"),"iso8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ fileName + "\"");
			response.setContentType("application/octet-stream; charset=UTF-8");
			java.io.OutputStream os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
			workbook.close();
		}catch(Exception e){
			logger.error("", e);
		}
	}
	
	private Boolean isZxgw(LxCustomerVO lxCustomer){
		long userId = this.getCurrentUser().getId().longValue();
		if(lxCustomer.getZxgwList() == null)
			return Boolean.FALSE;
		
		for(StuZxgw zxgw:lxCustomer.getZxgwList()){
			if(zxgw.getZxgwId() == userId)
				return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
