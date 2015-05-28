package com.stockp2p.framework.layoutmodules.listmodule;

import android.os.Bundle;

import com.stockp2p.R;
import com.stockp2p.framework.ListModules;
import com.stockp2p.framework.baseframe.BaseFragmentActivity;

public class ListModuleActivity  extends BaseFragmentActivity {
	
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
