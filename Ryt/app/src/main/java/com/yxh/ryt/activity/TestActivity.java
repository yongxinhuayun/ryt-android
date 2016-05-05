package com.yxh.ryt.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.yxh.ryt.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/22.
 */
public class TestActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.utf_iv_zan)
    ImageView zan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userpt__touguo_item);
        ButterKnife.bind(this);
        zan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AnimationSet animationSet=new AnimationSet(true);
        ScaleAnimation scaleAnimation=new ScaleAnimation(1,1.5f,1,1.5f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(200);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet animationSet=new AnimationSet(true);
                ScaleAnimation scaleAnimation=new ScaleAnimation(1.5f,1f,1.5f,1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                scaleAnimation.setDuration(200);
                animationSet.addAnimation(scaleAnimation);
                animationSet.setFillAfter(true);
                zan.startAnimation(animationSet);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        zan.startAnimation(animationSet);

    }
}
