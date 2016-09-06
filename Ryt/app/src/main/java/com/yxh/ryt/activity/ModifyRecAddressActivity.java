package com.yxh.ryt.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.RegisterCallBack;
import com.yxh.ryt.custemview.CustomDialog;
import com.yxh.ryt.custemview.WheelSheetDialog;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.PasswordValidation;
import com.yxh.ryt.validations.ReceiverAddressDetailValidation;
import com.yxh.ryt.validations.ReceiverNameValidation;
import com.yxh.ryt.validations.UserNameValidation;
import com.yxh.ryt.vo.CityModel;
import com.yxh.ryt.vo.DistrictModel;
import com.yxh.ryt.vo.ProvinceModel;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class ModifyRecAddressActivity extends BaseActivity implements View.OnClickListener{

    private EditText reciever;
    private EditText phone;
    private TextView local;
    private EditText detail;
    private TextView save;
    private int unDefult;
    private ImageView back;
    private String addressId;
    private boolean distract=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rec_address1);
        reciever = (EditText) findViewById(R.id.et_reciever);
        phone = (EditText) findViewById(R.id.et_phone);
        local = (TextView) findViewById(R.id.et_local);
        local.setOnClickListener(this);
        detail = (EditText) findViewById(R.id.et_detail);
        save = (TextView) findViewById(R.id.tv_save);
        back = (ImageView) findViewById(R.id.ib_top_lf);
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        loadData();
        reciever.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        phone.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        local.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        detail.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        addressId = getIntent().getStringExtra("addressId");
    }

    private void loadData() {
        if (getIntent().getStringExtra("consignee")!=null){
            reciever.setText(getIntent().getStringExtra("consignee")+"");
        }else {
            reciever.setText("");
        }
        if (getIntent().getStringExtra("phone")!=null){
            phone.setText(getIntent().getStringExtra("phone")+"");
        }else {
            phone.setText("");
        }
        if (getIntent().getStringExtra("provinceStr")!=null){
            local.setText(getIntent().getStringExtra("provinceStr")+"");
        }else {
            local.setText("");
        }
        if (getIntent().getStringExtra("details")!=null){
            detail.setText(getIntent().getStringExtra("details")+"");
        }else {
            detail.setText("");
        }
        if (getIntent().getStringExtra("status")==null || "".equals(getIntent().getStringExtra("status"))){
            unDefult=2;
        }else {
            unDefult = Integer.parseInt(getIntent().getStringExtra("status"));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_lf:
                finish();
                break;
            case R.id.tv_save:
                saveRecAddress();
                break;
            case R.id.et_local:
                WheelSheetDialog wheelSheetDialog = new WheelSheetDialog(this);
                wheelSheetDialog
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .show();
                wheelSheetDialog.setOkClickLinster(new WheelSheetDialog.OkClickLinster() {
                    @Override
                    public void click(ProvinceModel p, CityModel c, DistrictModel d) {
                        distract=true;
                        local.setText(p.getName() + "-" + c.getName() + "-" + d.getName());
                    }
                });
                break;
            default:
                break;

        }
    }

    private void saveRecAddress() {
        AppApplication.getSingleEditTextValidator()
                .add(new ValidationModel(phone,new UserNameValidation()))
                .add(new ValidationModel(reciever,new ReceiverNameValidation()))
                .add(new ValidationModel(detail,new ReceiverAddressDetailValidation()))
                .execute();
        //表单没有检验通过直接退出方法
        if(!AppApplication.getSingleEditTextValidator().validate()){
            return;
        }
        if (!distract){
            ToastUtil.showShort(ModifyRecAddressActivity.this,"所在地区不能为空");
            return;
        }
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
                    Intent intent = new Intent("android.intent.action.ADDRESS_FINISH");
                    ModifyRecAddressActivity.this.sendBroadcast(intent);
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
