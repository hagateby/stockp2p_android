package com.ktsf.framework;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stockp2p.R;
import com.ktsf.common.cache.UserInfoManager;
import com.ktsf.common.data.MyApplication;
import com.ktsf.common.data.MySetting;
import com.ktsf.common.data.UpdateDownloadManger;
import com.ktsf.common.db.Version;

import com.ktsf.common.serviceengin.Des3;
import com.ktsf.common.serviceengin.EnginCallback;
import com.ktsf.common.serviceengin.ServiceEngin;
import com.ktsf.common.util.CommonUtil;
import com.ktsf.common.util.ExitApplication;
import com.ktsf.common.util.TipUitls;
import com.ktsf.common.view.CommonDialog;
import com.ktsf.common.view.SlipButton;
import com.ktsf.common.view.SlipButtonOnChangedListener;
import com.ktsf.framework.launch.WelcomeViewPagerActivity;
import com.ktsf.framework.BaseFragment;
import com.ktsf.framework.Framework;
import com.ktsf.framework.longin.LoginActicity;
import com.ktsf.framework.Manager;

/**
 * 设置
 * 
 * @author haix
 * 
 */
@SuppressLint("ValidFragment")
public class SetFragment extends BaseFragment {

	private static final String TAG = "SetFragment";
	@ViewInject(R.id.ll_personalInfo)
	private RelativeLayout ll_personalInfo;
	@ViewInject(R.id.ll_modifyPassword)
	private RelativeLayout ll_modifyPassword;
	@ViewInject(R.id.ll_feedback)
	private RelativeLayout ll_feedback;
	@ViewInject(R.id.ll_twoDimensionCode)
	private RelativeLayout ll_twoDimensionCode;
	@ViewInject(R.id.ll_about)
	private RelativeLayout ll_about;
	@ViewInject(R.id.rl_check_update)
	private RelativeLayout rl_check_update;
	@ViewInject(R.id.ll_welcome_page)
	private RelativeLayout ll_welcome_page;
	@ViewInject(R.id.button_exit)
	private Button button_exit;
	@ViewInject(R.id.slipButton2)
	private SlipButton slipButton2;

	private SharedPreferences sp;

	// ------------------------
	@ViewInject(R.id.tv_settting_username)
	private TextView userName;

	private String updatedFlag;
	private String uploadUrl;
	private String versionCode;
	private String versionDesc;

	private MyApplication myApplication;
	boolean login;
	private View thisView;
	private Framework framework;

	public SetFragment(Framework framework) {
		this.framework = framework;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.set_settings, null);
		ViewUtils.inject(thisView);

		init(thisView, "设置");

		setMainContentField();

