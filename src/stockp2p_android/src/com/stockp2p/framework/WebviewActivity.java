package com.stockp2p.framework;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.stockp2p.R;
import com.stockp2p.common.data.Framework;
import com.stockp2p.common.view.CommonDialog;
import com.stockp2p.framework.baseframe.BaseFragmentActivity;

/**
 * 所有 Web页面  
 * 
 * @author haix
 * 
 */
public class WebviewActivity extends BaseFragmentActivity {
	private WebView myWebView;

	private CommonDialog commonDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContainerView("新华保险", R.layout.framework_webview);
		String url = null ;
		if (getIntent() != null) {
			url= getIntent().getExtras().getString("url");
		}
		init(url);
	}

	public void init(String url) {
		if (framework_!=null) {
			url = framework_.getClickUrl();
		}

//		url="http://www.newchinalife.com/jsp/phoneProductCenter/showInfo.jsp?path=/%E4%BA%A7%E5%93%81%E4%B8%AD%E5%BF%83/%E4%B8%AA%E4%BA%BA%E4%BF%9D%E9%99%A9/%E7%90%86%E8%B4%A2%E4%BF%9D%E9%99%A9/%E9%87%91%E5%BD%A9%E4%B8%80%E7%94%9F%E7%BB%88%E8%BA%AB%E5%B9%B4%E9%87%91%E4%BF%9D%E9%99%A9%E8%AE%A1%E5%88%92";
		if (url != null && url.length() > 0) {
			myWebView = (WebView) findViewById(R.id.webview);
			//设置WebView宽度自适应
//			myWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			
			myWebView.getSettings().setJavaScriptEnabled(true);
			myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(
					true);
			System.out.println("url="+url);
			myWebView.loadUrl(url);
			myWebView.requestFocus();
			myWebView.getSettings().setSupportZoom(true);
			myWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
			myWebView.getSettings().setBuiltInZoomControls(true);

			myWebView.setWebChromeClient(new WebChromeClient() {
				@Override
				public boolean onJsAlert(WebView view, String url,
						String message, final JsResult result) {

					commonDialog= new CommonDialog(context, 1, "确定", null,
							new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									 result.confirm();
									 commonDialog.dismiss();
								}
							}, null, "提示", message);
					commonDialog.show();
					
					return true;
				}
			});

			myWebView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					//解决产品专区页内链接进不去
					view.loadUrl(url);
					return true;
				}

				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);

				}
			});
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 解决网页音乐不关闭问题
		myWebView.loadUrl("about:blank");
	}

}