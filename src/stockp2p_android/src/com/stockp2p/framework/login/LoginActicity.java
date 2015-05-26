package com.stockp2p.framework.login;

import java.util.HashMap;
import java.util.Map;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import com.stockp2p.R;
import com.stockp2p.common.cache.UserInfoManager;
import com.stockp2p.common.data.Constants;
import com.stockp2p.common.serviceengin.Des3;
import com.stockp2p.common.serviceengin.EnginCallback;
import com.stockp2p.common.serviceengin.ServiceEngin;
import com.stockp2p.common.util.CommonUtil;
import com.stockp2p.common.util.ExitApplication;
import com.stockp2p.common.util.MD5Util;
import com.stockp2p.common.util.NetUtil;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.common.view.ClearEditText;
import com.stockp2p.common.view.CommonDialog;
import com.stockp2p.framework.BaseFragmentActivity;
import com.stockp2p.framework.Manager;

/**
 * 登录
 * 
 * @author haix
 * 
 */
// @ContentView(value = R.layout.framework_login)

public class LoginActicity extends BaseFragmentActivity {

	private static String TAG = "LoginActicity";
	private final String Phone = "1";
	private final String UserName = "0";
	private String LoginWay = UserName;
	/**
	 * 电话输入 layout
	 */
	@ViewInject(R.id.login_ll_accountphone)
	private LinearLayout layoutPhone;
	/**
	 * 用户名输入 layout
	 */
	@ViewInject(R.id.login_ll_accountuser)
	private LinearLayout layoutUser;
	/**
	 * 登录按钮
	 */
	@ViewInject(R.id.login_bt_login)
	private Button login;
	/**
	 * 注册
	 */
	@ViewInject(R.id.login_bt_resgister)
	private Button resgister;
	/**
	 * 用户名输入
	 */
	@ViewInject(R.id.login_et_accountuser)
	private ClearEditText inputAccount;
	/**
	 * 手机输入
	 */
	@ViewInject(R.id.login_et_accountphone)
	private ClearEditText inputPhone;
	/**
	 * 密码输入
	 */
	@ViewInject(R.id.login_et_password)
	private EditText inputPassword;
	/**
	 * 忘记密码
	 */
	@ViewInject(R.id.login_bt_forgetpassword)
	private Button forgetPassword;
	/**
	 * 账户输入方式
	 */
	@ViewInject(R.id.login_bt_user)
	private Button accountPage;
	/**
	 * 电话输入方式
	 */
	@ViewInject(R.id.login_bt_tel)
	private Button telPage;
	/**
	 * 眼睛
	 */
	@ViewInject(R.id.login_cb_checkbox)
	private CheckBox eye;
	/**
	 * 最下方提示文字
	 */
	@ViewInject(R.id.login_tv_info)
	private TextView mInfor;
	private SharedPreferences sp;
	private View thisView;

	// 提交的账户和密码
	private String loginId_account;
	private String loginId_phone;
	private String passwordDigest;
	private CommonDialog commonDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContainerView("登录", R.layout.framework_login);
		ViewUtils.inject(this);

		init();

