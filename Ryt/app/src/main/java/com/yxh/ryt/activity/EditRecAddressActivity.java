package com.yxh.ryt.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.callback.RegisterCallBack;
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
import java.util.Map;

import okhttp3.Call;

public class EditRecAddressActivity extends BaseActivity implements View.OnClickListener{

    private EditText reciever;
    private EditText phone;
    private TextView local;
    private EditText detail;
    private ImageButton selected;
    private TextView save;
    private int unDefult;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rec_address);
        reciever = (EditText) findViewById(R.id.et_reciever);
        phone = (EditText) findViewById(R.id.et_phone);
        local = (TextView) findViewById(R.id.et_local);
        local.setOnClickListener(this);
        detail = (EditText) findViewById(R.id.et_detail);
        selected = (ImageButton) findViewById(R.id.ib_selected);
        save = (TextView) findViewById(R.id.tv_save);
        back = (ImageView) findViewById(R.id.ib_top_lf);
        selected.setOnClickListener(this);
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        loadData();
        reciever.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        phone.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        local.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
        detail.setFilters(new InputFilter[]{EditTextFilterUtil.getEmojiFilter()});
    }

    private void loadData() {
        reciever.setText(getIntent().getStringExtra("consignee"));
        phone.setText(getIntent().getStringExtra("phone"));
        local.setText(getIntent().getStringExtra("provinceStr"));
        detail.setText(getIntent().getStringExtra("details"));
        unDefult = Integer.parseInt(getIntent().getStringExtra("status"));
        if (unDefult == 2) {
            selected.setBackground(getResources().getDrawable(R.mipmap.yixuanze));

        } else {
            selected.setBackground(getResources().getDrawable(R.mipmap.weixuanze));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_top_lf:
                finish();
                break;
            case R.id.ib_selected:
                if (unDefult == 1) {
                    selected.setBackground(getResources().getDrawable(R.mipmap.yixuanze));
                    unDefult = 2;
                } else {
                    selected.setBackground(getResources().getDrawable(R.mipmap.weixuanze));
                    unDefult = 1;
                }
                break;
            case R.id.tv_save:
                if ("".equals(reciever.getText().toString())){
                    ToastUtil.showLong(this,"收货人不能为空");
                    return;
                }
                AppApplication.getSingleEditTextValidator()
                        .add(new ValidationModel(phone, new UserNameValidation()))
                        .execute();
                if ("".equals(detail.getText().toString())){
                    ToastUtil.showLong(this,"详细地址不能为空");
                    return;
                }
                if ("".equals(local.getText().toString())){
                    ToastUtil.showLong(this,"所在地区不能为空");
                    return;
                }
                //表单没有检验通过直接退出方法
                if(!AppApplication.getSingleEditTextValidator().validate()){
                    return;
                }
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
                        local.setText(p.getName() + "-" + c.getName() + "-" + d.getName());
                    }
                });
            default:
                break;

        }
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
