package com.yxh.ryt.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.Artwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;


public class YSJWorkFragment extends StickHeaderBaseFragment{
	private ListView lstv;
	private CommonAdapter<Artwork> ySJWorkCommonAdapter;
	private List<Artwork> ySJWorkDatas;
	private int currentPage=1;
	private View footer;
	private TextView loadFull;
	private TextView noData;
	private TextView more;
	private ProgressBar loading;
	private int lastItem;
	private boolean loadComplete=true;
	static StickHeaderViewPagerManager stickHeaderViewPagerManager;
	public YSJWorkFragment(StickHeaderViewPagerManager manager, int position) {
		super(manager, position);
	}

	public YSJWorkFragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
		super(manager, position, isCanPulltoRefresh);
	}
	public static YSJWorkFragment newInstance(StickHeaderViewPagerManager manager, int position) {
		YSJWorkFragment listFragment = new YSJWorkFragment(manager, position);
		return listFragment;
	}

	public static YSJWorkFragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
		YSJWorkFragment listFragment = new YSJWorkFragment(manager, position, isCanPulltoRefresh);
		stickHeaderViewPagerManager=manager;
		return listFragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ySJWorkDatas=new ArrayList<Artwork>();
	}
	@Override
	public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_listview_ysj_work,container, false);
		lstv = (ListView)view.findViewById(R.id.fiyw_lstv);
		TextView tvNoData = (TextView) view.findViewById(R.id.fiyw_tv_noData);
		footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null);
		placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.fiyw_placehoder);
		setAdapter();
		onScroll();
		Log.d("oncreateView", "oncreateViewoncreateViewoncreateViewoncreateViewoncreateViewoncreateViewoncreateViewoncreateViewoncreateView");
		ySJWorkDatas.clear();
		LoadData(true, currentPage);
		return view;
	}
	private void setAdapter() {
		ySJWorkCommonAdapter=new CommonAdapter<Artwork>(AppApplication.getSingleContext(),ySJWorkDatas,R.layout.mrdm_work_item) {
			@Override
			public void convert(ViewHolder helper, Artwork item) {
				/*helper.setText(R.id.cl_01_tv_title,item.getTitle());
				helper.setText(R.id.cl_01_tv_brief,item.getBrief());
				helper.setText(R.id.cl_01_tv_name,item.getAuthor().getName());
//				helper.setText(R.id.fli_ll_tv_investGoalMoney,item.getInvestGoalMoney().intValue()+"元");
				helper.setText(R.id.fli_ll_tv_remainingTime, Utils.timeToFormatTemp("HH时MM分SS秒",item.getInvestEndDatetime()-item.getInvestStartDatetime()));
//				helper.setText(R.id.fli_ll_tv_investGoalPeople, item.getInvestorsNum() + "");
				helper.setImageByUrl(R.id.cl_01_tv_prc, item.getPicture_url());
				helper.setImageByUrl(R.id.cl_01_civ_headPortrait,item.getAuthor().getPictureUrl());
				if (null!=item.getAuthor().getMaster()&&!"".equals(item.getAuthor().getMaster().getTitle())){
					helper.getView(R.id.cl_01_ll_zhicheng).setVisibility(View.VISIBLE);
					helper.setText(R.id.cl_01_tv_zhicheng, item.getAuthor().getMaster().getTitle());
				}else{
					helper.getView(R.id.cl_01_ll_zhicheng).setVisibility(View.GONE);
				}*/
//				double value = item.getInvestsMoney().doubleValue() / item.getInvestGoalMoney().doubleValue();
//				helper.setProgress(R.id.progressBar1, (int)(value*100));
//				helper.setText(R.id.tv_pb_value, (int)(value*100)+"%");
			}
		};
		lstv.setAdapter(ySJWorkCommonAdapter);
		lstv.addFooterView(footer);
		loadFull = (TextView) footer.findViewById(R.id.loadFull);
		noData = (TextView) footer.findViewById(R.id.noData);
		more = (TextView) footer.findViewById(R.id.more);
		loading = (ProgressBar) footer.findViewById(R.id.loading);
		more.setVisibility(View.VISIBLE);
		loading.setVisibility(View.GONE);
		loadFull.setVisibility(View.GONE);
		noData.setVisibility(View.GONE);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	private void onScroll() {
		stickHeaderViewPagerManager.setOnListViewScrollListener(new StickHeaderViewPagerManager.OnListViewScrollListener() {
			@Override
			public void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				lastItem = firstVisibleItem + visibleItemCount - 2;
			}

			@Override
			public void onListViewScrollStateChanged(AbsListView view, int scrollState) {
				if (lastItem == ySJWorkCommonAdapter.getCount() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && loadComplete) {
					more.setVisibility(View.GONE);
					loading.setVisibility(View.VISIBLE);
					loadFull.setVisibility(View.GONE);
					noData.setVisibility(View.GONE);
					currentPage = currentPage + 1;
					LoadData(false, currentPage);
				}
			}
		});
	}
	@Override
	protected void lazyLoad() {

	}
	private void LoadData(final boolean flag,int pageNum) {
		more.setVisibility(View.GONE);
		loading.setVisibility(View.VISIBLE);
		loadFull.setVisibility(View.GONE);
		noData.setVisibility(View.GONE);
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("artWorkId","qydeyugqqiugd2");
		paramsMap.put("pageSize", Constants.pageSize+"");
		paramsMap.put("pageIndex", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkComment.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("444444失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				System.out.println(response+"dudududuuuuuuuuuuuuuuuuuuuuu");
				if ("0".equals(response.get("resultCode"))) {
					Map<String, Object> object = (Map<String, Object>) response.get("object");
					if (flag) {
						List<Artwork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkCommentList")), new TypeToken<List<Artwork>>() {
						}.getType());
						if (commentList == null) {
							more.setVisibility(View.GONE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.GONE);
							noData.setVisibility(View.VISIBLE);
						} else if (commentList.size() < Constants.pageSize){
							more.setVisibility(View.GONE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.VISIBLE);
							noData.setVisibility(View.GONE);
							ySJWorkDatas.addAll(commentList);
							commentList.clear();
							ySJWorkCommonAdapter.notifyDataSetChanged();
						}else {
							more.setVisibility(View.VISIBLE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.GONE);
							noData.setVisibility(View.GONE);
						}
						if (commentList != null) {
							ySJWorkDatas.addAll(commentList);
							commentList.clear();
						}
						ySJWorkCommonAdapter.notifyDataSetChanged();
					}else {
						List<Artwork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkCommentList")), new TypeToken<List<Artwork>>() {
						}.getType());
						if (commentList == null || commentList.size() < Constants.pageSize) {
							more.setVisibility(View.GONE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.VISIBLE);
							noData.setVisibility(View.GONE);
						} else {
							more.setVisibility(View.VISIBLE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.GONE);
							noData.setVisibility(View.GONE);
						}
						if (commentList != null) {
							ySJWorkDatas.addAll(commentList);
							commentList.clear();
						}
						ySJWorkCommonAdapter.notifyDataSetChanged();
					}
				}
			}
		});
	}
}