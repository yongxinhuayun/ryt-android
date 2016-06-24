package com.yxh.ryt.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
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
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.JsInterface;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/6.
 */
public class CreateSummaryActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    private JsInterface jsInterface = new JsInterface();
    private ImageButton back;
    private ImageButton share;
    IWXAPI api;
    private LinearLayout tab1;
    private LinearLayout tab2;
    private TextView top;
    private String id;
    private String name;
    private List<User> users;
    private ImageView dianzan;
    private LinearLayout comment;
    private boolean isPraise1;
    private TextView zan;
    protected static final int PRAISE_SUC = 100;
    private String artworkId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PRAISE_SUC:
                    int a = Integer.parseInt(zan.getText().toString());
                    a++;
                    zan.setText(a+"");
                    break;
            }
        }
    };
    private LinearLayout ll_Praise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createsummary);
        back = (ImageButton) findViewById(R.id.ib_top_lf);
        share = (ImageButton) findViewById(R.id.ib_top_rt);
        dianzan = (ImageView) findViewById(R.id.iv_tab_01);
        comment = (LinearLayout) findViewById(R.id.ll_comment);
        zan = (TextView) findViewById(R.id.rzxq_tv_zan);
        ll_Praise = (LinearLayout) findViewById(R.id.ll_dianzan);
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        dianzan.setOnClickListener(this);
        ll_Praise.setOnClickListener(this);
        comment.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent != null) artworkId = intent.getStringExtra("id");
        webView = (WebView) findViewById(R.id.acs_wb_all);
        top = (TextView) findViewById(R.id.tv_top_ct);
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        top.setText(name);
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("javascript:initPage('" + id + "','" + AppApplication.gUser.getId() + "')");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadData(0, 1);
        webView.loadUrl("file:///android_asset/A2.html");
        webView.addJavascriptInterface(new JavaInterfaceDemo(), "demo");
    }
    private void LoadData(int tabtype, int pageNum) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artWorkId", artworkId + "");
        if (!"".equals(AppApplication.gUser.getId())) {
            paramsMap.put("currentUserId", AppApplication.gUser.getId());
        } /*else {
            paramsMap.put("currentUserId", AppApplication.gUser.getId());
        }*/
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "artWorkCreationView.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                Map<String, Object> object = (Map<String, Object>) response.get("object");
                if (object != null) {

                    isPraise1 = Boolean.parseBoolean(AppApplication.getSingleGson().toJson(object.get("isPraise")));
                    Artwork artwork = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artwork")), Artwork.class);

                    if (isPraise1) {
                        dianzan.setImageResource(R.mipmap.dianzanhou);
                        dianzan.setEnabled(false);
                    }
                    if (artwork!=null){
                        zan.setText(artwork.getPraiseNUm() + "");
                    }

                }

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_top_lf:
                finish();
                break;
            //分享
            case R.id.ib_top_rt:
                showShareDialog();
                break;
            //点赞
            case R.id.ll_dianzan:
                if ("".equals(AppApplication.gUser.getId())){
                    Intent intent2=new Intent(this,LoginActivity.class);
                    startActivity(intent2);
                }else {
                    if (!isPraise1){
                        AnimationSet animationSet=new AnimationSet(true);
                        ScaleAnimation scaleAnimation=new ScaleAnimation(1,1.5f,1,1.5f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                        scaleAnimation.setDuration(200);
                        animationSet.addAnimation(scaleAnimation);
                        animationSet.setFillAfter(true);
                        animationSet.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                AnimationSet animationSet = new AnimationSet(true);
                                ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f, 1f, 1.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                scaleAnimation.setDuration(200);
                                animationSet.addAnimation(scaleAnimation);
                                animationSet.setFillAfter(true);
                                dianzan.startAnimation(animationSet);
                                dianzan.setEnabled(false);
                                praise(artworkId,AppApplication.gUser.getId()+"");
                            }
                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        dianzan.setImageResource(R.mipmap.dianzanhou);
                        dianzan.startAnimation(animationSet);
                    }
                }
                break;
            //评论
            case R.id.ll_comment:
                if ("".equals(AppApplication.gUser.getId())) {
                    Intent intent2 = new Intent(this, LoginActivity.class);
                    startActivity(intent2);
                } else {
                    Intent intent = new Intent(this, ProjectCommentReply.class);
                    intent.putExtra("fatherCommentId", "");
                    intent.putExtra("messageId", "");
                    intent.putExtra("name", top.getText().toString());
                    intent.putExtra("flag", 2);
                    intent.putExtra("artworkId", artworkId);
                    intent.putExtra("currentUserId", AppApplication.gUser.getId());
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    class JavaInterfaceDemo {
        @JavascriptInterface
        public void clickOnAndroid(final  String id) {
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
                }

                @Override
                public void onResponse(Map<String, Object> response) {
                    if ("0".equals(response.get("resultCode"))){
                       Map<Object,Object> data= (Map<Object, Object>) response.get("data");
                        User user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(data.get("user")), User.class);
                        if (user!=null){
                            if (user.getMaster()!=null){
                                Intent intent =new Intent(CreateSummaryActivity.this,UserYsjIndexActivity.class);
                                intent.putExtra("userId", id);
                                intent.putExtra("currentId", AppApplication.gUser.getId());
                                CreateSummaryActivity.this.startActivity(intent);
                            }else {
                                Intent intent =new Intent(CreateSummaryActivity.this,UserPtIndexActivity.class);
                                intent.putExtra("userId", id);
                                intent.putExtra("currentId", AppApplication.gUser.getId());
                                CreateSummaryActivity.this.startActivity(intent);
                            }
                        }
                    }
                }
            });
        }
        @JavascriptInterface
        public String fetchParamObject() {
            return "{\"artWorkId\":\""+id+"\",\"currentUserId\":\""+AppApplication.gUser.getId()+"\"}";
        }
        @JavascriptInterface
        public void comment(String artworkId,String currentUserId,String messageId,String fatherCommentId,String name) {
            if ("undefined".equals(fatherCommentId)){
                if ("".equals(AppApplication.gUser.getId())) {
                    Intent intent2 = new Intent(CreateSummaryActivity.this, LoginActivity.class);
                    CreateSummaryActivity.this.startActivity(intent2);
                }else {
                    Intent intent = new Intent(CreateSummaryActivity.this, ProjectCommentReply.class);
                    intent.putExtra("fatherCommentId", "");
                    intent.putExtra("messageId", messageId);
                    intent.putExtra("flag", 1);
                    intent.putExtra("artworkId", artworkId);
                    intent.putExtra("currentUserId", AppApplication.gUser.getId());
                    CreateSummaryActivity.this.startActivity(intent);
                }
            }else {
                if ("".equals(AppApplication.gUser.getId())) {
                    Intent intent2 = new Intent(CreateSummaryActivity.this, LoginActivity.class);
                    CreateSummaryActivity.this.startActivity(intent2);
                } else {
                    Intent intent = new Intent(CreateSummaryActivity.this, ProjectCommentReply.class);
                    intent.putExtra("fatherCommentId", fatherCommentId);
                    intent.putExtra("messageId", messageId);
                    intent.putExtra("flag", 0);
                    intent.putExtra("name", name);
                    intent.putExtra("artworkId", artworkId);
                    intent.putExtra("currentUserId", AppApplication.gUser.getId());
                    CreateSummaryActivity.this.startActivity(intent);
                }
            }
        }
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
    private void shareWx(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://baidu.com";
        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = "title";
        msg.description = getResources().getString(
                R.string.app_name);
        Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                R.mipmap.logo_qq);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req reqShare = new SendMessageToWX.Req();
        reqShare.transaction = String.valueOf(System.currentTimeMillis());
        reqShare.message = msg;
        reqShare.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;

        api.sendReq(reqShare);

    }

    private void praise(String artworkId, String s) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artworkId", artworkId+"");
        paramsMap.put("currentUserId", s);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "artworkPraise.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    ToastUtil.showLong(getApplicationContext(), "点赞成功");
                    Message msg = Message.obtain();
                    msg.what = PRAISE_SUC;
                    handler.sendMessage(msg);
                }
            }
        });
    }
}
