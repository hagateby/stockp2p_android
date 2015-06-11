package com.stockp2p.components.homepage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.stockp2p.R;
import com.stockp2p.common.data.Framework;
import com.stockp2p.common.data.MyApplication;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.common.util.PubFun;
import com.stockp2p.common.view.CommonDialog;
import com.stockp2p.framework.Frameworkdate;
import com.stockp2p.framework.WebviewActivity;
import com.stockp2p.framework.baseframe.BaseFragment;
import com.stockp2p.framework.baseframe.Manager;
import com.stockp2p.framework.layoutmodules.chkboardmodule.MenuColumn;

/**
 * 广告栏
 * 
 * @author haix
 * 
 */
public class BannerBoards extends BaseFragment {

	private static String TAG = "BannerBoards";
	protected static boolean flag = true;
	private BitmapDisplayConfig config;
	private com.lidroid.xutils.BitmapUtils bitmapUtils;
	private ViewPager viewPager;
	private String downurl;
	private ArrayList<View> dots;// 广告栏指示点
	private AdAdapter adAdapter;// 广告adapter
	private int oldPostion = 0;// 原先位置
	private int currentItem;// 当前页面
	private WebView webview;
	private ScheduledExecutorService scheduledExecutorService = Executors
			.newSingleThreadScheduledExecutor();
	private View thisView;

	/** 浮动按钮 **/

	private ArrayList<Framework> frameworks;

