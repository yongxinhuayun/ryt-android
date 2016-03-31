package com.yxh.ryt.activity;

import android.os.Bundle;
import android.widget.Button;
import com.yxh.ryt.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/30.
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.btn_center_login)
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);/*启用注解绑定*/
    }
    /*登录点击事件*/
    @OnClick(R.id.btn_center_login)
    public void loginClick(){

    }
}
