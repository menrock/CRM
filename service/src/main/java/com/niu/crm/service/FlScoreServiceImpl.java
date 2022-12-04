package com.niu.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.niu.crm.dao.mapper.FlScoreMapper;
import com.niu.crm.model.FlScore;
import com.niu.crm.model.User;

@Service
public class FlScoreServiceImpl implements FlScoreService {

    @Autowired
    private FlScoreMapper  flScoreMapper;
    
	public int save(Long stuId, List<FlScore> lsScore){
		flScoreMapper.deleteByStuId(stuId);
		if(lsScore == null)
			return 0;
		
		int count =0;
		for(int i=0; i < lsScore.size(); i++){
			FlScore flScore = lsScore.get(i);
			
			flScore.setShowIndex(i+1);
			count += flScoreMapper.insert(flScore);
		}
		return count;
	}
	
	public List<FlScore> loadScore(Long stuId){
		return flScoreMapper.selectByStuId(stuId);
	}
}
