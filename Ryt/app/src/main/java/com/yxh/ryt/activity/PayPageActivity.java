package com.yxh.ryt.activity;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.Xml;
import android.webkit.WebView;

import com.yxh.ryt.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_page);
        ButterKnife.bind(this);
        page.getSettings().setJavaScriptEnabled(true);
        url = getIntent().getStringExtra("url");
        page.loadUrl(url);
    }
    @OnClick(R.id.ib_top_lf)
    void back(){
        finish();
    }
}
