package com.yxh.ryt.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.yxh.ryt.R;

/**
 * Created by Administrator on 2016/6/24.
 */
public class AuctionProtocolActivity extends BaseActivity {
    private WebView webView;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auctionprotocol);
        webView = (WebView) findViewById(R.id.apt_wb_all);
        back = (ImageView) findViewById(R.id.ib_top_lf);
        webView.loadUrl("file:///android_asset/AuctionProtocol.html");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
