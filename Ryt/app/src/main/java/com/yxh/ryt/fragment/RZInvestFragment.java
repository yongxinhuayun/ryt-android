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
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.DateUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.vo.ArtworkInvest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class RZInvestFragment extends BaseFragment{

	private String artWorkId;
	private AutoListView invester;
	private List<ArtworkInvest> investorDatas;
	private CommonAdapter<ArtworkInvest> investorRecordCommonAdapter;
	private int currentPage = 1;

	public RZInvestFragment(String artWorkId) {
		super();
		this.artWorkId = artWorkId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		investorDatas = new ArrayList<>();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.rz_invest, null);
		invester = (AutoListView) view.findViewById(R.id.at_invester);
		loadInvesterData(true, currentPage);
		return view;
	}



	private void loadInvesterData(final boolean flag, int pageNum) {

		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("artWorkId", artWorkId);
		paramsMap.put("pageSize", Constants.pageSize + "");
		paramsMap.put("pageIndex", pageNum + "");
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkInvest.do", paramsMap, new RZCommentCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
				ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if ("0".equals(response.get("resultCode"))) {
					Map<String, Object> object = (Map<String, Object>) response.get("object");

					List<ArtworkInvest> investList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestList")), new TypeToken<List<ArtworkInvest>>() {
					}.getType());

					if (investList != null) {
						investorDatas.addAll(investList);
						investList.clear();
						if (investList.size() < Constants.pageSize) {

							investorDatas.addAll(investList);
							investList.clear();
						}
					}
				}
				setInvesterAdapter();
			}
		});
	}
	private void setInvesterAdapter() {
		investorRecordCommonAdapter = new CommonAdapter<ArtworkInvest>(getActivity(), investorDatas, R.layout.investorrecord_item) {
			@Override
			public void convert(ViewHolder helper, ArtworkInvest item) {
				if (item.getCreator() != null) {
					helper.setImageByUrl(R.id.iri_iv_icon, item.getCreator().getPictureUrl());
					if (item.getCreator().getName() != null) {
						if (item.getCreator().getName().length() > 5) {
							helper.setText(R.id.iri_tv_nickname, item.getCreator().getName().substring(0, 5) + "...");
						} else {
							helper.setText(R.id.iri_tv_nickname, item.getCreator().getName());
						}
					}
				}

				if (helper.getPosition() == 0) {
					helper.getView(R.id.civ_top).setVisibility(View.VISIBLE);
					helper.getView(R.id.cl_01_civ_pm).setVisibility(View.GONE);
					helper.setImageResource(R.id.civ_top, R.mipmap.jinpai);
				} else if (helper.getPosition() == 1) {
					helper.getView(R.id.civ_top).setVisibility(View.VISIBLE);
					helper.getView(R.id.cl_01_civ_pm).setVisibility(View.GONE);
					helper.setImageResource(R.id.civ_top, R.mipmap.yinpai);
				} else if (helper.getPosition() == 2) {
					helper.getView(R.id.civ_top).setVisibility(View.VISIBLE);
					helper.getView(R.id.cl_01_civ_pm).setVisibility(View.GONE);
					helper.setImageResource(R.id.civ_top, R.mipmap.tongpai);
				} else {
					helper.getView(R.id.civ_top).setVisibility(View.GONE);
					helper.getView(R.id.cl_01_civ_pm).setVisibility(View.VISIBLE);
					helper.setText(R.id.cl_01_civ_pm, (helper.getPosition() + 1) + "");
				}
				helper.setText(R.id.iri_tv_content, "￥" + item.getPrice() + ".00");
               /* String s = DateUtil.date2String(item.getCreateDatetime(),"yyyy-MM-dd  HH:mm:ss");
                Date dt = DateUtil.string2Date(s,"yyyy-MM-dd  HH:mm:ss");*/
				helper.setText(R.id.iri_tv_date, long2Timestamp(item.getCreateDatetime()));
			}
		};
		invester.setAdapter(investorRecordCommonAdapter);
	}
	private String long2Timestamp(long time) {
		String sTime = DateUtil.date2String(time,"yyyy-MM-dd  HH:mm:ss");
		Date dt = DateUtil.string2Date(sTime,"yyyy-MM-dd  HH:mm:ss");
		return DateUtil.getTimestampString(dt);
	}
	@Override
	protected void lazyLoad() {

	}
}