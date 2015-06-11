package com.stockp2p.components.setting;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.stockp2p.R;
import com.stockp2p.common.cache.UserInfoManager;
import com.stockp2p.framework.baseframe.BaseFragment;

public class Setting_PersonalInfoActivity extends BaseFragment {
	// 数据
	// private UserInfo userInfo;
	// 控件
	@ViewInject(R.id.tv_username)
	private TextView tv_username;
	@ViewInject(R.id.tv_nicheng)
	private TextView tv_nicheng;
	@ViewInject(R.id.tv_sex)
	private TextView tv_sex;
	@ViewInject(R.id.tv_userBirthday)
	private TextView tv_userBirthday;
	@ViewInject(R.id.tv_userEmail)
	private TextView tv_userEmail;
	@ViewInject(R.id.tv_province)
	private TextView tv_province;
	@ViewInject(R.id.tv_address)
	private TextView tv_address;
	@ViewInject(R.id.tv_postalCode)
	private TextView tv_postalCode;
	@ViewInject(R.id.tv_movePhone)
	private TextView tv_movePhone;
	@ViewInject(R.id.tv_landline)
	private TextView tv_landline;
	private View thisView;
	String sex="";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.set_personal_info, null);
		init(thisView, "个人信息");
		return thisView;
	}

	@Override
	public void init(View thisView, String title) {
		// TODO Auto-generated method stub
		super.init(thisView, title);
		requestServer();
	}

	/**
	 * 链接网络请求服务器
	 */
	private void requestServer() {
		UserInfoManager userInfoManager = UserInfoManager.getInstance();
		tv_username.setText(userInfoManager.getUserName());
		tv_nicheng.setText(userInfoManager.getNickName());
		sex=userInfoManager.getSex();
		if("0".equals(sex)){
			sex="男";
		}else if("1".equals(sex)){
			sex="女";
		}else{
			sex ="";
		}
		tv_sex.setText(sex);
		tv_userBirthday.setText(userInfoManager.getBirthday());
		tv_userEmail.setText(userInfoManager.getEmail());
		tv_province.setText(userInfoManager.getProvince());
		tv_address.setText(userInfoManager.getAddress());
		tv_postalCode.setText(userInfoManager.getZipCode());
		tv_movePhone.setText(userInfoManager.getMobile());
		tv_landline.setText(userInfoManager.getTelePhone());
	}

}
