package com.yxh.ryt.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.UserPtTabPageIndicatorAdapter;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.fragment.UserJianJieFragment;
import com.yxh.ryt.fragment.UserTouGuoFragment;
import com.yxh.ryt.fragment.UserZanGuoFragment;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import wuhj.com.mylibrary.StickHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

public class UserPtIndexActivity extends BaseActivity implements StickHeaderViewPagerManager.OnListViewScrollListener {
    private String userId;
    private String currentId;
    private boolean homeFlag;

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
    @Bind(R.id.uh1_ll_other)
    LinearLayout other;
    @Bind(R.id.uh1_iv_privateLetter)
    ImageView letter;
    @Bind(R.id.uh1_iv_attention)
    ImageView attention;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_pt_index);
        ButterKnife.bind(this);
        userId = getIntent().getStringExtra("userId");
        currentId = getIntent().getStringExtra("currentId");
        mViewPager = (ViewPager) findViewById(R.id.user_pt_pager);
        StickHeaderLayout root = (StickHeaderLayout) findViewById(R.id.user_pt_root);
        manager = new StickHeaderViewPagerManager(root, mViewPager);
        manager.setOnListViewScrollListener(this);
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(UserTouGuoFragment.newInstance(manager, 0, false, userId, currentId));
        mFragmentList.add(UserZanGuoFragment.newInstance(manager, 1, false,userId,currentId));
        mFragmentList.add(UserJianJieFragment.newInstance(manager, 2, false,userId));
        UserPtTabPageIndicatorAdapter pagerAdapter = new UserPtTabPageIndicatorAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.user_pt_indicator);
        indicator.setViewPager(mViewPager);
        if (!userId.equals(currentId)){
            other.setVisibility(View.GONE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (AppApplication.gUser!=null && AppApplication.gUser.getId()!=null && !"".equals(AppApplication.gUser.getId())){
            Map<String,String> paramsMap=new HashMap<>();
            paramsMap.put("userId", userId);
            paramsMap.put("currentId", currentId);
            paramsMap.put("pageIndex", "1");
            paramsMap.put("pageSize", "20");
            paramsMap.put("timestamp", System.currentTimeMillis() + "");
            try {
                paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
            } catch (Exception e) {
                e.printStackTrace();
            }
            NetRequestUtil.post(Constants.BASE_PATH + "my.do", paramsMap, new RegisterCallBack() {
                @Override
                public void onError(Call call, Exception e) {
                    System.out.println("失败了");
                }

                @Override
                public void onResponse(Map<String, Object> response) {
                    if (!response.get("resultCode").equals("0")) {
                        ToastUtil.showShort(AppApplication.getSingleContext(), "注册失败!");
                        return;
                    }
                    if (response.get("resultCode").equals("0")) {
                        Map<String, Object> pageInfo = (Map<String, Object>) response.get("pageInfo");
                        final User user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(pageInfo.get("user")), User.class);
                        boolean followed = (boolean) pageInfo.get("followed");
                        if (followed){
                            attention.setImageResource(R.mipmap.guanzhuhou);
                        }else {
                            attention.setImageResource(R.mipmap.guanzhuqian);
                        }
                        if (user != null) {
                            setLoginedViewValues(user);
                            if (!currentId.equals(userId)){
                                letter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        letterTrans(user);
                                    }
                                });
                            }
                        }
                    }

                }
            });
        }else {
//            ButterKnife.apply(linearLayouts, ISVISIBLE, 1);
            setLoginViewValues();
            return;
        }
    }
    private void letterTrans(User user) {
        Intent intent=new Intent(this,MsgActivity.class);
        intent.putExtra("userId",currentId);
        intent.putExtra("currentName",AppApplication.gUser.getName());
        intent.putExtra("formId", userId);
        intent.putExtra("name", user.getName());
        startActivity(intent);
    }
    //登录成功设置控件元素的值
    private void setLoginedViewValues(User user) {
        AppApplication.displayImage(user.getPictureUrl(),rsIvHeadPortrait);
        tvUserHeaderName.setText(user.getName()+"");
        tvUserHeaderFsNum.setText(user.getCount1()+"");
        tvUserHeaderGzNum.setText(user.getCount()+"");
        tvUserHeaderTxt.setText(user.getUserBrief()==null?"一句话20字以内":user.getUserBrief().getContent()+"");
        tvUserHeaderJeValue01.setText("￥"+user.getInvestsMoney());
        tvUserHeaderJeValue02.setText("￥"+user.getRoiMoney());
        tvUserHeaderJeValue03.setText(0==user.getRate()?"0%":user.getRate()*100+"%");
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
    @OnClick(R.id.ib_top_lf)
    public void back(){
        finish();
    }

    @Override
    public void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        homeFlag=firstVisibleItem+visibleItemCount==totalItemCount+1;
    }

    @Override
    public void onListViewScrollStateChanged(AbsListView view, int scrollState) {
        if (R.id.fit_lstv==view.getId() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&homeFlag){
            Intent intent = new Intent("android.intent.action.CAST_BROADCAST");
            sendBroadcast(intent);
        }else if (R.id.fiz_lstv==view.getId() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&homeFlag){
            Intent intent = new Intent("android.intent.action.PRAISE_BROADCAST");
            sendBroadcast(intent);
        }
    }
}

