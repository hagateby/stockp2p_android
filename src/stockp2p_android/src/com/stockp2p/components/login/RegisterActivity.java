package com.stockp2p.components.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsMessage;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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
import com.stockp2p.common.serviceengin.Des3;
import com.stockp2p.common.serviceengin.EnginCallback;
import com.stockp2p.common.serviceengin.ServiceEngin;
import com.stockp2p.common.util.CommonUtil;
import com.stockp2p.common.util.MD5Util;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.common.view.ClearEditText;
import com.stockp2p.common.view.CommonDialog;
import com.stockp2p.components.login.LoginActicity;
import com.stockp2p.framework.baseframe.BaseFragmentActivity;


public class RegisterActivity extends BaseFragmentActivity {

	private final static String TAG = "RegisterActivity";

	private String userName;
	private String userPassword;
	private String affirmPassword;
	private String userArea = "";
	private String userEmail;
	/**
	 * 电话的 String
	 */
	private String userTelephone;
	private String userTest;
	private boolean isAgree = true;

	private Button submit;
	private Button reSet;
	private CheckBox check_isAgree;
	private Spinner spinner_area;
	private ClearEditText et_username;
	private ClearEditText et_userpassword;
	private ClearEditText et_affirmpassword;
	private ClearEditText et_email;
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
		setContainerView("用户注册", R.layout.framework_register);

