package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yxh.ryt.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class UserChongZhiActivity extends BaseActivity{
    private String remainMoney;
    @Bind(R.id.ucz_tv_remainMoney)
    TextView tv_remainMoney;
    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserChongZhiActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_chongzhi);
        ButterKnife.bind(this);
        remainMoney = getIntent().getStringExtra("remainMoney");
        tv_remainMoney.setText(remainMoney);
    }
    @OnClick(R.id.ib_top_lf)
    public void back() {
        finish();
    }
}
