package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.custemview.WheelSheetDialog;
import com.yxh.ryt.util.DisplayUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.util.phote.util.Bimp;
import com.yxh.ryt.util.phote.util.Bimp02;
import com.yxh.ryt.util.phote.util.Bimp03;
import com.yxh.ryt.util.phote.util.Bimp04;
import com.yxh.ryt.util.phote.util.ImageItem;
import com.yxh.ryt.util.phote.util.PublicWay;
import com.yxh.ryt.util.phote.util.PublicWay02;
import com.yxh.ryt.util.phote.util.PublicWay03;
import com.yxh.ryt.util.phote.util.PublicWay04;
import com.yxh.ryt.util.phote.util.Res;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.CityModel;
import com.yxh.ryt.vo.DistrictModel;
import com.yxh.ryt.vo.ProvinceModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;

/**
 * Created by 吴洪杰 on 2016/4/21.
 */
public class YsjRzActivity extends BaseActivity {
    @Bind(R.id.ib_top_lf)
    ImageButton ibTopLf;
    @Bind(R.id.tv_top_ct)
    TextView tvTopCt;
    @Bind(R.id.rl_top)
    RelativeLayout rlTop;
    @Bind(R.id.ev_name)
    EditText evName;
    @Bind(R.id.ev_phone)
    EditText evPhone;
    @Bind(R.id.ev_diqu)
    TextView evDiqu;
    @Bind(R.id.ev_address)
    EditText evAddress;
    @Bind(R.id.ev_type)
    EditText evType;
    @Bind(R.id.ev_title)
    EditText evTitle;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    public final int REQUEST_IMAGE_01=1;
    public final int REQUEST_IMAGE_02=2;
    public final int REQUEST_IMAGE_03=3;
    public final int REQUEST_IMAGE_04=4;
    private GridView noScrollgridview_01;
    private GridView noScrollgridview_02;
    private GridView noScrollgridview_03;
    private GridView noScrollgridview_04;
    private GridAdapter01 adapter_01;
    private GridAdapter02 adapter_02;
    private GridAdapter03 adapter_03;
    private GridAdapter04 adapter_04;
    Map<String,File> fileMap1=new HashMap<>();
    Map<String,File> fileMap2=new HashMap<>();
    Map<String,File> fileMap3=new HashMap<>();
    Map<String,File> fileMap4=new HashMap<>();
    Map<String,File> fileMap5=new HashMap<>();

