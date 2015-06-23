package com.stockp2p.framework.baseframe;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;
import com.stockp2p.R;
import com.stockp2p.common.cache.UserInfoManager;
import com.stockp2p.common.data.Constants;
import com.stockp2p.common.data.MyApplication;
import com.stockp2p.common.data.MySetting;
import com.stockp2p.common.data.ScreenObserver;
import com.stockp2p.common.data.UpdateDownloadManger;
import com.stockp2p.common.data.ScreenObserver.observerScreenStateUpdateListener;
import com.stockp2p.common.db.DBManager;
import com.stockp2p.common.db.FrameWork_Frame;
import com.stockp2p.common.db.FrameWork_Frame_DAO;
import com.stockp2p.common.db.Version;

import com.stockp2p.common.util.ScreenShot;
import com.stockp2p.common.util.ShareUtils;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.common.util.PubFun;

import com.stockp2p.components.login.LoginActicity;
import com.stockp2p.framework.ExitApplication;
import com.stockp2p.framework.SetActivity;



public class BaseFragmentActivity extends FragmentActivity {

	private static final String TAG = "BaseFragmentActivity";
	protected FragmentManager thisManager = getSupportFragmentManager();
	protected MyApplication myApplication;
	protected FragmentActivity context = this;
	private ScreenObserver mScreenObserver;
	protected FrameWork_Frame framework_;
	/**
	 * isExit != null时显示底栏 isExit 值为 true 返回退出应用 isExit 值为 false 返回上一页面
	 */
	protected String isExit = null;
	private String isExit2 = "true";
	/**
	 * Bottombar状态
	 */
	protected static int bottomBarStatus;
	public static final int MAINPAGE = 1;
	protected static final int NCIINTRODUCTION = 2;
	protected static final int VIPSERVICE = 3;
	protected static final int MYINSURANCE = 4;

	private View inflateView = null;
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			   case Constants.MSG_SHARE_HANDLE:// 分享返回值
				
