package com.yxh.ryt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.ArtUserFollowed;
import com.yxh.ryt.vo.FollowUserUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class AttentionArtItemFragment extends BaseFragment implements AutoListView.OnRefreshListener,
		AutoListView.OnLoadListener {
	private AutoListView lstv;
	private CommonAdapter<FollowUserUtil> attentionCommonAdapter;
	private List<FollowUserUtil> attentionDatas;
	private int currentPage=1;
	private String flag="1";
	private AnimationSet animationSet;
	private ScaleAnimation scaleAnimation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		attentionDatas=new ArrayList<FollowUserUtil>();
	}
	private void LoadData(final int state,int pageNum) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("userId","ieatht97wfw30hfd");
		paramsMap.put("type", "1");
		paramsMap.put("pageSize", Constants.pageSize + "");
		paramsMap.put("pageIndex", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
			paramsMap.put("flag",flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "userFollowed.do", paramsMap, new AttentionListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				Log.d("AttentionUserItemFragment1111111111", AppApplication.getSingleGson().toJson(response.get("followsNum")));
				Constants.ATTENTION_TITLE[0]="艺术家("+AppApplication.getSingleGson().toJson(response.get("followsNum"))+")";
				Intent intent = new Intent("android.intent.action.MY_BROADCAST");
				AppApplication.getSingleContext().sendBroadcast(intent);
				if (state == AutoListView.REFRESH) {
					lstv.onRefreshComplete();
					attentionDatas.clear();
					List<FollowUserUtil> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("pageInfoList")), new TypeToken<List<FollowUserUtil>>() {
					}.getType());
					if (null == objectList || objectList.size() == 0) {
						lstv.setResultSize(0);
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
					List<FollowUserUtil> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("pageInfoList")), new TypeToken<List<FollowUserUtil>>() {
					}.getType());
					if (null == objectList || objectList.size() == 0) {
						lstv.setResultSize(1);
					}
					if (null != objectList && objectList.size() > 0) {
						lstv.setResultSize(objectList.size());
						attentionDatas.addAll(objectList);
						attentionCommonAdapter.notifyDataSetChanged();
					}
					return;
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
		attentionCommonAdapter=new CommonAdapter<FollowUserUtil>(AppApplication.getSingleContext(),attentionDatas,R.layout.fragment_attention_item) {
			@Override
			public void convert(ViewHolder helper, FollowUserUtil item) {
				helper.setText(R.id.fai_tv_name,item.getArtUserFollowed().getFollower().getName());
				helper.setText(R.id.fai_tv_brief, item.getMaster().getTitle());
				helper.setImageByUrl(R.id.fai_iv_icon, item.getMaster().getFavicon());

			}
		};
		lstv.setAdapter(attentionCommonAdapter);
		lstv.setOnRefreshListener(this);
		lstv.setOnLoadListener(this);
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
		currentPage=1;
		LoadData(AutoListView.REFRESH,currentPage);
	}

	@Override
	public void onLoad() {
		currentPage++;
		LoadData(AutoListView.LOAD, currentPage);
	}

}