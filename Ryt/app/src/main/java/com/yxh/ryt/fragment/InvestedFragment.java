package com.yxh.ryt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yxh.ryt.R;

/**
 * Created by Administrator on 2016/7/18.
 */
public class InvestedFragment extends BaseFragment {
    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_invested, null);

        return view;
    }


    }


