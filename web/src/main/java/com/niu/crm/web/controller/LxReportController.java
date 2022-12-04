package com.niu.crm.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.niu.crm.dao.mapper.LxStatMapper;
import com.niu.crm.dao.mapper.ZxgwCallbackRemindMapper;
import com.niu.crm.form.CallbackCheckForm;
import com.niu.crm.form.CallbackSearchForm;
import com.niu.crm.form.MarketStaffReportSearchForm;
import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.model.CallbackCheckDetail;
import com.niu.crm.model.CallbackCheckLine;
import com.niu.crm.model.Dict;
import com.niu.crm.model.LxContactRecord;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.ZxgwCallbackRemind;
import com.niu.crm.model.type.CallbackRemindType;
import com.niu.crm.model.type.CallbackType;
import com.niu.crm.service.DictService;
import com.niu.crm.service.LxContactRecordService;
import com.niu.crm.service.LxStuZxgwService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.ZxgwCallbackRemindVO;

@Controller
@RequestMapping(value = "/lx/report")
public class LxReportController extends BaseController {
	@Autowired
	private LxStatMapper statMapper;
	@Autowired
	private ZxgwCallbackRemindMapper remindMapper;

	@Autowired
	private DictService dictService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private UserService userService;
	@Autowired
	private LxContactRecordService lxContactRecordService;
	@Autowired
	private LxStuZxgwService stuZxgwService;

	@RequestMapping(value = "/resource")
	public String resource(Boolean singlePage, ModelMap model) {
		if (singlePage == null || singlePage == false)
			model.put("singlePage", Boolean.FALSE);
		else
			model.put("singlePage", Boolean.TRUE);

		model.addAttribute("companyList", unitService.getAllCompany());
		return "lx/report/resource";
	}

	@RequestMapping(value = "/resourceExcel")
	public void resourceExcel(HttpServletRequest req, HttpServletResponse resp,
			StudentSearchForm searchForm) {
		String statBy = req.getParameter("statBy");

		XSSFWorkbook wb = null;
		String fileName = null;
		if ("byFrom".equals(statBy)) {
			wb = resourceByFrom(req, searchForm);
			fileName = "按渠道统计的资源报表.xlsx";
		} else {
			wb = resourceByZxgw(req, searchForm);
			fileName = "按顾问统计的资源报表.xlsx";
		}

		try {
			fileName = new String(fileName.getBytes("utf-8"), "iso8859-1");
			resp.setHeader("Content-Disposition", "attachment; filename=\""
					+ fileName + "\"");
			resp.setContentType("application/octet-stream; charset=UTF-8");
			java.io.OutputStream os = resp.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			getLogger().error("", e);
		}
	}

	private XSSFWorkbook resourceByFrom(HttpServletRequest req,
			StudentSearchForm searchForm) {
		XSSFWorkbook wb = new XSSFWorkbook(); // or new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("sheet1");

		XSSFCellStyle titleStyle = wb.createCellStyle();
		XSSFCellStyle headerStyle = wb.createCellStyle();
		XSSFCellStyle itemStyle = wb.createCellStyle();

		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		titleStyle.setBorderBottom(BorderStyle.THIN);
		titleStyle.setBorderRight(BorderStyle.THIN);

		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);

		itemStyle.setBorderBottom(BorderStyle.THIN);
		itemStyle.setBorderRight(BorderStyle.THIN);

		// company_id-->Map<fromId, qty>
		Map<Long, Map<Long, Long>> mapData = new HashMap<Long, Map<Long, Long>>();

		Long companyId = null;
		Long qty = null;
		List<Dict> lsDict = dictService.loadChildren("stufrom");
		for (Dict dict : lsDict) {
			searchForm.setStuFromCode(dict.getDictCode());
			List<Map<String, Object>> ls = statMapper
					.statResourceByFrom(searchForm);
			for (Map<String, Object> row : ls) {
				companyId = (Long) row.get("company_id");
				qty = (Long) row.get("qty");
				Map<Long, Long> data = mapData.get(companyId);
				if (data == null) {
					data = new HashMap<Long, Long>();
					mapData.put(companyId, data);
				}
				data.put(dict.getId(), qty);
			}
		}

