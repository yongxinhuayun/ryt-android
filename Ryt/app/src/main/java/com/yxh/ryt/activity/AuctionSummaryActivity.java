package com.yxh.ryt.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.JsInterface;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.User;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/6.
 */
public class AuctionSummaryActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    private JsInterface jsInterface = new JsInterface();
    private String id;
    private String userId;
    private String name;
    private ImageView back;
    private TextView title;
    private String titleName;
    private ImageButton share;
    private IWXAPI api;
    private LinearLayout llPay;
    private LinearLayout llBid;
    private String depositNum;
    private TextView tvAdd;
    private TextView tvSubtraction;
    private TextView bidPrice;
    private LinearLayout llPayFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID); //初始化api
        api.registerApp(Constants.APP_ID); //将APP_ID注册到微信中
        setContentView(R.layout.activity_auctionsummary);

        webView = (WebView) findViewById(R.id.aas_wb_all);
        back = (ImageView) findViewById(R.id.ib_top_lf);
        title = (TextView) findViewById(R.id.tv_top_ct);
        tvAdd = (TextView) findViewById(R.id.tv_add);
        bidPrice = (TextView) findViewById(R.id.tv_bid_price);
        tvSubtraction = (TextView) findViewById(R.id.tv_subtraction);
        llPay = (LinearLayout) findViewById(R.id.ll_pay);
        llPayFinal = (LinearLayout) findViewById(R.id.ll_pay_final);
        llBid = (LinearLayout) findViewById(R.id.ll_bid);
        share = (ImageButton) findViewById(R.id.ib_top_rt);
        back.setOnClickListener(this);
        llPay.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        tvSubtraction.setOnClickListener(this);
        llPayFinal.setOnClickListener(this);
        llPay.setOnClickListener(this);
        id = getIntent().getStringExtra("id");
        userId = getIntent().getStringExtra("userId");
        name = getIntent().getStringExtra("name");
        titleName = getIntent().getStringExtra("title");
        title.setText(titleName);

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
                String isSubmitDepositPrice = (String) response.get("isSubmitDepositPrice");
                //拍卖预告
                if ("30".equals(artwork.getStep())){
                    llBid.setVisibility(View.VISIBLE);
                    llPay.setVisibility(View.INVISIBLE);
                    llPay.setBackgroundColor(Color.GRAY);
                    llPay.setEnabled(false);
                }else if ("31".equals(artwork.getStep())){
                    //拍卖中
                    if ("0".equals(isSubmitDepositPrice)) {
                        llBid.setVisibility(View.VISIBLE);
                        llPay.setVisibility(View.INVISIBLE);
                        llPayFinal.setVisibility(View.INVISIBLE);
                    } else {
                        llBid.setVisibility(View.INVISIBLE);
                        llPay.setVisibility(View.VISIBLE);
                        llPayFinal.setVisibility(View.INVISIBLE);
                        llPay.setBackgroundColor(Color.rgb(205,55,56));
                        llPay.setEnabled(true);
                    }
                }else if ("31".equals(artwork.getStep())){
                    //拍卖结束
                    if (artwork.getWinner().getId().equals(AppApplication.gUser.getId())){
                        llBid.setVisibility(View.INVISIBLE);
                        llPayFinal.setVisibility(View.VISIBLE);
                        llPay.setVisibility(View.VISIBLE);
                        llPayFinal.setBackgroundColor(Color.rgb(205,55,56));
                        llPayFinal.setEnabled(true);
                    } else {
                        llBid.setVisibility(View.INVISIBLE);
                        llPayFinal.setVisibility(View.VISIBLE);
                        llPay.setVisibility(View.VISIBLE);
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
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/auction.html");
        webView.addJavascriptInterface(new JavaInterfaceDemo(), "demo1");
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog();
            }
        });
        initbutton();
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
            case R.id.ll_pay:
                Intent mIntent = new Intent(this,CommitDepositPriceActivity.class);
                mIntent.putExtra("artWorkId",id);
                mIntent.putExtra("userId",userId);
                this.startActivity(mIntent);
                break;
            //出价+
            case R.id.tv_add:
                break;
            //出价-
            case R.id.tv_subtraction:
                break;
            //支付尾款
            case R.id.ll_pay_final:
                break;
            default:
                break;

        }
    }



    class JavaInterfaceDemo {
        @JavascriptInterface
        public void clickOnAndroid1(String id) {
            loadData(id);
        }
        @JavascriptInterface
        public String fetchParamObject1() {
            return "{\"artWorkId\":\""+id+"\",\"currentUserId\":\""+AppApplication.gUser.getId()+"\"}";
        }
        @JavascriptInterface
        public void auctionProtocol() {
            Intent intent=new Intent(AuctionSummaryActivity.this,AuctionProtocolActivity.class);
            AuctionSummaryActivity.this.startActivity(intent);
        }
        @JavascriptInterface
        public void  finalPayment( String price, String action, String artWorkId) {
            loadData1(price,action,artWorkId);
            Map<String,String> paramsMap=new HashMap<>();
            //paramsMap.put("userId", AppApplication.gUser.getId());
            //paramsMap.put("userId", "imhfp1yr4636pj49");
            paramsMap.put("money", price);
            paramsMap.put("action", action);
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
                }

                @Override
                public void onResponse(Map<String, Object> response) {
                    String url = response.get("url").toString();
                    Intent intent=new Intent(AuctionSummaryActivity.this,PayPageActivity.class);
                    intent.putExtra("url",url);
                    AuctionSummaryActivity.this.startActivity(intent);
                }
            });
        }
        @JavascriptInterface
        public void comment(String artworkId,String currentUserId,String messageId,String fatherCommentId,String name) {
            if ("".equals(AppApplication.gUser.getId())) {
                Intent intent2 = new Intent(AuctionSummaryActivity.this, LoginActivity.class);
                AuctionSummaryActivity.this.startActivity(intent2);
            } else {
                Intent intent = new Intent(AuctionSummaryActivity.this, ProjectCommentReply.class);
                intent.putExtra("fatherCommentId", fatherCommentId);
                intent.putExtra("messageId", messageId);
                intent.putExtra("flag", 0);
                intent.putExtra("name", name);
                intent.putExtra("artworkId", artworkId);
                intent.putExtra("currentUserId", AppApplication.gUser.getId());
                AuctionSummaryActivity.this.startActivity(intent);
            }
        }
    }
    private void loadData(final String id) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userId", id);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "user.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(AuctionSummaryActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    Map<Object,Object> data= (Map<Object, Object>) response.get("data");
                    User user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(data.get("user")), User.class);
                    if (user.getMaster()!=null){
                        Intent intent =new Intent(AuctionSummaryActivity.this,ArtistIndexActivity.class);
                        intent.putExtra("userId", id);
                        intent.putExtra("name", user.getName());
                        AuctionSummaryActivity.this.startActivity(intent);
                    }else {
                        Intent intent =new Intent(AuctionSummaryActivity.this,UserIndexActivity.class);
                        intent.putExtra("userId", id);
                        intent.putExtra("name", user.getName());
                        AuctionSummaryActivity.this.startActivity(intent);
                    }
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                loadData(id);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
    private void loadData1(final String price,final String action,final String artWorkId) {
        Map<String,String> paramsMap=new HashMap<>();
        //paramsMap.put("userId", AppApplication.gUser.getId());
        paramsMap.put("money", price);
        paramsMap.put("action", action);
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
                                loadData1(price,action,artWorkId);
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

}
