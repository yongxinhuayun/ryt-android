package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
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
import com.yxh.ryt.vo.PageinfoList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

@SuppressLint("ValidFragment")
public class UserZanGuoFragment extends StickHeaderBaseFragment{
	private ListView lstv;
	private CommonAdapter<Artwork> userZGCommonAdapter;
	private List<Artwork> userZGDatas;
	private int currentPage=1;
	private View footer;
	private TextView loadFull;
	private TextView noData;
	private TextView more;
	private ProgressBar loading;
	private int lastItem;
	private boolean loadComplete=true;
	static StickHeaderViewPagerManager stickHeaderViewPagerManager;
	private TextView tv_noData;
	private static String userId;
	private static String currentId;
	public UserZanGuoFragment(StickHeaderViewPagerManager manager, int position) {
		super(manager, position);
	}
	public UserZanGuoFragment() {
		super();
	}
	public UserZanGuoFragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
		super(manager, position, isCanPulltoRefresh);
	}
	public static UserZanGuoFragment newInstance(StickHeaderViewPagerManager manager, int position ) {
		UserZanGuoFragment listFragment = new UserZanGuoFragment(manager, position);
		return listFragment;
	}

	public static UserZanGuoFragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh, String userID, String currentID) {
		UserZanGuoFragment listFragment = new UserZanGuoFragment(manager, position, isCanPulltoRefresh);
		stickHeaderViewPagerManager=manager;
		userId=userID;
		currentId=currentID;
		return listFragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userZGDatas=new ArrayList<Artwork>();
	}
	@Override
	public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_listview_zanguo, null);
		placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);
		tv_noData = (TextView) view.findViewById(R.id.flz_tv_noData);
		tv_noData.setVisibility(View.GONE);
		lstv = (ListView)view.findViewById(R.id.fiz_lstv);
		footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null);
		setAdapter();
		onScroll();
		userZGDatas.clear();
		LoadData(true, currentPage);
		return view;
	}
	private void setAdapter() {
		userZGCommonAdapter=new CommonAdapter<Artwork>(AppApplication.getSingleContext(),userZGDatas,R.layout.userpt__zanguo_item) {
			@Override
			public void convert(ViewHolder helper, final Artwork item) {
				if (item!=null){
					helper.setImageByUrl(R.id.uzf_iv_icon, item.getPicture_url());
					helper.setText(R.id.uzf_tv_proName, item.getTitle());
					helper.setText(R.id.uzf_tv_proStage, AppApplication.map.get(item.getStep()));
					helper.setText(R.id.uzf_tv_money,"项目金额:"+item.getInvestGoalMoney());
					helper.setText(R.id.uzf_tv_name,item.getAuthor().getName());
					helper.setText(R.id.uzf_tv_zhicheng,item.getAuthor().getMaster().getTitle());
					helper.setText(R.id.uzf_tv_zanTotal,item.getPraiseNUm()+"");
					if (item.isPraise()){
						helper.getView(R.id.uzf_iv_zan).setBackgroundResource(R.mipmap.dianzanhou);
						helper.getView(R.id.uzf_iv_zan).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								cancelPraise(v,item.getId(), AppApplication.gUser.getId());
							}
						});
					}else {
						helper.getView(R.id.uzf_iv_zan).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								praise(v, item.getId(), AppApplication.gUser.getId());
							}
						});
					}
				}
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

	private void praise(final View v, String id, String id1) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("artworkId",id);
		paramsMap.put("currentUserId",id1);
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "artworkPraise.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("444444失败了");
			}
			@Override
			public void onResponse(Map<String, Object> response) {
				System.out.println(response+"dudududuuuuuuuuuuuuuuuuuuuuu");
				if ("0".equals(response.get("resultCode"))) {
					((ImageView) v).setImageResource(R.mipmap.dianzanhou);
					AnimationSet animationSet=new AnimationSet(true);
					ScaleAnimation scaleAnimation=new ScaleAnimation(1,1.5f,1,1.5f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
					scaleAnimation.setDuration(200);
					animationSet.addAnimation(scaleAnimation);
					animationSet.setFillAfter(true);
					animationSet.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							AnimationSet animationSet=new AnimationSet(true);
							ScaleAnimation scaleAnimation=new ScaleAnimation(1.5f,1f,1.5f,1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
							scaleAnimation.setDuration(200);
							animationSet.addAnimation(scaleAnimation);
							animationSet.setFillAfter(true);
							((ImageView) v).startAnimation(animationSet);
							currentPage=1;
							userZGDatas.clear();
							LoadData(true,currentPage);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}
					});
					((ImageView) v).startAnimation(animationSet);
				}
			}
		});
	}

	private void cancelPraise(View v, String id, String id1) {

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
		paramsMap.put("userId",userId);
		paramsMap.put("currentId",currentId);
		paramsMap.put("type","1");
		paramsMap.put("pageSize", Constants.pageSize+"");
		paramsMap.put("pageIndex", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "followed.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("444444失败了");
			}
			@Override
			public void onResponse(Map<String, Object> response) {
				System.out.println(response+"dudududuuuuuuuuuuuuuuuuuuuuu");
				if ("0".equals(response.get("resultCode"))) {
					System.out.println(response.get("resultCode")+"dudududuuuuuuuuuuuuuuuuuuuuu");
					if (flag) {
						List<Artwork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("pageInfoList")), new TypeToken<List<Artwork>>() {
						}.getType());
						if (commentList == null || commentList.size()==0) {
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
						List<Artwork> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("pageInfoList")), new TypeToken<List<Artwork>>() {
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