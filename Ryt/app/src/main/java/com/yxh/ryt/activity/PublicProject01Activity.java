package com.yxh.ryt.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.custemview.ActionSheetDialog;
import com.yxh.ryt.custemview.CustomDialogView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.GetPathFromUri4kitkat;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by 吴洪杰 on 2016/4/11.
 */
public class PublicProject01Activity extends  BaseActivity {
    private AlertDialog dialog;
    private CustomDialogView customDialogView;
    private RedrawCustomDialogViewThread redrawCdvRunnable;

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, PublicProject01Activity.class));
    }
    public final int REQUEST_IMAGE=0;
    @Bind(R.id.btn_next)
    TextView btnNext;
    @Bind(R.id.ev_title)
    EditText evTitle;
    @Bind(R.id.ev_des)
    EditText evDes;
    @Bind(R.id.ev_duration)
    EditText evDuration;
    @Bind(R.id.ev_menoy)
    EditText evMenoy;
    @Bind(R.id.iv_image)
    ImageView ivImage;
    String filePath="";
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_project_01);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.btn_next)
    public void next(View v){
        oneStepRequst();
    }
    @OnClick(R.id.ib_top_lf)
    public void back(View v){
        finish();
    }
    @OnClick(R.id.iv_image)
    public void addImage(View v){
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.
                                        getExternalStorageDirectory(), "pushArtFirst.jpg")));
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

    //艺术家发布项目第一步接口一网络请求
    private void oneStepRequst() {
        Map<String,File> fileMap=new HashMap<>();
        File file = new File(filePath);
        fileMap.put(file.getName(),file);
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("title",evTitle.getText().toString());
        paramsMap.put("duration",evDuration.getText().toString());
        //paramsMap.put("userId",AppApplication.gUser.getId());
        paramsMap.put("investGoalMoney",evMenoy.getText().toString());
        paramsMap.put("timestamp",System.currentTimeMillis()+"");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        paramsMap.put("brief",evDes.getText().toString());
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        NetRequestUtil.postFile(Constants.BASE_PATH + "initNewArtWork.do", "picture_url", fileMap, paramsMap, headers, new CompleteUserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                ToastUtil.showLong(PublicProject01Activity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
               if ("0".equals(response.get("resultCode"))){
                   Intent intent=new Intent(PublicProject01Activity.this,PublicProject02Activity.class);
                   intent.putExtra("artworkId", (String)response.get("artworkId")+"");
                   startActivity(intent);
                   finish();
               }else if ("000000".equals(response.get("resultCode"))){
                   SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                       @Override
                       public void getCode(String code) {
                           if ("0".equals(code)){
                               oneStepRequst();
                           }
                       }
                   });
                   sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
               }
            }
        });
    }
     class RedrawCustomDialogViewThread implements Runnable {

        private boolean isRun = true;

        @Override
        public void run() {

            while (isRun && dialog != null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 通知重绘
                customDialogView.postInvalidate();
            }

        }

        public boolean isRun() {
            return isRun;
        }

        public void setRun(boolean isRun) {
            this.isRun = isRun;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int widthzong = metric.widthPixels; // 屏幕宽度（像素）
        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                int height1=Utils.dip2px(PublicProject01Activity.this,224);
                int left1=Utils.dip2px(PublicProject01Activity.this,0);
                int right1=Utils.dip2px(PublicProject01Activity.this,0);
                int top1=Utils.dip2px(PublicProject01Activity.this,10);
                int bottom1=Utils.dip2px(PublicProject01Activity.this,0);

                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(widthzong,height1);
                params1.setMargins(left1,top1,right1,bottom1);
                ivImage.setLayoutParams(params1);
                Bitmap bitmap = getBitmap(data.getData());
                int bmpWidth1  = bitmap.getWidth();
                int bmpHeight1  = bitmap.getHeight();
                float scaleWidth1  = (float) widthzong / bmpWidth1;     //按固定大小缩放  sWidth 写多大就多大
                Matrix matrix1 = new Matrix();
                matrix1.postScale(scaleWidth1, scaleWidth1);//产生缩放后的Bitmap对象
                Bitmap resizeBitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth1, bmpHeight1, matrix1, false);
                bitmap.recycle();
                ivImage.setImageBitmap(resizeBitmap1);
                break;
            case CAMERA_REQUEST_CODE:
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/pushArtFirst.jpg");
                Bitmap bitmap1 = getBitmap(Uri.fromFile(picture));
                if (bitmap1==null){
                    break;
                }
                int height=Utils.dip2px(PublicProject01Activity.this,224);
                int left=Utils.dip2px(PublicProject01Activity.this,0);
                int right=Utils.dip2px(PublicProject01Activity.this,0);
                int top=Utils.dip2px(PublicProject01Activity.this,10);
                int bottom=Utils.dip2px(PublicProject01Activity.this,0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthzong,height);
                params.setMargins(left,top,right,bottom);
                ivImage.setLayoutParams(params);
                int bmpWidth  = bitmap1.getWidth();
                int bmpHeight  = bitmap1.getHeight();
                float scaleWidth  = (float) widthzong / bmpWidth;     //按固定大小缩放  sWidth 写多大就多大
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleWidth);//产生缩放后的Bitmap对象
                Bitmap resizeBitmap = Bitmap.createBitmap(bitmap1, 0, 0, bmpWidth, bmpHeight, matrix, false);
                ivImage.setImageBitmap(resizeBitmap);
                break;
            default:
                break;
        }
    }
    public Bitmap getBitmap(Uri data){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 4;
       /* if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){*/
        String filePath1 = GetPathFromUri4kitkat.getPath(data);
        /*}else{
            filePath= ImageUtils.getRealPathByUriOld(data);
        }*/
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(filePath1, options);
        if (bm==null){
            return null;
        }
        Bitmap bitmap1=compressImage(bm);
        File file = new File(Environment.getExternalStorageDirectory()
                + "/pushArtFirst"+Utils.getImageFormat(filePath1));
        try {
            filePath=file.getPath();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap1;
    }
    private Bitmap comp(Bitmap response) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
        response.compress(format, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            response.compress(format, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }
    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap.CompressFormat format=Bitmap.CompressFormat.JPEG;
        image.compress(format, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        int length = baos.toByteArray().length;
        while ( baos.toByteArray().length / 1024>300) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(format, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
