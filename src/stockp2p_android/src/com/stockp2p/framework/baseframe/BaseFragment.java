package com.stockp2p.framework.baseframe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.stockp2p.R;
import com.stockp2p.common.data.MyApplication;

/**
 * 所有引擎fragment 都要继承 并实现init 方法
 * 
 * @author haix
 * 
 */
public class BaseFragment extends Fragment {

	protected MyApplication myApplication;
	protected FragmentManager thisManager;
	protected FragmentActivity context;
	protected TextView titleText;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		myApplication = (MyApplication) context.getApplicationContext();
		thisManager = context.getSupportFragmentManager();

	}

	protected void init(View view, String title) {

		if (view != null) {
			ViewUtils.inject(this, view);
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(getActivity().INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}

		titleText = (TextView) getActivity().findViewById(
				R.id.base_activity_tv_titletxt);
		titleText.setText(title);

	}

}
