package com.niu.crm.model;

import com.niu.crm.model.type.BizType;


/**
 * 客户内部推荐
 * @author 天雨
 *
 */
public class CustomerRecommend extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8876475284427273825L;
	
	private Long cstmId;
	private BizType fromBiz;
	private BizType toBiz;
	private Long fromCompanyId;
	private Long toCompanyId;
	private Long fromGwId;
	private Long toGwId;
	private Long fromBizCstmId;
	private Long toBizCstmId;
	
	public Long getCstmId() {
		return cstmId;
	}
	public void setCstmId(Long cstmId) {
		this.cstmId = cstmId;
	}
	
	public BizType getFromBiz() {
		return fromBiz;
	}
	public void setFromBiz(BizType fromBiz) {
		this.fromBiz = fromBiz;
	}
	
	public BizType getToBiz() {
		return toBiz;
	}
	public void setToBiz(BizType toBiz) {
		this.toBiz = toBiz;
	}
	
	public Long getFromCompanyId() {
		return fromCompanyId;
	}
	public void setFromCompanyId(Long fromCompanyId) {
		this.fromCompanyId = fromCompanyId;
	}
	
	public Long getToCompanyId() {
		return toCompanyId;
	}
	public void setToCompanyId(Long toCompanyId) {
		this.toCompanyId = toCompanyId;
	}
	
	public Long getFromGwId() {
		return fromGwId;
	}
	public void setFromGwId(Long fromGwId) {
		this.fromGwId = fromGwId;
	}
	
	public Long getToGwId() {
		return toGwId;
	}
	public void setToGwId(Long toGwId) {
		this.toGwId = toGwId;
	}
	
	public Long getFromBizCstmId() {
		return fromBizCstmId;
	}
	public void setFromBizCstmId(Long fromBizCstmId) {
		this.fromBizCstmId = fromBizCstmId;
	}
	
	public Long getToBizCstmId() {
		return toBizCstmId;
	}
	public void setToBizCstmId(Long toBizCstmId) {
		this.toBizCstmId = toBizCstmId;
	}
}
