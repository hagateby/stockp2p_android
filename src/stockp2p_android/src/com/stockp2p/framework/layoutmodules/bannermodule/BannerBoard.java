package com.stockp2p.framework.layoutmodules.bannermodule;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.stockp2p.R;
import com.stockp2p.common.data.MyApplication;
import com.stockp2p.common.db.FrameWork_Frame_DAO;
import com.stockp2p.common.db.FrameWork_LocalFileDesc;
import com.stockp2p.common.db.FrameWork_LocalFileDesc_DAO;
import com.stockp2p.common.db.FrameWork_Frame;
import com.stockp2p.common.util.SYSTEMCONST;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.common.util.PubFun;
import com.stockp2p.common.view.CommonDialog;

import com.stockp2p.framework.baseframe.BaseFragment;
import com.stockp2p.framework.baseframe.Manager;
import com.stockp2p.framework.layoutmodules.chkboardmodule.MenuColumn;

public class BannerBoard  extends BaseFragment {
	private static String TAG = "BannerBoard";
	protected static boolean flag = true;
	private BitmapDisplayConfig config;
	private com.lidroid.xutils.BitmapUtils bitmapUtils;
	private ViewPager viewPager;
	private String downurl;
	private ArrayList<FrameWork_LocalFileDesc> lstBannerImage;
	private ArrayList<View> dots;// 广告栏指示点
	private AdAdapter adAdapter;// 广告adapter
	private int oldPostion = 0;// 原先位置
	private int currentItem;// 当前页面
	private WebView webview;
	private ScheduledExecutorService scheduledExecutorService = Executors
			.newSingleThreadScheduledExecutor();
	private View thisView;
	private int imgecount; 

	/** 浮动按钮 **/


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
		thisView = inflater.inflate(R.layout.components_homepage_content_banner, null);

          
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
       
		
	    lstBannerImage = FrameWork_LocalFileDesc_DAO.findByFileCode(myApplication.db,SYSTEMCONST.LOCALFILEDESC_GROUPCODE_BANNER);
		
		imgecount =lstBannerImage.size();
		// 广告栏
		viewPager = (ViewPager) thisView.findViewById(R.id.homepage_view_pager);

		init(thisView, "首页");

		return thisView;
	}

	@Override
	public void init(View view, String title) {
		// TODO Auto-generated method stub
		super.init(view, title);

		//  pubfun.printFrames(TAG,frameworks);

		// 广告栏图片上的点
		setDot();

		//
		creatadAdapter();

		autoScroll(); // 广告条自动、手动滚动
		/* lwh
		thisManager.beginTransaction()
				.replace(R.id.homepage_rl_menucontainer, new MenuColumn())
				.commit();
				
		*/		
	}

	// 广告栏图片上的点
	private void setDot() {
		// 热点
		LinearLayout dot_layout = (LinearLayout) thisView
				.findViewById(R.id.homepage_dot_layout);
		
		dots = new ArrayList<View>();
		
		if ((lstBannerImage.size() > 0)) {
			for (int i = 0; (i < lstBannerImage.size()); i++) {
				
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
		// 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
		@Override
		public int getCount() {
			return imgecount;
		}

		// 判断View和Object是否是同一对象
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (ImageView) object;
		}
		// PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}

		// 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
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
				
					TipUitls.Log(TAG, "clickurl--------"
			 				+ lstBannerImage.get(position).getClickAdress());

				 //	Manager.branch(context, (Framework)pubfun.toClassbyName(lstBannerImage.get(position).getRemark1()));
                
				}
			});
			// 本地广告栏图片

			String iconname = lstBannerImage.get(position).getFileName();
			
			TipUitls.Log(TAG, "lstBannerImage.get(position).getImageName()--->"
					+  lstBannerImage.get(position).getFileName());
	
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
	private void autoScroll() {
		// 广告条滚动定时器
		scheduledExecutorService.scheduleAtFixedRate(new AdTask(), 5, 2,
				TimeUnit.SECONDS);
	}
	private final class AdTask implements Runnable {
		public void run() {
			if (flag) {
				currentItem = (currentItem + 1) % lstBannerImage.size();
				Message msg = new Message();
				msg.what = 0;
				msg.getData().putLong("currentItem", currentItem);
				AdHandler.sendMessage(msg);
			} else {
				flag = true;
			}
		}
	}


}
