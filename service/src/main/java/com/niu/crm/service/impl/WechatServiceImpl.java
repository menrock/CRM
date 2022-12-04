package com.niu.crm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.dao.mapper.UnitMapper;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserService;
import com.niu.crm.service.WechatService;
import com.niu.crm.util.WechatUtil;

@Service
public class WechatServiceImpl extends BaseService implements WechatService {
	@Autowired
	private UserService userService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private UnitMapper unitMapper;
	
	@Override
	public JSONObject listDepartment(Long unitId, Long wxUnitId){
		if(unitId !=null)
			wxUnitId = unitService.load(unitId).getWxUnitId();
		WechatUtil wxUtil = WechatUtil.getInstance();
		String szUrl = "https://qyapi.weixin.qq.com/cgi-bin/department/list?";
		szUrl += "access_token=" + wxUtil.getAccess_token() + "&id=" + wxUnitId;
			
		return WechatUtil.httpRequest(szUrl, "GET", null);
	}

	@Override
	public JSONObject listUser(Long unitId, Boolean fetchChild, Integer status){
		Unit unit = unitService.load(unitId);
		WechatUtil wxUtil = WechatUtil.getInstance();
		
		String szUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?";
		szUrl += "access_token=" + wxUtil.getAccess_token() + "&department_id=" + unit.getWxUnitId();
		if(null != fetchChild)
			szUrl +="&fetch_child=" + (fetchChild.booleanValue()?"1":"0");
		if( null != status)
			szUrl +="&status=" + status;
		
		return WechatUtil.httpRequest(szUrl, "GET", null);
	}

	@Override
	public boolean createUnit(Unit unit){
		if( isDevProfile() )
			return false;
		
		Long wx_parentid = null;  //上级部门id
		Unit pUnit = null;
		if(unit.getParentId() !=null){
			pUnit = unitService.load(unit.getParentId());
			wx_parentid = pUnit.getWxUnitId();
			if(null == wx_parentid){
				createUnit(pUnit);
				wx_parentid = pUnit.getWxUnitId();
			}
		}	
		WechatUtil wxUtil = WechatUtil.getInstance();
		String szUrl = "https://qyapi.weixin.qq.com/cgi-bin/department/create?";
		szUrl += "access_token=" + wxUtil.getAccess_token();
			
		JSONObject json = new JSONObject();
		if(wx_parentid !=null)
			json.put("parentid", wx_parentid);
		json.put("name",  unit.getName() );
		json.put("order", unit.getShowIndex());
		json = WechatUtil.httpRequest(szUrl, "post", json.toJSONString() );
		if(json.getIntValue("errcode") ==0 ){
			Long wxUnitId = json.getLong("id");
			unit.setWxUnitId(wxUnitId);
			unitMapper.updateWxUnitId(unit);
			
			return true;
		}
		else{
			getLogger().error("创建部门失败" + json.toJSONString() );
			return false;
		}
	}
	
	@Override
	public void deleteUnit(Unit unit){
		if( isDevProfile() )
			return;
	}
	
	@Override
	public void updateUnit(Unit unit){
		if( isDevProfile() )
			return;
		
		if( unit.getWxUnitId() == null){
			this.createUnit(unit);
			return;
		}
			
		
		WechatUtil wxUtil = WechatUtil.getInstance();
		String szUrl = "https://qyapi.weixin.qq.com/cgi-bin/department/update?";
		szUrl += "access_token=" + wxUtil.getAccess_token();
			
		JSONObject json = new JSONObject();
		
		Long wx_parentid = null;  //上级部门id
		Unit pUnit = null;
		if(unit.getParentId() !=null){
			pUnit = unitService.load(unit.getParentId());
			wx_parentid = pUnit.getWxUnitId();
			if(null == wx_parentid){
				createUnit(pUnit);
				wx_parentid = pUnit.getWxUnitId();
			}
		}	
		if(wx_parentid !=null)
			json.put("parentid", wx_parentid);
		json.put("id", unit.getWxUnitId());
		json.put("name",  unit.getName() );
		json.put("order", unit.getShowIndex());
		
		
		json = WechatUtil.httpRequest(szUrl, "post", json.toJSONString() );
		if(json.getIntValue("errcode") ==0 ){
		}
		else{
			getLogger().error("创建部门失败" + json.toJSONString() );
		}
	}

	@Override
	public JSONObject getUser(Long userId)
			throws Exception {
		WechatUtil wxUtil = WechatUtil.getInstance();
		String szUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/get?";
		szUrl += "access_token=" + wxUtil.getAccess_token() + "&userid="
				+ userId;
		return WechatUtil.httpRequest(szUrl, "get", null);
	}

	@Override
	/**
	 * 
	 */
	public JSONObject createUser(Long userId)
			throws Exception {
		if(userId !=null){
			return createUser( userService.load(userId) );
		}
		else{
			return null;
		}
	}
	
