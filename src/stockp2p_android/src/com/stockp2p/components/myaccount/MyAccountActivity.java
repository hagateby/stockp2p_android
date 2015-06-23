package com.stockp2p.components.myaccount;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.stockp2p.R;
import com.stockp2p.common.cache.SertificatonInfor;
import com.stockp2p.common.cache.SertificatonInfor.ErrorCallBack;
import com.stockp2p.common.cache.UserInfoManager;
import com.stockp2p.common.util.NetUtil;
import com.stockp2p.common.view.CommonDialog;
import com.stockp2p.components.login.LoginActicity;
import com.stockp2p.framework.ExitApplication;
import com.stockp2p.framework.baseframe.BaseFragmentActivity;
import com.stockp2p.framework.layoutmodules.chkboardmodule.QuicksrvActivity;

/**
 * 我的账户
 * 
 * @author haix
 * 
 */
public class MyAccountActivity extends BaseFragmentActivity {

	private SertificatonInfor sertiInfor;

	Handler handler = new Handler() {

		private CommonDialog commonDialog;

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:

				// 加载页面
				if (sertiInfor != null) {
					// 加载 fragment 前先移除默认界面
					FrameLayout container = (FrameLayout) findViewById(R.id.tab_container);
					container.removeAllViews();

					if (sertiInfor.sertificationStatus()) {
						// 已认证
						thisManager
								.beginTransaction()
								.replace(R.id.tab_container,
										new Verified(sertiInfor)).commit();
					} else if (!sertiInfor.sertificationStatus()) {
						// 未认证
						thisManager
								.beginTransaction()
								.replace(R.id.tab_container,
										new NoVerify(sertiInfor)).commit();
					}

				}
				break;
			case 1:
				pd.dismiss();
				commonDialog = new CommonDialog(context, 1, "确定", null,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								commonDialog.dismiss();
								finish();
								Intent intent = new Intent(context,
										QuicksrvActivity.class);
								startActivity(intent);
							}
						}, null, "提示", msg.obj.toString());
				commonDialog.show();
				break;
			case 99:
				pd.dismiss();
				commonDialog = new CommonDialog(context, 1, "确定", null,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								commonDialog.dismiss();
								UserInfoManager.getInstance().exitLogin();
								ExitApplication.getInstance().exitLogin(context);
								context.startActivity(new Intent(context,
										LoginActicity.class).putExtra("isExit",
										"true"));
							}
						}, null, "提示", msg.obj.toString());
				commonDialog.show();
				break;

			default:
				break;
			}
		}

	};
	private ProgressDialog pd;
	private CommonDialog commonDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 默认界面
		setContainerView("我的账户", R.layout.myaccount_notrele);

		// List<Framework> list = Frameworkdate.findByParentId(myApplication.db,
		// "0", context);
		// // 点击底栏后记录底栏显示的位置
		// Constants.staticFramework = list.get(2);
		// 隐藏更多
		context.findViewById(R.id.base_activity_bt_base_title_more)
				.setVisibility(View.GONE);
		((Button) findViewById(R.id.base_activity_bt_titleback))
				.setCompoundDrawables(null, null, null, null);

		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("onResume");
		init();
	}

	private void init() {
		// 每次进入此界面都要刷新数据
		SertificatonInfor.clearSertificationInfor();
		if (NetUtil.hasNetWork(context)) {
			// 获取认证信息
			pd = new ProgressDialog(context);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("正在请求服务器...");
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					sertiInfor = SertificatonInfor.getInstance(context,
							new ErrorCallBack() {

								@Override
								public void onFailure(int i, String resultMsg) {
									// TODO Auto-generated method stub
									System.out.println("onFailure");
									Message message = Message.obtain();
									message.obj = resultMsg;
									message.what = i;
									handler.sendMessage(message);
								}
							});
					try {
						System.out.println("dismiss");
						pd.dismiss();
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("Exception");
					}
					handler.sendEmptyMessage(0);

				}
			}).start();
		} else {
			commonDialog = new CommonDialog(context, 2, "确定", "取消",
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							commonDialog.dismiss();
							Intent intent = null;
							// 判断手机系统的版本 即API大于10 就是3.0或以上版本
							if (android.os.Build.VERSION.SDK_INT > 10) {
								intent = new Intent(
										android.provider.Settings.ACTION_WIRELESS_SETTINGS);
							} else {
								intent = new Intent();
								ComponentName component = new ComponentName(
										"com.android.settings",
										"com.android.settings.WirelessSettings");
								intent.setComponent(component);
								intent.setAction("android.intent.action.VIEW");
							}
							context.startActivity(intent);

						}
					}, new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							commonDialog.dismiss();
							context.finish();
							context.startActivity(new Intent(context,
									QuicksrvActivity.class));
						}
					}, "网络设置提示", "网络连接不可用,是否进行设置?");
			commonDialog.show();

		}

	}
}
