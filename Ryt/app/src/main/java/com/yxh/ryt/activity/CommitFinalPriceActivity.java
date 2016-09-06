package com.yxh.ryt.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.custemview.CustomDialog;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.ConsumerAddress;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by YangZhenjie on 2016/8/17.
 */
public class CommitFinalPriceActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llPay;
    private TextView tvPay;
    private ImageView ivPay;
    private ImageView add;
    private ImageView selected;
    private TextView tvFinalPrice;

    private TextView auctionProtocol;
    private String artWorkId;
    private String userId;
    private String finalPrice;
    private boolean agree = false;
    private RelativeLayout address;
    private TextView userName;
    private TextView userPhone;
    private TextView userAddress;
    private LinearLayout llAdd;
    private LinearLayout finishView;
    private String consumerAddressId;
    private boolean selecte=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.auction_pay_final);

        llPay = (LinearLayout) findViewById(R.id.ll_pay);
        tvPay = (TextView) findViewById(R.id.tv_pay);
        ivPay = (ImageView) findViewById(R.id.iv_pay);
        add = (ImageView) findViewById(R.id.iv_add);
        tvFinalPrice = (TextView) findViewById(R.id.tv_price);
        auctionProtocol = (TextView) findViewById(R.id.tv_auction_protocol);
        selected = (ImageView) findViewById(R.id.iv_selected);
        address = (RelativeLayout) findViewById(R.id.rl_address);
        finishView = ((LinearLayout) findViewById(R.id.ll_finish));
        userName = (TextView) findViewById(R.id.tv_name);
        userPhone = (TextView) findViewById(R.id.tv_phone);
        userAddress = (TextView) findViewById(R.id.tv_address);
        llAdd = (LinearLayout) findViewById(R.id.ll_add_address);
        artWorkId = getIntent().getStringExtra("artWorkId");
        userId = getIntent().getStringExtra("userId");
        finalPrice = getIntent().getStringExtra("finalPrice");
        tvFinalPrice.setText("￥" + finalPrice);
        llPay.setOnClickListener(this);
        address.setOnClickListener(this);
        selected.setOnClickListener(this);
        auctionProtocol.setOnClickListener(this);
        finishView.setOnClickListener(this);
        initAddress();
        selected.setImageResource(R.mipmap.commit_money);
        llPay.setBackgroundColor(Color.rgb(202,203,205));
        agree = true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_pay:
                investMoney(artWorkId,finalPrice);
                break;
            case R.id.ll_finish:
                finish();
                break;
            case R.id.tv_auction_protocol:
                startActivity(new Intent(this,AuctionProtocolActivity.class));
                break;
            case R.id.iv_selected:
                if (!agree){
                selected.setImageResource(R.mipmap.commit_money);
                    agree = true;
                    if (selecte){
                        llPay.setEnabled(true);
                        llPay.setBackgroundColor(Color.rgb(87,172,104));
                    }else {
                        llPay.setEnabled(false);
                        llPay.setBackgroundColor(Color.rgb(202,203,205));
                    }
                } else {
                    selected.setImageResource(R.mipmap.before);
                    agree = false;
                    llPay.setEnabled(false);
                    llPay.setBackgroundColor(Color.rgb(202,203,205));
                }
                break;
            //编辑收货地址
            case R.id.rl_address:
                Intent intent = new Intent(this, ReceiverAdressActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void investMoney(final String artWorkId, final String price) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("money", price);
        paramsMap.put("action", "auction");
        paramsMap.put("type", "1");
        paramsMap.put("consumerAddressId",consumerAddressId);
        paramsMap.put("artWorkId", artWorkId);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "pay/main.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(CommitFinalPriceActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                investMoney(artWorkId,price);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }else if ("0".equals(response.get("resultCode"))){
                    ToastUtil.showLong(CommitFinalPriceActivity.this,"投资成功!");
                    finish();
                }else if ("100015".equals(response.get("resultCode"))){
                    CustomDialog.Builder builder = new CustomDialog.Builder(CommitFinalPriceActivity.this);
                    builder.setTitle("余额不足,确认要充值吗");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Map<String,String> paramsMap=new HashMap<>();
                            //paramsMap.put("userId", AppApplication.gUser.getId());
                            paramsMap.put("money", price);
                            paramsMap.put("action", "invest");
                            paramsMap.put("type", "1");
                            paramsMap.put("artWorkId", artWorkId);
                            paramsMap.put("timestamp", System.currentTimeMillis() + "");
                            try {
                                AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
                                paramsMap.put("signmsg", AppApplication.signmsg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            NetRequestUtil.post(Constants.BASE_PATH + "pay/main.do", paramsMap, new AttentionListCallBack() {
                                @Override
                                public void onError(Call call, Exception e) {
                                    e.printStackTrace();
                                    System.out.println("失败了");
                                    ToastUtil.showLong(CommitFinalPriceActivity.this,"网络连接超时,稍后重试!");
                                }

                                @Override
                                public void onResponse(Map<String, Object> response) {
                                    if ("000000".equals(response.get("resultCode"))){
                                        SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                                            @Override
                                            public void getCode(String code) {
                                                if ("0".equals(code)){
                                                    investMoney(artWorkId,price);
                                                }
                                            }
                                        });
                                        sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                                    }else {
                                        String url = response.get("url").toString();
                                        Intent intent=new Intent(CommitFinalPriceActivity.this,PayPageActivity.class);
                                        intent.putExtra("url",url);
                                        CommitFinalPriceActivity.this.startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    CustomDialog customDialog = builder.create();
                    customDialog.setCanceledOnTouchOutside(false);
                    // 设置点击屏幕Dialog不消失
                    customDialog.show();
                }
            }
        });
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
                ToastUtil.showLong(CommitFinalPriceActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                System.out.println(response);
                List<ConsumerAddress> addressComment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(
                        response.get("consumerAddressList")), new TypeToken<List<ConsumerAddress>>() {}.getType());
                if ( addressComment != null && addressComment.size() == 0) {
                    address.setVisibility(View.INVISIBLE);
                    llAdd.setVisibility(View.VISIBLE);
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(),NewAddressActivity.class));
                        }
                    });
                    selecte=false;
                    llPay.setEnabled(false);
                    llPay.setBackgroundColor(Color.rgb(202,203,205));
                }else {
                    llAdd.setVisibility(View.INVISIBLE);
                    address.setVisibility(View.VISIBLE);
                    consumerAddressId=addressComment.get(0).getId();
                    userName.setText(addressComment.get(0).getConsignee());
                    userPhone.setText(addressComment.get(0).getPhone());
                    userAddress.setText(addressComment.get(0).getDetails());
                    selecte=true;
                    if (agree){
                        llPay.setBackgroundColor(Color.rgb(87,172,104));
                        llPay.setEnabled(true);
                    }else {
                        llPay.setEnabled(false);
                        llPay.setBackgroundColor(Color.rgb(202,203,205));
                    }
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
