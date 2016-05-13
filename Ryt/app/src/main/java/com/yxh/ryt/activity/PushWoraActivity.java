package com.yxh.ryt.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.IcsLinearLayout;
import com.yxh.ryt.Constants;
import com.yxh.ryt.DatePicker.DatePickerView;
import com.yxh.ryt.R;
import com.yxh.ryt.custemview.ActionSheetDialog;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/11.
 */
public class PushWoraActivity extends BaseActivity {
    @Bind(R.id.pw_iv_work)
    ImageView imageWork;
    @Bind(R.id.pw_et_workName)
    EditText workName;
    @Bind(R.id.pw_et_material)
    EditText material;
    @Bind(R.id.pw_tv_state)
    TextView state;
    @Bind(R.id.pw_tv_year)
    TextView year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pushwork);
        ButterKnife.bind(this);/*启用注解绑定*/
    }
    @OnClick({R.id.pw_sale,R.id.pw_rl_year} )
    public void isSale(View view){
        switch (view.getId()){
            case R.id.pw_sale:
                new ActionSheetDialog(this)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("可售", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        state.setText("可售");
                                    }
                                })
                        .addSheetItem("已售", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        state.setText("已售");
                                    }
                                })
                        .addSheetItem("非卖品", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        state.setText("非卖品");
                                    }
                                })
                        .show();
                break;
            case R.id.pw_rl_year:
                DatePickerView pickerView=new DatePickerView(PushWoraActivity.this, new DatePickerView.DatePickerListener() {
                    @Override
                    public void dateChange(String string) {
                        year.setText(string);
                    }

                    @Override
                    public void finish(String string) {
                        year.setText(string);

                    }
                });
                pickerView.setFromYearAndToYear(1900,2016);
                pickerView.initDate(2016);
                pickerView.show();
                break;
        }
    }

}
