package com.niu.crm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niu.crm.service.BaseService;
import com.niu.crm.service.AreaService;
import com.niu.crm.core.error.GlobalErrorCode;
import com.niu.crm.core.error.BizException;
import com.niu.crm.dao.mapper.AreaMapper;
import com.niu.crm.model.Area;

@Service
public class AreaServiceImpl extends BaseService implements AreaService {
	private Map<Long, Area> cacheById = null;

	@Autowired
	private AreaMapper areaMapper;

	@Override
	public void refrechCache() {
		cacheById = null;
	}

	private void refreshCache(Long id) {
		if (cacheById == null) {
			cacheById = new HashMap<Long, Area>();
		}

		Area area = areaMapper.selectById(id);

		if (area == null) {
			getLogger().error("area not found id=" + id );
		} else {
			cacheById.put(area.getId(), area);
		}
	}

	@Override
	public Area load(Long id) {
		Area unit = null;
		if (null != cacheById) {
			unit = cacheById.get(id);
		}

		if (unit == null) {
			refreshCache(id);
			if (null != id)
				unit = cacheById.get(id);
		}

		return unit;
	}


	@Override
	public List<Area> loadChildren(Long parentId) {
		return areaMapper.selectChildren(parentId);
	}
}
