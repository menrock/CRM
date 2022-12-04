package com.niu.crm.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.niu.crm.model.FlScore;

public interface FlScoreMapper {
	int insert(FlScore flScore);	

	int update(FlScore flScore);
	
	int delete(@Param("id") Long id);
	
	int deleteByStuId(@Param("stuId") Long stuId);
	
	List<FlScore> selectByStuId(@Param("stuId") Long stuId);
}
