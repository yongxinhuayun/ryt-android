package com.yxh.ryt.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

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
import com.yxh.ryt.vo.Notification;
import com.yxh.ryt.vo.PrivateLetter;

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
public class PrivateLetterActivity extends BaseActivity implements AutoListView.OnLoadListener, AutoListView.OnRefreshListener {
    private CommonAdapter<PrivateLetter> plfAdapter;
    private List<PrivateLetter> privateLetterDatas;
    private int currentPage=1;
    @Bind(R.id.pl_message_listView)
    AutoListView plflistview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privateletter_listview);
        ButterKnife.bind(this);/*启用注解绑定*/
        privateLetterDatas=new ArrayList<PrivateLetter>();
        initView();
        LoadData(AutoListView.REFRESH, currentPage);
    }

    private void initView() {
        plfAdapter=new CommonAdapter<PrivateLetter>(AppApplication.getSingleContext(),privateLetterDatas,R.layout.privateletter_item) {
            @Override
            public void convert(ViewHolder helper, PrivateLetter item) {
                /*if (item.getIsWatch()==0){
                    helper.setColor(R.id.ni_ll_top, Color.RED);
                }*/
                helper.setText(R.id.ni_tv_content,item.getContent());
                helper.setText(R.id.ni_tv_date, item.getCreateDatetime()+"");
            }
        };
        plflistview.setAdapter(plfAdapter);
        plflistview.setOnLoadListener(this);
        plflistview.setOnRefreshListener(this);
    }

    private void LoadData(final int state,int pageNum) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userId","iijq9f1r7apprtab");
        paramsMap.put("type","0");
        paramsMap.put("pageSize",2+"");
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
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (state==AutoListView.REFRESH){
                    plflistview.onRefreshComplete();
                    privateLetterDatas.clear();
                    List<PrivateLetter> notificationList = null;
                    try {
                        notificationList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<PrivateLetter>>() {
                        }.getType());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    if(null==notificationList||notificationList.size()==0){
                        plflistview.setResultSize(1);
                    }
                    if (null!=notificationList&&notificationList.size()>0){
                        plflistview.setResultSize(plflistview.getPageSize());
                        privateLetterDatas.addAll(notificationList);
                        plfAdapter.notifyDataSetChanged();
                    }
                    return;
                }
                if (state==AutoListView.LOAD){
                    plflistview.onLoadComplete();
                    List<PrivateLetter> notificationList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<PrivateLetter>>() {
                    }.getType());
                    if(null==notificationList||notificationList.size()==0){
                        plflistview.setResultSize(1);
                    }
                    if (null!=notificationList&&notificationList.size()>0) {
                        plflistview.setResultSize(plflistview.getPageSize());
                        privateLetterDatas.addAll(notificationList);
                        plfAdapter.notifyDataSetChanged();
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
