package com.yxh.ryt.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.ArtistTabPageIndicatorAdapter;
import com.yxh.ryt.custemview.ActionSheetDialog;
import com.yxh.ryt.fragment.ArtistHomeFragment;
import com.yxh.ryt.fragment.BriefFragment;
import com.yxh.ryt.fragment.InvestedFragment;
import com.yxh.ryt.fragment.UserPraiseFragment;
import com.yxh.ryt.fragment.WorksFragment;
import com.yxh.ryt.vo.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/8.
 */
public class ArtistIndexActivity extends BaseActivity {
    List<Fragment> indexChildFragments=new ArrayList<>();
    FragmentPagerAdapter indexChildAdapter;
    private String userId;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private PushWorkReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artistindex);

        userId = getIntent().getStringExtra("userId");
        String name=getIntent().getStringExtra("name");
        TextView textName = (TextView) findViewById(R.id.aai_tv_name);
        ((ImageView) findViewById(R.id.aai_top_lf)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textName.setText(name);
        indexChildFragments.add(new ArtistHomeFragment(userId));
        indexChildFragments.add(new BriefFragment(userId));
        indexChildFragments.add(new WorksFragment(userId));
        indexChildFragments.add(new InvestedFragment(userId));
        indexChildFragments.add(new UserPraiseFragment(userId));
        indexChildAdapter = new ArtistTabPageIndicatorAdapter(getSupportFragmentManager(),indexChildFragments);
        ViewPager pager = (ViewPager)findViewById(R.id.aai_pager);
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(indexChildAdapter);
        //实例化TabPageIndicator然后设置ViewPager与之关联
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.aai_indicator);
        indicator.setViewPager(pager);
        receiver = new PushWorkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.FW_BROADCAST");
        registerReceiver(receiver, filter);
    }
    public class PushWorkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            new ActionSheetDialog(ArtistIndexActivity.this)
                    .builder()
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    /*intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.
                                            getExternalStorageDirectory(), "pushWork.jpg")));*/
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                //Bitmap bitmap = getBitmap(data.getData());
//                Bitmap bitmap=getBitmapFromUri(data.getData());
                //circleImageView.setImageBitmap(bitmap);
//                saveFile(bitmap);
                Intent intent=new Intent(this,PushWoraActivity.class);
                intent.putExtra("intent",data);
                startActivity(intent);
                break;
            case CAMERA_REQUEST_CODE:
                if (data==null){
                    return;
                }
                Intent intent1=new Intent(this,PushWoraActivity.class);
                intent1.putExtra("intent",data);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

}
