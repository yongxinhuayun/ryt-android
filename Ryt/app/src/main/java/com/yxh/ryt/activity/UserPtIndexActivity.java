package com.yxh.ryt.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.RongZiXqTabPageIndicatorAdapter;
import com.yxh.ryt.adapter.UserPtTabPageIndicatorAdapter;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.fragment.RongZiItemFragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab01Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab02Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab03Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab04Fragment;
import com.yxh.ryt.fragment.UserJianJieFragment;
import com.yxh.ryt.fragment.UserTouGuoFragment;
import com.yxh.ryt.fragment.UserZanGuoFragment;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.Artwork;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import wuhj.com.mylibrary.StickHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

public class UserPtIndexActivity extends BaseActivity {
    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserPtIndexActivity.class));
    }
    ArrayList<Fragment> mFragmentList;
    ViewPager mViewPager;
    StickHeaderViewPagerManager manager;
    RelativeLayout rlUserIndex;
    @Bind(R.id.tv_user_header_gz_num)
    TextView tvUserHeaderGzNum;
    @Bind(R.id.tv_user_header_gz)
    TextView tvUserHeaderGz;
    @Bind(R.id.rs_iv_headPortrait)
    CircleImageView rsIvHeadPortrait;
    @Bind(R.id.tv_user_header_name)
    TextView tvUserHeaderName;
    @Bind(R.id.ll_user_header)
    LinearLayout llUserHeader;
    @Bind(R.id.tv_user_header_fs_num)
    TextView tvUserHeaderFsNum;
    @Bind(R.id.tv_user_header_fs)
    TextView tvUserHeaderFs;
    @Bind(R.id.tv_user_header_txt)
    TextView tvUserHeaderTxt;
    @Bind(R.id.tv_user_header_je_value_01)
    TextView tvUserHeaderJeValue01;
    @Bind(R.id.tv_user_header_je_txt_01)
    TextView tvUserHeaderJeTxt01;
    @Bind(R.id.tv_user_header_je_value_02)
    TextView tvUserHeaderJeValue02;
    @Bind(R.id.tv_user_header_je_txt_02)
    TextView tvUserHeaderJeTxt02;
    @Bind(R.id.tv_user_header_je_value_03)
    TextView tvUserHeaderJeValue03;
    @Bind(R.id.tv_user_header_je_txt_03)
    TextView tvUserHeaderJeTxt03;
//    @Bind({R.id.ll_header_gz, R.id.ll_header_fs, R.id.ll_header_qm, R.id.ll_header_value})
//    List<LinearLayout> linearLayouts;
//    static final ButterKnife.Setter<View, Integer> ISVISIBLE = new ButterKnife.Setter<View, Integer>() {
//        @Override
//        public void set(View view, Integer value, int index) {
//            if (value == 0) {//显示
//                view.setVisibility(View.VISIBLE);
//                return;
//            }
//            if (value == 1) {//隐藏
//                view.setVisibility(View.GONE);
//                return;
//            }
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_pt_index);
        ButterKnife.bind(this);
        mViewPager = (ViewPager) findViewById(R.id.user_pt_pager);
        StickHeaderLayout root = (StickHeaderLayout) findViewById(R.id.user_pt_root);
        manager = new StickHeaderViewPagerManager(root, mViewPager);
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(UserTouGuoFragment.newInstance(manager, 0, false));
        mFragmentList.add(UserZanGuoFragment.newInstance(manager, 1, false));
        mFragmentList.add(RongZiXiangQingTab04Fragment.newInstance(manager, 2, false));
        UserPtTabPageIndicatorAdapter pagerAdapter = new UserPtTabPageIndicatorAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.user_pt_indicator);
        indicator.setViewPager(mViewPager);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (AppApplication.gUser == null) {
            setLoginViewValues();
            return;
        }
        setLoginedViewValues();
    }

    //登录成功设置控件元素的值
    private void setLoginedViewValues() {
            tvUserHeaderName.setText(AppApplication.gUser.getUsername());
            tvUserHeaderFsNum.setText(AppApplication.gUser.getCount1()+"");
            tvUserHeaderGzNum.setText(AppApplication.gUser.getCount()+"");
            tvUserHeaderTxt.setText("null".equals(AppApplication.gUser.getUserBrief())?"一句话20字以内":AppApplication.gUser.getUserBrief());
            tvUserHeaderJeValue01.setText("￥"+AppApplication.gUser.getInvestsMoney());
            tvUserHeaderJeValue02.setText("￥"+AppApplication.gUser.getRoiMoney());
            tvUserHeaderJeValue03.setText(0==AppApplication.gUser.getRate()?"0%":AppApplication.gUser.getRate()*100+"%");
            tvUserHeaderJeTxt01.setText("投资金额");
            tvUserHeaderJeTxt02.setText("投资收益");
            tvUserHeaderJeTxt03.setText("投资回报率");

    }
    //未登录成功设置控件元素的值
    private void setLoginViewValues() {
        tvUserHeaderFsNum.setText("0");
        tvUserHeaderGzNum.setText("0");
        tvUserHeaderName.setText("游客");
        tvUserHeaderTxt.setText("一句话20字以内");
        tvUserHeaderJeValue01.setText("￥0");
        tvUserHeaderJeValue02.setText("￥0");
        tvUserHeaderJeValue03.setText("0%");
        tvUserHeaderJeTxt01.setText("投资金额");
        tvUserHeaderJeTxt02.setText("投资收益");
        tvUserHeaderJeTxt03.setText("投资回报率");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

