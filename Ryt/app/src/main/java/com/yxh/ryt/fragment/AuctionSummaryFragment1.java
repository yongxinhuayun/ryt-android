package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.ArtistIndexActivity;
import com.yxh.ryt.activity.AuctionListActivity;
import com.yxh.ryt.activity.AuctionProtocolActivity;
import com.yxh.ryt.activity.HeadImageActivity;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.activity.MsgActivity;
import com.yxh.ryt.activity.PayPageActivity;
import com.yxh.ryt.activity.ProjectCommentReply;
import com.yxh.ryt.activity.UserIndexActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.custemview.ExpandView;
import com.yxh.ryt.custemview.ListViewForScrollView;
import com.yxh.ryt.custemview.ScaleScreenImageView;
import com.yxh.ryt.util.DateUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.JsInterface;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.ArtWorkBidding;
import com.yxh.ryt.vo.ArtWorkPraiseList;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.ArtworkInvestTop;
import com.yxh.ryt.vo.HomeYSJArtWork;
import com.yxh.ryt.vo.User;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

@SuppressLint("ValidFragment")
public class AuctionSummaryFragment1 extends BaseFragment implements View.OnClickListener {
	@Bind(R.id.fsl_sv_picture)
	ScaleScreenImageView artworkPicture;
	@Bind(R.id.fli1_pb_progress)
	ProgressBar progressBarTop;
	@Bind(R.id.fsl_tv_state)
	TextView stateName;
	@Bind(R.id.fsl_tv_finalPerson)
	TextView finalPerson;
	@Bind(R.id.fsl_tv_title)
	TextView title;
	@Bind(R.id.fsl_tv_brief)
	TextView brief;
	@Bind(R.id.fsl_tv_price)
	TextView priceMoney;
	@Bind(R.id.fsl_tv_priceName)
	TextView priceName;
	@Bind(R.id.fsl_ll_before)
	LinearLayout beforeLayout;
	@Bind(R.id.fsl_tv_startTime)
	TextView startTime;
	@Bind(R.id.fsl_tv_wenGuan)
	TextView wenGuanBefore;
	@Bind(R.id.fsl_ll_after)
	LinearLayout afterLayout;
	@Bind(R.id.fsl_tv_frequency)
	TextView frequency;
	@Bind(R.id.fsl_tv_range)
	TextView range;
	@Bind(R.id.fsl_tv_afterWenGuan)
	TextView wenguanAfter;
	@Bind(R.id.fsl_cls_listNumber)
	ListViewForScrollView investorList;
	@Bind(R.id.cl_headPortrait)
	CircleImageView headPortrait;
	@Bind(R.id.tv_name)
	TextView artisterName;
	@Bind(R.id.tv_title)
	TextView artistTitle;
	@Bind(R.id.tv_num_works)
	TextView works;
	@Bind(R.id.tv_num_fans)
	TextView fans;
	@Bind(R.id.ib_attention)
	ImageButton attention;
	@Bind(R.id.fsl_iv_progerssShow)
	ImageView progressShow;
	@Bind(R.id.expandView_auctionFlow)
	ExpandView progressView;
	@Bind(R.id.fsl_iv_depositShow)
	ImageView depositShow;
	@Bind(R.id.expandView_deposit)
	ExpandView depositView;
	private String id;
	private String step;
	private List<ArtWorkBidding> artWorkBiddingDatas;
	private CommonAdapter<ArtWorkBidding> artWorkBiddingCommonAdapter;
	private boolean currentIsFollow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.fragment_auctionsummary, container, false);
		ButterKnife.bind(this, contextView);
		artWorkBiddingDatas=new ArrayList<>();
		loadData();
		artWorkBiddingCommonAdapter = new CommonAdapter<ArtWorkBidding>(getActivity(), artWorkBiddingDatas, R.layout.auctionfragment_item) {
			@Override
			public void convert(ViewHolder helper, final ArtWorkBidding item) {
				helper.setImageByUrl(R.id.fsl_ci_currentHeader,item.getCreator().getPictureUrl());
				helper.getView(R.id.fsl_ci_currentHeader).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if ("1".equals(item.getCreator().getType())){
							Intent intent=new Intent(getActivity(),ArtistIndexActivity.class);
							intent.putExtra("userId",item.getCreator().getId());
							intent.putExtra("name",item.getCreator().getName());
							getActivity().startActivity(intent);
						}else if ("2".equals(item.getCreator().getType())){
							Intent intent=new Intent(getActivity(),UserIndexActivity.class);
							intent.putExtra("userId",item.getCreator().getId());
							intent.putExtra("name",item.getCreator().getName());
							getActivity().startActivity(intent);
						}
					}
				});
				if (helper.getPosition()==0){
					((TextView) helper.getView(R.id.fsl_tv_currentName)).setTextColor(Color.rgb(199,31,33));
					((TextView) helper.getView(R.id.fsl_tv_currentDate)).setTextColor(Color.rgb(199,31,33));
					((TextView) helper.getView(R.id.fsl_tv_currentNumber)).setTextColor(Color.rgb(199,31,33));

				}
				helper.setText(R.id.fsl_tv_currentName,item.getCreator().getName());
				helper.setText(R.id.fsl_tv_currentDate,Utils.timeToFormatTemp("MM.dd HH:mm:ss",item.getCreateDatetime()));
				helper.setText(R.id.fsl_tv_currentNumber,item.getPrice()+"");
			}
		};
		investorList.setAdapter(artWorkBiddingCommonAdapter);
		progressView.setLayoutId(R.layout.progressexpandview);
		depositView.setLayoutId(R.layout.depositexpandview);
		progressView.collapse();
		depositView.collapse();
		for (int i = 0; i < progressView.getChildCount(); i++) {
			progressView.getChildAt(i).setVisibility(View.GONE);
		}
		for (int i = 0; i < depositView.getChildCount(); i++) {
			depositView.getChildAt(i).setVisibility(View.GONE);
		}
		progressShow.setOnClickListener(this);
		depositShow.setOnClickListener(this);
		return contextView;
	}
	private void loadData() {
		final Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("artWorkId",id);
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.get(Constants.BASE_PATH + "artWorkAuctionView.do", paramsMap, new AttentionListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("".equals(AppApplication.gUser.getId())){
					if ("0".equals(response.get("resultCode"))){
						final Artwork artwork = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
								toJson(response.get("artwork")), Artwork.class);
						final List<ArtWorkBidding> artWorkBiddingList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
								toJson(response.get("artWorkBidding")), new TypeToken<List<ArtWorkBidding>>() {
						}.getType());
						investorList.hideFooterView();
						artWorkBiddingDatas.clear();
						artWorkBiddingCommonAdapter.notifyDataSetChanged();
						if (artWorkBiddingList!=null){
							if (artWorkBiddingList.size()>0){
								if (artWorkBiddingList.size()>5){
									for (int i=0;i<5;i++){
										artWorkBiddingDatas.add(artWorkBiddingList.get(i));
										investorList.showFooterView();
									}
									investorList.footerView.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											Intent intent=new Intent(getActivity(), AuctionListActivity.class);
											intent.putExtra("artWorkBiddingDatas", (Serializable) artWorkBiddingList);
											getActivity().startActivity(intent);
										}
									});
									artWorkBiddingCommonAdapter.notifyDataSetChanged();
								}else {
									artWorkBiddingDatas.addAll(artWorkBiddingList);
									investorList.hideFooterView();
									artWorkBiddingCommonAdapter.notifyDataSetChanged();
								}
							}

						}else if (artWorkBiddingList ==null ||artWorkBiddingList.size()==0){
							investorList.setVisibility(View.GONE);
						}
						attention.setImageResource(R.mipmap.guanzhuqian);
						attention.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent=new Intent(getActivity(),LoginActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								getActivity().startActivity(intent);
							}
						});
						title.setText(artwork.getTitle());
						if (brief!=null){
							brief.setText(artwork.getBrief());
						}else {
							brief.setText("无简介");
						}
						AppApplication.displayImage(artwork.getAuthor().getPictureUrl(),headPortrait);
						headPortrait.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent=new Intent(getActivity(),ArtistIndexActivity.class);
								intent.putExtra("userId",artwork.getAuthor().getId());
								intent.putExtra("name",artwork.getAuthor().getName());
								getActivity().startActivity(intent);
							}
						});
						AppApplication.displayImage(artwork.getPicture_url(),artworkPicture);
						artisterName.setText(artwork.getAuthor().getName()+"");
						if (artwork.getAuthor().getMaster().getTitle()==null || "".equals(artwork.getAuthor().getMaster().getTitle().trim())){
							artistTitle.setText("青年艺术家");
						}else {
							artistTitle.setText(artwork.getAuthor().getMaster().getTitle());
						}
						works.setText(artwork.getAuthor().getMasterWorkNum()+"件作品");
						fans.setText(artwork.getAuthor().getFansNum()+"个粉丝");
						long progress=(artwork.getAuctionStartDatetime()-System.currentTimeMillis())/24/1000/60/60*100;
						progressBarTop.setProgress((int) progress);
						if ("30".equals(step)){
							beforeLayout.setVisibility(View.VISIBLE);
							afterLayout.setVisibility(View.GONE);
							stateName.setText("拍卖预告");
							priceName.setText("起拍价");
							finalPerson.setText(Utils.getJudgeDate2(artwork.getAuctionStartDatetime())+"后开始拍卖");
							priceMoney.setText(artwork.getInvestGoalMoney()+"");
							startTime.setText(Utils.timeToFormatTemp("M月d日 H:m:s",artwork.getAuctionStartDatetime())+"开拍");
							wenGuanBefore.setText(artwork.getViewNum()+"");
						}if ("31".equals(step)){
							beforeLayout.setVisibility(View.GONE);
							afterLayout.setVisibility(View.VISIBLE);
							stateName.setText("拍卖中...");
							priceName.setText("当前价");
							finalPerson.setText(Utils.getJudgeDate2(artwork.getAuctionEndDatetime())+"后拍卖结束");
							priceMoney.setText(artwork.getNewBidingPrice()+"");
							frequency.setText(artwork.getAuctionNum()+"");
							range.setText(getAuctionPrice(artwork.getInvestGoalMoney().longValue())+"");
							wenguanAfter.setText(artwork.getViewNum()+"");
						}if ("32".equals(step)){
							beforeLayout.setVisibility(View.GONE);
							afterLayout.setVisibility(View.VISIBLE);
							stateName.setText("拍卖结束");
							priceName.setText("成交价");
							priceMoney.setText(artwork.getNewBidingPrice()+"");
							if(artwork.getWinner()!=null){
								finalPerson.setText("恭喜"+artwork.getWinner().getName()+"拍得此件作品");
							}
							frequency.setText(artwork.getAuctionNum()+"");
							range.setText(getAuctionPrice(artwork.getInvestGoalMoney().longValue())+"");
							wenguanAfter.setText(artwork.getViewNum()+"");
						}else {
							beforeLayout.setVisibility(View.GONE);
							afterLayout.setVisibility(View.VISIBLE);
							stateName.setText("拍卖结束");
							priceName.setText("成交价");
							priceMoney.setText(artwork.getNewBidingPrice()+"");
							if(artwork.getWinner()!=null){
								finalPerson.setText("恭喜"+artwork.getWinner().getName()+"拍得此件作品");
							}
							frequency.setText(artwork.getAuctionNum()+"");
							range.setText(getAuctionPrice(artwork.getInvestGoalMoney().longValue())+"");
							wenguanAfter.setText(artwork.getViewNum()+"");
						}
					}
				}else {
					SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
						@Override
						public void getCode(String code) {
							if ("0".equals(code)){
								NetRequestUtil.get(Constants.BASE_PATH + "artWorkAuctionView.do", paramsMap, new RongZiListCallBack() {
									@Override
									public void onError(Call call, Exception e) {
										ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
									}

									@Override
									public void onResponse(Map<String, Object> response) {
										if ("0".equals(response.get("resultCode"))){
											final Artwork artwork = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
													toJson(response.get("artwork")), Artwork.class);
											final List<ArtWorkBidding> artWorkBiddingList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
													toJson(response.get("artWorkBidding")), new TypeToken<List<ArtWorkBidding>>() {
											}.getType());
											investorList.hideFooterView();
											artWorkBiddingDatas.clear();
											artWorkBiddingCommonAdapter.notifyDataSetChanged();
											if (artWorkBiddingList!=null){
												if (artWorkBiddingList.size()>0){
													if (artWorkBiddingList.size()>5){
														for (int i=0;i<5;i++){
															artWorkBiddingDatas.add(artWorkBiddingList.get(i));
															artWorkBiddingCommonAdapter.notifyDataSetChanged();
															investorList.showFooterView();
														}
														investorList.footerView.setOnClickListener(new View.OnClickListener() {
															@Override
															public void onClick(View v) {
																Intent intent=new Intent(getActivity(), AuctionListActivity.class);
																intent.putExtra("artWorkBiddingDatas", (Serializable) artWorkBiddingList);
																getActivity().startActivity(intent);
															}
														});
														artWorkBiddingCommonAdapter.notifyDataSetChanged();
													}else {
														artWorkBiddingDatas.addAll(artWorkBiddingList);
														investorList.hideFooterView();
														artWorkBiddingCommonAdapter.notifyDataSetChanged();
													}
												}
											}
											else if (artWorkBiddingList ==null ||artWorkBiddingList.size()==0){
												investorList.setVisibility(View.GONE);
											}
											if (AppApplication.gUser.getId().equals(artwork.getAuthor().getId())){
												attention.setVisibility(View.GONE);
											}
											currentIsFollow=Boolean.parseBoolean(AppApplication.getSingleGson().toJson(response.get("isFollowed")));
											if (currentIsFollow){
												attention.setImageResource(R.mipmap.guanzhuhou);
											}else {
												attention.setImageResource(R.mipmap.guanzhuqian);
											}
											attention.setOnClickListener(new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													if (currentIsFollow){
														attention.setEnabled(false);
														noAttention_user(attention,artwork.getAuthor().getId());
													}else {
														attention.setEnabled(false);
														attention_user(attention,artwork.getAuthor().getId());
													}
												}
											});
											title.setText(artwork.getTitle());
											if (brief!=null){
												brief.setText(artwork.getBrief());
											}else {
												brief.setText("无简介");
											}
											AppApplication.displayImage(artwork.getAuthor().getPictureUrl(),headPortrait);
											headPortrait.setOnClickListener(new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													Intent intent=new Intent(getActivity(),ArtistIndexActivity.class);
													intent.putExtra("userId",artwork.getAuthor().getId());
													intent.putExtra("name",artwork.getAuthor().getName());
													getActivity().startActivity(intent);
												}
											});
											AppApplication.displayImage(artwork.getPicture_url(),artworkPicture);
											artisterName.setText(artwork.getAuthor().getName()+"");
											if (artwork.getAuthor().getMaster().getTitle()==null || "".equals(artwork.getAuthor().getMaster().getTitle().trim())){
												artistTitle.setText("青年艺术家");
											}else {
												artistTitle.setText(artwork.getAuthor().getMaster().getTitle());
											}
											works.setText(artwork.getAuthor().getMasterWorkNum()+"件作品");
											fans.setText(artwork.getAuthor().getFansNum()+"个粉丝");
											long progress=(artwork.getAuctionStartDatetime()-System.currentTimeMillis())/24/1000/60/60*100;
											progressBarTop.setProgress((int) progress);
											if ("30".equals(step)){
												beforeLayout.setVisibility(View.VISIBLE);
												afterLayout.setVisibility(View.GONE);
												stateName.setText("拍卖预告");
												priceName.setText("起拍价");
												finalPerson.setText(Utils.getJudgeDate2(artwork.getAuctionStartDatetime())+"后开始拍卖");
												priceMoney.setText(artwork.getInvestGoalMoney()+"");
												startTime.setText(Utils.timeToFormatTemp("M月d日 H:m:s",artwork.getAuctionStartDatetime())+"开拍");
												wenGuanBefore.setText(artwork.getViewNum()+"");
											}if ("31".equals(step)){
												beforeLayout.setVisibility(View.GONE);
												afterLayout.setVisibility(View.VISIBLE);
												stateName.setText("拍卖中...");
												priceName.setText("当前价");
												finalPerson.setText(Utils.getJudgeDate2(artwork.getAuctionEndDatetime())+"后拍卖结束");
												priceMoney.setText(artwork.getNewBidingPrice()+"");
												frequency.setText(artwork.getAuctionNum()+"");
												range.setText(getAuctionPrice(artwork.getInvestGoalMoney().longValue())+"");
												wenguanAfter.setText(artwork.getViewNum()+"");
											}if ("32".equals(step)){
												beforeLayout.setVisibility(View.GONE);
												afterLayout.setVisibility(View.VISIBLE);
												stateName.setText("拍卖结束");
												priceName.setText("成交价");
												priceMoney.setText(artwork.getNewBidingPrice()+"");
												if(artwork.getWinner()!=null){
													finalPerson.setText("恭喜"+artwork.getWinner().getName()+"拍得此件作品");
												}
												frequency.setText(artwork.getAuctionNum()+"");
												range.setText(getAuctionPrice(artwork.getInvestGoalMoney().longValue())+"");
												wenguanAfter.setText(artwork.getViewNum()+"");
											}
										}
									}
								});
							}
						}
					});
					sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
				}
			}
		});
	}
	private void attention_user(final View v, final String followId) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("followId", followId);
		paramsMap.put("identifier", "0");
		paramsMap.put("followType", "1");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "changeFollowStatus.do", paramsMap, new AttentionListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))){
					ToastUtil.showShort(getActivity(),"关注成功");
					((ImageView) v).setImageResource(R.mipmap.guanzhuhou);
					v.setEnabled(true);
					currentIsFollow=true;
				}else if ("000000".equals(response.get("resultCode"))){
					SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
						@Override
						public void getCode(String code) {
							if ("0".equals(code)){
								attention_user(v,followId);
							}
						}
					});
					sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
				}

			}
		});
	}
	private void noAttention_user(final View v, final String followId) {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("followId", followId);
		paramsMap.put("identifier", "1");
		paramsMap.put("followType", "1");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "changeFollowStatus.do", paramsMap, new AttentionListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))){
					ToastUtil.showShort(getActivity(),"取消关注");
					((ImageView) v).setImageResource(R.mipmap.guanzhuqian);
					v.setEnabled(true);
					currentIsFollow=false;
				}else if ("000000".equals(response.get("resultCode"))){
					SessionLogin sessionLogin=new SessionLogin(new SessionLogin.CodeCallBack() {
						@Override
						public void getCode(String code) {
							if ("0".equals(code)){
								noAttention_user(v,followId);
							}
						}
					});
					sessionLogin.resultCodeCallback(AppApplication.gUser.getLoginState());
				}
			}
		});
	}
	public AuctionSummaryFragment1() {
	}

	public AuctionSummaryFragment1(String id, String step) {
		this.id = id;
		this.step=step;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	protected void lazyLoad() {

	}
	//计算加价幅度
	private int getAuctionPrice(long price) {
		//计算加价幅度
		if (price <= 499) {
			return 10;
		} else if (price >= 500 && price <= 999) {
			return 50;
		} else if (price >= 1000 && price <= 4999) {
			return 100;
		} else if (price >= 5000 && price <= 9999) {
			return 200;
		} else if (price >= 10000 && price <= 29999) {
			return 500;
		} else if (price >= 30000 && price <= 99999) {
			return 1000;
		} else if (price >= 100000) {
			return 2000;
		}
		return 0;
	}
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.fsl_iv_progerssShow:
				if (progressView.isExpand()) {
					progressView.collapse();
					for (int i = 0; i < progressView.getChildCount(); i++) {
						progressView.getChildAt(i).setVisibility(View.GONE);
					}
					progressShow.setImageResource(R.mipmap.show_progress);
					progressView.setVisibility(View.VISIBLE);
				} else {
					progressView.expand();

					for (int i = 0; i < progressView.getChildCount(); i++) {
						progressView.getChildAt(i).setVisibility(View.VISIBLE);
					}
					progressShow.setImageResource(R.mipmap.no_show_progress);
				}
				break;
			case R.id.fsl_iv_depositShow:
				if (depositView.isExpand()) {
					depositView.collapse();
					for (int i = 0; i < depositView.getChildCount(); i++) {
						depositView.getChildAt(i).setVisibility(View.GONE);
					}
					depositShow.setImageResource(R.mipmap.show_progress);
					depositView.setVisibility(View.VISIBLE);
				} else {
					depositView.expand();

					for (int i = 0; i < depositView.getChildCount(); i++) {
						depositView.getChildAt(i).setVisibility(View.VISIBLE);
					}
					depositShow.setImageResource(R.mipmap.no_show_progress);
				}

		}
	}
}