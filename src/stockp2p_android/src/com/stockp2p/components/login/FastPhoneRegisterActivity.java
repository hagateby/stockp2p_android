package com.stockp2p.components.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.stockp2p.R;
import com.stockp2p.common.cache.UserInfoManager;
import com.stockp2p.common.db.Area;
import com.stockp2p.common.ifinvoke.Des3;
import com.stockp2p.common.ifinvoke.EnginCallback;
import com.stockp2p.common.ifinvoke.JsonInvok;
import com.stockp2p.common.util.CommonUtil;
import com.stockp2p.common.util.MD5Util;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.common.view.ClearEditText;
import com.stockp2p.common.view.CommonDialog;

import com.stockp2p.components.login.RegisterActivity.SpinnerAdaper;
import com.stockp2p.framework.baseframe.BaseFragmentActivity;

public class FastPhoneRegisterActivity extends BaseFragmentActivity {

	private final static String TAG = "FastPhoneRegisterActivity";

	/**
	 * 电话的 String
	 */
	private String userTelephone;
	private String userTest;
	private boolean isAgree = true;

	private Button submit;
	private Button reSet;
	private CheckBox check_isAgree;

	private ClearEditText et_telphone;
	private ClearEditText testdate;
	/**
	 * 协议
	 */
	private TextView negotiate;
	/**
	 * 获取验证码
	 */
	private Button testGetCode;
	private List<Area> areaList;
	private SpinnerAdaper spinnerAdapter;
	private MyCount mc;

	private boolean isSubmit; // 验证结果，是否可以提交

	private BroadcastReceiver receiver;

