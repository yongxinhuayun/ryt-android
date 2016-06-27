package com.yxh.ryt.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.RongZiXQActivity;
import com.yxh.ryt.activity.UserYsjIndexActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.RongZi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class RongZiItemFragment extends BaseFragment implements AutoListView.OnRefreshListener,
		AutoListView.OnLoadListener, AdapterView.OnItemClickListener {
	private AutoListView lstv;
	private CommonAdapter<RongZi> rongZiCommonAdapter;
	private List<RongZi> rongZiDatas;
	private int currentPage=1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rongZiDatas=new ArrayList<RongZi>();
	}
	private void LoadData(final int state,int pageNum) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("pageSize", Constants.pageSize+"");
		paramsMap.put("pageNum", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "investorIndex.do", paramsMap, new RongZiListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if (state == AutoListView.REFRESH) {
					lstv.onRefreshComplete();
					rongZiDatas.clear();
					List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<RongZi>>() {
					}.getType());
					if (null == objectList || objectList.size() == 0) {
						lstv.setResultSize(0);
					}
					if (null != objectList && objectList.size() > 0) {
						lstv.setResultSize(objectList.size());
						rongZiDatas.addAll(objectList);
						rongZiCommonAdapter.notifyDataSetChanged();
					}
					return;
				}
				if (state == AutoListView.LOAD) {
					lstv.onLoadComplete();
					List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<RongZi>>() {
					}.getType());
					if (null == objectList || objectList.size() == 0) {
						lstv.setResultSize(1);
					}
					if (null != objectList && objectList.size() > 0) {
						lstv.setResultSize(objectList.size());
						rongZiDatas.addAll(objectList);
						rongZiCommonAdapter.notifyDataSetChanged();
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
		rongZiCommonAdapter=new CommonAdapter<RongZi>(AppApplication.getSingleContext(),rongZiDatas,R.layout.finance_list_item) {
			@Override
			public void convert(ViewHolder helper, final RongZi item) {
				if (item!=null){
					helper.setText(R.id.cl_01_tv_title,item.getTitle());
					helper.setText(R.id.cl_01_tv_brief,item.getBrief());
					helper.setText(R.id.cl_01_tv_name,item.getAuthor().getName());
					/*helper.setText(R.id.fli_ll_tv_investGoalMoney,item.getInvestGoalMoney().intValue()+"元");
					helper.setText(R.id.fli_ll_tv_remainingTime,item.getInvestRestTime());*/
					helper.setText(R.id.fli_ll_tv_investGoalPeople, item.getInvestNum()+"");
					helper.setText(R.id.tv_rongzi_content, "目标：" + item.getInvestGoalMoney().intValue() + "元 | " + "已投：" +"3000元 | " +item.getInvestRestTime() + "后截止");

				/*	for (int i = 0; i < lstv.getChildCount(); i++) {
						LinearLayout layout = (LinearLayout) lstv.getChildAt(i);// 获得子item的layout
						TextView tv = (TextView) layout.findViewById(R.id.tv_rongzi_content);// 从layout中获得控件,根据其id
							if (!"".equals(tv.getText())){*/
						SpannableStringBuilder builder = new SpannableStringBuilder(((TextView)helper.getView(R.id.tv_rongzi_content)).getText().toString());



						ForegroundColorSpan blackSpan1 = new ForegroundColorSpan(Color.BLACK);
						ForegroundColorSpan blackSpan2 = new ForegroundColorSpan(Color.BLACK);
						ForegroundColorSpan blackSpan3 = new ForegroundColorSpan(Color.BLACK);
						ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.GRAY);


						builder.setSpan(blackSpan1, 3, (item.getInvestGoalMoney().intValue() + "").length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						//builder.setSpan(graySpan, (item.getInvestGoalMoney().intValue() + "").length() + 4, (item.getInvestGoalMoney().intValue() + "").length() + 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						builder.setSpan(blackSpan2, (item.getInvestGoalMoney().intValue() + "").length() + 10, (item.getInvestGoalMoney().intValue() + "").length() + 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						//builder.setSpan(graySpan, (item.getInvestGoalMoney().intValue() + "").length() + 15, (item.getInvestGoalMoney().intValue() + "").length() + 18 + item.getInvestRestTime().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						builder.setSpan(blackSpan3, (item.getInvestGoalMoney().intValue() + "").length() + 18, (item.getInvestGoalMoney().intValue() + "").length() + 18 + item.getInvestRestTime().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						//builder.setSpan(graySpan, (item.getInvestGoalMoney().intValue() + "").length() + 18 + item.getInvestRestTime().length(), (item.getInvestGoalMoney().intValue() + "").length() + 13 + item.getInvestRestTime().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


					helper.setText(R.id.tv_rongzi_content,builder);
				//}
					//}
					helper.setImageByUrl(R.id.cl_01_tv_prc, item.getPicture_url());
					helper.setImageByUrl(R.id.cl_01_civ_headPortrait,item.getAuthor().getPictureUrl());
					if (null!=item.getAuthor().getMaster()&&!"".equals(item.getAuthor().getMaster().getTitle())){
						helper.getView(R.id.cl_01_ll_zhicheng).setVisibility(View.VISIBLE);
						helper.setText(R.id.cl_01_tv_zhicheng, item.getAuthor().getMaster().getTitle());
					}else{
						helper.getView(R.id.cl_01_ll_zhicheng).setVisibility(View.GONE);
					}
					double value = item.getInvestsMoney().doubleValue() / item.getInvestGoalMoney().doubleValue();
					/*helper.setProgress(R.id.progressBar1, (int)(value*100));
					helper.setText(R.id.tv_pb_value, (int) (value * 100) + "%");*/
					helper.getView(R.id.cl_01_civ_headPortrait).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getActivity(), UserYsjIndexActivity.class);
							intent.putExtra("userId", item.getAuthor().getId());
							intent.putExtra("currentId", AppApplication.gUser.getId());
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							getActivity().startActivity(intent);
						}
					});
					/*helper.getView(R.id.cl_01_tv_prc).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent=new Intent(getActivity(), RongZiXQActivity.class);
							intent.putExtra("id",item.getId());
							startActivity(intent);
						}
					});*/
				}
			}
		};
		lstv.setOnItemClickListener(this);
		lstv.setAdapter(rongZiCommonAdapter);
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
		if(rongZiDatas!=null&&rongZiDatas.size()>0)return;
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


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position<=rongZiDatas.size()){
			Intent intent=new Intent(getActivity(), RongZiXQActivity.class);
			intent.putExtra("id",rongZiDatas.get(position-1).getId());
			startActivity(intent);
		}
	}
}