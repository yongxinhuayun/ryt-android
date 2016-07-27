package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.custemview.ActionSheetDialog;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.GetPathFromUri4kitkat;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SPUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 吴洪杰 on 2016/4/21.
 */
public class UserEditZiLiaoActivity extends BaseActivity implements View.OnClickListener {


    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int PHOTO_RESOULT = 4;
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CROP_REQUEST_CODE = 4;
    private boolean flag = false;
    String filePath = "";
    private String username;
    private CircleImageView circleImageView;
    private ImageView imageView;
    private RelativeLayout nickName;
    private RelativeLayout sign_message;
    private RelativeLayout rl_sex;
    private TextView iv_sign;
    private TextView tv_nickname;
    private TextView tv_sex;
    private EditReceiver receiver;
    private Double sex;
    private RelativeLayout address;

    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserEditZiLiaoActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_edit_ziliao);
        circleImageView = (CircleImageView) findViewById(R.id.uez_iv_icon);
        imageView = (ImageView) findViewById(R.id.ib_top_lf);
        nickName = (RelativeLayout) findViewById(R.id.rl_nickName);
        sign_message = (RelativeLayout) findViewById(R.id.rl_sign);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        iv_sign = (TextView) findViewById(R.id.iv_sign);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        address = (RelativeLayout) findViewById(R.id.rl_address);
        //给控件设置内容
        //AppApplication.displayImage(AppApplication.gUser.getPictureUrl(), circleImageView);


        inflatSign();
        //设置点击
        circleImageView.setOnClickListener(this);
        imageView.setOnClickListener(this);
        nickName.setOnClickListener(this);
        sign_message.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        address.setOnClickListener(this);

    }

  /*  receiver = new AttentionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.EDIT_NICK_BROADCAST");
        registerReceiver(receiver, filter);*/



    @Override
    protected void onStart() {
        super.onStart();
        //注册这两个广播
         receiver = new EditReceiver();
        IntentFilter myFilter = new IntentFilter();
        myFilter.addAction("android.intent.action.EDIT_NICK_BROADCAST");
        myFilter.addAction("android.intent.action.EDIT_SIGN_BROADCAST");
        AppApplication.getSingleContext().registerReceiver(receiver, myFilter);
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
                flag = true;
                commitHead();
                break;
            case CAMERA_REQUEST_CODE:
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/editContent.jpg");
                Bitmap bitmap1 = getBitmap(Uri.fromFile(picture));
                /* Bitmap bitmap2 = Utils.rotaingImageView(filePath, bitmap1);
                bitmap1.recycle();
                circleImageView.setImageBitmap(bitmap2);*/
                circleImageView.setImageBitmap(bitmap1);
                commitHead();

                break;
//
            default:
                break;
        }
    }

    //上传头像
    public void commitHead() {
        Map<String, File> fileMap = new HashMap<>();
        File file = new File(filePath);
        fileMap.put(file.getName(), file);
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", AppApplication.gUser.getId());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        NetRequestUtil.postFile(Constants.BASE_PATH + "editPicUrl.do", "headPortrait", fileMap, paramsMap, headers, new CompleteUserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                ToastUtil.showLong(UserEditZiLiaoActivity.this,"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    getUser(response);
                    ToastUtil.showLong(UserEditZiLiaoActivity.this,"修改图片成功!");
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                commitHead();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
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
                + "/editContent"+Utils.getImageFormat(filePath1));
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
        while ( baos.toByteArray().length / 1024>100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(format, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
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

    private void getUser(Map<String, Object> response) {
        User user = new User();
        user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("userInfo")), User.class);
        if (user!=null){
            SPUtil.put(AppApplication.getSingleContext(), "current_id", user.getId() + "");
            SPUtil.put(AppApplication.getSingleContext(), "current_username", user.getUsername() + "");
            SPUtil.put(AppApplication.getSingleContext(), "current_name", user.getName() + "");
            SPUtil.put(AppApplication.getSingleContext(), "current_sex", user.getSex() + "");
        }
        if (user.getUserBrief()!=null){
            SPUtil.put(AppApplication.getSingleContext(), "current_userBrief", user.getUserBrief().getSigner() + "");
        }
        if (user.getMaster() != null) {
            SPUtil.put(AppApplication.getSingleContext(), "current_master", "master");
        } else {
            SPUtil.put(AppApplication.getSingleContext(), "current_master", "");
        }
        SPUtil.put(AppApplication.getSingleContext(), "current_pictureUrl", user.getPictureUrl() + "");
        AppApplication.gUser = user;
        if (user.getMaster() != null) {
            AppApplication.gUser.setMaster1("master");
        } else {
            AppApplication.gUser.setMaster1("");
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uez_iv_icon:
                new ActionSheetDialog(UserEditZiLiaoActivity.this)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.
                                                getExternalStorageDirectory(), "editContent.jpg")));
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
                break;
            case R.id.ib_top_lf:
                finish();
                break;
            case R.id.rl_nickName:
                startActivity(new Intent(this, EditNicknameActivity.class));
                break;
            case R.id.rl_sign:
                startActivity(new Intent(this, EditSignActivity.class));
                break;
            case R.id.rl_sex:
                new ActionSheetDialog(UserEditZiLiaoActivity.this)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("男", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        tv_sex.setText("男");
                                        postSex("1");
                                    }
                                })
                        .addSheetItem("女", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        tv_sex.setText("女");
                                        postSex("2");
                                    }
                                })
                        .addSheetItem("保密", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        tv_sex.setText("保密");
                                        postSex("0");
                                    }
                                })
                        .show();
                break;
            case R.id.rl_address:
                startActivity(new Intent(this,ReceiverAdressActivity.class));
                break;
            default:
                break;
        }
    }
    public void postSex(String sex) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", AppApplication.gUser.getId());
        paramsMap.put("type", "14");
        paramsMap.put("content", sex);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + " editProfile.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(Map<String, Object> response) {
                //getUser(response);
            }
        });
    }

    public class EditReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
                if(action.equals("android.intent.action.EDIT_NICK_BROADCAST")){
                    String name = intent.getExtras().getString("nick");
                    tv_nickname.setText(name);
                }else if (action.equals("android.intent.action.EDIT_SIGN_BROADCAST")){
                    String signer = intent.getExtras().getString("signer");
                    iv_sign.setText(signer);
                }
            }

    }

    //填充签名,性别
    public void inflatSign(){
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userId", AppApplication.gUser.getId());
        //paramsMap.put("currentId", AppApplication.gUser.getId());
        paramsMap.put("pageIndex", "1");
        paramsMap.put("pageSize", "20");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "my.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                Map<String, Map<String, Map<String,String>>> map1 = (Map<String, Map<String, Map<String,String>>>) response.get("pageInfo");
                Map<String, Map<String,String>> map2 =null;
                if (map1!=null){
                    map2 =  map1.get("user");
                }
                Map<String,String> map3 =null;
                if (map2!=null){
                    map3=map2.get("userBrief");
                }
                String sign =null;
                if (map3==null){
                    sign = "";
                }else {
                    sign = map3.get("signer");
                }
                Map<String,Map<String,Double>> map11 = (Map<String, Map<String, Double>>) response.get("pageInfo");
                Map<String,Double> map22 = null;
                if (map11 != null) {
                    map22 = map11.get("user");
                }

                if (map22 !=null){
                    sex = map22.get("sex");
                }
                Map<String,Map<String,String>> map111 = (Map<String, Map<String, String>>) response.get("pageInfo");
                Map<String,String> map222 = null;
                if (map111 != null) {
                    map222 = map111.get("user");
                }
                String name = "";
                if (map222 != null){
                    name = map222.get("name");
                }
                String pictureUrl = "";
                if (map222 != null){
                    pictureUrl = map222.get("pictureUrl");
                }
                //AppApplication.getSingleGson().toJson(response.get("followsNum"));
                /*Map<String,Map<String,Double>> map11 = (Map<String, Map<String, Double>>) response.get("pageInfo");
                Map<String,Double> map22 = map11.get("user");
                Double sex = map22.get("sex");
                Map<String,Map<String,String>> map111 = (Map<String, Map<String, String>>) response.get("pageInfo");
                Map<String,String> map222 = map111.get("user");
                String name = map222.get("name");*/
                /*User user = new User();
                user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("pageInfo")), User.class);*/
                if (sign == null) {
                    iv_sign.setText("");
                }else{
                iv_sign.setText(sign);
                }
                if (sex != null) {
                    tv_sex.setText(changSex(sex));
                }else{
                    tv_sex.setText(changSex(3.0));
                }
                tv_nickname.setText(name);
                AppApplication.displayImage(AppApplication.gUser.getPictureUrl(), circleImageView);
            }
        });
    }

    public String changSex(Double s) {
        if(s == 2.0) {
            return "女";
        }else if(s == 1.0){
            return "男";
        }else{
            return "保密";
        }
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        inflatSign();
    }
*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver!=null){
            AppApplication.getSingleContext().unregisterReceiver(receiver);
        }
    }
}
