package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Sha1;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.User;
import com.yxh.ryt.vo.UserBrief;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by 吴洪杰 on 2016/4/21.
 */
public class PublicBriefActivity extends BaseActivity {
    @Bind(R.id.ib_top_lf)
    ImageButton ibTopLf;
    @Bind(R.id.tv_top_ct)
    TextView tvTopCt;
    @Bind(R.id.tv_top_rt)
    TextView tvTopRt;
    @Bind(R.id.tv_content)
    EditText tvContent;
    UserBrief userBrief;
    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, PublicBriefActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_brief);
        ButterKnife.bind(this);
        if (getIntent()!=null)userBrief=(UserBrief)getIntent().getSerializableExtra("userbrief");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(userBrief!=null){
            tvContent.setText(userBrief.getContent());
        }
    }
    @OnClick(R.id.tv_top_rt)
    void tvTopRtClick(){
        publicBriefRequst();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    private void publicBriefRequst() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("type","2");
        paramsMap.put("userId","in9xyax5cagsn8g7");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        paramsMap.put("content", tvContent.getText().toString());
        NetRequestUtil.post(Constants.BASE_PATH + "saveUserBrief.do", paramsMap, new LoginCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if (Integer.valueOf((String) response.get("resultCode")) > 0) {
                    ToastUtil.showShort(PublicBriefActivity.this, ((String) response.get("resultMsg")));
                    return;
                }
                finish();
            }
        });
    }
}
