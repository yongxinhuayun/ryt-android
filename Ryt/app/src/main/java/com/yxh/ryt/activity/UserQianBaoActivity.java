package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.yxh.ryt.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 吴洪杰 on 2016/4/21.
 */
public class UserQianBaoActivity extends BaseActivity {
    @Bind(R.id.ib_top_lf)
    ImageButton ibTopLf;
    @Bind(R.id.btn_cz)
    Button btnCz;
    @Bind(R.id.btn_tx)
    Button btnTx;

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserQianBaoActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_qianbao);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_cz, R.id.btn_tx})
    void btnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cz:
                UserChongZhiActivity.openActivity(this);
                break;
            case R.id.btn_tx:
                break;
        }
    }

    @OnClick(R.id.ib_top_lf)
    public void back1() {
        finish();
    }

    @OnClick(R.id.ib_top_rt)
    public void back2() {
        finish();
    }
}