package com.stockp2p.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	public static String MD5(String val) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
		}
		md5.reset();
		md5.update(val.getBytes());
		byte[] m = md5.digest();// 加密
		String result = toHexString(m, "");
		return result;
	}
	
	private static String toHexString(byte[] bytes, String separator) {
		StringBuilder hexString = new StringBuilder();
		String hexstring ;
		for (byte b : bytes) {
			hexstring = Integer.toHexString(0xFF & b);
			if (hexstring.length()<2) 	hexString.append("0");	//对于转换后长度不足两位的字符串做补"0"处理
			hexString.append(Integer.toHexString(0xFF & b)).append(separator);
			
		}
		return hexString.toString();
	}
}
