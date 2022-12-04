package com.niu.crm.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.niu.crm.form.UserSearchForm;
import com.niu.crm.service.UserService;
import com.niu.crm.service.WechatService;
import com.niu.crm.vo.UserVO;

@Component
public class UserTask extends BaseTask {
	@Autowired
	private UserService userService;
	
	@Autowired
	private WechatService wechatService;
	
	/**
	 * 每天5点整同步用户信息 扫描提醒
	 */
	@Scheduled(cron = "0 0 5 * * ?")
	public void update2Wechat() throws Exception{
		logger.info("--------------update2Wechat t=" + System.currentTimeMillis() );
		
		UserSearchForm form = new UserSearchForm();
		Pageable pager = null;
		int pageSize=200;
		
		int total = userService.countUser(form); 
		int pages = (total -1)/pageSize +1;
		
		for(int i=0; i < pages; i++){
			pager = new PageRequest(i, pageSize);
			List<UserVO> ls = userService.queryUser(form, pager);
			for(UserVO u:ls){
				if(u.isEnabled()){
					logger.info("--------------createUser1 userName=" + u.getName() + "," + u.getAccount() );
					wechatService.createUser(u.getId());
				}else{
					JSONObject json = wechatService.getUser(u.getId());
					int errcode = json.getIntValue("errcode");
					if(errcode == 0 && json.getIntValue("status") !=1){
						logger.info("--------------deleteUser userName=" + u.getName() + "," + u.getAccount() );
						wechatService.deleteUser(u.getId());
					}else{
						logger.info("--------------createUser2 userName=" + u.getName() + "," + u.getAccount() );
						wechatService.createUser(u.getId());
					}
				}	
			}
		}
	}

}
