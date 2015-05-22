package com.stockp2p.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import android.util.Log;

public class ThreadPoolManager {
	private static final String TAG = "ThreadPoolManager";

	private ExecutorService service;
	
	private ThreadPoolManager(){
		int num = Runtime.getRuntime().availableProcessors();
		service = Executors.newFixedThreadPool(num*2);
	}
	
	private static final ThreadPoolManager manager = new ThreadPoolManager();
	
	public static ThreadPoolManager getInstance(){
		return manager;
	}
	
	public ExecutorService getExecutorServiceInstance(){
		return service;
	}
	
	public void addTask(Runnable runnable){
		try{
			service.execute(runnable);
		}catch(RejectedExecutionException e){
			Log.i(TAG, "抛出异常RejectedExecutionException");
		}
	}
	
	public void shutDownNow(){
		service.shutdownNow();
	}
	
	public void shutDown(){
		service.shutdown();
	}
}
