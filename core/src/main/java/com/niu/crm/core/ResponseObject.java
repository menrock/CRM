package com.niu.crm.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.core.error.BizException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseObject<T> {

	private GlobalErrorCode status = GlobalErrorCode.SUCESS;
	private String moreInfo;
	private T data;

	/**
	 * 正常返回，有数据
	 * 
	 * @param data
	 */
	public ResponseObject(T data) {
		this.data = data;
	}

	/**
	 * 正常返回，无数据
	 */
	public ResponseObject() {
	}

	/**
	 * 错误状态返回
	 * 
	 * @param status
	 */
	public ResponseObject(GlobalErrorCode status) {
		this.status = status;
	}

	/**
	 * 错误状态返回
	 * 
	 * @param moreInfo
	 * @param status
	 */
	public ResponseObject(String moreInfo, GlobalErrorCode status) {
		this.moreInfo = moreInfo;
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getErrorCode() {
		return status.getCode();
	}

	public String getError() {
		return status.getError();
	}

	public String getMoreInfo() {
		return moreInfo;
	}
}
