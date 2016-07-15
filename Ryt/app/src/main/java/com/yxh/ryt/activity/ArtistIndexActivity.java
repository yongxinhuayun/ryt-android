package com.yxh.ryt.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.ArtistTabPageIndicatorAdapter;
import com.yxh.ryt.adapter.IndexTabPageIndicatorAdapter;
import com.yxh.ryt.fragment.ArtistHomeFragment;
import com.yxh.ryt.fragment.BaseFragment;
import com.yxh.ryt.fragment.ChuangZuoItemFragment;
import com.yxh.ryt.fragment.PaiMaiItemFragment;
import com.yxh.ryt.fragment.RongZiItemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/8.
 */
public class ArtistIndexActivity extends BaseActivity {
    List<Fragment> indexChildFragments=new ArrayList<>();
    FragmentPagerAdapter indexChildAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artistindex);
        indexChildFragments.add(new ArtistHomeFragment("",""));
        indexChildFragments.add(new ChuangZuoItemFragment());
        indexChildFragments.add(new PaiMaiItemFragment());
        indexChildFragments.add(new PaiMaiItemFragment());
        indexChildAdapter = new ArtistTabPageIndicatorAdapter(getSupportFragmentManager(),indexChildFragments);
        ViewPager pager = (ViewPager)findViewById(R.id.aai_pager);
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(indexChildAdapter);
        //实例化TabPageIndicator然后设置ViewPager与之关联
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.aai_indicator);
        indicator.setViewPager(pager);
    }

}
