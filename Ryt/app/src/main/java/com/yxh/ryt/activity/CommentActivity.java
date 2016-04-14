package com.yxh.ryt.activity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.Constants;
import com.yxh.ryt.R;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.CommentCallBack;
import com.yxh.ryt.custemview.AutoListView;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.vo.ArtworkComment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/4/9.
 */
public class CommentActivity extends BaseActivity implements AutoListView.OnLoadListener, AutoListView.OnRefreshListener {
    private CommonAdapter<ArtworkComment> cmAdapter;
    private List<ArtworkComment> artworkCommentDatas;
    private int currentPage=1;
    @Bind(R.id.pl_message_listView)
    AutoListView cmlistview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_listview);
        ButterKnife.bind(this);/*启用注解绑定*/
        artworkCommentDatas =new ArrayList<ArtworkComment>();
        initView();
        cmlistview.setPageSize(Constants.pageSize);
        LoadData(AutoListView.REFRESH, currentPage);
    }

    private void initView() {
        cmAdapter=new CommonAdapter<ArtworkComment>(AppApplication.getSingleContext(), artworkCommentDatas,R.layout.comment_item) {
            @Override
            public void convert(ViewHolder helper, ArtworkComment item) {
                Log.d("ArtworkComment", item.toString());
                /*if (item.getIsWatch()==0){
                    helper.setColor(R.id.ni_ll_top, Color.RED);
                }
                helper.setText(R.id.ni_tv_content,item.getContent());
                helper.setText(R.id.ni_tv_date, Utils.timeTrans(item.getCreateDatetime()));
                if (!(item.getFromUser()==null)){
                    helper.setVisible(R.id.ni_iv_prijectIcon);
                    helper.setVisible(R.id.ni_tv_Projectcontent);
                    helper.setText(R.id.ni_tv_Projectcontent, item.getFromUser().getUserName() + "");
                }*/
            }
        };
        cmlistview.setAdapter(cmAdapter);
        cmlistview.setOnLoadListener(this);
        cmlistview.setOnRefreshListener(this);
    }

    private void LoadData(final int state,int pageNum) {
        Map<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userId","iijq9f1r7apprtab");
        paramsMap.put("type","1");
        paramsMap.put("pageSize",Constants.pageSize+"");
        paramsMap.put("pageNum", pageNum+"");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            paramsMap.put("signmsg", EncryptUtil.encrypt(paramsMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "information.do", paramsMap, new CommentCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                Log.d("response",response.toString());
               if (state == AutoListView.REFRESH) {
                   cmlistview.onRefreshComplete();
                   artworkCommentDatas.clear();
                    List<ArtworkComment> ArtworkComment = null;
                    try {
                        ArtworkComment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<ArtworkComment>>() {
                        }.getType());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    if (null == ArtworkComment || ArtworkComment.size() == 0) {
                        cmlistview.setResultSize(0);
                    }
                    if (null != ArtworkComment && ArtworkComment.size() > 0) {
                        cmlistview.setResultSize(ArtworkComment.size());
                        artworkCommentDatas.addAll(ArtworkComment);
                        cmAdapter.notifyDataSetChanged();
                    }
                    return;
                }
                if (state == AutoListView.LOAD) {
                    cmlistview.onLoadComplete();
                    List<ArtworkComment> ArtworkComment = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(response.get("objectList")), new TypeToken<List<ArtworkComment>>() {
                    }.getType());
                    if (null == ArtworkComment || ArtworkComment.size() == 0) {
                        cmlistview.setResultSize(1);
                    }
                    if (null != ArtworkComment && ArtworkComment.size() > 0) {
                        cmlistview.setResultSize(ArtworkComment.size());
                        artworkCommentDatas.addAll(ArtworkComment);
                        cmAdapter.notifyDataSetChanged();
                    }
                    return;
                }
            }
        });
    }

    @Override
    public void onLoad() {
        currentPage++;
        LoadData(AutoListView.LOAD,currentPage);
    }

    @Override
    public void onRefresh() {
        currentPage=1;
        LoadData(AutoListView.REFRESH,currentPage);
    }
}
