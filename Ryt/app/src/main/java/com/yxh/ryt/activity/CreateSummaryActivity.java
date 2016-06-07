package com.yxh.ryt.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.LinearLayout;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.JsInterface;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.User;

import java.util.HashMap;
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
private int a ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createsummary);
        back = (ImageButton) findViewById(R.id.ib_top_lf);
        share = (ImageButton) findViewById(R.id.ib_top_rt);
        tab1 = (LinearLayout) findViewById(R.id.rl_tab_01);
        tab2 = (LinearLayout) findViewById(R.id.rl_tab_02);
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        webView = (WebView) findViewById(R.id.acs_wb_all);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/A2.html");
        webView.addJavascriptInterface(new JavaInterfaceDemo(), "demo");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_top_lf:
                finish();
                break;
            case R.id.ib_top_rt:
                showShareDialog();
                break;
            case R.id.rl_tab_01:
                startActivity(new Intent(this,CommentActivity.class));
                break;
            case R.id.rl_tab_02:
                startActivity(new Intent(this,InvestActivity.class));
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
            });
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
}
