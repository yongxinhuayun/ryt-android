package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yxh.ryt.R;

import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;


/**
 * Created by sj on 15/11/25.
 */
@SuppressLint("ValidFragment")
public class RongZiXiangQingTab02Fragment extends StickHeaderBaseFragment{

    public RongZiXiangQingTab02Fragment(StickHeaderViewPagerManager manager, int position) {
        super(manager, position);
    }

    public RongZiXiangQingTab02Fragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        super(manager, position, isCanPulltoRefresh);
    }

    public static RongZiXiangQingTab02Fragment newInstance(StickHeaderViewPagerManager manager, int position) {
        RongZiXiangQingTab02Fragment listFragment = new RongZiXiangQingTab02Fragment(manager, position);
        return listFragment;
    }

    public static RongZiXiangQingTab02Fragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        RongZiXiangQingTab02Fragment listFragment = new RongZiXiangQingTab02Fragment(manager, position, isCanPulltoRefresh);
        return listFragment;
    }
    public RongZiXiangQingTab02Fragment(){
        super();
    }
    @Override
    public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        ListView mListview = (ListView)view.findViewById(R.id.v_scroll);
        placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);

        int size = 100;
        String[] stringArray = new String[size];
        for (int i = 0; i < size; ++i) {
            stringArray[i] = ""+i;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, stringArray);
        mListview.setAdapter(adapter);
        return view;
    }

    @Override
    protected void lazyLoad() {

    }
}
