package com.stockp2p.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.stockp2p.R;
import com.stockp2p.common.data.MyApplication;
import com.stockp2p.framework.baseframe.BaseFragment;

public class pubfun {

	/*
	 * 查询IMGEDOWNLOGDURL
	 */

	public static String getimageDownloadUrl() {
		SQLiteDatabase db;
		String code = null;

		db = MyApplication.getdb();

		Cursor c = db.rawQuery("select * from code where codetype=?",
				new String[] { "imageDownloadUrl" });
		if (c.moveToFirst()) {
			code = c.getString(c.getColumnIndex("code"));
		}

		return code;
	}

	public static boolean isLogin(String classname) {
		return SYSTEMCONST.loginclassname.equals(classname);
	}

	public static String getStorayDirectory() {

		return Environment.getExternalStorageDirectory() + "/nci/";
	}

	public static Bitmap loadfilebyName(BaseFragment fragment, String filename) {

		InputStream inputStream = null;
		Bitmap bitmap = null;

		try {
			inputStream = fragment.getResources().getAssets().open(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (inputStream != null) {

			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inTempStorage = new byte[100 * 1024];
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inPurgeable = true;
			opts.inSampleSize = 2;
			opts.inInputShareable = true;
			bitmap = BitmapFactory.decodeStream(inputStream, null, opts);
		}
		return bitmap;
	}

	public static String getlocalFilebyName(String filename) {
		String uri;
		File file = new File(getStorayDirectory() + filename);
		if (file.exists()) {// 如果本地存在
			uri = Uri.fromFile(file).toString();
		} else {// 如果本地没有,去网络加载
			uri = SYSTEMCONST.DowdURL + filename;
		}
		return uri;
	}
	public static void writeStrToFile(String url, String xml) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(url));
			Writer os = new OutputStreamWriter(fos, "UTF-8");
			os.write(xml);
			os.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void initMyApp() {
		
		SYSTEMCONST.DowdURL = getimageDownloadUrl();
		
		
		

	}

}
