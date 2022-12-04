package com.niu.crm.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import com.niu.crm.dao.mapper.SmsMessageMapper;
import com.niu.crm.dao.mapper.UserMapper;
import com.niu.crm.form.SmsSearchForm;
import com.niu.crm.form.SysMessageSearchForm;
import com.niu.crm.model.SmsMessage;
import com.niu.crm.model.SysMessage;
import com.niu.crm.model.User;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.SmsService;
import com.niu.crm.service.UserService;

@Service
public class SmsServiceImpl extends BaseService implements SmsService {
	@Autowired
	private SmsMessageMapper smsMessageMapper;
	
	// 接口帐号(1001@501042340002)
	private  final String account = "1001@501042340002";
	
	// 接口密钥 (5A949FE6877CE94F49120AAE85D5C409)
	private  final String authkey = "5A949FE6877CE94F49120AAE85D5C409";
	
	// 通道组编号
	private  final int cgid = 52;
	
	// 默认使用的签名编号(未指定签名编号时传此值到服务器)
	private  final int csid = 7281;
	
	@Autowired
	private UserService userService;

	@Override
	public String querySent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryCapacity() {
        String  szBalance = null;
		
		String szUrl = "http://smsapi.c123.cn/OpenPlatform/OpenApi?action=getBalance";
		String param = "&ac=" + account + "&authkey=" + authkey ;
		
		try{
			String result = httpPost(szUrl, param);
			Document document = DocumentHelper.parseText(result);
			Element root = document.getRootElement();
			if(root.attributeValue("result").equals("1")){
				Element item = root.element("Item");
				szBalance = item.attributeValue("remain");
			}
		}
		catch(Exception e){
			getLogger().error("", e);
		}
		
		return szBalance;
	}
	
	@Override
	public String send2Unit(String content, Long toUnitId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String send2User(String content, Long toUserId) {
		User toUser = userService.load(toUserId);
		
		SmsMessage message = new SmsMessage();
		message.setContent(content);
		message.setMobile(toUser.getPhone());
		
		return send(message);
	}

	@Override
	public String send(SmsMessage message) {
        String result = "";
        
        smsMessageMapper.insert(message);
        
		String szUrl = "http://smsapi.c123.cn/OpenPlatform/OpenApi";
		String param = "action=sendOnce&ac=" + account + "&authkey=" + authkey + "&cgid=" + cgid + "&csid=" + csid;
		
		try{
			param += "&m=" + URLEncoder.encode(message.getMobile(),"utf-8")
				+"&c=" + URLEncoder.encode(message.getContent(),"utf-8");
			
			result = httpPost(szUrl, param);
		}
		catch(Exception e){
			getLogger().error("", e);
		}
		
		return result;
	}
	
	
	private String httpPost(String szUrl, String param) {
		PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        
		
		try{
			java.net.URL url = new java.net.URL(szUrl);
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
		}
		catch(Exception e){
			this.getLogger().error("", e);
		}
		finally{
            if(out!=null) try{out.close();}catch(Exception e){}
            if(in!=null) try{in.close();}catch(Exception e){}
        }
		
        return result;
	}

	@Override
	public List<SmsMessage> queryMessage(SmsSearchForm form, Pageable pageable) {
		List<SmsMessage> ls = smsMessageMapper.queryMessage(form, pageable);

		for(SmsMessage msg:ls){
		}
		return ls;
	}

	@Override
	public Integer countMessage(SmsSearchForm form) {
		return smsMessageMapper.countMessage(form);
	}
	

}
