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
import com.yxh.ryt.vo.ConsumerAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.Call;

public class ReceiverAdressActivity extends BaseActivity implements AutoListView.OnLoadListener, AutoListView.OnRefreshListener, View.OnClickListener {

    private AutoListView adListview;
    private CommonAdapter<ConsumerAddress> cmAdapter;
    private List<ConsumerAddress> addressDatas;
    private int currentPage = 1;
    private String details;
    private String name;
    private String phone;
    private Button addAddress;
    private Button edit;
    private Button del;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_adress);
        adListview = (AutoListView) findViewById(R.id.pl_message_listView);
        addAddress = (Button) findViewById(R.id.btn_add);
        addressDatas = new ArrayList<ConsumerAddress>();

        /*edit = (Button) findViewById(R.id.bt_edit);
        del = (Button) findViewById(R.id.bt_del);
        edit.setOnClickListener(this);
        del.setOnClickListener(this);*/
        addAddress.setOnClickListener(this);
        initView();
        adListview.setPageSize(Constants.pageSize);
        /*addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReceiverAdressActivity.this,NewAddressActivity.class));
            }
        });
*/
    }

    private void initView() {

        cmAdapter = new CommonAdapter<ConsumerAddress>(AppApplication.getSingleContext(), addressDatas, R.layout.address_item) {
            @Override
            public void convert(ViewHolder helper, ConsumerAddress item) {
                helper.getView(R.id.bt_edit).setOnClickListener(ReceiverAdressActivity.this);
                helper.getView(R.id.bt_del).setOnClickListener(ReceiverAdressActivity.this);
                helper.setText(R.id.tv_name, item.getConsignee());
                helper.setText(R.id.tv_phone, item.getPhone());
                helper.setText(R.id.tv_adress, item.getDetails());
            }
        };
        adListview.setAdapter(cmAdapter);
        adListview.setOnLoadListener(this);
        adListview.setOnRefreshListener(this);
    }

    private void LoadData(final int state) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("currentUserId", AppApplication.gUser.getId());

        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "listAddress.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                Log.w("response", response.toString());
                if (state == AutoListView.REFRESH) {
                    adListview.onRefreshComplete();
                    addressDatas.clear();
                    List<ConsumerAddress> addressComment = null;
                    try {
                        addressComment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("consumerAddressList")), new TypeToken<List<ConsumerAddress>>() {
                        }.getType());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    if (null == addressComment || addressComment.size() == 0) {
                        adListview.setResultSize(0);
                    }
                    if (null != addressComment && addressComment.size() > 0) {
                        adListview.setResultSize(addressComment.size());
                        addressDatas.addAll(addressComment);
                        cmAdapter.notifyDataSetChanged();
                    }
                    return;
                }
                if (state == AutoListView.LOAD) {
                    adListview.onLoadComplete();
                    List<ConsumerAddress> addressComment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("consumerAddressList")), new TypeToken<List<ConsumerAddress>>() {
                    }.getType());
                    if (null == addressComment || addressComment.size() == 0) {
                        adListview.setResultSize(1);
                    }
                    if (null != addressComment && addressComment.size() > 0) {
                        adListview.setResultSize(addressComment.size());
                        addressDatas.addAll(addressComment);
                        cmAdapter.notifyDataSetChanged();
                    }
                    return;
                }
            }
        });
    }

    @Override
    public void onLoad() {
        //currentPage++;
        LoadData(AutoListView.LOAD);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // currentPage = 1;
       // addressDatas.clear();
        LoadData(AutoListView.REFRESH);
    }

    @Override
    public void onRefresh() {
       // currentPage = 1;
        LoadData(AutoListView.REFRESH);
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }


    //点击
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit:
                startActivity(new Intent(ReceiverAdressActivity.this,EditRecAddressActivity.class));
                break;
            case R.id.bt_del:
                break;
            case R.id.btn_add:
                startActivity(new Intent(ReceiverAdressActivity.this,NewAddressActivity.class));
                break;
            default:
                break;
        }
    }
}
