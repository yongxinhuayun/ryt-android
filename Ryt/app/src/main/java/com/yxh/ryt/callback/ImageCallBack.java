package com.yxh.ryt.callback;

import android.graphics.Bitmap;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/31.
 */
public  abstract class ImageCallBack extends Callback<Bitmap>{

    @Override
    public Bitmap parseNetworkResponse(Response response) throws Exception {
        return null;
    }
}
