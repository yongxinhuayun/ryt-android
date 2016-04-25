package com.yxh.ryt.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.AttentionIndicatorAdapter;
import com.yxh.ryt.adapter.IndexTabPageIndicatorAdapter;
import com.yxh.ryt.fragment.AttentionItemFragment;
import com.yxh.ryt.fragment.BaseFragment;
import com.yxh.ryt.fragment.ChuangZuoItemFragment;
import com.yxh.ryt.fragment.PaiMaiItemFragment;
import com.yxh.ryt.fragment.RongZiItemFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/4/25.
 */
public class AttentionActivity extends BaseActivity {
    List<BaseFragment> indexChildFragments=new ArrayList<>();
    FragmentPagerAdapter indexChildAdapter;
    @Bind(R.id.attention_pager)
    ViewPager pager;
    @Bind(R.id.attention_indicator)
    TabPageIndicator indicator;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention);
        ButterKnife.bind(this);/*启用注解绑定*/
        indexChildFragments.add(new AttentionItemFragment());
        indexChildFragments.add(new AttentionItemFragment());
        indexChildAdapter = new AttentionIndicatorAdapter(getSupportFragmentManager(),indexChildFragments);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(indexChildAdapter);
        //实例化TabPageIndicator然后设置ViewPager与之关联
        indicator.setViewPager(pager);
    }
    @OnClick(R.id.attention_ib_top)
    public void back(){
        finish();
    }
}
