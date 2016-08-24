package com.yxh.ryt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

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
import com.yxh.ryt.vo.ArtWorkPraiseList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by YangZhenjie on 2016/8/1.
 */
public class PraiseListActivity extends BaseActivity implements AutoListView.OnLoadListener, AutoListView.OnRefreshListener, AdapterView.OnItemClickListener {

    private ImageView back;
    private AutoListView lvPraise;
    private String artWorkId;
    //private List<ArtWorkPraiseList> artWorkPraiseList;
    private List<ArtWorkPraiseList> praiseDatas;
    private CommonAdapter<ArtWorkPraiseList> praiseAdapter;
    private int currentPage = 1;
    private  Map<Integer, Boolean> selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.praise_list);
        back = (ImageView) findViewById(R.id.iv_back);
        lvPraise = (AutoListView) findViewById(R.id.lv_praise);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        praiseDatas = new ArrayList<ArtWorkPraiseList>();
        selected=new HashMap<>();
        artWorkId = getIntent().getStringExtra("artWorkId");
        lvPraise.setPageSize(Constants.pageSize);
        praiseAdapter = new CommonAdapter<ArtWorkPraiseList>(this, praiseDatas, R.layout.praise_list_item) {
            @Override
            public void convert(final ViewHolder helper, final ArtWorkPraiseList item) {
                AppApplication.displayImage(item.getUser().getPictureUrl(), (ImageView) helper.getView(R.id.civ_head));
                helper.setText(R.id.tv_name, item.getUser().getName());
                if (selected.get(helper.getPosition())){
                    ((ImageView) helper.getView(R.id.iv_attention)).setImageResource(R.mipmap.guanzhuhou);
                }else {
                    ((ImageView) helper.getView(R.id.iv_attention)).setImageResource(R.mipmap.guanzhuqian);
                }
                helper.getView(R.id.iv_attention).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        helper.getView(R.id.iv_attention).setEnabled(false);
                        String followType="0";
                        if ("1".equals(item.getUser().getType())){
                            followType ="1";
                        }else if("2".equals(item.getUser().getType())){
                            followType ="2";
                        }
                        if (selected.get(helper.getPosition())){
                            noAttention_user(helper.getView(R.id.iv_attention),helper.getView(R.id.iv_attention),item.getUser().getId(),helper,followType);
                        }else {
                            attention_user(helper.getView(R.id.iv_attention),helper.getView(R.id.iv_attention),item.getUser().getId(),helper,followType);
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
    private void noAttention_user(final View v, final View view, final String followId, final ViewHolder helper, final String followType) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("followId", followId);
        paramsMap.put("identifier", "1");
        paramsMap.put("followType", followType);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "changeFollowStatus.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    ToastUtil.showShort(PraiseListActivity.this,"取消关注");
                    ((ImageView) view).setImageResource(R.mipmap.guanzhuqian);
                    v.setEnabled(true);
                    selected.put(helper.getPosition(),false);
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                noAttention_user(v, view, followId, helper, followType);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }
    private void attention_user(final View v, final View view, final String followId, final ViewHolder helper, final String followType) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("followId", followId);
        paramsMap.put("identifier", "0");
        paramsMap.put("followType", followType);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "changeFollowStatus.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))){
                    ToastUtil.showShort(PraiseListActivity.this,"关注成功");
                    ((ImageView) view).setImageResource(R.mipmap.guanzhuhou);
                    v.setEnabled(true);
                    selected.put(helper.getPosition(),true);
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                attention_user(v, view, followId, helper, followType);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }

            }
        });
    }
    private void loadData(final int state, final int pageNum) {
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
                ToastUtil.showLong(PraiseListActivity.this, "网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("".equals(AppApplication.gUser.getId())) {
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
                                        ToastUtil.showLong(PraiseListActivity.this, "网络连接超时,稍后重试!");
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 1;
        loadData(AutoListView.REFRESH, currentPage);
    }

    @Override
    public void onLoad() {
        currentPage++;
        loadData(AutoListView.LOAD, currentPage);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        loadData(AutoListView.REFRESH, currentPage);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position <= praiseDatas.size()) {
            if ("1".equals(praiseDatas.get(position-1).getUser().getType())){
                Intent intent = new Intent(this, ArtistIndexActivity.class);
                intent.putExtra("userId", praiseDatas.get(position - 1).getUser().getId());
                intent.putExtra("name", praiseDatas.get(position - 1).getUser().getName());
                startActivity(intent);
            }else {
                Intent intent = new Intent(this, UserIndexActivity.class);
                intent.putExtra("userId", praiseDatas.get(position - 1).getUser().getId());
                intent.putExtra("name", praiseDatas.get(position - 1).getUser().getName());
                startActivity(intent);
            }
        }
    }
}