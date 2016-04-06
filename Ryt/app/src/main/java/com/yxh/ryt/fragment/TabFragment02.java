package com.yxh.ryt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.IndexTabPageIndicatorAdapter;
import com.yxh.ryt.adapter.PaiHangTabPageIndicatorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-4-4.
 */
public class TabFragment02 extends  BaseFragment {
    List<BaseFragment> paiHangChildFragments=new ArrayList<>();
    FragmentPagerAdapter paiHangChildAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paiHangChildFragments.add(new PaiHangItemFragment01());
        paiHangChildFragments.add(new PaiHangItemFragment01());
    }


    @Override
    protected void lazyLoad() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_02, null);
        paiHangChildAdapter = new PaiHangTabPageIndicatorAdapter(getActivity().getSupportFragmentManager(),paiHangChildFragments);
        ViewPager pager = (ViewPager)view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(paiHangChildAdapter);
        TabPageIndicator indicator = (TabPageIndicator)view.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        return view;
    }


}
