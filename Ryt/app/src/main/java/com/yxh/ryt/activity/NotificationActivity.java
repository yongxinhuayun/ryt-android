package com.yxh.ryt.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.NotifaicationCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/5.
 */
public class NotificationActivity extends BaseActivity implements AutoListView.OnLoadListener, AutoListView.OnRefreshListener {
    private CommonAdapter<Notification> ntfAdapter;
    private List<Notification> notificationDatas;
    private int currentPage=1;
    @Bind(R.id.nl_message_listView)
    AutoListView ntflistview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_listview);
        ButterKnife.bind(this);/*启用注解绑定*/
        notificationDatas=new ArrayList<Notification>();
        ntflistview.setPageSize(Constants.pageSize);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadData(AutoListView.REFRESH, currentPage);
    }

    private void initView() {
        ntfAdapter=new CommonAdapter<Notification>(AppApplication.getSingleContext(),notificationDatas,R.layout.notification_item) {
            @Override
            public void convert(ViewHolder helper, Notification item) {
                Log.d("Notification", item.toString());
                if (item.getIsWatch()==0){
                    helper.setColor(R.id.ni_ll_top, Color.RED);
                }
                helper.setText(R.id.ni_tv_content,item.getContent());
                helper.setText(R.id.ni_tv_date, Utils.timeTrans(item.getCreateDatetime()));
                if (!(item.getFromUser()==null)){
                    helper.setVisible(R.id.ni_iv_prijectIcon);
                    helper.setVisible(R.id.ni_tv_Projectcontent);
                    helper.setText(R.id.ni_tv_Projectcontent, item.getFromUser().getUserName() + "");
                }
            }
        };
        ntflistview.setAdapter(ntfAdapter);
        ntflistview.setOnLoadListener(this);
        ntflistview.setOnRefreshListener(this);
    }

    private void LoadData(final int state,int pageNum) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userId","iijq9f1r7apprtab");
        paramsMap.put("type","0");
        paramsMap.put("pageSize",Constants.pageSize+"");
        paramsMap.put("pageNum", pageNum+"");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "information.do", paramsMap, new NotifaicationCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (state==AutoListView.REFRESH){
                    ntflistview.onRefreshComplete();
                    notificationDatas.clear();
                    List<Notification> notificationList = null;
                    try {
                        notificationList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<Notification>>() {
                        }.getType());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    if(null==notificationList||notificationList.size()==0){
                        ntflistview.setResultSize(0);
                    }
                    if (null!=notificationList&&notificationList.size()>0){
                        ntflistview.setResultSize(notificationList.size());
                        notificationDatas.addAll(notificationList);
                        ntfAdapter.notifyDataSetChanged();
                    }
                    return;
                }
                if (state==AutoListView.LOAD){
                    ntflistview.onLoadComplete();
                    List<Notification> notificationList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<Notification>>() {
                    }.getType());
                    if(null==notificationList||notificationList.size()==0){
                        ntflistview.setResultSize(1);
                    }
                    if (null!=notificationList&&notificationList.size()>0) {
                        ntflistview.setResultSize(notificationList.size());
                        notificationDatas.addAll(notificationList);
                        ntfAdapter.notifyDataSetChanged();
                    }
                    return;
                }
            }
        });
    }

    @Override
    public void onLoad() {
        currentPage++;
        LoadData(AutoListView.LOAD,currentPage);
    }

    @Override
    public void onRefresh() {
        currentPage=1;
        LoadData(AutoListView.REFRESH,currentPage);
    }
}
