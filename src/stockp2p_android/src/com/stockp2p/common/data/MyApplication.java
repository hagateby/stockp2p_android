package com.stockp2p.common.data;

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
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.stockp2p.common.cache.UserInfoManager;
import com.stockp2p.common.db.Channel;
import com.stockp2p.common.db.Content;
import com.stockp2p.common.db.Network;


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
	public BMapManager bMapManager = null;
	public String routeSelectStartName;
	public GeoPoint routeSelectEnd;
	public String routeSelectEndName;
	public MKPlanNode startPoint = new MKPlanNode();
	public MKPlanNode endPoint = new MKPlanNode();
	public ArrayList<Channel> channelList;
	public ArrayList<Content> contentList;

	// public ApplicantInfoBean applicantInfoBean;
	// public ApplicantInfoBean applicantInfoBeanNew;
	// public InsuredInfoBean insuredInfoBean;
	// public InsuredInfoBean insuredInfoBeanNew;
	// public InsuredGetStyleBean insuredGetStyleBean;
	// public InsuredGetStyleBean insuredGetStyleBeanNew;
	// public ClaimServiceBean claimserviceBeanNew;

	// 我的保险--保单查询--保单详情--险类列表
	public Object risObj;
	// 网点查询列表
	public ArrayList<Network> networkList;

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

	public Content car;
	public Content medical;
	public Content tooth;
	public Content info;
	public Content homeDoctor;
	public Content globalGuest;
	public Content doubleTreat;
	public Content portGuest;
	public Content health;
	public Content festival;
	public Content useHonorService;
	public Content insInstruction;

	public ArrayList<ImageView> cacheAdImageViews;
	public ArrayList<String> cacheAdImageNames;
	public boolean loginStatus = UserInfoManager.getInstance().isLogin();

	@Override
	public void onCreate() {
		super.onCreate();
		myApplication = this;
		System.out.println("myapplication oncreat……");
		// 捕获错误 log
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
	
	}

	// 常用事件监听，用来处理日常的网络错误，授权验证错误等
	private static MyApplication myApplication = null;

	public static MyApplication getInstance() {
		return myApplication;
	}

	public static class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(
						MyApplication.getInstance().getApplicationContext(),
						"您的网络出错啦！", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(
						MyApplication.getInstance().getApplicationContext(),
						"输入正确的检索条件！", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				Toast.makeText(
						MyApplication.getInstance().getApplicationContext(),
						"请输入正确的KEY", Toast.LENGTH_LONG).show();
			}
		}
	}

	// 屏幕分辨率
	private int screen_width;
	private int screen_height;

	public int getScreen_width() {
		return screen_width;
	}

	public void setScreen_width(int screen_width) {
		this.screen_width = screen_width;
	}

	public int getScreen_height() {
		return screen_height;
	}

	public void setScreen_height(int screen_height) {
		this.screen_height = screen_height;
	}

	// Xutils工具
	public static HttpUtils httpUtils;
	public static BitmapUtils bitmapUtils;

	/**
	 * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
	 * 
	 * @param appContext
	 *            application context
	 * @return
	 */
	public static BitmapUtils getBitmapUtils(Context appContext) {
		if (bitmapUtils == null) {
			bitmapUtils = new BitmapUtils(appContext);
		}
		return bitmapUtils;
	}

	// httputils
	public static HttpUtils getHttpUtils(Context appContext) {
		if (httpUtils == null) {
			httpUtils = new HttpUtils();
		}
		return httpUtils;
	}
}