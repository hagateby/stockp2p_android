package com.stockp2p.framework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.stockp2p.R;
import com.stockp2p.common.cache.UserInfoManager;
import com.stockp2p.common.data.Constants;
import com.stockp2p.components.login.LoginActicity;
import com.stockp2p.framework.ListModulesActivity;
import com.stockp2p.framework.baseframe.BaseFragmentActivity;
import com.stockp2p.framework.baseframe.Manager;

/**
 * 设置的 {@link Activity} 载体
 * 
 * @author haix
 * 
 */
public class SetActivity extends BaseFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		findViewById(R.id.base_activity_bt_base_title_more).setVisibility(
				View.GONE);

		// 标题栏返回
		((Button) findViewById(R.id.base_activity_bt_titleback))
		.setCompoundDrawables(null, null, null, null);
		findViewById(R.id.base_activity_bt_titleback).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!UserInfoManager.getInstance().isLogin()) {
							finish();
							Manager.branch(context, framework_);
						} else if (thisManager.getBackStackEntryCount() <= 1) {
							context.finish();
						} else {
							thisManager.popBackStackImmediate();
						}

					}
				});

		thisManager.beginTransaction()
				.replace(R.id.tab_container, new SetFragment(framework_))
				.addToBackStack("SetFragment").commit();

	}

	/**
	 * 上一次按回退键的时间
	 */
	private long mExitTime;

	/**
	 * home 返回
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			if (!UserInfoManager.getInstance().isLogin()) {
				finish();
				Manager.branch(context, framework_);
				return true;
			} else if (thisManager.getBackStackEntryCount() <= 1) {
				context.finish();
				return true;
			} else {
				thisManager.popBackStackImmediate();
			}

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}
