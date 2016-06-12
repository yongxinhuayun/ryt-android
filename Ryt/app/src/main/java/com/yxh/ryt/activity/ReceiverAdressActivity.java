package com.yxh.ryt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.ArtworkCommentMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.Call;

public class ReceiverAdressActivity extends BaseActivity implements AutoListView.OnLoadListener, AutoListView.OnRefreshListener {

    private AutoListView adListview;
    private CommonAdapter<ArtworkCommentMsg> cmAdapter;
    private List<ArtworkCommentMsg> artworkCommentDatas;
    private int currentPage = 1;
    private String details;
    private String name;
    private String phone;
    private Button addAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_adress);
        adListview = (AutoListView) findViewById(R.id.pl_message_listView);
        addAddress = (Button) findViewById(R.id.btn_add);
        artworkCommentDatas = new ArrayList<ArtworkCommentMsg>();
        initView();
        adListview.setPageSize(Constants.pageSize);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReceiverAdressActivity.this,NewAddressActivity.class));
            }
        });

    }

    private void initView() {
       /* Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", AppApplication.gUser.getId());
        paramsMap.put("pageIndex", "1");
        paramsMap.put("pageSize", "2");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + " addressView.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                 details = (String) response.get("details");
                 name = (String) response.get("consumer");
                 phone = (String) response.get("phone");
            }
        });*/
        cmAdapter = new CommonAdapter<ArtworkCommentMsg>(AppApplication.getSingleContext(), artworkCommentDatas, R.layout.address_item) {
            //@Override
            public void convert(ViewHolder helper, final ArtworkCommentMsg item) {

                helper.setText(R.id.tv_name, name);
                helper.setText(R.id.tv_phone, phone);
                helper.setText(R.id.tv_adress, details);

            }
        };
        adListview.setAdapter(cmAdapter);
        adListview.setOnLoadListener(this);
        adListview.setOnRefreshListener(this);
    }

    private void LoadData(final int state, int pageNum) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", AppApplication.gUser.getId());
        paramsMap.put("pageIndex", "1");
        paramsMap.put("pageSize", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "addressView.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                Log.d("response", response.toString());
                if (state == AutoListView.REFRESH) {
                    adListview.onRefreshComplete();
                    artworkCommentDatas.clear();
                    List<ArtworkCommentMsg> ArtworkComment = null;
                    try {
                        ArtworkComment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<ArtworkCommentMsg>>() {
                        }.getType());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    if (null == ArtworkComment || ArtworkComment.size() == 0) {
                        adListview.setResultSize(0);
                    }
                    if (null != ArtworkComment && ArtworkComment.size() > 0) {
                        adListview.setResultSize(ArtworkComment.size());
                        artworkCommentDatas.addAll(ArtworkComment);
                        cmAdapter.notifyDataSetChanged();
                    }
                    return;
                }
                if (state == AutoListView.LOAD) {
                    adListview.onLoadComplete();
                    List<ArtworkCommentMsg> ArtworkComment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<ArtworkCommentMsg>>() {
                    }.getType());
                    if (null == ArtworkComment || ArtworkComment.size() == 0) {
                        adListview.setResultSize(1);
                    }
                    if (null != ArtworkComment && ArtworkComment.size() > 0) {
                        adListview.setResultSize(ArtworkComment.size());
                        artworkCommentDatas.addAll(ArtworkComment);
                        cmAdapter.notifyDataSetChanged();
                    }
                    return;
                }
            }
        });
    }

    @Override
    public void onLoad() {
        currentPage++;
        LoadData(AutoListView.LOAD, currentPage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 1;
        artworkCommentDatas.clear();
        LoadData(AutoListView.REFRESH, currentPage);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        LoadData(AutoListView.REFRESH, currentPage);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }
}
