package com.niu.crm.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.alibaba.fastjson.JSONArray;
import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.model.Dict;
import com.niu.crm.model.User;
import com.niu.crm.service.DictService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.vo.DictVO;
import com.niu.crm.vo.UserFuncVO;

@Controller
@RequestMapping(value = "/dict")
public class DictController extends BaseController{
	@Autowired
	private DictService dictService;
	
	@Autowired
	private UserFuncService userFuncService;
	
	@RequestMapping(value = "/refreshCache")
    @ResponseBody
    public ResponseObject<Boolean> refreshCache(){
		dictService.refrechCache();		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/main")
    public String dictMain(){
		return "dict/main";
	}
	
	
	@RequestMapping(value = "/treeData")
    public void treeData(HttpServletResponse response, String rootCode) throws IOException{
		response.setContentType("text/json");
		response.setCharacterEncoding("gb2312");
		
		Long rootId = null;
		if( !StringUtils.isEmpty(rootCode) )
			rootId = dictService.loadByCode(rootCode).getId();
		
		JSONArray tree = dictService.getJSONTree(rootId);
		String szTxt = tree.toString();
		response.getWriter().write( szTxt );
	}
	
	@RequestMapping(value = "/contractTypes")
    @ResponseBody
    public Map<String, Object> contractTypes(String keywords){
		Dict dict = dictService.loadByCode("custcontract.type");
		return children(dict.getId(), keywords);
	}
	
	@RequestMapping(value = "/{id}/children")
    @ResponseBody
    public Map<String, Object> children(@PathVariable("id") Long id, String keywords){
		Map<String,Object> map = new java.util.HashMap<String, Object>();
		List<Dict> children = dictService.loadChildren(id);
		List<Dict> ls = new ArrayList<>();
		if(StringUtils.isBlank(keywords)){
			ls.addAll(children);
		}else{
			for(Dict dict:children){
				keywords = keywords.toUpperCase().trim();
				String dictName = dict.getDictName()==null?"":dict.getDictName();
				String dictKeywords = dict.getKeywords()==null?"":dict.getKeywords();
				
				if(dictName.indexOf(keywords) >=0 || dictKeywords.indexOf(keywords) >=0)
					ls.add(dict);
			}
		}
		List<DictVO> list = new ArrayList<>();
		for(Dict dict:ls){
			list.add( dictService.dict2VO(dict) );
		}
		
		map.put("total", list.size());
		map.put("rows", list);
		
		return map;
	}
	
	@RequestMapping(value = "/edit")
    public String dictEdit(Long id, Model model){
		Dict dict = dictService.load(id);
		model.addAttribute("dict", dict);
		
		if (dict.getParentId() != null) {
			Dict pDict = dictService.load(dict.getParentId());
			model.addAttribute("pDict", pDict);
		}
		
		return "dict/detail";
	}
	
	@RequestMapping(value = "/create")
    public String dictCreate(Long parentId, Model model){
		
		Dict pDict = dictService.load(parentId);
		model.addAttribute("pDict", pDict);
		
		return "dict/detail";
	}
	
	@RequestMapping(value = "/{id}")
    @ResponseBody
    public ResponseObject<Dict> getDict(@PathVariable("id") Long id){
		Dict dict = dictService.load(id);
		if(dict == null)
			throw new BizException(GlobalErrorCode.DATA_NOT_EXIST, "数据没找到");
		
		return new ResponseObject<Dict>(dict);
	}
	
	@RequestMapping(value = "/save")
    @ResponseBody
    public ResponseObject<Dict> save(DictVO dict){
		User user = this.getCurrentUser();
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "admin");
		if( func == null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED,"权限不够");
		
		if(dict.getId() == null)
			dictService.add(user, dict);
		else
			dictService.update(user, dict);
		
		return new ResponseObject<Dict>(dict);
	}
	
	@RequestMapping(value = "/delete")
    @ResponseBody
    public ResponseObject<Boolean> delete(Long id){
		User user = this.getCurrentUser();
		
		List<Dict> ls = dictService.loadChildren(id);
		if(ls.size() >0)
			return new ResponseObject<Boolean>(Boolean.FALSE);
		
		dictService.delete(user, id);		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/stuFromMerge")
    @ResponseBody
    public ResponseObject<Boolean> stuFromMerge(Long fromId, Long toId){
		User user = this.getCurrentUser();
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "admin");
		if( func == null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED,"权限不够");
				
		dictService.stuFromMergeTo(user, fromId, toId);
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/stuFromMoveTo")
    @ResponseBody
    public ResponseObject<Boolean> stuFromMoveTo(Long fromId, Long toId){
		User user = this.getCurrentUser();
		UserFuncVO func = userFuncService.loadByCode(user.getId(), "admin");
		if( func == null)
			throw new BizException(GlobalErrorCode.UNAUTHORIZED,"权限不够");
				
		dictService.stuFromMoveTo(user, fromId, toId);
		
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	public static void main(String... args){
		System.out.println("hello");
	}
}
