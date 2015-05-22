package com.stockp2p.framework.longin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.stockp2p.R;
import com.stockp2p.common.data.Constants;
import com.stockp2p.common.db.Content;
import com.stockp2p.framework.BaseFragmentActivity;

/**
 * 应备文件查询
 * @author haix
 *
 */
public class RequiredFileQueryActivity extends BaseFragmentActivity {

	private int channelId;
	/**
	 * WebView
	 */
	private WebView file_browser_wv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContainerView("应备文件查询", R.layout.required_file_query_file_browser);

		Bundle bundle = getIntent().getExtras();
		String flag = bundle.getString("flag");
		if ("5".equals(flag)) {
			setContainerView("网站协议", R.layout.required_file_query_file_browser);
		}
		if (null != bundle) {
			channelId = bundle.getInt("channelId");
		}

		file_browser_wv = (WebView) findViewById(R.id.file_browser_wv);
		TextView title_txt = (TextView) findViewById(R.id.base_activity_tv_titletxt);
		SQLiteDatabase db = myApplication.db;
		if ("1".equals(flag)) { // 保全业务
			if (Content.findByContentId(db, Constants.BAOQUANYINGBEI) != null)
				file_browser_wv.loadDataWithBaseURL(null, Content
						.findByContentId(db, Constants.BAOQUANYINGBEI)
						.getBody(), "text/html", "UTF-8", null);
		} else if ("2".equals(flag)) { // 理赔业务
			if (Content.findByContentId(db, Constants.LIPEIYINGBEI) != null)
				file_browser_wv.loadDataWithBaseURL(null, Content
						.findByContentId(db, Constants.LIPEIYINGBEI).getBody(),
						"text/html", "UTF-8", null);
		} else if ("3".equals(flag)) {
			String url = "";
			if (110 == channelId) {
				title_txt.setText("保单权益");
				url = Environment.getExternalStorageDirectory()
						+ "/nci/insuranceRights.html";
			} else if (111 == channelId) {
				title_txt.setText("红利分配");
				url = Environment.getExternalStorageDirectory()
						+ "/nci/dividendDistribution.html";
			} else if (112 == channelId) {
				title_txt.setText("保全手续");
				url = Environment.getExternalStorageDirectory()
						+ "/nci/securityProcedures.html";
			} else {
				title_txt.setText("理赔权益");
				url = Environment.getExternalStorageDirectory()
						+ "/nci/claim_rights.html";
			}
			String htmlData = Content.findByChannelId(db, channelId).getBody();
			writeStrToFile(url, htmlData);
			loadWebView(url);
		} else if ("5".equals(flag)) {
			if (Content.findByContentId(db, Constants.NEGOTIATE) != null)
				file_browser_wv.loadDataWithBaseURL(null, Content
						.findByContentId(db, Constants.NEGOTIATE).getBody(),
						"text/html", "UTF-8", null);
		} else if ("4".equals(flag)) {
			title_txt.setText("账户规则");
			if (Content.findByContentId(db, Constants.ACCRULE) != null)
				file_browser_wv.loadDataWithBaseURL(null, Content
						.findByContentId(db, Constants.ACCRULE).getBody(),
						"text/html", "UTF-8", null);
		}

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

	/**
	 * 加载网页
	 */
	private void loadWebView(String url) {
		try {
			url = "file://" + url;
			// 设置支持Javascript
			file_browser_wv.getSettings().setJavaScriptEnabled(true);
			file_browser_wv.getSettings().setCacheMode(
					WebSettings.LOAD_NO_CACHE);
			file_browser_wv.getSettings().setDefaultTextEncodingName("UTF-8");

			// WebSettings settings = webview.getSettings();
			// settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

			WebSettings ws = file_browser_wv.getSettings();
			ws.setUseWideViewPort(true);
			ws.setLoadWithOverviewMode(true);
			ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

			// file_browser_wv.getSettings().setSupportZoom(true);
			// file_browser_wv.getSettings().setBuiltInZoomControls(true);
			// 页面添加缩放按钮
			// 触摸焦点起作用，如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件
			file_browser_wv.requestFocus();
			// 加载网络页面
			file_browser_wv.loadUrl(url);
			file_browser_wv.setWebViewClient(new WebViewClient() {
				// 这个方法在用户试图点开页面上的某个链接时被调用
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// 设置点击网页里面的链接还是在当前的webview里跳转
					// view.loadUrl(url);
					return false;
				}

				@Override
				public void onReceivedSslError(WebView view,
						SslErrorHandler handler, android.net.http.SslError error) {
					// 设置webview处理https请求
					handler.proceed();
				}

				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {

				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}