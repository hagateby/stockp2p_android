package com.stockp2p.common.serviceengin;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 閲嶆柊瀹氫箟DES鍔犲瘑锛岀粺涓�ndroid銆乮os銆乯ava骞冲彴鍔犲瘑瑙ｅ瘑
 * <p>
 * Title: Des3.java
 * </p>
 * 
 * @author wang_xiaofeng
 * @date 2014-6-6 涓婂崍10:39:12
 */
public class Des3 {

	/**
	 * 瀵嗛挜
	 */
	private final static String defaultKey = "agnbemgeiandeai!feian45#w3";
	private final static String KeyVer = "default";
	/**
	 * 鍚戦噺
	 */
	private final static String iv = "01234567";
	/**
	 * 鍔犺В瀵嗙粺涓�娇鐢ㄧ殑缂栫爜鏂瑰紡
	 */
	private final static String encoding = "UTF-8";

	/**
	 * 3DES鍔犲瘑
	 * 
	 * @param plainText
	 *            鏅�鏂囨湰
	 * @return
	 * @throws Exception
	 */
	public static String encode(String plainText) throws Exception {
		return encode(plainText,defaultKey);
	}
	/**
	 * 鍔犲瘑
	 * @param plainText
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encode(String plainText,String key) throws Exception {
//		System.out.println("encode key:"+key);
//		System.out.println("encode key:"+plainText);
		if("".equals(key)){
			key = defaultKey;
		}
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
		return Base64.encode(encryptData);
	}

	/**
	 * 3DES瑙ｅ瘑
	 * 
	 * @param encryptText
	 *            鍔犲瘑鏂囨湰
	 * @return
	 * @throws Exception
	 */
	public static String decode(String encryptText) throws Exception {
		return decode(encryptText,defaultKey);
	}
	
	/**
	 * 瑙ｅ瘑
	 * @param encryptText
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decode(String encryptText,String key) throws Exception {
//		System.out.println("decode key:"+key);
//		System.out.println("decode key:"+encryptText);
		if("".equals(key)){
			key = defaultKey;
		}
		Key deskey = null;
		DESedeKeySpec spec = new DESedeKeySpec(key.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
		byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));
		return new String(decryptData, encoding);
	}

	public static void main(String[] args) throws Exception {
		Des3 des3 = new Des3();
		String encode = des3.encode("123456");
		System.out.println("鍔犲瘑------------->" + encode);
		String decode = des3.decode("RlK6fSFWQt0rOu06V8xv50e1uNjH81HoFpltN41cupcNnKzYcuHXxnpe6Kdo Iv+zr3wkXn9I9Q7sCCpH3VzTpGFCy39Li2jNSB+4pYXF+a4=");
		System.out.println("瑙ｅ瘑------------->" + decode);
	}
}
