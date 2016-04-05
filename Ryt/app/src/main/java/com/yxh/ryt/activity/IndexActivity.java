package com.yxh.ryt.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.yxh.ryt.R;
import com.yxh.ryt.fragment.TabFragment01;
import com.yxh.ryt.fragment.TabFragment02;
import com.yxh.ryt.fragment.TabFragment03;
import com.yxh.ryt.fragment.TabFragment04;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IndexActivity extends BaseActivity {
    FragmentManager fm;
    FragmentTransaction transaction;
    TabFragment01 tabFragment01;
    TabFragment02 tabFragment02;
    TabFragment03 tabFragment03;
    TabFragment04 tabFragment04;
    @Bind({ R.id.tv_tab_01, R.id.tv_tab_02, R.id.tv_tab_03 ,R.id.tv_tab_04})
    List<TextView> tabTvs;
    static final ButterKnife.Setter<View, Integer> SETCOLOR = new ButterKnife.Setter<View, Integer>() {
        @Override public void set(View view, Integer value, int index) {
            TextView textView=(TextView)view;
            if(value==index){
                textView.setTextColor(Color.RED);//可以将选择和未选择的color抽出
                return;
            }
            textView.setTextColor(Color.WHITE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        ButterKnife.bind(this);
        // 设置默认的Fragment
        setDefaultFragment();

    }

    private void setDefaultFragment()
    {
        fm =getSupportFragmentManager();
        transaction = fm.beginTransaction();
        tabFragment01 = new TabFragment01();
        transaction.replace(R.id.fl_tab, tabFragment01);
        ButterKnife.apply(tabTvs, SETCOLOR, 0);
        transaction.commit();
    }

    @OnClick({R.id.rl_tab_01,R.id.rl_tab_02,R.id.rl_tab_03,R.id.rl_tab_04})
    public void onClick(View v)
    {
        fm = getSupportFragmentManager();
        // 开启Fragment事务
        transaction = fm.beginTransaction();

        switch (v.getId())
        {
            case R.id.rl_tab_01:
                if (tabFragment01 == null)
                {
                    tabFragment01 = new TabFragment01();
                }
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.fl_tab, tabFragment01);
                ButterKnife.apply(tabTvs, SETCOLOR, 0);
                break;
            case R.id.rl_tab_02:
                if (tabFragment02 == null)
                {
                    tabFragment02 = new TabFragment02();
                }
                transaction.replace(R.id.fl_tab, tabFragment02);
                ButterKnife.apply(tabTvs, SETCOLOR, 1);
                break;
            case R.id.rl_tab_03:
                if (tabFragment03 == null)
                {
                    tabFragment03 = new TabFragment03();
                }
                transaction.replace(R.id.fl_tab, tabFragment03);
                ButterKnife.apply(tabTvs, SETCOLOR, 2);
                break;
            case R.id.rl_tab_04:
                if (tabFragment04 == null)
                {
                    tabFragment04 = new TabFragment04();
                }
                transaction.replace(R.id.fl_tab, tabFragment04);
                ButterKnife.apply(tabTvs, SETCOLOR, 3);
                break;
        }
        // transaction.addToBackStack();
        // 事务提交
        transaction.commit();
    }

}
