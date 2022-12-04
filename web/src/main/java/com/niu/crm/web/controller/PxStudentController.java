package com.niu.crm.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.niu.crm.model.PxContactRecord;
import com.niu.crm.model.CustAssign;
import com.niu.crm.model.PxCustomer;
import com.niu.crm.model.StuLevel;
import com.niu.crm.model.StuPlanCountry;
import com.niu.crm.model.StuSingleShare;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.UserFunc;
import com.niu.crm.model.type.AclScope;
import com.niu.crm.model.type.CallbackType;
import com.niu.crm.service.CountryService;
import com.niu.crm.service.PxContactRecordService;
import com.niu.crm.service.PxStuOperatorService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.PxCustomerService;
import com.niu.crm.vo.PxCustAssignVO;
import com.niu.crm.vo.PxCustomerVO;
import com.niu.crm.vo.PxCustomerZxgwVO;
import com.niu.crm.vo.PxStuLevelVO;
import com.niu.crm.vo.MyCustomerVO;
import com.niu.crm.vo.RepeatCustVO;
import com.niu.crm.vo.UserFuncVO;
import com.niu.crm.form.StuShareSearchForm;
import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.dao.mapper.DictMapper;


@Controller
@RequestMapping(value = "/px/student")
public class PxStudentController extends BaseController{
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
	private PxCustomerService pxCustomerService;
	@Autowired
	private PxContactRecordService contactRecordService;
	@Autowired
	private UserFuncService userFuncService;
	@Autowired
	private PxStuOperatorService stuOperatorService;

	/**
	 * 客户池
	 * @return
	 */
	@RequestMapping(value = "/pool.do")
    public String pool(ModelMap model){
		
		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("xlList", dictService.loadChildren("xl") );
		model.addAttribute("gradeList", dictService.loadChildren("grade") );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("levelList", dictService.loadChildren("stulevel") );
		
		return "px/student/pool";
	}
	
	
	/**
	 * 客户管理
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
		
		return "px/student/list";
	}
	
	private boolean canExport(User user){
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "px_custexport");
		if(func !=null)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}
	
	/**
	 * 顾问操作台
	 * @return
	 */
	@RequestMapping(value = "/myStudents.do")
    public String myStudents(ModelMap model){
		
		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("xlList", dictService.loadChildren("xl") );
		model.addAttribute("gradeList", dictService.loadChildren("grade") );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("levelList", dictService.loadChildren("stulevel") );
		
		return "px/student/myStudents";
	}
	
	/**
	 * 列出客户信息
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
		
		int total = pxCustomerService.countStuZxgw(form);
		List<PxCustomerZxgwVO> ls = pxCustomerService.queryStuZxgw(form, pager);
		
		for(PxCustomerZxgwVO vo:ls){
			processVOWhenList(vo, false);
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	
	/**
	 * 我录入的客户
	 * @return
	 */
	@RequestMapping(value = "/myCreatedStudents.do")
    public String myCreatedStudents(ModelMap model){
		
		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("xlList", dictService.loadChildren("xl") );
		model.addAttribute("gradeList", dictService.loadChildren("grade") );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("levelList", dictService.loadChildren("stulevel") );
		
		return "px/student/myCreatedStudents";
	}
	