	protected CommonDialog commonDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContainerView("快速注册", R.layout.components_login_register);
		init();
		addListeners();
	}

	/**
	 * 获取验证码接口
	 */
	private void testCM() {

		TipUitls.Log(TAG, "userTelephone----->" + userTelephone);

		JsonInvok.invoksendsms(userTelephone, context, new EnginCallback(
				context) {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
				new CommonDialog(context, 1, "确定", null, null, null, "提示",
						getResources().getString(R.string.request_failed))
						.show();
			}

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

				TipUitls.Log(TAG, "result------>" + result);
				if (result != null && JSONObject.parseObject(result) != null) {
					JSONObject object = JSONObject.parseObject(result);
					String resultCode = object.getString("ResultCode");
					String resultMsg = object.getString("ResultMsg");
					if ("0".equals(resultCode)) {
						mc = new MyCount(120000, 1000);
						mc.start();
					} else if (!"99".equals(resultCode)) {
						new CommonDialog(context, 1, "确定", null, null, null,
								"提示", resultMsg).show();
					}
				}
			}

		});
	}

	/**
	 * 倒计时类
	 * 
	 * @author haix
	 * 
	 */
	private class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFinish() {
			testGetCode.setText("获取手机\n验证码");
			testGetCode.setClickable(true);
			testGetCode.setBackgroundResource(R.drawable.common_bt_bg_down);

		}

		@Override
		public void onTick(long millisUntilFinished) {
			testGetCode.setText("剩余" + millisUntilFinished / 1000 + "秒");
			testGetCode.setClickable(false);
			testGetCode
					.setBackgroundResource(R.drawable.framework_register_input);

		}

	}

	private void init() {

		et_telphone = (ClearEditText) findViewById(R.id.et_telphone);

		check_isAgree = (CheckBox) findViewById(R.id.check_isAgree);

		submit = (Button) findViewById(R.id.button_submit);

		reSet = (Button) findViewById(R.id.button_reSet);

		negotiate = (TextView) findViewById(R.id.negotiate);

		testdate = (ClearEditText) findViewById(R.id.testDate);

		testGetCode = (Button) findViewById(R.id.test);

		negotiate.setText(Html
				.fromHtml("我已经查看并同意《<font color=blue>新华保险网站协议</font>》"));

		// 初始化数据
		areaList = Area.queryArea(myApplication.db);

		MyOnFocusChangeListener myOnFocusChangeListener = new MyOnFocusChangeListener();

		et_telphone.setOnFocusChangeListener(myOnFocusChangeListener);

		testdate.setOnFocusChangeListener(myOnFocusChangeListener);

	}

	/**
	 * 获取数据
	 */
	private void getData() {

		userTelephone = et_telphone.getText().toString().trim();

		isAgree = check_isAgree.isChecked();
		userTest = testdate.getText().toString().trim();

	}

	/**
	 * 清空
	 */
	private void reSet() {

		et_telphone.setText("");
		testdate.setText("");
	}

	private void submit() {

		Map map = new HashMap();

		map.put("mobile", userTelephone);

		map.put("transType", "app");

		map.put("random", userTest);// 校验码

		String servicePara = JSON.toJSONString(map);
		TipUitls.Log(TAG, "servicePara----->" + servicePara);

		// ServiceEngin.Request(context, "00_01_I01", "userToRegister",
		JsonInvok.invokfastregister(servicePara, context, new EnginCallback(
				context) {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
				new CommonDialog(context, 1, "确定", null, null, null, "提示",
						getResources().getString(R.string.request_failed))
						.show();
			}

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

				TipUitls.Log(TAG, "result------>" + result);
				if (result != null && JSONObject.parseObject(result) != null) {
					JSONObject object = JSONObject.parseObject(result);
					String resultCode = object.getString("ResultCode");
					String resultMsg = object.getString("ResultMsg");
					if ("0".equals(resultCode)) {
						commonDialog = new CommonDialog(context, 1, "确定", null,
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										commonDialog.dismiss();
										context.startActivity(new Intent(
												context, LoginActicity.class)
												.putExtra("framework",
														framework_).putExtra(
														"userName",
														userTelephone));
									}
								}, null, "提示", "注册成功，请您登录并进行客户信息认证\n" + "用户名："
										+ userTelephone);
						commonDialog.show();
					} else if (!"99".equals(resultCode)) {
						new CommonDialog(context, 1, "确定", null, null, null,
								"提示", resultMsg).show();
					}
				}
			}

		});

	}

	private int check1, check2, check3, check4, check5;

	/**
	 * 点击事件
	 */
	private void addListeners() {

		// 提交
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取数据
				getData();
				if (checkTelphone() != 2) {
					return;
				}

				if (userTest.length() != 4) {
					Toast.makeText(FastPhoneRegisterActivity.this, "请输入正确验证码",
							0).show();
					return;
				}
				if (!isAgree) {
					Toast.makeText(FastPhoneRegisterActivity.this,
							"您是否已经阅读了新华保险网站协议", Toast.LENGTH_SHORT).show();
					return;
				}

				else {
					submit();

				}
			}
		});

		// 重置
		reSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				reSet();
			}
		});

		// 电话
		et_telphone.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					check5 = checkTelphone();
				}
			}
		});

		// 获取验证码
		testGetCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				userTelephone = et_telphone.getText().toString().trim();
				if (CommonUtil.isMobileNO(userTelephone)) {
					testCM();

				} else {
					Toast.makeText(FastPhoneRegisterActivity.this,
							"您输入的移动电话不正确", Toast.LENGTH_SHORT).show();
				}

			}
		});

		// 协议
		negotiate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * lwh Bundle bundle = new Bundle(); bundle.putString("flag",
				 * "5"); Intent intent = new Intent(RegisterActivity.this,
				 * RequiredFileQueryActivity.class); intent.putExtras(bundle);
				 * startActivity(intent);
				 */
			}
		});

	}

	private int checkTelphone() {
		getData();
		int length = userTelephone.length();
		if (length == 0) {
			Toast.makeText(this, "电话不能为空", 0).show();
			return 0;
		} else {
			if (!CommonUtil.isMobileNO(userTelephone)) {
				Toast.makeText(this, "手机号不正确", 0).show();
				return 1;
			}
		}
		return 2;
	}

	private class MyOnFocusChangeListener implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub

			checkTelphone();

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
