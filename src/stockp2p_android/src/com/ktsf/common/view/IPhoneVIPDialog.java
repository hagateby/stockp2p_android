package com.ktsf.common.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.stockp2p.R;

/**
 * iPhoneDialog
 * 
 * @author 彭鹏
 * @version 1.0
 * @since 2012-01-17
 */

public class IPhoneVIPDialog extends AlertDialog {

	protected IPhoneVIPDialog(Context context) {
		super(context);
	}

	public IPhoneVIPDialog(Context context, int theme) {
		super(context, theme);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipsrv_dailog_layout_vip);
	}
}
