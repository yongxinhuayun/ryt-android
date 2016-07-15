package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.activity.UserYsjIndexActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.FollowUserUtil;
import com.yxh.ryt.vo.HomeYSJArtWork;

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

    public ArtistHomeFragment( String userId, String flag) {
        super();
        this.userId=userId;
        this.flag=flag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attentionDatas=new ArrayList<HomeYSJArtWork>();
    }

    private void LoadData(final int state,int pageNum) {
        Map<String,String> paramsMap=new HashMap<>();
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
                if ("0".equals(response.get("resultCode"))){
                    Constants.ATTENTION_TITLE[0]="艺术家("+AppApplication.getSingleGson().toJson(response.get("followsNum"))+")";
                    Intent intent = new Intent("android.intent.action.MY_BROADCAST");
                    AppApplication.getSingleContext().sendBroadcast(intent);
                    if (state == AutoListView.REFRESH) {
                        lstv.onRefreshComplete();
                        attentionDatas.clear();
                        List<HomeYSJArtWork> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("pageInfoList")), new TypeToken<List<HomeYSJArtWork>>() {
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
                        List<HomeYSJArtWork> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("pageInfoList")), new TypeToken<List<HomeYSJArtWork>>() {
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
                }else if ("000000".equals(response.get("resultCode"))){
                    SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)){
                                currentPage=1;
                                LoadData(AutoListView.REFRESH, currentPage);
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
        header = LayoutInflater.from(getActivity()).inflate(R.layout.touguo_header, null);
        attentionCommonAdapter=new CommonAdapter<HomeYSJArtWork>(AppApplication.getSingleContext(),attentionDatas,R.layout.fragment_attention_item) {
            @Override
            public void convert(final ViewHolder helper, final HomeYSJArtWork item) {


            }
        };
        lstv.setAdapter(attentionCommonAdapter);
        lstv.addHeaderView(header);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        return contextView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
		/*AttentionActivity activity = (AttentionActivity) getActivity();
		activity.pager.setCurrentItem(2);*/
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
