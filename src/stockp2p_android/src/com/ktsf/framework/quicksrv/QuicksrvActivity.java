package com.ktsf.framework.quicksrv;

import android.os.Bundle;

import com.stockp2p.R;
import com.ktsf.framework.BaseFragmentActivity;

public class QuicksrvActivity extends BaseFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		thisManager.beginTransaction()
				.replace(R.id.tab_container, new Billboards()).commit();

	}

}
