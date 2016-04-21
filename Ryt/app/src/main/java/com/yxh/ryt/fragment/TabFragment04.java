package com.yxh.ryt.fragment;

import android.graphics.Color;
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
import com.yxh.ryt.activity.UserPtIndexActivity;
import com.yxh.ryt.activity.UserYsjIndexActivity;
import com.yxh.ryt.custemview.CircleImageView;

import java.util.List;

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
    @Bind({ R.id.ll_header_gz, R.id.ll_header_fs, R.id.ll_header_qm ,R.id.ll_header_value})
    List<LinearLayout> linearLayouts;
    static final ButterKnife.Setter<View, Integer> ISVISIBLE = new ButterKnife.Setter<View, Integer>() {
        @Override public void set(View view, Integer value, int index) {
            if(value==0){//显示
                view.setVisibility(View.VISIBLE);
                return;
            }
            if(value==1){//隐藏
                view.setVisibility(View.GONE);
                return;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_center, null);
        ButterKnife.bind(this, view);
        return view;
    }
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
            ButterKnife.apply(linearLayouts, ISVISIBLE, 1);
        }else{
            ButterKnife.apply(linearLayouts, ISVISIBLE, 0);
        }
    }

    //登录成功设置控件元素的值
    private void setViewValues(){

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
