package com.yxh.ryt.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.custemview.CustomDialog;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.ConsumerAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.Call;

public class ReceiverAdressActivity extends BaseActivity implements AutoListView.OnLoadListener, AutoListView.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private AutoListView adListview;
    private CommonAdapter<ConsumerAddress> cmAdapter;
    private List<ConsumerAddress> addressDatas;
    private int currentPage = 1;
    private ImageButton addAddress;
    private int unDefult;
    private ImageView back;
    private LinearLayout ll_new_add;
    private LinearLayout ll_add;
    private Map<Integer, String> defaultAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_adress);
        adListview = (AutoListView) findViewById(R.id.pl_message_listView);
        ll_new_add = (LinearLayout) findViewById(R.id.ll_new_add);
        ll_add = (LinearLayout) findViewById(R.id.ll_add);
        addressDatas = new ArrayList<ConsumerAddress>();
        addAddress= (ImageButton) findViewById(R.id.btn_add);
        back = (ImageView) findViewById(R.id.iv_back);
        defaultAddress = new HashMap<>();
        addAddress.setOnClickListener(this);
        initView();
        adListview.setPageSize(Constants.pageSize);
        back.setOnClickListener(this);
        adListview.setOnItemClickListener(this);
    }

    private void initView() {

        cmAdapter = new CommonAdapter<ConsumerAddress>(AppApplication.getSingleContext(), addressDatas, R.layout.address_item) {
            @Override
            public void convert(final ViewHolder helper, final ConsumerAddress item) {
                if (addressDatas == null ||addressDatas.size()==0) {
                    ll_add.setVisibility(View.INVISIBLE);
                    ll_new_add.setVisibility(View.VISIBLE);
                } else {
                    ll_add.setVisibility(View.VISIBLE);
                    ll_new_add.setVisibility(View.INVISIBLE);
                    helper.setText(R.id.aass_tv_addressName, item.getConsignee());
                    helper.setText(R.id.aass_tv_addressPhone, item.getPhone());
                    if ("2".equals(item.getStatus())){
                        helper.setText(R.id.aass_tv_addressDetail, "[默认]"+item.getProvinceStr()+item.getDetails());
                    }else {
                        helper.setText(R.id.aass_tv_addressDetail, item.getProvinceStr()+item.getDetails());
                    }
                }
            }
        };
        adListview.setAdapter(cmAdapter);
        adListview.setOnLoadListener(this);
        adListview.setOnRefreshListener(this);
    }

    private void LoadData(final int state) {
        Map<String, String> paramsMap = new HashMap<>();
        //paramsMap.put("currentUserId", AppApplication.gUser.getId());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "listAddress.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showLong(ReceiverAdressActivity.this, "网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
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
                        }
                        cmAdapter.notifyDataSetChanged();
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
                        }
                        cmAdapter.notifyDataSetChanged();
                        return;
                    }
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
            case R.id.btn_add:
                if (addressDatas==null || addressDatas.size()==0){
                    Intent edIntent = new Intent(ReceiverAdressActivity.this, ModifyRecAddressActivity.class);
                    edIntent.putExtra("status","2");
                    startActivity(edIntent);
                }else {
                    Intent edIntent = new Intent(ReceiverAdressActivity.this, ModifyRecAddressActivity.class);
                    edIntent.putExtra("status","1");
                    startActivity(edIntent);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position<=addressDatas.size()){
            Intent edIntent = new Intent(ReceiverAdressActivity.this, EditRecAddressActivity.class);
            edIntent.putExtra("addressId", addressDatas.get(position-1).getId());
            edIntent.putExtra("status", addressDatas.get(position-1).getStatus());
            edIntent.putExtra("consignee", addressDatas.get(position-1).getConsignee());
            edIntent.putExtra("details", addressDatas.get(position-1).getDetails());
            edIntent.putExtra("phone", addressDatas.get(position-1).getPhone());
            edIntent.putExtra("provinceStr", addressDatas.get(position-1).getProvinceStr());
            edIntent.putExtra("districtStr", addressDatas.get(position-1).getDistrictStr());
            edIntent.putExtra("cityStr", addressDatas.get(position-1).getCityStr());
            startActivity(edIntent);
        }
    }
}
