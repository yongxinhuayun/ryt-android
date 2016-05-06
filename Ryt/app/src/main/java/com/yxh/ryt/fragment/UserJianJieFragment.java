package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.yxh.ryt.vo.Artwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;

@SuppressLint("ValidFragment")
public class UserJianJieFragment extends StickHeaderBaseFragment{
	private AutoListView lstv;
	private CommonAdapter<Artwork> userTGCommonAdapter;
	private List<Artwork> userTGDatas;
	private int currentPage=1;
	static StickHeaderViewPagerManager stickHeaderViewPagerManager;
	public UserJianJieFragment(StickHeaderViewPagerManager manager, int position) {
		super(manager, position);
	}
	public UserJianJieFragment(){
		super();
	}
	public UserJianJieFragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
		super(manager, position, isCanPulltoRefresh);
	}
	public static UserJianJieFragment newInstance(StickHeaderViewPagerManager manager, int position) {
		UserJianJieFragment listFragment = new UserJianJieFragment(manager, position);
		return listFragment;
	}

	public static UserJianJieFragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
		UserJianJieFragment listFragment = new UserJianJieFragment(manager, position, isCanPulltoRefresh);
		stickHeaderViewPagerManager=manager;
		return listFragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userTGDatas=new ArrayList<Artwork>();
	}
	private void LoadData() {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("userId","ieatht97wfw30hfd");
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
				Log.d("introintro","introintrointrointrointrointrointrointrointrointrointrointrointro");
			}
		});
	}

	@Override
	public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contextView = inflater.inflate(R.layout.fragment_user_edit, container, false);
		TextView content = (TextView) contextView.findViewById(R.id.fue_tv_content);
		TextView edit = (TextView) contextView.findViewById(R.id.fue_bt_edit);
		placeHoderHeaderLayout = (PlaceHoderHeaderLayout) contextView.findViewById(R.id.fue_placehoder);
		return contextView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	@Override
	protected void lazyLoad() {
		if(userTGDatas!=null&&userTGDatas.size()>0)return;
		LoadData();
	}
}