package com.yxh.ryt.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
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
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.ConsumerAddress;
import com.yxh.ryt.vo.User;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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
    private TextView tvPay;
    private ImageView ivPay;
    private ImageView add;
    private ImageView selected;
    private TextView depositPrice;
    private RelativeLayout address;
    private LinearLayout llAdd;
    private TextView userName;
    private TextView userPhone;
    private TextView userAddress;
    private TextView auctionProtocol;
    private boolean agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID); //初始化api
        api.registerApp(Constants.APP_ID); //将APP_ID注册到微信中
        setContentView(R.layout.activity_auctionsummary);
        llPay = (LinearLayout) findViewById(R.id.ll_pay);
        tvPay = (TextView) findViewById(R.id.tv_pay);
        ivPay = (ImageView) findViewById(R.id.iv_pay);
        webView = (WebView) findViewById(R.id.aas_wb_all);
        back = (ImageView) findViewById(R.id.ib_top_lf);
        title = (TextView) findViewById(R.id.tv_top_ct);
        share = (ImageButton) findViewById(R.id.ib_top_rt);
        back.setOnClickListener(this);
        id = getIntent().getStringExtra("id");
        userId = getIntent().getStringExtra("userId");
        name = getIntent().getStringExtra("name");
        titleName = getIntent().getStringExtra("title");
        title.setText(titleName);
        llPay.setOnClickListener(this);


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
                ivPay.setImageResource(R.mipmap.commit_money);
                tvPay.setText("提交保证金");
                llPay.setBackgroundColor(Color.rgb(87,172,104));
                showPopup(llPay);
                break;
            case R.id.iv_selected:
                if (!agree) {
                selected.setImageResource(R.mipmap.commit_money);
                    llPay.setEnabled(true);
                agree = true;
                } else {
                    selected.setImageResource(R.mipmap.before);
                    llPay.setEnabled(false);
                    agree = false;
                }
        }
    }

    private void showPopup(View view) {
        // 加载pop显示的布局文件
        View contentView = View.inflate(getApplicationContext(),
                R.layout.auction_pay_pop, null);
        // 得到pop界面中的控件
        add = (ImageView) contentView.findViewById(R.id.iv_add);
        depositPrice = (TextView) contentView.findViewById(R.id.tv_price);
        auctionProtocol = (TextView) contentView.findViewById(R.id.tv_auction_protocol);
        selected = (ImageView) contentView.findViewById(R.id.iv_selected);
        address = (RelativeLayout) contentView.findViewById(R.id.rl_address);
        llAdd = (LinearLayout) contentView.findViewById(R.id.ll_add_address);
        //姓名
        userName = (TextView) contentView.findViewById(R.id.tv_name);
        userPhone = (TextView) contentView.findViewById(R.id.tv_phone);
        userAddress = (TextView) contentView.findViewById(R.id.tv_address);
        initData(id);
        initAddress();
        selected.setOnClickListener(this);
        auctionProtocol.setOnClickListener(this);

        // 弹出一个泡泡
        PopupWindow pw = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置背景：只有添加了背景后才能响应返回键的事件
        pw.setBackgroundDrawable(new ColorDrawable());
        pw.showAsDropDown(view, 0, Utils.px2dip(this,-5));
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivPay.setImageResource(R.mipmap.money);
                tvPay.setText("缴纳保证金");
                llPay.setBackgroundColor(Color.rgb(205,55,56));
            }
        });
    }
//获取并加载保证金金额
    private void initData(final String artWorkId) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artWorkId", artWorkId);
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
                depositPrice.setText("￥" + artwork.getInvestGoalMoney().divide(new BigDecimal(10),0, BigDecimal.ROUND_UP).toString() + "");

            }
        });
    }
    //加载收货地址
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
                ToastUtil.showLong(AuctionSummaryActivity.this,"网络连接超时,稍后重试!");
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
                            startActivity(new Intent(getApplicationContext(),ReceiverAdressActivity.class));
                        }
                    });
                }else {
                    llAdd.setVisibility(View.INVISIBLE);
                    address.setVisibility(View.VISIBLE);
                    userName.setText(addressComment.get(0).getConsignee());
                    userPhone.setText(addressComment.get(0).getPhone());
                    userAddress.setText(addressComment.get(0).getDetails());
                }
            }
        });

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