		init();
		receiverSMS();
		addListeners();

	}

	/**
	 * 获取验证码接口
	 */
	private void testCM() {
		Map map = new HashMap();
		map.put("mobile", userTelephone);
		String servicePara = JSON.toJSONString(map);
		TipUitls.Log(TAG, "servicePara----->" + servicePara);
		ServiceEngin.Request(context, "00_01_I02", "getRandom", servicePara,
				new EnginCallback(context) {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
						new CommonDialog(context, 1, "确定", null, null, null,
								"提示", getResources().getString(
										R.string.request_failed)).show();
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
						if (result != null
								&& JSONObject.parseObject(result) != null) {
							JSONObject object = JSONObject.parseObject(result);
							String resultCode = object.getString("ResultCode");
							String resultMsg = object.getString("ResultMsg");
							if ("0".equals(resultCode)) {
								mc = new MyCount(120000, 1000);
								mc.start();
							} else if (!"99".equals(resultCode)) {
								new CommonDialog(context, 1, "确定", null, null,
										null, "提示", resultMsg).show();
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
			testGetCode.setBackgroundResource(R.drawable.framework_register_input);

		}

	}

	/**
	 * 注册个广播,拦截95567号码的短信内容
	 */
	private void receiverSMS() {
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				if (!intent.getAction().equals(
						"android.provider.Telephony.SMS_RECEIVED")) {
					return;
				}
				Bundle bundle = intent.getExtras();
				if (bundle == null) {
					return;
				}
				Object[] pdus = (Object[]) bundle.get("pdus");
				for (Object pdu : pdus) {
					SmsMessage smsMessage = SmsMessage
							.createFromPdu((byte[]) pdu);
					String body = smsMessage.getMessageBody();
					String sender = smsMessage.getOriginatingAddress();
					if ("95567".equals(sender)) {
						if (null != body && body.contains("您的校验码为")) {
							testdate.setText(body.substring(6, 10));
						}
					}

				}

			}
		};
		IntentFilter filter = new IntentFilter();
		filter.setPriority(1000);
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(receiver, filter);
	}

	private void init() {
		et_username = (ClearEditText) findViewById(R.id.et_username);
		et_userpassword = (ClearEditText) findViewById(R.id.et_userpassword);
		et_affirmpassword = (ClearEditText) findViewById(R.id.et_affirmpassword);
		spinner_area = (Spinner) findViewById(R.id.spinner_area);
		et_email = (ClearEditText) findViewById(R.id.et_email);
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

		spinnerAdapter = new SpinnerAdaper();
		spinner_area.setAdapter(spinnerAdapter);

		MyOnFocusChangeListener myOnFocusChangeListener = new MyOnFocusChangeListener();
		et_username.setOnFocusChangeListener(myOnFocusChangeListener);
		et_userpassword.setOnFocusChangeListener(myOnFocusChangeListener);
		et_affirmpassword.setOnFocusChangeListener(myOnFocusChangeListener);
		et_email.setOnFocusChangeListener(myOnFocusChangeListener);
		et_telphone.setOnFocusChangeListener(myOnFocusChangeListener);
		testdate.setOnFocusChangeListener(myOnFocusChangeListener);

	}

	/**
	 * 获取数据
	 */
	private void getData() {
		userName = et_username.getText().toString().trim();

		userPassword = et_userpassword.getText().toString().trim();
		userTelephone = et_telphone.getText().toString().trim();
		affirmPassword = et_affirmpassword.getText().toString().trim();
		userEmail = et_email.getText().toString().trim();

		isAgree = check_isAgree.isChecked();
		userTest = testdate.getText().toString().trim();

	}

	/**
	 * 清空
	 */
	private void reSet() {
		et_username.setText("");
		et_userpassword.setText("");
		et_affirmpassword.setText("");
		et_email.setText("");
		et_telphone.setText("");
		testdate.setText("");
//		check_isAgree.setClickable(false);
		spinner_area.setSelection(0);
	}

	private void submit() {
		String passwordDegest = MD5Util.MD5(userPassword).toUpperCase(); // MD5加密
		UserInfoManager.getInstance().setUserName(userName);
		UserInfoManager.getInstance().setPassword(passwordDegest);
		Map map = new HashMap();
		map.put("email", userEmail);
		map.put("region", userArea);
		map.put("mobile", userTelephone);
		map.put("password", passwordDegest);
		map.put("transType", "app");
		map.put("loginId", userName);
		map.put("random", userTest);// 校验码
		String servicePara = JSON.toJSONString(map);
		TipUitls.Log(TAG, "servicePara----->" + servicePara);
		ServiceEngin.Request(context, "00_01_I01", "userToRegister",
				servicePara, new EnginCallback(context) {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
						new CommonDialog(context, 1, "确定", null, null, null,
								"提示", getResources().getString(
										R.string.request_failed)).show();
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
						if (result != null
								&& JSONObject.parseObject(result) != null) {
							JSONObject object = JSONObject.parseObject(result);
							String resultCode = object.getString("ResultCode");
							String resultMsg = object.getString("ResultMsg");
							if ("0".equals(resultCode)) {
								commonDialog = new CommonDialog(context, 1,
										"确定", null, new OnClickListener() {

											@Override
											public void onClick(View v) {
												commonDialog.dismiss();
												context.startActivity(new Intent(
														context,
														LoginActicity.class)
														.putExtra("framework",
																framework_)
														.putExtra("userName",
																userName)
														.putExtra(
																"userPassword",
																userPassword));
											}
										}, null, "提示", "注册成功，请您登录并进行客户信息认证\n"
												+ "用户名：" + userName + "\n密码："
												+ userPassword);
								commonDialog.show();
							} else if (!"99".equals(resultCode)) {
								new CommonDialog(context, 1, "确定", null, null,
										null, "提示", resultMsg).show();
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

				// 检查数据
				if (checkUserName() != 2) {
					// Toast.makeText(RegisterActivity.this, "用户名为必录项",
					// 0).show();
					return;
				}
				if (checkPassword() != 2) {
					// Toast.makeText(RegisterActivity.this, "密码为必录项",
					// 0).show();
					return;
				}
				if (checkAffirmPassword() != 2) {
					// Toast.makeText(RegisterActivity.this, "密码不一致", 0).show();
					return;
				}
				if (checkEmail() != 2) {
					// Toast.makeText(RegisterActivity.this, "邮箱为必录项",
					// 0).show();
					return;
				}
				if (checkTelphone() != 2) {
					// Toast.makeText(RegisterActivity.this, "手机为必录项",
					// 0).show();
					return;
				}
				if (userArea != null && userArea.equals("")) {
					// Toast.makeText(RegisterActivity.this, "请选择地区", 0).show();
					return;
				}
				if (userTest.length() != 4) {
					Toast.makeText(RegisterActivity.this, "请输入正确验证码", 0).show();
					return;
				}
				if (!isAgree) {
					Toast.makeText(RegisterActivity.this, "您是否已经阅读了新华保险网站协议",
							Toast.LENGTH_SHORT).show();
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

		// 获取区域
		spinner_area.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				userArea = areaList.get(position).getCode();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// 检查用户名
		et_username.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					check1 = checkUserName();
				}
			}
		});

		// 检查密码
		et_userpassword.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					check2 = checkPassword();
				}
			}
		});

		// 确认密码
		et_affirmpassword.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					check3 = checkAffirmPassword();
				}
			}
		});

		// email
		et_email.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					check4 = checkEmail();
				}
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
					Toast.makeText(RegisterActivity.this, "您输入的移动电话不正确",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		// 协议
		negotiate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Bundle bundle = new Bundle();
				bundle.putString("flag", "5");
				Intent intent = new Intent(RegisterActivity.this,
						RequiredFileQueryActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

	}

	/**
	 * 检验用户名是否正确
	 * 
	 * @return
	 */
	private int checkUserName() {
		getData();
		int length = userName.length();
		if (length == 0) {
			Toast.makeText(this, "用户名不能为空", 0).show();
			return 0;
		} else if (!CommonUtil.checkUserName(userName)) {
			Toast.makeText(this, "用户名格式错误，请输入5-20个字母或数字", 0).show();
			return 1;
		}

		return 2;

	}

	/**
	 * 检验密码
	 * 
	 * @return
	 */
	private int checkPassword() {
		getData();
		int length = userPassword.length();
		if (length == 0) {
			Toast.makeText(this, "密码不能为空", 0).show();
			return 0;
		} else {
			if (!CommonUtil.checkPassword(userPassword)) {
				Toast.makeText(this, "密码格式错误，密码长度为6至16个字符", 0).show();
				return 1;
			}
		}
		return 2;
	}

	/**
	 * 确认用户密码是否正确
	 */
	private int checkAffirmPassword() {
		getData();
		int length = affirmPassword.length();
		if (length == 0) {
			Toast.makeText(this, "确认密码不能为空", 0).show();
			return 0;
		} else {
			if (!affirmPassword.equals(userPassword)) {
				Toast.makeText(this, "密码不一致", 0).show();
				return 1;
			}
		}
		return 2;
	}

	private int checkEmail() {
		getData();

		if (userEmail.length() > 0) {
			// int length = userEmail.length();
			// if (length == 0) {
			// Toast.makeText(this, "邮箱不能为空", 0).show();
			// return 0;
			// }
			if (!CommonUtil.isEmail(userEmail)) {
				Toast.makeText(this, "邮箱格式不对", 0).show();
				return 1;
			}
		}
		return 2;
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

	class SpinnerAdaper extends BaseAdapter {

		LayoutInflater inflater = getLayoutInflater();

		@Override
		public int getCount() {
			if (areaList != null) {
				return areaList.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return areaList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = inflater.inflate(R.layout.common_simple_spinner_item, null);
			TextView tv = (TextView) view.findViewById(R.id.tv1);
			tv.setTextSize(17f);

			Area area = areaList.get(position);
			tv.setText(area.getName());
			return view;
		}
	}

	private class MyOnFocusChangeListener implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			checkUserName();
			checkPassword();
			checkAffirmPassword();
			checkEmail();
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
