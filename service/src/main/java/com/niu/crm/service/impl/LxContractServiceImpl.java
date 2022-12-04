package com.niu.crm.service.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.core.error.BizException;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.dao.mapper.LxContractCountryMapper;
import com.niu.crm.dao.mapper.CustContractLineMapper;
import com.niu.crm.dao.mapper.CustContractMapper;
import com.niu.crm.dao.mapper.CustContractSkMapper;
import com.niu.crm.dao.mapper.LxContractMapper;
import com.niu.crm.dao.mapper.MaxidMapper;
import com.niu.crm.dto.LxContractDTO;
import com.niu.crm.form.LxContractSearchForm;
import com.niu.crm.model.Alarm;
import com.niu.crm.model.Country;
import com.niu.crm.model.CustContract;
import com.niu.crm.model.LxContractCountry;
import com.niu.crm.model.CustContractLine;
import com.niu.crm.model.Customer;
import com.niu.crm.model.Dict;
import com.niu.crm.model.LxContract;
import com.niu.crm.model.LxCustomer;
import com.niu.crm.model.Maxid;
import com.niu.crm.model.Unit;
import com.niu.crm.model.User;
import com.niu.crm.model.type.BizType;
import com.niu.crm.model.type.CustContractStatus;
import com.niu.crm.model.type.LxContractStatus;
import com.niu.crm.model.type.MessageType;
import com.niu.crm.service.AlarmService;
import com.niu.crm.service.BaseService;
import com.niu.crm.service.CountryService;
import com.niu.crm.service.LxContractService;
import com.niu.crm.service.CustomerService;
import com.niu.crm.service.DictService;
import com.niu.crm.service.LxCustomerService;
import com.niu.crm.service.UnitService;
import com.niu.crm.service.UserFuncService;
import com.niu.crm.service.UserService;
import com.niu.crm.vo.CustContractVO;

@Service
public class LxContractServiceImpl extends BaseService implements LxContractService {
	@Autowired
	private MaxidMapper maxidMapper;
	@Autowired
	private LxContractCountryMapper lxContractCountryMapper;
	@Autowired
	private CustContractMapper custContractMapper;
	@Autowired
	private LxContractMapper contractMapper;
	@Autowired
	private CustContractSkMapper skMapper;
	@Autowired
	private CustContractLineMapper contractLineMapper;
	@Autowired
	private LxCustomerService lxCustomerService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private DictService dictService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserFuncService userFuncService;
	@Autowired
	private AlarmService alarmService;

	@Override
	public LxContract load(Long id) {
		LxContract contract = contractMapper.selectByPrimaryKey(id);
		return contract;
	}
	
	@Override
	public LxContract loadByConNo(String conNo) {
		LxContract contract = contractMapper.selectByConNo(conNo);
		return contract;
	}

	@Override
	public LxContractDTO loadDTO(Long id) {
		LxContract contract = contractMapper.selectByPrimaryKey(id);
		if(contract == null)
			return null;
		
		return loadDTO(contract);
	}
	
	private LxContractDTO loadDTO(LxContract contract){
		Long cstmId = contract.getCstmId();
		
		LxContractDTO dto = new LxContractDTO();
		BeanUtils.copyProperties(contract, dto);
		initDTO(dto);
		
		Customer customer = customerService.load(cstmId);
		dto.setCstmId(customer.getId());
		dto.setCstmName(customer.getName());
					
		List<CustContractLine> feeLines = contractLineMapper.selectByConId( contract.getConId(), BizType.LX );
		dto.setFeeLines(feeLines);
				
		return dto;
	}

	@Override
	public List<LxContract> loadByCstmId(Long id) {
		return contractMapper.selectByCstmId(id);
	}

	@Transactional
	@Override
	public int add(User user, LxContractDTO contract) {
		Long conType = contract.getConType();
		if(conType !=null){
			List<Dict> lsChild = dictService.loadChildren(conType);
			if( !lsChild.isEmpty() )
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"错误的合同类型");
		}
		
		contract.setStatus(LxContractStatus.DRAFT);		
		initCountryNames(contract);
		contract.setCreatorId(user.getId());
		
		CustContract custContract = new CustContract();
		custContract.setCstmId( contract.getCstmId() );
		custContract.setConNo( contract.getConNo() );
		custContract.setSignDate( contract.getSignDate() );
		custContract.setCreatorId(user.getId());
		
		custContractMapper.insert(custContract);
		
		Long conId = custContract.getId();
		contract.setConId( conId );
		int count = contractMapper.insert(contract);

		List<CustContractLine> feeLines = contract.getFeeLines();
		for (int i = 0; feeLines != null && i < feeLines.size(); i++) {
			CustContractLine line = feeLines.get(i);
			
			Dict skItem = dictService.load(line.getItemId());
			List<Dict> lsChild = dictService.loadChildren(skItem.getId());
			if(lsChild.size() >0)
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"费用项目错误  id=" + skItem.getId());
			
