package com.yxh.ryt;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class AppApplication extends Application {

	private  static Context context;
	@Override
	public void onCreate() {
		super.onCreate();
		context=this;
		Log.d("dddddddddd","fffffffffggghhddd");
	}

	//获取全局Context
	public static Context getSingleContext(){
		return  context;
	}
}
