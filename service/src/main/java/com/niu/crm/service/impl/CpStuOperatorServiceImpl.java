package com.niu.crm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.dao.mapper.CpStuOperatorMapper;
import com.niu.crm.dao.mapper.CpStuZxgwMapper;
import com.niu.crm.model.CpCustomer;
import com.niu.crm.model.StuOperator;
import com.niu.crm.model.StuZxgw;
import com.niu.crm.model.User;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.CpCustomerService;
import com.niu.crm.service.CpStuOperatorService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.vo.UserFuncVO;

@Service
public class CpStuOperatorServiceImpl extends BaseService implements
		CpStuOperatorService {
	@Autowired
	private CpStuOperatorMapper operatorMapper;
	@Autowired
	private CpStuZxgwMapper stuZxgwMapper;

	@Autowired
	private DictService dictService;
	@Autowired
	private CpCustomerService cpCustomerService;
	@Autowired
	private UserFuncService userFuncService;

	@Transactional
	@Override
	public void fix(Long stuId) {
		long now = System.currentTimeMillis();
		long one_day = 24 * 60 * 60 * 1000L;
		CpCustomer stu = cpCustomerService.load(stuId);

		List<StuZxgw> zxgwList = new ArrayList<StuZxgw>();
		// 剔除无效的
		{
			List<StuZxgw> ls = stuZxgwMapper.selectByStuId(stuId);
			for (StuZxgw zxgw : ls) {
				if (zxgw.getStuLevel() != null) {
					String levelCode = dictService.load(zxgw.getStuLevel())
							.getDictCode();
					if (levelCode.endsWith(".invalid")
							|| levelCode.endsWith(".discard")) {
						continue;
					}
				}
				zxgwList.add(zxgw);
			}
		}

		operatorMapper.deleteByStuId(stuId);

		if (stu.getCreatorId() != null) {
			// 查询自己录入的
			boolean hasFunc = userFuncService.hasFunc(
					stu.getCreatorId(), "queryselfcreated");
			if (hasFunc
					|| stu.getCreatedAt().getTime() > now - one_day * 10) {
				StuOperator stuOperator = new StuOperator();
				stuOperator.setStuId(stuId);
				stuOperator.setOperatorId(stu.getCreatorId());
				stuOperator.setAcl(StuOperator.ACL_CREATOR);
				addOperator(stuOperator);
			}
		}
		if (stu.getOwnerId() != null) {
			StuOperator stuOperator = new StuOperator();
			stuOperator.setStuId(stuId);
			stuOperator.setOperatorId(stu.getOwnerId());
			stuOperator.setAcl(StuOperator.ACL_OWNER);
			addOperator(stuOperator);
		}
		for (StuZxgw zxgw : zxgwList) {
			StuOperator stuOperator = new StuOperator();
			stuOperator.setStuId(stuId);
			stuOperator.setAcl(StuOperator.ACL_ZXGW);
			stuOperator.setIsZxgw(true);
			stuOperator.setOperatorId(zxgw.getZxgwId());
			stuOperator.setAssignDate(zxgw.getAssignDate());
			stuOperator.setStuLevel(zxgw.getStuLevel());
			addOperator(stuOperator);
		}
	}

	@Override
	public StuOperator load(Long id) {
		return operatorMapper.selectByPrimaryKey(id);
	}

	@Override
	public StuOperator loadByStuIdAndOperator(Long stuId, Long operator) {
		return operatorMapper.selectByStuIdAndOperator(stuId, operator);
	}

	@Override
	public List<StuOperator> loadByStuId(Long stuId) {
		return operatorMapper.selectByStuId(stuId);
	}

	@Override
	public void insert(StuOperator stuOperator) {
		operatorMapper.insert(stuOperator);
	}

	@Override
	public void update(StuOperator stuOperator) {
		operatorMapper.update(stuOperator);
	}

	@Override
	public void addOperator(StuOperator stuOperator) {
		if (stuOperator.getIsZxgw() == null)
			stuOperator.setIsZxgw(Boolean.FALSE);
		StuOperator oldOperator = loadByStuIdAndOperator(
				stuOperator.getStuId(), stuOperator.getOperatorId());
		if (oldOperator == null) {
			this.insert(stuOperator);
		} else {
			Long oldAcl = oldOperator.getAcl();
			Long acl = stuOperator.getAcl();
			acl = Long.valueOf(oldAcl.longValue() | acl.longValue());
			oldOperator.setAcl(acl);
			if (stuOperator.getIsZxgw()) {
				oldOperator.setIsZxgw(true);
				oldOperator.setAssignDate(stuOperator.getAssignDate());
				oldOperator.setStuLevel(stuOperator.getStuLevel());
			}

			operatorMapper.update(oldOperator);
		}
	}

	@Transactional
	@Override
	public void deleteByStuId(Long stuId) {
		operatorMapper.deleteByStuId(stuId);
	}

	@Transactional
	@Override
	public void deleteInvalid() {
		operatorMapper.deleteInvalid();
	}

}