		List<Unit> lsCompany = new ArrayList<Unit>();
		for (Long unitId : mapData.keySet()) {
			lsCompany.add(unitService.load(unitId));
		}

		XSSFRow row = null;
		XSSFCell cell = null;
		int rownum = 0, cellnum = 0;

		row = sheet.createRow(rownum++);
		cellnum = 0;
		cell = row.createCell(cellnum++);
		cell.setCellStyle(titleStyle);
		cell.setCellValue("按渠道统计");
		for (int i = 0; i < lsDict.size(); i++) {
			cell = row.createCell(cellnum++);
			cell.setCellStyle(titleStyle);
		}

		sheet.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
				0, // last row (0-based)
				0, // first column (0-based)
				row.getLastCellNum() - 1));

		row = sheet.createRow(rownum++);
		cellnum = 0;
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		for (Dict dict : lsDict) {
			cell = row.createCell(cellnum++);
			cell.setCellValue(dict.getDictName());
			cell.setCellStyle(headerStyle);
		}

		for (Unit unit : lsCompany) {
			row = sheet.createRow(rownum++);
			cellnum = 0;
			cell = row.createCell(cellnum++);
			cell.setCellStyle(itemStyle);
			cell.setCellValue(unit.getName());

			Map<Long, Long> data = mapData.get(unit.getId());

			for (Dict dict : lsDict) {
				cell = row.createCell(cellnum++);
				cell.setCellStyle(itemStyle);
				qty = data.get(dict.getId());
				cell.setCellValue(qty == null ? 0L : qty);
			}
		}

		return wb;
	}

	private XSSFWorkbook resourceByZxgw(HttpServletRequest req,
			StudentSearchForm searchForm) {
		XSSFWorkbook wb = new XSSFWorkbook(); // or new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("sheet1");

		XSSFCellStyle titleStyle = wb.createCellStyle();
		XSSFCellStyle headerStyle = wb.createCellStyle();
		XSSFCellStyle itemStyle = wb.createCellStyle();

		titleStyle.setAlignment(HorizontalAlignment.CENTER);

		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);

		itemStyle.setBorderBottom(BorderStyle.THIN);
		itemStyle.setBorderRight(BorderStyle.THIN);

		// company_id|userId-->Map<fromId, qty>
		Map<String, Map<Long, Long>> mapData = new HashMap<String, Map<Long, Long>>();

		List<String> lsKey = new ArrayList<String>();

		Long companyId = null, zxgwId = null;
		Long qty = null;
		List<Dict> lsDict = dictService.loadChildren("stufrom");
		for (Dict dict : lsDict) {
			searchForm.setStuFromCode(dict.getDictCode());
			List<Map<String, Object>> ls = statMapper
					.statResourceByZxgw(searchForm);
			for (Map<String, Object> row : ls) {
				companyId = (Long) row.get("company_id");
				zxgwId = (Long) row.get("zxgw_id");
				if (zxgwId == null)
					continue;

				String szKey = companyId + "|" + zxgwId;
				if (!lsKey.contains(szKey))
					lsKey.add(szKey);

				qty = (Long) row.get("qty");
				Map<Long, Long> data = mapData.get(szKey);
				if (data == null) {
					data = new HashMap<Long, Long>();
					mapData.put(szKey, data);
				}
				data.put(dict.getId(), qty);
			}
		}

		XSSFRow row = null;
		XSSFCell cell = null;
		int rownum = 0, cellnum = 0;

		row = sheet.createRow(rownum++);
		cellnum = 0;
		cell = row.createCell(cellnum++);
		cell.setCellStyle(titleStyle);
		cell.setCellValue("按渠道及顾问统计");

		cell = row.createCell(cellnum++);
		for (int i = 0; i < lsDict.size(); i++) {
			cell = row.createCell(cellnum++);
			cell.setCellStyle(titleStyle);
		}

		sheet.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
				0, // last row (0-based)
				0, // first column (0-based)
				row.getLastCellNum() - 1));

		row = sheet.createRow(rownum++);
		cellnum = 0;
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellStyle(headerStyle);
		cell = row.createCell(cellnum++);
		cell.setCellStyle(headerStyle);
		cell.setCellStyle(headerStyle);
		for (Dict dict : lsDict) {
			cell = row.createCell(cellnum++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(dict.getDictName());
		}

		for (String szKey : lsKey) {
			int idx = szKey.indexOf("|");
			Unit unit = unitService
					.load(Long.parseLong(szKey.substring(0, idx)));
			User u = userService.load(Long.parseLong(szKey.substring(idx + 1)));
			row = sheet.createRow(rownum++);
			cellnum = 0;
			cell = row.createCell(cellnum++);
			cell.setCellStyle(itemStyle);
			cell.setCellValue(unit.getName());

			cell = row.createCell(cellnum++);
			cell.setCellStyle(itemStyle);
			cell.setCellValue(u.getName());

			Map<Long, Long> data = mapData.get(szKey);

			for (Dict dict : lsDict) {
				cell = row.createCell(cellnum++);
				cell.setCellStyle(itemStyle);
				qty = data.get(dict.getId());
				cell.setCellValue(qty == null ? 0L : qty);
			}
		}

		return wb;
	}

	@RequestMapping(value = "/callbackCheck")
	public String callbackCheck(Boolean singlePage, ModelMap model) {
		if (singlePage == null || singlePage == false)
			model.put("singlePage", Boolean.FALSE);
		else
			model.put("singlePage", Boolean.TRUE);

		{
			CallbackType[] callbackTypes = CallbackType.values();
			List<CallbackType> ls = new ArrayList<>();
			for (CallbackType item : callbackTypes) {
				if (item == CallbackType.KF || item == CallbackType.FORWARD)
					continue;

				ls.add(item);
			}
			model.addAttribute("callbackTypes", ls);
		}

		model.addAttribute("companyList", unitService.getAllCompany());
		return "lx/report/callbackCheck";
	}

	@RequestMapping(value = "/callbackCheckData.do")
	@ResponseBody
	public Map<String, Object> callbackCheckData(CallbackCheckForm form) {
		User user = this.getCurrentUser();
		Map<String, Object> map = new java.util.HashMap<String, Object>();

		processCallbackCheckForm(form);
		
		List<CallbackCheckLine> lines = this.getCallbackCheckData(form);

		map.put("total", lines.size());
		map.put("rows", lines);

		return map;
	}
	
	@RequestMapping(value = "/callbackCheckDetailData.do")
	@ResponseBody
	public Map<String, Object> callbackCheckDetailData(CallbackCheckForm form) {
		User user = this.getCurrentUser();
		Pageable pager = new PageRequest(0, 100);
		Map<String, Object> map = new java.util.HashMap<String, Object>();
		processCallbackCheckForm(form);

		List<CallbackCheckDetail> lines = remindMapper.checkReportDetail(form, pager);

		map.put("total", lines.size());
		map.put("rows", lines);

		return map;
	}
	
	private void processCallbackCheckForm(CallbackCheckForm form){
		if(form.getStuFromId() !=null){
			Long stuFromId = form.getStuFromId();
			List<Dict> lsChild = dictService.loadChildren(stuFromId);
			if(lsChild.size() >0){
				Dict dictFrom = dictService.load(stuFromId);
				form.setStuFromId(null);
				form.setStuFromCode(dictFrom.getDictCode() + ".");
			}
		}
		
		if(form.getDateTo() !=null)
			form.setDateTo( toDateLastMillis(form.getDateTo()) );
		if(form.getStuCreatedTo() !=null)
			form.setStuCreatedTo( toDateLastMillis(form.getStuCreatedTo()) );
	}

	@RequestMapping(value = "/callbackCheckExcel")
	public void callbackCheckExcel(HttpServletResponse resp,CallbackCheckForm form) {
		String[] captions = new String[]{
				"分公司","顾问","回访类型","应完成数量","已完成数量","未完成数量","完成率","逾期完成数量"
		};;
		
		if(form.getCallbackType() == CallbackType.DAY5 )
			captions[1] = "组长";
		
		
		List<CallbackType> lsCallbackType = new ArrayList<>();
		
		if( form.getCallbackType() != null){
			lsCallbackType.add(form.getCallbackType());
		}else{
			for(CallbackType item:CallbackType.values()){
				if(item == CallbackType.KF || item == CallbackType.FORWARD)
					continue;
				
				lsCallbackType.add(item);
			}
		}
		
		
		XSSFWorkbook wb =null; 
		String fileName = null;
		fileName = "回访检查报表.xlsx";
				
		try{
			wb = new XSSFWorkbook();  // or new XSSFWorkbook();
		    
		    XSSFCellStyle titleStyle  = wb.createCellStyle();
		    XSSFCellStyle headerStyle = wb.createCellStyle();
		    XSSFCellStyle itemStyle   = wb.createCellStyle();
		    XSSFCellStyle itemRightStyle = wb.createCellStyle();
		    
		    titleStyle.setAlignment(HorizontalAlignment.CENTER);
		    titleStyle.setBorderBottom(BorderStyle.THIN);
		    titleStyle.setBorderRight(BorderStyle.THIN);
		    
		    headerStyle.setAlignment(HorizontalAlignment.CENTER);
		    headerStyle.setBorderBottom(BorderStyle.THIN);
		    headerStyle.setBorderRight(BorderStyle.THIN);
		    
		    itemStyle.setBorderBottom(BorderStyle.THIN);
		    itemStyle.setBorderRight(BorderStyle.THIN);

		    itemRightStyle.setAlignment(HorizontalAlignment.RIGHT);
		    itemRightStyle.setBorderBottom(BorderStyle.THIN);
		    itemRightStyle.setBorderRight(BorderStyle.THIN);

		    XSSFSheet sheet = null;
		    XSSFRow row = null;
		    XSSFCell cell = null;
		    
		    
		    for(CallbackType callbackType:lsCallbackType){
		    	int rownum = 0, cellnum=0;
		    	
		    	form.setCallbackType(callbackType);
		    	sheet = wb.createSheet(callbackType.getName());
		    	row = sheet.createRow(rownum ++);
		    	cellnum =0;
		    	for(int i=0; i < captions.length; i++){
		    		cell = row.createCell(cellnum ++);
			    	cell.setCellStyle(titleStyle);	
		    	}
		    	
				sheet.addMergedRegion(new CellRangeAddress(0, 
						0, // last row (0-based)
						0, // first column (0-based)
						row.getLastCellNum() - 1));

				row = sheet.createRow(rownum++);
				cellnum = 0;
				for (int i = 0; i < captions.length; i++) {
					cell = row.createCell(cellnum++);
					cell.setCellValue(captions[i]);
					cell.setCellStyle(headerStyle);
				}

				List<CallbackCheckLine> lines = this.getCallbackCheckData(form);
				for (CallbackCheckLine line : lines) {
					row = sheet.createRow(rownum++);
					cellnum = 0;

					int totalCount = (line.getTotalCount() == null ? 0 : line
							.getTotalCount());
					int finishCount = (line.getFinishCount() == null ? 0 : line
							.getFinishCount());
					int ontimeCount = (line.getOntimeCount() == null ? 0 : line
							.getOntimeCount());

					// 分公司名称
					cell = row.createCell(cellnum++);
					cell.setCellStyle(itemStyle);
					cell.setCellValue(line.getCompanyName());

					// 顾问
					cell = row.createCell(cellnum++);
					cell.setCellStyle(itemStyle);
					cell.setCellValue(line.getZxgwName());

					// 回访类型
					cell = row.createCell(cellnum++);
					cell.setCellStyle(itemStyle);
					cell.setCellValue(line.getCallbackTypeName());

					// 应完成数量
					cell = row.createCell(cellnum++);
					cell.setCellStyle(itemRightStyle);
					cell.setCellValue(totalCount);

					// 已完成数量
					cell = row.createCell(cellnum++);
					cell.setCellStyle(itemRightStyle);
					cell.setCellValue(finishCount);

					// 未完成数量
					cell = row.createCell(cellnum++);
					cell.setCellStyle(itemRightStyle);
					cell.setCellValue(totalCount - finishCount);

					// 完成率
					cell = row.createCell(cellnum++);
					cell.setCellStyle(itemRightStyle);
					cell.setCellValue(line.getFinishRatio());

					// 逾期完成数量
					cell = row.createCell(cellnum++);
					cell.setCellStyle(itemRightStyle);
					cell.setCellValue(finishCount - ontimeCount);
				}

				sheet.setColumnWidth(0, 14 * 256);
				sheet.setColumnWidth(1, 12 * 256);
				sheet.setColumnWidth(2, 12 * 256);
				sheet.setColumnWidth(3, 12 * 256);
				sheet.setColumnWidth(4, 12 * 256);
				sheet.setColumnWidth(5, 12 * 256);
				sheet.setColumnWidth(6, 12 * 256);
				sheet.setColumnWidth(7, 14 * 256);
			}
		    
			
			fileName = new String(fileName.getBytes("utf-8"),"iso8859-1");
			resp.setHeader("Content-Disposition", "attachment; filename=\""
					+ fileName + "\"");
			resp.setContentType("application/octet-stream; charset=UTF-8");
			java.io.OutputStream os = resp.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		}catch(Exception e){
			logger.error("", e);
		}
	}

	private List<CallbackCheckLine> getCallbackCheckData(CallbackCheckForm form) {
		// 按公司组织数据(companyId--List)
		Map<Long, List<CallbackCheckLine>> map = new HashMap<>();

		// zxgwId -- Line
		Map<Long, CallbackCheckLine> mapLine = new HashMap<>();
		
		List<CallbackCheckLine> ls = null;
		form.setCallbackStatus(null);
		ls = remindMapper.checkReportStat(form);
		for (CallbackCheckLine line : ls) {
			Long zxgwId = line.getZxgwId();
			mapLine.put(zxgwId, line);
			User u = userService.load(zxgwId);
			line.setCompanyId(u.getCompanyId());

			List<CallbackCheckLine> lsOfCompany = map.get(u.getCompanyId());
			if (lsOfCompany == null) {
				lsOfCompany = new ArrayList<>();
				map.put(u.getCompanyId(), lsOfCompany);
			}
			lsOfCompany.add(line);
		}

		form.setCallbackStatus(CallbackCheckForm.CallbackStatus.FINISH);
		ls = remindMapper.checkReportStat(form);
		for (CallbackCheckLine item : ls) {
			Long zxgwId = item.getZxgwId();
			CallbackCheckLine line = mapLine.get(zxgwId);
			line.setFinishCount(item.getTotalCount());
		}

		form.setCallbackStatus(CallbackCheckForm.CallbackStatus.ONTIME);
		ls = remindMapper.checkReportStat(form);
		for (CallbackCheckLine item : ls) {
			Long zxgwId = item.getZxgwId();
			CallbackCheckLine line = mapLine.get(zxgwId);
			line.setOntimeCount(item.getTotalCount());
		}

		List<CallbackCheckLine> lines = new ArrayList<>();
		for (List<CallbackCheckLine> lsOfCompany : map.values()) {
			lines.addAll(lsOfCompany);
		}

		Map<Long, String> mapCompanyName = new HashMap<>();
		for (CallbackCheckLine line : lines) {
			line.setCallbackType(form.getCallbackType());
			line.setZxgwName(userService.getUserName(line.getZxgwId()));

			String companyName = mapCompanyName.get(line.getCompanyId());
			if (companyName == null) {
				companyName = unitService.getFullName(line.getCompanyId());
				mapCompanyName.put(line.getCompanyId(), companyName);
			}
			line.setCompanyName(companyName);
		}

		return lines;
	}
	
	@RequestMapping(value = "/marketStaffResource.do")
	public String marketStaff(HttpServletRequest req, ModelMap model) {
		model.addAttribute("companyList", unitService.getAllCompany() );
		
		return "lx/report/marketStaffResource";
	}
	
	@RequestMapping(value = "/marketStaffResourceData.do")
    @ResponseBody
	public Map<String,Object> marketStaffData(HttpServletRequest req, HttpServletResponse resp,
			MarketStaffReportSearchForm searchForm) {
		
		Map<String,Object> map = new HashMap<>();
		
		List<Map<String,Object>> rows = statMapper.queryMarketStaffResource(searchForm);
		List<MarketStaffResourceLine> ls = new ArrayList<>();
		for(Map<String,Object> row:rows){
			MarketStaffResourceLine line = new MarketStaffResourceLine();			
			ls.add(line);
			
			Long stuId = (Long)row.get("stu_id");
			line.setCompanyId( (Long)row.get("company_id") );
			line.setStuId( stuId );
			line.setCstmId( (Long)row.get("cstm_id") );
			line.setCstmName( (String)row.get("cstm_name") );
			line.setCreatedAt( (Date)row.get("created_at"));

			Long stuFromId = (Long)row.get("stu_from_id");
			line.setStuFromId( stuFromId );
			line.setOwnerId( (Long)row.get("owner_id") );
			
			if(line.getCompanyId() !=null)
				line.setCompanyName( unitService.getFullName(line.getCompanyId()) );
			if(line.getOwnerId() !=null)
				line.setOwnerName( userService.getUserName(line.getOwnerId()) );
			if(stuFromId !=null){
				Dict dict = dictService.load(stuFromId);
				line.setStuFromName( dict.getDictName() );
			}
			
			List<LxContactRecord> contactRecords = lxContactRecordService.queryLast(stuId, null, 10000);
			line.setContactRecords(contactRecords);
			
			List<StuZxgw> lsZxgw= stuZxgwService.listByStuId(stuId);
			for(StuZxgw zxgw:lsZxgw){
				zxgw.setZxgwName( userService.getUserName(zxgw.getZxgwId()) );
				
				if(zxgw.getStuLevel() !=null){
					Dict dict = dictService.load( zxgw.getStuLevel() );
					if(dict !=null)
						zxgw.setStuLevelName( dict.getDictName() );
				}
			}
			line.setZxgwList(lsZxgw);
		}
		
		map.put("rows", ls);
		map.put("total", ls.size());
		return map;
	}
	
	public static class MarketStaffResourceLine{
		private Long companyId;
		private String companyName;
		private Long stuId;
		private Long cstmId;
		private String cstmName;
		private Long ownerId;
		private String ownerName;
		private Long stuFromId;
		private String stuFromName;
		
		private Date createdAt;
		
		private List<LxContactRecord> contactRecords;
		private List<StuZxgw> zxgwList;
		
		public Long getCompanyId() {
			return companyId;
		}
		public void setCompanyId(Long companyId) {
			this.companyId = companyId;
		}

		public String getCompanyName() {
			return companyName;
		}
		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}
		
		public Long getStuId() {
			return stuId;
		}
		public void setStuId(Long stuId) {
			this.stuId = stuId;
		}
		
		public Long getCstmId() {
			return cstmId;
		}
		public void setCstmId(Long cstmId) {
			this.cstmId = cstmId;
		}
		
		public String getCstmName() {
			return cstmName;
		}
		public void setCstmName(String cstmName) {
			this.cstmName = cstmName;
		}

		public Long getOwnerId() {
			return ownerId;
		}
		public void setOwnerId(Long ownerId) {
			this.ownerId = ownerId;
		}
		
		public String getOwnerName() {
			return ownerName;
		}
		public void setOwnerName(String ownerName) {
			this.ownerName = ownerName;
		}
		
		public Date getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(Date createdAt) {
			this.createdAt = createdAt;
		}
		public Long getStuFromId() {
			return stuFromId;
		}
		public void setStuFromId(Long stuFromId) {
			this.stuFromId = stuFromId;
		}
		public String getStuFromName() {
			return stuFromName;
		}
		public void setStuFromName(String stuFromName) {
			this.stuFromName = stuFromName;
		}
		
		public List<LxContactRecord> getContactRecords(){
			return this.contactRecords;
		}
		public void setContactRecords(List<LxContactRecord> contactRecords){
			this.contactRecords = contactRecords;
		}
		
		public List<StuZxgw> getZxgwList(){
			return this.zxgwList;
		}
		public void setZxgwList(List<StuZxgw> zxgwList){
			this.zxgwList = zxgwList;
		}
	}
}
