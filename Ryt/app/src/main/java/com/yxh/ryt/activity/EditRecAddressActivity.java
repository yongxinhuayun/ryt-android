package com.yxh.ryt.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.custemview.ActionSheetDialog;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.custemview.CustomDialog;
import com.yxh.ryt.custemview.WheelSheetDialog;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.UserNameValidation;
import com.yxh.ryt.vo.CityModel;
import com.yxh.ryt.vo.DistrictModel;
import com.yxh.ryt.vo.ProvinceModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.Call;

public class EditRecAddressActivity extends BaseActivity implements View.OnClickListener{

    private TextView reciever;
    private TextView phone;
    private TextView local;
    private TextView detail;
    private TextView save;
    private int unDefult;
    private ImageView back;
    private String addressId;
    private TextView isDefault;
    private RelativeLayout delete;
    private AddressFinishReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rec_address);
        reciever = (TextView) findViewById(R.id.et_reciever);
        phone = (TextView) findViewById(R.id.et_phone);
        local = (TextView) findViewById(R.id.et_local);
        local.setOnClickListener(this);
        detail = (TextView) findViewById(R.id.et_detail);
        save = (TextView) findViewById(R.id.tv_modify);
        isDefault = ((TextView) findViewById(R.id.aera_tv_default));
        delete = (RelativeLayout) findViewById(R.id.aera_rl_delete);
        back = (ImageView) findViewById(R.id.ib_top_lf);
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        delete.setOnClickListener(this);
        isDefault.setOnClickListener(this);
        loadData();
        reciever.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        phone.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        local.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        detail.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        addressId = getIntent().getStringExtra("addressId");
        receiver = new AddressFinishReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.ADDRESS_FINISH");
        registerReceiver(receiver, filter);
    }
    public class AddressFinishReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reciever!=null){
            unregisterReceiver(receiver);
        }
    }
    private void loadData() {
        reciever.setText(getIntent().getStringExtra("consignee"));
        phone.setText(getIntent().getStringExtra("phone"));
        local.setText(getIntent().getStringExtra("provinceStr"));
        detail.setText(getIntent().getStringExtra("details"));
        unDefult = Integer.parseInt(getIntent().getStringExtra("status"));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_lf:
                finish();
                break;
            case R.id.tv_modify:
                Intent edIntent = new Intent(EditRecAddressActivity.this, ModifyRecAddressActivity.class);
                edIntent.putExtra("addressId", addressId);
                edIntent.putExtra("status", getIntent().getStringExtra("status"));
                edIntent.putExtra("consignee", getIntent().getStringExtra("consignee"));
                edIntent.putExtra("details", getIntent().getStringExtra("details"));
                edIntent.putExtra("phone", getIntent().getStringExtra("phone"));
                edIntent.putExtra("provinceStr", getIntent().getStringExtra("provinceStr"));
                edIntent.putExtra("districtStr", getIntent().getStringExtra("districtStr"));
                edIntent.putExtra("cityStr", getIntent().getStringExtra("cityStr"));
                startActivity(edIntent);
                break;
            case R.id.aera_tv_default:
                isDefault(addressId);
                break;
            case R.id.aera_rl_delete:
                CustomDialog.Builder builder = new CustomDialog.Builder(EditRecAddressActivity.this);
                builder.setMessage("确认要删除该收货地址吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        delAddress(addressId);
                    }
                });


                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
                break;
            default:
                break;

        }
    }
    private void delAddress(final String addressId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("addressId", addressId);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "removeAddress.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showShort(getApplicationContext(), "删除失败");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    ToastUtil.showShort(getApplicationContext(), "删除成功");
                    finish();
                }else if ("000000".equals(response.get("resultCode"))) {
                    SessionLogin sessionLogin = new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)) {
                                delAddress(addressId);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
    private void isDefault(String id) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("consumerAddressId", id);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "setDefaultAddress.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showShort(getApplicationContext(), "保存失败");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    ToastUtil.showShort(getApplicationContext(), "设置成功");
                    finish();
                }
            }
        });
    }
    private void saveRecAddress() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("addressId", getIntent().getStringExtra("addressId"));
        paramsMap.put("status", unDefult + "");
        paramsMap.put("consignee", reciever.getText().toString());
        paramsMap.put("details", detail.getText().toString());
        paramsMap.put("phone", phone.getText().toString());
        paramsMap.put("provinceStr", local.getText().toString());
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "saveAddress.do", paramsMap, new RegisterCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showShort(getApplicationContext(), "保存失败");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    ToastUtil.showShort(getApplicationContext(), "保存成功");
                    finish();
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                saveRecAddress();
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
}
