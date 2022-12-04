package com.niu.crm.web.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.dto.CustContractDTO;
import com.niu.crm.dto.LxContractDTO;
import com.niu.crm.form.ContractSearchForm;
import com.niu.crm.form.LxContractSearchForm;
import com.niu.crm.model.CustContract;
import com.niu.crm.model.CustContractLine;
import com.niu.crm.model.CustContractSk;
import com.niu.crm.model.Dict;
import com.niu.crm.model.LxContract;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.Payment;
import com.niu.crm.model.User;
import com.niu.crm.model.type.AclScope;
import com.niu.crm.model.type.CustContractStatus;
import com.niu.crm.model.type.PaymentStatus;
import com.niu.crm.service.CountryService;
import com.niu.crm.service.CustContractSkService;
import com.niu.crm.service.LxContractService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.LxCustomerService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.CustContractSkVO;
import com.niu.crm.vo.CustContractVO;
import com.niu.crm.vo.LxCustomerVO;
import com.niu.crm.vo.UserFuncVO;

@Controller
@RequestMapping(value = "/lx/contract")
public class LxContractController extends BaseController{
	@Autowired
	private DictService dictService;
	@Autowired
	private LxCustomerService lxCustomerService;
	@Autowired
	private LxContractService contractService;
	@Autowired
	private CustContractSkService contractSkService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private UserFuncService userFuncService;
	@Autowired
	private UserService userService;
	@Autowired
	private UnitService unitService;
		
	@RequestMapping(value = "/stuContracts.do")
    public String stuContracts(Long stuId,ModelMap model){
		User user = this.getCurrentUser();
		LxCustomer stu = lxCustomerService.load(stuId);
		LxCustomerVO vo = new LxCustomerVO();
		BeanUtils.copyProperties(stu, vo);
		vo.setCompanyName( unitService.load(stu.getCompanyId()).getName() );

		model.addAttribute("user", user);	
		model.addAttribute("stu", vo);	

		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("companyList", unitService.getAllCompany() );
		
		{
			List<Dict> lsFeeItem = new ArrayList<Dict>();
			List<Dict> feeItems = dictService.loadChildren("feeitem");
			for(Dict item:feeItems){
				//订金和退费 不应该出现在合同款中（488，397）
				if(item.getId() ==488L )
					continue;
				
				List<Dict> lsChild = dictService.loadChildren(item.getId());
				for(Dict a:lsChild)
					a.setDictName( item.getDictName() + "-" + a.getDictName());
				
				if(lsChild.size() ==0)
					lsFeeItem.add(item);
				else
					lsFeeItem.addAll(lsChild);
			}
			model.addAttribute("feeItemList",  lsFeeItem);
		}
		
		
		
		model.addAttribute("conTypeList", dictService.loadChildren("custcontract.type") );	
		return "lx/contract/stuContracts";
	}
	
	@RequestMapping(value = "/list.do")
    public String list(ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("canExport", this.canExport(user));
		
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "contract_archive");
		model.addAttribute("canArchive",  (func !=null));
		
		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("conTypeList", dictService.loadChildren("custcontract.type") );
		
