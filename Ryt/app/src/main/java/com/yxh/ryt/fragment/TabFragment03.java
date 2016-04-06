package com.yxh.ryt.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yxh.ryt.R;
import com.yxh.ryt.activity.NotificationActivity;
import com.yxh.ryt.activity.PrivateLetterActivity;
import com.yxh.ryt.custemview.BadgeView;
import com.yxh.ryt.custemview.CircleImageView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016-4-4.
 */
public class TabFragment03 extends  BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_03, null);
        ButterKnife.bind(this, view);
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.ft3_ci_notification);
        BadgeView bv = new BadgeView(getActivity(),circleImageView);
        bv.setText("5");
        bv.setTextColor(Color.WHITE);
        bv.setTextSize(7);
        bv.setBadgePosition(BadgeView.POSITION_TOP_RIGHT); //Ĭ��ֵ
        bv.show();
        return view;
    }

    @Override
    protected void lazyLoad() {

    }
    @OnClick({ R.id.ft3_ll_notification, R.id.ft3_ll_comment, R.id.ft3_ll_privateLetter })
    public void onClick(View view){
        Intent intent=null;
        switch (view.getId()){
            case R.id.ft3_ll_notification:
                intent=new Intent(getActivity(), NotificationActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.ft3_ll_comment:
                break;
            case R.id.ft3_ll_privateLetter:
                intent=new Intent(getActivity(), PrivateLetterActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

}
