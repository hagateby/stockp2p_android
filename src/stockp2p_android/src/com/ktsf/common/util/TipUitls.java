package com.ktsf.common.util;

import android.content.Context;

public class TipUitls {

	public static boolean test = true;

	public static void Toast(Context context, String tip) {
		if (test)
			android.widget.Toast.makeText(context, tip, 1).show();

	}

	public static void Log(String ClassName, String infor) {
		if (test)
			android.util.Log.d("next", ClassName + "--------->" + infor);

	}

	public static void syso(String ClassName, String infor) {
		if (test)
			System.out.println("next//" + ClassName + "--------->" + infor);

	}
}