		return "lx/contract/list";
	}
	
	@RequestMapping(value = "listData.do")
	@ResponseBody
    public Map<String, Object> listData(HttpServletRequest req, LxContractSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		
		boolean hasFooter = false;
		if(req.getParameter("footer") !=null){
			String szFooter = req.getParameter("footer").trim().toLowerCase();
			if(szFooter.equals("1") || szFooter.equals("true"))
				hasFooter = true;
		}
		
		setLxListClause(user, form);
		
		Map<String,Object> map = new HashMap<String,Object>();		
		
		int total = contractService.countContract(form);
		List<LxContractDTO> ls = contractService.queryContract(form, pager);
		
		map.put("total", total);
		map.put("rows", ls);
		
		if(hasFooter){
			List<LxContractDTO> footer = new ArrayList<>();
			LxContractDTO vo = new LxContractDTO();
			
			vo.setCstmName("本页合计");
			vo.setConValue(BigDecimal.ZERO);
			vo.setConDiscount(BigDecimal.ZERO);
			vo.setSkValue(BigDecimal.ZERO);
			
			for(LxContractDTO item:ls){
				vo.setConValue(vo.getConValue().add(item.getConValue()));
				vo.setConDiscount( vo.getConDiscount().add(item.getConDiscount()));
				vo.setSkValue( vo.getSkValue().add(item.getSkValue()));
			}
			footer.add(vo);
			
			vo = contractService.statContract(form);
			if(vo == null){
				vo = new LxContractDTO();
				
				vo.setConValue(BigDecimal.ZERO);
				vo.setConDiscount(BigDecimal.ZERO);
				vo.setSkValue(BigDecimal.ZERO);
			}
			vo.setCstmName("合计");
			footer.add(vo);
			
			map.put("footer", footer);
		}
		
		return map;
	}
	
	@RequestMapping(value = "/myCases.do")
    public String myCases(ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("canExport", this.canExport(user));
		
		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("conTypeList", dictService.loadChildren("custcontract.type") );
		
		return "lx/contract/myCases";
	}
	
	@RequestMapping(value = "myCaseData.do")
	@ResponseBody
    public Map<String, Object> myCaseData(HttpServletRequest req, LxContractSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		
		boolean hasFooter = false;
		if(req.getParameter("footer") !=null){
			String szFooter = req.getParameter("footer").trim().toLowerCase();
			if(szFooter.equals("1") || szFooter.equals("true"))
				hasFooter = true;
		}
		form.setCaseGwId(user.getId());
		
		Map<String,Object> map = new HashMap<String,Object>();		
		
		int total = contractService.countContract(form);
		List<LxContractDTO> ls = contractService.queryContract(form, pager);
		
		map.put("total", total);
		map.put("rows", ls);
		
		if(hasFooter){
			List<LxContractDTO> footer = new ArrayList<>();
			LxContractDTO vo = new LxContractDTO();
			
			vo.setCstmName("本页合计");
			vo.setConValue(BigDecimal.ZERO);
			vo.setConDiscount(BigDecimal.ZERO);
			vo.setSkValue(BigDecimal.ZERO);
			
			for(LxContractDTO item:ls){
				vo.setConValue(vo.getConValue().add(item.getConValue()));
				vo.setConDiscount( vo.getConDiscount().add(item.getConDiscount()));
				vo.setSkValue( vo.getSkValue().add(item.getSkValue()));
			}
			footer.add(vo);
			
			vo = contractService.statContract(form);
			if(vo == null){
				vo = new LxContractDTO();
				
				vo.setConValue(BigDecimal.ZERO);
				vo.setConDiscount(BigDecimal.ZERO);
				vo.setSkValue(BigDecimal.ZERO);
			}
			vo.setCstmName("合计");
			footer.add(vo);
			
			map.put("footer", footer);
		}
		
		return map;
	}

	@RequestMapping(value = "stuContractData.do")
	@ResponseBody
    public Map<String, Object> stuContractData(HttpServletRequest req, Long stuId, Long cstmId){
		User user = this.getCurrentUser();
				
		Map<String,Object> map = new HashMap<String,Object>();		
		
		List<LxContractDTO> ls = null;
		if (stuId != null || cstmId !=null) {
			Pageable pager = new PageRequest(0, 50000);
			LxContractSearchForm form = new LxContractSearchForm();
			form.setStuId(stuId);
			form.setCstmId(cstmId);

			ls = contractService.queryContract(form, pager);
		} else {
			ls = new ArrayList<>();
		}
		
		map.put("total", ls.size());
		map.put("rows", ls);
		
		return map;
	}
	
	@RequestMapping(value = "detailData.do")
    @ResponseBody
    public ResponseObject<LxContractDTO> detailData(Long id, String conNo){
		LxContractDTO dto = null;
		if( id == null){
			LxContract contract = contractService.loadByConNo(conNo);
			id = contract.getId();
		}
		
		dto = contractService.loadDTO(id);
		
		return new ResponseObject<LxContractDTO>(dto);
	}
	
	@RequestMapping(value = "feeLineData.do")
    @ResponseBody
    public Map<String, Object> feeLineData(Long lxConId){
		Map<String, Object> map = new HashMap<>();
		LxContractDTO dto = contractService.loadDTO(lxConId);
		
		List<CustContractLine> feeLines = dto.getFeeLines();
		for(int i=0; i < feeLines.size(); i++){
			CustContractLine feeLine = feeLines.get(i);
			feeLine.setItemName( dictService.load(feeLine.getItemId()).getDictName() );
		}
		
		map.put("total", feeLines.size() );
		map.put("rows", feeLines );
		
		return map;
	}
	
	@RequestMapping(value = "/delete.do")
	@ResponseBody
    public ResponseObject<Integer> delete(Long[] ids){
		User user = this.getCurrentUser();
		UserFuncVO adminFunc = userFuncService.loadByCode(user.getId(), "admin");
		
		long userId = user.getId().longValue();
		int deletedCount = 0;
		for(int i=0; ids !=null && i < ids.length; i++){
			LxContract contract = contractService.load(ids[i]);
			long creatorId = contract.getCreatorId().longValue();
			if(contract.getSkValue().compareTo(BigDecimal.ZERO) >0)
				continue;
			
			if(userId == creatorId || adminFunc !=null){
				contractService.delete(user, contract.getId());
				deletedCount ++;
			}
		}
		return new ResponseObject<Integer>(deletedCount);
	}
	
	
	/**
	 * 合同框架主页面
	 * @return
	 */
	@RequestMapping(value ="/main.do")
    public String contractMain(Long id, ModelMap model){
		model.addAttribute("id", id );
		model.addAttribute("user", this.getCurrentUser() );
		return "lx/contract/main";
	}
	
	/**
	 * 合同详情
	 * @return
	 */
	@RequestMapping(value ="/detail.do")
    public String detail(Long id, ModelMap model){
		LxContractDTO dto = contractService.loadDTO(id);
		if(dto.getCreatorId() !=null){
			String creatorName = userService.getUserName(dto.getCreatorId());
			dto.setCreatorName(creatorName);
		}
		
		model.addAttribute("id", id );
		model.addAttribute("contract", dto );
		model.addAttribute("user", this.getCurrentUser() );
		return "lx/contract/detail";
	}
	
	/**
	 * 合同收款信息
	 * @return
	 */
	@RequestMapping(value ="/skData.do")
    @ResponseBody
    public List<CustContractSkVO>  skData(Long conId){
		List<CustContractSkVO> skList = contractSkService.loadByConId(conId);
		return skList;
	}

	@RequestMapping(value = "/save.do")
    @ResponseBody
	public ResponseObject<LxContract> save(HttpServletRequest req, LxContractDTO contract){
		User user = this.getCurrentUser();
		BigDecimal conValue = BigDecimal.ZERO;
		BigDecimal conDiscount = BigDecimal.ZERO;
		
		if(contract.getConNo() !=null){
			String conNo = contract.getConNo().trim().toUpperCase();
			contract.setConNo(conNo);
		}
			
		
		boolean isHisAdd = false; //是否是补录
		{
			String szHisAdd = req.getParameter("isHisAdd");
			if(StringUtils.isNotEmpty(szHisAdd) && szHisAdd.equals("1"))
				isHisAdd = true;
		}
		if(isHisAdd && StringUtils.isEmpty( contract.getConNo() ) ){
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"合同号必须录入");
		}
		
		List<CustContractLine> feeLines = contract.getFeeLines();
		for (int i = 0; feeLines != null && i < feeLines.size(); i++) {
			CustContractLine line = feeLines.get(i);
			if(line.getItemDiscount() == null)
				line.setItemDiscount(BigDecimal.ZERO);
			
			conValue = conValue.add( line.getItemValue() );
			conDiscount = conDiscount.add(line.getItemDiscount());
		}
		contract.setConValue(conValue);
		contract.setConDiscount(conDiscount);
		
		if(contract.getId() == null)
			contractService.add(user, contract);
		else
			contractService.update(user, contract);
		
		return new ResponseObject<LxContract>(contract);
	}
	
	/**
	 * 导出合同信息
	 * @return
	 */
	@RequestMapping(value = "/exportData.do")
    @ResponseBody
    public void exportData(HttpServletRequest request, HttpServletResponse response,LxContractSearchForm form ){
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
		
		setLxListClause(user, form);
		
		lsFld.add( new String[]{"companyName","公司"} );
		lsFld.add( new String[]{"conNo","合同号"} );
		lsFld.add( new String[]{"conTypeName1","合同类型1"} );
		lsFld.add( new String[]{"conTypeName","合同类型"} );
		lsFld.add( new String[]{"cstmName","姓名"} );
		lsFld.add( new String[]{"countryNames","签约国家"} );
		lsFld.add( new String[]{"planEnterSeason","申请季度"} );
		lsFld.add( new String[]{"stuFromName","来源"} );
		lsFld.add( new String[]{"currSchool","毕业/在读学校"} );
		lsFld.add( new String[]{"conValue", "标准金额"} );
		lsFld.add( new String[]{"conDiscount", "优惠金额"} );
		lsFld.add( new String[]{"conInv", "应收金额"} );
		lsFld.add( new String[]{"skValue", "实收金额"} );
		lsFld.add( new String[]{"balance", "差额"} );
		
		lsFld.add( new String[]{"signGwName", "签约顾问"} );
		lsFld.add( new String[]{"planGwName", "规划顾问"} );
		lsFld.add( new String[]{"applyGwName", "申请顾问"} );
		lsFld.add( new String[]{"writeGwName", "写作顾问"} );
		lsFld.add( new String[]{"serviceGwName", "客服顾问"} );
		
		lsFld.add( new String[]{"stuCity", "所在城市"} );
		lsFld.add( new String[]{"signDate", "签约日期"} );
		lsFld.add( new String[]{"archiveDate", "归档日期"} );
		lsFld.add( new String[]{"createdAt", "录入时间"} );
		
		
		List<LxContractDTO> ls = null;
		if( canExport(user) )
			ls = contractService.queryContract(form, pager);
		else
			ls = new ArrayList<LxContractDTO>();
		
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
		
		for(LxContractDTO vo:ls){
			Dict conType = dictService.load( vo.getConType() );
			String conTypeName1 = conType.getDictName();
			String conTypeName  = conType.getDictName();
			while(conType.getLayer() >3){
				conType = dictService.load( conType.getParentId() );
				conTypeName1 = conType.getDictName();
			}
			
			
			Map<String,Object> mapVal = new HashMap<String,Object>();
			lsRows.add(mapVal);
			
			BigDecimal conInv = vo.getConValue().subtract( vo.getConDiscount() );
			
			mapVal.put("companyName", vo.getCompanyName() );
			mapVal.put("conNo", vo.getConNo());
			mapVal.put("conTypeName1", conTypeName1 );
			mapVal.put("conTypeName", conTypeName );
			mapVal.put("cstmName", vo.getCstmName() );
			mapVal.put("countryNames", vo.getCountryNames() );
			
			String szPlanSeason = "";
			if(vo.getPlanEnterYear() !=null){
				szPlanSeason = vo.getPlanEnterYear() + vo.getPlanEnterSeason();
			}
			mapVal.put("planEnterSeason", szPlanSeason);
			mapVal.put("stuFromName", vo.getStuFromName());
			mapVal.put("currSchool", vo.getCurrSchool());
				
			mapVal.put("conValue", vo.getConValue() );
			mapVal.put("conDiscount", vo.getConDiscount() );
			mapVal.put("conInv", conInv);
			mapVal.put("skValue", vo.getSkValue() );
			mapVal.put("balance", conInv.subtract(vo.getSkValue()) );
		

			mapVal.put("signGwName", vo.getSignGwName() );
			mapVal.put("planGwName", vo.getPlanGwName() );
			mapVal.put("applyGwName", vo.getApplyGwName() );
			mapVal.put("writeGwName", vo.getWriteGwName() );
			mapVal.put("serviceGwName", vo.getServiceGwName() );

			mapVal.put("stuCity", vo.getStuCity() );
			mapVal.put("signDate", vo.getSignDate() );
			mapVal.put("archiveDate", vo.getArchiveDate() );
			mapVal.put("createdAt", vo.getCreatedAt() );
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
		
		
		String fileName = "合同导出" + System.currentTimeMillis() + ".xls";
		
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
	
	private void setLxListClause(User user, LxContractSearchForm form){
		UserFuncVO queryFunc = userFuncService.loadByCode(user.getId(), "contractquery");
		AclScope aclScope = (queryFunc==null?null:queryFunc.getAclScope());
		if(queryFunc == null){
			form.setAclClause(" and (c.creator_id=" + user.getId() + " or d.sign_gw_id=" + user.getId() + " or d.plan_gw_id=" + user.getId() + ")" );
		}
		else if( StringUtils.isNotBlank(queryFunc.getClause()) ){
			form.setAclClause( queryFunc.getClause() );
		}
		else if( queryFunc.getAclScope() == AclScope.ALL || aclScope == AclScope.SOMECOMPANY || aclScope == AclScope.SELFCOMPANY){
			StringBuffer buf = new StringBuffer(128);
			if( queryFunc.getAclScope() == AclScope.ALL){
				//不需要增加
			}
			else{
				String companyIds = String.valueOf( user.getCompanyId() );
				if(aclScope == AclScope.SOMECOMPANY)
					companyIds = queryFunc.getCompanyIds();
				buf.append(" and (");
				buf.append("  d.company_id in(" + companyIds +") ");
				buf.append(" )");
			}
			if(queryFunc.getFromIdList() !=null && queryFunc.getFromIdList().length >0){
				buf.append(" and exists ( select * from t_dict where id=b.stu_from_id and (");
				Long[] arrId = queryFunc.getFromIdList();
				for(int i=0; i < arrId.length; i++ ){
					Long id = arrId[i];
					Dict dict = dictService.load(id);
					if(i >0) buf.append(" or ");
					buf.append(" stu_from_id=" + id + " or dict_code like '" + dict.getDictCode() + ".%'");
				} 
				buf.append("))");
			}
			
			form.setAclClause( buf.toString() );
		}else{
			form.setAclClause(" and (1=2)");
		}
	}
	
	private boolean canExport(User user){
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "contract_export");
		if(func !=null)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}
	
	public static void main(String... args){
		System.out.println("hello");
	}
}
