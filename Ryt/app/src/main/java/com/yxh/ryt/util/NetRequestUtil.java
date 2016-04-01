package com.yxh.ryt.util;

import com.yxh.ryt.AppApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.Iterator;
import java.util.Map;

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

    public static <T> void get(String url,Map<String,String> paramsMap,Callback<T> callback){
        GetBuilder getBuilder = OkHttpUtils.get();
        getBuilder.url(url);
        Iterator<Map.Entry<String, String>> it = paramsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            getBuilder.addParams(entry.getKey(),  entry.getValue());
        }
        RequestCall build = getBuilder.build();
        build.execute(callback);
    }
}
