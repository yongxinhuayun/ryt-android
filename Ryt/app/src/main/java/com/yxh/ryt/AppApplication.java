package com.yxh.ryt;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.yxh.ryt.util.avalidations.EditTextValidator;

public class AppApplication extends Application {

	//获取到主线程的handler
	private static Handler mMainThreadHandler = null;
	//获取到主线程的looper
	private static Looper mMainThreadLooper = null;
	//获取到主线程
	private static Thread mMainThead = null;
	//获取到主线程的id
	private static int mMainTheadId ;

	private  static Context context;
	private  static Gson gson;
	private static EditTextValidator editTextValidator;
	@Override
	public void onCreate() {
		super.onCreate();
		context=this;
		gson=new Gson();
		editTextValidator=new EditTextValidator(this);

		this.mMainThreadHandler = new Handler();
		this.mMainThreadLooper = getMainLooper();
		this.mMainThead = Thread.currentThread();
		this.mMainTheadId = android.os.Process.myTid();//主線程id
	}

	//获取全局Context
	public static Context getSingleContext(){
		return  context;
	}
	//获取全局Gson
	public static Gson getSingleGson(){
		return  gson;
	}
	//获取全局Gson
	public static EditTextValidator getSingleEditTextValidator(){
		return  editTextValidator;
	}

	public static Handler getMainThreadHandler(){
		return mMainThreadHandler;
	}
	public static Looper getMainThreadLooper(){
		return mMainThreadLooper;
	}
	public static Thread getMainThread(){
		return mMainThead;
	}
	public static int getMainThreadId(){
		return mMainTheadId;
	}
}
