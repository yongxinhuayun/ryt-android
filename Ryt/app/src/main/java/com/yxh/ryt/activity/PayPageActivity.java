package com.yxh.ryt.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.Xml;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yxh.ryt.R;
import com.yxh.ryt.util.LoadingUtil;
import com.yxh.ryt.util.URLEncoderURI;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayPageActivity extends BaseActivity {
    @Bind(R.id.app_wb_page)
    WebView page;
    private String url;
    private LoadingUtil loadingUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_page);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");
        loadingUtil = new LoadingUtil(this, this);
        loadingUtil.show();
        WebSettings webSettings = page.getSettings();
        webSettings.setJavaScriptEnabled(true);
        CookieManager manager=CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            manager.setAcceptThirdPartyCookies(page, true);
        }
        page.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if( url.startsWith("http:") || url.startsWith("https:") ) {
                    return false;
                }
                // Otherwise allow the OS to handle things like tel, mailto, etc.
                /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity( intent );*/
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingUtil.dismiss();
            }
        });
        page.loadUrl(url);
    }
    @OnClick(R.id.ib_top_lf)
    void back(){
        finish();
    }
}
