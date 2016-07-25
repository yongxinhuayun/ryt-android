package com.yxh.ryt.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.ArtistIndexActivity;
import com.yxh.ryt.activity.FinanceSummaryActivity;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.activity.UserYsjIndexActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.RongZi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/18.
 */
public class InvestedFragment extends BaseFragment implements AdapterView.OnItemClickListener, AutoListView.OnRefreshListener, AutoListView.OnLoadListener {
    private AutoListView lstv;
    private CommonAdapter<RongZi> rongZiCommonAdapter;
    private List<RongZi> rongZiDatas;
    private int currentPage = 1;
    private Map<Integer,Boolean> selected;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rongZiDatas=new ArrayList<RongZi>();
        selected=new HashMap<>();
    }
    private void LoadData(final int state, final int pageNum) {
        final Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userId", AppApplication.gUser.getId());
        paramsMap.put("action", "invest");
        paramsMap.put("pageSize", Constants.pageSize + "");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "getArtWorkList.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(final Map<String, Object> response) {
                if ("".equals(AppApplication.gUser.getId())){
                    if (state == AutoListView.REFRESH) {
                        lstv.onRefreshComplete();
                        rongZiDatas.clear();
                        List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson()
                                .toJson(((Map<Object,Object>) response.get("data")).get("artworkList")), new TypeToken<List<RongZi>>() {
                        }.getType());
                        if (null == objectList || objectList.size() == 0) {
                            lstv.setResultSize(0);
                        }
                        if (null != objectList && objectList.size() > 0) {
                            lstv.setResultSize(objectList.size());
                            rongZiDatas.addAll(objectList);
                            rongZiCommonAdapter.notifyDataSetChanged();
                        }
                        return;
                    }
                    if (state == AutoListView.LOAD) {
                        lstv.onLoadComplete();
                        List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson()
                                .toJson(((Map<Object,Object>) response.get("data")).get("artworkList")), new TypeToken<List<RongZi>>() {
                        }.getType());
                        if (null == objectList || objectList.size() == 0) {
                            lstv.setResultSize(1);
                        }
                        if (null != objectList && objectList.size() > 0) {
                            lstv.setResultSize(objectList.size());
                            rongZiDatas.addAll(objectList);
                            rongZiCommonAdapter.notifyDataSetChanged();
                        }
                        return;
                    }
                }else {
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){

                                NetRequestUtil.post(Constants.BASE_PATH + "getArtWorkList.do", paramsMap, new RongZiListCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e) {
                                        ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
                                    }

                                    @Override
                                    public void onResponse(Map<String, Object> response) {
                                        if (state == AutoListView.REFRESH) {
                                            lstv.onRefreshComplete();
                                            rongZiDatas.clear();
                                            List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson()
                                                    .toJson(((Map<Object,Object>) response.get("data")).get("artworkList")), new TypeToken<List<RongZi>>() {
                                            }.getType());
                                            if (null == objectList || objectList.size() == 0) {
                                                lstv.setResultSize(0);
                                            }
                                            if (null != objectList && objectList.size() > 0) {
                                                lstv.setResultSize(objectList.size());
                                                rongZiDatas.addAll(objectList);
                                                rongZiCommonAdapter.notifyDataSetChanged();
                                            }
                                            return;
                                        }
                                        if (state == AutoListView.LOAD) {
                                            lstv.onLoadComplete();
                                            List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
                                                    toJson(((Map<Object,Object>) response.get("data")).get("artworkList")), new TypeToken<List<RongZi>>() {
                                            }.getType());
                                            if (null == objectList || objectList.size() == 0) {
                                                lstv.setResultSize(1);
                                            }
                                            if (null != objectList && objectList.size() > 0) {
                                                lstv.setResultSize(objectList.size());
                                                rongZiDatas.addAll(objectList);
                                                rongZiCommonAdapter.notifyDataSetChanged();
                                            }
                                            return;
                                        }
                                    }
                                });
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.fragment_item, container, false);
        lstv = (AutoListView) contextView.findViewById(R.id.lstv);
        lstv.setPageSize(Constants.pageSize);
        rongZiCommonAdapter=new CommonAdapter<RongZi>(AppApplication.getSingleContext(),rongZiDatas,R.layout.invest_item) {

            @Override
            public void convert(final ViewHolder helper, final RongZi item) {
                if (item!=null){
                    helper.setText(R.id.clh_tv_title,item.getTitle());
                    helper.setText(R.id.clh_tv_brief,item.getBrief());
                    if (selected.size()<rongZiDatas.size()){
                        for (int i=selected.size();i<rongZiDatas.size();i++){
                            selected.put(i,false);
                        }
                    }
                    if (item.getAuthor()!=null){
                        helper.setText(R.id.clh_tv_artistName,item.getAuthor().getName()+"");
                        helper.setImageByUrl(R.id.clh_cv_headerImage,item.getAuthor().getPictureUrl());
                        helper.setText(R.id.clh_tv_totalWork,item.getAuthor().getMasterWorkNum()+"件作品");
                        helper.setText(R.id.clh_tv_totalFans,item.getAuthor().getFansNum()+"个粉丝");
                        helper.getView(R.id.clh_cv_headerImage).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getActivity(), ArtistIndexActivity.class);
                                intent.putExtra("userId",item.getAuthor().getId());
                                intent.putExtra("name",item.getAuthor().getId());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
                            }
                        });
                    }
                    helper.setImageByUrl(R.id.clh_tv_prc, item.getPicture_url());
                    if (null!=item.getAuthor().getMaster()&&!"".equals(item.getAuthor().getMaster().getTitle())){
                        helper.getView(R.id.clh_tv_artistTitle).setVisibility(View.VISIBLE);
                        helper.setText(R.id.clh_tv_artistTitle, item.getAuthor().getMaster().getTitle());
                    }else{
                        helper.getView(R.id.clh_tv_artistTitle).setVisibility(View.GONE);
                    }
                    if ("1".equals(item.getType())){
                        helper.getView(R.id.ll_finance).setVisibility(View.VISIBLE);
                        helper.getView(R.id.ll_state_auction).setVisibility(View.GONE);
                        helper.getView(R.id.ll_creat).setVisibility(View.GONE);
                        helper.getView(R.id.ll_auction).setVisibility(View.GONE);
                        helper.setText(R.id.fli1_tv_date, Utils.getJudgeDate(item.getInvestRestTime())+"后截止");
                        helper.setText(R.id.fli1_tv_money,item.getInvestsMoney()+"元/"+item.getInvestGoalMoney()+"元");
                        double value = item.getInvestsMoney().doubleValue() / item.getInvestGoalMoney().doubleValue();
                        helper.setProgress(R.id.fli1_pb_progress, (int)(value*100));
                    }else if ("2".equals(item.getType())){
                        helper.getView(R.id.ll_finance).setVisibility(View.GONE);
                        helper.getView(R.id.ll_state_auction).setVisibility(View.GONE);
                        helper.getView(R.id.ll_creat).setVisibility(View.VISIBLE);
                        helper.getView(R.id.ll_auction).setVisibility(View.GONE);
                        helper.setText(R.id.cli1_tv_update,Utils.timeAndIos(item.getNewCreationDate())+"更新:");
                        helper.setText(R.id.cli1_tv_finish,"预计"+Utils.timeAndIos(item.getCreationEndDatetime())+"完工");
                    }else if ("3".equals(item.getType())){
                        helper.getView(R.id.ll_finance).setVisibility(View.GONE);
                        helper.getView(R.id.ll_state_auction).setVisibility(View.VISIBLE);
                        helper.getView(R.id.ll_creat).setVisibility(View.GONE);
                        helper.getView(R.id.ll_auction).setVisibility(View.VISIBLE);
                        if ("30".equals(item.getStep())){
                            helper.setText(R.id.ali1_tv_content,"拍卖时间 "+Utils.timeAuction(item.getAuctionStartDatetime()));
                            helper.setText(R.id.clh1_tv_state,"拍卖预告");
                        }else if ("31".equals(item.getStep())){
                            helper.setText(R.id.ali1_tv_content,Utils.getJudgeDate1(item.getAuctionEndDatetime())+"后截止");
                            helper.setText(R.id.clh1_tv_state,"拍卖中");
                        }else {
                            helper.setText(R.id.ali1_tv_content,"拍卖得主 "+item.getWinner().getName());
                            helper.setText(R.id.clh1_tv_state,"拍卖结束");
                        }
                    }else {
                        helper.getView(R.id.ll_finance).setVisibility(View.VISIBLE);
                        helper.getView(R.id.ll_state_auction).setVisibility(View.GONE);
                        helper.getView(R.id.ll_creat).setVisibility(View.GONE);
                        helper.getView(R.id.ll_auction).setVisibility(View.GONE);
                        helper.setText(R.id.fli1_tv_date, Utils.getJudgeDate(item.getInvestRestTime())+"后截止");
                        helper.setText(R.id.fli1_tv_money,item.getInvestsMoney()+"元/"+item.getInvestGoalMoney()+"元");
                        double value = item.getInvestsMoney().doubleValue() / item.getInvestGoalMoney().doubleValue();
                        helper.setProgress(R.id.fli1_pb_progress, (int)(value*100));
                    }
                    if (item.isPraise()){
                        helper.getView(R.id.clh_ll_praise).setBackgroundResource(R.drawable.praise1);
                        helper.getView(R.id.clh_ll_praise).setBackgroundColor(Color.rgb(205,55,56));
                        ((TextView) helper.getView(R.id.clh_tv_praiseNum)).setTextColor(Color.rgb(255,255,255));
                        helper.setText(R.id.clh_tv_praiseNum,item.getPraiseNUm()+"");
                        helper.getView(R.id.clh_ll_praise).setEnabled(false);
                    }else {
                        if (selected.get(helper.getPosition())){
                            helper.getView(R.id.clh_ll_praise).setBackgroundResource(R.drawable.praise1);
                            helper.getView(R.id.clh_ll_praise).setBackgroundColor(Color.rgb(205,55,56));
                            ((TextView) helper.getView(R.id.clh_tv_praiseNum)).setTextColor(Color.rgb(255,255,255));
                            helper.setText(R.id.clh_tv_praiseNum,item.getPraiseNUm()+1+"");
                            helper.getView(R.id.clh_ll_praise).setEnabled(false);
                        }else {
                            helper.getView(R.id.clh_ll_praise).setBackgroundResource(R.drawable.praise);
                            ((TextView) helper.getView(R.id.clh_tv_praiseNum)).setTextColor(Color.rgb(205,55,56));
                            helper.setText(R.id.clh_tv_praiseNum,item.getPraiseNUm()+"");
                            helper.getView(R.id.clh_ll_praise).setEnabled(true);
                            helper.getView(R.id.clh_ll_praise).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if ("".equals(AppApplication.gUser.getId())){
                                        Intent intent=new Intent(getActivity(), LoginActivity.class);
                                        intent.putExtra("userId",item.getAuthor().getId());
                                        intent.putExtra("currentId",AppApplication.gUser.getId());
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getActivity().startActivity(intent);
                                    }else {
                                        praise(item.getId(), ((LinearLayout) helper.getView(R.id.clh_ll_praise)),((TextView) helper.getView(R.id.clh_tv_praiseNum)),item.getPraiseNUm(), ((ImageView) helper.getView(R.id.clh_iv_attention)), helper);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        };
        lstv.setOnItemClickListener(this);
        lstv.setAdapter(rongZiCommonAdapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        return contextView;
    }

    private void praise(final String artworkId, final LinearLayout view, final TextView textView, final int praiseNum, final ImageView imageView, final ViewHolder helper) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artworkId", artworkId + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "artworkPraise.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    ToastUtil.showLong(getActivity(), "点赞成功");
                    view.setBackgroundResource(R.drawable.praise1);
                    textView.setTextColor(Color.rgb(255,255,255));
                    textView.setText(praiseNum+1+"");
                    view.setEnabled(false);
                    selected.put(helper.getPosition(),true);
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                praise(artworkId, view, textView, praiseNum, imageView, helper);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    protected void lazyLoad() {
        if(rongZiDatas!=null&&rongZiDatas.size()>0)return;
        LoadData(AutoListView.REFRESH, currentPage);
    }
    @Override
    public void onRefresh() {
        currentPage=1;
        LoadData(AutoListView.REFRESH,currentPage);
    }

    @Override
    public void onLoad() {
        currentPage++;
        LoadData(AutoListView.LOAD, currentPage);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position<=rongZiDatas.size()){
			/*int[] location = new  int[2] ;
			view1.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
			view1.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
			System.out.println("view--->x坐标:"+location [0]+"view--->y坐标:"+location [1]);
			int[] location1 = new  int[2] ;
			view2.getLocationInWindow(location1); //获取在当前窗口内的绝对坐标
			view2.getLocationOnScreen(location1);//获取在整个屏幕内的绝对坐标
			System.out.println("view--->x坐标:"+location1 [0]+"view--->y坐标:"+location1 [1]);*/
            Intent intent=new Intent(getActivity(), FinanceSummaryActivity.class);
            intent.putExtra("id",rongZiDatas.get(position-1).getId());
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    }


