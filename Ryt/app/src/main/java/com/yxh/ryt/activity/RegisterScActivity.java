package com.yxh.ryt.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.custemview.ActionSheetDialog;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.GetPathFromUri4kitkat;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SPUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.NickNameValidation;
import com.yxh.ryt.vo.User;

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
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/3/31.
 */
public class RegisterScActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.rs_et_nickName)
    EditText nickName;
    @Bind(R.id.rs_rg_sex)
    RadioGroup sexGroup;
    @Bind(R.id.rs_iv_headPortrait)
    CircleImageView circleImageView;
    @Bind(R.id.rs_rb_nan)
    RadioButton nan;
    @Bind(R.id.rs_rb_nv)
    RadioButton nv;
    @Bind(R.id.rs_bt_commit)
    Button commit;
    PopupWindow window;
    private int sex = -1;
    private boolean flag = false;
    private boolean isNickyname;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int PHOTO_RESOULT = 4;
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    String filePath = "";
    private String username;
    private Bitmap bitmap2;
    private GoogleApiClient client;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registersucced);
        ButterKnife.bind(this);/*启用注解绑定*/
        String userid = getIntent().getStringExtra("userId");
        username = getIntent().getStringExtra("username");
        //username = "18510251819";
        password = getIntent().getStringExtra("password");
        //password = "123456";
        sexGroup.setOnCheckedChangeListener(this);
        commit.setEnabled(false);
        onEnabled();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       /* client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();*/
    }

    private void onEnabled() {
        nickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    isNickyname = true;
                    dianji(flag, isNickyname, sex);
                } else {
                    isNickyname = false;
                    dianji(flag, isNickyname, sex);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void dianji(boolean flag, boolean isNickyname, int sex) {
        if (flag && isNickyname && sex > 0) {
            commit.setEnabled(true);
            commit.setBackgroundResource(R.mipmap.anniu_kedianji);
        } else {
            commit.setEnabled(false);
            commit.setBackgroundResource(R.mipmap.anniu_bukedianji);
        }
    }

    @OnClick(R.id.rs_bt_commit)
    public void completeClick() {
        Map<String, File> fileMap = new HashMap<>();
        File file = new File(filePath);
        fileMap.put(file.getName(), file);
        AppApplication.getSingleEditTextValidator()
                .add(new ValidationModel(nickName, new NickNameValidation()))
                .execute();
        //表单没有检验通过直接退出方法
        if (!AppApplication.getSingleEditTextValidator().validate()) {
            return;
        }
        if (!flag) {
            ToastUtil.show(this, "请选择头像", Toast.LENGTH_LONG);
            return;
        }
        if (sex == -1) {
            ToastUtil.show(this, "请选择性别", Toast.LENGTH_LONG);
            return;
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("username", username + "");
        paramsMap.put("nickname", nickName.getText().toString());
        paramsMap.put("sex", sex + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        NetRequestUtil.postFile(Constants.BASE_PATH + "completeUserInfo.do", "headPortrait", fileMap, paramsMap, headers, new CompleteUserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                User user = new User();
                user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("userInfo")), User.class);
                getUser(user, 1);
                Map<String,String> paramsMap=new HashMap<>();
                paramsMap.put("username",username+"");
                paramsMap.put("password", password+"");
                paramsMap.put("timestamp", System.currentTimeMillis() + "");
                try {
                    AppApplication.signmsg=EncryptUtil.encrypt(paramsMap);
                    paramsMap.put("signmsg", AppApplication.signmsg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NetRequestUtil.post(Constants.BASE_PATH + "j_spring_security_check", paramsMap, new LoginCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {
                        System.out.println("失败了");
                    }

                    @Override
                    public void onResponse(Map<String, Object> response) {
                        if (Integer.valueOf((String) response.get("resultCode")) > 0) {
                            ToastUtil.showShort(RegisterScActivity.this, "登录失败");
                            return;
                        }
                        User user = new User();
                        user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("userInfo")), User.class);
                        getUser(user,1);
                        Map<String, String> paramsMap = new HashMap<>();
                        paramsMap.put("username", username+"");
                        paramsMap.put("password", password+"");
                        paramsMap.put("cid", JPushInterface.getRegistrationID(RegisterScActivity.this));
                        paramsMap.put("timestamp", System.currentTimeMillis() + "");
                        try {
                            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
                            paramsMap.put("signmsg", AppApplication.signmsg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        NetRequestUtil.post(Constants.BASE_PATH + "userBinding.do", paramsMap, new LoginCallBack() {
                            @Override
                            public void onError(Call call, Exception e) {
                                System.out.println("失败了");
                            }

                            @Override
                            public void onResponse(Map<String, Object> response) {

                                    Intent intent=new Intent(RegisterScActivity.this,IndexActivity.class);
                                    startActivity(intent);
                                    finish();
                            }
                        });
                    }
                });
            }
        });
    }
    private void getUser(User user, int i) {
        SPUtil.put(AppApplication.getSingleContext(), "current_id", user.getId() + "");
        SPUtil.put(AppApplication.getSingleContext(), "current_username", user.getUsername()+"");
        SPUtil.put(AppApplication.getSingleContext(), "current_name", user.getName()+"");
        SPUtil.put(AppApplication.getSingleContext(), "current_sex", user.getSex() + "");
        SPUtil.put(AppApplication.getSingleContext(), "current_loginState", i+"");
        SPUtil.put(AppApplication.getSingleContext(), "current_password",password);
        if (user.getMaster()!=null){
            SPUtil.put(AppApplication.getSingleContext(), "current_master","master");
        }else {
            SPUtil.put(AppApplication.getSingleContext(), "current_master","");
        }
        SPUtil.put(AppApplication.getSingleContext(), "current_pictureUrl", user.getPictureUrl()+"");
        AppApplication.gUser = user;
        if (user.getMaster()!=null){
            AppApplication.gUser.setMaster1("master");
        }else {
            AppApplication.gUser.setMaster1("");
        }
        AppApplication.gUser.setLoginState(i+"");
        AppApplication.gUser.setPassword(password);
    }

    @OnClick(R.id.rs_ib_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.rs_iv_headPortrait)
    public void headPortrait() {
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
                                        getExternalStorageDirectory(), "upLoad.jpg")));
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

   /* private void showPopwindowHead() {
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
    }*/

    /*private void common(View view){
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
                if (window != null && window.isShowing()) {
                    window.dismiss();
                    window = null;
                }
                return false;
            }
        });

    }*/

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case ALBUM_REQUEST_CODE:
                if (data == null) {
                    return;
                }
                Bitmap bitmap = getBitmap(data.getData());
               //Bitmap bitmap=getBitmapFromUri(data.getData());
                circleImageView.setImageBitmap(bitmap);
