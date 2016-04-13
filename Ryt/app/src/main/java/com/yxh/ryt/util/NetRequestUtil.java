package com.yxh.ryt.util;

import com.yxh.ryt.AppApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/31.
 */
public class NetRequestUtil<T> {

    public static <T> void post(String url,Map<String,String> t,Callback<T> callback){
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
    public static <T> void postFile(String url,String fileKey,Map<String,File> fileMap,Map<String,String> paramsMap,  Map<String, String> headers,Callback<T> callback){
        PostFormBuilder post = OkHttpUtils.post();
        Iterator<Map.Entry<String, File>> it = fileMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, File> entry = it.next();
            post.addFile(fileKey,entry.getKey(),  entry.getValue());
        }
        post.url(url);
        post.params(paramsMap);
        post.headers(headers);
        RequestCall build = post.build();
        build.connTimeOut(500000);
        build.readTimeOut(500000);
        build.writeTimeOut(500000);
        build.execute(callback);
    }
}
