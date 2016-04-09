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
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;

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

		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);     		// 初始化 JPush
		OkHttpUtils.getInstance().setConnectTimeout(10, TimeUnit.SECONDS);
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
