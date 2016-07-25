package com.yxh.ryt.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.ArtistTabPageIndicatorAdapter;
import com.yxh.ryt.fragment.ArtistHomeFragment;
import com.yxh.ryt.fragment.BriefFragment;
import com.yxh.ryt.fragment.InvestedFragment;
import com.yxh.ryt.fragment.WorksFragment;

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
        String userId = getIntent().getStringExtra("userId");
        String name=getIntent().getStringExtra("name");
        TextView textName = (TextView) findViewById(R.id.aai_tv_name);
        textName.setText(name);
        indexChildFragments.add(new ArtistHomeFragment(userId));
        indexChildFragments.add(new BriefFragment(userId));
        indexChildFragments.add(new WorksFragment(userId));
        indexChildFragments.add(new InvestedFragment());
        indexChildAdapter = new ArtistTabPageIndicatorAdapter(getSupportFragmentManager(),indexChildFragments);
        ViewPager pager = (ViewPager)findViewById(R.id.aai_pager);
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(indexChildAdapter);
        //实例化TabPageIndicator然后设置ViewPager与之关联
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.aai_indicator);
        indicator.setViewPager(pager);
    }

}
