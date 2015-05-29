package com.stockp2p.common.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySetting {
  
	static	String  shardname ="nci";
	
	private static SharedPreferences sharedPreferences;
	
	// SlipButton 1
	public static void putSlipButton1Boolean(Context context, Boolean cb1) {
		sharedPreferences = context.getSharedPreferences(shardname,
				Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("slipButton1", cb1);
		editor.commit();
	}

	public static boolean getSlipButton1Boolean(Context context) {
		sharedPreferences = context.getSharedPreferences(shardname,
				Context.MODE_PRIVATE);
		boolean sb1 = sharedPreferences.getBoolean("slipButton1", true);
		return sb1;
	}

	// SlipButton2
	public static void putSlipButton2Boolean(Context context, Boolean cb2) {
		sharedPreferences = context.getSharedPreferences(shardname,
				Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("slipButton2", cb2);
		editor.commit();
	}

	public static boolean getSlipButton2Boolean(Context context) {
		sharedPreferences = context.getSharedPreferences(shardname,
				Context.MODE_PRIVATE);
		boolean sb2 = sharedPreferences.getBoolean("slipButton2", false);
		return sb2;
	}
	
}