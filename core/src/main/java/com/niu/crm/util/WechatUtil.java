package com.niu.crm.util;

import java.io.InputStreamReader;
import java.net.ConnectException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class WechatUtil {

	static Logger logger = LoggerFactory.getLogger(WechatUtil.class);
	
	private static WechatUtil util = null;

	/*
	private String corpId = "wxdd5624bd15b1691a";
	private String corpSecret = "ANH8wXZRgmN5qTrRMtn-aVqNuX2V1t4dzJ27mp0M7e22xEyI6MVg3b4vpX8KS9QA";
	*/

	private String corpId = "wx5aca76ef8920957d";
	private String corpSecret = "_z80thXeGIKaWxab4LuQnUn4OtbNsjpQD9RSIUdjftLuRrMnP-Im6qsZJo_5cAK7";
	

	private String access_token = null;
	private long expire_time = 0; // access_token 失效时间

	/**
	 * 取默认 AgentId
	 * 
	 * @return
	 */
	public static String agentId() {
		return "0";
	}


	public static WechatUtil getInstance() {
		if (util == null) {			
			util = new WechatUtil();

			try {
				util.createAccess_token();
			} catch (Throwable e) {
				logger.error("", e);
			}
		}
		return util;
	}

	public String getCorpSecret() throws Exception {
		return this.corpSecret;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void createAccess_token() throws Exception {
		String szUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?";
		szUrl += "corpid=" + corpId + "&corpsecret=" + corpSecret;

		String txt = getFileContent(szUrl);
		JSONObject json = JSON.parseObject(txt);
		if (json.getInteger("errcode") !=0)
			return;

		this.access_token = json.getString("access_token");
		{
			int t = json.getIntValue("expires_in"); // 有效时间 单位 秒

			this.expire_time = System.currentTimeMillis() + t * 1000L;
		}
	}

	public static String getFileContent(String szUrl)
			throws java.io.IOException {
		java.net.URL urlObj = new java.net.URL(szUrl);
		java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
		java.io.InputStream is = urlObj.openStream();

		byte buf[] = new byte[1024];
		int len = is.read(buf);
		while (len >= 0) {
			bos.write(buf, 0, len);
			len = is.read(buf);
		}
		return new String(bos.toByteArray(), "utf-8");
	}
	
	public static JSONObject httpRequest(String reqUrl, String reqMethod,
			String outStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		reqMethod = reqMethod.toUpperCase().trim();

		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			java.net.URL url = new java.net.URL(reqUrl);
			HttpsURLConnection httpConn = (HttpsURLConnection) url
					.openConnection();
			httpConn.setSSLSocketFactory(ssf);

			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpConn.setRequestMethod(reqMethod);

			if ("GET".equalsIgnoreCase(reqMethod))
				httpConn.connect();

			// 当有数据需要提交时
			if (outStr != null) {
				java.io.OutputStream os = httpConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				os.write(outStr.getBytes("UTF-8"));
				os.close();
			}

			// 将返回的输入流转换成字符串
			java.io.InputStream is = httpConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(is,
					"utf-8");
			java.io.BufferedReader bufferedReader = new java.io.BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();

			// 释放资源
			is.close();
			is = null;
			httpConn.disconnect();
			jsonObject = JSON.parseObject(buffer.toString());
		} catch (ConnectException ex) {
			logger.error("", ex);
		} catch (Exception ex) {
			logger.error("", ex);
		}
		return jsonObject;
	}
}
