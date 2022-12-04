package com.niu.crm.web.controller;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.niu.crm.form.StudentSearchForm;
import com.niu.crm.model.User;

public abstract class BaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public User getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof User)
                return (User) principal;
        }

        return null;
    }

    public Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }
    
    @InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));
		
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		binder.registerCustomEditor(Date.class, "contactDate", new CustomDateEditor(df, true));
	}
    
    public Pageable convertPager(HttpServletRequest request, Pageable pageable){
    	if( null == pageable)
    		return null;
    	
	    Sort.Direction dir = Sort.Direction.ASC;
		String szSort  = request.getParameter("sort");
		String szOrder = request.getParameter("order");
		if("desc".equalsIgnoreCase(szOrder))
			dir = Sort.Direction.DESC;
		
		if(StringUtils.isEmpty(szSort))
			return pageable;
		
		pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), dir, szSort);
		
		return pageable;
    }
    
    /**
     * 设置时间为当天最后一个毫秒
     * @param d
     * @return
     */
    public  Date toDateLastMillis(Date d){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(d);
    	
    	Calendar cal2 = Calendar.getInstance();
    	cal2.clear();
    	cal2.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
    	cal2.add(Calendar.DATE, 1);
    	
    	cal.setTimeInMillis( cal2.getTimeInMillis() -1L );
    	
    	return cal.getTime();
    }
}
