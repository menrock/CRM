package com.niu.crm.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.form.UserSearchForm;
import com.niu.crm.model.User;
import com.niu.crm.vo.LxCustomerVO;
import com.niu.crm.vo.UserVO;

public interface UserService extends UserDetailsService {
	
	//登录日志
	void loginLog(User user, String fromIP);
	
	void refrechCache();
	
	String getUserName(Long id);
	
    User load(Long id) ;
    
    User loadByAccount(String account);

	boolean add(User user);

    int update(User user);

    boolean changePwd(Long userId, String password);

    boolean isRegistered(String loginname);

    /**
     * 根据用户名获取用户信息-- spring-security 接口需要的方法
     */
    User loadUserByUsername(String loginname) throws UsernameNotFoundException;

    /**
     * 用户登陆
     * @param userName
     * @param userPwd
     * @return
     */
    User login(String userName, String userPwd);
    
    int countUser(UserSearchForm form);
	
    List<UserVO> queryUser(UserSearchForm form, Pageable pageable);
    
    /**
     * 将用户设置为 在岗 状态
     * @param userId
     * @return
     */
    void setOnline(Long userId);
    
    /**
     * 将用户设置为离岗 状态
     * @param userId
     * @return
     */
    void setOffline(Long userId);

}
