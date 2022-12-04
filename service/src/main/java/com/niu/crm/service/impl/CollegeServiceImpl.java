package com.niu.crm.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.niu.crm.service.BaseService;
import com.niu.crm.service.CollegeService;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.dao.mapper.CollegeMapper;
import com.niu.crm.form.CollegeSearchForm;
import com.niu.crm.model.College;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;

@Service
public class CollegeServiceImpl extends BaseService implements CollegeService {
	
    @Autowired
    private CollegeMapper  collegeMapper;
	
    @Override
	public College load(Long id){
		return collegeMapper.selectByPrimaryKey(id);
	}
    
    @Override
	public int add(User user, College college){
    	college.setCreatorId(user.getId());
    	
		return collegeMapper.insert(college);
	}
    
    @Override
	public void delete(User user, Long id){
    	collegeMapper.delete(id);
	}
    
    @Override
	public int update(User user, College college){
		return collegeMapper.update(college);
	}
    
    @Override
    public int countCollege(CollegeSearchForm form){
    	return collegeMapper.countCollege(form);
    }
    
    @Override
    public List<College> queryCollege(CollegeSearchForm form, Pageable pager){
    	return collegeMapper.queryCollege(form, pager);
    }
    
    @Transactional
    @Override
    public int importCollege(User user, HttpServletRequest request, MultipartFile file) throws IOException
    {
    	String colType  = request.getParameter("colType");
    	String colLevel = request.getParameter("colLevel");
    	String countryCode = request.getParameter("countryCode");
    	
    	if(StringUtils.isEmpty(countryCode))
    		countryCode = "US";
    	
    	Workbook wb = null;
    	Sheet sheet = null;
    	
    	int count = 0;
    	
    	String fileNameLower = file.getName().toLowerCase();

    	if( fileNameLower.endsWith(".xlsx"))
    		wb = new XSSFWorkbook( file.getInputStream() );
    	else
    		wb = new HSSFWorkbook( file.getInputStream() );
    	
    	try{
    		sheet = wb.getSheetAt(0);
    		int lastRowNum = sheet.getLastRowNum();
    		if(lastRowNum <1)
    			return 0;
	
    		/*
	    	captionRow = sheet.getRow(0);//文件格式校验
	    	if(captionRow.getLastCellNum() != 9)
	    		throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "文件格式错误  列数不对");
	    	
	    	List<String> lsCaption = new ArrayList<>();
	    	for(int i=0; i < captionRow.getLastCellNum(); i++){
	    		lsCaption.add( captionRow.getCell(i).getStringCellValue() );
	    	}
	    	*/
	    	
	    	String cnName=null, enName = null;
	    	for(int i=1; i <= lastRowNum; i++){
	    		Row row = sheet.getRow(i);
	    		cnName = row.getCell(0).getStringCellValue();
	    		enName = row.getCell(1).getStringCellValue();
	    		
	    		if(StringUtils.isEmpty(enName))
	    			continue;
	    		
	    		CollegeSearchForm form = new CollegeSearchForm();
	    		form.setEnName(enName);
	    		int colCount = collegeMapper.countCollege(form);
	    		if(colCount >0)
	    			continue;
	    		
	    		College college = new College();
	    		college.setCountryCode( countryCode );
	    		college.setEnName(enName);
	    		college.setCnName(cnName);
	    		college.setColType(colType);
	    		college.setLevel(colLevel);
	    		
	    		
	    		
	    		this.add(user, college);
	    		count ++;
	    	}
    	}catch(Exception e){
	    	getLogger().error("", e);
	    	throw new BizException(GlobalErrorCode.INTERNAL_ERROR, "导入失败");	
	    }finally{
	    	if( wb !=null)
	    		wb.close();
	    }
    	
    	return count;
    }
}
