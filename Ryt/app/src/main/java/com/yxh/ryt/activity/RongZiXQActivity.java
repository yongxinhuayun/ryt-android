package com.yxh.ryt.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.RongZiXqTabPageIndicatorAdapter;
import com.yxh.ryt.fragment.RongZiXiangQingTab01Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab02Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab03Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab04Fragment;
import com.yxh.ryt.util.ToastUtil;

import java.util.ArrayList;

import wuhj.com.mylibrary.StickHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

public class RongZiXQActivity extends BaseActivity {

    public static void openActivity(Activity activity){
        activity.startActivity(new Intent(activity, RongZiXQActivity.class));
    }
    ArrayList<Fragment> mFragmentList;
    ViewPager mViewPager;
    StickHeaderViewPagerManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rongzi_xiangqing);
        mViewPager = (ViewPager)findViewById(R.id.v_scroll);
        ((ImageView)findViewById(R.id.cl_01_tv_prc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(AppApplication.getSingleContext(),"dddddddddddddddd");
            }
        });
        StickHeaderLayout shl_root = (StickHeaderLayout)findViewById(R.id.shl_root);
        manager = new StickHeaderViewPagerManager(shl_root,mViewPager);
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(RongZiXiangQingTab01Fragment.newInstance(manager, 0,false));
        mFragmentList.add(RongZiXiangQingTab02Fragment.newInstance(manager, 1,false));
        mFragmentList.add(RongZiXiangQingTab03Fragment.newInstance(manager, 2,false));
        mFragmentList.add(RongZiXiangQingTab04Fragment.newInstance(manager, 3,false));
        RongZiXqTabPageIndicatorAdapter pagerAdapter=new RongZiXqTabPageIndicatorAdapter(getSupportFragmentManager(),mFragmentList);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(pagerAdapter);
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
    }

}

