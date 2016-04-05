package com.yxh.ryt.activity;

import android.os.Bundle;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.vo.Notification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/5.
 */
public class NotificationActivity extends BaseActivity {
    private AutoListView ntflistview;
    private CommonAdapter<Notification> ntfAdapter;
    private List<Notification> rongZiDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userId",5+"");
        paramsMap.put("type","0");
        paramsMap.put("pageSize",1+"");
        paramsMap.put("pageNum", 5+"");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
