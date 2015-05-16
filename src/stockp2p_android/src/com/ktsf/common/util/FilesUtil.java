package com.ktsf.common.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

/**
 * 文件存储数据方式工具类
 * 
 * @author haixu
 */
public class FilesUtil {

	/**
	 * 保存文件内容
	 * 
	 * @param c
	 * @param fileName
	 *            文件名称
	 * @param content
	 *            内容
	 */
	public static void writeFiles(FragmentActivity context, String fileName,
			String content) {
		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			fos.write(content.getBytes());
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 读取文件内容
	 * 
	 * @param c
	 * @param fileName
	 *            文件名称
	 * @return 返回文件内容
	 */
	public static String readFiles(FragmentActivity context, String fileName) {
		String content = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			FileInputStream fis = context.openFileInput(fileName);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			content = baos.toString();
			fis.close();
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
}
