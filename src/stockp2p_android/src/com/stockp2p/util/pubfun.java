package com.stockp2p.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.stockp2p.R;
import  com.stockp2p.common.data.MyApplication;


public class pubfun {

	
	/*
	 * 查询IMGEDOWNLOGDURL
	 * */
	
	public static String getimageDownloadUrl()
	{
		 SQLiteDatabase db;
	     String code =null; 
		  
		db = MyApplication.getdb();

		Cursor c = db.rawQuery("select * from code where codetype=?",
					new String[] { "imageDownloadUrl" });
			if (c.moveToFirst()) {
				code = c.getString(c.getColumnIndex("code"));
			}
	
		
		return code;
	}
	

	
	
}
