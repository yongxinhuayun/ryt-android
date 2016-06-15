package com.yxh.ryt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.fragment.UserJianJieFragment;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class EditBriefActivity extends BaseActivity implements View.OnClickListener {

    private TextView back;
    private TextView save;
    private EditText et_brief;
    private UserJianJieFragment mUserJianJieFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief);
        back = (TextView) findViewById(R.id.tv_back);
        save = (TextView) findViewById(R.id.tv_save);
        et_brief = (EditText) findViewById(R.id.et_brief);
        mUserJianJieFragment = new UserJianJieFragment();
        back.setOnClickListener(this);
        save.setOnClickListener(this);
    }
/*
    public void setMainListener(OnMainListener mainListener) {
        this.mainListener = mainListener;
    }

    private OnMainListener mainListener;

// 绑定接口

    @Override

    public void onAttachFragment(Fragment fragment) {

        try {

            mainListener = (OnMainListener)mUserJianJieFragment;

        } catch (Exception e) {

            throw new ClassCastException(this.toString() + " must implementOnMainListener");

        }

        super.onAttachFragment(fragment);

    }
    // 接口

    public interface OnMainListener {
         void onMainAction();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_save:
                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("userId", AppApplication.gUser.getId());
                paramsMap.put("type", "2");
                paramsMap.put("timestamp", System.currentTimeMillis() + "");
                try {
                    paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                paramsMap.put("content", et_brief.getText().toString());
                NetRequestUtil.post(Constants.BASE_PATH + " saveUserBrief.do", paramsMap, new RegisterCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.show(getApplicationContext(),"保存失败",0);
                    }

                    @Override
                    public void onResponse(Map<String, Object> response) {
                        ToastUtil.show(getApplicationContext(),"保存成功",0);
                       /* if(mUserJianJieFragment instanceof OnMainListener){
                            mUserJianJieFragment.onMainAction();
                        }*/
                        finish();
                    }
                });
                break;
        }
    }
}
