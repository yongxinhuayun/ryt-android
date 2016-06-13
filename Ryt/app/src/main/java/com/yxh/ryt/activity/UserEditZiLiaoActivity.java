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
import com.yxh.ryt.vo.User;

import java.io.File;
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
        AppApplication.displayImage(AppApplication.gUser.getPictureUrl(), circleImageView);
        //tv_nickname.setText(AppApplication.gUser.getName());
       // tv_sex.setText(changSex(AppApplication.gUser.getSex()));

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
                        + "/temp.jpg");
                Bitmap bitmap1 = getBitmap(Uri.fromFile(picture));
               /* filePath = Utils.getFilePathFromUri(Uri.fromFile(picture), this);
                Bitmap bitmap2 = Utils.rotaingImageView(filePath, bitmap1);
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
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                getUser(response);
            }
        });
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
        System.out.print(AppApplication.gUser.toString());
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
        paramsMap.put("currentId", AppApplication.gUser.getId());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver!=null){
            AppApplication.getSingleContext().unregisterReceiver(receiver);
        }
    }
}
