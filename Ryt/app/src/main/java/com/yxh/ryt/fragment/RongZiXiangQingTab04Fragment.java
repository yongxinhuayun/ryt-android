package com.yxh.ryt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.InvestorRecord;
import com.yxh.ryt.vo.RongZi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;


/**
 * Created by sj on 15/11/25.
 */
public class RongZiXiangQingTab04Fragment extends StickHeaderBaseFragment{
    private ListView lstv;
    private CommonAdapter<InvestorRecord> investorRecordCommonAdapter;
    private List<InvestorRecord> investorRecordDatas;
    private int currentPage=1;
    public RongZiXiangQingTab04Fragment(StickHeaderViewPagerManager manager, int position) {
        super(manager, position);
    }

    public RongZiXiangQingTab04Fragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        super(manager, position, isCanPulltoRefresh);
    }

    public static RongZiXiangQingTab04Fragment newInstance(StickHeaderViewPagerManager manager, int position) {
        RongZiXiangQingTab04Fragment listFragment = new RongZiXiangQingTab04Fragment(manager, position);
        return listFragment;
    }

    public static RongZiXiangQingTab04Fragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        RongZiXiangQingTab04Fragment listFragment = new RongZiXiangQingTab04Fragment(manager, position, isCanPulltoRefresh);
        return listFragment;
    }

    @Override
    public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        ListView mListview = (ListView)view.findViewById(R.id.v_scroll);
        placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);
        return view;
    }

    @Override
    protected void lazyLoad() {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artWorkId","qydeyugqqiugd2");
        paramsMap.put("tab", "invest");
        paramsMap.put("pageSize", Constants.pageSize + "");
        paramsMap.put("pageIndex", currentPage + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "investorArtWork.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {

            }
        });
    }
}