	Handler AdHandler = new Handler() {

		private CommonDialog commonDialog;

		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				viewPager.setCurrentItem(currentItem);
				break;
			case 1:
				commonDialog = new CommonDialog(context, 2, "参加", "不参加",
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								commonDialog.dismiss();
								/*
								 * Intent intent = new Intent(getActivity(),
								 * ShakeprizeActivity.class);
								 * intent.putExtra("ActivityId", activityId);
								 * intent.putExtra("ActivityUrl", activityUrl);
								 * startActivity(intent);
								 */
							}
						}, null, "提示", msg.obj.toString());
				commonDialog.show();
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.homepage_content, null);


		myApplication.setScreen_width(PubFun.getWindowWidth(context));
		
		myApplication.setScreen_height(PubFun.getWindowHeight(context));

		if (bitmapUtils == null) {
			bitmapUtils = ((MyApplication) getActivity().getApplication())
					.getBitmapUtils(getActivity());
		}
		if (config == null) {
			config = new BitmapDisplayConfig();

			config.setLoadFailedDrawable(getResources().getDrawable(
					R.drawable.defaultshowimage));

			config.setLoadingDrawable(getResources().getDrawable(
					R.drawable.defaultshowimage));
		}

		// 广告栏
		viewPager = (ViewPager) thisView.findViewById(R.id.view_pager);

		init(thisView, "首页");

		// 加载WEBVIEW

		thisManager.beginTransaction()
				.replace(R.id.home_page_rl_menucontainer, new ContentWebView())
				.commit();

		return thisView;
	}

	@Override
	public void init(View view, String title) {
		// TODO Auto-generated method stub
		super.init(view, title);

		// webhtml
		frameworks = (ArrayList<Framework>) Frameworkdate.findByParentId(
				myApplication.db, "999", context);

		if (frameworks.size() == 0) {
			frameworks.add(new Framework());
		}

		  PubFun.printFrames(TAG,frameworks);

		// 查询图片地址
		//downurl = pubfun.getimageDownloadUrl();

		// 广告栏图片上的点
		setDot();

		//
		creatadAdapter();

		autoScroll(); // 广告条自动、手动滚动
		// 添加菜单
		thisManager.beginTransaction()
				.replace(R.id.home_page_rl_menucontainer, new MenuColumn())
				.commit();
	}

	// 广告栏图片上的点
	private void setDot() {
		// 热点
		LinearLayout dot_layout = (LinearLayout) thisView
				.findViewById(R.id.dot_layout);
		
		dots = new ArrayList<View>();
		
		if ((frameworks.size() > 0)) {
			for (int i = 0; (i < frameworks.size()); i++) {
				View ad_images_dot_item = View.inflate(getActivity(),
						R.layout.quicksrv_ad_images_dot_item, null);
		
				View ad_dot = ad_images_dot_item.findViewById(R.id.ad_dot);
				
				dot_layout.addView(ad_images_dot_item);
				
				dots.add(ad_dot);
			}
			
			dots.get(0).setBackgroundResource(R.drawable.framework_dot_foc);
		}

	}

	private void creatadAdapter() {
		adAdapter = new AdAdapter();
		viewPager.setAdapter(adAdapter);

		viewPager.setOffscreenPageLimit(3);

		viewPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				flag = false;
				return false;
			}
		});
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				dots.get(position).setBackgroundResource(
						R.drawable.framework_dot_foc);
				dots.get(oldPostion).setBackgroundResource(
						R.drawable.quicksrv_dot_nor);
				currentItem = position;
				oldPostion = position;

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	// 广告adapter
	private final class AdAdapter extends PagerAdapter {
		public AdAdapter() {
		}

		@Override
		public int getCount() {
			return frameworks.size();
		}

		// 判断View和Object是否是同一对象
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (ImageView) object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}

		// 向viewPager添加一张图片
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			ImageView mImageView = new ImageView(context);

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);

			mImageView.setLayoutParams(layoutParams);
			mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
			mImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e(TAG, "clickurl--------"
							+ frameworks.get(position).getClickUrl());

					Manager.branch(context, frameworks.get(position));

				}
			});
			// 本地广告栏图片

			String iconname = frameworks.get(position).getIconName();
			TipUitls.Log(TAG, "frameworks.get(position).getIconName()--->"
					+ frameworks.get(position).getIconName());
	/*		
			String uri = null;
			// 先从assets看加载，再从内存卡中加载，最后从网络下载
			InputStream inputStream = null;
			try {
				inputStream = getResources().getAssets().open(
						frameworks.get(position).getIconName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (inputStream != null) {

				// Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inTempStorage = new byte[100 * 1024];
				opts.inPreferredConfig = Bitmap.Config.RGB_565;
				opts.inPurgeable = true;
				opts.inSampleSize = 2;
				opts.inInputShareable = true;
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null,
						opts);
				if (bitmap != null) {
					mImageView.setImageBitmap(bitmap);
				}

			} else {
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/nci/" + frameworks.get(position).getIconName());
				if (file.exists()) {// 如果本地存在
					uri = Uri.fromFile(file).toString();
				} else {// 如果本地没有,去网络加载
					uri = downurl + frameworks.get(position).getIconName();
					bitmapUtils.display(mImageView, uri, config);
				}
			}

	*/		
   
			String uri = null;
			// 先从assets看加载，再从内存卡中加载，最后从网络下载

			Bitmap bitmap = PubFun.loadfilebyName(getActivity(), iconname);

			if (bitmap != null) {
				mImageView.setImageBitmap(bitmap);
			} else {

				uri = PubFun.getlocalFilebyName(iconname);
				bitmapUtils.display(mImageView, uri, config);
			}
	
			container.addView(mImageView, 0);
			return mImageView;
		
		}
	}

	private final class AdTask implements Runnable {
		public void run() {
			if (flag) {
				currentItem = (currentItem + 1) % frameworks.size();
				Message msg = new Message();
				msg.what = 0;
				msg.getData().putLong("currentItem", currentItem);
				AdHandler.sendMessage(msg);
			} else {
				flag = true;
			}
		}
	}

	private void autoScroll() {
		// 广告条滚动定时器
		scheduledExecutorService.scheduleAtFixedRate(new AdTask(), 5, 2,
				TimeUnit.SECONDS);
	}

}
