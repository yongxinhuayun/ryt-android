package com.yxh.ryt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yxh.ryt.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 吴洪杰 on 2016/4/11.
 */
public class PublicProject01Activity extends  BaseActivity {
    @Bind(R.id.btn_next)
    Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_project_01);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.btn_next)
    public void next(View v){
        Intent intent=new Intent(this,PublicProject02Activity.class);
        startActivity(intent);
    }
}
