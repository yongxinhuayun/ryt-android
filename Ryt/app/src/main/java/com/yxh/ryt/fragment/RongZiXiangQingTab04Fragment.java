package com.yxh.ryt.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.ArtworkInvest;
import com.yxh.ryt.vo.RongZi;
import com.yxh.ryt.vo.User;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.ScrollHolder;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;


/**
 * Created by sj on 15/11/25.
 */
public class RongZiXiangQingTab04Fragment extends StickHeaderBaseFragment{
    private ListView mListview;
    private CommonAdapter<ArtworkInvest> investorRecordCommonAdapter;
    private List<ArtworkInvest> investorDatas;
    private List<ArtworkInvest> investorTOpDatas;
    private int currentPage=1;
    private View footer;
    private TextView loadFull;
    private TextView noData;
    private TextView more;
    private ProgressBar loading;
    private int lastItem;
    private boolean loadComplete=true;
    static StickHeaderViewPagerManager stickHeaderViewPagerManager;
    private CircleImageView icon1;
    private CircleImageView icon2;
    private CircleImageView icon3;
    private TextView title1;
    private TextView title2;
    private TextView title3;
    private TextView date1;
    private TextView date2;
    private TextView date3;
    private TextView money1;
    private TextView money2;
    private TextView money3;

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
        stickHeaderViewPagerManager=manager;
        return listFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        investorDatas=new ArrayList<>();
        investorTOpDatas=new ArrayList<>();
    }

    @Override
    public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_record, null);
        mListview = (ListView)view.findViewById(R.id.flr_scroll);
        footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null);
        placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);
        findView(view);
        setAdapter();
        onScroll();
        investorDatas.clear();
        investorTOpDatas.clear();
        LoadData(true, currentPage);
        return view;
    }

    private void topDatas() {
        if (investorTOpDatas.size()!=0){
             AppApplication.displayImage(investorTOpDatas.get(0).getCreator().getPictureUrl(),icon1);
            AppApplication.displayImage(investorTOpDatas.get(1).getCreator().getPictureUrl(), icon2);
            AppApplication.displayImage(investorTOpDatas.get(2).getCreator().getPictureUrl(),icon3);
            title1.setText(investorTOpDatas.get(0).getCreator().getName());
            title2.setText(investorTOpDatas.get(1).getCreator().getName());
            title3.setText(investorTOpDatas.get(2).getCreator().getName());
            date1.setText( Utils.timeTransComment(investorTOpDatas.get(0).getCreateDatetime()));
            date2.setText(Utils.timeTransComment(investorTOpDatas.get(1).getCreateDatetime()));
            date3.setText(Utils.timeTransComment(investorTOpDatas.get(2).getCreateDatetime()));
            money1.setText(investorTOpDatas.get(0).getPrice()+"");
            money2.setText(investorTOpDatas.get(1).getPrice()+"");
            money3.setText(investorTOpDatas.get(2).getPrice()+"");
        }
    }

    private void findView(View view) {
        icon1 = ((CircleImageView) view.findViewById(R.id.flr_iv_icon1));
        icon2 = ((CircleImageView) view.findViewById(R.id.flr_iv_icon2));
        icon3 = ((CircleImageView) view.findViewById(R.id.flr_iv_icon3));
        title1 = ((TextView) view.findViewById(R.id.flr_tv_title1));
        title2 = ((TextView) view.findViewById(R.id.flr_tv_title2));
        title3 = ((TextView) view.findViewById(R.id.flr_tv_title3));
        date1 = ((TextView) view.findViewById(R.id.flr_tv_date1));
        date2 = ((TextView) view.findViewById(R.id.flr_tv_date2));
        date3 = ((TextView) view.findViewById(R.id.flr_tv_date3));
        money1 = ((TextView) view.findViewById(R.id.flr_tv_money1));
        money2 = ((TextView) view.findViewById(R.id.flr_tv_money2));
        money3 = ((TextView) view.findViewById(R.id.flr_tv_money3));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void onScroll() {
        stickHeaderViewPagerManager.setOnListViewScrollListener(new StickHeaderViewPagerManager.OnListViewScrollListener() {
            @Override
            public void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 2;
            }

            @Override
            public void onListViewScrollStateChanged(AbsListView view, int scrollState) {
                if (lastItem==investorRecordCommonAdapter.getCount() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && loadComplete) {
                    more.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    loadFull.setVisibility(View.GONE);
                    noData.setVisibility(View.GONE);
                    currentPage = currentPage + 1;
                    LoadData(false, currentPage);
                }
            }
        });
    }
    private void setAdapter() {
        investorRecordCommonAdapter=new CommonAdapter<ArtworkInvest>(getActivity(),investorDatas,R.layout.investorrecord_item) {
            @Override
            public void convert(ViewHolder helper, ArtworkInvest item) {
                helper.setText(R.id.iri_tv_nickname,item.getCreator().getName());
                helper.setText(R.id.iri_tv_content,"投资了"+item.getPrice()+"元");
                helper.setImageByUrl(R.id.iri_iv_icon, item.getCreator().getPictureUrl());
                helper.setText(R.id.iri_tv_date, Utils.timeTransComment(item.getCreateDatetime()));
            }
        };
        mListview.setAdapter(investorRecordCommonAdapter);
        mListview.addFooterView(footer);
        loadFull = (TextView) footer.findViewById(R.id.loadFull);
        noData = (TextView) footer.findViewById(R.id.noData);
        more = (TextView) footer.findViewById(R.id.more);
        loading = (ProgressBar) footer.findViewById(R.id.loading);
        more.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        loadFull.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);

    }

    private void LoadData(final boolean flag,int pageNum) {
        more.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        loadFull.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artWorkId","qydeyugqqiugd2");
        paramsMap.put("pageSize",Constants.pageSize+"");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkInvest.do", paramsMap, new RZCommentCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("564545455489失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    Map<String,Object> object= (Map<String, Object>) response.get("object");
                    if (flag){
                        List<ArtworkInvest> topList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestTopList")), new TypeToken<List<ArtworkInvest>>() {
                        }.getType());
                        if (topList==null){
                            more.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        }else {
                            investorTOpDatas.addAll(topList);
                            topDatas();
                            topList.clear();
                        }
                        List<ArtworkInvest> investList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestList")), new TypeToken<List<ArtworkInvest>>() {
                        }.getType());
                        if (investList ==null|| investList.size()<Constants.pageSize){
                            more.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                            loadComplete=false;
                        }else {
                            more.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.GONE);
                        }
                        if (investList!=null){
                            investorDatas.addAll(investList);
                            investList.clear();
                        }

                        investorRecordCommonAdapter.notifyDataSetChanged();
                    }else {
                        List<ArtworkInvest> investList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestList")), new TypeToken<List<ArtworkInvest>>() {
                        }.getType());
                        if (investList ==null|| investList.size()<Constants.pageSize){
                            more.setVisibility(View.GONE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                            loadComplete=false;
                        }else {
                            more.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.GONE);
                        }
                        if (investList!=null){
                            investorDatas.addAll(investList);
                            investList.clear();
                        }
                        investorRecordCommonAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    @Override
    protected void lazyLoad() {
    }

}