		setListener();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		// 注册跳回时用
		if (intent.getStringExtra("userPassword") != null) {
			// 注册后直接登录
			loginId_account = intent.getStringExtra("userName");
			loginId_phone = intent.getStringExtra("phone");
			String password = intent.getStringExtra("userPassword");
			passwordDigest = MD5Util.MD5(password).toUpperCase();
			// init();
			login();
		}
	}

	public void init() {
		// TODO Auto-generated method stub

		// 帐号字体设置 要和密码设置的字体一样
		inputAccount.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		inputAccount.setSelection(inputPassword.getText().toString().trim()
				.length());
		inputAccount.setTextColor(Color.parseColor("#666666"));
		/*
		 * mInfor.setText(Html .fromHtml(
		 * "<font color=red>温馨提示 : </font><font color=grey>网站、掌上新华注册用户在网站、掌上新华均可直接登录使用</font>"
		 * ));
		 */
		sp = context.getSharedPreferences("userprefs", Context.MODE_PRIVATE);
		String loginId_account = sp.getString("loginId_account", null);
		String loginId_phone = sp.getString("loginId_phone", null);

		if (loginId_account != null) {
			inputAccount.setText(loginId_account);
		}
		if (loginId_phone != null) {
			inputPhone.setText(loginId_phone);
		}

	}

	/**
	 * 设置监听器
	 */
	private void setListener() {

		// 登录
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkAccount() && checkPassword()) {
					getInputContent();

					login();
				}
			}
		});

		// 注册
		resgister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转页面
				Intent intent = new Intent(context, RegisterActivity.class);
				intent.putExtra("framework", framework_);
				startActivity(intent);
			}
		});

		// 忘记密码
		forgetPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,
						ForgetPassWordActivity.class);
				intent.putExtra("loginPass", inputAccount.getText().toString()
						.trim());
				startActivity(intent);
			}
		});

		// 用户名登录方式
		accountPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginWay = UserName;
				accountPage
						.setBackgroundResource(R.drawable.framework_login_account_way_on);

				telPage.setBackgroundResource(R.drawable.framework_login_tel_way);

				layoutPhone.setVisibility(View.GONE);

				layoutUser.setVisibility(View.VISIBLE);
			}
		});

	}

	/**
	 * 
	 */
	private void getInputContent() {
		loginId_account = inputAccount.getText().toString().trim();
		loginId_phone = inputPhone.getText().toString().trim();
		String password = inputPassword.getText().toString().trim();
		passwordDigest = MD5Util.MD5(password).toUpperCase();
	}

	/**
	 * 
	 */
	private void login() {
		if (UserName.equals(LoginWay)) {
			// 点击提交后就会把用户名记录
			sp.edit().putString("loginId_account", loginId_account).commit();
			Map map = new HashMap<String, String>();
			map.put("loginId", loginId_account);
			map.put("loginType", "u");
			map.put("password", passwordDigest);
			String mapString = JSON.toJSONString(map);
			// lwh loginRequest(mapString);
			testlogin();

		} else if (Phone.equals(LoginWay)) {
			// 点击提交后就会把手机号记录
			sp.edit().putString("loginId_phone", loginId_phone).commit();
			Map map = new HashMap<String, String>();
			map.put("loginId", loginId_phone);
			map.put("loginType", "m");
			map.put("password", passwordDigest);
			String mapString = JSON.toJSONString(map);
			// lwh loginRequest(mapString);
			testlogin();
		}

	}

	private void loginRequest(String str) {

		if (NetUtil.hasNetWork(context)) {

			ServiceEngin.Request(context, "00_00_I01", "login", str,
					new EnginCallback(context) {

						@Override
						public void onSuccess(ResponseInfo arg0) {
							// TODO Auto-generated method stub
							super.onSuccess(arg0);
							String result = null;
							try {
								result = Des3.decode(arg0.result.toString());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							TipUitls.Log(TAG, "result---->" + result);
							if (result != null
									&& JSONObject.parseObject(result) != null) {
								JSONObject jsonObject = JSONObject
										.parseObject(result);
								JSONObject user = (JSONObject) jsonObject
										.get("User");
								String resultMsg = (String) jsonObject
										.get("ResultMsg");
								String resultCode = (String) jsonObject
										.get("ResultCode");

								if ("0".equals(resultCode)) {
									// 先清空单例再登录
									UserInfoManager.getInstance().exitLogin();
									// 取数据
									setUserInfo(user, resultCode, resultMsg);

									TipUitls.Log(TAG, "framework_----->"
											+ framework_
											+ "staticFramework----->"
											+ Constants.staticFramework);
									if (framework_ == null) {
										framework_ = Constants.staticFramework;
										
										// 当用户被挤掉后,重新登录后,bottom 显示的位置

									} else {
										Constants.staticFramework = framework_;
									}

									ExitApplication.getInstance().exitLogin(
											context);
									Manager.branch(context, framework_);

								} else {
									new CommonDialog(context, 1, "确定", null,
											null, null, "登录失败", resultMsg)
											.show();
								}
							}

						}
					});
		} else {
			commonDialog = new CommonDialog(context, 2, "确定", "取消",
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
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
						}
					}, "网络设置提示", "网络连接不可用,是否进行设置?");
			commonDialog.show();
		}

	}

	private void testlogin() {

		TipUitls.Log(TAG, "framework_----->" + framework_
				+ "staticFramework----->" + Constants.staticFramework);
		if (framework_ == null) {
			if (Constants.staticFramework ==null)
			{
			  
			}
			framework_ = Constants.staticFramework;
			// 当用户被挤掉后,重新登录后,bottom 显示的位置

		} else {
			Constants.staticFramework = framework_;
		}

		ExitApplication.getInstance().exitLogin(context);
		
		Manager.branch(context, Constants.moduleList.get(0));

	}
	
	
	private void setPaySdk(JSONObject user) {

		// 支付控件请求头,调用支付控件前必须先初始化 共有两次初始化,另一个在我的账户
		/*
		 * SdkRequestHeader header = new SdkRequestHeader(); header.extSysID =
		 * "049"; header.extSysUserName = user .getString("cid");
		 * header.extSysVersion = "1.0"; header.sessionMAC = "";// 在我的账户初始化
		 * header.sessionToken = user .getString("sessionId");
		 * PaySdk.init(context, header);
		 */
	}

	private void setUserInfo(JSONObject user, String resultCode,
			String resultMsg) {

		UserInfoManager userInfo = UserInfoManager.getInstance();
		userInfo.setResultCode(resultCode);
		userInfo.setResultMsg(resultMsg);
		userInfo.setCid(user.getString("cid"));
		userInfo.setSessionId(user.getString("sessionId"));
		userInfo.setBindingMobile(user.getString("bindingMobile"));
		userInfo.setBindingState(user.getString("bindingState"));
		userInfo.setNickName(user.getString("nickName"));
		userInfo.setPassword(passwordDigest);
		userInfo.setSex(user.getString("sex"));
		userInfo.setBirthday(user.getString("birthday"));
		userInfo.setEmail(user.getString("email"));
		userInfo.setProvince(user.getString("province"));
		userInfo.setAddress(user.getString("address"));
		userInfo.setZipCode(user.getString("zipCode"));
		userInfo.setMobile(user.getString("mobile"));
		userInfo.setTelePhone(user.getString("telePhone"));
		userInfo.setUserName(user.getString("userName"));
		userInfo.setLoginDate(user.getString("loginDate"));
		userInfo.setPolicyCount(user.getString("policyCount"));
		if (UserName.equals(LoginWay)) {
			userInfo.setLoginType("u");
		} else if (Phone.equals(LoginWay)) {
			userInfo.setLoginType("m");
		}

	}

	/**
	 * 检查账户格式是否正确
	 */
	private boolean checkAccount() {
		if (UserName.equals(LoginWay)) {
			String account = inputAccount.getText().toString().trim();
			if (account == null || account.length() == 0) {
				Toast.makeText(LoginActicity.this, "用户名不能为空", Toast.LENGTH_LONG)
						.show();
				return false;
			} else if (!CommonUtil.checkUserName(account)) {
				Toast.makeText(LoginActicity.this, "用户名输入不符合要求",
						Toast.LENGTH_LONG).show();
				return false;
			}
		} else if (Phone.equals(LoginWay)) {
			String phone = inputPhone.getText().toString().trim();
			if (phone == null || phone.length() == 0) {
				Toast.makeText(LoginActicity.this, "手机号不能为空", Toast.LENGTH_LONG)
						.show();
				return false;
			} else if (!CommonUtil.isMobileNO(phone)) {
				Toast.makeText(LoginActicity.this, "手机号输入不符合要求",
						Toast.LENGTH_LONG).show();
				return false;
			}
		}
		return true;

	}

	/**
	 * 检查密码是否正确
	 * 
	 * @return
	 */
	private boolean checkPassword() {
		String password = inputPassword.getText().toString().trim();
		if (password == null || password.length() == 0) {
			Toast.makeText(LoginActicity.this, "密码不能为空", Toast.LENGTH_LONG)
					.show();
			return false;
		} else if (!CommonUtil.checkPassword(password)) {
			Toast.makeText(LoginActicity.this, "密码输入不符合要求", Toast.LENGTH_LONG)
					.show();
			return false;
		}
		return true;
	}

}
