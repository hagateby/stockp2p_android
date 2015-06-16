package com.stockp2p.components.homepage;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.stockp2p.R;
import com.stockp2p.common.data.MyApplication;
import com.stockp2p.common.db.FrameWork_Frame;
import com.stockp2p.common.view.CommonDialog;
import com.stockp2p.framework.baseframe.BaseFragment;
import com.stockp2p.framework.layoutmodules.bannermodule.BannerBoard;

public class HomePageArticle  extends BaseFragment {

	private static String TAG = "HomePageArticle";


	private View thisView;
	
	private View bannerView;
	
	private View contentView;

	/** 浮动按钮 **/

	private ArrayList<FrameWork_Frame> frameworks;

	Handler AdHandler = new Handler() {

		private CommonDialog commonDialog;

		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
	
				break;
			case 1:
				commonDialog = new CommonDialog(context, 2, "参加", "不参加",
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								commonDialog.dismiss();
							
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
		
		thisView = inflater.inflate(R.layout.components_homepage_content, null);

		thisManager.beginTransaction()
			     // 广告栏
				.replace(R.id.homepage_content_banner_frame, new BannerBoard())
				// 加载WEBVIEW
				.replace(R.id.homepage_content_article_frame, new ContentWebView())		
				.commit();

		return thisView;
	}

	
}
