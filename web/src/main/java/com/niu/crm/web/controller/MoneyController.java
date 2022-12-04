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
import com.niu.crm.form.ContractSearchForm;
import com.niu.crm.form.SkSearchForm;
import com.niu.crm.model.Country;
import com.niu.crm.model.CustContract;
import com.niu.crm.model.CustContractLine;
import com.niu.crm.model.CustContractSk;
import com.niu.crm.model.Customer;
import com.niu.crm.model.Dict;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.StuPlanCountry;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.type.AclScope;
import com.niu.crm.model.type.SkStatus;
import com.niu.crm.service.CountryService;
import com.niu.crm.service.CustContractService;
import com.niu.crm.service.CustContractSkService;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.LxCustomerService;
import com.niu.crm.service.StuPlanCountryService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.CustContractSkVO;
import com.niu.crm.vo.CustContractVO;
import com.niu.crm.vo.LxCustomerVO;
import com.niu.crm.vo.UserFuncVO;

@Controller
@RequestMapping(value = "/money")
public class MoneyController extends BaseController{
	@Autowired
	private DictService dictService;	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustContractService contractService;
	@Autowired
	private CustContractSkService skService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserFuncService userFuncService;
	@Autowired
	private UnitService unitService;
	
	@RequestMapping(value = "/list.do")
    public String list(ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("canExport", this.canExport(user));
		
		UserFuncVO skFunc = userFuncService.loadByCode(user.getId(), "sk_input");
		if(skFunc == null)
			model.addAttribute("canSkInput", Boolean.FALSE);
		else
			model.addAttribute("canSkInput", Boolean.TRUE);
		
		
		List<Dict> feeItemList = new ArrayList<>();
		List<Dict> lsItem = dictService.loadChildren("feeitem");
		
		for(Dict item:lsItem){
			feeItemList.add(item);
			List<Dict> lsChild = dictService.loadChildren(item.getId());
			if(lsChild.size() >0){
				for(Dict a:lsChild){
					a.setDictName( item.getDictName() + "-" + a.getDictName());
					feeItemList.add(a);
				}
			}
		}
		
		model.addAttribute("countryList", countryService.loadAll() );
		model.addAttribute("companyList", unitService.getAllCompany() );
		model.addAttribute("feeItemList", feeItemList );	
		model.addAttribute("conTypeList", dictService.loadChildren("custcontract.type") );	
		
		return "money/skList";
	}

	@RequestMapping(value = "/listData.do")
	@ResponseBody
    public Map<String, Object> listData(HttpServletRequest req, SkSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		
		boolean hasFooter = false;
		if(req.getParameter("footer") !=null){
			String szFooter = req.getParameter("footer").trim().toLowerCase();
			if(szFooter.equals("1") || szFooter.equals("true"))
				hasFooter = true;
		}
		
		if(form.getStuFromId() !=null){
			Dict dict = dictService.load(form.getStuFromId());
			if(dictService.loadChildren(dict.getId()).isEmpty()){
				form.setStuFromCode(dict.getDictCode());
			}else{
				form.setStuFromCode(dict.getDictCode() + ".%");
			}
			form.setStuFromId(null);
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
				
		this.setListClause(user, form);
		
		if(form.getItemId() !=null){
			List<Dict> ls = dictService.loadChildren(form.getItemId());
			if(ls.size() >0){
				Dict item = dictService.load(form.getItemId());
				form.setItemCode( item.getDictCode() + ".%");
				form.setItemId( null );
			}
		}
		
		int total = skService.countSk(form);
		List<CustContractSkVO> ls = skService.querySk(form, pager);
		for(CustContractSkVO vo:ls){
			processVOWhenList(vo);
		}
		
		map.put("total", total);
		map.put("rows", ls);
		
		if(hasFooter){
			List<CustContractSkVO> footer = new ArrayList<CustContractSkVO>();
			CustContractSkVO vo = new CustContractSkVO();
			vo.setCstmName("本页合计");
			vo.setSkValue(BigDecimal.ZERO);
			vo.setAchivement(BigDecimal.ZERO);
			for(CustContractSkVO item:ls){
				vo.setSkValue(vo.getSkValue().add(item.getSkValue()));
				vo.setAchivement(vo.getAchivement().add(item.getAchivement()));
			}
			footer.add(vo);
			
			vo = skService.queryStat(form);
			if(vo == null){
				vo = new CustContractSkVO();
				vo.setSkValue(BigDecimal.ZERO);
			}
			vo.setCstmName("合计");
			footer.add(vo);
			
			map.put("footer", footer);
		}
		
		return map;
	}
	
	@RequestMapping(value = "/detailData.do")
    @ResponseBody
    public ResponseObject<Map<String,Object>> detailData(Long id){
		CustContractSk skLine = skService.load(id);
		if(skLine ==null){
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "数据没找到");
		}
		
		CustContractSkVO skVO = new CustContractSkVO();
		BeanUtils.copyProperties(skLine, skVO);
		skVO.setCompanyName( unitService.load(skLine.getCompanyId()).getName() );
		skVO.setItemName( dictService.load(skLine.getItemId()).getDictName() );

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("skLine", skVO);
		
		if(skLine.getConId() != null){
			CustContract contract = contractService.load(skLine.getConId());
			skVO.setCstmName(contract.getCstmName());
			
			map.put("contract", contract);
		}else{
			Customer customer = customerService.load(skLine.getCstmId());
			skVO.setCstmName(customer.getName());
		}
		
		return new ResponseObject<Map<String,Object>>(map);
	}
	
