package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.AuctionSummaryActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.RongZi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class ArtistCompletedFragment extends BaseFragment implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener, AdapterView.OnItemClickListener {

    private String artWorkId;
    private AutoListView lstV;
    private CommonAdapter<RongZi> rongZiCommonAdapter;
    private List<RongZi> rongZiDatas;
    private int currentPage = 1;

    public ArtistCompletedFragment(String artWorkId) {
        super();
        this.artWorkId = artWorkId;
    }

    public ArtistCompletedFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rongZiDatas = new ArrayList<RongZi>();

    }
    private void loadData(final int state, final int pageNum) {
        final Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type","4");
        paramsMap.put("pageSize", Constants.pageSize + "");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "getArtWorkListByAuthor.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(getActivity(), "网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(final Map<String, Object> response) {
                if ("".equals(AppApplication.gUser.getId())) {
                    if (state == AutoListView.REFRESH) {
                        lstV.onRefreshComplete();
                        rongZiDatas.clear();
                        List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson()
                                .toJson( response.get("artworkList")), new TypeToken<List<RongZi>>() {
                        }.getType());
                        if (null == objectList || objectList.size() == 0) {
                            lstV.setResultSize(0);
                        }
                        if (null != objectList && objectList.size() > 0) {
                            lstV.setResultSize(objectList.size());
                            rongZiDatas.addAll(objectList);
                            rongZiCommonAdapter.notifyDataSetChanged();
                        }
                    }
                    if (state == AutoListView.LOAD) {
                        lstV.onLoadComplete();
                        List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson()
                                .toJson( response.get("artworkList")), new TypeToken<List<RongZi>>() {
                        }.getType());
                        if (null == objectList || objectList.size() == 0) {
                            lstV.setResultSize(1);
                        }
                        if (null != objectList && objectList.size() > 0) {
                            lstV.setResultSize(objectList.size());
                            rongZiDatas.addAll(objectList);
                            rongZiCommonAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    SessionLogin sessionLogin = new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)) {
                                NetRequestUtil.post(Constants.BASE_PATH + "getArtWorkListByAuthor.do", paramsMap, new RongZiListCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e) {
                                        ToastUtil.showLong(getActivity(), "网络连接超时,稍后重试!");
                                    }

                                    @Override
                                    public void onResponse(Map<String, Object> response) {
                                        if (state == AutoListView.REFRESH) {
                                            lstV.onRefreshComplete();
                                            rongZiDatas.clear();
                                            List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson( response.get("artworkList")), new TypeToken<List<RongZi>>() {
                                            }.getType());
                                            if (null == objectList || objectList.size() == 0) {
                                                lstV.setResultSize(0);
                                            }
                                            if (null != objectList && objectList.size() > 0) {
                                                lstV.setResultSize(objectList.size());
                                                rongZiDatas.addAll(objectList);
                                                rongZiCommonAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        if (state == AutoListView.LOAD) {
                                            lstV.onLoadComplete();
                                            List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson( response.get("artworkList")), new TypeToken<List<RongZi>>() {
                                            }.getType());
                                            if (null == objectList || objectList.size() == 0) {
                                                lstV.setResultSize(1);
                                            }
                                            if (null != objectList && objectList.size() > 0) {
                                                lstV.setResultSize(objectList.size());
                                                rongZiDatas.addAll(objectList);
                                                rongZiCommonAdapter.notifyDataSetChanged();
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
    //计算加价幅度
    private int getAuctionPrice(long price) {
        //计算加价幅度
        if (price <= 499) {
            return 10;
        } else if (price >= 500 && price <= 999) {
            return 50;
        } else if (price >= 1000 && price <= 4999) {
            return 100;
        } else if (price >= 5000 && price <= 9999) {
            return 200;
        } else if (price >= 10000 && price <= 29999) {
            return 500;
        } else if (price >= 30000 && price <= 99999) {
            return 1000;
        } else if (price >= 100000) {
            return 2000;
        }
        return 0;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        lstV = ((AutoListView) view.findViewById(R.id.lstv));
        lstV.setPageSize(Constants.pageSize);
        rongZiCommonAdapter = new CommonAdapter<RongZi>(AppApplication.getSingleContext(), rongZiDatas, R.layout.fragment_artistcompleted) {

            @Override
            public void convert(final ViewHolder helper, final RongZi item) {
                if (item != null) {
                    helper.setImageByUrl(R.id.fac_si_picture,item.getPicture_url());
                    helper.setText(R.id.fac_tv_title,item.getTitle());
                    if (item.getBrief()!=null){
                        helper.setText(R.id.fac_tv_brief,item.getBrief());
                    }else {
                        helper.setText(R.id.fac_tv_brief,"暂无简介！");
                    }
                }
            }
        };
        lstV.setAdapter(rongZiCommonAdapter);
        lstV.setOnRefreshListener(this);
        lstV.setOnLoadListener(this);
        lstV.setOnItemClickListener(this);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void lazyLoad() {
        if (rongZiDatas != null && rongZiDatas.size() > 0) return;
        loadData(AutoListView.REFRESH, currentPage);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        loadData(AutoListView.REFRESH, currentPage);
    }

    @Override
    public void onLoad() {
        currentPage++;
        loadData(AutoListView.LOAD, currentPage);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position <= rongZiDatas.size()) {
            Intent intent=new Intent(getActivity(), AuctionSummaryActivity.class);
            intent.putExtra("id", rongZiDatas.get(position-1).getId());
            intent.putExtra("title", rongZiDatas.get(position-1).getTitle());
            intent.putExtra("name", rongZiDatas.get(position-1).getAuthor().getName());
            intent.putExtra("userId", rongZiDatas.get(position-1).getAuthor().getId());
            intent.putExtra("step", rongZiDatas.get(position-1).getStep());
            startActivity(intent);
        }
    }
}