//                saveFile(bitmap);
                flag = true;
                dianji(flag, isNickyname, sex);
                break;
            case CAMERA_REQUEST_CODE:
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/upLoad.jpg");
                Bitmap bitmap2 = getBitmap(Uri.fromFile(picture));
                //filePath = Utils.getFilePathFromUri(Uri.fromFile(picture), this);
                /* Bitmap bitmap2 = Utils.rotaingImageView(filePath, bitmap1);
                bitmap1.recycle();
                circleImageView.setImageBitmap(bitmap2);*/
                circleImageView.setImageBitmap(bitmap2);
//                saveFile(bitmap1);
//                startCrop(Uri.fromFile(picture));
                flag = true;
                dianji(flag, isNickyname, sex);
                break;
            case CROP_REQUEST_CODE:
//                if (data == null) {
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
        while ( baos.toByteArray().length / 1024>100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(format, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    public Bitmap getBitmap(Uri data) {
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
        Bitmap bitmap1=comp(bm);
        File file = new File(Environment.getExternalStorageDirectory()
                + "/upLoad"+Utils.getImageFormat(filePath1));
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == nan.getId()) {
            sex = 1;
            dianji(flag, isNickyname, sex);
        } else {
            sex = 2;
            dianji(flag, isNickyname, sex);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
