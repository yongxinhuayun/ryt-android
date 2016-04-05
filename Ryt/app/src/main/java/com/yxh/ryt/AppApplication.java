package com.yxh.ryt;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yxh.ryt.util.avalidations.EditTextValidator;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;

public class AppApplication extends Application {

	public static BaseResp resp;
	public static IWXAPI api;
	public static PayReq req;
	private  static Context context;
	private  static Gson gson;
	public static  String signmsg;
	private static EditTextValidator editTextValidator;
	@Override
	public void onCreate() {
		super.onCreate();
		context=this;
		gson=new Gson();
		editTextValidator=new EditTextValidator(this);
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
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
