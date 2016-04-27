package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.custemview.ActionSheetDialog;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.GetPathFromUri4kitkat;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.PasswordValidation;
import com.yxh.ryt.validations.UserNameValidation;
import com.yxh.ryt.validations.ZuoPinNameValidation;
import com.yxh.ryt.validations.ZuoPinYearValidation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by 吴洪杰 on 2016/4/21.
 */
public class PublicZuoPinActivity extends BaseActivity {
    @Bind(R.id.tv_top_lf)
    TextView tvTopLf;
    @Bind(R.id.tv_top_rt)
    TextView tvTopRt;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.ll_image)
    LinearLayout llImage;
    @Bind(R.id.ev_name)
    EditText evName;
    @Bind(R.id.et_year)
    EditText etYear;
    @Bind(R.id.et_cz)
    EditText etCz;
    @Bind(R.id.rt_ss)
    RelativeLayout rtSs;
    @Bind(R.id.tv_cs)
    TextView tvCs;
    String csState="";
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int PHOTO_RESOULT = 4;
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    String filePath = "";


    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, PublicZuoPinActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_work);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_add)
    void addImage() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                Bitmap bitmap = getBitmap(data.getData());
                ivAdd.setImageBitmap(bitmap);
                break;
            case CAMERA_REQUEST_CODE:
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/temp.jpg");
                Bitmap bitmap1 = getBitmap(Uri.fromFile(picture));
                ivAdd.setImageBitmap(bitmap1);
                break;
            case CROP_REQUEST_CODE:
            default:
                break;
        }
    }

    public Bitmap getBitmap(Uri data) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 4;
        filePath = GetPathFromUri4kitkat.getPath(data);
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(filePath, options);
        return bm;
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            filePath = GetPathFromUri4kitkat.getPath(uri);
            System.out.println(filePath);
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @OnClick(R.id.rt_ss)
    void isChuShou() {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("可售", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                csState="1";
                                tvCs.setText("可售");
                                tvCs.setTextColor(Color.BLACK);
                            }
                        })
                .addSheetItem("已售", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                csState="2";
                                tvCs.setText("已售");
                                tvCs.setTextColor(Color.BLACK);
                            }
                        })
                .addSheetItem("非卖品", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                csState="0";
                                tvCs.setText("非卖品");
                                tvCs.setTextColor(Color.BLACK);
                            }
                        })
                .show();
    }

    @OnClick(R.id.tv_top_rt)
    public void completeClick() {
        AppApplication.getSingleEditTextValidator()
                .add(new ValidationModel(evName,new ZuoPinNameValidation()))
                .add(new ValidationModel(etYear,new ZuoPinYearValidation()))
                .execute();
        //表单没有检验通过直接退出方法
        if(!AppApplication.getSingleEditTextValidator().validate()){
            return;
        }
        Map<String, File> fileMap = new HashMap<>();
        File file = new File(filePath);
        fileMap.put(file.getName(), file);
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("name", evName.getText().toString());
        paramsMap.put("material",etCz.getText().toString() );
        paramsMap.put("currentUserId", "in9xyax5cagsn8g7");
        paramsMap.put("type", csState);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        NetRequestUtil.postFile(Constants.BASE_PATH + "saveMaterWork.do", "pictureUrl", fileMap, paramsMap, headers, new CompleteUserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                System.out.println("成功了");
                finish();
            }
        });
    }

}
