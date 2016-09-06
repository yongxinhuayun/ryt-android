package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.PushWoraActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.custemview.ActionSheetDialog;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.MasterWork;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/18.
 */
@SuppressLint("ValidFragment")
public class WorksFragment extends BaseFragment {
    private String userId;
    private List<MasterWork> workDatas;
    private int currentPage = 1;
    private CommonAdapter<MasterWork> imageAdapter;
    private GridView gridView;
    private TextView pushWork;
    public WorksFragment(String userId) {
        this.userId = userId;
    }

    public WorksFragment() {
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workDatas = new ArrayList<MasterWork>();
        if (savedInstanceState != null
                && savedInstanceState.getString("userId")!=null) {
            userId=savedInstanceState.getString("userId");

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_works, container,false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        pushWork = (TextView) view.findViewById(R.id.fub_tv_edit);
        setAdapter();
        if (AppApplication.gUser.getId().equals(userId)){
            pushWork.setVisibility(View.VISIBLE);
            pushWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.FW_BROADCAST");
                    AppApplication.getSingleContext().sendBroadcast(intent);
                }
            });
        }else {
            pushWork.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("userId",userId);
    }

    private void loadData(final boolean flag, int pageNum) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("pageSize", Constants.pageSize + "");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
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
                if ("0".equals(response.get("resultCode"))) {
                    workDatas.clear();
                    Map<String, Object> object = (Map<String, Object>) response.get("object");
                    if (flag) {
                        List<MasterWork> imageList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("masterWorkList")), new TypeToken<List<MasterWork>>() {
                        }.getType());
                        if (imageList != null) {
                            workDatas.addAll(imageList);
                            imageList.clear();
                        }
                        imageAdapter.notifyDataSetChanged();

                    } else {
                        List<MasterWork> imageList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("masterWorkList")), new TypeToken<List<MasterWork>>() {
                        }.getType());

                        if (imageList != null) {
                            workDatas.addAll(imageList);
                            imageList.clear();
                        }
                        imageAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void setAdapter() {
        imageAdapter = new CommonAdapter<MasterWork>(getActivity(),workDatas,R.layout.item_image_works) {
            @Override
            public void convert(ViewHolder helper, MasterWork item) {
                helper.setText(R.id.tv_title,item.getName());
                helper.setImageByUrl(R.id.iv_works,item.getPictureUrl());
                if (item.getCreateYear()==null){
                    helper.setText(R.id.tv_detail, item.getMaterial()  + "/" + judgeStaus(item.getType()));
                }else {
                    helper.setText(R.id.tv_detail,item.getMaterial() + "/" + item.getCreateYear() + "/" + judgeStaus(item.getType()));
                }
            }
        };
        gridView.setAdapter(imageAdapter);

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

    @Override
    public void onResume() {
        super.onResume();
        currentPage=1;
        loadData(true,currentPage);
    }
}


