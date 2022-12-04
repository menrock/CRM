package com.niu.crm.model;

import java.math.BigDecimal;
 
public class SmsSendResult {
	private Integer result;
	
	//企业编号(发送短信的企业编号)	
	private String cid;
	//员工编号(发送短信的员工编号)
	private String sid;
	
	//发送编号(每次发送的消息编号)
	private String msgid;
	
	//短信条数(任务需要的短信条数)
	private Integer	total;
	
	//单价(任务中每条短信的价格)
	private BigDecimal price;
	
	//余额(本次发送后企业的帐户余额)
	private BigDecimal remain;

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getRemain() {
		return remain;
	}

	public void setRemain(BigDecimal remain) {
		this.remain = remain;
	}


}
