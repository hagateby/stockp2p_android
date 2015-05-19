package com.ktsf.common.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.stockp2p.R;
import com.ktsf.common.data.Constants;
import com.ktsf.common.data.Graphics;
import com.ktsf.common.data.MyApplication;

import com.ktsf.common.db.Network;
import com.ktsf.framework.BaseFragmentActivity;

public class MapActivity extends BaseFragmentActivity {

	private MyApplication app;

	// 地图相关
	private static MapView mapView;
	private MapController mapController;

	// 位置图层
	LocationData locationData = null;
	MyLocationOverlay myLocationOverlay = null;

	// poi图层
	List<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();
	public Drawable res;
	OverlayTest overlayTest;

	Button mBtnDrive = null; // 驾车搜索
	Button mBtnTransit = null; // 公交搜索
	Button mBtnWalk = null; // 步行搜索
	Button mBtnCusRoute = null; // 自定义路线

	MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	private int source;
	private int showway;



	// 当前位置坐标
	private double locLatitude;
	private double locLongitude;

	static double DEF_PI = 3.14159265359; // PI2.
	static double DEF_2PI = 6.28318530712; // 2*PI3.
	static double DEF_PI180 = 0.01745329252; // PI/180.04.
	static double DEF_R = 6370693.5; // radius of earth

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		app = MyApplication.getInstance();
		if (app.bMapManager == null) {
			app.bMapManager = new BMapManager(this);
			app.bMapManager.init(MyApplication.strKey,
					new MyApplication.MyGeneralListener());
		}

		if (getIntent().getExtras() != null) {
			source = getIntent().getExtras().getInt("source");
			showway = getIntent().getExtras().getInt("showway");
		}

		if (source == Constants.NETWORK) {
			setContainerView("网点查询", R.layout.common_map);
		} else {
			setContainerView("定点医院", R.layout.common_map);
		}

		// 地图
		mapView = (MapView) findViewById(R.id.bmapView);
		mapController = mapView.getController();
		mapController.setZoom(14);
		mapController.enableClick(true);
		mapView.setBuiltInZoomControls(true);
		mapView.regMapViewListener(app.bMapManager, new MyMapViewListener());

		// 位置图层
		myLocationOverlay = new MyLocationOverlay(mapView);
		locationData = new LocationData();
		mapView.getOverlays().clear();
		myLocationOverlay.setData(locationData);
		mapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		mapView.refresh();

		// poi图层
		res = getResources().getDrawable(R.drawable.common_mapactivity_ballon);

		overlayItemList.clear();
		overlayItemList = new ArrayList<OverlayItem>();

	

	

