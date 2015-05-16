package com.ktsf.common.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.baidu.frontia.FrontiaApplication;


import com.ktsf.common.cache.UserInfoManager;;


//import dalvik.system.VMRuntime;

public class MyApplication extends FrontiaApplication {

	/**
	 * 五要素 map 在CustomerInforAssociatedActivity客户信息关联获取用户五要素时赋值
	 */
	public static Map Five_elements = new HashMap<String, String>();
	// 本地存储添加菜单
	public JSONArray perList = new JSONArray();
	private final static float HEAP_UTILIZATION = 0.75f;
	private final static int MIN_HEAP_SIZE = 6 * 1024 * 1024;

	public static final String strKey = "8D946821B72175E6A2F802DBBF40430DA83EA6E9";


	// public ApplicantInfoBean applicantInfoBean;
	// public ApplicantInfoBean applicantInfoBeanNew;
	// public InsuredInfoBean insuredInfoBean;
	// public InsuredInfoBean insuredInfoBeanNew;
	// public InsuredGetStyleBean insuredGetStyleBean;
	// public InsuredGetStyleBean insuredGetStyleBeanNew;
	// public ClaimServiceBean claimserviceBeanNew;


	// 当前位置经纬度
	public double currentLatitude = 0;
	public double currentLongitude = 0;
	// 添加菜单是否是可删除状态
	public int isDelStatus = 0;
	// 长按出删除菜单的页面
	public int delPage = 0;

	// 房贷计算器的map
	public static List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();

	public SQLiteDatabase db;


	public ArrayList<ImageView> cacheAdImageViews;
	public ArrayList<String> cacheAdImageNames;
	public boolean loginStatus = UserInfoManager.getInstance().isLogin();

	
	public void onCreate() {
		
		myApplication = this;
		System.out.println("myapplication oncreat……");
		// 捕获错误 log
		//CrashHandler crashHandler = CrashHandler.getInstance();
		//crashHandler.init(this);
		// VMRuntime.getRuntime().setTargetHeapUtilization(HEAP_UTILIZATION);
		// VMRuntime.getRuntime().setMinimumHeapSize(MIN_HEAP_SIZE);
	}

	// 常用事件监听，用来处理日常的网络错误，授权验证错误等
	private static MyApplication myApplication = null;

	public static MyApplication getInstance() {
		return myApplication;
	}

	

 
	

	
}