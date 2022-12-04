package com.niu.crm.util;

import java.net.Inet4Address;
import java.util.Calendar;

public class UniqueNoUtils {
	public enum UniqueNoType {
		P,//支付请求号
	}
	private static Calendar cal = Calendar.getInstance();
	private static int seq = 0;
	private static final int ROTATION = 99;
	private static int ipMix = 0;
	

	/**
	 * 订单号生成规则：时间戳（精确到秒）+最后一段的IP地址+序列号
	 * @return 唯一订单号
	 */
	public static synchronized String next(UniqueNoType type) {
		if (seq > ROTATION){
			seq = 0;
		}
		if (ipMix == 0) {
			try {
				String ipAddress = Inet4Address.getLocalHost().getHostAddress();
				String[] ipAddresses = ipAddress.split("\\.");
				ipMix = Integer.parseInt(ipAddresses[3]);
			} catch (Exception e) {
				ipMix = 1;
			}
		}
		cal.setTimeInMillis(System.currentTimeMillis());
		return type+String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%2$03d%3$03d", cal, ipMix, seq++).substring(2);
	}
	
	public static synchronized String next() {
		if (seq > ROTATION){
			seq = 0;
		}
		if (ipMix == 0) {
			try {
				String ipAddress = Inet4Address.getLocalHost().getHostAddress();
				String[] ipAddresses = ipAddress.split("\\.");
				ipMix = Integer.parseInt(ipAddresses[3]);
			} catch (Exception e) {
				ipMix = 1;
			}
		}
		cal.setTimeInMillis(System.currentTimeMillis());
		return String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%2$03d%3$03d", cal, ipMix, seq++);
	}
}
