package com.yxh.ryt.fragment;

import android.content.Intent;
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
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.ArtistIndexActivity;
import com.yxh.ryt.activity.AttentionActivity;
import com.yxh.ryt.activity.AuctionOrderActivity;
import com.yxh.ryt.activity.FansActivity;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.activity.PublicProject01Activity;
import com.yxh.ryt.activity.UserEditZiLiaoActivity;
import com.yxh.ryt.activity.UserIndexActivity;
import com.yxh.ryt.activity.UserPtIndexActivity;
import com.yxh.ryt.activity.UserQianBaoActivity;
import com.yxh.ryt.activity.UserSettingActivity;
import com.yxh.ryt.activity.UserYiJianActivity;
import com.yxh.ryt.activity.YsjRzActivity;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
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
public class TabFragment04 extends BaseFragment {
    @Bind(R.id.rl_user_index)
    RelativeLayout rlUserIndex;
    /*@Bind(R.id.tv_user_header_gz_num)*/
    TextView tvUserHeaderGzNum;
    @Bind(R.id.tv_user_header_gz)
    TextView tvUserHeaderGz;
    @Bind(R.id.rs_iv_headPortrait)
    CircleImageView rsIvHeadPortrait;
    @Bind(R.id.tv_user_header_name)
    TextView tvUserHeaderName;
    @Bind(R.id.ll_user_header)
    LinearLayout llUserHeader;
    /*@Bind(R.id.tv_user_header_fs_num)*/
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
    @Bind(R.id.btn_lf)
    TextView btnLf;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_center, container,false);
        ButterKnife.bind(this, view);
        tvUserHeaderFsNum= (TextView) view.findViewById(R.id.tv_user_header_fs_num);
        tvUserHeaderGzNum= (TextView) view.findViewById(R.id.tv_user_header_gz_num);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    //头像点击事件
    @OnClick(R.id.ll_user_header)
    void userHeaderClick() {
        if ("".equals(AppApplication.gUser.getId())) {
            LoginActivity.openActivity(getActivity());
            return;
        } else {
            UserEditZiLiaoActivity.openActivity(getActivity());
            return;
        }
    }
    //关注点击事件
    @OnClick(R.id.ll_header_gz)
    public void fans(){
        if (!"".equals(AppApplication.gUser.getId())){
            Intent intent=new Intent(getActivity(), AttentionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("userId", AppApplication.gUser.getId() + "");
            intent.putExtra("otherUserId",AppApplication.gUser.getId()+"");
            intent.putExtra("flag","1");
            getActivity().startActivity(intent);
        }else {
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
    }
    //粉丝点击事件
    @OnClick(R.id.ll_header_fs)
    public void attention(){
        if (!"".equals(AppApplication.gUser.getId())){
            Intent intent=new Intent(getActivity(), FansActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("userId", AppApplication.gUser.getId() + "");
            intent.putExtra("otherUserId",AppApplication.gUser.getId()+"");
            intent.putExtra("flag","1");
            getActivity().startActivity(intent);
        }else {
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
    }
    //拍卖订单
    @OnClick(R.id.rl_auction)
    public void order(){
        if (!"".equals(AppApplication.gUser.getId())){
            Intent intent=new Intent(getActivity(), AuctionOrderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("userId", AppApplication.gUser.getId() + "");
            getActivity().startActivity(intent);
        }else {
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
    }

    //设置点击事件
    @OnClick(R.id.user_setting)
    void userSettingClick() {
        if ("".equals(AppApplication.gUser.getId())){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }else {
            UserSettingActivity.openActivity(getActivity());
        }
    }
    //钱包点击事件
    @OnClick(R.id.rl_02)
    void userQbClick() {
        if ("".equals(AppApplication.gUser.getId())){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }else {
            UserQianBaoActivity.openActivity(getActivity());
        }
    }
    //意见点击事件
    @OnClick(R.id.rl_yijian)
    void userYiJianClick() {
        if ("".equals(AppApplication.gUser.getId())){
            Intent intent=new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }else {
            UserYiJianActivity.openActivity(getActivity());
        }
    }
    //我的主页点击事件
    @OnClick(R.id.rl_user_index)
    public void userIndexClick() {
        if ("".equals(AppApplication.gUser.getId())) {
            LoginActivity.openActivity(getActivity());
            return;
        }
        if (AppApplication.gUser != null&&"master".equals(AppApplication.gUser.getMaster1())) {
            Intent intent=new Intent(AppApplication.getSingleContext(),ArtistIndexActivity.class);
            intent.putExtra("userId", AppApplication.gUser.getId());
            intent.putExtra("name",tvUserHeaderName.getText().toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }else if (AppApplication.gUser != null&&"".equals(AppApplication.gUser.getMaster1())){
            Intent intent=new Intent(AppApplication.getSingleContext(),UserIndexActivity.class);
            intent.putExtra("userId", AppApplication.gUser.getId());
            intent.putExtra("name",tvUserHeaderName.getText().toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppApplication.gUser!=null && AppApplication.gUser.getId()!=null && !"".equals(AppApplication.gUser.getId())){
            btnLf.setVisibility(View.VISIBLE);
            Map<String,String> paramsMap=new HashMap<>();
            paramsMap.put("userId", AppApplication.gUser.getId());
            //paramsMap.put("currentId", AppApplication.gUser.getId());
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
                    ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
                }

                @Override
                public void onResponse(Map<String, Object> response) {
                    /*if (!response.get("resultCode").equals("0")) {
                        ToastUtil.showShort(AppApplication.getSingleContext(), "失败!");
                        return;
                    }*/
                    if (response.get("resultCode").equals("0")) {
                        Map<String, Object> pageInfo = (Map<String, Object>) response.get("pageInfo");
                        User user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(pageInfo.get("user")), User.class);
                       tvUserHeaderFsNum.setText(AppApplication.getSingleGson().toJson(pageInfo.get("followNum")));
                        tvUserHeaderGzNum.setText(AppApplication.getSingleGson().toJson(pageInfo.get("num")));
                       /* Double sa = (Double) pageInfo.get("followNum");
                        int a =(int)sa;
                        tvUserHeaderGzNum.setText(a+"");*/
                        if (user != null) {
                            if ("1".equals(user.getType()) &&null!=btnLf) {
                                btnLf.setText("申请为艺术家");
                                setLoginedViewValues(1, user);
                                btnLf.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        YsjRzActivity.openActivity(getActivity());
                                    }
                                });
                                AppApplication.gUser.setMaster1("");
                            } else if ("2".equals(user.getType()) &&null!=btnLf) {
                                btnLf.setText("发起项目");
                                setLoginedViewValues(2, user);
                                btnLf.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PublicProject01Activity.openActivity(getActivity());
                                    }
                                });
                                AppApplication.gUser.setMaster1("master");
                            }else if ("0".equals(user.getType()) &&null!=btnLf) {
                                btnLf.setTextColor(Color.rgb(128,128,128));
                                btnLf.setText("申请为艺术家中");
                                btnLf.setEnabled(false);
                                setLoginedViewValues(2, user);
                                AppApplication.gUser.setMaster1("");
                            }
                        }
                    }

                }
            });
        }else {
            btnLf.setVisibility(View.GONE);
//            ButterKnife.apply(linearLayouts, ISVISIBLE, 1);
            setLoginViewValues();
            return;
        }

    }

    //登录成功设置控件元素的值
    private void setLoginedViewValues(int type,User user) {
        if (type==2) {
            AppApplication.displayImage(user.getPictureUrl(),rsIvHeadPortrait);
            tvUserHeaderName.setText(user.getName()+"");
           /* tvUserHeaderFsNum.setText(user.getCount1()+"");
            tvUserHeaderGzNum.setText(user.getCount()+"");*/
            tvUserHeaderTxt.setText(user.getUserBrief()==null?"一句话20字以内":user.getUserBrief().getSigner()+"");
            tvUserHeaderJeValue01.setText("￥"+user.getInvestsMoney());
            tvUserHeaderJeValue02.setText("￥"+user.getRoiMoney());
            tvUserHeaderJeValue03.setText(0==user.getRate()?"0%":user.getRate()*100+"%");
            tvUserHeaderJeTxt01.setText("项目总金额");
            tvUserHeaderJeTxt02.setText("项目拍卖总金额");
            tvUserHeaderJeTxt03.setText("拍卖溢价率");
        }else{
            AppApplication.displayImage(user.getPictureUrl(),rsIvHeadPortrait);
            tvUserHeaderName.setText(user.getName()+"");
            /*tvUserHeaderFsNum.setText(user.getCount1()+"");
            tvUserHeaderGzNum.setText(user.getCount()+"");*/
            tvUserHeaderTxt.setText(user.getUserBrief()==null?"一句话20字以内":user.getUserBrief().getContent()+"");
            tvUserHeaderJeValue01.setText("￥"+user.getInvestsMoney());
            tvUserHeaderJeValue02.setText("￥"+user.getRoiMoney());
            tvUserHeaderJeValue03.setText(0==user.getRate()?"0%":user.getRate()*100+"%");
            tvUserHeaderJeTxt01.setText("投资金额");
            tvUserHeaderJeTxt02.setText("投资收益");
            tvUserHeaderJeTxt03.setText("投资回报率");
        }
    }

    //未登录成功设置控件元素的值
    private void setLoginViewValues() {
        rsIvHeadPortrait.setImageResource(R.mipmap.jibenziliao_touxiang);
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
