package com.yxh.ryt.activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import com.yxh.ryt.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @OnClick(R.id.rv_ib_cancel)
    public void cancel(){
        finish();
    }
}