	/**
	 * 列出客户信息
	 * @return
	 */
	@RequestMapping(value = "/myCreatedStudentsData.do")
    @ResponseBody
    public Map<String, Object> myCreatedStudentsData(HttpServletRequest req,StudentSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		form.setCreator_id(user.getId());
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		
		int total = pxCustomerService.countStudents(form);
		List<PxCustomerVO> ls = pxCustomerService.queryStudents(form, pager);
		
		for(PxCustomerVO vo:ls){
			processVOWhenList(vo, false);
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
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
	 * 列出客户信息
	 * @return
	 */
	@RequestMapping(value = "/listData.do")
    @ResponseBody
    public Map<String, Object> listData(HttpServletRequest req,StudentSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		
		setListClause(user, form);
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		
		int total = pxCustomerService.countStudents(form);
		List<PxCustomerVO> ls = pxCustomerService.queryStudents(form, pager);
		
		for(PxCustomerVO vo:ls){
			processVOWhenList(vo, false);
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	/**
	 * 导出客户信息
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
		
		lsFld.add( new String[]{"companyName","公司"} );
		lsFld.add( new String[]{"cstmName","姓名"} );
		lsFld.add( new String[]{"mobile","手机"} );
		lsFld.add( new String[]{"phone","备用电话"} );
		lsFld.add( new String[]{"wechat", "微信"} );
		lsFld.add( new String[]{"qq", "QQ"} );
		lsFld.add( new String[]{"email", "Email"} );
		lsFld.add( new String[]{"stuCity", "所在城市"} );
		lsFld.add( new String[]{"planCountry", "意向国家"} );
		
		lsFld.add( new String[]{"currSchool", "国内在读/毕业学校"});
		lsFld.add( new String[]{"currGrade", "在读年级"});
		lsFld.add( new String[]{"planXl", "申请学历"});
		lsFld.add( new String[]{"currentSpecialty", "当前专业"});
		lsFld.add( new String[]{"hopeSpecialty", "申请专业"});
		lsFld.add( new String[]{"enterTime", "入学时间"});
		lsFld.add( new String[]{"stuLevelName", "顾问评级"});
		lsFld.add( new String[]{"zxgwNames", "咨询顾问"});
		lsFld.add( new String[]{"contactRecords", "回访内容"});
		lsFld.add( new String[]{"stuFromName", "咨询渠道"});
		lsFld.add( new String[]{"creatorName", "录入人"});
		lsFld.add( new String[]{"memo", "备注"});
		
		List<PxCustomerVO> ls = null;
		if( canExport(user) )
			ls = pxCustomerService.queryStudents(form, pager);
		else
			ls = new ArrayList<PxCustomerVO>();
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;

		// 创建Excel工作簿的第一个Sheet页
		sheet = workbook.createSheet("sheet1");
		
		int rowNo =0;
		row = sheet.createRow(rowNo ++); 

		for(int i=0; i < lsFld.size(); i++){
			String[] pair = lsFld.get(i);
			
			cell = row.createCell(i); 
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(pair[1]));
		}
		
		for(PxCustomerVO vo:ls){
			processVOWhenList(vo,true);
			
			Map<String,Object> mapVal = new HashMap<String,Object>();
			lsRows.add(mapVal);
			
			StringBuffer contactRecords = new StringBuffer();
			List<PxContactRecord> lsContacts = vo.getLastContactRecords();
			for(int idx=0; lsContacts !=null && idx < lsContacts.size(); idx++){
				PxContactRecord record = lsContacts.get(idx);
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
	
			mapVal.put("stuFromName", vo.getStuFromName());
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
				
		String fileName = "学生信息" + System.currentTimeMillis() + ".xls";		
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
		 * //侯悦  北京非美
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
	
	private void processVOWhenList(PxCustomerVO vo, boolean bExport){
		Long stuId = vo.getId();
		//屏蔽mobile/phone/email
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
			}
		}
		vo.setPlanCountryName( translateCountry(vo.getId(),vo.getPlanCountry()) );

		if( vo.getOwnerId() !=null)
			vo.setOwnerName( userService.load(vo.getOwnerId()).getName() );
		if( vo.getCreatorId() !=null)
			vo.setCreatorName( userService.load(vo.getCreatorId()).getName() );
		
		List<PxContactRecord> lastContactRecords = null;
		
		if( bExport )
			lastContactRecords = contactRecordService.queryLast(stuId, null, 5);	
		else
			lastContactRecords = contactRecordService.queryLast(stuId, null, 3);
			
		vo.setLastContactRecords(lastContactRecords);
	}
	
	private void processVOWhenList(PxCustomerZxgwVO vo, boolean bExport){
		Long stuId = vo.getStuId();
		//屏蔽mobile/phone/email
		
		Customer customer = vo.getCustomer();
		if( !bExport ){
			customer.setMobile("");
			customer.setPhone("");
			customer.setEmail("");
		}
		
		PxCustomer student = vo.getStudent();
		
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
		
		//
		List<PxContactRecord> lastContactRecords = contactRecordService.queryLast(stuId, vo.getZxgwId(), 3);
		vo.setLastContactRecords(lastContactRecords);
	}
	
	/**
	 * 客户信息录入表格
	 * @return
	 */
	@RequestMapping(value ="/main.do")
    public String studentMain(Long id, ModelMap model){
		model.addAttribute("id", id );
		model.addAttribute("user", this.getCurrentUser() );
		return "px/student/student";
	}
	
	private boolean canModifyStuFrom(User user, Long companyId){
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "px_modify_stufrom");
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
	 * 客户信息录入表格
	 * @return
	 */
	@RequestMapping(value ="/inquireRecord.do")
    public String detail(Long id, ModelMap model){
		User user = this.getCurrentUser();
		Customer customer = null;
		
		PxCustomerVO pxCustomer = pxCustomerService.loadVO(id, null);
		
		customer = customerService.load(pxCustomer.getCstmId());
		List<CustPhone> custPhones = customerService.loadCustPhone(pxCustomer.getCstmId(), Boolean.FALSE);
		customer.setCustPhones(custPhones);
		
		inquireModel(model);
		if(pxCustomer.getUnitId() !=null){ 
			Long companyId = pxCustomer.getCompanyId();
			Unit company = unitService.load(companyId);
			String szUnitName = unitService.getFullName(pxCustomer.getUnitId());
			
			pxCustomer.setUnitName(szUnitName);
			pxCustomer.setCompanyName(company.getName());
		}
		
		
		if(pxCustomer.getStuFromId() !=null){
			Dict dict = dictService.load(pxCustomer.getStuFromId());
			String stuFromName = dict.getDictName();
			if(dict.getParentId() !=null){
				dict = dictService.load(dict.getParentId());
				stuFromName = dict.getDictName() + "-" + stuFromName;
			}
			pxCustomer.setStuFromName(stuFromName);
		}
		
		if(pxCustomer.getOwnerId() !=null){ 
			User creator = userService.load(pxCustomer.getOwnerId());
			pxCustomer.setOwnerName(creator.getName());
		}
		
		if(pxCustomer.getCreatorId() !=null){ 
			User creator = userService.load(pxCustomer.getCreatorId());
			pxCustomer.setCreatorName(creator.getName());
		}
		
		model.addAttribute("stu", pxCustomer);
		model.addAttribute("cust", customer);
		
		model.addAttribute("isZxgw", this.isZxgw(pxCustomer));
		model.addAttribute("canModifyStuFrom", canModifyStuFrom(user, pxCustomer.getCompanyId()) );
		
		return "px/student/inquireRecord";
	}
	
	/**
	 * 客户信息录入表格
	 * @return
	 */
	@RequestMapping(value = "/add.do")
    public String add(Long cstmId, ModelMap model){
		PxCustomerVO pxCustomer = null;
		Customer customer = null;

		pxCustomer = new PxCustomerVO();
		if(cstmId != null){
			customer = customerService.load(pxCustomer.getCstmId());
			pxCustomer.setCstmId(cstmId);
		}
		else{
			customer = new Customer();
		}
		inquireModel(model);
		
		List<StuPlanCountry> lsPlanCountry = new ArrayList<StuPlanCountry>();
		model.addAttribute("lsPlanCountry", lsPlanCountry);
		model.addAttribute("stu", pxCustomer);
		model.addAttribute("cust", customer);
		
		model.addAttribute("isZxgw", Boolean.FALSE);
		model.addAttribute("canModifyStuFrom", Boolean.TRUE );
		return "px/student/inquireRecord";
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
	 * 客户信息录入表格
	 * @return
	 */
	@RequestMapping(value = "/checkRepeat.do")
    @ResponseBody
    public ResponseObject<List<RepeatCustVO>> checkRepeat(Long cstmId, String... phones){

    	List<RepeatCustVO> ls = pxCustomerService.queryRepeat(cstmId, phones);
    	
	    for(RepeatCustVO vo:ls){
	    	if(vo.getCompanyId() !=null){
	    		Unit unit = unitService.load(vo.getCompanyId());
	    		vo.setCompanyName(unit.getName());
	    	}
	    }
    	
    	return new ResponseObject<List<RepeatCustVO>>(ls);
	}
	
	/**
	 * 保存客户信息到数据库
	 * @return
	 */
	@RequestMapping(value = "/save.do")
    @ResponseBody
    public ResponseObject<PxCustomer> save(Customer customer, PxCustomer pxCustomer){
		User user = this.getCurrentUser();
		Long cstmId = pxCustomer.getCstmId();
    	customer.setId( cstmId );
    	
		//查重
    	List<String> lsPhone = new ArrayList<>();
    	lsPhone.add(customer.getMobile());
		if (customer.getCustPhones() != null) {
			for (CustPhone custPhone : customer.getCustPhones()) {
				if (StringUtils.isNotEmpty(custPhone.getPhone()))
					lsPhone.add(custPhone.getPhone());
			}
		}
    	String[] phones = new String[lsPhone.size()];
    	lsPhone.toArray(phones);
    	List<RepeatCustVO> lsRepeat = pxCustomerService.queryRepeat(cstmId, phones);
    	if(lsRepeat.size() >0){
    		throw new BizException(GlobalErrorCode.INTERNAL_ERROR,"存在相同电话号码的客户");
    	}
    	
    	Unit unit = unitService.load(pxCustomer.getUnitId());
    	if (unit == null) {
    		String errMsg = "unit not found id=" + pxCustomer.getUnitId();
    		errMsg += " stu_id=" + pxCustomer.getId() + " mobile=" + customer.getMobile();
			getLogger().error(errMsg);
    	}
    	
    	pxCustomer.setCompanyId(unit.getCompanyId());
    	    					
		if(pxCustomer.getId() == null){
			pxCustomerService.add(user, customer, pxCustomer);
		}else{
			pxCustomerService.update(user, customer, pxCustomer);
		}
		return new ResponseObject<PxCustomer>(pxCustomer);
	}
	
	/**
	 * 更新顾问评级
	 * @return
	 */
	@RequestMapping(value = "/updateLevel.do")
    @ResponseBody
    public ResponseObject<Boolean> updateLevel(Long stuId, Long levelId){
		User user = this.getCurrentUser();
		Long zxgwId = user.getId();
		
		pxCustomerService.updateStuZxgwLevel(user, zxgwId, stuId, levelId);		
		
		return new ResponseObject<Boolean>(Boolean.TRUE);		
	}
	
	/**
	 * 客户信分配
	 * @return
	 */
	@RequestMapping(value = "/assign/allocate.do")
    @ResponseBody
    public ResponseObject<Boolean> assign(Long[] stuIds, Long zxgwId){
		User user = this.getCurrentUser();
		
		for(Long stuId:stuIds){
			pxCustomerService.assign2Zxgw(user, stuId, zxgwId);
		}
				
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	/**
	 * 退回客户信分配
	 * @return
	 */
	@RequestMapping(value = "/assign/revoke.do")
    @ResponseBody
    public ResponseObject<Boolean> revokeAssign(Long stuId, Long[] zxgwIds){
		User user = this.getCurrentUser();
		for(Long zxgwId:zxgwIds){
			pxCustomerService.revokeAssignFromZxgw(user, stuId, zxgwId);
		}

		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	/**
	 * 删除客户信息
	 * @return
	 */
	@RequestMapping(value = "/delete.do")
    @ResponseBody
    public ResponseObject<Integer> delete(Long id){
		User user = this.getCurrentUser();
		
		int count = 0;
		UserFunc queryFunc = userFuncService.loadByCode(user.getId(), "admin");
		if( queryFunc != null && id != null)
			count = pxCustomerService.delete(user, id);
		
		return new ResponseObject<Integer>(count);
	}
	
	
	/**
	 * 列出客户顾问评级记录
	 * @return
	 */
	@RequestMapping(value = "/stuLevelLog.do")
    @ResponseBody
    public List<PxStuLevelVO> stuLevelLog(HttpServletRequest req, Long stuId){
		List<StuLevel> ls = pxCustomerService.loadStuLevelByStuId(stuId);
		List<PxStuLevelVO> lsVO = new ArrayList<>();
		for(StuLevel item:ls){
			PxStuLevelVO vo = new PxStuLevelVO();
			lsVO.add(vo);
			
			BeanUtils.copyProperties(item, vo);
			vo.setCreatorName( userService.load(vo.getCreatorId()).getName() );
			if( vo.getLevelId() !=null)
				vo.setLevelName( dictService.load(vo.getLevelId()).getDictName() );
		}
		return lsVO;
	}
	
	/**
	 * 列出客户咨询顾问
	 * @return
	 */
	@RequestMapping(value = "/listStuZxgw.do")
    @ResponseBody
    public Map<String, Object> listStuZxgw(Long stuId){
		User user = this.getCurrentUser();
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();		
		List<StuZxgw> ls = pxCustomerService.loadStuZxgwByStuId(stuId);
				
		map.put("total", ls.size());
		map.put("rows", ls);
		
		return map;
	}
	
	/**
	 * 列出客户顾问评级记录
	 * @return
	 */
	@RequestMapping(value = "/stuAssignLog.do")
    @ResponseBody
    public List<PxCustAssignVO> stuAssignLog(HttpServletRequest req, Long stuId){
		User user = this.getCurrentUser();
		
		List<CustAssign> ls = pxCustomerService.loadCustAssignByStuId(stuId);
		List<PxCustAssignVO> lsVO = new ArrayList<>();
		for(CustAssign item:ls){
			PxCustAssignVO vo = new PxCustAssignVO();
			lsVO.add(vo);
			
			BeanUtils.copyProperties(item, vo);
			if( user.getId() ==1)
				vo.setCreatorName( userService.load(vo.getCreatorId()).getName() );
			else
				vo.setCreatorId(null);
			
			if( vo.getZxgwId() !=null)
				vo.setZxgwName( userService.load(vo.getZxgwId()).getName() );
		}
		
		return lsVO;
	}
	
	/**
	 * 修复客户可访问顾问列表
	 * @return
	 */
	@RequestMapping(value = "/fixOperator1.do")
    @ResponseBody
    public ResponseObject<Boolean> fixOperator(Long id){
		stuOperatorService.fix(id);
    	return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	/**
	 * 修复客户可访问顾问列表
	 * @return
	 */
	@RequestMapping(value = "/fixOperator.do")
    @ResponseBody
    public ResponseObject<Boolean> fixOperator(){
		StudentSearchForm form = new StudentSearchForm();
		int total = pxCustomerService.countStudents(form);
		getLogger().info("total=" + total);
				
		int pageNo =0;
		while(true){
			PageRequest pager = new PageRequest(pageNo++, 200);
			List<PxCustomerVO> ls = pxCustomerService.queryStudents(form, pager);
			for(PxCustomerVO item:ls){
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
	 * 资源分配信息
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
		
		return "px/student/stuZxgwList";
	}
	
	/**
	 * 列出客户信息
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
		int total = pxCustomerService.countStuZxgw(form);
		List<PxCustomerZxgwVO> ls = pxCustomerService.queryStuZxgw(form, pager);
		
		for(PxCustomerZxgwVO vo:ls){
			processVOWhenList(vo, false);
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		return map;
	}
	
	
	/**
	 * 列出客户信息
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
		

		lsFld.add( new String[]{"companyName","公司"} );
		lsFld.add( new String[]{"cstmName","姓名"} );
		lsFld.add( new String[]{"mobile","手机"} );
		lsFld.add( new String[]{"phone","备用电话"} );
		lsFld.add( new String[]{"wechat", "微信"} );
		lsFld.add( new String[]{"qq", "QQ"} );
		lsFld.add( new String[]{"email", "Email"} );
		lsFld.add( new String[]{"stuCity", "所在城市"} );
		lsFld.add( new String[]{"planCountry", "意向国家"} );
		
		lsFld.add( new String[]{"currSchool", "国内在读/毕业学校"});
		lsFld.add( new String[]{"currGrade", "在读年级"});
		lsFld.add( new String[]{"planXl", "申请学历"});
		lsFld.add( new String[]{"currentSpecialty", "当前专业"});
		lsFld.add( new String[]{"hopeSpecialty", "申请专业"});
		lsFld.add( new String[]{"enterTime", "入学时间"});
		lsFld.add( new String[]{"stuLevelName", "顾问评级"});
		lsFld.add( new String[]{"zxgwName", "咨询顾问"});
		lsFld.add( new String[]{"contactRecords", "回访内容"});
		lsFld.add( new String[]{"stuFromName", "咨询渠道"});
		lsFld.add( new String[]{"memo", "备注"});		
		
		List<PxCustomerZxgwVO> ls = null;
		if( canExport(user) )
			ls = pxCustomerService.queryStuZxgw(form, pager);
		else
			ls = new ArrayList<PxCustomerZxgwVO>();
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;

		// 创建Excel工作簿的第一个Sheet页
		sheet = workbook.createSheet("sheet1");
		
		int rowNo =0;
		row = sheet.createRow(rowNo ++); 

		for(int i=0; i < lsFld.size(); i++){
			String[] pair = lsFld.get(i);
			
			cell = row.createCell(i); 
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(pair[1]));
		}
		
		for(PxCustomerZxgwVO vo:ls){
			processVOWhenList(vo, false);
			
			Map<String,Object> mapVal = new HashMap<String,Object>();
			lsRows.add(mapVal);
			
			StringBuffer contactRecords = new StringBuffer();
			List<PxContactRecord> lsContacts = vo.getLastContactRecords();
			for(int idx=0; lsContacts !=null && idx < lsContacts.size(); idx++){
				PxContactRecord record = lsContacts.get(idx);
				if(idx >0)
					contactRecords.append("\r\n");
				
				contactRecords.append("[" + record.getGwName() + "]" + record.getContactText());
			}
			
			Customer cust = vo.getCustomer();
			PxCustomer stu = vo.getStudent();
			
			mapVal.put("companyName", stu.getCompanyName() );
			mapVal.put("cstmName", cust.getName() );
			mapVal.put("mobile", cust.getMobile());
			mapVal.put("phone", cust.getPhone() );
			mapVal.put("wechat", cust.getWechat() );
			mapVal.put("qq", cust.getQQ() );
			mapVal.put("email", cust.getEmail() );
			mapVal.put("stuCity", vo.getStudent().getStuCity());
			
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
		
		String fileName = "学生顾问信息" + System.currentTimeMillis() + ".xls";
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
	
	private Boolean isZxgw(PxCustomerVO pxCustomer){
		long userId = this.getCurrentUser().getId().longValue();
		if(pxCustomer.getZxgwList() == null)
			return Boolean.FALSE;
		
		for(StuZxgw zxgw:pxCustomer.getZxgwList()){
			if(zxgw.getZxgwId() == userId)
				return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
