package com.yxh.ryt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.ChuangZuoXQActivity;
import com.yxh.ryt.activity.CreateSummaryActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.Create;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class ChuangZuoItemFragment extends BaseFragment implements AutoListView.OnRefreshListener,
		AutoListView.OnLoadListener{
	private AutoListView lstv;
	private CommonAdapter<Create> chuangZuoCommonAdapter;
	private List<Create> chuangZuoDatas;
	private int currentPage=1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		chuangZuoDatas=new ArrayList<Create>();
	}


	private void LoadData(final int state,int pageNum) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("pageSize",Constants.pageSize+"");
		paramsMap.put("pageIndex", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "artWorkCreationList.do", paramsMap, new RongZiListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))){
					Map<String,String> object= (Map<String, String>) response.get("object");
					if (state==AutoListView.REFRESH){
						lstv.onRefreshComplete();
						chuangZuoDatas.clear();
						List<Create> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkList")), new TypeToken<List<Create>>() {
						}.getType());
						if(null==objectList||objectList.size()==0){
							lstv.setResultSize(0);
						}
						if (null!=objectList&&objectList.size()>0){
							lstv.setResultSize(objectList.size()); //还有数据加载。。。
							chuangZuoDatas.addAll(objectList);
							chuangZuoCommonAdapter.notifyDataSetChanged();
						}
						return;
					}
					if (state==AutoListView.LOAD){
						lstv.onLoadComplete();
						List<Create> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkList")), new TypeToken<List<Create>>() {
						}.getType());
						if(null==objectList||objectList.size()==0){
							lstv.setResultSize(1);   //已全部加载完毕
						}
						if (null!=objectList&&objectList.size()>0) {
							lstv.setResultSize(objectList.size());  //还有数据加载。。。
							chuangZuoDatas.addAll(objectList);
							chuangZuoCommonAdapter.notifyDataSetChanged();
						}
						return;
					}
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
		chuangZuoCommonAdapter=new CommonAdapter<Create>(AppApplication.getSingleContext(),chuangZuoDatas,R.layout.create_list_item) {
			@Override
			public void convert(ViewHolder helper, final Create item) {
				helper.setText(R.id.cl_01_tv_title,item.getTitle());
				helper.setText(R.id.cl_01_tv_brief,item.getBrief());
				helper.setText(R.id.cl_01_tv_name,item.getAuthor().getName());
				helper.setImageByUrl(R.id.cl_01_tv_prc, item.getPicture_url());
				helper.setImageByUrl(R.id.cl_01_civ_headPortrait,item.getAuthor().getPictureUrl());
				helper.getView(R.id.cli_ll_top).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(getActivity(), CreateSummaryActivity.class);
						intent.putExtra("id", item.getId());
						startActivity(intent);
					}
				});
			}
		};
		lstv.setAdapter(chuangZuoCommonAdapter);
		lstv.setOnRefreshListener(this);
		lstv.setOnLoadListener(this);
		return contextView;
	}

	@Override
	protected void lazyLoad() {
		if(chuangZuoDatas!=null&&chuangZuoDatas.size()>0)return;
		LoadData(AutoListView.REFRESH, currentPage);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onRefresh() {
		currentPage=1;
		LoadData(AutoListView.REFRESH,currentPage);
	}

	@Override
	public void onLoad() {
		currentPage++;
		LoadData(AutoListView.LOAD,currentPage);
	}
}