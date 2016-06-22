package com.yxh.ryt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;

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
public class AuctionOrderSummaryActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    private JsInterface jsInterface = new JsInterface();
    private String id;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctionordersummary);
        webView = (WebView) findViewById(R.id.aaos_wb_all);
        back = ((ImageView) findViewById(R.id.aaos_ib_back));
        id = getIntent().getStringExtra("artWorkOrderId");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/A5-3.html");
        webView.addJavascriptInterface(new JavaInterfaceDemo(), "summary");
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
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
                            Intent intent =new Intent(AuctionOrderSummaryActivity.this,UserYsjIndexActivity.class);
                            intent.putExtra("userId", id);
                            intent.putExtra("currentId", AppApplication.gUser.getId());
                            AuctionOrderSummaryActivity.this.startActivity(intent);
                        }else {
                            Intent intent =new Intent(AuctionOrderSummaryActivity.this,UserPtIndexActivity.class);
                            intent.putExtra("userId", id);
                            intent.putExtra("currentId", AppApplication.gUser.getId());
                            AuctionOrderSummaryActivity.this.startActivity(intent);
                        }
                    }
                }
            });
        }
        @JavascriptInterface
        public String fetchParamObject() {
            return "{\"artWorkOrderId\":\""+id+"\",\"currentUserId\":\""+AppApplication.gUser.getId()+"\"}";
        }
        @JavascriptInterface
        public void payMoney(final String price,final String type) {
            Map<String,String> paramsMap=new HashMap<>();
            paramsMap.put("userId", AppApplication.gUser.getId());
            paramsMap.put("money", price);
            paramsMap.put("action", type);
            paramsMap.put("type", "1");
            paramsMap.put("orderId", id);
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
                    Intent intent=new Intent(AuctionOrderSummaryActivity.this,PayPageActivity.class);
                    intent.putExtra("url",url);
                    AuctionOrderSummaryActivity.this.startActivity(intent);
                }
            });
        }
    }

}
