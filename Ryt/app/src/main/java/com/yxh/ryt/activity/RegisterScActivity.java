package com.yxh.ryt.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yxh.ryt.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/31.
 */
public class RegisterScActivity extends BaseActivity{
    @Bind(R.id.rs_tv_sexInput)
    TextView sexInput;
    PopupWindow window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registersucced);
        ButterKnife.bind(this);/*启用注解绑定*/
    }
    /*登录点击事件*/
    @OnClick(R.id.rs_rl_sex)
    public void sexSelect(){
        showPopwindow();
    }

    private void showPopwindow() {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.popwindowsex, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);


        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);


        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(RegisterScActivity.this.findViewById(R.id.rs_ll_view),
                Gravity.BOTTOM, 0, 0);
        //popWindow消失监听方法
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (window != null && window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
                return false;
            }
        });
        TextView man= (TextView) view.findViewById(R.id.pds_tv_man);
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexInput.setText("男");
                window.dismiss();
            }
        });
        TextView woMan= (TextView) view.findViewById(R.id.pds_tv_woman);
        woMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexInput.setText("女");
                window.dismiss();
            }
        });
    }

}
