package com.yxh.ryt.util;

import com.yxh.ryt.AppApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

/**
 * Created by Administrator on 2016/3/31.
 */
public class NetRequestUtil<T> {

    public static <T> void post(String url,T t,Callback<T> callback){
        System.out.println(AppApplication.getSingleGson().toJson(t));
        OkHttpUtils
                .postString()
                .url(url)
                .content(AppApplication.getSingleGson().toJson(t))
                .build()
                .execute(callback);
    }
}
