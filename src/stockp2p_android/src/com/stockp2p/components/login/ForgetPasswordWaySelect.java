package com.stockp2p.components.login;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stockp2p.R;
import com.stockp2p.common.ifinvoke.Des3;
import com.stockp2p.common.ifinvoke.EnginCallback;
import com.stockp2p.common.ifinvoke.ServiceEngin;
import com.stockp2p.common.ifinvoke.JsonInvok;
import com.stockp2p.common.util.CommonUtil;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.common.view.ClearEditText;
import com.stockp2p.common.view.CommonDialog;
import com.stockp2p.framework.baseframe.BaseFragment;

/**
 * 找回密码确定界面
 * 
 * @author haix
 * 
 */
@SuppressLint("ValidFragment")
public class ForgetPasswordWaySelect extends BaseFragment {
	private final static String TAG = " ForgetPasswordWaySelect";

	/**
	 * 输入注册的用户名
	 */
	@ViewInject(R.id.forget_password_edit_username)
	private ClearEditText forgetUserName;
	/**
	 * group
	 */
	@ViewInject(R.id.forget_password_rg_radiogroup)
	private RadioGroup radiogroup;

	private final String TEL_WAY = "0";
	private final String EMAIL_WAY = "1";
	/**
	 * 0手机号 1邮箱
	 */
	private String Way = TEL_WAY;

	/**
	 * 方式的输入框
	 */
	@ViewInject(R.id.forget_password_edit_way)
	private EditText wayEdit;
	/**
	 * 验证码输入框
	 */
	@ViewInject(R.id.forget_password_testDate)
	private ClearEditText verifiCode;
	/**
	 * 获取验证码
	 */
	@ViewInject(R.id.forget_password_bt_get)
	private Button getCode;
	/**
	 * 确定
	 */
	@ViewInject(R.id.forget_password_bt_next)
	private Button next;
	/**
	 * 验证码部分 layout
	 */
	@ViewInject(R.id.forget_password_rel_7)
	private LinearLayout layout;

	private View thisView;

	protected CommonDialog commonDialog;

	/**
	 * 输入的用户名
	 */
	private String userName;

	/**
	 * 输入的手机号
	 */
	private String telContent;
	/**
	 * 验证码
	 */
	private String random;
	
	/**
	 * 传递进的用户名
	 */
	private String usrName;

