package com.yxh.ryt.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.ChatMsgViewAdapter;
import com.yxh.ryt.callback.LoginCallBack;
import com.yxh.ryt.callback.NotifaicationCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Sha1;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.ChatMsgEntity;
import com.yxh.ryt.vo.PrivateLetter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import okhttp3.Call;

public class MsgActivity extends BaseActivity implements OnClickListener {

	private TextView mBtnSend;
	private EditText mEditTextContent;
	private LinearLayout mBottom;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	private String fromId="";
	private String name;
	private TextView title;
	private String userId;
	private String currentName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		fromId=getIntent().getStringExtra("formId");
		name = getIntent().getStringExtra("name");
		userId = getIntent().getStringExtra("userId");
		currentName = getIntent().getStringExtra("currentName");
		EventBus.getDefault().register(this);
		initView();
	}

	public void initView() {
		mListView = (ListView) findViewById(R.id.listview);
		mBtnSend = (TextView) findViewById(R.id.btn_send);
		title = (TextView) findViewById(R.id.tv_top_ct);
		title.setText(name);
		mBtnSend.setOnClickListener(this);
		mBottom = (LinearLayout) findViewById(R.id.btn_bottom);
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
	}

	private String[] msgArray = new String[] {};

	private String[] dataArray = new String[] { };

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_send:
			send();
			break;
		}
	}

	private void send() {
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0) {
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(Utils.getCurrentTime());
			entity.setName(currentName);
			entity.setMsgType(false);
			entity.setText(contString);
			pushMessageRequst();
			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();
			mEditTextContent.setText("");
			mListView.setSelection(mListView.getCount() - 1);
		}
	}

	private void pushMessageRequst() {
		Map<String,String> paramsMap=new HashMap<>();
		paramsMap.put("content",mEditTextContent.getText()+"");
		/*paramsMap.put("fromUserId",AppApplication.gUser.getId() );*/
		paramsMap.put("fromUserId",userId );
		paramsMap.put("targetUserId",fromId );
		paramsMap.put("timestamp",System.currentTimeMillis()+"");
		try {
			AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
			paramsMap.put("signmsg", AppApplication.signmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "pushMessage.do", paramsMap, new LoginCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				e.printStackTrace();
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				System.out.println("成功了");
			}
		});
	}
	@Subscribe
	public void onEventMainThread(ChatMsgEntity entity) {
		if (entity.getUserId().equals(fromId)){
			entity.setName(name);
			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();
			mListView.setSelection(mListView.getCount() - 1);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		LoadData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	private void LoadData() {
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("userId", userId);
		paramsMap.put("fromUserId", fromId);
		paramsMap.put("timestamp", System.currentTimeMillis() + "");
		try {
			paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
		NetRequestUtil.post(Constants.BASE_PATH + "commentDetail.do", paramsMap, new NotifaicationCallBack() {
			@Override
			public void onError(Call call, Exception e) {
				System.out.println("失败了");
			}

			@Override
			public void onResponse(Map<String, Object> response) {
				List<PrivateLetter> notificationList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<PrivateLetter>>() {
				}.getType());
				if(notificationList!=null){
					Iterator<PrivateLetter> iterator = notificationList.iterator();
					while (iterator.hasNext()){
						ChatMsgEntity entity = new ChatMsgEntity();
						PrivateLetter next = iterator.next();
						entity.setDate(Utils.timeToFormatTemp("yyyy-MM-dd hh:mm:ss", next.getCreateDatetime()));
						entity.setName(next.getFromUser().getName());
						entity.setText(next.getContent());
						/*if(AppApplication.gUser.getId().equals(next.getFromUser().getId())) {
							entity.setMsgType(false);
						}*/
						if(userId.equals(next.getFromUser().getId())) {
							entity.setMsgType(false);
						}
						mDataArrays.add(entity);
					}
					mAdapter.notifyDataSetChanged();
					mListView.setSelection(mListView.getCount() - 1);
				}
			}
		});
	}
}