package com.yxh.ryt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yxh.ryt.R;

/**
 * Created by Administrator on 2016-4-4.
 */
public class TabFragment03 extends  BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_03, null);
        return view;
    }

    @Override
    protected void lazyLoad() {

    }

}
