package com.yxh.ryt.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.CompleteUserInfoCallBack;
import com.yxh.ryt.callback.RelesaseVideoCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.NickNameValidation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/12.
 */
public class ReleaseVideoActivity extends  BaseActivity {

    private String file;//视频路径
    @Bind(R.id.rv_vv_video)
    VideoView videoView;//视频播放控件
    @Bind(R.id.rv_et_content)
    EditText content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);*/
        setContentView(R.layout.releasevideo);
        ButterKnife.bind(this);/*启用注解绑定*/
        content.requestFocus();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Bundle bundle = getIntent().getExtras();
        file = bundle.getString("text");//获得拍摄的短视频保存地址
        setValue();
    }
    private void setValue() {

        videoView.setVideoPath(file);
        videoView.start();
        videoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    public void onPrepared(MediaPlayer mp) {// TODO Auto-generated method stub
                        mp.start();
                        mp.setLooping(true);
                    }
                });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVideoPath(file);
                videoView.start();
            }
        });
        /*InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(content,InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);*/


    }
    @OnClick(R.id.rv_tv_push)
    public  void push(){
        Map<String,File> fileMap=new HashMap<>();
        File file1 = new File(file);
        fileMap.put(file1.getName(), file1);
        String s = content.getText().toString();
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artworkId","imy8yuae256uv1vp");
        paramsMap.put("timestamp",System.currentTimeMillis()+"");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        paramsMap.put("type","1");
        paramsMap.put("content",content.getText().toString());
        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");
        NetRequestUtil.postFile(Constants.BASE_PATH + "releaseArtworkDynamic.do", "video", fileMap, paramsMap, headers, new RelesaseVideoCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                Log.d("XXXXXXXXXXXXXXXXXXXXX", "失败了啊");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                System.out.println("成功了");
                Log.d("XXXXXXXXXXXXXXXXXXXXX", "YYYYYYYYYYY");
                Log.d("tagonResponse", response.toString());
            }
        });
    }
    @OnClick(R.id.rv_ib_cancel)
    public void cancel(){
        finish();
    }
}
