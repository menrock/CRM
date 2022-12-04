package com.niu.crm.util;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;


public class CsvUtil {
	
	public static List<List<String>> parse(File file, String charset) throws IOException,Exception {  
		java.io.InputStream is = new java.io.FileInputStream(file);
		InputStreamReader reader=new InputStreamReader(is,"gbk");
		
		List<List<String>> data = null;
		try{
			data = parse(reader);
		}
		catch(IOException ex){
			throw ex;
		}
		finally{
			if(is !=null) is.close(); 
		}
		
		return data;
	}
	
	public static List<List<String> > parse(Reader reader) throws IOException { 
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader).getRecords();
		
        List< List<String> > lsRows = new ArrayList<List<String>>(); 
        
        for (CSVRecord record : records) {
        	java.util.List<String> rowData = new java.util.ArrayList<String>();
        	lsRows.add(rowData);
        	
        	java.util.Iterator<String> iter = record.iterator();
        	//rowData
        	while(iter.hasNext())
        		rowData.add(iter.next());
        }
        
        return lsRows;  
    }  

}
