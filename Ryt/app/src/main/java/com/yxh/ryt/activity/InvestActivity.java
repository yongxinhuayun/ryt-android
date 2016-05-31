package com.yxh.ryt.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxh.ryt.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/3.
 */
public class InvestActivity extends BaseActivity implements TextWatcher {
    @Bind({ R.id.imp_ll_2, R.id.imp_ll_5, R.id.imp_ll_10 ,R.id.imp_ll_28,R.id.imp_ll_88,R.id.imp_ll_all})
    List<LinearLayout> tabTvs;
    ButterKnife.Setter<View, Integer> SETCOLOR = new ButterKnife.Setter<View, Integer>() {
        @Override public void set(View view, Integer value, int index) {
            LinearLayout ll=(LinearLayout)view;
            if(value==index){
                ll.setBackgroundResource(R.mipmap.heise);
            }else {
                ll.setBackgroundResource(R.drawable.qianbaoweixuanzhong);
            }
        }
    };
    @Bind({ R.id.imp_tv_2, R.id.imp_tv_5, R.id.imp_tv_10 ,R.id.imp_tv_28,R.id.imp_tv_88,R.id.imp_tv_all})
    List<TextView> tabTvs1;
    static final ButterKnife.Setter<View, Integer> SETCOLOR1 = new ButterKnife.Setter<View, Integer>() {
        @Override public void set(View view, Integer value, int index) {
            TextView textView=(TextView)view;
            if(value==index ){
                textView.setTextColor(Color.WHITE);//可以将选择和未选择的color抽出
            }else {
                textView.setTextColor(Color.BLACK);
            }
        }
    };
    @Bind(R.id.imp_tv_invest)
    TextView invest;
    @Bind(R.id.imp_et_other)
    EditText other;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.investmentpage);
        ButterKnife.bind(this);
        other.addTextChangedListener(this);
    }
    @OnClick({R.id.imp_ll_2,R.id.imp_ll_5,R.id.imp_ll_10,R.id.imp_ll_28,R.id.imp_ll_88,R.id.imp_ll_all,R.id.imp_tv_invest})
    public void click(View view){
        switch (view.getId()){
            case R.id.imp_ll_2:
                ButterKnife.apply(tabTvs, SETCOLOR,0);
                ButterKnife.apply(tabTvs1, SETCOLOR1,0);
                invest.setText("投资"+2+"元");
                break;
            case R.id.imp_ll_5:
                ButterKnife.apply(tabTvs, SETCOLOR,1);
                ButterKnife.apply(tabTvs1, SETCOLOR1,1);
                invest.setText("投资" + 5 + "元");
                break;
            case R.id.imp_tv_invest:

                break;
            case R.id.imp_ll_10:
                ButterKnife.apply(tabTvs, SETCOLOR,2);
                ButterKnife.apply(tabTvs1, SETCOLOR1,2);
                invest.setText("投资" + 10 + "元");
                break;
            case R.id.imp_ll_28:
                ButterKnife.apply(tabTvs, SETCOLOR,3);
                ButterKnife.apply(tabTvs1, SETCOLOR1,3);
                invest.setText("投资" + 28 + "元");
                break;
            case R.id.imp_ll_88:
                ButterKnife.apply(tabTvs, SETCOLOR,4);
                ButterKnife.apply(tabTvs1, SETCOLOR1,4);
                invest.setText("投资" + 88 + "元");
                break;
            case R.id.imp_ll_all:
                ButterKnife.apply(tabTvs, SETCOLOR, 5);
                ButterKnife.apply(tabTvs1, SETCOLOR1, 5);
                invest.setText("投资" + 1111 + "元");
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        for (int i=0;i<6;i++){
            tabTvs.get(i).setBackgroundResource(R.drawable.qianbaoweixuanzhong);
            tabTvs1.get(i).setTextColor(Color.BLACK);
        }
        if ("2".equals(s.toString())){
            ButterKnife.apply(tabTvs, SETCOLOR,0);
            ButterKnife.apply(tabTvs1, SETCOLOR1,0);
        }
        if ("5".equals(s.toString())){
            ButterKnife.apply(tabTvs, SETCOLOR,1);
            ButterKnife.apply(tabTvs1, SETCOLOR1,1);
        }
        if ("10".equals(s.toString())){
            ButterKnife.apply(tabTvs, SETCOLOR,2);
            ButterKnife.apply(tabTvs1, SETCOLOR1,2);
        }
        if ("28".equals(s.toString())){
            ButterKnife.apply(tabTvs, SETCOLOR,3);
            ButterKnife.apply(tabTvs1, SETCOLOR1,3);
        }
        if ("88".equals(s.toString())){
            ButterKnife.apply(tabTvs, SETCOLOR,4);
            ButterKnife.apply(tabTvs1, SETCOLOR1,4);
        }
        if ("1111".equals(s.toString())){
            ButterKnife.apply(tabTvs, SETCOLOR,5);
            ButterKnife.apply(tabTvs1, SETCOLOR1,5);
        }
        invest.setText("投资" + s + "元");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    @OnClick(R.id.ip_ib_back)
    public void back(){
        finish();
    }
}
