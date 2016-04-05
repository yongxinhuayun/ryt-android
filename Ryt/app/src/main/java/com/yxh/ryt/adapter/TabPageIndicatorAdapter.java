package com.yxh.ryt.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yxh.ryt.Constants;
import com.yxh.ryt.fragment.ItemFragment;

/**
 * Created by 吴洪杰 on 2016/4/5.
 */
public class TabPageIndicatorAdapter extends FragmentPagerAdapter {

    public TabPageIndicatorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        //新建一个Fragment来展示ViewPager item的内容，并传递参数
        Fragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString("arg", Constants.TITLE[position]);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Constants.TITLE[position % Constants.TITLE.length];
    }

    @Override
    public int getCount() {
        return Constants.TITLE.length;
    }
}
