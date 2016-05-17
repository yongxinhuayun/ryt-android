package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.activity.ProjectCommentReply;
import com.yxh.ryt.activity.RegisterActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.vo.ArtworkComment;
import com.yxh.ryt.vo.ArtworkInvest;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import wuhj.com.mylibrary.PlaceHoderHeaderLayout;
import wuhj.com.mylibrary.StickHeaderViewPagerManager;


/**
 * Created by sj on 15/11/25.
 */
@SuppressLint("ValidFragment")
public class RongZiXiangQingTab03Fragment extends StickHeaderBaseFragment {
    private static String artWorkId;
    private ListView mListview;
    private CommonAdapter<ArtworkComment> artCommentAdapter;
    private List<ArtworkComment> artCommentDatas;
    private int currentPage=1;
    private View footer;
    private TextView loadFull;
    private TextView noData;
    private TextView more;
    private ProgressBar loading;
    private int lastItem;
    private boolean loadComplete=true;
    static StickHeaderViewPagerManager stickHeaderViewPagerManager;
    public RongZiXiangQingTab03Fragment(StickHeaderViewPagerManager manager, int position) {
        super(manager, position);
    }

    public RongZiXiangQingTab03Fragment(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh) {
        super(manager, position, isCanPulltoRefresh);
    }

    public static RongZiXiangQingTab03Fragment newInstance(StickHeaderViewPagerManager manager, int position) {
        RongZiXiangQingTab03Fragment listFragment = new RongZiXiangQingTab03Fragment(manager, position);
        return listFragment;
    }

    public static RongZiXiangQingTab03Fragment newInstance(StickHeaderViewPagerManager manager, int position, boolean isCanPulltoRefresh,String id) {
        RongZiXiangQingTab03Fragment listFragment = new RongZiXiangQingTab03Fragment(manager, position, isCanPulltoRefresh);
        artWorkId=id;
        stickHeaderViewPagerManager=manager;
        return listFragment;
    }
    public RongZiXiangQingTab03Fragment(){
        super();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artCommentDatas=new ArrayList<>();
    }

    @Override
    public View oncreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        mListview = (ListView)view.findViewById(R.id.v_scroll);
        footer = LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer, null);
        placeHoderHeaderLayout = (PlaceHoderHeaderLayout) view.findViewById(R.id.v_placehoder);
        setAdapter();
        onScroll();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        artCommentDatas.clear();
        LoadData(true, currentPage);
    }

    private void setAdapter() {
        artCommentAdapter=new CommonAdapter<ArtworkComment>(getActivity(),artCommentDatas,R.layout.pdonclicktab_comment_item) {
            @Override
            public void convert(ViewHolder helper, final ArtworkComment item) {
                TextView user=helper.getView(R.id.pdctci_tv_nickName);
                LinearLayout linearLayout=helper.getView(R.id.pdctci_ll_all);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(AppApplication.getSingleContext(), ProjectCommentReply.class);
                        intent.putExtra("name", item.getCreator().getName());
                        intent.putExtra("fatherCommentId",item.getId());
                        intent.putExtra("artworkId",artWorkId);
                        intent.putExtra("flag", 0);
                        intent.putExtra("messageId","");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        AppApplication.getSingleContext().startActivity(intent);
                    }
                });
                user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(AppApplication.getSingleContext(), RegisterActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        AppApplication.getSingleContext().startActivity(intent);
                    }
                });
                helper.setText(R.id.pdctci_tv_nickName, item.getCreator().getName());
                helper.setImageByUrl(R.id.pdctci_iv_icon, item.getCreator().getPictureUrl());
                helper.setText(R.id.pdctci_tv_date, Utils.timeTransComment1(item.getCreateDatetime()));
                if (item.getFatherComment()!=null){
                    TextView textView=helper.getView(R.id.pdctci_tv_content);
                    String fatherUser = item.getFatherComment().getCreator().getName();
                    SpannableString spanFatherUser = new SpannableString(fatherUser);
                    ClickableSpan click= new ShuoMClickableSpan(fatherUser, AppApplication.getSingleContext());
                    spanFatherUser.setSpan(click, 0, fatherUser.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    textView.setText("回复");
                    textView.append(spanFatherUser);
                    textView.append(":");
                    textView.append(item.getContent());
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                }else {
                    helper.setText(R.id.pdctci_tv_content,item.getContent());
                }
            }
        };
        mListview.setAdapter(artCommentAdapter);
        mListview.addFooterView(footer);
        loadFull = (TextView) footer.findViewById(R.id.loadFull);
        noData = (TextView) footer.findViewById(R.id.noData);
        more = (TextView) footer.findViewById(R.id.more);
        loading = (ProgressBar) footer.findViewById(R.id.loading);
        more.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        loadFull.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
    }


    public class ShuoMClickableSpan extends ClickableSpan {

        String string;
        Context context;
        public ShuoMClickableSpan(String str,Context context){
            super();
            this.string = str;
            this.context = context;
        }


        @Override
        public void updateDrawState(TextPaint ds) {

            ds.setColor(Color.rgb(7,99,198));
        }


        @Override
        public void onClick(View widget) {
            Intent intent=new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
    private void onScroll() {
        stickHeaderViewPagerManager.setOnListViewScrollListener(new StickHeaderViewPagerManager.OnListViewScrollListener() {
            @Override
            public void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 2;
            }

            @Override
            public void onListViewScrollStateChanged(AbsListView view, int scrollState) {
                if (lastItem == artCommentAdapter.getCount() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && loadComplete) {
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
    private void LoadData(final boolean flag,int pageNum) {
        more.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        loadFull.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("artWorkId",artWorkId);
        paramsMap.put("pageSize", Constants.pageSize+"");
        paramsMap.put("pageIndex", pageNum + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg= EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkComment.do", paramsMap, new RZCommentCallBack() {
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
                        List<ArtworkComment> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkCommentList")), new TypeToken<List<ArtworkComment>>() {
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
                            artCommentDatas.addAll(commentList);
                            commentList.clear();
                            artCommentAdapter.notifyDataSetChanged();
                        }else {
                            more.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            loadFull.setVisibility(View.GONE);
                            noData.setVisibility(View.GONE);
                        }
                        if (commentList != null) {
                            artCommentDatas.addAll(commentList);
                            commentList.clear();
                        }
                        artCommentAdapter.notifyDataSetChanged();
                    }else {
                        List<ArtworkComment> commentList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkCommentList")), new TypeToken<List<ArtworkComment>>() {
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
                            artCommentDatas.addAll(commentList);
                            commentList.clear();
                        }
                        artCommentAdapter.notifyDataSetChanged();
                    }

                }
            }
        });
    }
    @Override
    protected void lazyLoad() {
        Log.d("oncreateView","lazyLoadlazyLoadlazyLoadlazyLoadlazyLoadlazyLoadlazyLoadlazyLoadlazyLoadlazyLoad");
    }
}
