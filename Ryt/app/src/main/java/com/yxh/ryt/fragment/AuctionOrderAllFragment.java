package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.AuctionOrderConfirmActivity;
import com.yxh.ryt.activity.AuctionOrderPayMoneyActivity;
import com.yxh.ryt.activity.AuctionOrderSendActivity;
import com.yxh.ryt.activity.AuctionOrderSuccessActivity;
import com.yxh.ryt.activity.AuctionSummaryActivity;
import com.yxh.ryt.activity.CommitFinalPriceActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.PermissionUtils;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.AuctionOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class AuctionOrderAllFragment extends BaseFragment implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener, AdapterView.OnItemClickListener {

    private String artWorkId;
    private AutoListView lstV;
    private CommonAdapter<AuctionOrder> rongZiCommonAdapter;
    private List<AuctionOrder> rongZiDatas;
    private int currentPage = 1;

    public AuctionOrderAllFragment(String artWorkId) {
        super();
        this.artWorkId = artWorkId;
    }

    public AuctionOrderAllFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rongZiDatas = new ArrayList<AuctionOrder>();

    }
    private void loadData(final int state, final int pageNum) {
        final Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type","1");
        paramsMap.put("pageSize", Constants.pageSize + "");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "getListOrder.do", paramsMap, new RongZiListCallBack() {
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
                        List<AuctionOrder> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson()
                                .toJson( response.get("auctionOrderList")), new TypeToken<List<AuctionOrder>>() {
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
                        List<AuctionOrder> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson()
                                .toJson( response.get("auctionOrderList")), new TypeToken<List<AuctionOrder>>() {
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
                                NetRequestUtil.post(Constants.BASE_PATH + "getListOrder.do", paramsMap, new RongZiListCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e) {
                                        ToastUtil.showLong(getActivity(), "网络连接超时,稍后重试!");
                                    }

                                    @Override
                                    public void onResponse(Map<String, Object> response) {
                                        if (state == AutoListView.REFRESH) {
                                            lstV.onRefreshComplete();
                                            rongZiDatas.clear();
                                            List<AuctionOrder> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson( response.get("auctionOrderList")), new TypeToken<List<AuctionOrder>>() {
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
                                            List<AuctionOrder> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson( response.get("auctionOrderList")), new TypeToken<List<AuctionOrder>>() {
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        lstV = ((AutoListView) view.findViewById(R.id.lstv));
        lstV.setPageSize(Constants.pageSize);
        rongZiCommonAdapter = new CommonAdapter<AuctionOrder>(AppApplication.getSingleContext(), rongZiDatas, R.layout.fragment_orderall) {

            @Override
            public void convert(final ViewHolder helper, final AuctionOrder item) {
                if (item != null) {
                    helper.setImageByUrl(R.id.foa_iv_projectPicture,item.getArtwork().getPicture_url());
                    helper.setText(R.id.foa_iv_projectTitle,item.getArtwork().getTitle()+"");
                    if (item.getArtwork().getBrief()==null || "".equals(item.getArtwork().getBrief())){
                        helper.setText(R.id.foa_iv_projectBrief,"暂无简介");
                    }else {
                        helper.setText(R.id.foa_iv_projectBrief,item.getArtwork().getBrief());
                    }
                    helper.setText(R.id.foa_tv_projectPrice,item.getAmount()+"");
                    if ("0".equals(item.getType())){
                        helper.setText(R.id.foa_iv_projectState,"待付尾款");
                        ((TextView) helper.getView(R.id.foa_iv_projectState)).setBackgroundColor(Color.rgb(219,40,40));
                        helper.setText(R.id.foa_tv_edit,"支付尾款");
                        ((TextView) helper.getView(R.id.foa_tv_edit)).setTextColor(Color.rgb(87,173,104));
                        helper.getView(R.id.foa_tv_edit).setBackgroundResource(R.drawable.edit_focus2);
                        helper.getView(R.id.foa_tv_edit).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                payRemainMoney(item.getId(),item.getFinalPayment());
                            }
                        });
                    }else if ("1".equals(item.getType())){
                        helper.setText(R.id.foa_iv_projectState,"待发货");
                        ((TextView) helper.getView(R.id.foa_iv_projectState)).setBackgroundColor(Color.rgb(251,189,8));
                        helper.setText(R.id.foa_tv_edit,"提醒发货");
                        ((TextView) helper.getView(R.id.foa_tv_edit)).setTextColor(Color.rgb(87,173,104));
                        helper.getView(R.id.foa_tv_edit).setBackgroundResource(R.drawable.edit_focus2);
                        helper.getView(R.id.foa_tv_edit).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                remindSendGoods(item.getId());
                            }
                        });
                    } else if ("2".equals(item.getType())){
                        helper.setText(R.id.foa_iv_projectState,"交易成功");
                        ((TextView) helper.getView(R.id.foa_iv_projectState)).setBackgroundColor(Color.rgb(153,153,153));
                        helper.setText(R.id.foa_tv_edit,"删除订单");
                        ((TextView) helper.getView(R.id.foa_tv_edit)).setTextColor(Color.rgb(153,153,153));
                        helper.getView(R.id.foa_tv_edit).setBackgroundResource(R.drawable.edit_focus1);
                        helper.getView(R.id.foa_tv_edit).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteOrder(item.getId(),helper.getPosition());
                            }
                        });
                    } else if ("3".equals(item.getType())){
                        helper.setText(R.id.foa_iv_projectState,"待收货");
                        ((TextView) helper.getView(R.id.foa_iv_projectState)).setBackgroundColor(Color.rgb(33,133,208));
                        helper.setText(R.id.foa_tv_edit,"确认收货");
                        ((TextView) helper.getView(R.id.foa_tv_edit)).setTextColor(Color.rgb(87,173,104));
                        helper.getView(R.id.foa_tv_edit).setBackgroundResource(R.drawable.edit_focus2);
                        helper.getView(R.id.foa_tv_edit).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmReceiveGoods(item.getId());
                            }
                        });
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

    private void payRemainMoney(String id, BigDecimal finalPayment) {
        Intent intent = new Intent(getActivity(),CommitFinalPriceActivity.class);
        intent.putExtra("artWorkId",id);
        intent.putExtra("finalPrice",finalPayment+"");
        this.startActivity(intent);
    }

    private void deleteOrder(final String id, final int position) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("auctionOrderId", id);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "deleteOrder.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    rongZiDatas.remove(position);
                    rongZiCommonAdapter.notifyDataSetChanged();
                    ToastUtil.showShort(getActivity(),"删除订单成功");
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                deleteOrder(id, position);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }

    private void confirmReceiveGoods(final String id) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("auctionOrderId", id);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "confirmReceipt.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    ToastUtil.showShort(getActivity(),"确认收货！");
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                confirmReceiveGoods(id);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
    }

    private void remindSendGoods(final String id) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("auctionOrderId", id);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "reminderDelivery.do", paramsMap, new AttentionListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    ToastUtil.showShort(getActivity(),"提醒发货成功");
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                remindSendGoods(id);
                            }
                        }
                    });
                    sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
                }
            }
        });
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
        if (position<=rongZiDatas.size()){
            if ("1".equals(rongZiDatas.get(position-1).getType())){
                Intent intent=new Intent(getActivity(), AuctionOrderSendActivity.class);
                intent.putExtra("data",rongZiDatas.get(position-1));
                startActivity(intent);
            }else if ("3".equals(rongZiDatas.get(position-1).getType())){
                Intent intent=new Intent(getActivity(), AuctionOrderConfirmActivity.class);
                intent.putExtra("data",rongZiDatas.get(position-1));
                startActivity(intent);
            }else if ("2".equals(rongZiDatas.get(position-1).getType())){
                Intent intent=new Intent(getActivity(), AuctionOrderSuccessActivity.class);
                intent.putExtra("data",rongZiDatas.get(position-1));
                startActivity(intent);
            } else if ("0".equals(rongZiDatas.get(position-1).getType())){
                Intent intent=new Intent(getActivity(), AuctionOrderPayMoneyActivity.class);
                intent.putExtra("data",rongZiDatas.get(position-1));
                startActivity(intent);
            }
        }
    }
}