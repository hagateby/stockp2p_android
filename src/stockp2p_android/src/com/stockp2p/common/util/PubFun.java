package com.stockp2p.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.WindowManager;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.stockp2p.R;
import com.stockp2p.common.data.Constants;
import com.stockp2p.common.data.MyApplication;
import com.stockp2p.common.db.DBManager;
import com.stockp2p.common.db.FrameWork_Frame;
import com.stockp2p.common.ifinvoke.Des3;
import com.stockp2p.framework.baseframe.BaseFragment;

public class PubFun {

	/*
	 * 查询IMGEDOWNLOGDURL
	 */
	private static String TAG = "pubfun";
	
public static String getimageDownloadUrl() {
		SQLiteDatabase db;
		String code = null;

		db = MyApplication.getdb();

		Cursor c = db.rawQuery("select * from FrameWork_Code where codetype=?",
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

	/*
	 * 给出屏幕高度
	 * */
	public static int getWindowHeight(Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        
		Display display = wm.getDefaultDisplay();
		
       return  display.getHeight();
		
	}
	/*
	 * 给出屏幕宽度
	 * */
	public static int getWindowWidth(Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		
		Display display = wm.getDefaultDisplay();

		return display.getWidth();
    
		
	}
	
	public static Bitmap loadfilebyName(Activity activity, String filename) {

		InputStream inputStream = null;
		Bitmap bitmap = null;

		try {
			inputStream = activity.getResources().getAssets().open(filename);
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
	
	public static void initConstConfig( ) {	
		
		
		SYSTEMCONST.DowdURL = getimageDownloadUrl();		
	}
   

	/**
	 * 初始化网络设置
	 */
	public static void initHost() {
		if (Constants.ISDEBUG) {
			TipUitls.Log(TAG, "WelcomeViewPagerActivity----进入内网");

			Constants.PROTOCOL = "http://";
			Constants.DOMIN_NAME = "www.bigpot.cn";
			Constants.URLHTML5 = "http://" + Constants.DOMIN_NAME;

			Constants.URL = "http://10.8.24.43:8080/tprtmt/ajax/test_jsonTestAction.action";
          
	
		} else {
			TipUitls.Log(TAG,"WelcomeViewPagerActivity----进入外网");

			Constants.PROTOCOL = "https://";
			Constants.DOMIN_NAME = "www.bigpot.cn";
			Constants.URLHTML5 = "http://" + Constants.DOMIN_NAME;

			Constants.PORT = 443;

			Constants.URL = "";
		}
	}
	
	/*
	 * 加截缺省图片
	 * */
	public static void setDefaultImage(BitmapDisplayConfig config,Activity activity)
	{
		
		config.setLoadFailedDrawable(activity.getResources().getDrawable(
				R.drawable.defaultshowimage));

		config.setLoadingDrawable(activity.getResources().getDrawable(
				R.drawable.defaultshowimage));
	
	}
	/*
	 * 初始化数据库
	 * */
	public static  SQLiteDatabase getMyApplicationDb(MyApplication  myApplication , Context context)
	{
		
		if (myApplication.db == null) {		
			DBManager dBManager = new DBManager(context);
			myApplication.db = dBManager.openDatabase();
		}
		
		return myApplication.db;
	}
	

	/*
	 * 打印frame的清单
	 * */
	public static void printFrames(String TAG,  ArrayList<FrameWork_Frame> frameworks)
	{
		TipUitls.Log(TAG, "frameworks----->size:"  +frameworks.size() );
		for(int i=0 ;i < frameworks.size();i++)
		{
			if (null !=frameworks.get(i))
			   TipUitls.Log(TAG, "frameworks----->" + i +":" + frameworks.get(i).packageName);
			
		}
		
	}
	
	/**
	 * 根据类名转换成类对象
	 */
	
	public static Class toClassbyName(String classname)  {
		Class activity =null;
		try {
		
			activity = Class.forName(  classname);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activity;

	}
	
	/*
	 * 根据系统设置对字符串进行解密
	 * */	
	public static String getEncryptString(String str){
		
	  String result =new String();	
	if (SYSTEMCONST.EncryptFlag ==true) {
		try {
			result = Des3.decode(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		}else {
			result =str;
		}
	  
	return result;
	}
	
	/*
	 * 根据系统设置对字符串进行加密
	 * */
	public static String setEncryptString(String str){
		
		  String result =new String();	
		if (SYSTEMCONST.EncryptFlag ==true) {
			try {
				result = Des3.encode(str);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			}else {
				result =str;
			}
		  
		return result;
		}
	
	
	// 前一年当天日期
		public static final String LASTYEARCURRENTDATE = new SimpleDateFormat(
				"yyyy-MM-dd").format(lastDate()).toString();

		private static Date lastDate() {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, -1);
			return calendar.getTime();
		}

		// 当天日期
		public static final String CURRENTDATE = new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date()).toString();
	
}
