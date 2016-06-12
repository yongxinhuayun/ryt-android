package com.yxh.ryt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.Bill;
import com.yxh.ryt.vo.FollowUserUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by 吴洪杰 on 2016/4/21.
 */
public class UserQianBaoActivity extends BaseActivity {
    @Bind(R.id.ib_top_lf)
    ImageView ibTopLf;
    @Bind(R.id.btn_tx)
    Button btnTx;
    @Bind(R.id.uqb_listView)
    ListView listView;
    private List<Bill> bills;
    private CommonAdapter<Bill> billCommonAdapter;
    public static void openActivity(Activity activity) {
        activity.startActivity(new Intent(activity, UserQianBaoActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_qianbao);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userId",AppApplication.gUser.getId());
        paramsMap.put("pageSize",Constants.pageSize+"");
        paramsMap.put("pageIndex", "1");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "transactionRecord.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {

            }
        });
    }

    @OnClick({ R.id.btn_tx})
    void btnClick(View v) {
        switch (v.getId()) {
            /*case R.id.btn_cz:
                UserChongZhiActivity.openActivity(this);
                break;*/
            case R.id.btn_tx:
                break;
        }
    }

    @OnClick(R.id.ib_top_lf)
    public void back1() {
        finish();
    }

    @OnClick(R.id.ib_top_rt)
    public void back2() {
        finish();
    }
}