package com.ktsf.common.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.ktsf.common.data.Constants;

/**
 * 退出程序类
 * 
 * @author haix
 * 
 */
public class ExitApplication extends Application {

	public List<Activity> activityStack = new LinkedList<Activity>();

	private static ExitApplication instance;

	/**
	 * 单例模式中获取唯一的ExitApplication 实例
	 * 
	 * @return
	 */
	public static ExitApplication getInstance() {
		if (null == instance) {
			instance = new ExitApplication();
		}
		return instance;

	}

	/**
	 * 添加Activity 到容器中
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity, String isExit) {
		activityStack.add(activity);
		TipUitls.Log("ExitApplication",
				"activity 数量---->" + activityStack.size());
		for (Activity act : activityStack) {
			TipUitls.Log("ExitApplication", "activity 名称---->"
					+ act.getClass().getName().toString());
		}

		// 跳到登录时除了登录 activity 其它的都要 finish
		if (activity.getClass().getName()
				.contains("com.ktsf.framework.LoginActicity")
				&& "true".equals(isExit)) {
			for (Activity act : activityStack) {
				if (!act.getClass().getName()
						.contains("com.ktsf.framework.LoginActicity")) {
					act.finish();
				}

			}
		}
	}

	/**
	 * 销毁 activity
	 * 
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		TipUitls.Log("ExitApplication", "removeActivity---->"
				+ activity.getClass().getName());
		activityStack.remove(activity);
		TipUitls.Log("ExitApplication",
				"activity 当前数量---->" + activityStack.size());
	}
	
	/**
	 * 退出登录不跳到登录
	 */
	public void exitLoginSet(Context activity) {
		for (Activity act : activityStack) {

			if (act.getClass()
					.getName()
					.contains(
							activity.getPackageName()
									+ "."
									+ Constants.moduleList.get(0)
											.getPackageName())) {

			} else if (act
					.getClass()
					.getName()
					.contains(
							activity.getPackageName()
									+ "."
									+ Constants.moduleList.get(1)
											.getPackageName())) {

			} else if (act
					.getClass()
					.getName()
					.contains(
							activity.getPackageName()
									+ "."
									+ Constants.moduleList.get(2)
											.getPackageName())) {

			} else if (act
					.getClass()
					.getName()
					.contains(
							activity.getPackageName()
									+ "."
									+ Constants.moduleList.get(3)
											.getPackageName())) {

			} else if (act.getClass().getName()
					.contains(activity.getClass().getName())) {

			}

			else {
				act.finish();
			}
		}
	}

	/**
	 * 退出登录并跳到登录/重新登录后
	 */
	public void exitLogin(Context activity) {
		for (Activity act : activityStack) {

			if (act.getClass()
					.getName()
					.contains(
							activity.getPackageName()
									+ "."
									+ Constants.moduleList.get(0)
											.getPackageName())) {

			} else if (act
					.getClass()
					.getName()
					.contains(
							activity.getPackageName()
									+ "."
									+ Constants.moduleList.get(1)
											.getPackageName())) {

			} else if (act
					.getClass()
					.getName()
					.contains(
							activity.getPackageName()
									+ "."
									+ Constants.moduleList.get(2)
											.getPackageName())) {

			} else if (act
					.getClass()
					.getName()
					.contains(
							activity.getPackageName()
									+ "."
									+ Constants.moduleList.get(3)
											.getPackageName())) {

			} else {
				TipUitls.Log("ExitApplication", "finish----->"+act.getClass().getName());
				act.finish();
			}

		}
	}

	/**
	 * 遍历所有Activity 并finish
	 */
	public void exit() {

		for (Activity activity : activityStack) {
			TipUitls.Log("ExitApplication", "退出时activityList的数量------>"
					+ activity);
			activity.finish();
		}

		// System.exit(0);

	}
	
	public void forceStopAPK(String pkgName){
	    Process sh = null;
	    DataOutputStream os = null;
	    try {
	        sh = Runtime.getRuntime().exec("su");
	        os = new DataOutputStream(sh.getOutputStream());
	        final String Command = "am force-stop "+pkgName+ "\n";
	        os.writeBytes(Command);
	        os.flush();
	 
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	 
	    try {
	        sh.waitFor();
	    } catch (InterruptedException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
}