    public static Bitmap bimap ;
    int sum01=2;
    int sum02=3;
    int sum03=3;
    int sum04=3;
    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, YsjRzActivity.class));
    }
    @OnClick(R.id.btn_submit)
    void submitClick(){
        renZhengRequst();
    }
    void renZhengRequst(){
        Map<String,Map<String,File>> fileMap=new HashMap<>();
        fileMap.put("one",fileMap3);
        fileMap.put("two",fileMap4);
        fileMap.put("three",fileMap5);
        fileMap.put("identityFront",fileMap1);
        fileMap.put("identityBack",fileMap2);
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("provinceName","");
        paramsMap.put("province","");
        paramsMap.put("artCategory","");
        paramsMap.put("titleCertificate","");
        paramsMap.put("userId","in9xyax5cagsn8g7");
        paramsMap.put("paramType","0");
        paramsMap.put("timestamp",System.currentTimeMillis()+"");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        System.out.println(paramsMap.toString());
        NetRequestUtil.postMulFile(Constants.BASE_PATH + "applyArtMaster.do", fileMap, paramsMap, headers, new CompleteUserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                System.out.println("成功了");
                Log.d("XXXXXXXXXXXXXXXXXXXXX", "YYYYYYYYYYY");
                Log.d("tagonResponse", response.toString());
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.icon_addpic_unfocused);
        PublicWay.activityList.add(this);
        PublicWay02.activityList.add(this);
        setContentView(R.layout.ysj_sq);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        Res.init(this);
        noScrollgridview_01 = (GridView) findViewById(R.id.noScrollgridview_01);
        noScrollgridview_01.setSelector(new ColorDrawable(Color.TRANSPARENT));
        noScrollgridview_02 = (GridView) findViewById(R.id.noScrollgridview_02);
        noScrollgridview_02.setSelector(new ColorDrawable(Color.TRANSPARENT));
        noScrollgridview_03 = (GridView) findViewById(R.id.noScrollgridview_03);
        noScrollgridview_03.setSelector(new ColorDrawable(Color.TRANSPARENT));
        noScrollgridview_04 = (GridView) findViewById(R.id.noScrollgridview_04);
        noScrollgridview_04.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter_01 = new GridAdapter01(this);
        adapter_01.update();
        adapter_02 = new GridAdapter02(this);
        adapter_02.update();
        adapter_03 = new GridAdapter03(this);
        adapter_03.update();
        adapter_04 = new GridAdapter04(this);
        adapter_04.update();
        noScrollgridview_01.setAdapter(adapter_01);
        noScrollgridview_01.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    callMulImageSelector(sum01, REQUEST_IMAGE_01);
                } else {
                    Intent intent = new Intent(YsjRzActivity.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
        noScrollgridview_02.setAdapter(adapter_02);
        noScrollgridview_02.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp02.tempSelectBitmap.size()) {
                    callMulImageSelector02(sum02, REQUEST_IMAGE_02);
                } else {
                    Intent intent = new Intent(YsjRzActivity.this,
                            Gallery02Activity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
        noScrollgridview_03.setAdapter(adapter_03);
        noScrollgridview_03.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp03.tempSelectBitmap.size()) {
                    callMulImageSelector03(sum03, REQUEST_IMAGE_03);
                } else {
                    Intent intent = new Intent(YsjRzActivity.this,
                            Gallery03Activity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
        noScrollgridview_04.setAdapter(adapter_04);
        noScrollgridview_04.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp04.tempSelectBitmap.size()) {
                    callMulImageSelector04(sum04, REQUEST_IMAGE_04);
                } else {
                    Intent intent = new Intent(YsjRzActivity.this,
                            Gallery04Activity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
    }

    private void callMulImageSelector(int sum,int id) {
        PublicWay.num=sum;
        Intent intent = new Intent(AppApplication.getSingleContext(), MultiImageSelectorActivity.class);
        // 是否显示调用相机拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        if(Bimp.tempSelectBitmap.size()==0){
            // 最大图片选择数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, sum);
        }else{
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, sum-Bimp.tempSelectBitmap.size());
        }
        // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        startActivityForResult(intent, id);
    }
    private void callMulImageSelector02(int sum,int id) {
        PublicWay02.num=sum;
        Intent intent = new Intent(AppApplication.getSingleContext(), MultiImageSelectorActivity.class);
        // 是否显示调用相机拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        if(Bimp02.tempSelectBitmap.size()==0){
            // 最大图片选择数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, sum);
        }else{
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, sum-Bimp02.tempSelectBitmap.size());
        }
        // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        startActivityForResult(intent, id);
    }
    private void callMulImageSelector03(int sum,int id) {
        PublicWay03.num=sum;
        Intent intent = new Intent(AppApplication.getSingleContext(), MultiImageSelectorActivity.class);
        // 是否显示调用相机拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        if(Bimp03.tempSelectBitmap.size()==0){
            // 最大图片选择数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, sum);
        }else{
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, sum-Bimp03.tempSelectBitmap.size());
        }
        // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        startActivityForResult(intent, id);
    }
    private void callMulImageSelector04(int sum,int id) {
        PublicWay04.num=sum;
        Intent intent = new Intent(AppApplication.getSingleContext(), MultiImageSelectorActivity.class);
        // 是否显示调用相机拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        if(Bimp04.tempSelectBitmap.size()==0){
            // 最大图片选择数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, sum);
        }else{
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, sum-Bimp04.tempSelectBitmap.size());
        }
        // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        startActivityForResult(intent, id);
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Utils.setListViewHeightBasedOnChildren01(noScrollgridview_01);
                    adapter_01.notifyDataSetChanged();
                    break;
                case 2:
                    Utils.setListViewHeightBasedOnChildren01(noScrollgridview_02);
                    adapter_02.notifyDataSetChanged();
                    break;
                case 3:
                    Utils.setListViewHeightBasedOnChildren01(noScrollgridview_03);
                    adapter_03.notifyDataSetChanged();
                    break;
                case 4:
                    Utils.setListViewHeightBasedOnChildren01(noScrollgridview_04);
                    adapter_04.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_01){
            if(resultCode == RESULT_OK&&Bimp.tempSelectBitmap.size()<sum01){
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....
                for (String s:path){
                    File file = new File(s);
                    if (Bimp.tempSelectBitmap.size()==0){
                        fileMap1.put(file.getName(),file);
                    }else{
                        fileMap2.put(file.getName(),file);
                    }
                    String fileName = String.valueOf(System.currentTimeMillis());
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(null);
                    takePhoto.setImagePath(s);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
            }
        }
        if(requestCode == REQUEST_IMAGE_02){
            if(resultCode == RESULT_OK&&Bimp02.tempSelectBitmap.size()<sum02){
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....
                for (String s:path){
                    File file = new File(s);
                    fileMap3.put(file.getName(),file);
                    String fileName = String.valueOf(System.currentTimeMillis());
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(null);
                    takePhoto.setImagePath(s);
                    Bimp02.tempSelectBitmap.add(takePhoto);
                }
            }
        }
        if(requestCode == REQUEST_IMAGE_03){
            if(resultCode == RESULT_OK&& Bimp03.tempSelectBitmap.size()<sum02){
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....
                for (String s:path){
                    File file = new File(s);
                    fileMap4.put(file.getName(),file);
                    String fileName = String.valueOf(System.currentTimeMillis());
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(null);
                    takePhoto.setImagePath(s);
                    Bimp03.tempSelectBitmap.add(takePhoto);
                }
            }
        }
        if(requestCode == REQUEST_IMAGE_04){
            if(resultCode == RESULT_OK&& Bimp04.tempSelectBitmap.size()<sum04){
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....
                for (String s:path){
                    File file = new File(s);
                    fileMap5.put(file.getName(),file);
                    String fileName = String.valueOf(System.currentTimeMillis());
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(null);
                    takePhoto.setImagePath(s);
                    Bimp04.tempSelectBitmap.add(takePhoto);
                }
            }
        }
    }
    public class GridAdapter01 extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter01(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if(Bimp.tempSelectBitmap.size() == sum01){
                return sum01;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida_01,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position ==Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                holder.image.getLayoutParams().width=DisplayUtil.dip2px(90);
                holder.image.getLayoutParams().height=DisplayUtil.dip2px(90);
                if (position == sum01) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
                holder.image.getLayoutParams().width= ViewGroup.LayoutParams.WRAP_CONTENT;
                holder.image.getLayoutParams().height=ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }



        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }
    public class GridAdapter03 extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter03(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if(Bimp03.tempSelectBitmap.size() == sum01){
                return sum01;
            }
            return (Bimp03.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida_01,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position ==Bimp03.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                holder.image.getLayoutParams().width=DisplayUtil.dip2px(90);
                holder.image.getLayoutParams().height=DisplayUtil.dip2px(90);
                if (position == sum03) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp03.tempSelectBitmap.get(position).getBitmap());
                holder.image.getLayoutParams().width= ViewGroup.LayoutParams.WRAP_CONTENT;
                holder.image.getLayoutParams().height=ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }



        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp03.max == Bimp03.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 3;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp03.max += 1;
                            Message message = new Message();
                            message.what = 3;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }
    public class GridAdapter02 extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter02(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if(Bimp02.tempSelectBitmap.size() == sum02){
                return sum02;
            }
            return (Bimp02.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida_01,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position ==Bimp02.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                holder.image.getLayoutParams().width=DisplayUtil.dip2px(90);
                holder.image.getLayoutParams().height=DisplayUtil.dip2px(90);
                if (position == sum02) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp02.tempSelectBitmap.get(position).getBitmap());
                holder.image.getLayoutParams().width= ViewGroup.LayoutParams.WRAP_CONTENT;
                holder.image.getLayoutParams().height=ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }


        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp02.max == Bimp02.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp02.max += 1;
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }
    public class GridAdapter04 extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter04(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if(Bimp04.tempSelectBitmap.size() == sum04){
                return sum04;
            }
            return (Bimp04.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida_01,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position ==Bimp04.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                holder.image.getLayoutParams().width=DisplayUtil.dip2px(90);
                holder.image.getLayoutParams().height=DisplayUtil.dip2px(90);
                if (position == sum04) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp04.tempSelectBitmap.get(position).getBitmap());
                holder.image.getLayoutParams().width= ViewGroup.LayoutParams.WRAP_CONTENT;
                holder.image.getLayoutParams().height=ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }


        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp04.max == Bimp04.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 4;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp04.max += 1;
                            Message message = new Message();
                            message.what = 4;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }
    protected void onRestart() {
        adapter_01.update();
        adapter_02.update();
        adapter_03.update();
        adapter_04.update();
        super.onRestart();
    }
    @OnClick(R.id.ev_diqu)
    void diquClick(){
        WheelSheetDialog wheelSheetDialog = new WheelSheetDialog(this);
        wheelSheetDialog
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .show();
        wheelSheetDialog.setOkClickLinster(new WheelSheetDialog.OkClickLinster() {
            @Override
            public void click(ProvinceModel p, CityModel c, DistrictModel d) {
                evDiqu.setText(p.getName() + "-" + c.getName() + "-" + d.getName());
            }
        });
    }
}
