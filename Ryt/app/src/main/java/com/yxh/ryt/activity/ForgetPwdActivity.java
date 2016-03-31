package com.yxh.ryt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yxh.ryt.R;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/3/31.
 */
public class ForgetPwdActivity extends BaseActivity {
    @Bind(R.id.fp_et_phone)
    EditText eTPhone;
    @Bind(R.id.fp_et_password)
    EditText eTPassword;
    @Bind(R.id.fp_et_verifyCode)
    EditText eTVerfyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpasswrod);
    }
    public void sendCode(View view){


    }
    public void commit(View view){


    }
}
