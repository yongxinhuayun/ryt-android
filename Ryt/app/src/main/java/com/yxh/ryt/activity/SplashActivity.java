package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.util.SPUtil;


public class SplashActivity extends Activity {

    private RelativeLayout mRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        startAnimation();
    }
    /**
     * 初始化控件
     */
    private void initView() {
        mRoot = (RelativeLayout) findViewById(R.id.rel_splash_root);
    }
    /**
     * 设置动画
     */
    private void startAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);//动画的持续时间
        scaleAnimation.setFillAfter(true);//设置是否保持动画结束的状态，true：保持，false：不保持
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);//从透明到不透明效果
        alphaAnimation.setDuration(2000);//动画的持续时间
        alphaAnimation.setFillAfter(true);//设置是否保持动画结束的状态，true：保持，false：不保持


        AnimationSet animationSet = new AnimationSet(true);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        //执行动画
        mRoot.startAnimation(animationSet);

        //设置动画的监听，监听当动画结束的时候，跳转界面
        animationSet.setAnimationListener(animationListener);
    }
    /**
     * 动画监听
     */
    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        //动画开始的时候调用
        @Override
        public void onAnimationStart(Animation animation) {

        }
        //动画重复执行调用
        @Override
        public void onAnimationRepeat(Animation animation) {



        }
        //动画结束调用的方法
        @Override
        public void onAnimationEnd(Animation animation) {
            //跳转界面
            //判断，如果是第一次打开，跳转引导页面，如果不是第一次打开，跳转到首页
            boolean isfirstenter = (boolean) SPUtil.get(getApplicationContext(), Constants.ISFIRSTENTER, true);
            if (isfirstenter) {
                //跳转到引导界面
                startActivity(new Intent(SplashActivity.this,GuideActivity.class));
            }else{
                //跳转到首页
                startActivity(new Intent(SplashActivity.this,IndexActivity.class));
            }
            //跳转完成，移除splash界面
            finish();
        }
    };


}
