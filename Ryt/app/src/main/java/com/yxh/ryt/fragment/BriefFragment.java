package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.EditBriefActivity;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.JustifyTextView;
import com.yxh.ryt.custemview.ScaleScreenImageView;
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
 * Created by Administrator on 2016/7/11.
 */
@SuppressLint("ValidFragment")
public class BriefFragment extends BaseFragment {
    private String userId;
    @Bind(R.id.fb_siv_picture)
    public ScaleScreenImageView imageView;
    @Bind(R.id.fb_tv_name)
    public TextView name;
    @Bind(R.id.fb_jtv_content)
    public JustifyTextView content;
    @Bind(R.id.fub_tv_edit)
    TextView edit;
    public BriefFragment( String userId) {
        super();
        this.userId=userId;
    }

    public BriefFragment() {
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("userId",userId);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.getString("userId")!=null) {
            userId=savedInstanceState.getString("userId");

        }
    }

    private void LoadData() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userId",userId);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "intro.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    Map<String,Object> userBrief = (Map<String, Object>) response.get("userBrief");
                    if (userBrief!=null && userBrief.get("content")!=null){
                        content.setText(userBrief.get("content").toString());
                    }
                    //content.setVisibility(View.GONE);
                    User user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("user")), User.class);
                    if (user!=null && user.getPictureUrl()!=null){
                        AppApplication.displayImage(user.getPictureUrl(),imageView);
                    }else {
                        imageView.setVisibility(View.GONE);
                    }
                    if (user!=null){
                        name.setText(user.getName());
                    }
                    if (AppApplication.gUser.getId().equals(user.getId())){
                        edit.setVisibility(View.VISIBLE);
                    }else {
                        edit.setVisibility(View.GONE);
                    }
                }

            }
        });
    }
    @OnClick(R.id.fub_tv_edit)
    public void edit(){
        Intent intent=new Intent(getActivity(),EditBriefActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.fragment_brief, container, false);
        ButterKnife.bind(this, contextView);
        return contextView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    protected void lazyLoad() {
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadData();
    }
}