		// 初始化搜索模块，注册事件监听
		mSearch = new MKSearch();
		mSearch.init(app.bMapManager, new MKSearchListener() {
			@Override
			public void onGetPoiDetailSearchResult(int type, int error) {
			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
				// 错误号可参考MKEvent中的定义
				if (error != 0 || res == null) {
					Toast.makeText(MapActivity.this, "抱歉，未找到结果",
							Toast.LENGTH_SHORT).show();
					return;
				}
				RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this,
						mapView);
				// 此处仅展示一个方案作为示例
				routeOverlay.setData(res.getPlan(0).getRoute(0));
				mapView.getOverlays().clear();
				mapView.getOverlays().add(overlayTest);
				mapView.getOverlays().add(routeOverlay);
				mapView.refresh();
				// 使用zoomToSpan()绽放地图，使路线能完全显示在地图�?
				
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
				if (error != 0 || res == null) {
					Toast.makeText(MapActivity.this, "抱歉，未找到结果",
							Toast.LENGTH_SHORT).show();
					return;
				}
				TransitOverlay routeOverlay = new TransitOverlay(
						MapActivity.this, mapView);
				// 此处仅展示一个方案作为示例
				routeOverlay.setData(res.getPlan(0));
				mapView.getOverlays().clear();
				mapView.getOverlays().add(overlayTest);
				mapView.getOverlays().add(routeOverlay);
				mapView.refresh();
				// 使用zoomToSpan()绽放地图，使路线能完全显示在地图
				mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
						routeOverlay.getLonSpanE6());
				mapView.getController().animateTo(res.getStart().pt);
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
				if (error != 0 || res == null) {
					Toast.makeText(MapActivity.this, "抱歉，未找到结果",
							Toast.LENGTH_SHORT).show();
					return;
				}
				RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this,
						mapView);
				// 此处仅展示一个方案作为示�?
				routeOverlay.setData(res.getPlan(0).getRoute(0));
				mapView.getOverlays().clear();
				mapView.getOverlays().add(overlayTest);
				mapView.getOverlays().add(routeOverlay);
				mapView.refresh();
				// 使用zoomToSpan()绽放地图，使路线能完全显示在地图
				mapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
						routeOverlay.getLonSpanE6());
				mapView.getController().animateTo(res.getStart().pt);

			}

			public void onGetAddrResult(MKAddrInfo res, int error) {
			}

			public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
			}
		});

		// 设定搜索按钮的响应
		mBtnDrive = (Button) findViewById(R.id.drive);
		mBtnTransit = (Button) findViewById(R.id.transit);
		mBtnWalk = (Button) findViewById(R.id.walk);
		// mBtnCusRoute = (Button)findViewById(R.id.cusroute);

		OnClickListener clickListener = new OnClickListener() {
			public void onClick(View v) {
				SearchButtonProcess(v);
			}
		};

		mBtnDrive.setOnClickListener(clickListener);
		mBtnTransit.setOnClickListener(clickListener);
		mBtnWalk.setOnClickListener(clickListener);
		// mBtnCusRoute.setOnClickListener(clickListener);

		Button buttonConfig = (Button) findViewById(R.id.base_activity_bt_base_title_more);
		buttonConfig.setVisibility(View.VISIBLE);
		buttonConfig.setBackgroundResource(R.drawable.framework_more_selector);
		buttonConfig.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});


	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mapView.onRestoreInstanceState(savedInstanceState);
	}

	// 常用事件监听，用来处理异常的网络错误，授权验证错误等
	class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(MapActivity.this, "您的网络出错啦！", Toast.LENGTH_LONG)
						.show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(MapActivity.this, "输入正确的检索条件！",
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误
				Toast.makeText(MapActivity.this, "请输入正确的KEY", Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	public double GetLongDistance(double lon1, double lat1, double lon2,
			double lat2) {
		double ew1, ns1, ew2, ns2;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 求大圆劣弧与球心的角(弧度)
		distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1)
				* Math.cos(ns2) * Math.cos(ew1 - ew2);
		// 调整到[-1..1]范围内，避免溢出
		if (distance > 1.0)
			distance = 1.0;
		else if (distance < -1.0)
			distance = -1.0;
		// 求大圆劣弧长度
		distance = DEF_R * Math.acos(distance);
		return distance;
	}

	public class MyMapViewListener implements MKMapViewListener {
		@Override
		public void onMapMoveFinish() {
		}

		
		public void onClickMapPoi(MapPoi mapPoiInfo) {
			
		}

		@Override
		public void onGetCurrentMap(Bitmap b) {
		}

		@Override
		public void onMapAnimationFinish() {
		}
	}

	public class OverlayTest extends ItemizedOverlay<OverlayItem> {
		public OverlayTest(Drawable arg0, MapView arg1) {
			super(arg0, arg1);
			// TODO Auto-generated constructor stub
		}

		private Context mContext = null;
		PopupOverlay pop = null;
		int popIndex;
		Toast mToast = null;


		protected boolean onTap(int index) {
			this.popIndex = index;
			Bitmap[] bmps = new Bitmap[1];
			try {
				Bitmap img = BitmapFactory.decodeStream(
						mContext.getAssets().open("marker2.png")).copy(
						Bitmap.Config.ARGB_8888, true);
				if (source == Constants.NETWORK) {
					bmps[0] = Graphics.addTxtToBitmap(img, "新华保险");
				} else {
					bmps[0] = Graphics.addTxtToBitmap(img, "定点医院");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			pop.showPopup(bmps, overlayItemList.get(index).getPoint(), 32);
			if (null == mToast)
				mToast = Toast.makeText(mContext, overlayItemList.get(index)
						.getTitle(), Toast.LENGTH_SHORT);
			else
				mToast.setText(overlayItemList.get(index).getSnippet());
			mToast.show();
			return true;
		}

		public boolean onTap(GeoPoint pt, MapView mapView) {
			if (pop != null) {
				pop.hidePop();
			}
			super.onTap(pt, mapView);
			return false;
		}
	}

	// 切换当前位置到选择点的路线类型：公交、出租车、步行
	void SearchButtonProcess(View v) {

		MKPlanNode stNode = new MKPlanNode();
		stNode = app.startPoint;
		MKPlanNode enNode = new MKPlanNode();
		enNode = app.endPoint;

		
	}
}
