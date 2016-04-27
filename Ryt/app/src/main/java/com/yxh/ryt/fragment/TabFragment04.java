package com.yxh.ryt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.activity.PublicProject01Activity;
import com.yxh.ryt.activity.UserEditZiLiaoActivity;
import com.yxh.ryt.activity.UserPtIndexActivity;
import com.yxh.ryt.activity.UserQianBaoActivity;
import com.yxh.ryt.activity.UserSettingActivity;
import com.yxh.ryt.activity.UserYiJianActivity;
import com.yxh.ryt.activity.UserYsjIndexActivity;
import com.yxh.ryt.activity.YsjRzActivity;
import com.yxh.ryt.custemview.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016-4-4.
 */
public class TabFragment04 extends BaseFragment {
    @Bind(R.id.rl_user_index)
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
    @Bind(R.id.btn_lf)
    TextView btnLf;
    @Bind(R.id.user_setting)
    RelativeLayout userSetting;
    @Bind(R.id.rl_qb)
    RelativeLayout rlQb;
    @Bind(R.id.rl_yijian)
    RelativeLayout userYiJian;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_center, null);
        ButterKnife.bind(this, view);
        return view;
    }

    //头像点击事件
    @OnClick(R.id.ll_user_header)
    void userHeaderClick() {
        if (AppApplication.gUser == null) {
            LoginActivity.openActivity(getActivity());
            return;
        } else {
            UserEditZiLiaoActivity.openActivity(getActivity());
            return;
        }
    }
    //设置点击事件
    @OnClick(R.id.user_setting)
    void userSettingClick() {
        UserSettingActivity.openActivity(getActivity());
    }
    //钱包点击事件
    @OnClick(R.id.rl_qb)
    void userQbClick() {
        UserQianBaoActivity.openActivity(getActivity());
    }
    //左上角点击事件
    @OnClick(R.id.btn_lf)
    void btnLfClick() {
        if (0 == AppApplication.gUser.getUtype()) {
            YsjRzActivity.openActivity(getActivity());
            return;
        }
        if (10000 == AppApplication.gUser.getUtype()) {
            PublicProject01Activity.openActivity(getActivity());
            return;
        }
    }
    //意见点击事件
    @OnClick(R.id.rl_yijian)
    void userYiJianClick() {
        UserYiJianActivity.openActivity(getActivity());
    }
    //我的主页点击事件
    @OnClick(R.id.rl_user_index)
    void userIndexClick() {
        if (AppApplication.gUser == null) {
            LoginActivity.openActivity(getActivity());
            return;
        }
        if (0 == AppApplication.gUser.getUtype()) {
            UserPtIndexActivity.openActivity(getActivity());
            return;
        }
        if (10000 == AppApplication.gUser.getUtype()) {
            UserYsjIndexActivity.openActivity(getActivity());
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppApplication.gUser == null) {
            btnLf.setVisibility(View.GONE);
//            ButterKnife.apply(linearLayouts, ISVISIBLE, 1);
            setLoginViewValues();
            return;
        } else {
//            ButterKnife.apply(linearLayouts, ISVISIBLE, 0);
            btnLf.setVisibility(View.VISIBLE);
        }
        if (0 == AppApplication.gUser.getUtype()) {
            btnLf.setText("申请为艺术家");
            setLoginedViewValues(AppApplication.gUser.getUtype());
            return;
        }
        if (10000 == AppApplication.gUser.getUtype()) {
            btnLf.setText("发起项目");
            setLoginedViewValues(AppApplication.gUser.getUtype());
            return;
        }
    }

    //登录成功设置控件元素的值
    private void setLoginedViewValues(int type) {
        if (0 == AppApplication.gUser.getUtype()) {
            tvUserHeaderName.setText(AppApplication.gUser.getUsername());
            tvUserHeaderFsNum.setText("200");
            tvUserHeaderGzNum.setText("1000");
            tvUserHeaderTxt.setText("一句话20字以内");
            tvUserHeaderJeValue01.setText("￥300");
            tvUserHeaderJeValue02.setText("￥1000");
            tvUserHeaderJeValue03.setText("50%");
            tvUserHeaderJeTxt01.setText("投资金额");
            tvUserHeaderJeTxt02.setText("投资收益");
            tvUserHeaderJeTxt03.setText("投资回报率");
            return;
        }
        if (10000 == AppApplication.gUser.getUtype()) {
            tvUserHeaderName.setText(AppApplication.gUser.getUsername());
            tvUserHeaderFsNum.setText("5000");
            tvUserHeaderGzNum.setText("15000");
            tvUserHeaderTxt.setText("一句话20字以内");
            tvUserHeaderJeValue01.setText("￥5000");
            tvUserHeaderJeValue02.setText("￥9000");
            tvUserHeaderJeValue03.setText("80%");
            tvUserHeaderJeTxt01.setText("项目总金额");
            tvUserHeaderJeTxt02.setText("项目拍卖总金额");
            tvUserHeaderJeTxt03.setText("拍卖溢价率");
            return;
        }

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
    protected void lazyLoad() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
