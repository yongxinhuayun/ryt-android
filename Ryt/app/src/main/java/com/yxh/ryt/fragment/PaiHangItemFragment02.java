package com.yxh.ryt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.Artist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class PaiHangItemFragment02 extends BaseFragment implements AutoListView.OnRefreshListener,
		AutoListView.OnLoadListener {
	private AutoListView lstv;
	private CommonAdapter<Artist> artistCommonAdapter;
	private List<Artist> artistDatas;
	private int currentPage=1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		artistDatas=new ArrayList<Artist>();
	}
	private void LoadData(final int state,int pageNum) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("pageSize",Constants.pageSize+"");
		paramsMap.put("pageNum", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "getArtistTopList.do", paramsMap, new RongZiListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if (state==AutoListView.REFRESH){
					lstv.onRefreshComplete();
					artistDatas.clear();
					List<Artist> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("ArtistTopList")), new TypeToken<List<Artist>>() {
					}.getType());
					if(null==objectList||objectList.size()==0){
						lstv.setResultSize(0);
					}
					if (null!=objectList&&objectList.size()>0){
						lstv.setResultSize(objectList.size());
						artistDatas.addAll(objectList);
						artistCommonAdapter.notifyDataSetChanged();
					}
					return;
				}
				if (state==AutoListView.LOAD){
					lstv.onLoadComplete();
					List<Artist> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("ArtistTopList")), new TypeToken<List<Artist>>() {
					}.getType());
					if(null==objectList||objectList.size()==0){
						lstv.setResultSize(1);
					}
					if (null!=objectList&&objectList.size()>0) {
						lstv.setResultSize(objectList.size());
						artistDatas.addAll(objectList);
						artistCommonAdapter.notifyDataSetChanged();
					}
					return;
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
		View contextView = inflater.inflate(R.layout.paihang_yishujia, container, false);
		lstv = (AutoListView) contextView.findViewById(R.id.lstv);
		lstv.setPageSize(Constants.pageSize);
		artistCommonAdapter=new CommonAdapter<Artist>(AppApplication.getSingleContext(),artistDatas,R.layout.paihang_yishujia_lv_item) {
			@Override
			public void convert(ViewHolder helper, Artist item) {

				helper.setText(R.id.cl_01_civ_pm,(helper.getPosition()+1)+"");
				helper.setText(R.id.cl_01_civ_name, item.getTruename());
				helper.setText(R.id.cl_01_civ_price,df.format(item.getInvest_goal_money()));
				if (item.getTurnover()!=null){
					helper.setText(R.id.cl_01_civ_turnover,df.format(item.getTurnover()));
				}
				helper.setImageByUrl(R.id.cl_01_civ_headPortrait, "http://rongyitou2.efeiyi.com/headPortrait/" + item.getUsername() + ".jpg");
//				helper.setText(R.id.cl_01_civ_rois,df.format(item.getRois().doubleValue()));
//				helper.setImageByUrl(R.id.cl_01_civ_headPortrait, "http://rongyitou2.efeiyi.com/headPortrait/" + item.getUser_id() + ".jpg");
//				helper.setImageByUrl(R.id.cl_01_civ_headPortrait,item.getAuthor().getPictureUrl());
			}
		};
		lstv.setAdapter(artistCommonAdapter);
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
		if(artistDatas!=null&&artistDatas.size()>0)return;
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
		LoadData(AutoListView.LOAD,currentPage);
	}

}