package com.ktsf.framework;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.stockp2p.R;
import com.ktsf.common.util.CommonUtil;
import com.ktsf.framework.BaseFragment;

public class Setting_AboutActivity extends BaseFragment {
	String time;
	String version;
	TextView Tv;
	private View thisView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.set_about, null);
		init(thisView, "关于掌上新华");
		return thisView;
	}

	@Override
	public void init(View view, String title) {
		// TODO Auto-generated method stub
		super.init(view, title);
		TextView tv_updateTime = (TextView) thisView
				.findViewById(R.id.tv_updateTime);
		try {
			version = CommonUtil.getVersionName(context);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "获取当前版本信息失败", Toast.LENGTH_SHORT).show();
		}
		Tv = (TextView) thisView.findViewById(R.id.tv);
		Tv.setText("掌上新华" + version);

		getupdateTime();
		tv_updateTime.setText(time);
	}

	private void getupdateTime() {
		SharedPreferences sp = context.getSharedPreferences("userprefs",
				Context.MODE_PRIVATE);
		time = sp.getString("updateDate", "2013-8-01").replace("%20", " ");
	}
}
