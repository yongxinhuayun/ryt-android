package com.yxh.ryt.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.viewpagerindicator.IcsLinearLayout;
import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.AuctionOrderIndicatorAdapter;
import com.yxh.ryt.adapter.MyProjectIndicatorAdapter;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.fragment.ArtistAuctionFragment;
import com.yxh.ryt.fragment.ArtistCheckFragment;
import com.yxh.ryt.fragment.ArtistCompletedFragment;
import com.yxh.ryt.fragment.ArtistCreateFragment;
import com.yxh.ryt.fragment.ArtistFinanceFragment;
import com.yxh.ryt.fragment.AuctionOrderAllFragment;
import com.yxh.ryt.fragment.AuctionOrderFinishFragment;
import com.yxh.ryt.fragment.AuctionOrderPaymentFragment;
import com.yxh.ryt.fragment.AuctionOrderReceiveFragment;
import com.yxh.ryt.fragment.AuctionOrderSendFragment;
import com.yxh.ryt.fragment.BaseFragment;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.JsInterface;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.HomeYSJArtWork;
import com.yxh.ryt.vo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/6.
 */
public class AuctionOrderActivity extends BaseActivity implements View.OnClickListener {
    private String id;
    private ImageView back;
    List<BaseFragment> rZFragments=new ArrayList<>();
    FragmentPagerAdapter rZAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctionorder);
        back = ((ImageView) findViewById(R.id.aao_ib_back));
        id = getIntent().getStringExtra("userId");
        back.setOnClickListener(this);
        rZFragments.add(new AuctionOrderAllFragment());
        rZFragments.add(new AuctionOrderPaymentFragment());
        rZFragments.add(new AuctionOrderSendFragment());
        rZFragments.add(new AuctionOrderReceiveFragment());
        rZFragments.add(new AuctionOrderFinishFragment());
        rZAdapter = new AuctionOrderIndicatorAdapter(this.getSupportFragmentManager(),rZFragments);
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setOffscreenPageLimit(5);
        pager.setAdapter(rZAdapter);
        //实例化TabPageIndicator然后设置ViewPager与之关联
        TabPageIndicator mindicator = (TabPageIndicator) findViewById(R.id.indicator);
        mindicator.setViewPager(pager);
        ((IcsLinearLayout) mindicator.getChildAt(0)).getChildAt(0).setPadding(Utils.dip2px(this,28),0,Utils.dip2px(this,28),0);
        ((IcsLinearLayout) mindicator.getChildAt(0)).getChildAt(1).setPadding(Utils.dip2px(this,28),0,Utils.dip2px(this,28),0);
        ((IcsLinearLayout) mindicator.getChildAt(0)).getChildAt(2).setPadding(Utils.dip2px(this,28),0,Utils.dip2px(this,28),0);
        ((IcsLinearLayout) mindicator.getChildAt(0)).getChildAt(3).setPadding(Utils.dip2px(this,28),0,Utils.dip2px(this,28),0);
        ((IcsLinearLayout) mindicator.getChildAt(0)).getChildAt(4).setPadding(Utils.dip2px(this,28),0,Utils.dip2px(this,28),0);
    }

    @Override
    public void onClick(View v) {
        finish();
    }


}
