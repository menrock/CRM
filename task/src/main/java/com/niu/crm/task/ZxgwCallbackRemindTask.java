package com.niu.crm.task;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.niu.crm.form.ContactRecordSearchForm;
import com.niu.crm.model.Customer;
import com.niu.crm.model.LxContactRecord;
import com.niu.crm.model.SmsMessage;
import com.niu.crm.model.StuZxgwEx;
import com.niu.crm.model.SysMessage;
import com.niu.crm.model.User;
import com.niu.crm.model.ZxgwCallbackRemind;
import com.niu.crm.model.type.CallbackRemindType;
import com.niu.crm.model.type.MessageType;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.LxContactRecordService;
import com.niu.crm.service.SmsService;
import com.niu.crm.service.SysMessageService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.WechatService;
import com.niu.crm.service.ZxgwCallbackRemindService;

@Component
public class ZxgwCallbackRemindTask extends BaseTask{
	@Autowired
	private ZxgwCallbackRemindService remindService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private UserService userService;
	@Autowired
	private DictService dictService;
	@Autowired
	private WechatService wechatService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private SysMessageService sysMessageService;
	@Autowired
	private LxContactRecordService contactRecordService;
	
	private static final long ONE_HOUR = 60*60000L; 
	
	/**
	 * cron 格式
	 * 秒（0~59） 分钟（0~59）小时（0~23）
	 * 天（月）（1~31，但是你需要考虑你月的天数）
	 * 月（1~12）
	 * 星期几（1~7 1=MON 或 MON，TUE，WED，THU，FRI，SAT SUN）
	 */
		
	/**
	 * 每天8-18点 每隔5分钟 提醒
	 */
	/*
	@Scheduled(cron = "0 0/5 8-18 * * ?")
	public void remindOfMinAndDay() {
		List<StuZxgwEx> ls = remindService.getNeedRemindForMinAndDay(2000);
		String fromCode = null;
		Calendar cal = Calendar.getInstance();

		for(StuZxgwEx ex:ls){
			cal.setTime(ex.getAssignDate());
			if(cal.get(Calendar.HOUR_OF_DAY) >=18){
				this.remindOfDay(ex);
				continue;
			}
			
			fromCode = dictService.load(ex.getStuFromId()).getDictCode();
			
			if(fromCode.equals("stufrom.14.01") || fromCode.startsWith("stufrom.14.01.")){
				//在线咨询
				this.remindOf25Min(ex);
			}else if(fromCode.equals("stufrom.14.01") || fromCode.startsWith("stufrom.14.01.")){
				//新媒体资源
				this.remindOf25Min(ex);				
			}else if(fromCode.equals("stufrom.14.04") || fromCode.equals("stufrom.14.05")){
				//前台电话、400电话
				this.remindOf25Min(ex);
			}else if(fromCode.equals("stufrom.03.01") || fromCode.startsWith("stufrom.03.01.") ){
				//50MIN回访  集团渠道资源 -第三方渠道
				this.remindOf50Min(ex);
			}else{
				this.remindOfDay(ex);
			}
			
			this.remindOfDay(ex);
		}
	}
	private void remindOf25Min(StuZxgwEx ex) {
		ZxgwCallbackRemind remind = new ZxgwCallbackRemind();
		remind.setStuId(ex.getStuId());
		remind.setZxgwId(ex.getZxgwId());
		remind.setRemindType(CallbackRemindType.MIN25);
		
		Calendar latestTime = Calendar.getInstance();
		latestTime.setTime(ex.getAssignDate());
		latestTime.add(Calendar.MINUTE, 30);
		remind.setLatestContactTime(latestTime.getTime());
			
		Customer cust = customerService.load(ex.getCstmId());			
		String msg = "亲!" + cust.getName() + "(" + cust.getMobile() + ") 需要你关心下，做个回访吧。";
		sendMessage(msg, ex, MessageType.SYS|MessageType.SMS|MessageType.WX);			
		remindService.add(remind);
	}
	
	private void remindOf50Min(StuZxgwEx ex) {
		ZxgwCallbackRemind remind = new ZxgwCallbackRemind();
		remind.setStuId(ex.getStuId());
		remind.setZxgwId(ex.getZxgwId());
		remind.setRemindType(CallbackRemindType.MIN50);

		Calendar latestTime = Calendar.getInstance();
		latestTime.setTime(ex.getAssignDate());
		latestTime.add(Calendar.MINUTE, 60);
		remind.setLatestContactTime(latestTime.getTime());

		Customer cust = customerService.load(ex.getCstmId());
		String msg = "亲!" + cust.getName() + "(" + cust.getMobile() + ") 请回访。";
		sendMessage(msg, ex, MessageType.SYS | MessageType.SMS | MessageType.WX);
		remindService.add(remind);
	}
	private void remindOfDay(StuZxgwEx ex) {
		String remindType = CallbackRemindType.DAY1.toString();
		//今天零点
		Calendar zeroTime = Calendar.getInstance(); 
		
		//前一天录入的数据， 中午12点前完成回访
		Calendar latestTime1 = Calendar.getInstance(); 
		//当天18点前录入的数据，当天完成回访
		Calendar latestTime2 = Calendar.getInstance();
		{
			Calendar cal = Calendar.getInstance();			
			
			int y = cal.get(Calendar.YEAR);
			int m = cal.get(Calendar.MONTH);
			int d = cal.get(Calendar.DATE);		
			
			zeroTime.clear();
			zeroTime.set(y,m,d);
			
			latestTime1.clear();
			latestTime1.set(y,m,d,12,0);
			
			latestTime2.clear();
			latestTime2.set(y,m,d,22,0);
		}	
		
		{
			ZxgwCallbackRemind remind = new ZxgwCallbackRemind();
			remind.setStuId(ex.getStuId());
			remind.setZxgwId(ex.getZxgwId());
			remind.setRemindType(CallbackRemindType.DAY1);
			if(ex.getAssignDate().getTime() < zeroTime.getTimeInMillis())
				remind.setLatestContactTime(latestTime1.getTime());
			else
				remind.setLatestContactTime(latestTime2.getTime());
						
			Customer cust = customerService.load(ex.getCstmId());			
			String msg = "亲!" + cust.getName() + "(" + cust.getMobile() + ") 请做日回访。";
			sendMessage(msg, ex, MessageType.SYS);			
			remindService.add(remind);
		}
	}
	*/
	/**
	 * 周回访（【紧急】+【重要】+【未接听】)
	 * 周一，周二 (8-9点生成提醒信息)，
	 */
	@Scheduled(cron = "0 0/10 8-9 ? * 1-2")
	public void remindOfWeekly() {
		String remindType = CallbackRemindType.WEEKLY.toString();
		logger.info("--------------remindType=" + remindType +" t=" + System.currentTimeMillis() );
		
		Calendar latestTime = Calendar.getInstance();
		{
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 7);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			
			int y = cal.get(Calendar.YEAR);
			int m = cal.get(Calendar.MONTH);
			int d = cal.get(Calendar.DATE);
			latestTime.clear();
			latestTime.set(y,m,d,23,59,59);
		}
		
