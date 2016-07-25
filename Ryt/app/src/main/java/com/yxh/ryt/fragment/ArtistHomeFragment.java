package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.activity.UserYsjIndexActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.LoadingUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.FollowUserUtil;
import com.yxh.ryt.vo.HomeYSJArtWork;
import com.yxh.ryt.vo.RongZi;
import com.yxh.ryt.vo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/8.
 */
@SuppressLint("ValidFragment")
public class ArtistHomeFragment extends BaseFragment implements AutoListView.OnLoadListener, AutoListView.OnRefreshListener {
    private AutoListView lstv;
    private CommonAdapter<HomeYSJArtWork> attentionCommonAdapter;
    private List<HomeYSJArtWork> attentionDatas;
    private int currentPage=1;
    private String flag;
    private boolean bo = false;
    private String userId;
    private View header;
    private String reward;
    private String followNum;
    private String sumInvestsMoney;
    private boolean isFollowed;
    private User user;
    private CircleImageView headPicture;
    private TextView name;
    private TextView title;
    private ImageView attention;
    private TextView totalMoney;
    private TextView premiumRate;
    private TextView totalWrok;
    private TextView totalFans;
    private int width;
    private int height;
    private LoadingUtil loadingUtil;
    private LinearLayout other;

    public ArtistHomeFragment( String userId) {
        super();
        this.userId=userId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attentionDatas=new ArrayList<HomeYSJArtWork>();
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        width = metric.widthPixels;
        height = metric.heightPixels;
        loadingUtil = new LoadingUtil(getActivity(),width,height);
    }

