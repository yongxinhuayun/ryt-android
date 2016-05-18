package com.yxh.ryt;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yxh.ryt.util.DBHelper;
import com.yxh.ryt.util.avalidations.EditTextValidator;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.yxh.ryt.vo.User;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
	public static Map<String,String> map;
	public static Map<String,String> ptMap;
	public static DBHelper helper;
	@Override
	public void onCreate() {
		super.onCreate();
		context=this;
		editTextValidator=new EditTextValidator(this);
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
		gson = new GsonBuilder().
				registerTypeAdapter(Double.class, new JsonSerializer<Double>() {

					@Override
					public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
						if (src == src.longValue()){
							return new JsonPrimitive(src.longValue());
						}
						return new JsonPrimitive(src);
					}
				}).create();
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
		getMap();
		getPTMap();
	}
	public static DBHelper getDBHelper(){
		if (helper==null){
			helper=new DBHelper(AppApplication.getSingleContext());
			return helper;
		}
		return helper;
	}
	private static void getPTMap() {
		ptMap=new HashMap<String,String>();
		ptMap.put("12", "融资阶段");
		ptMap.put("14", "融资阶段");
		ptMap.put("15", "融资阶段");
		ptMap.put("21", "创作阶段");
		ptMap.put("22", "创作阶段");
		ptMap.put("23","创作阶段");
		ptMap.put("24", "创作阶段");
		ptMap.put("0","发起阶段");
		ptMap.put("1","融资阶段");
		ptMap.put("2","chu作阶段");
		ptMap.put("3","拍卖阶段");
		ptMap.put("4","抽奖阶段");
		ptMap.put("5","驳回");
		ptMap.put("100","");
		ptMap.put("10","审核阶段");
		ptMap.put("11","审核阶段");
		ptMap.put("13", "审核未通过");
		ptMap.put("20", "");
		ptMap.put("25", "创作阶段");
		ptMap.put("30", "拍卖前");
		ptMap.put("31", "拍卖中");
		ptMap.put("32", "拍卖结束");
		ptMap.put("33", "流拍");
		ptMap.put("34", "待支付尾款");
		ptMap.put("35","待发放");
		ptMap.put("36", "已发放");
	}
	private static void getMap() {
		map=new HashMap<String,String>();
		map.put("0","发起阶段");
		map.put("1","融资阶段");
		map.put("2","制作阶段");
		map.put("3","拍卖阶段");
		map.put("4","抽奖阶段");
		map.put("5","驳回");
		map.put("100","编辑阶段");
		map.put("10","项目审核中");
		map.put("11","项目审核中");
		map.put("12","");
		map.put("13", "审核未通过");
		map.put("14","融资中");
		map.put("15","");
		map.put("20", "创作前");
		map.put("21", "创作中");
		map.put("22", "创作延时");
		map.put("23", "");
		map.put("24", "创作完成审核中");
		map.put("25", "创作完成被驳回");
		map.put("30", "拍卖前");
		map.put("31", "拍卖中");
		map.put("32", "拍卖结束");
		map.put("33", "流拍");
		map.put("34", "待支付尾款");
		map.put("35","待发放");
		map.put("36", "已发放");
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
