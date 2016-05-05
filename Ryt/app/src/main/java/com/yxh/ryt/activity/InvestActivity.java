package com.yxh.ryt.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
public class InvestActivity extends BaseActivity{
    @Bind({ R.id.imp_ll_2, R.id.imp_ll_5, R.id.imp_ll_10 ,R.id.imp_ll_28,R.id.imp_ll_88,R.id.imp_ll_all})
    List<LinearLayout> tabTvs;
    ButterKnife.Setter<View, Integer> SETCOLOR = new ButterKnife.Setter<View, Integer>() {
        @Override public void set(View view, Integer value, int index) {
            LinearLayout ll=(LinearLayout)view;
            if(value==index){
                ll.setBackgroundResource(R.mipmap.heise);
                return;
            }
        }
    };
    @Bind({ R.id.imp_tv_2, R.id.imp_tv_5, R.id.imp_tv_10 ,R.id.imp_tv_28,R.id.imp_tv_88,R.id.imp_tv_all})
    List<TextView> tabTvs1;
    static final ButterKnife.Setter<View, Integer> SETCOLOR1 = new ButterKnife.Setter<View, Integer>() {
        @Override public void set(View view, Integer value, int index) {
            TextView textView=(TextView)view;
            if(value==index){
                textView.setTextColor(Color.WHITE);//可以将选择和未选择的color抽出
                return;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.investmentpage);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.imp_ll_2,R.id.imp_ll_5,R.id.imp_ll_10,R.id.imp_ll_28,R.id.imp_ll_88,R.id.imp_ll_all})
    public void click(View view){
        switch (view.getId()){
            case R.id.imp_ll_2:
                ButterKnife.apply(tabTvs, SETCOLOR,0);
                ButterKnife.apply(tabTvs1, SETCOLOR1,0);
                break;
            case R.id.imp_ll_5:
                ButterKnife.apply(tabTvs, SETCOLOR,1);
                ButterKnife.apply(tabTvs1, SETCOLOR1,1);
                break;
            case R.id.imp_ll_10:
                ButterKnife.apply(tabTvs, SETCOLOR,2);
                ButterKnife.apply(tabTvs1, SETCOLOR1,2);
                break;
            case R.id.imp_ll_28:
                ButterKnife.apply(tabTvs, SETCOLOR,3);
                ButterKnife.apply(tabTvs1, SETCOLOR1,3);
                break;
            case R.id.imp_ll_88:
                ButterKnife.apply(tabTvs, SETCOLOR,4);
                ButterKnife.apply(tabTvs1, SETCOLOR1,4);
                break;
            case R.id.imp_ll_all:
                ButterKnife.apply(tabTvs, SETCOLOR,0);
                ButterKnife.apply(tabTvs, SETCOLOR,1);
                ButterKnife.apply(tabTvs, SETCOLOR,2);
                ButterKnife.apply(tabTvs, SETCOLOR,3);
                ButterKnife.apply(tabTvs, SETCOLOR,4);
                ButterKnife.apply(tabTvs, SETCOLOR,5);
                ButterKnife.apply(tabTvs1, SETCOLOR1,0);
                ButterKnife.apply(tabTvs1, SETCOLOR1,1);
                ButterKnife.apply(tabTvs1, SETCOLOR1,2);
                ButterKnife.apply(tabTvs1, SETCOLOR1,3);
                ButterKnife.apply(tabTvs1, SETCOLOR1,4);
                ButterKnife.apply(tabTvs1, SETCOLOR1,5);
                break;
        }

    }
}
