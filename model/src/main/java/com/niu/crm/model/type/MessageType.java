package com.niu.crm.model.type;

public class MessageType {
	public static final int SYS =0x1;
	public static final int SMS =0x2;
	public static final int WX  =0x4;
	
	
	public static String getName(int msgType){
		if(msgType == SYS)
			return "系统消息";
		else if(msgType == SMS)
			return "短信";
		else if(msgType == WX)
			return "微信";
		else
			return "unknown";
	}
}