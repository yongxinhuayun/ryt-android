package com.yxh.ryt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.TabPageIndicatorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-4-4.
 */
public class TabFragment01 extends  BaseFragment {
    List<BaseFragment> indexChildFragments=new ArrayList<>();
    FragmentPagerAdapter indexChildAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        indexChildFragments.add(new RongZiItemFragment());
        indexChildFragments.add(new ChuangZuoItemFragment());
        indexChildFragments.add(new PaiMaiItemFragment());
    }


    @Override
    protected void lazyLoad() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_01, null);
        indexChildAdapter = new TabPageIndicatorAdapter(getActivity().getSupportFragmentManager(),indexChildFragments);
        ViewPager pager = (ViewPager)view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(indexChildAdapter);

        //实例化TabPageIndicator然后设置ViewPager与之关联
        TabPageIndicator indicator = (TabPageIndicator)view.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        return view;
    }


}
