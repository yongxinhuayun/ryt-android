package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.ArtistIndexActivity;
import com.yxh.ryt.activity.CommentListActivity;
import com.yxh.ryt.activity.EditProject01Activity;
import com.yxh.ryt.activity.InvestorActivity;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.activity.PraiseListActivity;
import com.yxh.ryt.activity.ProjectCommentReply;
import com.yxh.ryt.activity.UserIndexActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.AttentionListCallBack;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.custemview.ExpandView;
import com.yxh.ryt.custemview.HorizontalListView;
import com.yxh.ryt.custemview.ListViewForScrollView;
import com.yxh.ryt.custemview.RoundProgressBar;
import com.yxh.ryt.util.AnimPraiseCancel;
import com.yxh.ryt.util.DateUtil;
import com.yxh.ryt.util.EditTextFilterUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.LoadingUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.SessionLogin;
import com.yxh.ryt.util.ShuoMClickableSpan;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.ContentValidation;
import com.yxh.ryt.vo.ArtWorkPraiseList;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.ArtworkComment;
import com.yxh.ryt.vo.ArtworkInvest;
import com.yxh.ryt.vo.ArtworkInvestTop;
import com.yxh.ryt.vo.RongZi;
import com.yxh.ryt.vo.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

@SuppressLint("ValidFragment")
public class ArtistCheckFragment extends BaseFragment implements AutoListView.OnRefreshListener, AutoListView.OnLoadListener {

    private String artWorkId;
    private AutoListView lstV;
    private CommonAdapter<RongZi> rongZiCommonAdapter;
    private List<RongZi> rongZiDatas;
    private int currentPage = 1;

    public ArtistCheckFragment(String artWorkId) {
        super();
        this.artWorkId = artWorkId;
    }

    public ArtistCheckFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rongZiDatas = new ArrayList<RongZi>();

    }
    private void loadData(final int state, final int pageNum) {
        final Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type","0");
        paramsMap.put("pageSize", Constants.pageSize + "");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "getArtWorkListByAuthor.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
                ToastUtil.showLong(getActivity(), "网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(final Map<String, Object> response) {
                if ("".equals(AppApplication.gUser.getId())) {
                    if (state == AutoListView.REFRESH) {
                        lstV.onRefreshComplete();
                        rongZiDatas.clear();
                        List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson()
                                .toJson( response.get("artworkList")), new TypeToken<List<RongZi>>() {
                        }.getType());
                        if (null == objectList || objectList.size() == 0) {
                            lstV.setResultSize(0);
                        }
                        if (null != objectList && objectList.size() > 0) {
                            lstV.setResultSize(objectList.size());
                            rongZiDatas.addAll(objectList);
                            rongZiCommonAdapter.notifyDataSetChanged();
                        }
                    }
                    if (state == AutoListView.LOAD) {
                        lstV.onLoadComplete();
                        List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson()
                                .toJson( response.get("artworkList")), new TypeToken<List<RongZi>>() {
                        }.getType());
                        if (null == objectList || objectList.size() == 0) {
                            lstV.setResultSize(1);
                        }
                        if (null != objectList && objectList.size() > 0) {
                            lstV.setResultSize(objectList.size());
                            rongZiDatas.addAll(objectList);
                            rongZiCommonAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    SessionLogin sessionLogin = new SessionLogin(new SessionLogin.CodeCallBack() {
                        @Override
                        public void getCode(String code) {
                            if ("0".equals(code)) {
                                NetRequestUtil.post(Constants.BASE_PATH + "getArtWorkListByAuthor.do", paramsMap, new RongZiListCallBack() {
                                    @Override
                                    public void onError(Call call, Exception e) {
                                        ToastUtil.showLong(getActivity(), "网络连接超时,稍后重试!");
                                    }

                                    @Override
                                    public void onResponse(Map<String, Object> response) {
                                        if (state == AutoListView.REFRESH) {
                                            lstV.onRefreshComplete();
                                            rongZiDatas.clear();
                                            List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson( response.get("artworkList")), new TypeToken<List<RongZi>>() {
                                            }.getType());
                                            if (null == objectList || objectList.size() == 0) {
                                                lstV.setResultSize(0);
                                            }
                                            if (null != objectList && objectList.size() > 0) {
                                                lstV.setResultSize(objectList.size());
                                                rongZiDatas.addAll(objectList);
                                                rongZiCommonAdapter.notifyDataSetChanged();
                                            }
                                        }
                                        if (state == AutoListView.LOAD) {
                                            lstV.onLoadComplete();
                                            List<RongZi> objectList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson( response.get("artworkList")), new TypeToken<List<RongZi>>() {
                                            }.getType());
                                            if (null == objectList || objectList.size() == 0) {
                                                lstV.setResultSize(1);
                                            }
                                            if (null != objectList && objectList.size() > 0) {
                                                lstV.setResultSize(objectList.size());
                                                rongZiDatas.addAll(objectList);
                                                rongZiCommonAdapter.notifyDataSetChanged();
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        lstV = ((AutoListView) view.findViewById(R.id.lstv));
        lstV.setPageSize(Constants.pageSize);
        rongZiCommonAdapter = new CommonAdapter<RongZi>(AppApplication.getSingleContext(), rongZiDatas, R.layout.fragment_artistcheck) {

            @Override
            public void convert(final ViewHolder helper, final RongZi item) {
                if (item != null) {
                    helper.setImageByUrl(R.id.fac_si_picture,item.getPicture_url());
                    helper.setText(R.id.fac_tv_title,item.getTitle());
                    helper.setText(R.id.fac_tv_money,"￥"+item.getInvestGoalMoney()+"");
                    if (item.getBrief()!=null){
                        helper.setText(R.id.fac_tv_brief,item.getBrief());
                    }else {
                        helper.setText(R.id.fac_tv_brief,"暂无简介！");
                    }
                    if ("100".equals(item.getStep())){
                        helper.setText(R.id.fac_tv_state,"未审核");
                        helper.getView(R.id.fac_rl_button).setVisibility(View.VISIBLE);
                        ((TextView) helper.getView(R.id.fac_tv_commitCheck)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                submitArtwork(item.getId(), "10");
                            }
                        });
                        helper.getView(R.id.fac_tv_edit).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getActivity(), EditProject01Activity.class);
                                intent.putExtra("artWorkId",item.getId());
                                intent.putExtra("currentUserId",AppApplication.gUser.getId());
                                getActivity().startActivity(intent);
                            }
                        });
                    }else {
                        helper.setText(R.id.fac_tv_state,"审核中");
                        helper.getView(R.id.fac_rl_button).setVisibility(View.GONE);
                    }
                }
            }
        };
        lstV.setAdapter(rongZiCommonAdapter);
        lstV.setOnRefreshListener(this);
        lstV.setOnLoadListener(this);
        return view;
    }
    private void submitArtwork(String id, String s) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artworkId", id);
        paramsMap.put("userId",AppApplication.gUser.getId());
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
                ToastUtil.showLong(getActivity(),"网络连接超时,稍后重试!");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                rongZiDatas.clear();
                currentPage = 1;
                loadData(AutoListView.REFRESH,currentPage);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void lazyLoad() {
        if (rongZiDatas != null && rongZiDatas.size() > 0) return;
        loadData(AutoListView.REFRESH, currentPage);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        loadData(AutoListView.REFRESH, currentPage);
    }

    @Override
    public void onLoad() {
        currentPage++;
        loadData(AutoListView.LOAD, currentPage);
    }
}