package com.stockp2p.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络管理工具
 * 
 * @author user
 * 
 */
public class NetUtil {

	public static boolean hasNetWork(Context context) {
		ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnectivity != null) {
			NetworkInfo info = mConnectivity.getActiveNetworkInfo();
			if(info != null){
				return info.isAvailable();
			}
		}
		return false;
	}

}
