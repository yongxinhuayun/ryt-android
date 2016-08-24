package com.yxh.ryt.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.custemview.CustomDialog;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;

import java.util.HashMap;
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

        artWorkId = getIntent().getStringExtra("artWorkId");
        userId = getIntent().getStringExtra("userId");
        finalPrice = getIntent().getStringExtra("finalPrice");
        tvFinalPrice.setText("￥" + finalPrice);
        llPay.setOnClickListener(this);

        selected.setOnClickListener(this);
        auctionProtocol.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_pay:
                investMoney(artWorkId,finalPrice);
                break;
            case R.id.tv_auction_protocol:
                startActivity(new Intent(this,AuctionProtocolActivity.class));
                break;
            case R.id.iv_selected:
                if (!agree){
                selected.setImageResource(R.mipmap.commit_money);
                    agree = true;
                    llPay.setEnabled(true);
                } else {
                    selected.setImageResource(R.mipmap.before);
                    agree = false;
                    llPay.setEnabled(false);
                }
                break;

        }
    }
//支付尾款 待加地址
    public void investMoney(final String artWorkId, final String price) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("money", price);
        paramsMap.put("action", "auction");
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
}
