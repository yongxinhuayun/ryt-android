package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.yxh.ryt.activity.CreateSuccessActivity;
import com.yxh.ryt.activity.CreateSummaryActivity;
import com.yxh.ryt.activity.EditProject01Activity;
import com.yxh.ryt.activity.FinanceSummaryActivity;
import com.yxh.ryt.activity.PublicDongtaiImageActivity;
import com.yxh.ryt.activity.RecordVedioActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.ActionSheetDialog;
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
public class ArtistCreateFragment extends BaseFragment implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener, AdapterView.OnItemClickListener {

    private String artWorkId;
    private AutoListView lstV;
    private CommonAdapter<RongZi> rongZiCommonAdapter;
    private List<RongZi> rongZiDatas;
    private int currentPage = 1;

    public ArtistCreateFragment(String artWorkId) {
        super();
        this.artWorkId = artWorkId;
    }

    public ArtistCreateFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rongZiDatas = new ArrayList<RongZi>();

    }
    private void loadData(final int state, final int pageNum) {
        final Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type","2");
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        lstV = ((AutoListView) view.findViewById(R.id.lstv));
        lstV.setPageSize(Constants.pageSize);
        rongZiCommonAdapter = new CommonAdapter<RongZi>(AppApplication.getSingleContext(), rongZiDatas, R.layout.fragment_artistcreate) {

            @Override
            public void convert(final ViewHolder helper, final RongZi item) {
                if (item != null) {
                    helper.setImageByUrl(R.id.fac_si_picture,item.getPicture_url());
                    helper.setText(R.id.fac_tv_title,item.getTitle());
                    helper.setText(R.id.fac_tv_investMoney,"￥"+item.getInvestGoalMoney()+"");
                    helper.setText(R.id.fac_tv_goalMoney,"￥"+item.getInvestGoalMoney()+"");
                    if (item.getBrief()!=null){
                        helper.setText(R.id.fac_tv_brief,item.getBrief());
                    }else {
                        helper.setText(R.id.fac_tv_brief,"暂无简介！");
                    }
                    if ("21".equals(item.getStep())){
                        helper.setText(R.id.fac_tv_state,"创作中");
                        helper.getView(R.id.fac_rl_button).setVisibility(View.VISIBLE);
                        ((TextView) helper.getView(R.id.fac_tv_commitCheck)).setText("创作完成");
                        ((TextView) helper.getView(R.id.fac_tv_commitCheck)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getActivity(), CreateSuccessActivity.class);
                                intent.putExtra("artworkId",item.getId());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
                            }
                        });
                        ((TextView) helper.getView(R.id.fac_tv_edit)).setText("发布动态");
                        helper.getView(R.id.fac_tv_edit).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dynamic(item.getId());
                            }
                        });
                    }else {
                        helper.setText(R.id.fac_tv_state,"创作完成");
                        helper.getView(R.id.fac_rl_button).setVisibility(View.GONE);
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
    private void dynamic(final String id) {
        new ActionSheetDialog(getActivity())
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("发布图片", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent=new Intent(getActivity(), PublicDongtaiImageActivity.class);
                                intent.putExtra("artWorkId",id);
                                getActivity().startActivity(intent);
                            }
                        })
                .addSheetItem("发布视频", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent=new Intent(getActivity(), RecordVedioActivity.class);
                                intent.putExtra("artWorkId",id);
                                getActivity().startActivity(intent);
                            }
                        })
                .show();
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
            Intent intent = new Intent(getActivity(), CreateSummaryActivity.class);
            intent.putExtra("id", rongZiDatas.get(position - 1).getId());
            intent.putExtra("name", rongZiDatas.get(position - 1).getTitle());
            intent.putExtra("userId",rongZiDatas.get(position-1).getAuthor().getId());
            startActivity(intent);
        }
    }
}