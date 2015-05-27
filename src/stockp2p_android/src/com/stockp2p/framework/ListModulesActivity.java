package com.stockp2p.framework;

import android.os.Bundle;

import com.stockp2p.R;
import com.stockp2p.framework.baseframe.BaseFragmentActivity;

/**
 * 所有列表模块
 * 
 * @author haix
 * 
 */
public class ListModulesActivity extends BaseFragmentActivity {

	private static final String TAG = "ListModulesActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		thisManager.beginTransaction()
				.replace(R.id.tab_container, new ListModules(framework_))
				.addToBackStack(TAG).commit();
	}
}