		List<StuZxgwEx> ls = remindService.getNeedRemindForWeek(2000);
		for(StuZxgwEx ex:ls){
			ZxgwCallbackRemind remind = new ZxgwCallbackRemind();
			remind.setStuId(ex.getStuId());
			remind.setZxgwId(ex.getZxgwId());
			remind.setRemindType(CallbackRemindType.WEEKLY);
			remind.setLatestContactTime(latestTime.getTime());
			
			Customer cust = customerService.load(ex.getCstmId());			
			String msg = "亲!" + cust.getName() + "(" + cust.getMobile() + ") 请做周回访。";
			sendMessage(msg, ex, MessageType.SYS);			
			remindService.add(remind);
		}
	} 
	
	/**
	 * 月度回访（【犹豫待定】+【长期潜在】（每月1,2日提醒， 对应半月内完成)
	 * 8-9点产生提醒信息
	 */
	@Scheduled(cron = "0 0/10 8-9 1,2 * ?")
	public void remindOfMonthly() {
		String remindType = CallbackRemindType.MONTHLY.toString();
		logger.info("--------------remindType=" + remindType +" t=" + System.currentTimeMillis() );
		
		Calendar latestTime = Calendar.getInstance();
		{
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DATE, -1);
			
			int y = cal.get(Calendar.YEAR);
			int m = cal.get(Calendar.MONTH);
			int d = cal.get(Calendar.DATE);
			latestTime.clear();
			latestTime.set(y,m,d,23,59,59);
		}
		
		List<StuZxgwEx> ls = remindService.getNeedRemindForMonth(2000);
		for(StuZxgwEx ex:ls){
			ZxgwCallbackRemind remind = new ZxgwCallbackRemind();
			remind.setStuId(ex.getStuId());
			remind.setSourceZxgwId(ex.getZxgwId());
			remind.setZxgwId(ex.getZxgwId());
			remind.setRemindType(CallbackRemindType.MONTHLY);
			remind.setLatestContactTime(latestTime.getTime());
			
			Customer cust = customerService.load(ex.getCstmId());			
			String msg = "亲!" + cust.getName() + "(" + cust.getMobile() + ") 请做月度回访。";
			sendMessage(msg, ex, MessageType.SYS);			
			remindService.add(remind);
		}
	}
	
	/**
	 * 季度回访：内网所有资源回访（三月、六月、九月、十二月的最后两周）
	 * 每天8-10点提醒，
	 */
	@Scheduled(cron = "0 3/15 8,9 1,2,15,16 3,6,9,12 ?")
	public void remindOfSeason() {
		/*
		String remindType = CallbackRemindType.SEASON.toString();
		logger.info("--------------remindType=" + remindType +" t=" + System.currentTimeMillis() );
		
		Calendar latestTime = Calendar.getInstance();
		{
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DATE, -1);
			
			int y = cal.get(Calendar.YEAR);
			int m = cal.get(Calendar.MONTH);
			int d = cal.get(Calendar.DATE);
			latestTime.clear();
			latestTime.set(y,m,d,23,59,59);
		}
		
		List<StuZxgwEx> ls = remindService.getNeedRemindForSeason(2000);
		for(StuZxgwEx ex:ls){
			ZxgwCallbackRemind remind = new ZxgwCallbackRemind();
			remind.setStuId(ex.getStuId());
			remind.setSourceZxgwId(ex.getZxgwId());
			remind.setZxgwId(ex.getZxgwId());
			remind.setRemindType(CallbackRemindType.SEASON);
			remind.setLatestContactTime(latestTime.getTime());
			
			Customer cust = customerService.load(ex.getCstmId());			
			String msg = "亲!" + cust.getName() + "(" + cust.getMobile() + ") 请做季度回访。";
			sendMessage(msg, ex, MessageType.SYS);			
			remindService.add(remind);
		} */
	}
	
	
	/**
	 * 下次回访提醒
	 * 每隔5分钟 扫描提醒
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	public void nextContactAlarm() {
		Pageable pageable = new PageRequest(0, 500);
		ContactRecordSearchForm form = new ContactRecordSearchForm();
		
		Calendar cal = Calendar.getInstance();
		int y = cal.get(Calendar.YEAR);
		int m = cal.get(Calendar.MONTH);
		int d = cal.get(Calendar.DATE);
		
		Calendar nextDateFrom = Calendar.getInstance();
		nextDateFrom.add(Calendar.MINUTE, -30);
		
		Calendar nextDateTo = Calendar.getInstance();
		nextDateTo.add(Calendar.MINUTE, 5);
		
		form.setNextDateFrom(nextDateFrom.getTime());
		form.setNextDateTo(nextDateTo.getTime());
		
		//已提醒的不再重复提醒
		form.setAlarmed(Boolean.FALSE);
		
		List<LxContactRecord> ls= contactRecordService.query(form, pageable);
		
		for(LxContactRecord record :ls){
			
			//发送系统消息
			sendNextContactMsg("你设置的下次回访时间到了", record);
			
			contactRecordService.updateAlarmed(record.getId());
		}
	}
	
	private void sendNextContactMsg(String msg, LxContactRecord record){
		StuZxgwEx stuZxgw = new StuZxgwEx();
		stuZxgw.setZxgwId(record.getGwId());
		stuZxgw.setStuId(record.getStuId());
		
		Long toUserId = stuZxgw.getZxgwId();
		try{
			SysMessage sysMsg = new SysMessage();
			sysMsg.setContent(msg);
			sysMsg.setOwnerId(toUserId);
			sysMsg.setSenderId(this.getSysUserId());
			sysMsg.setUrl("/lx/student/main.do?id=" + stuZxgw.getStuId() );
			sysMsg.setSysMemo("lxContactId=" + record.getId() );
			
			sysMessageService.add(sysMsg);
		}
		catch(Exception e){
			getLogger().error("发送系统消息失败 toUser=" + toUserId, e);
		}
	}
	
	private void sendMessage(String msg, StuZxgwEx stuZxgw, int msgType){
		Long toUserId = stuZxgw.getZxgwId();
		User u = userService.load(toUserId);
		Long sysUserId = this.getSysUserId();
		
		//发送系统消息
		if( (msgType & MessageType.SYS) >1 ){
			try {
				SysMessage sysMsg = new SysMessage();
				sysMsg.setContent(msg);
				sysMsg.setOwnerId(toUserId);
				sysMsg.setSenderId(sysUserId);
				sysMsg.setUrl("/lx/student/main.do?id=" + stuZxgw.getStuId());

				sysMessageService.add(sysMsg);
			} catch (Exception e) {
				getLogger().error("发送系统消息失败 toUser=" + toUserId, e);
			}
		}
		

		boolean wxSuccess = false;
		if (!isDevProfile() || isCanTestUser(toUserId)) {
			// 先发送微信消息
			if ((msgType & MessageType.WX) > 1) {
				try {
					JSONObject json = wechatService.sendTxtMessage(msg, null,
							new Long[] { toUserId });
					if (json.getIntValue("errcode") == 0) {
						wxSuccess = true;
					} else {
						getLogger().error("发送微信消息失败 toUser=" + toUserId);
					}
				} catch (Exception e) {
					getLogger().error("发送微信消息失败 toUser=" + toUserId, e);
				}
			}

			
			// 如果微信发送失败 再发送短信
			/*
			if ((msgType & MessageType.SMS) > 1) {
				if (!wxSuccess && StringUtils.isNoneEmpty(u.getPhone())) {
					SmsMessage smsMessage = new SmsMessage();
					smsMessage.setContent(msg);
					smsMessage.setMobile(u.getPhone());

					smsService.send(smsMessage);
				}
			} */
		}
	}
}
