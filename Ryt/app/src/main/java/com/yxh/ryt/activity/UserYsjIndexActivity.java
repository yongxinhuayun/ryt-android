package com.yxh.ryt.activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.RongZiXqTabPageIndicatorAdapter;
import com.yxh.ryt.adapter.UserPtTabPageIndicatorAdapter;
import com.yxh.ryt.adapter.UserYsjTabPageIndicatorAdapter;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.ActionSheetDialog;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.fragment.RongZiItemFragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab01Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab02Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab03Fragment;
import com.yxh.ryt.fragment.RongZiXiangQingTab04Fragment;
import com.yxh.ryt.fragment.UserJianJieFragment;
import com.yxh.ryt.fragment.UserTouGuoFragment;
import com.yxh.ryt.fragment.UserZanGuoFragment;
import com.yxh.ryt.fragment.YSJHomeFragment;
import com.yxh.ryt.fragment.YSJWorkFragment;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.Artwork;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import wuhj.com.mylibrary.StickHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

public class UserYsjIndexActivity extends BaseActivity {
   /* private StickHeaderViewPagerManager manager1;
    private StickHeaderViewPagerManager manager2;
    private StickHeaderViewPagerManager manager3;
    private StickHeaderViewPagerManager manager4;*/

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserYsjIndexActivity.class));
    }
    ArrayList<Fragment> mFragmentList;
    ViewPager mViewPager;
    StickHeaderViewPagerManager manager;
    private String artworkId;
    RelativeLayout rlUserIndex;
    @Bind(R.id.tv_user_header_gz_num)
    TextView tvUserHeaderGzNum;
    @Bind(R.id.tv_user_header_gz)
    TextView tvUserHeaderGz;
    @Bind(R.id.rs_iv_headPortrait)
    CircleImageView rsIvHeadPortrait;
    @Bind(R.id.tv_user_header_name)
    TextView tvUserHeaderName;
    @Bind(R.id.ll_user_header)
    LinearLayout llUserHeader;
    @Bind(R.id.tv_user_header_fs_num)
    TextView tvUserHeaderFsNum;
    @Bind(R.id.tv_user_header_fs)
    TextView tvUserHeaderFs;
    @Bind(R.id.tv_user_header_txt)
    TextView tvUserHeaderTxt;
    @Bind(R.id.tv_user_header_je_value_01)
    TextView tvUserHeaderJeValue01;
    @Bind(R.id.tv_user_header_je_txt_01)
    TextView tvUserHeaderJeTxt01;
    @Bind(R.id.tv_user_header_je_value_02)
    TextView tvUserHeaderJeValue02;
    @Bind(R.id.tv_user_header_je_txt_02)
    TextView tvUserHeaderJeTxt02;
    @Bind(R.id.tv_user_header_je_value_03)
    TextView tvUserHeaderJeValue03;
    @Bind(R.id.tv_user_header_je_txt_03)
    TextView tvUserHeaderJeTxt03;
    private PushWorkReceiver receiver;