    private void LoadData(final int state,int pageNum) {
        loadingUtil.show();
        final Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userId",userId);
        //paramsMap.put("currentId",AppApplication.gUser.getId());
        paramsMap.put("pageSize", Constants.pageSize+"");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "userMain.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("".equals(AppApplication.gUser.getId())){
                    if ("0".equals(response.get("resultCode"))){
                        loadingUtil.dismiss();
                        Map<Object,Object> object= (Map<Object, Object>) response.get("object");
                        reward= AppApplication.getSingleGson().toJson(object.get("reward"));
                        followNum= AppApplication.getSingleGson().toJson(object.get("followNum"));
                        sumInvestsMoney= AppApplication.getSingleGson().toJson(object.get("sumInvestsMoney"));
                        isFollowed= Boolean.parseBoolean(AppApplication.getSingleGson().toJson(object.get("isFollowed")));
                        user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("master")), User.class);
                        AppApplication.displayImage(user.getPictureUrl(),headPicture);
                        name.setText(user.getName());
                        title.setText(user.getMaster().getTitle());
                        if (isFollowed){
                            attention.setImageResource(R.mipmap.yiguanzhu);
                        }else {
                            attention.setImageResource(R.mipmap.weiguanzhu);
                        }
                        totalMoney.setText("¥"+sumInvestsMoney);
                        if ("0".equals(sumInvestsMoney)){
                            premiumRate.setText("0%");
                        }else {
                            float rate=Integer.valueOf(reward)/Integer.valueOf(sumInvestsMoney);
                            premiumRate.setText((int)(rate*100)+"%");
                        }
                        totalWrok.setText(user.getMasterWorkNum()+"件作品");
                        totalFans.setText(user.getFansNum()+"位粉丝");
                        if (state == AutoListView.REFRESH) {
                            lstv.onRefreshComplete();
                            attentionDatas.clear();
                            List<HomeYSJArtWork> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkList")), new TypeToken<List<HomeYSJArtWork>>() {
                            }.getType());
                            if (null == objectList || objectList.size() == 0) {
                                lstv.setResultSize(0);
                                attentionCommonAdapter.notifyDataSetChanged();
                            }
                            if (null != objectList && objectList.size() > 0) {
                                lstv.setResultSize(objectList.size());
                                attentionDatas.addAll(objectList);
                                attentionCommonAdapter.notifyDataSetChanged();
                            }
                            return;
                        }
                        if (state == AutoListView.LOAD) {
                            lstv.onLoadComplete();
                            List<HomeYSJArtWork> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkList")), new TypeToken<List<HomeYSJArtWork>>() {
                            }.getType());
                            if (null == objectList || objectList.size() == 0) {
                                lstv.setResultSize(1);
                                attentionCommonAdapter.notifyDataSetChanged();
                            }
                            if (null != objectList && objectList.size() > 0) {
                                lstv.setResultSize(objectList.size());
                                attentionDatas.addAll(objectList);
                                attentionCommonAdapter.notifyDataSetChanged();
                            }
                            return;
                        }
                    }
                }else {
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){

                                NetRequestUtil.post(Constants.BASE_PATH + "userMain.do", paramsMap, new RongZiListCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e) {
                                        ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
                                    }

                                    @Override
                                    public void onResponse(Map<String, Object> response) {
                                        if ("0".equals(response.get("resultCode"))){
                                            loadingUtil.dismiss();
                                            Map<Object,Object> object= (Map<Object, Object>) response.get("object");
                                            reward= AppApplication.getSingleGson().toJson(object.get("reward"));
                                            followNum= AppApplication.getSingleGson().toJson(object.get("followNum"));
                                            sumInvestsMoney= AppApplication.getSingleGson().toJson(object.get("sumInvestsMoney"));
                                            isFollowed= Boolean.parseBoolean(AppApplication.getSingleGson().toJson(object.get("isFollowed")));
                                            user = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("master")), User.class);
                                            AppApplication.displayImage(user.getPictureUrl(),headPicture);
                                            name.setText(user.getName());
                                            title.setText(user.getMaster().getTitle());
                                            if (isFollowed){
                                                attention.setImageResource(R.mipmap.yiguanzhu);
                                            }else {
                                                attention.setImageResource(R.mipmap.weiguanzhu);
                                            }
                                            totalMoney.setText("¥"+sumInvestsMoney);
                                            if ("0".equals(sumInvestsMoney)){
                                                premiumRate.setText("0%");
                                            }else {
                                                float rate=Integer.valueOf(reward)/Integer.valueOf(sumInvestsMoney);
                                                premiumRate.setText((int)(rate*100)+"%");
                                            }
                                            totalWrok.setText(user.getMasterWorkNum()+"件作品");
                                            totalFans.setText(user.getFansNum()+"位粉丝");
                                            if (state == AutoListView.REFRESH) {
                                                lstv.onRefreshComplete();
                                                attentionDatas.clear();
                                                List<HomeYSJArtWork> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkList")), new TypeToken<List<HomeYSJArtWork>>() {
                                                }.getType());
                                                if (null == objectList || objectList.size() == 0) {
                                                    lstv.setResultSize(0);
                                                    attentionCommonAdapter.notifyDataSetChanged();
                                                }
                                                if (null != objectList && objectList.size() > 0) {
                                                    lstv.setResultSize(objectList.size());
                                                    attentionDatas.addAll(objectList);
                                                    attentionCommonAdapter.notifyDataSetChanged();
                                                }
                                                return;
                                            }
                                            if (state == AutoListView.LOAD) {
                                                lstv.onLoadComplete();
                                                List<HomeYSJArtWork> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkList")), new TypeToken<List<HomeYSJArtWork>>() {
                                                }.getType());
                                                if (null == objectList || objectList.size() == 0) {
                                                    lstv.setResultSize(1);
                                                    attentionCommonAdapter.notifyDataSetChanged();
                                                }
                                                if (null != objectList && objectList.size() > 0) {
                                                    lstv.setResultSize(objectList.size());
                                                    attentionDatas.addAll(objectList);
                                                    attentionCommonAdapter.notifyDataSetChanged();
                                                }
                                                return;
                                            }
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
        header = LayoutInflater.from(getActivity()).inflate(R.layout.header_artisthome, null);
        headPicture = ((CircleImageView) header.findViewById(R.id.rs_iv_headPortrait));
        name = ((TextView) header.findViewById(R.id.hah_tv_name));
        other = ((LinearLayout) header.findViewById(R.id.uh1_ll_other));
        title = ((TextView) header.findViewById(R.id.hah_tv_title));
        attention = ((ImageView) header.findViewById(R.id.uh1_iv_attention));
        totalMoney = ((TextView) header.findViewById(R.id.hah_tv_totalMoney));
        premiumRate = ((TextView) header.findViewById(R.id.hah_tv_premiumRate));
        totalWrok = ((TextView) header.findViewById(R.id.hah_tv_totalWork));
        totalFans = ((TextView) header.findViewById(R.id.hah_tv_totalFans));
        attentionCommonAdapter=new CommonAdapter<HomeYSJArtWork>(AppApplication.getSingleContext(),attentionDatas,R.layout.listview_item_artisthome) {
            @Override
            public void convert(final ViewHolder helper, final HomeYSJArtWork item) {
                helper.setText(R.id.liah_tv_title,item.getTitle());
                helper.setText(R.id.liah_tv_brief,item.getBrief());
                helper.setImageByUrl(R.id.liah_si_prc,item.getPicture_url());
                if ("".equals(AppApplication.artWorkMap.get(item.getStep()))){
                    helper.getView(R.id.lia_fl_state).setVisibility(View.GONE);
                }else if ("融资中".equals(AppApplication.artWorkMap.get(item.getStep()))){
                    helper.getView(R.id.lia_fl_state).setVisibility(View.VISIBLE);
                    helper.getView(R.id.liah_iv_progress).setVisibility(View.VISIBLE);
                    helper.setText(R.id.liah_iv_state,"融资中￥"+item.getInvestsMoney());
                }else {
                    helper.getView(R.id.lia_fl_state).setVisibility(View.VISIBLE);
                    helper.getView(R.id.liah_iv_progress).setVisibility(View.GONE);
                    helper.setText(R.id.liah_iv_state,AppApplication.artWorkMap.get(item.getStep()));
                }
            }
        };
        lstv.setAdapter(attentionCommonAdapter);
        lstv.addHeaderView(header);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        if (AppApplication.gUser.getId().equals(userId)){
            other.setVisibility(View.GONE);
        }
        return contextView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    protected void lazyLoad() {
        if(attentionDatas!=null&&attentionDatas.size()>0)return;
        LoadData(AutoListView.REFRESH, currentPage);
    }
    @Override
    public void onRefresh() {
        currentPage = 1;
        attentionDatas.clear();
        LoadData(AutoListView.REFRESH,currentPage);
    }
    @Override
    public void onLoad() {
        currentPage++;
        LoadData(AutoListView.LOAD, currentPage);
    }

}
