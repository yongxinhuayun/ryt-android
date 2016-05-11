package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
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
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.MasterWork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

@SuppressLint("ValidFragment")
public class YSJWorkFragment extends StickHeaderBaseFragment{
	private ListView lstv;
	private CommonAdapter<MasterWork> ySJWorkCommonAdapter;
	private List<MasterWork> ySJWorkDatas;
	private int currentPage=1;
	private View footer;
	private TextView loadFull;
	private TextView noData;
	private TextView more;
	private ProgressBar loading;
	private int lastItem;
	private boolean loadComplete=true;
	static StickHeaderViewPagerManager stickHeaderViewPagerManager;
	private View header;

	public YSJWorkFragment(StickHeaderViewPagerManager manager, int position) {
		super(manager, position);
	}
	public YSJWorkFragment(){
		super();
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
		ySJWorkDatas=new ArrayList<MasterWork>();
	}
	@Override
	public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_listview_ysj_work,container, false);
		lstv = (ListView)view.findViewById(R.id.fiyw_lstv);
		TextView tvNoData = (TextView) view.findViewById(R.id.fiyw_tv_noData);
		footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null);
		header = LayoutInflater.from(getActivity()).inflate(R.layout.workheaderview, lstv,false);
		placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);
		setAdapter();
		onScroll();
		ySJWorkDatas.clear();
		LoadData(true, currentPage);
		return view;
	}
	private String judgeStaus(String type){
		if (type.equals("0")){
			return "非卖品";
		}else if(type.equals("1")){
			return "可售";
		}else {
			return "已售";
		}
	}
	private void setAdapter() {
		ySJWorkCommonAdapter=new CommonAdapter<MasterWork>(AppApplication.getSingleContext(),ySJWorkDatas,R.layout.mrdm_work_item) {
			@Override
			public void convert(ViewHolder helper, MasterWork item) {
				helper.setImageByUrl(R.id.mwi_iv_icon, item.getPictureUrl());
				helper.setText(R.id.mwi_iv_title, item.getName());
				helper.setText(R.id.mwi_tv_description,item.getMaterial()+"/"+ Utils.timeToFormatTemp("yyyy",item.getCreateDatetime())+"/"+judgeStaus(item.getType()));
				helper.getView(R.id.mwi_iv_delete);
			}
		};
		lstv.setAdapter(ySJWorkCommonAdapter);
		lstv.addFooterView(footer);
		lstv.addHeaderView(header);
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
		paramsMap.put("userId","ieatht97wfw30hfd");
		paramsMap.put("pageSize", Constants.pageSize+"");
		paramsMap.put("pageIndex", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "userWork.do", paramsMap, new RZCommentCallBack() {
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
						List<MasterWork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("masterWorkList")), new TypeToken<List<MasterWork>>() {
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
						List<MasterWork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("masterWorkList")), new TypeToken<List<MasterWork>>() {
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