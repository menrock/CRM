package com.niu.crm.web.controller;


import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.model.User;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.impl.OssServiceImpl;
import com.niu.crm.util.VerifyCodeUtils;

@Controller
public class HomeController extends BaseController{
	@Autowired
	private CustomerService customerService;
	@Autowired
	private OssServiceImpl ossService;
	
	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@RequestMapping(value = "signin")
	public String signin(HttpServletRequest request, ModelMap model) {
		Exception e = (Exception) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		if(e != null){
			model.addAttribute("message", "用户名或密码错误" );
		}	
        return "signin/signin";
    }
	
	@RequestMapping(value = "layout/north")
	public String north(HttpServletRequest request, ModelMap model) {
		User user = this.getCurrentUser();
		model.put("user", user);
        return "layout/north";
    }
	
	/**
	 * 判断用户是否已经登录
	 * @param account
	 * @param oldPasswd
	 * @param newPasswd
	 * @return
	 */
	@RequestMapping(value = "/hasLogin")
    @ResponseBody
	public ResponseObject<Boolean> hasLogin(String account, String oldPasswd, String newPasswd){
		User user = this.getCurrentUser(); 
		
		return new ResponseObject<Boolean>(user==null?false:true);
	}
	
	@RequestMapping(value="/login") 
	@ResponseBody
	public LoginInfo login(@RequestParam(defaultValue="") String username,@RequestParam(defaultValue="") String password,HttpServletRequest request){
		/*
		if(!checkValidateCode(request)){
			return new LoginInfo().failed().setMsg("验证码错误！");
		}*/
		String fromIP = request.getRemoteAddr();
		username = username.trim();
		
		User u = userService.loadByAccount(username);
		if(u == null)
			return new LoginInfo().failed().setMsg("账号不存在！");
		if( !u.isEnabled() )
			return new LoginInfo().failed().setMsg("账号禁用！");
		
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
/*		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CwSysUser.class,"cwSysUser");
		detachedCriteria.add(Restrictions.eq("userNo", username));
		if(cwSysUserService.countUser(detachedCriteria)==0){
			return new LoginInfo().failed().msg("用户名: "+username+" 不存在.");
		}
*/		try {
			Authentication authentication = authenticationManager.authenticate(authRequest); //调用loadUserByUsername
			SecurityContextHolder.getContext().setAuthentication(authentication);
			HttpSession session = request.getSession();
			session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext()); // 这个非常重要，否则验证后将无法登陆
			
			User user = this.getCurrentUser();
			//登录日志
			userService.loginLog(user, fromIP);
			
			return new LoginInfo().success().setMsg(authentication.getName());
		} catch (AuthenticationException ex) {
			return new LoginInfo().failed().setMsg("用户名或密码错误");
		}
	}
	
	
	
	/**
	 * 验证码判断
	 * @param request
	 * @return
	 */
	protected boolean checkValidateCode(HttpServletRequest request) {
		String attrName = "verifyResult";
		
		HttpSession session = request.getSession();
		if(session.getAttribute(attrName) ==null)
			return false;
		
		String attrValue = session.getAttribute(attrName).toString(); // 获取存于session的验证值
		request.getSession().setAttribute(attrName, null);
		String user_verifyCode = request.getParameter("verifyCode");// 获取用户输入验证码
		if (null == user_verifyCode
				|| !attrValue.equalsIgnoreCase(user_verifyCode)) {
			return false;
		}
		return true;
	}
	
	@RequestMapping(value="/verifyCode") 
	public void verifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException { 
        response.setHeader("Pragma", "No-cache"); 
        response.setHeader("Cache-Control", "no-cache"); 
        response.setDateHeader("Expires", 0); 
        response.setContentType("image/jpeg"); 
        
        //生成随机字串 
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4); 
        //存入会话session 
        HttpSession session = request.getSession(true); 
        //删除以前的
        session.removeAttribute("verifyResult");
        session.setAttribute("verifyResult", verifyCode.toLowerCase()); 
        //生成图片 
        int w = 100, h = 30; 
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode); 
   
    } 
	
	public static class LoginInfo{
		private Boolean successFlag;
		private String msg;
		
		public Boolean getSuccessFlag(){
			return successFlag;
		}
		
		public LoginInfo success(){
			this.successFlag = true;
			return this;
		}
		
		public LoginInfo failed(){
			this.successFlag = false;
			return this;
		}

		public String getMsg() {
			return msg;
		}

		public LoginInfo setMsg(String msg) {
			this.msg = msg;
			return this;
		}
	}
}
