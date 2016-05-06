package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.ProjectCommentReply;
import com.yxh.ryt.activity.RegisterActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.ArtworkComment;
import com.yxh.ryt.vo.ConvertWork;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

@SuppressLint("ValidFragment")
public class UserTouGuoFragment extends StickHeaderBaseFragment{
	private ListView lstv;
	private CommonAdapter<ConvertWork> userZGCommonAdapter;
	private List<ConvertWork> userZGDatas;
	private int currentPage=1;
	private View footer;
	private TextView loadFull;
	private TextView noData;
	private TextView more;
	private TextView tvTotal,tvNoData;
	private ProgressBar loading;
	private int lastItem;
	private boolean loadComplete=true;
	static StickHeaderViewPagerManager stickHeaderViewPagerManager;
	public UserTouGuoFragment(StickHeaderViewPagerManager manager, int position) {
		super(manager, position);
	}

	public UserTouGuoFragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
		super(manager, position, isCanPulltoRefresh);
	}
	public static UserTouGuoFragment newInstance(StickHeaderViewPagerManager manager, int position) {
		UserTouGuoFragment listFragment = new UserTouGuoFragment(manager, position);
		return listFragment;
	}
	public UserTouGuoFragment(){
		super();
	}
	public static UserTouGuoFragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
		UserTouGuoFragment listFragment = new UserTouGuoFragment(manager, position, isCanPulltoRefresh);
		stickHeaderViewPagerManager=manager;
		return listFragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userZGDatas=new ArrayList<ConvertWork>();
	}
	@Override
	public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_listview_touguo, null);
		placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.fit_placehoder);
		lstv = (ListView)view.findViewById(R.id.fit_lstv);
		footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null);
		tvTotal = (TextView) view.findViewById(R.id.fit_tv);
		tvNoData = (TextView) view.findViewById(R.id.fit_tv_noData);
		setAdapter();
		onScroll();
		Log.d("oncreateView", "oncreateViewoncreateViewoncreateViewoncreateViewoncreateViewoncreateViewoncreateViewoncreateViewoncreateView");
		userZGDatas.clear();
		LoadData(true, currentPage);
		return view;
	}
	private void setAdapter() {
		userZGCommonAdapter=new CommonAdapter<ConvertWork>(AppApplication.getSingleContext(),userZGDatas,R.layout.userpt__touguo_item) {
			@Override
			public void convert(ViewHolder helper, ConvertWork item) {
				helper.setImageByUrl(R.id.utf_iv_icon, item.getPicture_url());
				helper.setText(R.id.utf_tv_proName, item.getTitle());
				helper.setText(R.id.utf_tv_proStage,AppApplication.map.get(item.getStep()));
				helper.setText(R.id.utf_tv_money,"项目金额:"+item.getGoalMoney());
				helper.setText(R.id.utf_tv_name,item.getUser().getName());
				helper.setText(R.id.utf_tv_zhicheng,item.getUser().getMaster().getTitle());
			}
		};
		lstv.setAdapter(userZGCommonAdapter);
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
				if (lastItem == userZGCommonAdapter.getCount() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && loadComplete) {
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
		NetRequestUtil.post(Constants.BASE_PATH + "my.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("444444失败了");
			}
			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))) {
					Map<String, Object> object = (Map<String, Object>) response.get("pageInfo");
					tvTotal.setText("投资项目:"+AppApplication.getSingleGson().toJson(((Map<String, Object>) response.get("pageInfo")).get("num"))+"个");
					if (flag) {
						List<ConvertWork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworks")), new TypeToken<List<ConvertWork>>() {
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
							userZGDatas.addAll(commentList);
							commentList.clear();
							userZGCommonAdapter.notifyDataSetChanged();
						}else {
							more.setVisibility(View.VISIBLE);
							loading.setVisibility(View.GONE);
							loadFull.setVisibility(View.GONE);
							noData.setVisibility(View.GONE);
						}
						if (commentList != null) {
							userZGDatas.addAll(commentList);
							commentList.clear();
						}

						userZGCommonAdapter.notifyDataSetChanged();
					}else {
						List<ConvertWork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("pageInfo")), new TypeToken<List<ConvertWork>>() {
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
							userZGDatas.addAll(commentList);
							commentList.clear();
						}
						userZGCommonAdapter.notifyDataSetChanged();
					}
				}
			}
		});
	}
}