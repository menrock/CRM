package com.niu.crm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.core.error.BizException;
import com.niu.crm.dao.mapper.UserMapper;
import com.niu.crm.form.UserSearchForm;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.WechatService;
import com.niu.crm.vo.UserVO;

@Service
public class UserServiceImpl extends BaseService implements UserService {
	private Map<Long,User> cacheById = null;
	private Map<String,User> cacheByAccount = null;
	
	@Autowired 
	private UnitService unitService;

	@Autowired 
	private WechatService wechatService;
	
	@Autowired 
	private UserMapper userMapper;
	
    @Autowired
    private PasswordEncoder    pwdEncoder;
    
    @Override
   	public void refrechCache(){
    	cacheById = null;
    	cacheByAccount = null;
    }
    
	private void refreshCache(Long id, String account) {
		if (cacheById == null) {
			cacheById = new HashMap<Long, User>();
			cacheByAccount = new HashMap<String, User>();
		}

		User user = null;
		if (null != id)
			user = userMapper.selectById(id);
		else
			user = userMapper.selectByAccount(account);
		

		if (null != user) {
			cacheById.put(user.getId(), user);
			cacheByAccount.put(user.getAccount(), user);
		}
	}
	
	@Override
    public String getUserName(Long id){
    	return load(id).getName();
    }
    
	@Override
    public User load(Long id){
    	return load(id, null);
    }
    
	@Override
	public User loadByAccount(String account) {
		return load(null, account);
	}

	private User load(Long id, String account) {
		User user = null;
		if (null != cacheById) {
			if (null != id)
				user = cacheById.get(id);
			else
				user = cacheByAccount.get(account);
		}

		if (user == null) {
			refreshCache(id, account);
			if (null != id)
				user = cacheById.get(id);
			else
				user = cacheByAccount.get(account);
		}
		
		if(user !=null){
			Unit unit = unitService.load(user.getUnitId());
			user.setCompanyId(unit.getCompanyId());
		}

		return user;
	}

    public boolean add(User user){
    	// 用户的验证应该放到这里来，否则有安全问题
    	String account = user.getAccount().trim().toLowerCase();
    	user.setAccount(account);
    	
        User u1 = userMapper.selectByAccount(account);
        if (u1 != null) {
            //throw new BizException(GlobalErrorCode.USER_EXIST, "用户名已被占用，请重新输入！");
        	return false;
        }
        if(StringUtils.isEmpty(user.getPassword()))
        	user.setPassword("crm2018");

        String pwd = pwdEncoder.encode(user.getPassword());
        user.setPassword(pwd);

        int result = userMapper.insert(user);
        
        refreshCache(user.getId(), null);
        
        //同步微信
        try{
        	if(user.isEnabled())
        		wechatService.createUser(user.getId());
			refreshCache(user.getId(), null);
		}
		catch(Exception e){
			this.getLogger().error("id=" + user.getId(), e);
		}
        
        return result > 0;
    }

    public int update(User user){
    	String account = user.getAccount().trim().toLowerCase();
    	user.setAccount(account);
        
    	int count = userMapper.update(user);
        refreshCache(user.getId(), null);
        
        //同步微信
        try{
        	if(user.isEnabled())
        		wechatService.createUser(user.getId());
        	else
        		wechatService.deleteUser(user.getId());
        	
			refreshCache(user.getId(), null);
		}
		catch(Exception e){
			this.getLogger().error("id=" + user.getId(), e);
		}
        
        return count;
    }

    public boolean changePwd(Long userId, String password){
        boolean changed = false;
        
        String newPwd = pwdEncoder.encode(password);
        changed = userMapper.changePwd(userId, newPwd) > 0;
        
        refreshCache(userId, null);

        return changed;
    }

    public boolean isRegistered(String loginname){
        return userMapper.countRegistered(loginname) > 0;
    }

    /**
     * 根据用户名获取用户信息-- spring-security 接口需要的方法
     */
    public User loadUserByUsername(String account) throws UsernameNotFoundException{    	    	
    	User user = userMapper.selectByAccount(account);
    	if(user == null)
    		throw new UsernameNotFoundException("user not found");
    	
    	if(	user.getUnitId() != null){
    		Unit unit = unitService.load(user.getUnitId());
    		String unitCode = unit.getCode();
    		int idx = unitCode.indexOf(".");
    		if(idx >0)
    		{
    			idx = unitCode.indexOf(".", idx +1);
        		if(idx >=0)
        			unit = unitService.loadByCode(unitCode.substring(0, idx));
        		else
        			unit = unitService.loadByCode(unitCode);
        		
        		user.setCompanyId(unit.getId());        		
    		}
    	}
    	
    	return user;
    }

    /**
     * 用户登录
     * @param userName
     * @param userPwd
     * @return
     */
    @Override
    public User login(String account, String userPwd){
    	User u = userMapper.selectByAccount(account);

        if (u == null) {
            getLogger().debug("u:{} not exist", account);
            throw new BizException(GlobalErrorCode.USER_NOT_EXIST, "valid.user.notExist.message");
        }

        if (!pwdEncoder.matches(userPwd, u.getPassword())) {
            getLogger().debug("userPwd:{} password.invalid", userPwd);
            throw new BizException(GlobalErrorCode.PASSWORD_INVALID, "password.invalid");
        }
        return u;
    }
    
    
    @Override
    public int countUser(UserSearchForm form){
    	return userMapper.queryCount(form);
    }
	
    @Override
    public List<UserVO> queryUser(UserSearchForm form, Pageable pageable){
    	List<User> ls = userMapper.query(form, pageable);
    	List<UserVO> lsUser = new java.util.ArrayList<UserVO>();
    	for(User user:ls){
    		UserVO vo = new UserVO();
    		lsUser.add(vo);
    		if( null != user.getUnitId() ){
    			Unit unit = unitService.load(user.getUnitId());
    			vo.setUnit(unit);
    			
				if (unit.getCompanyId() != null) {
					Unit company = unitService.load(unit.getCompanyId());
					vo.setCompanyName(company.getName());
				}
    		}
    		
    		BeanUtils.copyProperties(user, vo);    		
    	}
    	return lsUser;
    }
    
    @Override
    public void setOnline(Long userId){
    	userMapper.updateOnline(userId, Boolean.TRUE);
    }
    
    @Override
    public void setOffline(Long userId){
    	userMapper.updateOnline(userId, Boolean.FALSE);
    }
        
    @Override
    public void loginLog(User user, String fromIP){
    	userMapper.addLoginLog(user.getId(), user.getName(), fromIP);
    }

}
