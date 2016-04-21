package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yxh.ryt.R;

/**
 * Created by 吴洪杰 on 2016/4/21.
 */
public class UserSettingAboutActivity extends BaseActivity{
    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserSettingAboutActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting_about);
    }
}
