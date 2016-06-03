package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.viewpagerindicator.IcsLinearLayout;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.ChatMsgEntity;
import com.yxh.ryt.vo.HomeYSJArtWork;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

@SuppressLint("ValidFragment")
public class YSJHomeFragment extends StickHeaderBaseFragment{
	private ListView lstv;
	private CommonAdapter<HomeYSJArtWork> ySJHomeCommonAdapter;
	private List<HomeYSJArtWork> ySJHomeDatas;
	private int currentPage=1;
	private View footer;
	private TextView loadFull;
	private TextView noData;
	private TextView more;
	private ProgressBar loading;
	private int lastItem;
	private boolean loadComplete=true;
	static StickHeaderViewPagerManager stickHeaderViewPagerManager;
	private static String userId,currentId;
	private HomeReceiver receiver;

	public YSJHomeFragment(StickHeaderViewPagerManager manager, int position) {
		super(manager, position);
	}
	public YSJHomeFragment(){
		super();
	}
	public YSJHomeFragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
		super(manager, position, isCanPulltoRefresh);
	}
	public static YSJHomeFragment newInstance(StickHeaderViewPagerManager manager, int position) {
		YSJHomeFragment listFragment = new YSJHomeFragment(manager, position);
		return listFragment;
	}

	public static YSJHomeFragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh, String userID, String currentID) {
		YSJHomeFragment listFragment = new YSJHomeFragment(manager, position, isCanPulltoRefresh);
		stickHeaderViewPagerManager=manager;
		userId=userID;
		currentId=currentID;
		return listFragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ySJHomeDatas=new ArrayList<HomeYSJArtWork>();
	}
	@Override
	public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_listview_ysj_home,container, false);
		lstv = (ListView)view.findViewById(R.id.fiyh_lstv);
		TextView tvNoData = (TextView) view.findViewById(R.id.fiyh_tv_noData);
		footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null);
		placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);
		setAdapter();
		onScroll();
		receiver = new HomeReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.HOME_BROADCAST");
		AppApplication.getSingleContext().registerReceiver(receiver, filter);
		return view;
	}
	public class HomeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("dududududuudududuudud",currentPage+"");
			currentPage=currentPage+1;
			LoadData(false, currentPage);
		}
	}
	private void setAdapter() {
		ySJHomeCommonAdapter=new CommonAdapter<HomeYSJArtWork>(AppApplication.getSingleContext(),ySJHomeDatas,R.layout.mrdm_project_item) {
			@Override
			public void convert(ViewHolder helper, final HomeYSJArtWork item) {
				helper.setImageByUrl(R.id.mpi_iv_icon, item.getPicture_url());
				helper.setText(R.id.mpi_tv_tilte, item.getTitle());
				helper.setText(R.id.mpi_iv_project,"项目描述:"+item.getDescription());
				helper.setText(R.id.mpi_tv_money,item.getInvestGoalMoney()+"元");
				helper.setText(R.id.mpi_tv_stage,AppApplication.map.get(item.getStep()));
				helper.getView(R.id.mpi_tv_left).setVisibility(View.GONE);
				helper.getView(R.id.mpi_tv_right).setVisibility(View.GONE);
				if (userId.equals(currentId)){
					if ("100".equals(item.getStep())){
						helper.getView(R.id.mpi_tv_left).setVisibility(View.VISIBLE);
						((TextView) helper.getView(R.id.mpi_tv_left)).setText("提交项目");
						((TextView) helper.getView(R.id.mpi_tv_left)).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								submitArtwork(item.getId(),userId,"10");
							}
						});
						helper.getView(R.id.mpi_tv_right).setVisibility(View.VISIBLE);
						((TextView) helper.getView(R.id.mpi_tv_right)).setText("编辑项目");
					}else if ("21".equals(item.getStep()) || "22".equals(item.getStep())){
						helper.getView(R.id.mpi_tv_left).setVisibility(View.VISIBLE);
						((TextView) helper.getView(R.id.mpi_tv_left)).setText("创作完成");
						helper.getView(R.id.mpi_tv_right).setVisibility(View.VISIBLE);
						((TextView) helper.getView(R.id.mpi_tv_right)).setText("发布动态");
					}
				}

			}
		};
		lstv.setAdapter(ySJHomeCommonAdapter);
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

	private void submitArtwork(String id, String userId, String s) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("artworkId", id);
		paramsMap.put("userId", userId);
		paramsMap.put("step", s);
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "updateArtWork.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("444444失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				ySJHomeDatas.clear();
				currentPage = 1;
				LoadData(true, currentPage);
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onResume() {
		super.onResume();
		ySJHomeDatas.clear();
		currentPage=1;
		LoadData(true, currentPage);
	}

	private void onScroll() {
		/*stickHeaderViewPagerManager.setOnListViewScrollListener(new StickHeaderViewPagerManager.OnListViewScrollListener() {
			@Override
			public void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				Log.d("TTTTTTTTTTTTTTTTT","jskljfsfslfjslfjfs");

				lastItem = firstVisibleItem + visibleItemCount - 2;
			}

			@Override
			public void onListViewScrollStateChanged(AbsListView view, int scrollState) {
				if (lastItem == ySJHomeCommonAdapter.getCount() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && loadComplete) {
					more.setVisibility(View.GONE);
					loading.setVisibility(View.VISIBLE);
					loadFull.setVisibility(View.GONE);
					noData.setVisibility(View.GONE);
					currentPage = currentPage + 1;
					LoadData(false, currentPage);
				}
			}
		});*/
	}

	@Override
	protected void lazyLoad() {

	}
	public void LoadData(final boolean flag,int pageNum) {
		more.setVisibility(View.GONE);
		loading.setVisibility(View.VISIBLE);
		loadFull.setVisibility(View.GONE);
		noData.setVisibility(View.GONE);
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("userId","imhfp1yr4636pj49");
		paramsMap.put("currentId",currentId);
		paramsMap.put("pageSize", Constants.pageSize+"");
		paramsMap.put("pageIndex", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "userMain.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("444444失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))) {
					Map<String, Object> object = (Map<String, Object>) response.get("object");
					if (flag) {
						ySJHomeDatas.clear();
						List<HomeYSJArtWork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkList")), new TypeToken<List<HomeYSJArtWork>>() {
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
							ySJHomeDatas.addAll(commentList);
							commentList.clear();
							ySJHomeCommonAdapter.notifyDataSetChanged();
						}else {
							more.setVisibility(View.VISIBLE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.GONE);
							noData.setVisibility(View.GONE);
						}
						if (commentList != null) {
							ySJHomeDatas.addAll(commentList);
							commentList.clear();
						}
						ySJHomeCommonAdapter.notifyDataSetChanged();
					}else {
						List<HomeYSJArtWork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkList")), new TypeToken<List<HomeYSJArtWork>>() {
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
							ySJHomeDatas.addAll(commentList);
							commentList.clear();
						}
						ySJHomeCommonAdapter.notifyDataSetChanged();
					}
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}
}