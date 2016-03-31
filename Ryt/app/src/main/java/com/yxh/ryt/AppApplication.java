package com.yxh.ryt;

import android.app.Application;
import android.content.Context;
import com.google.gson.Gson;
import com.yxh.ryt.util.avalidations.EditTextValidator;

public class AppApplication extends Application {

	private  static Context context;
	private  static Gson gson;
	private static EditTextValidator editTextValidator;
	@Override
	public void onCreate() {
		super.onCreate();
		context=this;
		gson=new Gson();
		editTextValidator=new EditTextValidator(this);
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
}
