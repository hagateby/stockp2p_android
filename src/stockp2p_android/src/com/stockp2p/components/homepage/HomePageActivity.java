package com.stockp2p.components.homepage;


import android.os.Bundle;

import com.stockp2p.R;
import com.stockp2p.framework.baseframe.BaseFragmentActivity;

public class HomePageActivity extends BaseFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        
		/*
		thisManager.beginTransaction()
				.replace(R.id.tab_container, new BannerBoards()).commit();
		*/
		
		thisManager.beginTransaction()
		      .replace(R.id.tab_container, new HomePageArticle()).commit();	

	}

}
