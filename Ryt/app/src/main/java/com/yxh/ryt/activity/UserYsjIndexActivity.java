package com.yxh.ryt.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.RongZiXqTabPageIndicatorAdapter;
import com.yxh.ryt.adapter.UserYsjTabPageIndicatorAdapter;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.fragment.RongZiItemFragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab01Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab02Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab03Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab04Fragment;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.Artwork;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import wuhj.com.mylibrary.StickHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

public class UserYsjIndexActivity extends BaseActivity {
    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserYsjIndexActivity.class));
    }
    ArrayList<Fragment> mFragmentList;
    ViewPager mViewPager;
    StickHeaderViewPagerManager manager;
    private String artworkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_ysj_index);
        ButterKnife.bind(this);
        mViewPager = (ViewPager) findViewById(R.id.user_ysj_pager);
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(new RongZiItemFragment());
        mFragmentList.add(new RongZiItemFragment());
        mFragmentList.add(new RongZiItemFragment());
        mFragmentList.add(new RongZiItemFragment());
        mFragmentList.add(new RongZiItemFragment());
        UserYsjTabPageIndicatorAdapter pagerAdapter = new UserYsjTabPageIndicatorAdapter(getSupportFragmentManager(), mFragmentList);
        mFragmentList.add(RongZiXiangQingTab01Fragment.newInstance(manager, 0, false));
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(pagerAdapter);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.user_ysj_indicator);
        indicator.setViewPager(mViewPager);
    }


}

