package com.yxh.ryt.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.CommentActivity;
import com.yxh.ryt.activity.NotificationActivity;
import com.yxh.ryt.activity.PrivateLetterActivity;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.custemview.BadgeView;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Sha1;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.User;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016-4-4.
 */
public class TabFragment03 extends  BaseFragment {

    @Bind(R.id.ft3_ci_notification)
    CircleImageView circleNotification;
    @Bind(R.id.ft3_ci_comment)
    CircleImageView circleComment;
    @Bind(R.id.ft3_ci_privateLetter)
    CircleImageView circlePrivateLetter;
    private BadgeView bv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_03, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void lazyLoad() {

    }
    private void loadData() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userId", "ieatht97wfw30hfd");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "informationList.do", paramsMap, new LoginCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {

                if (response.get("resultCode").equals("0")){
                    if ( ((Double) response.get("noticeNum")) !=0){
                        bv = new BadgeView(getActivity(),circleNotification);
                        bv.setText(response.get("noticeNum")+"");
                        bv.setTextColor(Color.WHITE);
                        bv.setTextSize(7);
                        bv.setBadgePosition(BadgeView.POSITION_TOP_RIGHT); //默认
                        bv.show();
                    }else {
                        bv = new BadgeView(getActivity(),circleNotification);
                        bv.setVisibility(View.GONE);
                    }
                    if (((Double) response.get("commentNum")) !=0){
                        bv = new BadgeView(getActivity(),circleComment);
                        bv.setText(response.get("commentNum")+"");
                        bv.setTextColor(Color.WHITE);
                        bv.setTextSize(7);
                        bv.setBadgePosition(BadgeView.POSITION_TOP_RIGHT); //默认
                        bv.show();
                    }else {
                        bv = new BadgeView(getActivity(),circleComment);
                        bv.setVisibility(View.GONE);
                    }
                    if (((Double) response.get("messageNum")) !=0){
                        bv = new BadgeView(getActivity(),circlePrivateLetter);
                        bv.setText(response.get("messageNum")+"");
                        bv.setTextColor(Color.WHITE);
                        bv.setTextSize(7);
                        bv.setBadgePosition(BadgeView.POSITION_TOP_RIGHT); //默认
                        bv.show();
                    }else {
                        bv = new BadgeView(getActivity(),circlePrivateLetter);
                        bv.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @OnClick({ R.id.ft3_ll_notification, R.id.ft3_ll_comment, R.id.ft3_ll_privateLetter,R.id.tab03_ib_back })
    public void onClick(View view){
        Intent intent=null;
        switch (view.getId()){
            case R.id.ft3_ll_notification:
                intent=new Intent(getActivity(), NotificationActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.ft3_ll_comment:
                intent=new Intent(getActivity(), CommentActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.ft3_ll_privateLetter:
                intent=new Intent(getActivity(), PrivateLetterActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.tab03_ib_back:
                getActivity().finish();
                break;
        }
    }
}
