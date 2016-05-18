package com.yxh.ryt.activity;


import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.RongZiXqTabPageIndicatorAdapter;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.fragment.RongZiXiangQingTab01Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab02Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab03Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab04Fragment;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SPUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.Artwork;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import wuhj.com.mylibrary.StickHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

public class RongZiXQActivity extends BaseActivity {
    @Bind(R.id.cl_01_tv_prc)
    ImageView cl01TvPrc;
    @Bind(R.id.cl_01_tv_title)
    TextView cl01TvTitle;
    @Bind(R.id.tv_financing)
    TextView tvFinancing;
    @Bind(R.id.cl_01_civ_headPortrait)
    CircleImageView cl01CivHeadPortrait;
    @Bind(R.id.cl_01_tv_name)
    TextView cl01TvName;
    @Bind(R.id.cl_01_tv_zhicheng)
    TextView cl01TvZhicheng;
    @Bind(R.id.cl_01_ll_zhicheng)
    LinearLayout cl01LlZhicheng;
    @Bind(R.id.cl_01_tv_brief)
    TextView cl01TvBrief;
    @Bind(R.id.iv_tab_01)
    ImageView dianzan;
    @Bind(R.id.rzxq_tv_zan)
    TextView zan;
    @Bind(R.id.tv_top_ct)
    TextView topTitle;
    private StickHeaderLayout shl_root;
    private LinearLayout touziren_ll;
    private SQLiteDatabase database;
    private String isPraise;
    private String isPraise1;

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, RongZiXQActivity.class));
    }
    ArrayList<Fragment> mFragmentList;
    ViewPager mViewPager;
    StickHeaderViewPagerManager manager;
    private String artworkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rongzi_xiangqing);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        if (intent != null) artworkId = intent.getStringExtra("id");
        mViewPager = (ViewPager) findViewById(R.id.v_scroll);
        shl_root = (StickHeaderLayout) findViewById(R.id.shl_root);
        touziren_ll = ((LinearLayout) findViewById(R.id.rzxq_ll_touziren));
        manager = new StickHeaderViewPagerManager(shl_root, mViewPager);
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(RongZiXiangQingTab01Fragment.newInstance(manager, 0, false));
        mFragmentList.add(RongZiXiangQingTab02Fragment.newInstance(manager, 1, false));
        mFragmentList.add(RongZiXiangQingTab03Fragment.newInstance(manager, 2, false,artworkId));
        mFragmentList.add(RongZiXiangQingTab04Fragment.newInstance(manager, 3, false));
        RongZiXqTabPageIndicatorAdapter pagerAdapter = new RongZiXqTabPageIndicatorAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(pagerAdapter);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int widthzong = metric.widthPixels; // 屏幕宽度（像素）
        int width= Utils.dip2px(RongZiXQActivity.this, 24);
        int height= Utils.dip2px(RongZiXQActivity.this,24);
        int right=Utils.dip2px(RongZiXQActivity.this,10);
        int count = (widthzong-(Utils.dip2px(RongZiXQActivity.this, 20)*2)) / (width + right);
        int xunHuan=0;
        for (int i=0;i<=(29/count);i++){
            LinearLayout linearLayout=new LinearLayout(RongZiXQActivity.this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            if (xunHuan<(29/count)){
                for (int j=0;j<count;j++){
                    CircleImageView imageView=new CircleImageView(RongZiXQActivity.this);
                    // 获取LayoutParams，给view对象设置宽度，高度
                    LayoutParams params = new LayoutParams(width,height);
                    params.setMargins(0,0,right,0);
                    imageView.setImageResource(R.mipmap.anniu_kedianji);
                    imageView.setLayoutParams(params);
                    linearLayout.addView(imageView);
                }
            }else if (xunHuan==(29/count)){
                for (int j=0;j<(29-(count*(xunHuan)));j++){
                    CircleImageView imageView=new CircleImageView(RongZiXQActivity.this);
                    // 获取LayoutParams，给view对象设置宽度，高度
                    LayoutParams params = new LayoutParams(width,height);
                    params.setMargins(0,0,right,0);
                    imageView.setImageResource(R.mipmap.anniu_kedianji);
                    imageView.setLayoutParams(params);
                    linearLayout.addView(imageView);
                    Log.d("6666666666",xunHuan+"");
                }
            }
            xunHuan++;
            touziren_ll.addView(linearLayout);
        }
        LoadData(0, 1);
        database= AppApplication.getDBHelper().getWritableDatabase();
        Cursor cursor = database.query("rzxq_praise", null, "project_workID=? AND current_id=?", new String[]{artworkId,"ieatht97wfw30hfd"}, null, null, null);
        while (cursor.moveToNext()) {
            isPraise = cursor.getString(cursor.getColumnIndex("isPraise"));
        }
        if ("1".equals(isPraise)){
            dianzan.setEnabled(false);
            dianzan.setImageResource(R.mipmap.dianzanhou);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @OnClick({R.id.iv_tab_02,R.id.ib_top_lf,R.id.iv_tab_01,R.id.rzxq_tv_invest})
    public void comment(View view){
        switch (view.getId()){
            case R.id.iv_tab_02:
                Intent intent=new Intent(this, ProjectCommentReply.class);
                intent.putExtra("fatherCommentId","");
                intent.putExtra("messageId","");
                intent.putExtra("flag",1);
                intent.putExtra("artworkId",artworkId);
                startActivity(intent);
                break;
           /* case R.id.rzxq_iv_touziren:

                break;*/
            case R.id.ib_top_lf:
                finish();
                break;
            case R.id.rzxq_tv_invest:
                Intent intent1=new Intent(this,InvestActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.iv_tab_01:
                if (!"1".equals(isPraise)){
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
                            AnimationSet animationSet = new AnimationSet(true);
                            ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f, 1f, 1.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            scaleAnimation.setDuration(200);
                            animationSet.addAnimation(scaleAnimation);
                            animationSet.setFillAfter(true);
                            dianzan.startAnimation(animationSet);
                            ContentValues values = new ContentValues();
                            values.put("project_workID",artworkId);
                            values.put("current_id","ieatht97wfw30hfd");
                            values.put("isPraise", "1");
                            dianzan.setEnabled(false);
                            database.insert("rzxq_praise", null, values);
                            praise(artworkId,"ieatht97wfw30hfd");

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    dianzan.setImageResource(R.mipmap.dianzanhou);
                    dianzan.startAnimation(animationSet);
                }

                break;
        }

    }

    private void praise(String artworkId, String s) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artworkId", "imysevcw2mg3l2x4");
        paramsMap.put("currentUserId", "iijq9f1r7apprtab");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "artworkPraise.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    ToastUtil.showLong(RongZiXQActivity.this,"点赞成功");
                }
            }
        });
    }

    private void LoadData(int tabtype, int pageNum) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artWorkId", artworkId+"");
        /*if ("".equals(SPUtil.get(this,"currentUserId",""))){
            paramsMap.put("currentUserId", "");
        }else {*/
            paramsMap.put("currentUserId", "iijq9f1r7apprtab");
       /* }*/
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(paramsMap.toString() + "====");
        NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkView.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                Map<String, Object> object = (Map<String, Object>) response.get("object");
                isPraise1=AppApplication.getSingleGson().toJson(object.get("isPraise"));
                Artwork artwork = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artWork")), Artwork.class);
                topTitle.setText(artwork.getTitle());
                cl01TvTitle.setText(artwork.getTitle());
                cl01TvBrief.setText(artwork.getBrief());
                cl01TvName.setText(artwork.getAuthor().getName());
                cl01LlZhicheng.setVisibility(View.GONE);
                if (artwork.getAuthor() != null) {
                    if (artwork.getAuthor().getMaster() != null) {
                        if (artwork.getAuthor().getMaster().getTitle() != null && !"".equals(artwork.getAuthor().getMaster().getTitle())) {
                            cl01LlZhicheng.setVisibility(View.VISIBLE);
                            cl01TvZhicheng.setText(artwork.getAuthor().getMaster().getTitle());
                        }
                    }
                }
                AppApplication.displayImage(artwork.getPicture_url(), cl01TvPrc);
                EventBus.getDefault().post(object);
            }
        });
    }
    @Subscribe
    public void onEventMainThread(Artwork artwork){
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}

