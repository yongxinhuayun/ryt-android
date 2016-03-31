package com.yxh.ryt.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Sha1;
import com.zhy.http.okhttp.callback.Callback;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/30.
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.btn_center_login)
    Button btnLogin;
    @Bind(R.id.et_center_one)
    EditText etUsername;
    @Bind(R.id.et_center_two)
    EditText etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);/*启用注解绑定*/
    }
    /*登录点击事件*/
    @OnClick(R.id.btn_center_login)
    public void loginClick(){

        Map<String,Object> paramsMap=new HashMap<>();
        paramsMap.put("username","18510251819");
        paramsMap.put("password", Sha1.encodePassword("123456","SHA"));
        paramsMap.put("timestamp",System.currentTimeMillis()+"");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "login.do",  paramsMap, new Callback<Map<String,Object>>() {
            //
            @Override
            public Map<String,Object> parseNetworkResponse(Response response) throws Exception {
                String string = response.body().string();
                Map<String, Object> resultMap = AppApplication.getSingleGson().fromJson(string, new TypeToken<Map<String, Object>>() {
                }.getType());
                return resultMap;
            }

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, Object> response) {

            }
        });
    }
}
