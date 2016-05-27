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

import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.RongZiXqTabPageIndicatorAdapter;
import com.yxh.ryt.adapter.UserPtTabPageIndicatorAdapter;
import com.yxh.ryt.callback.RZCommentCallBack;
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
import com.yxh.ryt.vo.ConvertWork;
import com.yxh.ryt.vo.User;

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
    private String userId;
    private String currentId;

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
    @Bind(R.id.tv_top_ct)
    TextView topTitle;
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
        userId = getIntent().getStringExtra("userId");
        currentId="";
        if (AppApplication.gUser!=null && !"".equals(AppApplication.gUser.getId())){
            currentId=AppApplication.gUser.getId();
        }else {
            currentId="";
        }
        mFragmentList.add(UserTouGuoFragment.newInstance(manager, 0, false,currentId,userId));
        mFragmentList.add(UserZanGuoFragment.newInstance(manager, 1, false,currentId,userId));
        mFragmentList.add(UserJianJieFragment.newInstance(manager, 2, false,currentId,userId));
        UserPtTabPageIndicatorAdapter pagerAdapter = new UserPtTabPageIndicatorAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.user_pt_indicator);
        indicator.setViewPager(mViewPager);
    }
    @Override
    public void onResume() {
        super.onResume();
        setLoginedViewValues();
    }

    //登录成功设置控件元素的值
    private void setLoginedViewValues() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("currentId", currentId);
        paramsMap.put("pageSize", Constants.pageSize + "");
        paramsMap.put("pageIndex", "1");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "my.do", paramsMap, new RZCommentCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("444444失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    Map<String, Object> object = (Map<String, Object>) response.get("pageInfo");
                    User user=AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("user")),User.class);
                    if (user!=null){
                        AppApplication.displayImage(user.getPictureUrl(),rsIvHeadPortrait);
                        topTitle.setText(user.getName());
                        tvUserHeaderName.setText(user.getName());
                        tvUserHeaderFsNum.setText(user.getCount1()+"");
                        tvUserHeaderGzNum.setText(user.getCount()+"");
                        tvUserHeaderTxt.setText(user.getUserBrief()==null?"一句话20字以内":user.getUserBrief().getContent());
                        tvUserHeaderJeValue01.setText("￥"+user.getInvestsMoney());
                        tvUserHeaderJeValue02.setText("￥"+user.getRoiMoney());
                        tvUserHeaderJeValue03.setText(0==user.getRate()?"0%":user.getRate()*100+"%");
                        tvUserHeaderJeTxt01.setText("投资金额");
                        tvUserHeaderJeTxt02.setText("投资收益");
                        tvUserHeaderJeTxt03.setText("投资回报率");
                    }
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

