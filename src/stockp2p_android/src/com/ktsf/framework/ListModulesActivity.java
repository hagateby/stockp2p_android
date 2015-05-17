package com.pactera.nci.framework;

import android.os.Bundle;

import com.pactera.nci.R;

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
