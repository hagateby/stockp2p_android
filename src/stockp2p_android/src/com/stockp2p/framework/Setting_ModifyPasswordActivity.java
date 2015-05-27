package com.stockp2p.framework;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.http.ResponseInfo;
import com.stockp2p.R;
import com.stockp2p.common.cache.UserInfoManager;
import com.stockp2p.common.serviceengin.Des3;
import com.stockp2p.common.serviceengin.EnginCallback;
import com.stockp2p.common.serviceengin.ServiceEngin;
import com.stockp2p.common.util.ExitApplication;
import com.stockp2p.common.util.MD5Util;
import com.stockp2p.common.view.ClearEditText;
import com.stockp2p.common.view.CommonDialog;
import com.stockp2p.components.login.LoginActicity;
import com.stockp2p.framework.baseframe.BaseFragment;

public class Setting_ModifyPasswordActivity extends BaseFragment {

	private TextView userName;
	private TextView nicheng;

	private String prePassword;
	private String newPassword1;
	private String affirmPassword;

	private Button reSet;
	private Button submit;
	private ClearEditText et_prePassword;
	private ClearEditText et_newPassword;
	private ClearEditText et_affirmPassword;
	private View thisView;
	// 返回的json字符串
	private String json = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.set_modify_password, null);
		init(thisView, "修改密码");

		return thisView;
	}

	@Override
	public void init(View thisView, String title) {
		// TODO Auto-generated method stub
		super.init(thisView, title);
		initLayout();
		setListener();
	}

	private void initLayout() {
		userName = (TextView) thisView.findViewById(R.id.tv_userName);

		et_prePassword = (ClearEditText) thisView
				.findViewById(R.id.et_prePassword);
		et_newPassword = (ClearEditText) thisView
				.findViewById(R.id.et_newPassword);
		et_affirmPassword = (ClearEditText) thisView
				.findViewById(R.id.et_affirmPassword);
		reSet = (Button) thisView.findViewById(R.id.button_reset2);
		submit = (Button) thisView.findViewById(R.id.button_submit);

		MyOnFocusChangeListener1 myOnFocusChangeListener1 = new MyOnFocusChangeListener1();
		MyOnFocusChangeListener2 myOnFocusChangeListener2 = new MyOnFocusChangeListener2();
		MyOnFocusChangeListener3 myOnFocusChangeListener3 = new MyOnFocusChangeListener3();
		et_prePassword.setOnFocusChangeListener(myOnFocusChangeListener1);
		et_newPassword.setOnFocusChangeListener(myOnFocusChangeListener2);
		et_affirmPassword.setOnFocusChangeListener(myOnFocusChangeListener3);

		// ------
		userName.setText(UserInfoManager.getInstance().getUserName());

	}

	// 获取数据
	private void getData() {
		prePassword = et_prePassword.getText().toString().trim();
		newPassword1 = et_newPassword.getText().toString().trim();
		affirmPassword = et_affirmPassword.getText().toString().trim();
	}

	private void setListener() {

		reSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				reSet();
			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkPassword()) {
					submit();
				} else {
					// Toast.makeText(Setting_ModifyPasswordActivity.this,
					// "填写密码错误", 1).show();
				}
			}
		});
	}

	private boolean checkPassword() {
		getData();
		if (prePassword.equals("")) {
			Toast.makeText(context, "原密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		} else if (newPassword1.equals("")) {
			Toast.makeText(context, "新密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		} else if (affirmPassword.equals("")) {
			Toast.makeText(context, "确认新密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		} else if (!MD5Util.MD5(et_prePassword.getText().toString().trim())
				.toUpperCase()
				.equals(UserInfoManager.getInstance().getPassword())) {
			Toast.makeText(context, "原密码不正确", Toast.LENGTH_SHORT).show();
			return false;
		} else if (!et_prePassword
				.getText()
				.toString()
				.trim()
				.matches(
						"[\\w|[\\\\\"!#\\$%\\&'\\(\\)\\*\\+,-./:;<=>\\?@\\[\\]\\^`\\{\\}~\\|]]{6,16}")) {
			Toast.makeText(context, "原密码格式不符合要求", Toast.LENGTH_SHORT).show();
			return false;
		} else if (!et_newPassword
				.getText()
				.toString()
				.trim()
				.matches(
						"[\\w|[\\\\\"!#\\$%\\&'\\(\\)\\*\\+,-./:;<=>\\?@\\[\\]\\^`\\{\\}~\\|]]{6,16}")) {
			Toast.makeText(context, "新密码格式不符合要求", Toast.LENGTH_SHORT).show();
			return false;
		} else if (!newPassword1.equals(affirmPassword)) {
			Toast.makeText(context, "输入与新密码一致的确认密码", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	/**
	 * 提交
	 */
	private void submit() {
		HashMap<String, String> map = new HashMap<String, String>();
		String loginId = UserInfoManager.getInstance().getUserName();
		String cid = UserInfoManager.getInstance().getCid();
		String sessionId = UserInfoManager.getInstance().getSessionId();
		String password = MD5Util.MD5(prePassword).toUpperCase();
		String newPassword = MD5Util.MD5(newPassword1).toUpperCase();
		map.put("loginId", loginId);
		map.put("cid", cid);
		map.put("sessionId", sessionId);
		map.put("password", password);
		map.put("newPassword", newPassword);

		/**
		 * bizId 业务场景编码 serviceName 方法名 param 参数集合
		 */
		String bizId = "00_00_02_I01";
		String serviceName = "updatePW";
		String param = JSON.toJSONString(map);
		/**
		 * 通过业务场景编码，方法名，参数集合，对修改密码发送接口发送请求
		 */
		ServiceEngin.Request(context, bizId, serviceName, param,
				new EnginCallback(context) {

					@Override
					public void onSuccess(ResponseInfo arg0) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0);
						canclDialog();
						try {
							json = Des3.decode(arg0.result.toString());
							Log.e("请求成功", json);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/**
						 * json解析返回的结果
						 */
						parseJson(json);
					}
				});
	
	}

	/**
	 * 解析服务器返回Json
	 */
	protected void parseJson(String result) {
		JSONObject obj = JSON.parseObject(result);
		if (obj.get("ResultCode") != null
				&& Integer.parseInt(obj.get("ResultCode").toString()) == 0) {// 请求成功
			// 重置成功

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("提示");
			builder.setMessage("修改成功!  请牢记：\n" + "\n新密码：" + newPassword1);
			builder.setCancelable(false);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
							// 退出登录前先清空单例
							UserInfoManager.getInstance().exitLogin();
							ExitApplication.getInstance().exitLogin(context);
							startActivity(new Intent(context,
									LoginActicity.class));
						}
					}).show();

		} else if (obj.get("ResultCode") != null
				&& !obj.get("ResultCode").toString().equals("99")) {
			// 重置失败
			new CommonDialog(context, 1, "确定", null, null, null, "请求失败", obj
					.get("ResultMsg").toString()).show();

		}
	}

	/**
	 * 重置
	 */
	private void reSet() {
		et_prePassword.setText("");
		et_newPassword.setText("");
		et_affirmPassword.setText("");
	}

	private class MyOnFocusChangeListener1 implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if (!hasFocus) {
				checkPassword1();
			}
		}
	}

	private class MyOnFocusChangeListener2 implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if (!hasFocus) {
				checkPassword2();
			}
		}
	}

	private class MyOnFocusChangeListener3 implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if (!hasFocus) {
				checkPassword3();
			}
		}
	}

	public boolean checkPassword1() {
		prePassword = et_prePassword.getText().toString().trim();
		if (!et_prePassword
				.getText()
				.toString()
				.trim()
				.matches(
						"[\\w|[\\\\\"!#\\$%\\&'\\(\\)\\*\\+,-./:;<=>\\?@\\[\\]\\^`\\{\\}~\\|]]{6,16}")) {
			Toast.makeText(context, "原密码格式不对或者为空", Toast.LENGTH_SHORT).show();

			return false;
		} else if (!MD5Util.MD5(et_prePassword.getText().toString().trim())
				.toUpperCase()
				.equals(UserInfoManager.getInstance().getPassword())) {
			Toast.makeText(context, "原密码不正确", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	public boolean checkPassword2() {
		newPassword1 = et_newPassword.getText().toString().trim();
		if (!et_newPassword
				.getText()
				.toString()
				.trim()
				.matches(
						"[\\w|[\\\\\"!#\\$%\\&'\\(\\)\\*\\+,-./:;<=>\\?@\\[\\]\\^`\\{\\}~\\|]]{6,16}")) {
			Toast.makeText(context, "新密码格式不对或者为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public boolean checkPassword3() {
		affirmPassword = et_affirmPassword.getText().toString().trim();
		if (!affirmPassword
				.matches("[\\w|[\\\\\"!#\\$%\\&'\\(\\)\\*\\+,-./:;<=>\\?@\\[\\]\\^`\\{\\}~\\|]]{6,16}")) {
			Toast.makeText(context, "确认密码格式不对或者为空", Toast.LENGTH_SHORT).show();
		} else if (!newPassword1.equals(affirmPassword)) {
			Toast.makeText(context, "输入与新密码一致的确认密码", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}