	private JSONObject createUser(User user)
			throws Exception {
		if (user == null)
			return null;
		
		String account = user.getAccount().toLowerCase();
		
		if(account.equals("sys") || account.equals("system"))
			return null;		

		Unit unit = unitService.load(user.getUnitId());

		Long wxUnitId = unit.getWxUnitId();
		if ( null == wxUnitId){
			createUnit(unit);
			wxUnitId = unit.getWxUnitId();
		}	
		if ( null == wxUnitId)
			return null;

		JSONObject data = new JSONObject();
		data.put("userid", user.getId());
		data.put("name", user.getName());
		data.put("department", wxUnitId);
		data.put("position", user.getPosition());
		data.put("mobile", user.getPhone() );
		if ( "F".equalsIgnoreCase(user.getGender()))
			data.put("gender", "1");

		data.put("email", user.getEmail());
		if( StringUtils.isEmpty(user.getWeixinId()))
			data.put("weixinid", "");
		else
			data.put("weixinid", user.getWeixinId());
		data.put("enable", user.isEnabled() ? 1 :0);

		JSONObject json = updateUser(data);

		// 60111 用户不存在
		if (json.getIntValue("errcode") == 60111)
			if( user.isEnabled() ){
				data.remove("enable");
				json = createUser(data);
			}
		if (json.getIntValue("errcode") == 0) {
			// 更新成功
		} else {
			getLogger().info("user=" + user.getId() + "," + user.getAccount() + " json=" + json.toString());
		}
		return json;
	}

	@Override
	public JSONObject deleteUser(Long userId)
			throws Exception {
		WechatUtil wxUtil = WechatUtil.getInstance();
		String szUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?";
		szUrl += "access_token=" + wxUtil.getAccess_token() + "&userid="
				+ userId;
		return WechatUtil.httpRequest(szUrl, "get", null);
	}
	
	@Override
	public JSONObject updateUser(User user)
			throws Exception {
		return null;
	}

	private JSONObject createUser(JSONObject data)
			throws Exception {
		WechatUtil wxUtil = WechatUtil.getInstance();
		String szUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/create?";
		szUrl += "access_token=" + wxUtil.getAccess_token();
		return WechatUtil.httpRequest(szUrl, "post", data.toString());
	}

	private JSONObject updateUser(JSONObject data)
			throws Exception {
		WechatUtil wxUtil = WechatUtil.getInstance();
		JSONObject result = null;
		String szUrl = null;
		
		for (int i = 0; i < 2; i++) {
			szUrl = "https://qyapi.weixin.qq.com/cgi-bin/user/update?";
			szUrl += "access_token=" + wxUtil.getAccess_token();
			result = WechatUtil.httpRequest(szUrl, "post", data.toString());
			
			//invalid access_token
			if(result.getIntValue("errcode") !=40014)
				break;
			else
				wxUtil.createAccess_token();
		}
		return result;
	}
	
	@Override
	public JSONObject sendTxtMessage(String msg, Long[] toUnitId, Long[] toUserId) throws Exception{
		WechatUtil wxUtil = WechatUtil.getInstance();
				
		JSONObject result = null;
		for(int i=0; i <2; i++){
			String accessToken = wxUtil.getAccess_token();
			result = sendTxtMessage(accessToken,msg, toUnitId, toUserId);
			int errcode = result.getIntValue("errcode");
			//invalid access_token、access_token expired
			if( errcode !=40014 && errcode !=42001)
				break;
				
			//重新生成access_token
			wxUtil.createAccess_token();
		}
		return result;
	}
	
	public JSONObject sendTxtMessage(String accessToken, String msg, Long[] toUnitId, Long[] toUserId) throws Exception{
		if (isDevProfile() ) {
			//开发及测试环境 只允许发信息给个别用户			
			toUnitId = null;			
			List<Long> lsUserId = new ArrayList<Long>();
			for (int i = 0; toUserId !=null && i < toUserId.length; i++) {
				if(this.isCanTestUser(toUserId[i]))
					lsUserId.add(toUserId[i]);
			}
			toUserId = new Long[lsUserId.size()];
			lsUserId.toArray(toUserId);
		}
		
		JSONObject json = new JSONObject();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; toUserId !=null && i < toUserId.length; i++) {
			if (i > 0)
				buf.append("|");
			buf.append(toUserId[i]);
		}
		json.put("touser", buf.toString());
		
		buf = new StringBuffer();
		for (int i = 0; toUnitId != null && i < toUnitId.length; i++) {
			Unit unit = unitService.load(toUnitId[i]);
			if (i > 0)
				buf.append("|");
			buf.append(unit.getWxUnitId());
		}
		
		json.put("toparty", buf.toString());
		json.put("totag", "");
		json.put("msgtype", "text");
		json.put("agentid", "0");
		json.put("safe", "0");

		JSONObject obj = new JSONObject();
		obj.put("content", msg);
		json.put("text", obj);

		String msgTxt = json.toString();
		String szUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="
				+ accessToken;

		return WechatUtil.httpRequest(szUrl, "POST", msgTxt);
	}
}
