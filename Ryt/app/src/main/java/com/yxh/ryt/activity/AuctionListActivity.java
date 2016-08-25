package com.yxh.ryt.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.ArtWorkBidding;
import com.yxh.ryt.vo.ArtWorkPraiseList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by YangZhenjie on 2016/8/1.
 */
public class AuctionListActivity extends BaseActivity implements AutoListView.OnLoadListener, AutoListView.OnRefreshListener, AdapterView.OnItemClickListener {

    private ImageView back;
    private AutoListView lvPraise;
    private String artWorkId;
    //private List<ArtWorkPraiseList> artWorkPraiseList;
    private List<ArtWorkBidding> praiseDatas;
    private CommonAdapter<ArtWorkBidding> praiseAdapter;
    private int currentPage = 1;
    private  Map<Integer, Boolean> selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.praise_list1);
        back = (ImageView) findViewById(R.id.iv_back);
        lvPraise = (AutoListView) findViewById(R.id.lv_praise);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        praiseDatas = new ArrayList<ArtWorkBidding>();
        selected=new HashMap<>();
        artWorkId = getIntent().getStringExtra("artWorkId");
        lvPraise.setPageSize(Constants.pageSize);
        List<ArtWorkBidding> artWorkBiddingDatas = (List<ArtWorkBidding>) getIntent().getSerializableExtra("artWorkBiddingDatas");
        praiseAdapter.notifyDataSetChanged();
        lvPraise.setResultSize(0);
        praiseAdapter = new CommonAdapter<ArtWorkBidding>(this, artWorkBiddingDatas, R.layout.auctionfragment_item) {
            @Override
            public void convert(final ViewHolder helper, final ArtWorkBidding item) {
                helper.setImageByUrl(R.id.fsl_ci_currentHeader,item.getCreator().getPictureUrl());
                if (helper.getPosition()==0){
                    ((TextView) helper.getView(R.id.fsl_tv_currentName)).setTextColor(Color.rgb(199,31,33));
                    ((TextView) helper.getView(R.id.fsl_tv_currentDate)).setTextColor(Color.rgb(199,31,33));
                    ((TextView) helper.getView(R.id.fsl_tv_currentNumber)).setTextColor(Color.rgb(199,31,33));

                }
                helper.setText(R.id.fsl_tv_currentName,item.getCreator().getName());
                helper.setText(R.id.fsl_tv_currentDate, Utils.timeToFormatTemp("MM.dd HH:mm:ss",item.getCreateDatetime()));
                helper.setText(R.id.fsl_tv_currentNumber,item.getPrice()+"");
                helper.getView(R.id.fsl_ci_currentHeader).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("1".equals(item.getCreator().getType())){
                            Intent intent=new Intent(AuctionListActivity.this,ArtistIndexActivity.class);
                            intent.putExtra("userId",item.getCreator().getId());
                            intent.putExtra("name",item.getCreator().getName());
                            AuctionListActivity.this.startActivity(intent);
                        }else if ("2".equals(item.getCreator().getType())){
                            Intent intent=new Intent(AuctionListActivity.this,UserIndexActivity.class);
                            intent.putExtra("userId",item.getCreator().getId());
                            intent.putExtra("name",item.getCreator().getName());
                            AuctionListActivity.this.startActivity(intent);
                        }
                    }
                });
            }
        };
        lvPraise.setOnItemClickListener(this);
        lvPraise.setAdapter(praiseAdapter);
        lvPraise.setOnRefreshListener(this);
        lvPraise.setOnLoadListener(this);
    }
    /*private void loadData(final int state, final int pageNum) {
        final Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artWorkId", artWorkId);
        paramsMap.put("pageSize", Constants.pageSize + "");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(paramsMap.toString() + "====");
        NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkView.do", paramsMap, new RongZiListCallBack() {

            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(AuctionListActivity.this, "网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("".equals(AppApplication.gUser.getId())) {
                    Map<String, Object> object = (Map<String, Object>) response.get("object");
                    if (state == AutoListView.REFRESH) {
                        lvPraise.onRefreshComplete();
                        praiseDatas.clear();
                        List<ArtWorkPraiseList> artWorkPraiseList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
                                toJson(object.get("artWorkPraiseList")), new TypeToken<List<ArtWorkBidding>>() {
                        }.getType());
                        if (null == artWorkPraiseList || artWorkPraiseList.size() == 0) {
                            lvPraise.setResultSize(0);
                        }
                        if (null != artWorkPraiseList && artWorkPraiseList.size() > 0) {
                            lvPraise.setResultSize(artWorkPraiseList.size());
                            praiseDatas.addAll(artWorkPraiseList);
                            praiseAdapter.notifyDataSetChanged();
                        }
                    }
                    if (state == AutoListView.LOAD) {
                        lvPraise.onLoadComplete();
                        List<ArtWorkPraiseList> artWorkPraiseList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
                                toJson(object.get("artWorkPraiseList")), new TypeToken<List<ArtWorkPraiseList>>() {
                        }.getType());
                        if (null == artWorkPraiseList || artWorkPraiseList.size() == 0) {
                            lvPraise.setResultSize(1);
                        }
                        if (null != artWorkPraiseList && artWorkPraiseList.size() > 0) {
                            lvPraise.setResultSize(artWorkPraiseList.size());
                            praiseDatas.addAll(artWorkPraiseList);
                            praiseAdapter.notifyDataSetChanged();
                        }

                    }
                    if (pageNum == 1 && praiseDatas.size() > 0) {
                        selected.clear();
                    }
                    if (selected.size() <= praiseDatas.size()) {
                        for (int i = selected.size(); i < praiseDatas.size(); i++) {
                            selected.put(i, false);
                        }
                    }
                }else {
                    SessionLogin sessionLogin = new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)) {
                                NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkView.do", paramsMap, new RongZiListCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e) {
                                        ToastUtil.showLong(AuctionListActivity.this, "网络连接超时,稍后重试!");
                                    }

                                    @Override
                                    public void onResponse(Map<String, Object> response) {
                                        Map<String, Object> object = (Map<String, Object>) response.get("object");
                                        if (state == AutoListView.REFRESH) {
                                            lvPraise.onRefreshComplete();
                                            praiseDatas.clear();
                                            List<ArtWorkPraiseList> artWorkPraiseList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
                                                    toJson(object.get("artWorkPraiseList")), new TypeToken<List<ArtWorkPraiseList>>() {
                                            }.getType());
                                            if (null == artWorkPraiseList || artWorkPraiseList.size() == 0) {
                                                lvPraise.setResultSize(0);
                                            }
                                            if (null != artWorkPraiseList && artWorkPraiseList.size() > 0) {
                                                lvPraise.setResultSize(artWorkPraiseList.size());
                                                praiseDatas.addAll(artWorkPraiseList);
                                                praiseAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        if (state == AutoListView.LOAD) {
                                            lvPraise.onLoadComplete();
                                            List<ArtWorkPraiseList> artWorkPraiseList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
                                                    toJson(object.get("artWorkPraiseList")), new TypeToken<List<ArtWorkPraiseList>>() {
                                            }.getType());
                                            if (null == artWorkPraiseList || artWorkPraiseList.size() == 0) {
                                                lvPraise.setResultSize(1);
                                            }
                                            if (null != artWorkPraiseList && artWorkPraiseList.size() > 0) {
                                                lvPraise.setResultSize(artWorkPraiseList.size());
                                                praiseDatas.addAll(artWorkPraiseList);
                                                praiseAdapter.notifyDataSetChanged();
                                            }

                                        }
                                        if (pageNum == 1 && selected.size() > 0) {
                                            selected.clear();
                                        }
                                        if (selected.size() <= praiseDatas.size()) {
                                            for (int i = selected.size(); i < praiseDatas.size(); i++) {
                                                selected.put(i, praiseDatas.get(i).isFollowed());
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
    }*/

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position <= praiseDatas.size()) {

        }
    }
}