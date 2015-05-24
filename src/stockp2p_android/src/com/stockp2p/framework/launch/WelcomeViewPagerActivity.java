package com.stockp2p.framework.launch;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.stockp2p.R;
import com.stockp2p.common.data.Constants;
import com.stockp2p.common.data.MyApplication;
import com.stockp2p.common.db.DBManager;
import com.stockp2p.common.service.SyncService;
import com.stockp2p.common.util.CommonUtil;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.framework.Frameworkdate;
import com.stockp2p.framework.Manager;

public class WelcomeViewPagerActivity extends FragmentActivity {

	private static String TAG = "WelcomeViewPagerActivity";
	private ViewPager viewPager; // 为了支持11以下
	private List<ViewHolder> imageViews = null;

	// private int[] imageResId_middle; // 图片ID

	private int currentItem = 0;

	private ScheduledExecutorService scheduledExecutorService;

	private final int CHANGE_IMAGE = 6;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CHANGE_IMAGE:
				viewPager.setCurrentItem(currentItem);
				break;
			}
		};
	};

	private SharedPreferences sp;
	private boolean hasRun;
	/**
	 * 用来判断是不是从设置跳转
	 */
	private String source;
	/**
	 * 离线更新服务
	 */
	public static Intent syncService;

	private MyApplication myApplication;

	private SQLiteDatabase db;

	private String versionName;
	private String oldVersionName;
	private String positionStr;

	/** 跳过按钮 **/
	private Button jumpButton;
	private int[] dots;
	private Button jump;
	private ImageView viewDot;

	private Map<Integer, SoftReference<Bitmap>> imageCache = new HashMap<Integer, SoftReference<Bitmap>>();

	// private ImageView view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.framework_welcome);

		init();

		TipUitls.Log(TAG, "init----初始化完成");
	}

	/**
	 * 
	 */
	private void init() {
	     
		myApplication = (MyApplication) this.getApplication();
		
	     //myApplication = new MyApplication();

		SharedPreferences sPreferences = WelcomeViewPagerActivity.this
				.getSharedPreferences("positionaddress", Context.MODE_PRIVATE);

		positionStr = sPreferences.getString("regionCode", null);

		if (getIntent() != null && getIntent().getExtras() != null) {
			source = getIntent().getExtras().getString("source");
		}

		// 读取用户偏好文件
		sp = this.getSharedPreferences("userprefs", Context.MODE_PRIVATE);

		hasRun = sp.getBoolean("hasRun", false);
		
		initHost();

		initDatabase();

		startBaiduPush();

		Constants.moduleList = Frameworkdate.findByParentId(db, "0", this);

		 //后台数据同步 
			if (!"setting".equals(source)) {
				startSync();
			}
		//检查版本
		boolean chkversion=chkVersion();
        //判断是否要启动欢迎页面
		if ( chkversion && ("setting".equals(source))) {

				finish();
				Manager.branch(WelcomeViewPagerActivity.this,
						Constants.moduleList.get(0));
				
				overridePendingTransition(android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
		
			    TipUitls.Log(TAG, "跳过欢迎页面,版本号为:" + versionName);
			
			return;
		} else {

			initView();
			// 点的图片
			showimgview();
		}
         
		sp.edit().putBoolean("hasRun", true).commit();
		sp.edit().putString("oldVersionName", versionName).commit();
	}

	private void nextview()
	{
		
		if (!"setting".equals(source)) {
			// 不是从设置界面跳转
				finish();
				Manager.branch(WelcomeViewPagerActivity.this,
						Constants.moduleList.get(0));					
			if (!scheduledExecutorService.isShutdown()) {
				scheduledExecutorService.shutdown();
			}
		} else {
			// 从设置界面跳过来
			finish();
		}
	}
	
	private void showimgview() {
		
		//滚动小点图片
		imageCache.put(0,
				getSoftBitmap(R.drawable.framework_splash_bottom_dot));
		imageCache.put(1,
				getSoftBitmap(R.drawable.framework_splash_2_bottom_dot));
		imageCache.put(2,
				getSoftBitmap(R.drawable.framework_splash_3_bottom_dot));
		imageCache.put(3,
				getSoftBitmap(R.drawable.framework_splash_4_bottom_dot));

		viewDot = (ImageView) findViewById(R.id.v_dot);

		jump = (Button) findViewById(R.id.welcome_bt_jump);

		jump.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 跳过引导界面
				nextview();
			}
		});

		jumpButton = (Button) findViewById(R.id.jumpButton);
		jumpButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// 跳过引导界面
				nextview();

			}
		});

		viewPager = (ViewPager) findViewById(R.id.vp);
		viewPager.setAdapter(new MyAdapter());

		viewPager.setOnPageChangeListener(new MyPageChangeListener());

		scheduledExecutorService = Executors
				.newSingleThreadScheduledExecutor();
		// scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 4,
		// 4, TimeUnit.SECONDS);
	}

	private boolean  chkVersion() {
		try {
			// 取到版本号
			versionName = CommonUtil.getVersionName(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		oldVersionName = sp.getString("oldVersionName", "");
		
		return  (oldVersionName.equals(versionName));
	}

	private void initPositionActivity() {
		/*
		 * finish(); Intent intent = new Intent( WelcomeViewPagerActivity.this,
		 * PositionActivity.class); startActivity(intent);
		 */
	}

	/**
	 * 初始化网络设置
	 */
	private void initHost() {
		if (Constants.ISDEBUG) {
			TipUitls.Log(TAG, "WelcomeViewPagerActivity----进入内网");

			Constants.PROTOCOL = "https://";
			Constants.DOMIN_NAME = "zsxh.newchinalife.com";
			Constants.URLHTML5 = "http://" + Constants.DOMIN_NAME;

			Constants.URL = "";

		} else {
			TipUitls.Log(TAG, "WelcomeViewPagerActivity----进入外网");

			Constants.PROTOCOL = "https://";
			Constants.DOMIN_NAME = "zxh.newchinalife.com";
			Constants.URLHTML5 = "http://" + Constants.DOMIN_NAME;

			Constants.PORT = 443;

			Constants.URL = "";
		}
	}

	/**
	 * 初始化数据库
	 */
	private void initDatabase() {
		DBManager dBManager = new DBManager(this);
		db = dBManager.openDatabase();
		myApplication.db = db;
		TipUitls.Log(TAG, "WelcomeViewPagerActivity----初始化了 myApplication.db");
	}

	/**
	 * 开启服务
	 */
	private void startSync() {

		// 数据更新
		if (positionStr != null) {
			syncService = new Intent(this, SyncService.class);
			startService(syncService);
			TipUitls.Log(TAG, "开启同步服务");
		}
	}

	@Override
	protected void onStop() {
		if (scheduledExecutorService != null) {
			scheduledExecutorService.shutdown();
		}

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (viewPager != null) {
			viewPager = null;
		}

		if (imageViews != null) {
			imageViews.clear();
			imageViews = null;
		}

		if (imageCache != null) {
			imageCache = null;
		}

		if (dots != null) {
			dots = null;
		}

		System.gc();
	}

	private class ScrollTask implements Runnable {
		public void run() {
			// synchronized (viewPager) {
			currentItem = currentItem + 1;
			if (currentItem >= imageViews.size()) {
				if (!"setting".equals(source)) {
					if (positionStr == null) {
						initPositionActivity();
					} else {
						finish();

						Manager.branch(WelcomeViewPagerActivity.this,
								Constants.moduleList.get(0));
					}
					if (!scheduledExecutorService.isShutdown()) {
						scheduledExecutorService.shutdown();
					}
				}
				finish();
			}
			handler.sendEmptyMessage(CHANGE_IMAGE);
			// }
		}
	}

	private boolean left = false;
	private boolean right = false;
	private boolean isScrolling = false;
	private int lastValue = -1;
	private AlphaAnimation alphaAnimation;
	private TranslateAnimation alphaAnimation1;
	private TranslateAnimation alphaAnimation11;
	private TranslateAnimation alphaAnimation2;
	private TranslateAnimation alphaAnimation22;

	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		@Override
		public void onPageSelected(int position) {

			try {
				// 下方的点
				if (imageCache.get(position).get() != null) {
					viewDot.setImageBitmap(imageCache.get(position).get());
				} else {
					imageCache
							.put(0,
									getSoftBitmap(R.drawable.framework_splash_bottom_dot));
					imageCache
							.put(1,
									getSoftBitmap(R.drawable.framework_splash_2_bottom_dot));
					imageCache
							.put(2,
									getSoftBitmap(R.drawable.framework_splash_3_bottom_dot));
					imageCache
							.put(3,
									getSoftBitmap(R.drawable.framework_splash_4_bottom_dot));
					viewDot.setImageBitmap(imageCache.get(position).get());
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			// 点击进入
			if (position == imageCache.size() - 1) {
				jump.setVisibility(View.INVISIBLE);
				jumpButton.setVisibility(View.VISIBLE);
			} else {
				jump.setVisibility(View.VISIBLE);
				jumpButton.setVisibility(View.INVISIBLE);
			}

			imageViews.get(position).textView1.getBackground().setAlpha(255);
			imageViews.get(oldPosition).textView1.getBackground().setAlpha(0);

			if (position != 1) {
				imageViews.get(position).textView2.getBackground()
						.setAlpha(255);
			}
			if (oldPosition != 1) {
				imageViews.get(oldPosition).textView2.getBackground().setAlpha(
						0);
			}

			imageViews.get(position).imageView.getDrawable().setAlpha(255);
			imageViews.get(oldPosition).imageView.getDrawable().setAlpha(10);

			oldPosition = position;

			// 从完全透明到不透明
			alphaAnimation.setDuration(500);
			imageViews.get(position).imageView.startAnimation(alphaAnimation);

			if (left == true) {
				alphaAnimation1.setDuration(500);
				alphaAnimation1.setInterpolator(WelcomeViewPagerActivity.this,
						android.R.anim.accelerate_decelerate_interpolator);

				alphaAnimation1.setFillAfter(true);
				imageViews.get(position).textView1
						.startAnimation(alphaAnimation1);

				// 除第二张图
				if (position != 1) {
					alphaAnimation2.setDuration(500);
					alphaAnimation2.setStartOffset(600);
					alphaAnimation2.setInterpolator(
							WelcomeViewPagerActivity.this,
							android.R.anim.accelerate_decelerate_interpolator);
					alphaAnimation2.setFillAfter(true);
					imageViews.get(position).textView2
							.startAnimation(alphaAnimation2);
				}

			} else if (right == true) {
				alphaAnimation11.setDuration(500);
				alphaAnimation11.setInterpolator(WelcomeViewPagerActivity.this,
						android.R.anim.accelerate_decelerate_interpolator);
				alphaAnimation11.setFillAfter(true);
				imageViews.get(position).textView1
						.startAnimation(alphaAnimation11);
				// 除第二张图
				if (position != 1) {
					alphaAnimation22.setDuration(500);
					alphaAnimation22.setStartOffset(600);
					alphaAnimation22.setInterpolator(
							WelcomeViewPagerActivity.this,
							android.R.anim.accelerate_decelerate_interpolator);
					alphaAnimation22.setFillAfter(true);
					imageViews.get(position).textView2
							.startAnimation(alphaAnimation22);

				}
			}

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

			if (arg0 == 1) {
				isScrolling = true;
			} else {
				isScrolling = false;
			}

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

			if (isScrolling) {
				if (lastValue > arg2) {
					// 递减，向右滑动
					right = true;
					left = false;
				} else if (lastValue < arg2) {
					// 递减，向右滑动
					right = false;
					left = true;
				} else if (lastValue == arg2) {
					right = left = false;
				}
			}
			lastValue = arg2;

		}

	}

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageCache.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1).view);
			return imageViews.get(arg1).view;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(imageViews.get(arg1).view);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	private void initView() {
		alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		alphaAnimation1 = new TranslateAnimation(-1200, 0, 0.0f, 0.0f);
		alphaAnimation2 = new TranslateAnimation(-1200, 0, 0.0f, 0.0f);
		alphaAnimation11 = new TranslateAnimation(1200, 0, 0.0f, 0.0f);
		alphaAnimation22 = new TranslateAnimation(1200, 0, 0.0f, 0.0f);
		// 中间大图
		HashMap<Integer, SoftReference<Bitmap>> imageCacheMid = new HashMap<Integer, SoftReference<Bitmap>>();
		
		imageCacheMid.put(0, getSoftBitmap(R.drawable.framework_splash_1));
		imageCacheMid.put(1, getSoftBitmap(R.drawable.framework_splash_2));
		imageCacheMid.put(2, getSoftBitmap(R.drawable.framework_splash_3));
		imageCacheMid.put(3, getSoftBitmap(R.drawable.framework_splash_4));

		// 底部 logo
		HashMap<Integer, SoftReference<Bitmap>> imageCacheBottom = new HashMap<Integer, SoftReference<Bitmap>>();
		
		imageCacheBottom.put(0,
				getSoftBitmap(R.drawable.framework_splash_bottom));
		imageCacheBottom.put(1,
				getSoftBitmap(R.drawable.framework_splash_bottom));
		imageCacheBottom.put(2,
				getSoftBitmap(R.drawable.framework_splash_bottom));
		imageCacheBottom.put(3,
				getSoftBitmap(R.drawable.framework_splash_bottom));

		// 移动文字设置
		int[][] imageResId_text = new int[][] {
				{ R.drawable.framework_splash_1_text_up,
						R.drawable.framework_splash_1_text_down },
				{ R.drawable.framework_splash_2_text },
				{ R.drawable.framework_splash_3_text_up,
						R.drawable.framework_splash_3_text_down },
				{ R.drawable.framework_splash_4_text_up,
						R.drawable.framework_splash_4_text_down } };
		// 背景色设置
		int[] backColor = new int[] { Color.parseColor("#5692C8"),
				Color.parseColor("#B7BEE0"), Color.parseColor("#D6A778"),
				Color.parseColor("#DE6F50") };

		imageViews = new ArrayList<ViewHolder>();// adapter 用的 list

		ViewHolder viewHolder;
		for (int i = 0; i < imageCacheMid.size(); i++) {
			viewHolder = new ViewHolder();
			viewHolder.view = LayoutInflater
					.from(WelcomeViewPagerActivity.this).inflate(
							R.layout.framework_welcome_view, null);
			viewHolder.view.setBackgroundColor(backColor[i]);// 背景色
			viewHolder.imageView = (ImageView) viewHolder.view
					.findViewById(R.id.welcome_imageview);
			System.out.println("88888---------->" + i
					+ imageCacheMid.get(i).get());
			if (imageCacheMid.get(i).get() != null) {
				viewHolder.imageView.setImageBitmap(imageCacheMid.get(i).get());
			} else {
				imageCacheMid.put(0,
						getSoftBitmap2(R.drawable.framework_splash_1));
				imageCacheMid.put(1,
						getSoftBitmap2(R.drawable.framework_splash_2));
				imageCacheMid.put(2,
						getSoftBitmap2(R.drawable.framework_splash_3));
				imageCacheMid.put(3,
						getSoftBitmap2(R.drawable.framework_splash_4));
				viewHolder.imageView.setImageBitmap(imageCacheMid.get(i).get());
			}

			// 中间大图
			if (i != 0) {
				viewHolder.imageView.getDrawable().setAlpha(10);
				// 初始化默认透明度255
			}
			if (i == 0) {
				// 初始大图化动画
				alphaAnimation.setDuration(500);
				viewHolder.imageView.startAnimation(alphaAnimation);
			}
			viewHolder.imageView_bottom = (ImageView) viewHolder.view
					.findViewById(R.id.welcome_imageview1);
			if (imageCacheBottom.get(i).get() != null) {
				viewHolder.imageView_bottom.setImageBitmap(imageCacheBottom
						.get(i).get());
			} else {
				imageCacheBottom.put(0,
						getSoftBitmap(R.drawable.framework_splash_bottom));
				imageCacheBottom.put(1,
						getSoftBitmap(R.drawable.framework_splash_bottom));
				imageCacheBottom.put(2,
						getSoftBitmap(R.drawable.framework_splash_bottom));
				imageCacheBottom.put(3,
						getSoftBitmap(R.drawable.framework_splash_bottom));
				viewHolder.imageView_bottom.setImageBitmap(imageCacheBottom
						.get(i).get());
			}
			// logo
			viewHolder.textView1 = (ImageView) viewHolder.view
					.findViewById(R.id.welcome_text1);
			viewHolder.textView1.setBackgroundResource(imageResId_text[i][0]);// 第一行文字
			if (i != 0) {
				viewHolder.textView1.getBackground().setAlpha(0);
			}
			if (i == 0) {
				// 初始化文字动画
				alphaAnimation1.setDuration(500);
				alphaAnimation1.setInterpolator(WelcomeViewPagerActivity.this,
						android.R.anim.accelerate_decelerate_interpolator);
				alphaAnimation1.setFillAfter(true);
				viewHolder.textView1.startAnimation(alphaAnimation1);
			}
			// 判断有没有第二张图
			if (imageResId_text[i].length > 1) {
				viewHolder.textView2 = (ImageView) viewHolder.view
						.findViewById(R.id.welcome_text2);
				viewHolder.textView2
						.setBackgroundResource(imageResId_text[i][1]);// 第二行文字
				if (i != 0) {
					viewHolder.textView2.getBackground().setAlpha(0);// 初始化默认透明度255
				}
				if (i == 0) {
					// 第二行文字动画
					alphaAnimation2.setDuration(500);
					alphaAnimation2.setStartOffset(600);
					alphaAnimation2.setInterpolator(
							WelcomeViewPagerActivity.this,
							android.R.anim.accelerate_decelerate_interpolator);
					alphaAnimation2.setFillAfter(true);
					viewHolder.textView2.startAnimation(alphaAnimation2);
				}
			}

			imageViews.add(viewHolder);

		}
	}

	/*
	 * baidu推送
	 */
	private void startBaiduPush() {
		// 开启百度推送服务
		/*
		 * PushManager.startWork(getApplicationContext(),
		 * PushConstants.LOGIN_TYPE_API_KEY,
		 * Utils.getMetaValue(WelcomeViewPagerActivity.this, "api_key"));
		 */
		System.out.println("开启百度推送服务");
		// 开启百度推送debug模式
		// PushSettings.enableDebugMode(WelcomeViewPagerActivity.this, true);
		// Intent intent=new Intent(this, BaiduPushActivity.class);
		// intent.putExtra("url","www.baidu.com");
		// intent.putExtra("modelID","5");
		// System.out.println(intent.toURI());
		// }
	}

	private class ViewHolder {
		private ImageView imageView;
		private ImageView imageView_bottom;
		private ImageView textView1;
		private ImageView textView2;
		private View view;
	}

	private SoftReference<Bitmap> getSoftBitmap(int source) {
		Bitmap bitMap = CommonUtil.getBitMap(WelcomeViewPagerActivity.this,
				source);
		SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitMap);
		return softBitmap;
	}

	private SoftReference<Bitmap> getSoftBitmap2(int source) {
		Bitmap bitMap = CommonUtil.getBitMap2(WelcomeViewPagerActivity.this,
				source);
		SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitMap);
		return softBitmap;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return false;
	}
}