package com.ktsf.common.data;

import java.lang.reflect.Method;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
public class ScreenObserver {
	private Context mContext;
	private ScreenBroadcastReceiver mScreenBroadcastReceiver;
	private observerScreenStateUpdateListener mObserverScreenStateUpdateListener;
	private static Method mReflectIsScreenOnMethod;

	public ScreenObserver(Context context) {
		mContext = context;
		mScreenBroadcastReceiver = new ScreenBroadcastReceiver();
		try {
			mReflectIsScreenOnMethod = PowerManager.class.getMethod("isScreenOn",new Class[] {});
		} catch (NoSuchMethodException nsme) {
			System.out.println("API<7不可使用使用");
		}
	}

	//接收设备发出的广播
	private class ScreenBroadcastReceiver extends BroadcastReceiver {
		private String action = null;
		@Override
		public void onReceive(Context context, Intent intent) {
			action = intent.getAction();
			if (Intent.ACTION_SCREEN_ON.equals(action)) {
				mObserverScreenStateUpdateListener.onScreenOn();
			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				mObserverScreenStateUpdateListener.onScreenOff();
			}
		}
	}

	//此为入口.
	//监视屏幕状态
	public void observerScreenStateUpdate(observerScreenStateUpdateListener listener) {
		mObserverScreenStateUpdateListener = listener;
		registerScreenBroadcastReceiver();
		//该方法可以根据实际情况取舍
		firstGetScreenState();
	}

	// 注册监听
	private void registerScreenBroadcastReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mContext.registerReceiver(mScreenBroadcastReceiver, filter);
	}
	
	//刚运行应用程序时判断屏幕状态
	private void firstGetScreenState() {
		PowerManager manager = (PowerManager) mContext.getSystemService(Activity.POWER_SERVICE);
		if (isScreenOnForFirst(manager)) {
			if (mObserverScreenStateUpdateListener != null) {
				mObserverScreenStateUpdateListener.onScreenOn();
				System.out.println("===> 刚运行程序时,屏幕为打开状态");
			}
		} else {
			if (mObserverScreenStateUpdateListener != null) {
				mObserverScreenStateUpdateListener.onScreenOff();
				System.out.println("===> 刚运行程序时,屏幕为关闭状态");
			}
		}
	}
	
	//刚运行应用程序时判断屏幕是否关闭
	private static boolean isScreenOnForFirst(PowerManager powerManager) {
		boolean screenState;
		try {
			screenState = (Boolean) mReflectIsScreenOnMethod.invoke(powerManager);
			//亦可采用下一句代码判断,从而避免使用反射
			//screenState =powerManager.isScreenOn();
		} catch (Exception e) {
			screenState = false;
		}
		return screenState;
	}


	//停止监视屏幕状态
	public void stopScreenStateUpdate() {
		mContext.unregisterReceiver(mScreenBroadcastReceiver);
	}

	public interface observerScreenStateUpdateListener {
		public void onScreenOn();
		public void onScreenOff();
	}
}
