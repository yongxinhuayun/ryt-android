package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.GetPathFromUri4kitkat;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by 吴洪杰 on 2016/4/11.
 */
public class EditProject01Activity extends  BaseActivity {
    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, EditProject01Activity.class));
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
    //艺术家发布项目第一步接口一网络请求
    private void oneStepRequst() {
        Map<String,File> fileMap=new HashMap<>();
        File file = new File(filePath);
        fileMap.put(file.getName(),file);
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("title",evTitle.getText().toString());
        paramsMap.put("duration",evDuration.getText().toString());
        paramsMap.put("userId","ieatht97wfw30hfd");
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
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                System.out.println("成功了");
                Log.d("XXXXXXXXXXXXXXXXXXXXX", "YYYYYYYYYYY");
                Log.d("tagonResponse", response.toString());
                Intent intent=new Intent(EditProject01Activity.this,PublicProject02Activity.class);
                intent.putExtra("artworkId", (String)response.get("artworkId")+"");
                startActivity(intent);
            }
        });
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
                int height1=Utils.dip2px(EditProject01Activity.this,224);
                int left1=Utils.dip2px(EditProject01Activity.this,14);
                int right1=Utils.dip2px(EditProject01Activity.this,14);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(widthzong,height1);
                params1.setMargins(left1,0,right1,0);
                ivImage.setLayoutParams(params1);
                Bitmap bitmap = getBitmap(data.getData());
                /*Bitmap btm2=Bitmap.createScaledBitmap(bitmap, widthzong, height1, false); //自定义
                bitmap.recycle();*/
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
                int height=Utils.dip2px(EditProject01Activity.this,224);
                int left=Utils.dip2px(EditProject01Activity.this,14);
                int right=Utils.dip2px(EditProject01Activity.this,14);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthzong,height);
                params.setMargins(left,0,right,0);
                ivImage.setLayoutParams(params);
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/temp.jpg");
                Bitmap bitmap1 = getBitmap(Uri.fromFile(picture));
                filePath= Utils.getFilePathFromUri(Uri.fromFile(picture), this);
                Bitmap bitmap2 = Utils.rotaingImageView(filePath, bitmap1);
                bitmap1.recycle();
                int bmpWidth  = bitmap2.getWidth();
                int bmpHeight  = bitmap2.getHeight();
                float scaleWidth  = (float) widthzong / bmpWidth;     //按固定大小缩放  sWidth 写多大就多大
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleWidth);//产生缩放后的Bitmap对象
                Bitmap resizeBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bmpWidth, bmpHeight, matrix, false);
                bitmap2.recycle();
                /*Bitmap btm3=Bitmap.createScaledBitmap(bitmap2, widthzong, height, false); //自定义
                bitmap2.recycle();*/
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
        filePath= GetPathFromUri4kitkat.getPath(data);
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(filePath, options);
        return  bm;
    }
}
