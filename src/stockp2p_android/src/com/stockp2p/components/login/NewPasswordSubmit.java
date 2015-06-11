package com.stockp2p.components.login;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stockp2p.R;
import com.stockp2p.common.ifinvoke.Des3;
import com.stockp2p.common.ifinvoke.EnginCallback;
import com.stockp2p.common.ifinvoke.JsonInvok;
import com.stockp2p.common.ifinvoke.ServiceEngin;
import com.stockp2p.common.util.CommonUtil;
import com.stockp2p.common.util.MD5Util;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.common.view.CommonDialog;
import com.stockp2p.framework.baseframe.BaseFragment;

/**
 * 提交
 * 
 * @author haix
 * 
 */
@SuppressLint("ValidFragment")
public class NewPasswordSubmit extends BaseFragment {

	private final static String TAG = " NewPasswordSubmit";
	private View thisView;
	/**
	 * 传进的用户名
	 */
	private String userName;
	/**
	 * 传进的手机号
	 */
	private String tel;
	/**
	 * 显示的用户名
	 */
	@ViewInject(R.id.new_password_et_UserName)
	private TextView userNameEdit;
	/**
	 * 验证码
	 */
	@ViewInject(R.id.new_password_et_code)
	private EditText code;
	/**
	 * 新密码
	 */
	@ViewInject(R.id.new_password_et_new_number)
	private EditText newPassword;
	/**
	 * 确认密码
	 */
	@ViewInject(R.id.new_password_et_affirm_number)
	private EditText affirmPassword;
	/**
	 * 提交
	 */
	@ViewInject(R.id.new_password_bt_submit_passWord)
	private Button submit;
	protected CommonDialog commonDialog;

	public NewPasswordSubmit(String userName, String tel) {
		this.userName = userName;
		this.tel = tel;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.forget_password_newsubmit, null);
		init(thisView, "修改密码");
		return thisView;
	}

	@Override
	protected void init(View view, String title) {
		// TODO Auto-generated method stub
		super.init(view, title);
		userNameEdit.setText(userName);
		//receiverSMS();
		addListeners();

	}

	private void addListeners() {
		// TODO Auto-generated method stub
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String newPw = newPassword.getText().toString().trim();
				System.out.println("newPw--->"+newPw);
				String aPw = affirmPassword.getText().toString().trim();
				String telCode = code.getText().toString().trim()+"1111";//不需要这个字段了

				if ("".equals(telCode)) {
					Toast.makeText(context, "请输入验证码", 0).show();
				} else if (telCode.length() != 4) {
					Toast.makeText(context, "验证码为4位数字", 0).show();
				} else if ("".equals(newPw)) {
					Toast.makeText(context, "请输入新密码", 0).show();
				} else if (!CommonUtil.checkPassword(newPw)) {
					Toast.makeText(context, "新密码格式不正确，密码长度为6至16个字符", 0).show();
				} else if ("".equals(aPw)) {
					Toast.makeText(context, "请输入确认密码", 0).show();
				} else if (!newPw.equals(aPw)) {
					Toast.makeText(context, "两次输入的密码不一致", 0).show();
				} else {
					request(MD5Util.MD5(newPw).toUpperCase(), telCode);
				}

			}
		});
	}

	/**
	 * 提交
	 */
	private void request(String newPw, String telCode) {

		// TODO Auto-generated method stub
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("loginId", userName);
		map.put("password", newPw);// 新密码
		map.put("mobilephone", tel);// 手机号
		//map.put("random", telCode);// 验证码

		/**
		 * bizId 业务场景编码 serviceName 方法名 param 参数集合
		 */
		String bizId = "00_02_I02";
		String serviceName = "findPwdUpdate";
		String param = JSON.toJSONString(map);
		TipUitls.Log(TAG, "param------>" + param);
		/**
		 * 通过业务场景编码，方法名，参数集合，对修改密码发送接口发送请求
		 */
		//ServiceEngin.Request(context, bizId, serviceName, param,
		JsonInvok.invokFindPwdUpdate(param,context,
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
							TipUitls.Log(TAG, "result---->" + result);
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
				commonDialog = new CommonDialog(context, 1, "确定", null,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								commonDialog.dismiss();
								context.finish();
							}
						}, null, "提示", ResultMsg);
				commonDialog.show();

			} else if (!"99".equals(ResultCode)) {
				new CommonDialog(context, 1, "确定", null, null, null, "请求失败",
						ResultMsg).show();
			}

		}
	}

	/**
	 * 注册个广播,拦截95567号码的短信内容
	 */
	private void receiverSMS() {
		BroadcastReceiver receiver = new BroadcastReceiver() {

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
							code.setText(body.substring(6, 10));
						}
					}

				}

			}
		};
		IntentFilter filter = new IntentFilter();
		filter.setPriority(1000);
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		context.registerReceiver(receiver, filter);
	}
}
