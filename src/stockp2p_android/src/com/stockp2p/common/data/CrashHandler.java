package com.stockp2p.common.data;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import com.stockp2p.common.util.CommonUtil;
import com.stockp2p.common.view.CommonDialog;

import android.content.Context;
import android.util.Log;

public class CrashHandler implements UncaughtExceptionHandler {

	private UncaughtExceptionHandler mDefaultCrashHandler;
	private static CrashHandler instance;
	private Context con;

	private CrashHandler() {
	}

	public synchronized static CrashHandler getInstance() { // 同步方法，以免单例多线程环境下出现异常
		if (instance == null) {
			instance = new CrashHandler();
		}
		return instance;
	}

	/**
	 * 初始化，把当前对象设置成UncaughtExceptionHandler处理器
	 * 
	 * @param context
	 */
	public void init(Context context) {
		// 获取系统默认的异常处理器
		mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 将当前实例设为系统默认的异常处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
		this.con = context;
	}

	/**
	 * 发生异常就会调用此方法
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable e) {
		Writer result = new StringWriter();
		PrintWriter printWriter = new PrintWriter(result);
		e.printStackTrace(printWriter);
		String stacktrace = result.toString();
		String errorMessage = e.toString() + " " + stacktrace;
		//保存到本地文件中
		CommonUtil.SaveLog("---->", errorMessage);
		Log.e("错误的信息如下: ", errorMessage);
		
		try {
			thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
	}
}
