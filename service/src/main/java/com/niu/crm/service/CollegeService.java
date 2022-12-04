package com.niu.crm.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.niu.crm.form.CollegeSearchForm;
import com.niu.crm.model.College;
import com.niu.crm.model.User;

public interface CollegeService {
	College load(Long id);
	
	int add(User user, College org);
    
    void delete(User user, Long id);

    int update(User user, College org);
    
    int countCollege(CollegeSearchForm form);
    
    List<College> queryCollege(CollegeSearchForm form, Pageable pager);
    
    int importCollege(User user, HttpServletRequest request, MultipartFile file) throws IOException;
}
