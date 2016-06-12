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
import android.widget.TextView;

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
public class AuctionOrderActivity extends BaseActivity {
    private WebView webView;
    private JsInterface jsInterface = new JsInterface();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctionorder);
        webView = (WebView) findViewById(R.id.aao_wb_all);
        /*id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");*/
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/A5-1.html");
        webView.addJavascriptInterface(new JavaInterfaceDemo(), "demo");
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
                            Intent intent =new Intent(AuctionOrderActivity.this,UserYsjIndexActivity.class);
                            intent.putExtra("userId", id);
                            intent.putExtra("currentId", AppApplication.gUser.getId());
                            AuctionOrderActivity.this.startActivity(intent);
                        }else {
                            Intent intent =new Intent(AuctionOrderActivity.this,UserPtIndexActivity.class);
                            intent.putExtra("userId", id);
                            intent.putExtra("currentId", AppApplication.gUser.getId());
                            AuctionOrderActivity.this.startActivity(intent);
                        }
                    }
                }
            });
        }
    }

}
