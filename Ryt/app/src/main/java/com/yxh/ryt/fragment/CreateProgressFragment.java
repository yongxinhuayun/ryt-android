package com.yxh.ryt.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yxh.ryt.AppApplication;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.BaseActivity;
import com.yxh.ryt.activity.CreateSummaryActivity;
import com.yxh.ryt.adapter.circledemoadapter.CircleAdapter;
import com.yxh.ryt.bean.CircleItem;
import com.yxh.ryt.bean.CommentItem;
import com.yxh.ryt.custemview.OnMoreListener;
import com.yxh.ryt.custemview.SuperRecyclerView;
import com.yxh.ryt.custemview.SuperSwipeRefreshLayout;
import com.yxh.ryt.custemview.SuperSwipeRefreshLayout.OnPullRefreshListener;
import com.yxh.ryt.mvp.contract.CircleContract;
import com.yxh.ryt.mvp.presenter.CirclePresenter;
import com.yxh.ryt.util.utils.CommonUtils;
import com.yxh.ryt.vo.ArtWorkPraise;
import com.yxh.ryt.vo.ArtworkComment;
import com.yxh.ryt.vo.ArtworkMessage;
import com.yxh.ryt.vo.CommentConfig;
import com.yxh.ryt.widgets.CommentListView;
import com.yxh.ryt.widgets.DivItemDecoration;
import com.yxh.ryt.widgets.TitleBar;
import com.yxh.ryt.widgets.dialog.UpLoadDialog;

import java.util.List;


public class CreateProgressFragment extends BaseFragment implements CircleContract.View {
    protected static final String TAG = AppApplication.getSingleContext().getClass().getSimpleName();
    private CircleAdapter circleAdapter;
    private LinearLayout edittextbody;
    private EditText editText;
    private ImageView sendIv;

    private int screenHeight;
    private int editTextBodyHeight;
    private int currentKeyboardH;
    private int selectCircleItemH;
    private int selectCommentItemOffset;

    private CirclePresenter presenter;
    private CommentConfig commentConfig;
    private SuperRecyclerView recyclerView;
    private RelativeLayout bodyLayout;
    private LinearLayoutManager layoutManager;
    private TitleBar titleBar;

    // Header View
    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;
    private TextView lastUpdate;

    protected SuperSwipeRefreshLayout mPtrLayout;

