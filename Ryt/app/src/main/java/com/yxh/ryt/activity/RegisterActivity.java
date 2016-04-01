package com.yxh.ryt.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.obsever.Smsobserver;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Sha1;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.PasswordValidation;
import com.yxh.ryt.validations.UserNameValidation;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class RegisterActivity extends BaseActivity {
    @Bind(R.id.rg_et_phone)
    EditText eTPhone;
    @Bind(R.id.rg_et_password)
    EditText eTPassword;
    @Bind(R.id.rg_et_verifyCode)
    EditText eTVerfyCode;
    @Bind(R.id.rg_et_passwordAgain)
    EditText eTPasswordAgain;
    @Bind(R.id.rg_bt_verifyCode)
    TextView getVerifyCode;
    private Map<String,Object> paramsMap;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    int time = (Integer) msg.obj;
                    if (time > 0) {
                        getVerifyCode.setText("重新获取验证码（" + time + ")");
                    } else {
                        getVerifyCode.setText("重新获取验证码");
                        getVerifyCode.setEnabled(true);
                    }
                    break;
                case 1:
                    String code = (String) msg.obj;
                    Log.d("++++++++++++++++", code);
                    eTVerfyCode.setText(code);
                    break;
            }

        }
    };
    private Smsobserver smsObserver;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.bind(this);/*启用注解绑定*/
        smsBackfill();
        event();
    }

    private void event() {

        eTVerfyCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    AppApplication.getSingleEditTextValidator()
                            .add(new ValidationModel(eTPhone, new UserNameValidation()))
                            .execute();
                    //表单没有检验通过直接退出方法
                    if (!AppApplication.getSingleEditTextValidator().validate()) {
                        return;
                    }
                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put("username", eTPhone.getText().toString());
                    paramsMap.put("code", eTVerfyCode.getText().toString());
                    paramsMap.put("timestamp", System.currentTimeMillis() + "");
                    try {
                        paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    NetRequestUtil.post(Constants.BASE_PATH + "verifyCode.do", paramsMap, new RegisterCallBack() {
                        @Override
                        public void onError(Call call, Exception e) {
                            System.out.println("失败了");
                        }

                        @Override
                        public void onResponse(Map<String, Object> response) {
                            if (response.get("resultCode").equals("0")) {
                                ToastUtil.showShort(AppApplication.getSingleContext(), "验证码验证成功!");
                            } else {
                                ToastUtil.showShort(AppApplication.getSingleContext(), "验证码验证失败!");
                            }
                        }
                    });
                }
            }
        });
    }

    private void smsBackfill() {
        smsObserver = new Smsobserver(handler, RegisterActivity.this, new Smsobserver.SmsCallBack() {
            @Override
            public void response(String code) {
                Message message = Message.obtain();
                message.obj = code;
                message.what = 1;
                handler.sendMessage(message);
            }
        });
        getContentResolver().registerContentObserver(SMS_INBOX, true,
                smsObserver);
    }
    @OnClick(R.id.rg_tv_login)
    public void login(){
        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.rg_bt_register)
    public void register(){

        AppApplication.getSingleEditTextValidator()
                .add(new ValidationModel(eTPhone, new UserNameValidation()))
                .add(new ValidationModel(eTPassword,new PasswordValidation()))
                .execute();
        //表单没有检验通过直接退出方法
        if(!AppApplication.getSingleEditTextValidator().validate()){
            return;
        }
        if (eTVerfyCode.getText().toString().equals("")){
            ToastUtil.showShort(this,"验证码不能为空!");
            return;
        }
        if (!eTPassword.getText().toString().equals(eTPasswordAgain.getText().toString())){
            ToastUtil.showShort(this, "两次输入的密码不一致,请重新输入!");
            return;
        }
        Map<String,Object> paramsMap=new HashMap<>();
        paramsMap.put("username", eTPhone.getText().toString());
        paramsMap.put("password", Sha1.encodePassword(eTPassword.getText().toString(),"SHA"));
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "register.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (!response.get("resultCode").equals("0")) {
                    ToastUtil.showShort(AppApplication.getSingleContext(), "注册失败!");
                    return;
                }
                if (response.get("resultCode").equals("0")) {
                    ToastUtil.showShort(AppApplication.getSingleContext(), "注册成功!");
                    Intent intent=new Intent(RegisterActivity.this,RegisterScActivity.class);
                    RegisterActivity.this.startActivity(intent);
                    RegisterActivity.this.finish();
                }

            }
        });
    }
    @OnClick(R.id.rg_bt_verifyCode)
    public void sendCode(){

        AppApplication.getSingleEditTextValidator()
                .add(new ValidationModel(eTPhone,new UserNameValidation()))
                .execute();
        //表单没有检验通过直接退出方法
        if(!AppApplication.getSingleEditTextValidator().validate()){
            return;
        }
        paramsMap=new HashMap<>();
        paramsMap.put("username", eTPhone.getText().toString());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "checkUserName.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (response.get("resultCode").equals("0")) {
                    NetRequestUtil.post(Constants.BASE_PATH + "sendCode.do", paramsMap, new RegisterCallBack() {
                        @Override
                        public void onError(Call call, Exception e) {
                            System.out.println("失败了");
                        }
                        @Override
                        public void onResponse(Map<String, Object> response) {
                            ToastUtil.showShort(AppApplication.getSingleContext(), "验证码发送成功!");
                            startThrad();
                            getVerifyCode.setEnabled(false);
                        }
                    });
                } else {
                    ToastUtil.showShort(AppApplication.getSingleContext(), "该手机号已经注册,请重新输入!");
                }
            }
        });
    }
    private void startThrad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 59; i >= 0; i--) {
                    try {
                        Thread.sleep(1000);
                        Message message = Message.obtain();
                        message.obj = i;
                        message.what = 0;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