		addListenrs();
		return thisView;
	}

	@Override
	protected void init(View view, String title) {
		// TODO Auto-generated method stub
		super.init(view, title);

		login = UserInfoManager.getInstance().isLogin();
		myApplication = (MyApplication) context.getApplication();

		userName.setText(UserInfoManager.getInstance().getUserName());

		if (login) {
			button_exit.setBackgroundResource(R.drawable.set_btn_red_active);
			button_exit.setText("退出登录");

		} else {
			button_exit.setBackgroundResource(R.drawable.common_btn_selector);
			button_exit.setText("未登录");
		}

	}

	private void addListenrs() {

		// 个人信息
		ll_personalInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean login = UserInfoManager.getInstance().isLogin();
				if (login) {
					// 已经登录
					// Intent intent = new Intent(context,
					// Setting_PersonalInfoActivity.class);
					// startActivityForResult(intent, 1);
					thisManager
							.beginTransaction()
							.replace(R.id.tab_container,
									new Setting_PersonalInfoActivity())
							.addToBackStack(null).commit();
				} else {
					// 进入登录界面
					Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// 修改密码
		ll_modifyPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean login = UserInfoManager.getInstance().isLogin();
				if (login) {
					// 已经登录
					// Intent intent = new Intent(getActivity(),
					// Setting_ModifyPasswordActivity.class);
					// startActivity(intent);
					thisManager
							.beginTransaction()
							.replace(R.id.tab_container,
									new Setting_ModifyPasswordActivity())
							.addToBackStack(null).commit();
				} else {
					// 进入登录界面
					Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
				}

			}
		});

		// 意见反馈
		ll_feedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Intent intent = new Intent(getActivity(),
				// Setting_FeedbackActivity.class);
				// startActivity(intent);
				thisManager
						.beginTransaction()
						.replace(R.id.tab_container,
								new Setting_FeedbackActivity())
						.addToBackStack(null).commit();
			}
		});

		// 二维码
		ll_twoDimensionCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				thisManager
						.beginTransaction()
						.replace(R.id.tab_container,
								new Setting_TwoDimensionCodeActivity())
						.addToBackStack(null).commit();
			}
		});

		// 检测新版本
		rl_check_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rl_check_update.setEnabled(false);
				requestServerForUpdate();
			}
		});

		// 关于掌上新华
		ll_about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(getActivity(),
				// Setting_AboutActivity.class);
				// startActivity(intent);
				thisManager
						.beginTransaction()
						.replace(R.id.tab_container,
								new Setting_AboutActivity())
						.addToBackStack(null).commit();
			}
		});

		// 欢迎页面
		ll_welcome_page.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						WelcomeViewPagerActivity.class);
				intent.putExtra("source", "setting");
				startActivity(intent);
			}
		});

		button_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (login) {
					submitDialog();

				} else {
					exitLogin();
				}

			}
		});

	}

	public void submitDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage("是否退出？");
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				UserInfoManager.getInstance().exitLogin();
				ExitApplication.getInstance().exitLoginSet(context);
				userName.setText("");
				button_exit
						.setBackgroundResource(R.drawable.common_btn_selector);
				button_exit.setText("请登录");
				login = false;
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	/**
	 * 登录
	 */
	private void exitLogin() {

		boolean exit = UserInfoManager.getInstance().exitLogin();
		if (exit) {
			Intent intent = new Intent(context, LoginActicity.class);
			intent.putExtra("framework", framework);
			intent.putExtra("isExit", "false");
			startActivity(intent);

		}
	}

	private void setMainContentField() {

		SharedPreferences sp = context.getSharedPreferences("userprefs",
				Context.MODE_PRIVATE);
		String updateDateHere = sp.getString("updateDate",
				"2013-07-04%2002:17:35").replace("%20", " ");

		boolean sb1 = MySetting.getSlipButton1Boolean(context);
		boolean sb2 = MySetting.getSlipButton2Boolean(context);
		slipButton2.setChecked(sb2);
		slipButton2.SetOnChangedListener(null,
				new SlipButtonOnChangedListener() {
					@Override
					public void OnChanged(String strName, boolean CheckState) {
						MySetting.putSlipButton2Boolean(context, CheckState);
						if (CheckState) {
							
						}
					}
				});
	}

	/**
	 * 检测新版本
	 */
	private void requestServerForUpdate() {
		String versionName = null;
		try {
			versionName = CommonUtil.getVersionName(context);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "获取当前版本信息失败", Toast.LENGTH_SHORT).show();
			return;
		}

		Map map = new HashMap<String, String>();
		map.put("versionCode", versionName);
		map.put("versionType", "1");
		String servicePara = JSON.toJSONString(map);
		TipUitls.Log(TAG, "servicePara----->" + servicePara);
		ServiceEngin.Request(context, "05_I01", "checkVersion", servicePara,
				new EnginCallback(context) {

					private CommonDialog commonDialog;

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
						commonDialog = new CommonDialog(context, 1, "确定", null,
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										commonDialog.dismiss();
										rl_check_update.setEnabled(true);
									}
								}, null, "提示", getResources().getString(
										R.string.request_failed));
						commonDialog.show();
						rl_check_update.setEnabled(true);
					}

					public void onSuccess(ResponseInfo arg0) {
						super.onSuccess(arg0);
						String result = null;
						try {
							result = Des3.decode(arg0.result.toString());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						TipUitls.Log(TAG, "result----->" + result);
						if (result != null && JSON.parseObject(result) != null) {
							JSONObject jsonObject = JSON.parseObject(result);
							String ResultMsg = (String) jsonObject
									.get("ResultMsg");
							String ResultCode = (String) jsonObject
									.get("ResultCode");

							if ("0".equals(ResultCode)) {
								String object = jsonObject.getString("Version");
								Version version = new Version();
								version = JSON.parseObject(object,
										Version.class);

								updatedFlag = version.getOpeFlag();

								TipUitls.Log(TAG, "设置中的updatedFlag------>"
										+ updatedFlag);

								if ("1".equals(updatedFlag)) {
									rl_check_update.setEnabled(true);
									new UpdateDownloadManger(context)
											.update(version);
								} else {
									commonDialog = new CommonDialog(context, 1,
											"确定", null, new OnClickListener() {

												@Override
												public void onClick(View v) {
													commonDialog.dismiss();
												}
											}, null, "提示", "已经是最新版本");
									commonDialog.show();
									rl_check_update.setEnabled(true);

								}

							} else if (!"99".equals(ResultCode)) {

								commonDialog = new CommonDialog(context, 1,
										"确定", null, new OnClickListener() {

											@Override
											public void onClick(View v) {
												commonDialog.dismiss();
												thisManager
														.popBackStackImmediate();
											}
										}, null, "提示", ResultMsg);
								commonDialog.show();
							}

						}

						rl_check_update.setEnabled(true);
					}

				});

	}

}
