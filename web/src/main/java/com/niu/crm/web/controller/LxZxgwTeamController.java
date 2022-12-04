package com.niu.crm.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.niu.crm.core.ResponseObject;
import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.model.CoopOrg;
import com.niu.crm.model.User;
import com.niu.crm.model.ZxgwTeam;
import com.niu.crm.service.LxZxgwTeamService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.ZxgwTeamVO;

@Controller
@RequestMapping(value = "/lx/zxgwTeam")
public class LxZxgwTeamController extends BaseController{
	@Autowired
	private LxZxgwTeamService zxgwTeamService;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/list.do")
    public String list( Boolean singlePage,ModelMap model){
		if(singlePage == null || singlePage == false)
			model.put("singlePage", Boolean.FALSE);
		else
			model.put("singlePage", Boolean.TRUE);
		
		return "lx/zxgwTeam/list";
	}
	
	@RequestMapping(value = "/listData")
    @ResponseBody
    public Map<String, Object> listData(String keywords){

		Map<String,Object> map = new java.util.HashMap<String, Object>();
		
		List<ZxgwTeamVO> voList = new ArrayList<>();
		List<User> ls = zxgwTeamService.listTeamLeader();
		for(User u:ls){
			List<ZxgwTeam> lsMember = null;
			ZxgwTeamVO vo = new ZxgwTeamVO();
			vo.setLeaderId(u.getId());
			vo.setLeaderName( u.getName() );
			lsMember = zxgwTeamService.loadByLeaderId(vo.getLeaderId());
			StringBuffer buf = new StringBuffer();
			for(int i=0; lsMember !=null && i < lsMember.size(); i++){
				ZxgwTeam zxgw = lsMember.get(i);
				User member = userService.load(zxgw.getZxgwId());
				if(i>0)
					buf.append(",");
				buf.append(member.getName() + "(" + member.getAccount() + ")");
			}
			vo.setMemeberNames(buf.toString());
			voList.add(vo);
		}
		
		map.put("total", voList.size() );
		map.put("rows", voList);
		return map;
	}
	
	@RequestMapping(value = "/teamInfo")
    @ResponseBody
    public ResponseObject<ZxgwTeamVO> teamInfo(Long leaderId){
		ZxgwTeamVO vo = new ZxgwTeamVO();
		
		List<ZxgwTeam> lsMember = null;
		String leaderName = userService.getUserName(leaderId);
		lsMember = zxgwTeamService.loadByLeaderId(leaderId);
		for(int i=0; lsMember !=null && i < lsMember.size(); i++){
			ZxgwTeam zxgwTeam = lsMember.get(i);
			zxgwTeam.setZxgwName( userService.getUserName(zxgwTeam.getZxgwId()) );
		}
		vo.setMembers(lsMember);
		vo.setLeaderId(leaderId);
		vo.setLeaderName(leaderName);
		
		return new ResponseObject<>(vo);
	}
	
	@RequestMapping(value = "/save")
    @ResponseBody
    public ResponseObject<ZxgwTeamVO> save(ZxgwTeamVO vo){
		User user = this.getCurrentUser();
		Long leaderId = vo.getLeaderId();
		List<ZxgwTeam> members = new ArrayList<>();
		
		for(int i=0; vo.getMemberIds() !=null && i < vo.getMemberIds().size(); i++){
			ZxgwTeam zxgwTeam = new ZxgwTeam();
			members.add(zxgwTeam);
			Long zxgwId = vo.getMemberIds().get(i);
			
			zxgwTeam.setCreatorId(user.getId());
			zxgwTeam.setLeaderId(leaderId);
			zxgwTeam.setZxgwId(zxgwId);
			
			ZxgwTeam old = zxgwTeamService.loadByZxgwId(zxgwId);
			if(old !=null && !old.getLeaderId().equals(leaderId) ){
				String sName = userService.getUserName(old.getLeaderId());
				String zxgwName = userService.getUserName(zxgwId);
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, zxgwName + "已经在[" + sName + "]组");
			}
		}
		
		zxgwTeamService.deleteByLeaderId(leaderId);
		for(ZxgwTeam member:members){
			zxgwTeamService.add(member);
		}
		
		return new ResponseObject<>(vo);
	}
}
