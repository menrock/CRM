package com.niu.crm.service;

import java.util.List;

import com.niu.crm.model.FlScore;
import com.niu.crm.model.User;

public interface FlScoreService {

	int save(Long stuId, List<FlScore> lsScore);
    List<FlScore> loadScore(Long stuId);
}