	@RequestMapping(value = "/delete.do")
    @ResponseBody
    public ResponseObject<Boolean> delete(Long id){
		User user = this.getCurrentUser(); 
		Long userId = user.getId();
		CustContractSk skLine = skService.load(id);
		if(skLine ==null)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "数据没找到");
		
		if(skLine.getStatus() == SkStatus.AUDIT)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "已审核数据,不允许删除");
		
		if(skLine.getPayLineId() != null)
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "从付款单过来的数据， 请从付款单删除");
		
		boolean canDel = false;
		UserFuncVO adminFunc = userFuncService.loadByCode(user.getId(), "admin");
		if(skLine.getCreatorId().equals(userId) || adminFunc !=null){
			canDel = true;
		}
		if( !canDel ){
			UserFuncVO confirmFunc = userFuncService.loadByCode(user.getId(), "sk_confirm");
			if(confirmFunc !=null && userFuncService.allowAccess(user, confirmFunc, skLine.getCompanyId(), null)){
				canDel = true;
			}
		}
		
		if(!canDel)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED, "权限不够 不允许删除");
			
		skService.delete(user, id);
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/refreshContractSk.do")
    @ResponseBody
    public ResponseObject<Boolean> refreshContractSk(Long id){
		CustContractSk skLine = skService.load(id);
		if(skLine ==null){
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "数据没找到");
		}
		skService.refreshLxContractSk(skLine.getConId());
		
		return new ResponseObject<Boolean>(Boolean.TRUE); 
	}
	
	@RequestMapping(value = "/fixAchievement.do")
    @ResponseBody
    public ResponseObject<Boolean> fixAchievement(Long id){
		SkSearchForm form = null;
		Pageable pageable = new PageRequest(0, 10000);
		try{
			form = new SkSearchForm();
			form.setSkDateTo("2018-01-01");
			List<CustContractSkVO>  ls = skService.querySk(form, pageable);
			
			for(CustContractSkVO item:ls){
				skService.fixAchivement(item);
			}

			form = new SkSearchForm();
			form.setSkDateFrom("2018-01-01");
			ls = skService.querySk(form, pageable);
			for (CustContractSkVO item : ls) {
				skService.fixAchivement(item);
			}
		}catch(Exception e){
			getLogger().error("", e);
		}
		
		
		
		
		return new ResponseObject<Boolean>(Boolean.TRUE); 
	}
	
	@RequestMapping(value = "/save.do")
    @ResponseBody
	public ResponseObject<Boolean> save(HttpServletRequest req, SkForm skForm){
		User user = this.getCurrentUser();
		UserFuncVO skFunc = userFuncService.loadByCode(user.getId(), "sk_input");
		if(skFunc == null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED, "没有收款录入权限");
		
		CustContractSk[] skLines = skForm.getSkLines();
		
		for(int i=0; skLines !=null && i < skLines.length; i++){
			CustContractSk skRecord = skLines[i];
			skRecord.setStatus(SkStatus.SUBMIT);
			
			if(skRecord.getId() == null)
				skService.add(user, skRecord);
			else
				skService.update(user, skRecord);
		}
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	/**
	 * 导出合同信息
	 * @return
	 */
	@RequestMapping(value = "/exportData.do")
    @ResponseBody
    public void exportData(HttpServletRequest request, HttpServletResponse response,SkSearchForm form ){
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
		lsFld.add( new String[]{"conNo","合同号"} );
		lsFld.add( new String[]{"conTypeName1","合同类型1"} );
		lsFld.add( new String[]{"conTypeName","合同类型"} );
		lsFld.add( new String[]{"cstmName","姓名"} );
		
		lsFld.add( new String[]{"stuFromName1","客户来源1"} );
		lsFld.add( new String[]{"stuFromName","客户来源"} );
		lsFld.add( new String[]{"payTypeName","付款方式"} );
		lsFld.add( new String[]{"skDate", "收款日期"} );
		lsFld.add( new String[]{"skItemName", "收款项目"} );
		lsFld.add( new String[]{"skValue", "收款额"} );
		lsFld.add( new String[]{"achivement", "业绩额"} );

		lsFld.add( new String[]{"countryNames", "签约国家"} );
		lsFld.add( new String[]{"planEnterSeason","申请季度"} );
		lsFld.add( new String[]{"signGwName", "签约顾问"} );
		lsFld.add( new String[]{"planGwName", "规划顾问"} );
		lsFld.add( new String[]{"applyGwName", "申请顾问"} );
		lsFld.add( new String[]{"writeGwName", "写作顾问"} );
		lsFld.add( new String[]{"serviceGwName", "客服顾问"} );
		lsFld.add( new String[]{"kcgwName", "课程顾问"} );
				
		lsFld.add( new String[]{"memo", "备注"} );
		lsFld.add( new String[]{"createdAt", "录入时间"} );
				
		List<CustContractSkVO> ls = null;
		if( canExport(user) )
			ls = skService.querySk(form, pager);
		else
			ls = new ArrayList<CustContractSkVO>();
		
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
		
		for(CustContractSkVO vo:ls){
			processVOWhenList(vo);
			
			Map<String,Object> mapVal = new HashMap<String,Object>();
			lsRows.add(mapVal);
			
			mapVal.put("companyName", vo.getCompanyName() );
			mapVal.put("conNo", vo.getConNo());
			mapVal.put("conTypeName", vo.getConTypeName() );
			mapVal.put("conTypeName1", vo.getConTypeName1() );
			mapVal.put("cstmName", vo.getCstmName() );
			mapVal.put("stuFromName1", vo.getStuFromName1() );
			mapVal.put("stuFromName", vo.getStuFromName() );
			mapVal.put("payTypeName", vo.getPayTypeName() );
			mapVal.put("skDate", vo.getSkDate() );
			mapVal.put("skItemName", vo.getItemName() );
			mapVal.put("skValue", vo.getSkValue() );
			mapVal.put("achivement", vo.getAchivement() );
		
			mapVal.put("countryNames", vo.getCountryNames() );
			
			String szEnterSeason = vo.getPlanEnterSeason()==null?"":vo.getPlanEnterSeason();
			if(vo.getPlanEnterYear() !=null){
				szEnterSeason = vo.getPlanEnterYear() + szEnterSeason;
			}

			mapVal.put("planEnterSeason",szEnterSeason);
			mapVal.put("signGwName", vo.getSignGwName() );
			mapVal.put("planGwName", vo.getPlanGwName() );
			mapVal.put("applyGwName", vo.getApplyGwName() );
			mapVal.put("writeGwName", vo.getWriteGwName() );
			mapVal.put("serviceGwName", vo.getServiceGwName() );
			mapVal.put("kcgwName", vo.getKcgwName() );
			
			mapVal.put("memo", vo.getMemo());
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
		
		
		String fileName = "收款明细" + System.currentTimeMillis() + ".xls";
		
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
	
	private void processVOWhenList(CustContractSkVO vo){
		Dict dict = dictService.load(vo.getItemId());
		if(dict.getLayer() >2){
			Dict pDict = dictService.load(dict.getParentId());
			vo.setItemName( pDict.getDictName() + "-" + dict.getDictName() );
		}else{
			vo.setItemName( dict.getDictName() );
		}
		
		if(vo.getConType() !=null){
			dict = dictService.load(vo.getConType());
			vo.setConTypeName(dict.getDictName());
			while(dict.getLayer() >3){
				dict = dictService.load( dict.getParentId() );
				if(dict.getLayer() ==3){
					vo.setConTypeName1(dict.getDictName());
					break;
				}
			}
		}
		
		if(vo.getPayType() !=null){
			dict = dictService.load(vo.getPayType());
			vo.setPayTypeName( dict.getDictName() );
		}
		if(vo.getStuFromId() !=null){
			dict = dictService.load(vo.getStuFromId());
			if(dict.getLayer() > 3){
				Dict pDict = dictService.load( dict.getParentId() );
				vo.setStuFromName( pDict.getDictName() + "-" + dict.getDictName() );
				
			}else{
				vo.setStuFromName( dict.getDictName() );
			}
			Dict pDict = dict;
			while(pDict.getLayer() >2)
				pDict = dictService.load( pDict.getParentId() );
			vo.setStuFromName1(pDict.getDictName() );
		}
		
		Unit unit = unitService.load(vo.getCompanyId());
		vo.setCompanyName(unit.getName());
		
		if (vo.getSignGwId() != null)
			vo.setSignGwName(userService.load(vo.getSignGwId()).getName());
		if (vo.getPlanGwId() != null)
			vo.setPlanGwName(userService.load(vo.getPlanGwId()).getName());
		if (vo.getApplyGwId() != null)
			vo.setApplyGwName(userService.load(vo.getApplyGwId()).getName());
		if (vo.getWriteGwId() != null)
			vo.setWriteGwName(userService.load(vo.getWriteGwId()).getName());
		if (vo.getServiceGwId() != null)
			vo.setServiceGwName(userService.load(vo.getServiceGwId()).getName());
	}

	private void setListClause(User user, SkSearchForm form){
		UserFuncVO queryFunc = userFuncService.loadByCode(user.getId(), "sk_query");
		AclScope aclScope = (queryFunc==null?null:queryFunc.getAclScope());
		if(queryFunc == null){
			form.setAclClause(" and (sk.creator_id=" + user.getId() + ")" );
		}
		else if( queryFunc.getAclScope() == AclScope.ALL){
			//不需要增加
		}else if( aclScope == AclScope.SOMECOMPANY || aclScope == AclScope.SELFCOMPANY){
			String companyIds = String.valueOf( user.getCompanyId() );
			if(aclScope == AclScope.SOMECOMPANY)
				companyIds = queryFunc.getCompanyIds();
			
			StringBuffer buf = new StringBuffer(64);
			buf.append(" and (");
			buf.append("  sk.company_id in(" + companyIds +") ");
			buf.append(" )");
			form.setAclClause( buf.toString() );
		}else{
			form.setAclClause(" and (1=2)");
		}
	}
	
	private boolean canExport(User user){
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "sk_export");
		if(func !=null)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}
	
	public static class SkForm{
		private CustContractSk[] skLines;

		public CustContractSk[] getSkLines() {
			return skLines;
		}

		public void setSkLines(CustContractSk[] skLines) {
			this.skLines = skLines;
		}
		
	}
}
