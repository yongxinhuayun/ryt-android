package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.receiver.WxLoginBroadcastReciver;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SPUtil;
import com.yxh.ryt.util.Sha1;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.avalidations.EditTextValidator;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.PasswordValidation;
import com.yxh.ryt.validations.UserNameValidation;
import com.yxh.ryt.vo.PageinfoList;
import com.yxh.ryt.vo.User;
import com.yxh.ryt.vo.WxUser;
import com.yxh.ryt.wxapi.WxUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
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
            btnLogin.setBackgroundResource(R.mipmap.anniu_kedianji);
        }else {
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundResource(R.mipmap.anniu_bukedianji);
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
    /*按钮事件触发*/
    @OnClick(R.id.iv_center_wx)
    public void wxLoginClick(){
        if(WxUtil.regAndCheckWx(LoginActivity.this)){
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.WX_LOGIN_ACTION);
            mReciver = new WxLoginBroadcastReciver(new WxLoginBroadcastReciver.WxLoginCallBack() {
                @Override
                public void response(String wxUser) {
                    if (wxUser!=null){
                        WxUser user=AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(wxUser), WxUser.class);
                        Map<String,String> paramsMap=new HashMap<>();
                        paramsMap.put("nickname",user.getNickname());
                        paramsMap.put("headimgurl",user.getHeadimgurl());
                        paramsMap.put("unionid",user.getUnionid());
                        paramsMap.put("timestamp", System.currentTimeMillis() + "");
                        try {
                            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
                            paramsMap.put("signmsg", AppApplication.signmsg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        NetRequestUtil.post(Constants.BASE_PATH + "WxLogin.do.do", paramsMap, new RZCommentCallBack() {
                            @Override
                            public void onError(Call call, Exception e) {
                                e.printStackTrace();
                                System.out.println("444444失败了");
                            }
                            @Override
                            public void onResponse(Map<String, Object> response) {
                                System.out.println(response+"dudududuuuuuuuuuuuuuuuuuuuuu");
                                if ("0".equals(response.get("resultCode"))) {
                                    System.out.println(response.get("resultCode")+"dudududuuuuuuuuuuuuuuuuuuuuu");

                                }
                            }
                        });
                    }

                }
            });
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
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
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
                if (Integer.valueOf((String) response.get("resultCode")) > 0) {
                    ToastUtil.showShort(LoginActivity.this, ((String) response.get("resultMsg")));
                    return;
                }
                getUser(response);
                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("username", etUsername.getText().toString());
                paramsMap.put("password", Sha1.encodePassword(etPassword.getText().toString(), "SHA"));
                paramsMap.put("cid", JPushInterface.getRegistrationID(LoginActivity.this));
                paramsMap.put("timestamp", System.currentTimeMillis() + "");
                try {
                    AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
                    paramsMap.put("signmsg", AppApplication.signmsg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NetRequestUtil.post(Constants.BASE_PATH + "userBinding.do", paramsMap, new LoginCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {
                        System.out.println("失败了");
                    }

                    @Override
                    public void onResponse(Map<String, Object> response) {
                        ToastUtil.showLong(LoginActivity.this, "登录绑定成功");
                        Intent intent=new Intent(LoginActivity.this,IndexActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    private void getUser(Map<String, Object> response) {
        User user = new User();
        user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("userInfo")), User.class);
        if (user.getMaster()!=null){
            user.setMaster1("master");
        }else {
            user.setMaster1("");
        }
        SPUtil.put(AppApplication.getSingleContext(), "current_id", user.getId() + "");
        SPUtil.put(AppApplication.getSingleContext(), "current_username", user.getUsername()+"");
        SPUtil.put(AppApplication.getSingleContext(), "current_name", user.getName()+"");
        SPUtil.put(AppApplication.getSingleContext(), "current_sex", user.getSex()+"");
        SPUtil.put(AppApplication.getSingleContext(), "current_master", user.getMaster1()+"");
        SPUtil.put(AppApplication.getSingleContext(), "current_pictureUrl", user.getPictureUrl()+"");
        AppApplication.gUser = user;
        System.out.print(AppApplication.gUser.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReciver!=null){
            unregisterReceiver(mReciver);
        }
    }
}
