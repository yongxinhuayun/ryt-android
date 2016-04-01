package com.yxh.ryt.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.GetPathFromUri4kitkat;
import com.yxh.ryt.util.ImageUtils;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Sha1;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/3/31.
 */
public class RegisterScActivity extends BaseActivity{
    @Bind(R.id.rs_tv_sexInput)
    TextView sexInput;
    @Bind(R.id.rs_et_nickName)
    TextView nickName;
    @Bind(R.id.rs_iv_headPortrait)
    CircleImageView  circleImageView;
    @Bind(R.id.rs_tv_complete)
    TextView tvComplete;
    PopupWindow window;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int PHOTO_RESOULT = 4;
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    String filePath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registersucced);
        ButterKnife.bind(this);/*启用注解绑定*/
    }
    @OnClick(R.id.rs_tv_complete)
    public void completeClick(){
        Map<String,File> fileMap=new HashMap<>();
        File file = new File(filePath);
        System.out.println(file.getName() + "==========================");
        fileMap.put(file.getName(),file);
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("username","13466636718");
        paramsMap.put("nickname","吴洪杰");
        paramsMap.put("sex","1");
        paramsMap.put("timestamp",System.currentTimeMillis()+"");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        NetRequestUtil.postFile(Constants.BASE_PATH+"completeUserInfo.do","headPortrait",fileMap,paramsMap,headers,new CompleteUserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                System.out.println("成功了");
            }
        });
    }
    @OnClick(R.id.rs_rl_sex)
    public void sexSelect(){
        showPopwindowSex();
    }


    @OnClick(R.id.rs_iv_headPortrait)
    public void headPortrait(){
        showPopwindowHead();
    }

    private void showPopwindowHead() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.popwindowhead, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        common(view);
        TextView takePhoto= (TextView) view.findViewById(R.id.pdh_tv_takePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.
                        getExternalStorageDirectory(), "temp.jpg")));
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });
        TextView gallery= (TextView) view.findViewById(R.id.pdh_tv_gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                startActivityForResult(intent, ALBUM_REQUEST_CODE);
            }
        });
    }

    private void showPopwindowSex() {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.popwindowsex, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        common(view);
        TextView man= (TextView) view.findViewById(R.id.pds_tv_man);
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexInput.setText("男");
                window.dismiss();
            }
        });
        TextView woMan = (TextView) view.findViewById(R.id.pds_tv_woman);
        woMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexInput.setText("女");
                window.dismiss();
            }
        });
    }
    private void common(View view){
        window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        window.setBackgroundDrawable(dw);


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

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                Bitmap bitmap = getBitmap(data.getData());
//                Bitmap bitmap=getBitmapFromUri(data.getData());
                circleImageView.setImageBitmap(bitmap);
//                saveFile(bitmap);

                break;
            case CAMERA_REQUEST_CODE:
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/temp.jpg");
                Bitmap bitmap1 = getBitmap(Uri.fromFile(picture));
                circleImageView.setImageBitmap(bitmap1);
//                saveFile(bitmap1);
//                startCrop(Uri.fromFile(picture));
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

    public Bitmap getBitmap(Uri data){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 4;
//        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            filePath=GetPathFromUri4kitkat.getPath(data);
//        }else{
//            filePath=ImageUtils.getRealPathByUriOld(data);
//        }
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(filePath, options);
        return  bm;
    }
    private Bitmap getBitmapFromUri(Uri uri)
    {
        try
        {
            filePath=GetPathFromUri4kitkat.getPath(uri);
            System.out.println(filePath);
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
//    public void saveFile(Bitmap bm){
//        try {
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
////            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            bos.flush();
//            bos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    /**
     * 开始裁剪
     *
     * @param uri
     */
//    private void startCrop(Uri uri) {
//        Intent intent = new Intent("com.android.camera.action.CROP");//调用Android系统自带的一个图片剪裁页面,
//        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
//        intent.putExtra("crop", "true");//进行修剪
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX",1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 300);
//        intent.putExtra("outputY", 300);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, CROP_REQUEST_CODE);
//    }

}