			line.setCreatorId(user.getId());
			line.setConId(conId);
			contractLineMapper.insert(line);
		}
		this.saveContractCountry(contract);

		return count;
	}

	@Transactional
	@Override
	public void delete(User user, Long id) {
		LxContract contract = load(id);
		Long conId = contract.getConId();
		
		contractLineMapper.deleteByConId(conId, BizType.LX);
		lxContractCountryMapper.deleteByLxConId(id);
		
		contractMapper.delete(id);
		custContractMapper.delete(conId);
	}
	
	@Transactional
	@Override
	public int update(User user, LxContractDTO contract) {
		Long conType = contract.getConType();
		if(conType !=null){
			List<Dict> lsChild = dictService.loadChildren(conType);
			if( !lsChild.isEmpty() )
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT,"错误的合同类型");
		}

		initCountryNames(contract);
		LxContract oldContract = contractMapper.selectByPrimaryKey(contract.getId());
		contract.setConId(oldContract.getConId());
		if(contract.getStatus() == null)
			contract.setStatus(oldContract.getStatus());
		if(contract.getSignDate() == null)
			contract.setSignDate(oldContract.getSignDate());
		if(contract.getConNo() == null)
			contract.setConNo(oldContract.getConNo());
		
		String conNo = contract.getConNo();
		if(StringUtils.isEmpty(conNo) && contract.getStatus() != LxContractStatus.DRAFT ){
			Long companyId= contract.getCompanyId();
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.setTime(contract.getSignDate());
			int iYear = calendar.get(Calendar.YEAR);
			conNo = this.createContractNo(companyId, iYear);
			contract.setConNo(conNo);
		}
		
		this.saveContractCountry(contract);
		
		
		List<CustContractLine> feeLines = contract.getFeeLines();

		for (int i = 0; feeLines != null && i < feeLines.size(); i++) {
			CustContractLine line = feeLines.get(i);

			Dict skItem = dictService.load(line.getItemId());
			List<Dict> lsChild = dictService.loadChildren(skItem.getId());
			if (lsChild.size() > 0)
				throw new BizException(GlobalErrorCode.INVALID_ARGUMENT, "费用项目错误  id=" + skItem.getId());

			if (line.getId() == null) {
				line.setCreatorId(user.getId());
				line.setConId(contract.getConId());
				contractLineMapper.insert(line);
			} else {
				contractLineMapper.update(line);
			}
		}
		
		int count = contractMapper.update(contract);
		{
			CustContract custContract = new CustContract();
			custContract.setId( contract.getConId() );
			custContract.setConNo( contract.getConNo() );
			custContract.setSignDate( contract.getSignDate() );
			
			custContractMapper.update(custContract);
		}
		
		
		//update 学生的首次签约日期
		if( contract.getStatus() != LxContractStatus.DRAFT)
			lxCustomerService.updateSignDate(contract.getCstmId());

		return count;
	}

	private void initCountryNames(LxContractDTO contract) {
		if(contract == null)
			return;
		
		if (StringUtils.isEmpty(contract.getCountryCodes())) {
			contract.setCountryNames(null);
			return;
		}
		String[] codes = contract.getCountryCodes().split(",");
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < codes.length; i++) {
			if (i > 0)
				buf.append(",");
			Country country = countryService.loadByCode(codes[i]);
			if (country == null)
				buf.append(codes[i]);
			else
				buf.append(country.getName());
		}
		contract.setCountryNames(buf.toString());
	}

	@Override
	public int updateSk(User user, LxContract contract) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LxContractDTO statContract(LxContractSearchForm form) {
		return contractMapper.statContract(form);
	}

	@Override
	public int countContract(LxContractSearchForm form) {
		return contractMapper.countContract(form);
	}

	@Override
	public List<LxContractDTO> queryContract(LxContractSearchForm form, Pageable pageable) {
		List<LxContractDTO> ls = contractMapper.queryContract(form, pageable);
		for(LxContractDTO dto:ls){
			this.initDTO(dto);
		}

		return ls;
	}

	private void initDTO(LxContractDTO dto) {		
		if (dto.getCompanyId() != null) {
			Unit unit = unitService.load(dto.getCompanyId());
			dto.setCompanyName(unit.getName());
		}
		
		if (dto.getConType() != null) {
			Dict dict = dictService.load(dto.getConType());
			if (dict == null)
				dto.setConTypeName("lost-" + dto.getConType());
			else
				dto.setConTypeName(dict.getDictName());
		}
		if (dto.getStuFromId() != null) {
			Dict dict = dictService.load(dto.getStuFromId());
			dto.setStuFromName(dict.getDictName());
		}

		if (dto.getSignGwId() != null)
			dto.setSignGwName(userService.load(dto.getSignGwId()).getName());
		if (dto.getPlanGwId() != null)
			dto.setPlanGwName(userService.load(dto.getPlanGwId()).getName());
		if (dto.getApplyGwId() != null)
			dto.setApplyGwName(userService.load(dto.getApplyGwId()).getName());
		if (dto.getWriteGwId() != null)
			dto.setWriteGwName(userService.load(dto.getWriteGwId()).getName());
		if (dto.getServiceGwId() != null)
			dto.setServiceGwName(userService.load(dto.getServiceGwId()).getName());
	}
	
	private void saveContractCountry(LxContract contract) {
		Long lxConId = contract.getId();
		lxContractCountryMapper.deleteByLxConId(lxConId);
		if (StringUtils.isEmpty(contract.getCountryCodes()))
			return;

		String[] codes = contract.getCountryCodes().split(",");
		for (String szCode : codes) {
			LxContractCountry contractCountry = new LxContractCountry();
			contractCountry.setLxConId(lxConId);
			contractCountry.setCountryCode(szCode);

			lxContractCountryMapper.insert(contractCountry);
		}
	}
	
	private String createContractNo(Long companyId, Integer iYear){
		Unit unit = unitService.load(companyId);
		Maxid maxid = maxidMapper.select("CON_NO", iYear);
		if(maxid == null){
			maxid = new Maxid();
			maxid.setIdCode("CON_NO");
			maxid.setDay(iYear);
			maxid.setMaxid(1L);
			maxidMapper.insert(maxid);
		}else{
			maxid.setMaxid(maxid.getMaxid() +1L);
			maxidMapper.update(maxid);
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("0000");
		String conNo = unit.getAlias().toUpperCase() + iYear.toString() + df.format(maxid.getMaxid());
		if(conNo.endsWith("4"))
			return createContractNo(companyId, iYear);
		else
			return conNo;
		
	}
}