//    @Bind({R.id.ll_header_gz, R.id.ll_header_fs, R.id.ll_header_qm, R.id.ll_header_value})
//    List<LinearLayout> linearLayouts;
//    static final ButterKnife.Setter<View, Integer> ISVISIBLE = new ButterKnife.Setter<View, Integer>() {
//        @Override
//        public void set(View view, Integer value, int index) {
//            if (value == 0) {//显示
//                view.setVisibility(View.VISIBLE);
//                return;
//            }
//            if (value == 1) {//隐藏
//                view.setVisibility(View.GONE);
//                return;
//            }
//        }
//    };

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int PHOTO_RESOULT = 4;
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_ysj_index);
        ButterKnife.bind(this);
        mViewPager = (ViewPager) findViewById(R.id.user_ysj_pager);
        StickHeaderLayout root = (StickHeaderLayout) findViewById(R.id.user_ysj_root);
        manager = new StickHeaderViewPagerManager(root, mViewPager);
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(YSJHomeFragment.newInstance(manager, 0, false));
        /*manager1 = new StickHeaderViewPagerManager(root, mViewPager);*/
        mFragmentList.add(UserJianJieFragment.newInstance(manager, 1, false));
        /*manager2 = new StickHeaderViewPagerManager(root, mViewPager);*/
        mFragmentList.add(YSJWorkFragment.newInstance(manager, 2, false));
        /*manager3 = new StickHeaderViewPagerManager(root, mViewPager);*/
        mFragmentList.add(UserTouGuoFragment.newInstance(manager, 3, false));
       /* manager4 = new StickHeaderViewPagerManager(root, mViewPager);*/
        mFragmentList.add(UserZanGuoFragment.newInstance(manager, 4, false));
        UserYsjTabPageIndicatorAdapter pagerAdapter = new UserYsjTabPageIndicatorAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(pagerAdapter);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.user_ysj_indicator);
        indicator.setViewPager(mViewPager);
        receiver = new PushWorkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.FW_BROADCAST");
        registerReceiver(receiver, filter);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (AppApplication.gUser == null) {
            setLoginViewValues();
            return;
        }
        setLoginedViewValues();
    }

    //登录成功设置控件元素的值
    private void setLoginedViewValues() {
            tvUserHeaderName.setText(AppApplication.gUser.getUsername());
            tvUserHeaderFsNum.setText(AppApplication.gUser.getCount1()+"");
            tvUserHeaderGzNum.setText(AppApplication.gUser.getCount()+"");
            tvUserHeaderTxt.setText("null".equals(AppApplication.gUser.getUserBrief())?"一句话20字以内":AppApplication.gUser.getUserBrief());
            tvUserHeaderJeValue01.setText("￥"+AppApplication.gUser.getInvestsMoney());
            tvUserHeaderJeValue02.setText("￥"+AppApplication.gUser.getRoiMoney());
            tvUserHeaderJeValue03.setText(0==AppApplication.gUser.getRate()?"0%":AppApplication.gUser.getRate()*100+"%");
            tvUserHeaderJeTxt01.setText("项目总金额");
            tvUserHeaderJeTxt02.setText("项目拍卖总金额");
            tvUserHeaderJeTxt03.setText("拍卖溢价率");

    }
    //未登录成功设置控件元素的值
    private void setLoginViewValues() {
        tvUserHeaderFsNum.setText("0");
        tvUserHeaderGzNum.setText("0");
        tvUserHeaderName.setText("游客");
        tvUserHeaderTxt.setText("一句话20字以内");
        tvUserHeaderJeValue01.setText("￥0");
        tvUserHeaderJeValue02.setText("￥0");
        tvUserHeaderJeValue03.setText("0%");
        tvUserHeaderJeTxt01.setText("项目总金额");
        tvUserHeaderJeTxt02.setText("项目拍卖总金额");
        tvUserHeaderJeTxt03.setText("拍卖溢价率");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        unregisterReceiver(receiver);
    }
    public class PushWorkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            new ActionSheetDialog(UserYsjIndexActivity.this)
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.
                                            getExternalStorageDirectory(), "temp.jpg")));
                                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                                }
                            })
                    .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                                    startActivityForResult(intent, ALBUM_REQUEST_CODE);
                                }
                            })
                    .show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                /*Bitmap bitmap = getBitmap(data.getData());
//                Bitmap bitmap=getBitmapFromUri(data.getData());
                circleImageView.setImageBitmap(bitmap);*/
//                saveFile(bitmap);
                Intent intent=new Intent(this,PushWoraActivity.class);
                intent.putExtra("intent",data.getData());
                startActivity(intent);
                break;
            case CAMERA_REQUEST_CODE:
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/temp.jpg");
                Intent intent1=new Intent(this,PushWoraActivity.class);
                intent1.putExtra("intent",Uri.fromFile(picture));
                startActivity(intent1);
                break;
            case CROP_REQUEST_CODE:
//                if (data == null) {
//                    // TODO 如果之前以后有设置过显示之前设置的图片 否则显示默认的图片
//                    return;
//                }
//                Bundle extras = data.getExtras();
//                if (extras != null) {
//                    Bitmap photo = extras.getParcelable("data");
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 4;  //这里表示原来图片的1/4
//                    photo.
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                    InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//                    BitmapFactory.decodeStream(isBm, null, options);
//                    final int i = ImageUtils.calculateInSampleSize(new ImageUtils.ImageSize(options.outWidth, options.outHeight), new ImageUtils.ImageSize(100, 100));
//                }
//                break;
            default:
                break;
        }
    }
}

