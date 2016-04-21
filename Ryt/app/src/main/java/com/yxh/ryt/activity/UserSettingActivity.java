package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 吴洪杰 on 2016/4/21.
 */
public class UserSettingActivity extends BaseActivity {
    @Bind(R.id.rl_about)
    RelativeLayout rlAbout;
    @Bind(R.id.rl_hc)
    RelativeLayout rlHc;
    @Bind(R.id.btn_out)
    Button btnOut;

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserSettingActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(AppApplication.gUser==null){
            btnOut.setVisibility(View.GONE);
        }else{
            btnOut.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.rl_about,R.id.btn_out,R.id.rl_hc})
    void itemClick(View v){
        switch (v.getId()){
            case R.id.rl_about:
                UserSettingAboutActivity.openActivity(this);
                break;
            case R.id.btn_out:
                AppApplication.gUser=null;
                btnOut.setVisibility(View.GONE);
                Intent intent=new Intent(this, IndexActivity.class);
                intent.setAction("com.yxh.ryt.gouser");
                startActivity(intent);
                break;
            case R.id.rl_hc:

                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