	public ForgetPasswordWaySelect(String str){
		usrName = str;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.forget_password_layout, null);
		init(thisView, "密码找回");
		return thisView;
	}

	@Override
	protected void init(View view, String title) {
		// TODO Auto-generated method stub
		super.init(view, title);
		//forgetUserName.setText(usrName);
		addListeners();

	}

	private void addListeners() {
		// TODO Auto-generated method stub
		// 方式
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				// 手机方式
				if (checkedId == R.id.forget_password_rb_telway) {
					layout.setVisibility(View.VISIBLE);
					wayEdit.setHint("注册时的手机号码");
					Way = TEL_WAY;
				}
				// mail方式
				else if (checkedId == R.id.forget_password_rb_emailway) {
					layout.setVisibility(View.GONE);
					wayEdit.setHint("注册时的邮箱");
					Way = EMAIL_WAY;
				}

			}
		});

		// 获取验证码
		getCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userName = forgetUserName.getText().toString().trim();
				String wayContent = wayEdit.getText().toString().trim();
				telRequest(wayContent);
			}
		});

		// 确定
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (TEL_WAY.equals(Way)) {
					telWay();
				} else if (EMAIL_WAY.equals(Way)) {
					emailWay();
				}
			}

		});

	}

	private void emailWay() {
		userName = forgetUserName.getText().toString().trim();
		String wayContent = wayEdit.getText().toString().trim();
		if ("".equals(userName) || !CommonUtil.checkUserName(userName)) {
			Toast.makeText(context, "用户名不正确", 0).show();
			return;
		} else if ("".equals(wayContent) || !CommonUtil.isEmail(wayContent)) {
			Toast.makeText(context, "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
		} else {
			String emailContent = wayContent;
			telContent = "";
			request(telContent, emailContent);
		}
	}

	private void telWay() {
		userName = forgetUserName.getText().toString().trim();
		String wayContent = wayEdit.getText().toString().trim();
		if ("".equals(userName) || !CommonUtil.checkUserName(userName)) {
			Toast.makeText(context, "用户名不正确", 0).show();
			return;
		} else if ("".equals(wayContent) || !CommonUtil.isMobileNO(wayContent)) {
			Toast.makeText(context, "请输入正确的电话", Toast.LENGTH_SHORT).show();
		} else if ("".equals(verifiCode.getText().toString().trim())) {
			Toast.makeText(context, "验证码不能为空", 0).show();
		} else {
			random = verifiCode.getText().toString().trim();
			telContent = wayContent;
			String emailContent = "";
			request(telContent, emailContent);
		}
	}

	/**
	 *  获取验证码
	 */
	private void telRequest(String telContent) {

		// TODO Auto-generated method stub	
		/**
		 * 通过业务场景编码，方法名，参数集合，对修改密码发送接口发送请求
		 */
		JsonInvok.invoksendsms(telContent, context, 
		//ServiceEngin.Request(context, bizId, serviceName, param,
				new EnginCallback(context) {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						canclDialog();
						commonDialog = new CommonDialog(context, 1, "确定", null,
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										commonDialog.dismiss();
									}
								}, null, "提示", getResources().getString(
										R.string.request_failed));
						commonDialog.show();
					}

					@Override
					public void onSuccess(ResponseInfo arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
						canclDialog();
						String result = null;
						try {
							result = Des3.decode(arg0.result.toString());
							TipUitls.Log(TAG, "result--获取验证码-->" + result);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (result != null) {

							telParseJson(result);
						}
					}
				});

	}

	/**
	 * json解析返回的结果
	 */
	private void telParseJson(String result) {
		JSONObject jsonObject = JSON.parseObject(result);
		if (jsonObject != null) {
			String ResultCode = jsonObject.getString("ResultCode");
			String ResultMsg = jsonObject.getString("ResultMsg");
			if ("0".equals(ResultCode)) {
				if (TEL_WAY.equals(Way)) {
					//receiverSMS();
					MyCount mc = new MyCount(120000, 1000);
					mc.start();
				} else if (EMAIL_WAY.equals(Way)) {
					commonDialog = new CommonDialog(context, 1, "确定", null,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									commonDialog.dismiss();
									context.finish();
								}
							}, null, "提示", ResultMsg);
					commonDialog.show();

				}
			} else if (!"99".equals(ResultCode)) {
				new CommonDialog(context, 1, "确定", null, null, null, "请求失败",
						ResultMsg).show();
			}

		}
	}

	/**
	 * 用户验证接口
	 */
	private void request(String telContent, String emailContent) {

		// TODO Auto-generated method stub
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("loginId", userName);
		map.put("findType", Way);
		map.put("mobilephone", telContent);
		map.put("email", emailContent);
		map.put("random", random);

		/**
		 * bizId 业务场景编码 serviceName 方法名 param 参数集合
		 */
		String bizId = "00_02_I01";
		String serviceName = "findPwd";
		String param = JSON.toJSONString(map);
		TipUitls.Log(TAG, "param---确定--->" + param);
		System.out.println("param---确定--->" + param);
		/**
		 * 通过业务场景编码，方法名，参数集合，对修改密码发送接口发送请求
		 */
		ServiceEngin.Request(context, bizId, serviceName, param,
				new EnginCallback(context) {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						canclDialog();
						commonDialog = new CommonDialog(context, 1, "确定", null,
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										commonDialog.dismiss();
									}
								}, null, "提示", getResources().getString(
										R.string.request_failed));
						commonDialog.show();
					}

					@Override
					public void onSuccess(ResponseInfo arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
						canclDialog();
						String result = null;
						try {
							result = Des3.decode(arg0.result.toString());
							TipUitls.Log(TAG, "result--确定-->" + result);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (result != null) {

							parseJson(result);
						}
					}
				});

	}

	/**
	 * json解析返回的结果
	 */
	private void parseJson(String result) {
		JSONObject jsonObject = JSON.parseObject(result);
		if (jsonObject != null) {
			String ResultCode = jsonObject.getString("ResultCode");
			String ResultMsg = jsonObject.getString("ResultMsg");
			if ("0".equals(ResultCode)) {
				if (TEL_WAY.equals(Way)) {
					thisManager
							.beginTransaction()
							.replace(R.id.tab_container,
									new NewPasswordSubmit(userName, telContent))
							.addToBackStack(TAG).commit();
				} else if (EMAIL_WAY.equals(Way)) {
					commonDialog = new CommonDialog(context, 1, "确定", null,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									commonDialog.dismiss();
									context.finish();
								}
							}, null, "提示", ResultMsg);
					commonDialog.show();

				}
			} else if (!"99".equals(ResultCode)) {
				new CommonDialog(context, 1, "确定", null, null, null, "请求失败",
						ResultMsg).show();
			}

		}
	}


	class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFinish() {
			getCode.setText("获取手机\n验证码");
			getCode.setClickable(true);
			getCode.setBackgroundResource(R.drawable.common_bt_bg_down);

		}

		@Override
		public void onTick(long millisUntilFinished) {
			System.out.println("onTick");
			getCode.setText("剩余" + millisUntilFinished / 1000 + "秒");
			getCode.setClickable(false);
			getCode.setBackgroundResource(R.drawable.framework_register_input);

		}

	}

}
