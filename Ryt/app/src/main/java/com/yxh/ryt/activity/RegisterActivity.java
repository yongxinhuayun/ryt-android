package com.yxh.ryt.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.OkHttpClient;
import com.yxh.ryt.R;

import butterknife.Bind;

public class RegisterActivity extends BaseActivity {
    @Bind(R.id.rg_et_phone)
    EditText eTPhone;
    @Bind(R.id.rg_et_password)
    EditText eTPassword;
    @Bind(R.id.rg_et_verifyCode)
    EditText eTVerfyCode;
    @Bind(R.id.rg_et_passwordAgain)
    EditText eTPasswordAgain;
    private String phone;
    private OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        init();
    }

    private void init() {
        client=new OkHttpClient();
    }

    public void getCode(View view){
        phone = eTPhone.getText().toString();

    }
    public void register(View view){


    }

}
