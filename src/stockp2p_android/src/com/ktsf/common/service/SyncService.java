package com.ktsf.common.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.ktsf.common.util.JsonToDb;

public class SyncService extends IntentService {

	public SyncService() {
		super("SyncService");
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		Log.i("测试", "onCreate");
		
		try {
//			new OnlineCustomerServiceLogin().init(this); //登录在线客服
			new JsonToDb().convert(this); //在线更新数据
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}