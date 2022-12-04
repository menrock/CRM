package com.niu.crm.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.model.CustomerRecommend;
import com.niu.crm.model.User;
import com.niu.crm.model.type.BizType;
import com.niu.crm.service.CustomerRecommendService;


@Controller
@RequestMapping(value = "/customerRecommend")
public class CustomerRecommendController extends BaseController {
	
	@Autowired
	private CustomerRecommendService recommendService;
	
	@RequestMapping(value = "/lx2Px")
    @ResponseBody
    public ResponseObject<Boolean> lx2Px(CustomerRecommend recommend){
		User user = this.getCurrentUser();
		recommend.setFromBiz(BizType.LX);
		recommend.setToBiz(BizType.PX);
		recommend.setFromGwId(user.getId());
		
		recommendService.lx2Px(user, recommend);
			
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/lx2Cp")
    @ResponseBody
    public ResponseObject<Boolean>  lx2Cp(User user, CustomerRecommend recommend){
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/refrechCache")
    @ResponseBody
    public ResponseObject<Boolean>  px2Lx(User user, CustomerRecommend recommend){
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/px2Cp")
    @ResponseBody
    public ResponseObject<Boolean>  px2Cp(User user, CustomerRecommend recommend){
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/cp2Lx")
    @ResponseBody
    public ResponseObject<Boolean>  cp2Lx(User user, CustomerRecommend recommend){
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
	
	@RequestMapping(value = "/cp2Px")
    @ResponseBody
    public ResponseObject<Boolean>  cp2Px(User user, CustomerRecommend recommend){
		return new ResponseObject<Boolean>(Boolean.TRUE);
	}
}
