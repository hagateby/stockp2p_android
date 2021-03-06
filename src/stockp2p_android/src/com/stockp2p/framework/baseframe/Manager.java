package com.stockp2p.framework.baseframe;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.stockp2p.R;
import com.stockp2p.common.cache.UserInfoManager;
import com.stockp2p.common.db.FrameWork_Frame;
import com.stockp2p.common.util.TipUitls;
import com.stockp2p.components.login.LoginActicity;
import com.stockp2p.framework.ListModulesActivity;
import com.stockp2p.framework.WebviewActivity;

/**
 * 跳转管理类
 * 
 * @author haix
 * 
 */
public class Manager extends BaseFragment {

	/**
	 * 
	 * @param context
	 * @param framework
	 */
	public static void branch(FragmentActivity context, FrameWork_Frame framework) {

		FragmentManager thisManager = context.getSupportFragmentManager();

		// isLogin
		if ("1".equals(framework.getIsLogin())
				&& !UserInfoManager.getInstance().isLogin()) {

			Intent intent = new Intent(context, LoginActicity.class);
			intent.putExtra("framework", framework);
			intent.putExtra("isExit", "false");
			context.startActivity(intent);

		} else {
			// type
			if ("1".equals(framework.getFrameType())) {
                 
				Intent intent = new Intent(context, ListModulesActivity.class);
				intent.putExtra("framework", framework);
				context.startActivity(intent);
			} else if ("2".equals(framework.getFrameType())) {
				
				try {
					Intent intent = new Intent(context, toActivity(context,
							framework));
					intent.putExtra("framework", framework);
					context.startActivity(intent);
				} catch (Exception e) {
					TipUitls.syso("Manager", e.toString());
				}

			} else if ("3".equals(framework.getFrameType())) {
                //webview
				if (framework.getClickUrl() != null) {
					context.startActivity(new Intent(context, WebviewActivity.class)
					.putExtra("framework", framework));
				}
				
			} else if ("4".equals(framework.getFrameType())) {

				PackageManager packageManager = context.getPackageManager();
				Intent intent = new Intent();
				intent = packageManager.getLaunchIntentForPackage(framework
						.getPackageName());
				if (intent == null) {
					Toast.makeText(context, "应用未下载", Toast.LENGTH_LONG).show();
					return;
				}
				context.startActivity(intent);

			} else if ("0".equals(framework.getFrameType())) {
				try {
					Intent intent = new Intent(context, toActivity(context,
							framework));
					intent.putExtra("framework", framework);
					context.startActivity(intent);
				} catch (Exception e) {
					TipUitls.Log("Manager", e.toString());
				}
			}

		}

	}

	/**
	 * activity
	 */
	
	private static Class toActivity(FragmentActivity context,
			FrameWork_Frame framework) throws Exception {
		Class activity = Class.forName(  framework.getPackageName());
		return activity;

	}
}
