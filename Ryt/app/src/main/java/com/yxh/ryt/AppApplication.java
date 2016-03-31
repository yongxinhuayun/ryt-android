package com.yxh.ryt;

import android.app.Application;
import android.content.Context;
import com.google.gson.Gson;

public class AppApplication extends Application {

	private  static Context context;
	private  static Gson gson;
	@Override
	public void onCreate() {
		super.onCreate();
		context=this;
		gson=new Gson();
	}

	//获取全局Context
	public static Context getSingleContext(){
		return  context;
	}
	//获取全局Context
	public static Gson getSingleGson(){
		return  gson;
	}
}
