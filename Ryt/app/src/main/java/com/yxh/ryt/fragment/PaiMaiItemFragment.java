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
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.RongZi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class PaiMaiItemFragment extends BaseFragment implements AutoListView.OnRefreshListener,
		AutoListView.OnLoadListener {
	private AutoListView lstv;
	private CommonAdapter<RongZi> paiMaiCommonAdapter;
	private List<RongZi> paiMaiDatas;
	private int currentPage=1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		paiMaiDatas=new ArrayList<RongZi>();
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
		NetRequestUtil.post(Constants.BASE_PATH + "artWorkAuctionList.do", paramsMap, new RongZiListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if (state==AutoListView.REFRESH){
					lstv.onRefreshComplete();
					paiMaiDatas.clear();
					List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<RongZi>>() {
					}.getType());
					if(null==objectList||objectList.size()==0){
						lstv.setResultSize(0);
					}
					if (null!=objectList&&objectList.size()>0){
						lstv.setResultSize(objectList.size());
						paiMaiDatas.addAll(objectList);
						paiMaiCommonAdapter.notifyDataSetChanged();
					}
					return;
				}
				if (state==AutoListView.LOAD){
					lstv.onLoadComplete();
					List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<RongZi>>() {
					}.getType());
					if(null==objectList||objectList.size()==0){
						lstv.setResultSize(1);
					}
					if (null!=objectList&&objectList.size()>0) {
						lstv.setResultSize(objectList.size());
						paiMaiDatas.addAll(objectList);
						paiMaiCommonAdapter.notifyDataSetChanged();
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
		final java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
		paiMaiCommonAdapter=new CommonAdapter<RongZi>(AppApplication.getSingleContext(),paiMaiDatas,R.layout.auction_list_item) {
			@Override
			public void convert(ViewHolder helper, RongZi item) {
				helper.setText(R.id.cl_01_tv_title,item.getTitle());
				helper.setText(R.id.cl_01_tv_brief,item.getBrief());
				helper.setText(R.id.cl_01_tv_name, item.getAuthor().getName());
				helper.setImageByUrl(R.id.cl_01_tv_prc, item.getPicture_url());
				helper.setImageByUrl(R.id.cl_01_civ_headPortrait,item.getAuthor().getPictureUrl());
				System.out.println(item.getStep()+"---------------------------");
				if(Integer.valueOf(item.getStep())==30){
					helper.getView(R.id.cli_tv_time).setVisibility(View.VISIBLE);
					helper.getView(R.id.cli_tv_chengjiao_price).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_size).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_state).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_current_price).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_user).setVisibility(View.GONE);
					helper.setText(R.id.cli_tv_time, "拍卖时间："+ Utils.timeToFormatTemp("MM月DD日 HH:mm",item.getAuctionStartDatetime())+"--"+ Utils.timeToFormatTemp("HH:mm",item.getAuctionEndDatetime()));
				}
				if(Integer.valueOf(item.getStep())==31){
					helper.getView(R.id.cli_tv_time).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_chengjiao_price).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_size).setVisibility(View.VISIBLE);
					helper.getView(R.id.cli_tv_state).setVisibility(View.VISIBLE);
					helper.getView(R.id.cli_tv_current_price).setVisibility(View.VISIBLE);
					helper.getView(R.id.cli_tv_user).setVisibility(View.GONE);
					helper.setText(R.id.cli_tv_size, "拍卖次数：" + item.getAuctionNum() + "次");
					helper.setText(R.id.cli_tv_state,"正在拍卖中...");
//					helper.setText(R.id.cli_tv_current_price,item.getNewBidingPrice()!=null?"当前价格："+df.format(item.getNewBidingPrice().doubleValue())+"元":"当前价格：0.00元");
				}
				if(Integer.valueOf(item.getStep())==32){
					helper.getView(R.id.cli_tv_time).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_size).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_state).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_current_price).setVisibility(View.GONE);
					helper.getView(R.id.cli_tv_chengjiao_price).setVisibility(View.VISIBLE);
					helper.getView(R.id.cli_tv_user).setVisibility(View.VISIBLE);
//					helper.setText(R.id.cli_tv_chengjiao_price,item.getNewBidingPrice()!=null?"成交价："+df.format(item.getNewBidingPrice().doubleValue())+"元":"成交价：0.00元");
					helper.setText(R.id.cli_tv_user,"");
				}
			}
		};
		lstv.setAdapter(paiMaiCommonAdapter);
		lstv.setOnRefreshListener(this);
		lstv.setOnLoadListener(this);
		return contextView;
	}

	@Override
	protected void lazyLoad() {
			if(paiMaiDatas!=null&&paiMaiDatas.size()>0)return;
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