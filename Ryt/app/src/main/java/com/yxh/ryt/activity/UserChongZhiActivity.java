package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yxh.ryt.R;

import butterknife.OnClick;

/**
 * Created by 吴洪杰 on 2016/4/21.
 */
public class UserChongZhiActivity extends BaseActivity{
    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserChongZhiActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_chongzhi);
    }
    @OnClick(R.id.ib_top_lf)
    public void back() {
        finish();
    }
}
