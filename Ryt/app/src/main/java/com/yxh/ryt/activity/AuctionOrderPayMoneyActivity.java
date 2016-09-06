package com.yxh.ryt.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.AuctionOrder;
import com.yxh.ryt.vo.ConsumerAddress;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class AuctionOrderPayMoneyActivity extends BaseActivity{
    @Bind(R.id.aass_tv_number)
    TextView number;
    @Bind(R.id.aass_tv_addressName)
    TextView addressName;
    @Bind(R.id.aass_tv_addressPhone)
    TextView addressPhone;
    @Bind(R.id.aass_tv_addressDetail)
    TextView addressDetail;
    @Bind(R.id.foa_iv_projectPicture)
    ImageView projectPicture;
    @Bind(R.id.foa_iv_projectTitle)
    TextView projectTitle;
    @Bind(R.id.foa_iv_projectState)
    TextView projectState;
    @Bind(R.id.foa_iv_projectBrief)
    TextView projectBrief;
    @Bind(R.id.foa_tv_projectPrice)
    TextView projectPrice;
    @Bind(R.id.aass_tv_marginMoney)
    TextView marginMoney;
    @Bind(R.id.aass_tv_auctionMoney)
    TextView auctionMoney;
    @Bind(R.id.aass_tv_realMoney)
    TextView realMoney;
    private AuctionOrder auctionOrder;
    @Bind(R.id.aass_tv_edit)
    TextView edit;
    @Bind(R.id.ll_add_address)
    LinearLayout noAddress;
    @Bind(R.id.aass_iv_go)
    ImageView go;
    @Bind(R.id.aass_ll_address)
    LinearLayout address;
    private boolean noAddressBollean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctionordersummarypaymoney);
        ButterKnife.bind(this);
        auctionOrder=null;
        if (getIntent()!=null){
            auctionOrder= (AuctionOrder) getIntent().getSerializableExtra("data");
        }
        initData();
    }

    private void initData() {
        number.setText(auctionOrder.getId()+"");
        AppApplication.displayImage(auctionOrder.getArtwork().getPicture_url(),projectPicture);
        projectTitle.setText(auctionOrder.getArtwork().getTitle());
        projectState.setText("待付尾款");
        projectState.setBackgroundColor(Color.rgb(219,40,40));
        if (auctionOrder.getArtwork().getBrief()==null || "".equals(auctionOrder.getArtwork().getBrief())){
            projectBrief.setText("暂无简介");
        }else {
            projectBrief.setText(auctionOrder.getArtwork().getBrief());
        }
        edit.setText("支付尾款");
        marginMoney.setText("￥"+auctionOrder.getAmount().longValue()*0.1+"");
        projectPrice.setText("￥"+auctionOrder.getAmount()+"");
        auctionMoney.setText("￥"+auctionOrder.getAmount()+"");
        realMoney.setText("￥"+auctionOrder.getFinalPayment()+"");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    //计算加价幅度
    private int getAuctionPrice(long price) {
        //计算加价幅度
        if (price <= 499) {
            return 10;
        } else if (price >= 500 && price <= 999) {
            return 50;
        } else if (price >= 1000 && price <= 4999) {
            return 100;
        } else if (price >= 5000 && price <= 9999) {
            return 200;
        } else if (price >= 10000 && price <= 29999) {
            return 500;
        } else if (price >= 30000 && price <= 99999) {
            return 1000;
        } else if (price >= 100000) {
            return 2000;
        }
        return 0;
    }
    @OnClick({R.id.aass_tv_edit,R.id.aaos_ib_back,R.id.aass_ll_address})
    public void back(View view){
        switch (view.getId()){
            case R.id.aaos_ib_back:
                finish();
                break;
            case R.id.aass_tv_edit:
                Intent intent = new Intent(this,CommitFinalPriceActivity.class);
                intent.putExtra("artWorkId",auctionOrder.getArtwork().getId()+"");
                intent.putExtra("finalPrice",auctionOrder.getFinalPayment()+"");
                startActivity(intent);
                break;
            case R.id.aass_ll_address:
                Intent intent1 = new Intent(this, ReceiverAdressActivity.class);
                startActivity(intent1);
                break;
        }
    }
    private void initAddress() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "listAddress.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(AuctionOrderPayMoneyActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                System.out.println(response);
                List<ConsumerAddress> addressComment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(
                        response.get("consumerAddressList")), new TypeToken<List<ConsumerAddress>>() {}.getType());
                if ( addressComment != null && addressComment.size() == 0) {
                    noAddress.setVisibility(View.VISIBLE);
                    go.setVisibility(View.GONE);
                    address.setVisibility(View.GONE);
                    noAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(),NewAddressActivity.class));
                        }
                    });
                    noAddressBollean=false;
                }else {
                    noAddress.setVisibility(View.GONE);
                    go.setVisibility(View.VISIBLE);
                    address.setVisibility(View.VISIBLE);
                    addressName.setText(addressComment.get(0).getConsignee());
                    addressPhone.setText(addressComment.get(0).getPhone()+"");
                    addressDetail.setText(addressComment.get(0).getDetails()+"");
                    noAddressBollean=true;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initAddress();
    }
}

