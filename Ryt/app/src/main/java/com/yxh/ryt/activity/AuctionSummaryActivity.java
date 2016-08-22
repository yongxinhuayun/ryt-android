package com.yxh.ryt.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.AuctionSummaryPageIndicatorAdapter;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.custemview.CustomDialog;
import com.yxh.ryt.fragment.AuctionSummaryFragment;
import com.yxh.ryt.fragment.BaseFragment;
import com.yxh.ryt.fragment.CAndAProjectFragment;
import com.yxh.ryt.fragment.RZDetailFragment;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.Artwork;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/6.
 */
public class AuctionSummaryActivity extends BaseActivity implements View.OnClickListener {
    private TextView top;
    private String id;
    private String name;
    List<BaseFragment> csFragments=new ArrayList<>();
    FragmentPagerAdapter csAdapter;
    private ImageView back;
    private TextView title;
    private String titleName;
    private ImageView share;
    private IWXAPI api;
    private LinearLayout llPay;
    private LinearLayout llBid;
    private String depositNum;
    private TextView tvAdd;
    private TextView tvSubtraction;
    private TextView tvBidPrice;
    private LinearLayout llPayFinal;
    private String userId;
    private TextView tvBid;
    private final int MORE_BID = 101;
    private final int LESS_BID = 102;
    private int bidPriceNum;
    private int bidOffset;
    private String finalPrice;
    private int temp;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MORE_BID:
                    temp = Integer.parseInt(tvBidPrice.getText().toString().substring(1)) + bidOffset;
                    tvBidPrice.setText("￥" + temp);
                    break;
                case LESS_BID:
                    temp = Integer.parseInt(tvBidPrice.getText().toString().substring(1)) - bidOffset;
                    tvBidPrice.setText("￥" + temp);
                    break;

            }
        }
    } ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID); //初始化api
        api.registerApp(Constants.APP_ID); //将APP_ID注册到微信中
        setContentView(R.layout.activity_auctionsummary);
        back = (ImageView) findViewById(R.id.ib_top_lf);
        share = (ImageView) findViewById(R.id.ib_top_rt);
        title = (TextView) findViewById(R.id.tv_top_ct);
        tvAdd = (TextView) findViewById(R.id.tv_add);
        //出价价格显示
        tvBidPrice = (TextView) findViewById(R.id.tv_bid_price);
        tvSubtraction = (TextView) findViewById(R.id.tv_subtraction);
        //出价按钮
        tvBid = (TextView) findViewById(R.id.tv_bid);
        llPay = (LinearLayout) findViewById(R.id.ll_pay);
        llPayFinal = (LinearLayout) findViewById(R.id.ll_pay_final);
        llBid = (LinearLayout) findViewById(R.id.ll_bid);
        top = (TextView) findViewById(R.id.csa_tv_title);
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        tvSubtraction.setOnClickListener(this);
        llPayFinal.setOnClickListener(this);
        llPay.setOnClickListener(this);
        tvBid.setOnClickListener(this);
        id = getIntent().getStringExtra("id");
        userId = getIntent().getStringExtra("userId");
        name = getIntent().getStringExtra("title");
        top.setText(name);
        csFragments.add(new AuctionSummaryFragment(id));
        csFragments.add(new CAndAProjectFragment(id));
        csFragments.add(new RZDetailFragment(id));
        csAdapter = new AuctionSummaryPageIndicatorAdapter(this.getSupportFragmentManager(),csFragments);
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(csAdapter);
        //实例化TabPageIndicator然后设置ViewPager与之关联
        TabPageIndicator mindicator = (TabPageIndicator) findViewById(R.id.indicator);
        mindicator.setViewPager(pager);
        //显示缴纳保证金按钮还是出价
        initbutton();
    }

    private void initbutton() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artWorkId", id);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.get(Constants.BASE_PATH + "artWorkAuctionView.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(AuctionSummaryActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                System.out.println(response);
                Artwork artwork = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
                        toJson(response.get("artwork")), Artwork.class);
                depositNum = artwork.getInvestGoalMoney().divide(new BigDecimal(10),0, BigDecimal.ROUND_UP).toString();
                bidOffset = getAuctionPrice(Long.parseLong(artwork.getInvestGoalMoney().toString()));
                if (artwork.getNewBidingPrice() != null) {
                    //出价金额
                    bidPriceNum = Integer.parseInt(artwork.getNewBidingPrice().toString());
                    tvBidPrice.setText(bidPriceNum + "");
                }
                String isSubmitDepositPrice = (String) response.get("isSubmitDepositPrice");
                //拍卖预告
                if ("30".equals(artwork.getStep())){
                    llBid.setVisibility(View.INVISIBLE);
                    llPay.setVisibility(View.VISIBLE);
                    llPayFinal.setVisibility(View.INVISIBLE);
                    llPay.setBackgroundColor(Color.GRAY);
                    llPay.setEnabled(false);
                }else if ("31".equals(artwork.getStep())){
                    //拍卖中
                    if ("0".equals(isSubmitDepositPrice)) {
                        //已缴纳保证金
                        llBid.setVisibility(View.VISIBLE);
                        llPay.setVisibility(View.INVISIBLE);
                        llPayFinal.setVisibility(View.INVISIBLE);
                        tvBidPrice.setText("￥" + bidPriceNum);
                    } else {
                        //未缴纳保证金
                        llBid.setVisibility(View.INVISIBLE);
                        llPay.setVisibility(View.VISIBLE);
                        llPayFinal.setVisibility(View.INVISIBLE);
                        llPay.setBackgroundColor(Color.rgb(205,55,56));
                        llPay.setEnabled(true);
                    }
                }else if ("32".equals(artwork.getStep())){
                    //拍卖结束
                    if (artwork.getWinner().getId().equals(AppApplication.gUser.getId())){
                        llBid.setVisibility(View.INVISIBLE);
                        llPayFinal.setVisibility(View.VISIBLE);
                        llPay.setVisibility(View.INVISIBLE);
                        llPayFinal.setBackgroundColor(Color.rgb(205,55,56));
                        llPayFinal.setEnabled(true);
                    } else {
                        llBid.setVisibility(View.INVISIBLE);
                        llPayFinal.setVisibility(View.VISIBLE);
                        llPay.setVisibility(View.INVISIBLE);
                        llPayFinal.setBackgroundColor(Color.GRAY);
                        llPayFinal.setEnabled(false);
                    }
                }




            }
        });
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
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showShareDialog() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_share_weixin_view, null);
        // 设置style 控制默认dialog带来的边距问题
        final Dialog dialog = new Dialog(this, R.style.common_dialog);
        dialog.setContentView(view);
        dialog.show();

        // 监听
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.view_share_weixin:
                        // 分享到微信
                        shareWx(0);
                        break;

                    case R.id.view_share_pengyou:
                        // 分享到朋友圈
                        shareWx(1);
                        break;

                    case R.id.share_cancel_btn:
                        // 取消
                        break;

                }

                dialog.dismiss();
            }

        };
        ViewGroup mViewWeixin = (ViewGroup) view.findViewById(R.id.view_share_weixin);
        ViewGroup mViewPengyou = (ViewGroup) view.findViewById(R.id.view_share_pengyou);
        Button mBtnCancel = (Button) view.findViewById(R.id.share_cancel_btn);
        //mBtnCancel.setTextColor(R.none_color);
        mViewWeixin.setOnClickListener(listener);
        mViewPengyou.setOnClickListener(listener);
        mBtnCancel.setOnClickListener(listener);

        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

    }
    private void shareWx(final int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://ryt.efeiyi.com/app/shareView.do";
        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = "融艺投App";
        msg.description = "面向艺术家与大众进行艺术交流与投资的应用";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                R.mipmap.logo108);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req reqShare = new SendMessageToWX.Req();
        reqShare.transaction = String.valueOf(System.currentTimeMillis());
        reqShare.message = msg;
        reqShare.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;

        api.sendReq(reqShare);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回
            case R.id.ib_top_lf:
                finish();
                break;
            //分享
            case R.id.ib_top_rt:
                showShareDialog();
                break;
            case R.id.ll_pay:
                Intent mIntent = new Intent(this,CommitDepositPriceActivity.class);
                mIntent.putExtra("artWorkId",id);
                mIntent.putExtra("userId",userId);
                this.startActivity(mIntent);
                break;
            //出价+
            case R.id.tv_add:
                Message msg = Message.obtain();
                msg.what = MORE_BID;
                handler.sendMessage(msg);
                break;
            //出价-
            case R.id.tv_subtraction:
                if(bidPriceNum < Integer.parseInt(tvBidPrice.getText().toString().substring(1))) {
                    Message message = Message.obtain();
                    message.what = LESS_BID;
                    handler.sendMessage(message);
                }
                break;
            //出价
            case R.id.tv_bid:
                bidPrice();
                initbutton();
               // investMoney(id,tvBidPrice.getText().toString().substring(1));
                break;
            //支付尾款
            case R.id.ll_pay_final:
                finalPrice = Integer.parseInt(tvBidPrice.getText().toString().substring(1)) - Integer.parseInt(depositNum) + "";
                Intent intent = new Intent(this,CommitFinalPriceActivity.class);
                intent.putExtra("artWorkId",id);
                intent.putExtra("userId",userId);
                intent.putExtra("finalPrice",finalPrice);
                this.startActivity(intent);
                break;
            default:
                break;

        }
    }

    private void bidPrice() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artWorkId", id);
        paramsMap.put("price", tvBidPrice.getText().toString().substring(1));
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.get(Constants.BASE_PATH + "artworkBid.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(AuctionSummaryActivity.this, "网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                ToastUtil.showLong(AuctionSummaryActivity.this, "出价完成");
            }



        });
    }

    public void investMoney(final String artWorkId, final String price) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("money", price);
        paramsMap.put("action", "investAccount");
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
                ToastUtil.showLong(AuctionSummaryActivity.this,"网络连接超时,稍后重试!");
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
                    ToastUtil.showLong(AuctionSummaryActivity.this,"投资成功!");
                }else if ("100015".equals(response.get("resultCode"))){
                    CustomDialog.Builder builder = new CustomDialog.Builder(AuctionSummaryActivity.this);
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
                                    ToastUtil.showLong(AuctionSummaryActivity.this,"网络连接超时,稍后重试!");
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
                                        Intent intent=new Intent(AuctionSummaryActivity.this,PayPageActivity.class);
                                        intent.putExtra("url",url);
                                        AuctionSummaryActivity.this.startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("取消",
                            new android.content.DialogInterface.OnClickListener() {
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
