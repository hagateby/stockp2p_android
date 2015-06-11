package com.stockp2p.common.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.stockp2p.common.util.JsonToDb;

public class SyncService extends IntentService {

	public SyncService() {
		super("SyncService");
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		Log.i("测试", "onCreate");
		
		try {
			new JsonToDb().convert(this); //在线更新数据			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}