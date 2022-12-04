package com.niu.crm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateUtil {
	
	public static Date parseDate(String str) throws ParseException{
		if(StringUtils.isEmpty(str) )
			return null;
		
		SimpleDateFormat sdf = null;
		
		if(str.length() ==10)
			sdf = new SimpleDateFormat("yyyy-MM-dd");	
		else if(str.length() ==16)
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");	
		else
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		
		return sdf.parse(str);
	}
	
	public static String formatDate(Date date, String fmt) throws ParseException{
		if(date==null || StringUtils.isEmpty(fmt) )
			return null;
		
		SimpleDateFormat sdf =new SimpleDateFormat(fmt);		
		
		return sdf.format(date);
	}

}
