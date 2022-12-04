package com.niu.crm.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.form.CollegeSearchForm;
import com.niu.crm.model.College;
import com.niu.crm.model.Country;
import com.niu.crm.model.User;
import com.niu.crm.service.CollegeService;
import com.niu.crm.service.CountryService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.vo.DictVO;
import com.niu.crm.vo.UserFuncVO;

@Controller
@RequestMapping(value = "/college")
public class CollegeController extends BaseController{
	@Autowired
	private CollegeService collegeService;
	@Autowired
	private CountryService countryService;
	
	@Autowired
	private UserFuncService userFuncService;
	
	@RequestMapping(value = "/listPage.do")
    public String list(ModelMap model) throws IOException{
		model.addAttribute("countryList", countryService.loadAll() );
		
		return "college/list";
	}
	
	@RequestMapping(value = "/listData.do")
    @ResponseBody
    public Map<String, Object> listData(HttpServletRequest req,CollegeSearchForm form, Pageable pager){
		User user = this.getCurrentUser();
		pager = convertPager(req, pager);
		
		if( StringUtils.isNotEmpty(form.getEnName()) ){
			form.setEnName( "%" + form.getEnName().trim() + "%" );
		}else{
			form.setEnName(null);
		}
		if( StringUtils.isNotEmpty(form.getCnName()) ){
			form.setCnName( "%" + form.getCnName().trim() + "%" );
		}else{
			form.setCnName(null);
		}
		
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		Integer count = collegeService.countCollege(form);
		List<College> list = collegeService.queryCollege(form, pager);
		for(College college:list){
			String szCode = college.getCountryCode();
			if(StringUtils.isNotEmpty(szCode)){
				Country country = countryService.loadByCode(szCode);
				college.setCountryName(country.getName());
			}
		}
		
		map.put("total", count);
		map.put("rows", list);
		
		return map;
	}
	
	@RequestMapping(value = "/save")
    @ResponseBody
    public ResponseObject<College> save(College college){
		User user = this.getCurrentUser();
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "admin");
		if( func == null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED,"权限不够");
		
		if(StringUtils.isEmpty(college.getCountryCode()))
			college.setCountryCode(null);
		if(StringUtils.isEmpty(college.getColType()))
			college.setColType(null);
		
		if(college.getId() == null)
			collegeService.add(user, college);
		else
			collegeService.update(user, college);
		
		return new ResponseObject<College>(college);
	}
	
	@RequestMapping(value = "/collegeInfo.do")
    @ResponseBody
    public ResponseObject<College> info(Long id){
		College college = collegeService.load(id);
		
		return new ResponseObject<College>(college);
	}
	
	@RequestMapping(value = "/delete")
    @ResponseBody
    public ResponseObject<Boolean> delete(Long id){
		User user = this.getCurrentUser();
		
		collegeService.delete(user, id);		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	/**
	 * 导入院校页面
	 * @return
	 */
	@RequestMapping(value = "/importCollege.do")
    public String importCollege(ModelMap model){
		User user = this.getCurrentUser();
		model.addAttribute("user", user);
		
		return "college/importCollege";
	}


/**
	 * 导入院校信息到数据库
	 * @return
	 */
	@RequestMapping(value = "/doImportCollege.do")
    @ResponseBody
    public ResponseObject<Map<String,Object>> doImportCollege(@RequestParam(value = "file") MultipartFile file, 
    		HttpServletRequest request, ModelMap model){
		User user = this.getCurrentUser();
		Map<String,Object> map = new HashMap<>();
		
		String fileName = (file==null)?null:file.getOriginalFilename().toLowerCase();
		if (fileName ==null ) {
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "请选择上传文件");
		}else if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
			throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "只支持excel文件格式");
		}
		else{
			Integer count = null; 
			
			try{
				count = collegeService.importCollege(user, request, file);				
			} catch (BizException e) {
				getLogger().error("", e);
				throw e;
			} catch (Exception e) {
				getLogger().error("", e);
				throw new BizException(GlobalErrorCode.INTERNAL_ERROR,
						e.getMessage());
			}
			
			map.put("count", count);
			map.put("msg", "导入成功");			
		}
		
		return new ResponseObject<>(map);
	}
	
	public static void main(String... args){
		System.out.println("hello");
	}
}