					break;
				case 2:
					Toast.makeText(getApplicationContext(), "分享出错", 0).show();
					break;
				case 3:
					Toast.makeText(getApplicationContext(), "分享取消", 0).show();
					break;
				default:
					break;
				}
			}
		
	};
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.framework_base_activity);

		if (getIntent() != null) {
			if ((FrameWork_Frame) getIntent().getParcelableExtra("framework") != null) {
				// 传递进的组件
				framework_ = (FrameWork_Frame) getIntent().getParcelableExtra(
						"framework");
				TipUitls.Log(TAG, "要跳进的组件名---->" + framework_.getFrameName());
			}
			if (getIntent().getStringExtra("isExit") != null) {
				// isExit !=null 为跳到登录
				isExit = getIntent().getStringExtra("isExit");
				TipUitls.Log(TAG, "登录页面是否退出应用----->" + isExit);
			}

		}

		myApplication = ((MyApplication) getApplicationContext());

		if (myApplication.db == null) {
			DBManager dBManager = new DBManager(this);
			SQLiteDatabase db = dBManager.openDatabase();
			myApplication.db = db;
			TipUitls.Log("BaseFragmentActivity", "初始化了 myApplication.db");
		}

		TipUitls.Log(TAG, "跳进的 activity---->"
				+ context.getClass().getName().toString());
		
		//initBottomBarStatus();		
		thisManager.beginTransaction()
	     // 底部工具栏
		.replace(R.id.toolsbarbottom, new MainToolsBar())
		.commit();
		
		// 遍历打开的 activity
		ExitApplication.getInstance().addActivity(context, isExit);

		initScreenObserver();
	}

	private void initBottomBarStatus() {
		if (Constants.moduleList == null) {
			Constants.moduleList = FrameWork_Frame_DAO.findByParentId(
					myApplication.db, "0", context);
		}
        //如果是登录页面
		if ( PubFun.isLogin(context.getClass().getName().toString())) {
			
		//lwh	ViewStub stub = (ViewStub) context.findViewById(R.id.bottom);
			
		//lwh	inflateView = stub.inflate();
			
			initBottombar();

		} else {
			
			//给每一framework增加操作bottom
			addbottomtobar();
		}
	}
   private void addbottomtobar()
   {
	   List<Integer> index = new ArrayList<Integer>();
		//给每一framework增加操作bottom
		for (int i = 0; i < Constants.moduleList.size(); i++) {
			TipUitls.Log(TAG, "---moduleList-->"
					+ Constants.moduleList.get(i).getPackageName());
			if (( Constants.moduleList.get(i)
					.getPackageName()).equals(context.getClass().getName()
					.toString()))
			{
				index.add(i);
		//lwh		ViewStub stub = (ViewStub) context
	//lwh					.findViewById(R.id.bottom);
				//lwh		if (stub != null) {
		//lwh			inflateView = stub.inflate();
		//		}

			}
		}

		if (index.size() == 1) {
			setbottomBarStatus(index.get(0) + 1);
			initBottombar();
		} else {
			for (Integer num : index) {
			 if ( !Constants.moduleList.get(num).getFrameId()
								.equals(framework_.getParentId())) {
					isExit2  = "false";
				
				}
				setbottomBarStatus(num + 1);
				initBottombar();
			}

		}

		index.clear();
		index = null;
   }
	private void initScreenObserver() {
		mScreenObserver = new ScreenObserver(context);
		mScreenObserver
				.observerScreenStateUpdate(new observerScreenStateUpdateListener() {
					@Override
					public void onScreenOn() {

					}

					@Override
					public void onScreenOff() {
						
						LogUtils.e("关闭音乐服务");
					}
				});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TipUitls.Log("BaseFragmentActivity", "注册更新广播---->");
		IntentFilter filter = new IntentFilter();
		filter.addAction("syncDone");
		context.registerReceiver(broadcastReceiver, filter);
		if (MySetting.getSlipButton2Boolean(context)) {
			
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		TipUitls.Log("BaseFragmentActivity", "取消注册广播---->");
		try {
			context.unregisterReceiver(broadcastReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
		ExitApplication.getInstance().removeActivity(context);
		super.onDestroy();
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			checkUpdate();
		}
	};

	private void checkUpdate() {
		// 如果手动检测新版本, 自动检测将取消
		if (Constants.isVersonUpdated == false) {
			SharedPreferences sp = context.getSharedPreferences("userprefs",
					Context.MODE_PRIVATE);
			String updatedFlag = sp.getString("updatedFlag", "0");
			TipUitls.Log("BaseFragmentActivity", "更新updatedFlag值为---->"
					+ updatedFlag);
			if ("1".equals(updatedFlag)) {
				String versionCode = sp.getString("versionCode", "");
				String versionDesc = sp.getString("versionDesc", "");
				String uploadUrl = sp.getString("uploadUrl", "");
				Version version = new Version();
				version.setOpeFlag(updatedFlag);
				version.setUploadUrl(uploadUrl);
				version.setVersionCode(versionCode);
				version.setVersionDesc(versionDesc);

				new UpdateDownloadManger(context).update(version);
			}

		}

	}

	public void click(View view) {
		switch (view.getId()) {
		case R.id.base_activity_bt_titleback:// 返回
			if (thisManager.getBackStackEntryCount() <= 1) {
				if ((inflateView != null || "true".equals(isExit)) && "true".equals(isExit2)) {
					finish();
					Manager.branch(context, Constants.moduleList.get(0));
				} else {
					finish();
				}
			} else {
				thisManager.popBackStackImmediate();
			}
			break;
		case R.id.base_activity_bt_base_title_more:// 更多

			if (true) {
				View layout = LayoutInflater.from(context).inflate(
						R.layout.framework_base_popset, null);
				final PopupWindow pop = new PopupWindow(layout,
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				pop.setFocusable(true);
				pop.setBackgroundDrawable(new BitmapDrawable());
				pop.setAnimationStyle(R.anim.common_animation_right_in);
				View mView = findViewById(R.id.base_activity_rl_titlelayout);
				pop.showAtLocation(layout, Gravity.TOP | Gravity.RIGHT, 0,
						getWindow().findViewById(Window.ID_ANDROID_CONTENT)
								.getTop() + mView.getHeight() - 10);
				// 设置
				layout.findViewById(R.id.base_set).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(context,
										SetActivity.class);
								intent.putExtra("framework", framework_);
								context.startActivity(intent);
								pop.dismiss();
							}
						});
				// 分享
				layout.findViewById(R.id.base_share).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String shareurl = "http://t.cn/8s0FjUd";
								String title = "掌上新华";
								String content = "移动新体验，欢迎您下载使用掌上新华。";
								// 先截图到sd卡再分享 速度慢
								ScreenShot.shoot(context);
								ShareUtils.showShare(
										false,
										null,
										context,
										handler,
										title,
										content,
										Environment
												.getExternalStorageDirectory()
												+ "/nci/xx.png", shareurl,
										false);
				
								pop.dismiss();
							}
						});
			}
			break;
		}
	}

	long mExitTime = 0;// 上一次按回退键的时间

	/**
	 * home 返回
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			if (thisManager.getBackStackEntryCount() <= 1) {
				// 显示底栏并且可以退出
				if (inflateView != null && !"false".equals(isExit) && "true".equals(isExit2)) {
					if ((System.currentTimeMillis() - mExitTime) > 2000) {
						Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT)
								.show();
						mExitTime = System.currentTimeMillis();

					} else {
						
					}
				} else {
					context.finish();
				}

			} else {
				thisManager.popBackStackImmediate();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void setContainerView(String title, int Resource) {
		FrameLayout container = (FrameLayout) findViewById(R.id.tab_container);
		TextView titleText = ((TextView) findViewById(R.id.base_activity_tv_titletxt));
		container.removeAllViews();
		titleText.setText(title);
		titleText.setVisibility(View.VISIBLE);
		View view_page = LayoutInflater.from(this).inflate(Resource, null);
		view_page.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		container.addView(view_page);
	}

	public void setbottomBarStatus(int i) {
		bottomBarStatus = i;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (isAppOnForeground()) {
			LogUtils.e("");
		} else {
			
			LogUtils.e("关闭音乐服务");
		}

	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}



	/**
	 * 底栏
	 */
	public void initBottombar() {
		
		TipUitls.Log(TAG, "initBottombar----->" + bottomBarStatus);
		int width = 0, height = 0;
		
		WindowManager.LayoutParams params = this.getWindow().getAttributes();
		
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		
		Display display = wm.getDefaultDisplay();
		
		height = display.getHeight() / 12;
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, height);
		
		inflateView.setLayoutParams(lp);
		
		if (MAINPAGE == bottomBarStatus) {
			RadioButton tab_a = (RadioButton) inflateView
					.findViewById(R.id.base_activity_rb_taba);
//			if (tab_a.isChecked()) {
//				tab_a.setBackgroundResource(R.drawable.addmenu_selected);
//			}else{
//				tab_a.setBackgroundResource(R.drawable.activitynotfound);
//			}
			tab_a.setChecked(true);
		} else if (NCIINTRODUCTION == bottomBarStatus) {
			RadioButton tab_b = (RadioButton) inflateView
					.findViewById(R.id.base_activity_rb_tabb);
			tab_b.setChecked(true);
		} else if (VIPSERVICE == bottomBarStatus) {
			RadioButton tab_c = (RadioButton) inflateView
					.findViewById(R.id.base_activity_rb_tabc);
			tab_c.setChecked(true);
		} else if (MYINSURANCE == bottomBarStatus) {
			RadioButton tab_d = (RadioButton) inflateView
					.findViewById(R.id.base_activity_rb_tabd);
			tab_d.setChecked(true);
		}

		RadioGroup group = (RadioGroup) inflateView
				.findViewById(R.id.base_activity_rg_bottombar);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.base_activity_rb_taba:
					finish();
					if ("1".equals(Constants.moduleList.get(0).getIsLogin())
							&& !UserInfoManager.getInstance().isLogin()) {
						setbottomBarStatus(1);
						context.startActivity(new Intent(context,
								LoginActicity.class).putExtra("isExit", "true")
								.putExtra("framework",
										Constants.moduleList.get(0)));
						overridePendingTransition(0, 0);
					} else {
						Manager.branch(context, Constants.moduleList.get(0));
					}
					break;
				case R.id.base_activity_rb_tabb:
					finish();
					if ("1".equals(Constants.moduleList.get(1).getIsLogin())
							&& !UserInfoManager.getInstance().isLogin()) {
						setbottomBarStatus(2);
						context.startActivity(new Intent(context,
								LoginActicity.class).putExtra("isExit", "true")
								.putExtra("framework",
										Constants.moduleList.get(1)));
						overridePendingTransition(0, 0);
					} else {
						Manager.branch(context, Constants.moduleList.get(1));
					}
					break;
				case R.id.base_activity_rb_tabc:
					finish();
					if ("1".equals(Constants.moduleList.get(2).getIsLogin())
							&& !UserInfoManager.getInstance().isLogin()) {
						setbottomBarStatus(3);
						context.startActivity(new Intent(context,
								LoginActicity.class).putExtra("isExit", "true")
								.putExtra("framework",
										Constants.moduleList.get(2)));
						overridePendingTransition(0, 0);
					} else {
						Manager.branch(context, Constants.moduleList.get(2));
					}
					break;
				case R.id.base_activity_rb_tabd:
					finish();
					if ("1".equals(Constants.moduleList.get(3).getIsLogin())
							&& !UserInfoManager.getInstance().isLogin()) {
						setbottomBarStatus(4);
						context.startActivity(new Intent(context,
								LoginActicity.class).putExtra("isExit", "true")
								.putExtra("framework",
										Constants.moduleList.get(3)));
						overridePendingTransition(0, 0);
					} else {
						Manager.branch(context, Constants.moduleList.get(3));
					}
					
					break;

				}
			}
		});

	}

}