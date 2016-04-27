package com.yxh.ryt;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yxh.ryt.util.avalidations.EditTextValidator;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.yxh.ryt.vo.User;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
	public static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	public static DisplayImageOptions options;
	public static User gUser;
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
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.showImageOnLoading(R.mipmap.default_icon);
		builder.showImageForEmptyUri(R.mipmap.default_icon);
		builder.showImageOnFail(R.mipmap.default_icon);
		builder.cacheInMemory(true);
		builder.cacheOnDisk(true);
		builder.considerExifParams(true);
		builder.imageScaleType(ImageScaleType.EXACTLY_STRETCHED);
		options=builder.build();
	}
	public static void displayImage(String url,ImageView view){
		ImageLoader.getInstance().displayImage(url, view, options, animateFirstListener);
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
	public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		public static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
