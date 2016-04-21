package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.receiver.WxLoginBroadcastReciver;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Sha1;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.avalidations.EditTextValidator;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.PasswordValidation;
import com.yxh.ryt.validations.UserNameValidation;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.User;
import com.yxh.ryt.wxapi.WxUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/3/30.
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.btn_center_login)
    Button btnLogin;
    @Bind(R.id.et_center_one)
    EditText etUsername;
    @Bind(R.id.et_center_two)
    EditText etPassword;
    @Bind(R.id.ib_top_rt)
    ImageButton ibRight;
    @Bind(R.id.iv_center_wx)
    ImageView ivWxLogin;
    @Bind(R.id.tv_center_forget)
    TextView tvForget;
    private  EditTextValidator editTextValidator;
    WxLoginBroadcastReciver mReciver;
    private boolean isPhone;
    private boolean isPassword;
    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);/*启用注解绑定*/
        btnLogin.setEnabled(false);
        clickable();
    }

    private void clickable() {
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isPhone = true;
                    dianji(isPhone, isPassword);
                } else {
                    isPhone = false;
                    dianji(isPhone, isPassword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isPassword = true;
                    dianji(isPhone, isPassword);
                } else {
                    isPassword = false;
                    dianji(isPhone, isPassword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void dianji(boolean isPhone, boolean isPassword){
        if (isPassword && isPhone){
            btnLogin.setEnabled(true);
            btnLogin.setBackgroundResource(R.mipmap.wangjimima_anniu);
        }else {
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundResource(R.mipmap.bukedianjianniu);
        }
    }
    /*返回按钮事件触发*/
    @OnClick(R.id.ib_top_rt)
    public void closeClick(){
        finish();
    }
    /*注册按钮事件触发*/
    @OnClick(R.id.tv_center_reg)
    public void regClick(){
        finish();
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
    /*忘记密码按钮事件触发*/
    @OnClick(R.id.tv_center_forget)
    public void forgetClick(){
        finish();
        Intent intent=new Intent(this,ForgetPwdActivity.class);
        startActivity(intent);
    }
    /*返回按钮事件触发*/
    @OnClick(R.id.iv_center_wx)
    public void wxLoginClick(){
        if(WxUtil.regAndCheckWx(LoginActivity.this)){
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.WX_LOGIN_ACTION);
            mReciver = new WxLoginBroadcastReciver();
            registerReceiver(mReciver, intentFilter);
            WxUtil.wxlogin();
        }
    }

    /*登录按钮点击事件触发*/
    @OnClick(R.id.btn_center_login)
    public void loginClick(){
       AppApplication.getSingleEditTextValidator()
                .add(new ValidationModel(etUsername,new UserNameValidation()))
                .add(new ValidationModel(etPassword,new PasswordValidation()))
                .execute();
        //表单没有检验通过直接退出方法
        if(!AppApplication.getSingleEditTextValidator().validate()){
            return;
        }
        LoginRequst();/*调用登录具体实现*/
    }

    //登录具体实现
    private void LoginRequst() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("username",etUsername.getText().toString());
        paramsMap.put("password", Sha1.encodePassword(etPassword.getText().toString(), "SHA"));
        paramsMap.put("timestamp",System.currentTimeMillis()+"");
        try {
            AppApplication.signmsg=EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "login.do", paramsMap, new LoginCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (Integer.valueOf((String) response.get("resultCode"))>0){
                    ToastUtil.showShort(LoginActivity.this,((String)response.get("resultMsg")));
                    return;
                }
                AppApplication.gUser=AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("userInfo")), User.class);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReciver!=null){
            unregisterReceiver(mReciver);
        }
    }
}
