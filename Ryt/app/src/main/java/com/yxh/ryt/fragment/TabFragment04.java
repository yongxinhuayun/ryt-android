package com.yxh.ryt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxh.ryt.R;
import com.yxh.ryt.activity.UserPtIndexActivity;
import com.yxh.ryt.activity.UserYsjIndexActivity;
import com.yxh.ryt.adapter.UserPtTabPageIndicatorAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016-4-4.
 */
public class TabFragment04 extends BaseFragment {
    @Bind(R.id.rl_user_index)
    RelativeLayout rlUserIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_center, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.rl_user_index)
    void userIndexClick(){
        UserYsjIndexActivity.openActivity(getActivity());
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