    private final static int TYPE_PULLREFRESH = 1;
    private final static int TYPE_UPLOADREFRESH = 2;
    private UpLoadDialog uploadDialog;
    private BaseActivity activity;
    private String artWorkId;
    private String name;
    private String picUrl;
    private ArtworkMessage artworkMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.layout_createprogress, container, false);
        mPtrLayout = (SuperSwipeRefreshLayout) contextView.findViewById(R.id.ptr_layout);
        mPtrLayout.setHeaderView(createHeaderView());// add headerView
        //mPtrLayout.setTargetScrollWithLayout(true);
        recyclerView = (SuperRecyclerView) contextView.findViewById(R.id.recyclerView);
        edittextbody = (LinearLayout) contextView.findViewById(R.id.editTextBodyLl);
        editText = (EditText) contextView.findViewById(R.id.circleEt);
        sendIv = (ImageView) contextView.findViewById(R.id.sendIv);
        bodyLayout = (RelativeLayout) contextView.findViewById(R.id.bodyLayout);
        presenter = new CirclePresenter(this, artWorkId);
        initView();
        presenter.loadData(artWorkId, TYPE_PULLREFRESH);

        return contextView;
    }

    private View createHeaderView() {
        View headerView = LayoutInflater.from(mPtrLayout.getContext())
                .inflate(R.layout.pull_to_refresh_header, null);
        progressBar = (ProgressBar) headerView.findViewById(R.id.refreshing);
        textView = (TextView) headerView.findViewById(R.id.tip);
        textView.setText("下拉可以刷新");
        imageView = (ImageView) headerView.findViewById(R.id.arrow);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.mipmap.refresh_arrow);
        progressBar.setVisibility(View.GONE);
        return headerView;
    }


    private void initView() {
        //initTitle();
        initUploadDialog();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DivItemDecoration(0, true));
        recyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        //下拉刷新
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edittextbody.getVisibility() == View.VISIBLE) {
                    updateEditTextBodyVisible(View.GONE, null,artworkMessage);
                    return true;
                }
                return false;
            }
        });

        recyclerView.setRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                textView.setText("正在刷新");
                imageView.clearAnimation();
                imageView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.loadData(artWorkId, TYPE_PULLREFRESH);
                        recyclerView.setRefreshing(false);
                        mPtrLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                    }
                }, 2000);
            }

            @Override
            public void onPullDistance(int distance) {
//TODO 下拉距离
            }

            @Override
            public void onPullEnable(boolean enable) {
                //TODO 下拉过程中，下拉的距离是否足够出发刷新
                // 设置箭头特效
                RotateAnimation animation = new RotateAnimation(0f, -180f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(300);
                animation.setFillAfter(true);

                RotateAnimation reverseAnimation = new RotateAnimation(-180f, -360f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                reverseAnimation.setDuration(300);
                reverseAnimation.setFillAfter(true);


                textView.setText(enable ? "松开立即刷新" : "下拉可以刷新");
                imageView.setVisibility(View.VISIBLE);
                if (enable) {
                    imageView.clearAnimation();
                    imageView.setAnimation(animation);
                    //imageView.startAnimation(animation);
                }else {
                    imageView.clearAnimation();
                    imageView.setAnimation(reverseAnimation);
                    //imageView.startAnimation(reverseAnimation);
                }
                //imageView.setRotation(enable ? 180 : 0);

            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Glide.with(getActivity()).resumeRequests();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState != RecyclerView.SCROLL_STATE_IDLE){
                    Glide.with(getActivity()).pauseRequests();
                }

            }
        });


        circleAdapter = new CircleAdapter(getActivity(),name,picUrl);
        circleAdapter.setCirclePresenter(presenter);
        recyclerView.setAdapter(circleAdapter);


        sendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    //发布评论
                    String content =  editText.getText().toString().trim();
                    if(TextUtils.isEmpty(content)){
                        Toast.makeText(getActivity(), "评论内容不能为空...", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    presenter.addComment(content, artworkMessage,commentConfig);
                }
                updateEditTextBodyVisible(View.GONE, null,artworkMessage);
            }
        });

        setViewTreeObserver();
    }
    private void initUploadDialog() {
        uploadDialog = new UpLoadDialog(getActivity());
    }

    /*private void initTitle() {

        titleBar = (TitleBar) findViewById(R.id.main_title_bar);
        titleBar.setTitle("朋友圈");
        titleBar.setTitleColor(getResources().getColor(R.color.white));
        titleBar.setBackgroundColor(getResources().getColor(R.color.title_bg));

        TextView textView = (TextView) titleBar.addAction(new TitleBar.TextAction("发布视频") {
            @Override
            public void performAction(View view) {
                //Toast.makeText(MainActivity.this, "敬请期待...", Toast.LENGTH_SHORT).show();

                QPManager.startRecordActivity(MainActivity.this);
            }
        });
        textView.setTextColor(getResources().getColor(R.color.white));
    }*/


    private void setViewTreeObserver() {
        final ViewTreeObserver swipeRefreshLayoutVTO = bodyLayout.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                bodyLayout.getWindowVisibleDisplayFrame(r);
                int statusBarH =  getStatusBarHeight();//状态栏高度
                int screenH = bodyLayout.getRootView().getHeight();
                if(r.top != statusBarH ){
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                Log.d(TAG, "screenH＝ "+ screenH +" &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);

                if(keyboardH == currentKeyboardH){//有变化时才处理，否则会陷入死循环
                    return;
                }

                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = edittextbody.getHeight();

                if(keyboardH<150){//说明是隐藏键盘的情况
                    updateEditTextBodyVisible(View.GONE, null,artworkMessage);
                    return;
                }
                //偏移listview
                if(layoutManager!=null && commentConfig != null){
                    layoutManager.scrollToPositionWithOffset(commentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE, getListviewOffset(commentConfig));
                }
            }
        });
    }

    /**
     * 获取状态栏高度
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(edittextbody != null && edittextbody.getVisibility() == View.VISIBLE){
                //edittextbody.setVisibility(View.GONE);
                updateEditTextBodyVisible(View.GONE, null);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public void update2DeleteCircle(String circleId) {
        List<CircleItem> circleItems = circleAdapter.getDatas();
        for(int i=0; i<circleItems.size(); i++){
            if(circleId.equals(circleItems.get(i).getId())){
                circleItems.remove(i);
                circleAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    /**点赞
     *List<ArtWorkPraise> favortDatas = artworkMessage.getArtWorkPraiseList();
     * @param circlePosition
     * @param addItem
     */
    @Override
    public void update2AddFavorite(int circlePosition, ArtWorkPraise addItem) {
        if(addItem != null){
            ArtworkMessage artworkMessage = (ArtworkMessage) circleAdapter.getDatas().get(circlePosition);
            artworkMessage.getArtWorkPraiseList().add(addItem);
            circleAdapter.notifyDataSetChanged();
            //circleAdapter.notifyItemChanged(circlePosition+1);
        }
    }

    /**
     * 取消赞
     * @param circlePosition
     * @param favortId
     */
    @Override
    public void update2DeleteFavort(int circlePosition, String favortId) {
        ArtworkMessage artworkMessage = (ArtworkMessage) circleAdapter.getDatas().get(circlePosition);
        List<ArtWorkPraise> items = artworkMessage.getArtWorkPraiseList();
        for(int i=0; i<items.size(); i++){
            if(favortId.equals(items.get(i).getId())){
                items.remove(i);
                circleAdapter.notifyDataSetChanged();
                //circleAdapter.notifyItemChanged(circlePosition+1);
                return;
            }
        }
    }

    @Override
    public void update2AddComment(int circlePosition, ArtworkComment addItem) {
        if(addItem != null){
            ArtworkMessage item =  (ArtworkMessage) circleAdapter.getDatas().get(circlePosition);
            item.getArtworkCommentList().add(addItem);
            circleAdapter.notifyDataSetChanged();
            //circleAdapter.notifyItemChanged(circlePosition+1);
        }
        //清空评论文本
        editText.setText("");
    }

    @Override
    public void update2DeleteComment(int circlePosition, String commentId) {
        CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
        List<CommentItem> items = item.getComments();
        for(int i=0; i<items.size(); i++){
            if(commentId.equals(items.get(i).getId())){
                items.remove(i);
                circleAdapter.notifyDataSetChanged();
                //circleAdapter.notifyItemChanged(circlePosition+1);
                return;
            }
        }
    }

    @Override
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig, ArtworkMessage artworkMessage) {
        this.commentConfig = commentConfig;
        this.artworkMessage = artworkMessage;
        edittextbody.setVisibility(visibility);

        measureCircleItemHighAndCommentItemOffset(commentConfig);

        if(View.VISIBLE==visibility){
            editText.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput( editText.getContext(),  editText);

        }else if(View.GONE==visibility){
            //隐藏键盘
            CommonUtils.hideSoftInput( editText.getContext(),  editText);
        }
    }


    @Override
    public void update2loadData(int loadType, List<ArtworkMessage> datas) {
        if (loadType == TYPE_PULLREFRESH){
            circleAdapter.setDatas(datas);
        }else if(loadType == TYPE_UPLOADREFRESH){
            circleAdapter.getDatas().addAll(datas);
        }
        circleAdapter.notifyDataSetChanged();

        if(circleAdapter.getDatas().size()<45 + CircleAdapter.HEADVIEW_SIZE){
            recyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            presenter.loadData(artWorkId, TYPE_UPLOADREFRESH);
                        }
                    }, 2000);

                }
            }, 1);
        }else{
            recyclerView.removeMoreListener();
            recyclerView.hideMoreProgress();
        }

    }


    /**
     * 测量偏移量
     * @param commentConfig
     * @return
     */
    private int getListviewOffset(CommentConfig commentConfig) {
        if(commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - titleBar.getHeight();
        if(commentConfig.commentType == CommentConfig.Type.REPLY){
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset;
        }
        Log.i(TAG, "listviewOffset : " + listviewOffset);
        return listviewOffset;
    }

    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig){
        if(commentConfig == null)
            return;

        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = layoutManager.getChildAt(commentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE - firstPosition);

        if(selectCircleItem != null){
            selectCircleItemH = selectCircleItem.getHeight();
        }

        if(commentConfig.commentType == CommentConfig.Type.REPLY){
            //回复评论的情况
            CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
            if(commentLv!=null){
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                if(selectCommentItem != null){
                    //选择的commentItem距选择的CircleItem底部的距离
                    selectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if(parentView != null){
                            selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        }
    }


    String videoFile;
    String [] thum;
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            RecordResult result =new RecordResult(data);
            //得到视频地址，和缩略图地址的数组，返回十张缩略图
            videoFile = result.getPath();
            thum = result.getThumbnail();
            result.getDuration();

            Log.e(TAG, "视频路径:" + videoFile + "图片路径:" + thum[0]);

            QPManager.getInstance(getApplicationContext()).startUpload(videoFile, thum[0], new IUploadListener() {
                @Override
                public void preUpload() {
                    uploadDialog.show();
                }

                @Override
                public void uploadComplet(String videoUrl, String imageUrl, String message) {
                    uploadDialog.hide();
                    Toast.makeText(MainActivity.this, "上传成功...", Toast.LENGTH_LONG).show();

                    //将新拍摄的video刷新到列表中
                    circleAdapter.getDatas().add(0, DatasUtil.createVideoItem(videoFile, thum[0]));
                    circleAdapter.notifyDataSetChanged();
                }

                @Override
                public void uploadError(int errorCode, final String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            uploadDialog.hide();
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void uploadProgress(int percentsProgress) {
                    uploadDialog.setPercentsProgress(percentsProgress);
                }
            });

            *//**
             * 清除草稿,草稿文件将会删除。所以在这之前我们执行拷贝move操作。
             * 上面的拷贝操作请自行实现，第一版本的copyVideoFile接口不再使用
             *//*
            *//*QupaiService qupaiService = QupaiManager
                    .getQupaiService(MainActivity.this);
            qupaiService.deleteDraft(getApplicationContext(),data);*//*

        } else {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "RESULT_CANCELED", Toast.LENGTH_LONG).show();
            }
        }
    }*/

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String errorMsg) {

    }
    @Override
    public void onDestroy() {
        if(presenter !=null){
            presenter.recycle();
        }
        super.onDestroy();
    }
    @Override
    protected void lazyLoad() {

    }


    @SuppressLint("ValidFragment")
    public CreateProgressFragment(String id,String name, String picUrl, CreateSummaryActivity createSummaryActivity) {
        this.picUrl = picUrl;
        this.name = name;
        this.artWorkId = id;
        this.activity=createSummaryActivity;
    }

}