package com.stockp2p.common.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;


import com.stockp2p.R;
import com.stockp2p.common.data.Constants;
import com.stockp2p.common.util.CommonUtil;
import com.stockp2p.common.util.SYSTEMCONST;
/**
 * 数据库管理类
 * 
 * @author 一龙
 * 
 */
public class DBManager {
	private final int BUFFER_SIZE = 400000;

	// 注意这里的PACKAGE_NAME改成你的项目的包名字，如果你不用你的包名那么下面的FileOutputStream fos = new
	// FileOutputStream(dbfile);
	
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ SYSTEMCONST.PACKAGE_NAME ;//+"/databases"; // 在手机里存放数据库的位置

	private SQLiteDatabase sQLiteDatabase;

	private Context context;

	public DBManager(Context context) {
		this.context = context;
	}

	public SQLiteDatabase openDatabase() {
		this.sQLiteDatabase = this.openDatabase(DB_PATH + "/" + SYSTEMCONST.DB_NAME);
		return sQLiteDatabase;
	}

	private SQLiteDatabase openDatabase(String dbfile) {
		try {
			String versionName = CommonUtil.getVersionName(context);
			String oldVersionName = context.getSharedPreferences("userprefs",
					Context.MODE_PRIVATE).getString("versionName", "");
			if (!(new File(dbfile).exists())
					|| !oldVersionName.equals(versionName)) {// 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
				// 如果更新版本，需要拷贝新版本数据库和最后更新时间
				copy();// 拷贝assets里的图片到sd卡里
				context.getSharedPreferences("userprefs", Context.MODE_PRIVATE)
						.edit()
						.putString("updateDate", Constants.UPDATEDATEORIGINAL)
						.commit();
				InputStream is = this.context.getResources().openRawResource(
						R.raw.xinhua_phone); // 欲导入的数据库
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			context.getSharedPreferences("userprefs", Context.MODE_PRIVATE)
					.edit().putString("versionName", versionName).commit();
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
					null);
			return db;
		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void copy() {
		String[] assets = { "bg.png", "cfj3d9gm.jpg", "head.png",
				"introduce1.png", "introduce2.png", "iovxe24e.jpg",
				"jquery1.8.2.min.js", "jquery.mobile1.4.2.min.css",
				"jquery.mobile1.4.2.min.js", "marker1.png", "marker2.png",
				"marker3.png", "product1.png", "product2.png", "q79ceb2l.jpg",
				"service1.png", "service2.png", "site.png", "tableth.png",
				"title.png", "zcmd76z3.jpg", "edyl.jpg", "jfhd.jpg", "xunbao.jpg" };
		for (int i = 0; i < assets.length; i++) {
			copy(assets[i]);
		}
	}

	public void copy(String fileName) {
		String savename = Environment.getExternalStorageDirectory() + "/nci/"
				+ fileName;
		File dir = new File(Environment.getExternalStorageDirectory() + "/nci/");
		// 如果目录不中存在，创建这个目录
		if (!dir.exists())
			dir.mkdir();
		try {
			if (!(new File(savename)).exists()) {
				InputStream is = context.getResources().getAssets()
						.open(fileName);
				FileOutputStream fos = new FileOutputStream(savename);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}