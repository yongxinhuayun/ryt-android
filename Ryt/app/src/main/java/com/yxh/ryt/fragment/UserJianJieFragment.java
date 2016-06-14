package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.EditBriefActivity;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

@SuppressLint("ValidFragment")
public class UserJianJieFragment extends StickHeaderBaseFragment implements View.OnClickListener {
	static StickHeaderViewPagerManager stickHeaderViewPagerManager;
	private TextView content;
	private TextView wenZi;
	private TextView edit;
	private static String userId;
	public UserJianJieFragment(StickHeaderViewPagerManager manager, int position) {
		super(manager, position);
	}
	public UserJianJieFragment(){
		super();
	}
	public UserJianJieFragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
		super(manager, position, isCanPulltoRefresh);
	}
	public static UserJianJieFragment newInstance(StickHeaderViewPagerManager manager, int position ) {
		UserJianJieFragment listFragment = new UserJianJieFragment(manager, position);
		return listFragment;
	}

	public static UserJianJieFragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh,String userID) {
		UserJianJieFragment listFragment = new UserJianJieFragment(manager, position, isCanPulltoRefresh);
		stickHeaderViewPagerManager=manager;
		userId=userID;
		return listFragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	private void LoadData() {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("userId",userId);
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "intro.do", paramsMap, new RongZiListCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				if (response.get("resultCode").equals("0")){
					Map<String,Object> userBrief = (Map<String, Object>) response.get("userBrief");
					if (userId.equals(AppApplication.gUser.getId())){
						if (userBrief!=null){
							if (null==userBrief.get("content")|| "".equals(userBrief.get("content").toString())){
								wenZi.setVisibility(View.VISIBLE);
								edit.setVisibility(View.VISIBLE);
								content.setVisibility(View.GONE);
							}else {
								wenZi.setVisibility(View.GONE);
								edit.setVisibility(View.GONE);
								content.setVisibility(View.VISIBLE);
								content.setText(userBrief.get("content").toString());
							}
						}
						else {
							wenZi.setVisibility(View.VISIBLE);
							edit.setVisibility(View.VISIBLE);
							content.setVisibility(View.GONE);
						}
					}else {
						if (userBrief!=null){
							if (null==userBrief.get("content") || "".equals(userBrief.get("content").toString())){
								wenZi.setVisibility(View.VISIBLE);
								edit.setVisibility(View.GONE);
								content.setVisibility(View.GONE);
							}else {
								wenZi.setVisibility(View.GONE);
								edit.setVisibility(View.GONE);
								content.setVisibility(View.VISIBLE);
								content.setText(userBrief.get("content").toString());
							}
						}
						else {
							wenZi.setVisibility(View.VISIBLE);
							edit.setVisibility(View.GONE);
							content.setVisibility(View.GONE);
						}
					}

				}
			}
		});
	}
	@Override
	public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.fragment_user_edit, container, false);
		content = (TextView) contextView.findViewById(R.id.fue_tv_content);
		wenZi = (TextView) contextView.findViewById(R.id.fue_tv_weiContent);
		edit = (TextView) contextView.findViewById(R.id.fue_bt_edit);
		placeHoderHeaderLayout = (PlaceHoderHeaderLayout) contextView.findViewById(R.id.fue_placehoder);
		edit.setOnClickListener(this);
		return contextView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	@Override
	protected void lazyLoad() {

	}

	@Override
	public void onResume() {
		super.onResume();
		LoadData();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.fue_bt_edit:
				startActivity(new Intent(getActivity(), EditBriefActivity.class));
				break;
			default:
				break;

		}
	}
}