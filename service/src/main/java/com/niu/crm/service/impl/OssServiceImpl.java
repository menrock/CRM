package com.niu.crm.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.niu.crm.service.BaseService;

@Service
public class OssServiceImpl extends BaseService{

	@Value("${oss.bucketName}")
	private String bucketName = "meishi2017-public-test";
	
	@Value("${oss.endpoint}")
	private String endpoint;
	
	@Value("${oss.accessKeyId}")
	private String accessKeyId = "LTAIaRaEsRWul8FM";
	
	@Value("${oss.accessKeySecret}")
	private String accessKeySecret = "KRqDCuidDsYmtRA5Jzzygm2ueGSppd";
	
	
	public String putObject(MultipartFile file) throws java.io.IOException { 
        String key = null;
        
        com.aliyun.oss.OSSClient client = new com.aliyun.oss.OSSClient(endpoint, accessKeyId, accessKeySecret);
        
        try{
        	// 获取指定文件的输入流
        	String fileName = file.getOriginalFilename();
        	String fileExt = getFileExt(fileName);
        	
        	key = UUID.randomUUID().toString() + fileExt;
            
     
            // 创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            
            if(fileExt.equals(".gif"))
            	metadata.setContentType("image/gif");
            else if( fileExt.equals(".jpg") || fileExt.equals(".jpeg") || fileExt.equals(".jpe") )
            	metadata.setContentType("image/jpeg");
            else if(fileExt.equals(".png"))
            	metadata.setContentType("image/png");
            else
            	metadata.setContentType("application/octet-stream");
                 
            // 必须设置ContentLength
            metadata.setContentLength(file.getSize());
             
            Date expire = new Date(new Date().getTime() + 30 * 1000);
            metadata.setExpirationTime(expire);
    
            // 上传Object.
            PutObjectResult result = client.putObject(bucketName, key, file.getInputStream());
            
            return key;
        }catch(Exception e){
        	throw e;
        }finally{
        	if(client !=null)
        		client.shutdown();
        }
    }
    
    public void download(String key, OutputStream os){
    	com.aliyun.oss.OSSClient client = new com.aliyun.oss.OSSClient(endpoint, accessKeyId, accessKeySecret);
        
        try{
        	OSSObject ossObject = client.getObject(bucketName, key);
        	java.io.InputStream is = ossObject.getObjectContent();
        	byte[] b = new byte[1024];
        	        	
        	while(true){
        		int len = is.read(b);
        		if(len <0)
        			break;
        		os.write(b, 0, len);
        	}
        }catch(Exception e){
        	
        }finally{
        	if(client !=null)
        		client.shutdown();
        }
    }
    
    /*
     * 取文件扩展名
     */
    public String getFileExt(String fileName){
    	if(fileName == null)
    		return null;
    	
    	int idx = fileName.lastIndexOf(".");
    	if(idx >=0)
    		return fileName.substring(idx).toLowerCase().trim();
    	else
    		return "";
    }
}
