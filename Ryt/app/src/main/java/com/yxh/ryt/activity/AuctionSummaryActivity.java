package com.yxh.ryt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageButton;

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
public class AuctionSummaryActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    private JsInterface jsInterface = new JsInterface();
    private String id;
    private String name;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctionsummary);
        webView = (WebView) findViewById(R.id.aas_wb_all);
        back = (ImageButton) findViewById(R.id.ib_top_lf);
        back.setOnClickListener(this);
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        webView.getSettings().setJavaScriptEnabled(true);
        Log.d("xxxxxxxxxxxxxxxx", id);
        webView.loadUrl("file:///android_asset/A3-1.html");
        webView.addJavascriptInterface(new JavaInterfaceDemo(), "demo1");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回
            case R.id.ib_top_lf:
                finish();
                break;
            default:
                break;
        }
    }

    class JavaInterfaceDemo {
        @JavascriptInterface
        public void clickOnAndroid1(final  String id) {
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
                            Intent intent =new Intent(AuctionSummaryActivity.this,UserYsjIndexActivity.class);
                            intent.putExtra("userId", id);
                            intent.putExtra("currentId", AppApplication.gUser.getId());
                            AuctionSummaryActivity.this.startActivity(intent);
                        }else {
                            Intent intent =new Intent(AuctionSummaryActivity.this,UserPtIndexActivity.class);
                            intent.putExtra("userId", id);
                            intent.putExtra("currentId", AppApplication.gUser.getId());
                            AuctionSummaryActivity.this.startActivity(intent);
                        }
                    }
                }
            });
        }
        @JavascriptInterface
        public String fetchParamObject1() {
            return "{\"artWorkId\":\""+id+"\",\"currentUserId\":\""+AppApplication.gUser.getId()+"\"}";
        }
        @JavascriptInterface
        public void  finalPayment(final String price,final String action,final String artWorkId) {
            Map<String,String> paramsMap=new HashMap<>();
            //paramsMap.put("userId", AppApplication.gUser.getId());
            paramsMap.put("userId", "imhfp1yr4636pj49");
            paramsMap.put("money", price);
            paramsMap.put("action", action);
            paramsMap.put("type", "1");
            paramsMap.put("artWorkId", artWorkId);
            paramsMap.put("addressId","ip6e59sx17aseim2");
            paramsMap.put("timestamp", System.currentTimeMillis() + "");
            paramsMap.put("auctionMoney", "6666");
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
    }
}
