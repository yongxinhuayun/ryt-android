package com.yxh.ryt.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.webkit.WebView;
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
import com.yxh.ryt.activity.AttentionActivity;
import com.yxh.ryt.activity.LoginActivity;
import com.yxh.ryt.activity.ProjectCommentReply;
import com.yxh.ryt.activity.UserPtIndexActivity;
import com.yxh.ryt.activity.UserYsjIndexActivity;
import com.yxh.ryt.adapter.CommonAdapter;
import com.yxh.ryt.adapter.ViewHolder;
import com.yxh.ryt.callback.RZCommentCallBack;
import com.yxh.ryt.callback.RongZiListCallBack;
import com.yxh.ryt.custemview.CircleImageView;
import com.yxh.ryt.custemview.ExpandView;
import com.yxh.ryt.custemview.ListViewForScrollView;
import com.yxh.ryt.custemview.RoundProgressBar;
import com.yxh.ryt.util.DateUtil;
import com.yxh.ryt.util.EncryptUtil;
import com.yxh.ryt.util.NetRequestUtil;
import com.yxh.ryt.util.ShuoMClickableSpan;
import com.yxh.ryt.util.ToastUtil;
import com.yxh.ryt.util.Utils;
import com.yxh.ryt.util.avalidations.ValidationModel;
import com.yxh.ryt.validations.ContentValidation;
import com.yxh.ryt.vo.ArtWorkPraiseList;
import com.yxh.ryt.vo.Artwork;
import com.yxh.ryt.vo.ArtworkComment;
import com.yxh.ryt.vo.ArtworkInvest;
import com.yxh.ryt.vo.User;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class RZProjectFragment extends BaseFragment implements View.OnClickListener {

    private String artWorkId;
    private ListViewForScrollView mListview;
    private ListViewForScrollView iListview;
    private CommonAdapter<ArtworkComment> artCommentAdapter;
    private List<ArtworkComment> artCommentDatas;
    private int currentPage = 1;
    private List<User> users;
    private List<ArtWorkPraiseList> artWorkPraiseList;
    private ImageView imageTitle;
    private ImageView dianzan;
    private boolean flag1 = true;
    private boolean isPraise1;
    private LinearLayout ll_invester;
    private TextView tv_project_name;
    private TextView tv_project_brief;
    private TextView tv_price;
    private TextView tv_name;
    private TextView tv_name2;
    private TextView praiseNum;
    private TextView deadline;
    private TextView tvProgress;
    private CircleImageView cl_headPortrait;
    private CommonAdapter<ArtworkInvest> investorRecordCommonAdapter;
    private List<ArtworkInvest> investorDatas;
    private LinearLayout llpraise;
    private LinearLayout llinvester;
    private ScrollView sv;
    protected static final int PRAISE_SUC = 100;
    private int widthScreen;
    private int widthImage;
    private int widthView;
    private int heightIamge;
    private int right;
    private int count;
    private int time = 0;
    private int padding;
    private ImageButton go;
    private RoundProgressBar mRoundProgressBar;
    private int progress = 0;
    private int max = 0;
    private int screenWidth;
    private int screenHeight;
    private Rect rect;
    private double progressCurrent;
    private ImageView headV;
    private ImageView iv_show;
    private WebView webView;
    private boolean isShow;
    private Button comment;
    private EditText etComment;
    private ImageButton attention;
    private LinearLayout llprogress;
    private ExpandView mExpandView;
    private LinearLayout ll_project;
    private RelativeLayout rl_progress;
    private TextView worksNum;
    private TextView fansNum;
    private Artwork artwork;
    private TextView reader;

    public RZProjectFragment(String artWorkId) {
        super();
        this.artWorkId = artWorkId;
    }

    private final Handler mHandler = new Handler();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PRAISE_SUC:
                    int a = Integer.parseInt(praiseNum.getText().toString());
                    a++;
                    praiseNum.setText(a + "");
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artCommentDatas = new ArrayList<ArtworkComment>();
        investorDatas = new ArrayList<>();
        showOther();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rz_project, null);
        imageTitle = (ImageView) view.findViewById(R.id.cl_01_tv_prc);
        dianzan = (ImageView) view.findViewById(R.id.iv_praise);
        headV = (ImageView) view.findViewById(R.id.iv_master);
        ll_invester = (LinearLayout) view.findViewById(R.id.ll_invester);
        ll_project = (LinearLayout) view.findViewById(R.id.ll_project);
        rl_progress = (RelativeLayout) view.findViewById(R.id.rl_progress);
        tv_project_name = (TextView) view.findViewById(R.id.tv_project_name);
        deadline = (TextView) view.findViewById(R.id.tv_deadline);
        tvProgress = (TextView) view.findViewById(R.id.tv_progress);
        reader = (TextView) view.findViewById(R.id.tv_reader);
        tv_project_brief = (TextView) view.findViewById(R.id.tv_project_brief);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_name2 = (TextView) view.findViewById(R.id.tv_name2);
        praiseNum = (TextView) view.findViewById(R.id.tv_praise);
        worksNum = (TextView) view.findViewById(R.id.tv_num_works);
        fansNum = (TextView) view.findViewById(R.id.tv_num_fans);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        go = (ImageButton) view.findViewById(R.id.ib_go);
        comment = (Button) view.findViewById(R.id.bt_comment);
        etComment = (EditText) view.findViewById(R.id.et_comment);
        attention = (ImageButton) view.findViewById(R.id.ib_attention);
        attention.setOnClickListener(this);
        comment.setOnClickListener(this);
        llpraise = (LinearLayout) view.findViewById(R.id.ll_prise);
        //llprogress = (LinearLayout) view.findViewById(R.id.ll_progress);
        llinvester = (LinearLayout) view.findViewById(R.id.ll_invester);
        mExpandView = (ExpandView) view.findViewById(R.id.expandView);
        sv = (ScrollView) view.findViewById(R.id.sv_sv);
        mRoundProgressBar = (RoundProgressBar) view.findViewById(R.id.rpb_progress);
        iv_show = (ImageView) view.findViewById(R.id.iv_is_show);
        iv_show.setOnClickListener(this);
        mRoundProgressBar.setTextSize(28);
        cl_headPortrait = (CircleImageView) view.findViewById(R.id.cl_headPortrait);
        //webView = (WebView) view.findViewById(R.id.wv_invest_process);
        //webView.loadUrl("file:///android_asset/InvestFlowControlller.html");
        mExpandView.collapse();
        for (int i = 0; i < mExpandView.getChildCount(); i++) {
            mExpandView.getChildAt(i).setVisibility(View.GONE);
        }

        imageTitle.setFocusable(true);
        imageTitle.setFocusableInTouchMode(true);
        imageTitle.requestFocus();
        llpraise.setOnClickListener(this);
        go.setOnClickListener(this);
        mListview = (ListViewForScrollView) view.findViewById(R.id.lv_comment);
        iListview = (ListViewForScrollView) view.findViewById(R.id.lv_invester);

        //点赞头像展示
        showHeadNum();
        loadInvesterData(true, currentPage);
        loadCommentData(true, currentPage);
        return view;
    }

    private void showHeadNum() {
        //测量箭头的宽高
        go.measure(0, 0);
        int heightArrow = go.getMeasuredHeight();
        int widthArrow = go.getMeasuredWidth();
        //测量prise宽高
        llpraise.measure(0, 0);
        int heightPraise = llpraise.getMeasuredHeight();
        int widthPraise = llpraise.getMeasuredWidth();
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        widthScreen = metric.widthPixels;
        //图片宽高
        widthImage = Utils.dip2px(getActivity(), 24);
        heightIamge = Utils.dip2px(getActivity(), 24);
        //图片padding
        right = Utils.dip2px(getActivity(), 10);
        //一行能容纳的图片个数
        //praise与view间距
        padding = Utils.dip2px(getActivity(), 30);
        widthView = widthScreen - widthPraise - widthArrow - padding;
        count = widthView / (widthImage + right);
    }

    private void setCommentAdapter() {
        artCommentAdapter = new CommonAdapter<ArtworkComment>(getActivity(), artCommentDatas, R.layout.pdonclicktab_comment_item) {
            @Override
            public void convert(ViewHolder helper, final ArtworkComment item) {
                LinearLayout linearLayout = helper.getView(R.id.pdctci_ll_all);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AppApplication.getSingleContext(), ProjectCommentReply.class);
                        if (item.getCreator() != null) {
                            intent.putExtra("name", item.getCreator().getName());
                        } else {
                            intent.putExtra("name", "");
                        }
                        intent.putExtra("currentUserId", AppApplication.gUser.getId());
                        intent.putExtra("fatherCommentId", item.getId());
                        intent.putExtra("artworkId", artWorkId);
                        intent.putExtra("flag", 0);
                        intent.putExtra("messageId", "");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (!item.getId().equals(AppApplication.gUser.getId())) {
                            AppApplication.getSingleContext().startActivity(intent);
                        }
                    }
                });
                helper.getView(R.id.pdctci_tv_nickName).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getCreator().getMaster() != null) {
                            Intent intent = new Intent(getActivity(), UserYsjIndexActivity.class);
                            intent.putExtra("userId", item.getCreator().getId());
                            intent.putExtra("currentId", AppApplication.gUser.getId());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), UserPtIndexActivity.class);
                            intent.putExtra("userId", item.getCreator().getId());
                            intent.putExtra("currentId", AppApplication.gUser.getId());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                        }
                    }
                });
                if (item.getCreator() != null) {
                    helper.setText(R.id.pdctci_tv_nickName, item.getCreator().getName());
                    helper.setImageByUrl(R.id.pdctci_iv_icon, item.getCreator().getPictureUrl());
                }
                helper.setText(R.id.pdctci_tv_date, long2Timestamp(item.getCreateDatetime()));
                if (item.getFatherComment() != null) {
                    TextView textView = helper.getView(R.id.pdctci_tv_content);
                    String fatherUser = item.getFatherComment().getCreator().getName();
                    SpannableString spanFatherUser = new SpannableString("@" + fatherUser);
                    ClickableSpan click = new ShuoMClickableSpan(fatherUser, AppApplication.getSingleContext()) {
                        @Override
                        public void onClick(View widget) {
                            if (item.getFatherComment().getCreator().getMaster() != null) {
                                Intent intent = new Intent(getActivity(), UserYsjIndexActivity.class);
                                intent.putExtra("userId", item.getFatherComment().getCreator().getId());
                                intent.putExtra("currentId", AppApplication.gUser.getId());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), UserPtIndexActivity.class);
                                intent.putExtra("userId", item.getFatherComment().getCreator().getId());
                                intent.putExtra("currentId", AppApplication.gUser.getId());
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().startActivity(intent);
                            }
                        }
                    };
                    spanFatherUser.setSpan(click, 0, fatherUser.length() + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    textView.setText("回复");
                    textView.append(spanFatherUser);
                    textView.append(":");
                    textView.append(item.getContent());
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    helper.setText(R.id.pdctci_tv_content, item.getContent());
                }
            }
        };
        mListview.setAdapter(artCommentAdapter);

    }

    private void loadCommentData(final boolean flag, int pageNum) {
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
                        if (commentList == null || commentList.size() == 0) {

                        } else if (commentList.size() < Constants.pageSize) {

                            loadNum5(mListview, artCommentDatas, commentList);
                            commentList.clear();
                        }

                        if (commentList != null) {
                            loadNum5(mListview, artCommentDatas, commentList);
                            commentList.clear();
                        }
                    } else {
                        List<ArtworkComment> commentList = AppApplication.getSingleGson().fromJson(
                                AppApplication.getSingleGson().toJson(object.get("artworkCommentList")),
                                new TypeToken<List<ArtworkComment>>() {
                                }.getType());
                        if (commentList != null) {
                            loadNum5(mListview, artCommentDatas, commentList);
                            commentList.clear();
                        }
                    }

                }
                setCommentAdapter();
            }
        });
    }

    private void showOther() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artWorkId", artWorkId);
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(paramsMap.toString() + "====");
        NetRequestUtil.post(Constants.BASE_PATH + "investorArtWorkView.do", paramsMap, new RongZiListCallBack() {


            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                Map<String, Object> object = (Map<String, Object>) response.get("object");
                if (object != null) {
                    artWorkPraiseList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().
                            toJson(object.get("artWorkPraiseList")), new TypeToken<List<ArtWorkPraiseList>>() {
                    }.getType());
                    if (artWorkPraiseList != null && artWorkPraiseList.size() > 0 && flag1) {
                        flag1 = false;
                        showInvesterHead(artWorkPraiseList);
                    }

                    isPraise1 = Boolean.parseBoolean(AppApplication.getSingleGson().toJson(object.get("isPraise")));
                    artwork =   AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artWork")), Artwork.class);
                    tv_project_name.setText(artwork.getTitle());
                    tv_project_brief.setText(artwork.getBrief());
                   // worksNum.setText(artwork.);
                    tv_price.setText("￥ " + artwork.getInvestsMoney() + ".00");
                    tvProgress.setText(artwork.getInvestNum() + "人投资 " + "￥" + artwork.getInvestsMoney() + "/" + artwork.getInvestGoalMoney());
                    deadline.setText(DateUtil.millisToStringShort(Long.parseLong(artwork.getInvestRestTime()),false,false) + "后截止");
                    //进度显示
                    progressCurrent = artwork.getInvestsMoney().doubleValue() / artwork.getInvestGoalMoney().doubleValue();
                    max = artwork.getInvestGoalMoney().intValue();
                    isVisibleBar();

                    if (artwork.getAuthor() != null) {
                        tv_name.setText(artwork.getAuthor().getName());
                        tv_name2.setText(artwork.getAuthor().getName());
                        AppApplication.displayImage(artwork.getAuthor().getPictureUrl(), cl_headPortrait);
                        if ("master".equals(artwork.getAuthor().getMaster1())) {
                            headV.setVisibility(View.VISIBLE);
                        } else if ("".equals(artwork.getAuthor().getMaster1())) {
                            headV.setVisibility(View.INVISIBLE);
                        }
                    }
                    if (isPraise1) {
                        dianzan.setImageResource(R.mipmap.after_praise);
                        dianzan.setEnabled(false);
                    }
                    praiseNum.setText(artwork.getPraiseNUm() + "");
                    reader.setText(artwork.getViewNUm() + "");
                    AppApplication.displayImage(artwork.getPicture_url(), imageTitle);
                }
            }
        });
    }

    private void isVisibleBar() {
        Point p = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(p);
        screenWidth = p.x;
        screenHeight = p.y;
        rect = new Rect(0, 0, screenWidth, screenHeight);
        int[] location = new int[2];
        mRoundProgressBar.getLocationInWindow(location);
        System.out.println(Arrays.toString(location));
        sv.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    if (mRoundProgressBar.getLocalVisibleRect(rect)) {/*rect.contains(ivRect)*/
                        if (progress == 0 && progressCurrent != 0) {
                            initProgressBar((int) (progressCurrent * 100));
                        }
                    } else {
                        progress = 0;
                    }

                } else if (event.getAction() == MotionEvent.ACTION_POINTER_UP) {

                    if (mRoundProgressBar.getLocalVisibleRect(rect)) {/*rect.contains(ivRect)*/
                        if (progress == 0) {
                            initProgressBar((int) (progressCurrent * 100));
                        }
                    } else {
                        progress = 0;
                    }

                }
                return false;
            }

        });


    }

    private void initProgressBar(final int temp) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (progress <= temp) {
                    progress += 1;
                    System.out.println(progress);
                    mRoundProgressBar.setProgress(progress);
                    try {
                        Thread.sleep(250 / temp);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void showInvesterHead(final List<ArtWorkPraiseList> artWorkPraiseList) {
        if (artWorkPraiseList.size() > count) {
            final LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < count; j++) {
                final int temp = j;
                final CircleImageView imageView = new CircleImageView(getActivity());
                // 获取LayoutParams，给view对象设置宽度，高度
                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthImage, heightIamge);
                params.setMargins(0, 0, right, 0);
                NetRequestUtil.downloadImage(artWorkPraiseList.get(j).getUser().getPictureUrl(), new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                        imageView.setLayoutParams(params);
                        linearLayout.addView(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clickHead(temp);
                            }
                        });
                    }
                });

            }
            llinvester.addView(linearLayout);
        } else {
            final LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < artWorkPraiseList.size(); j++) {
                User user = artWorkPraiseList.get(j).getUser();

                final CircleImageView imageView = new CircleImageView(getActivity());
                // 获取LayoutParams，给view对象设置宽度，高度
                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthImage, heightIamge);
                params.setMargins(0, 0, right, 0);
                final int temp = j;
                NetRequestUtil.downloadImage(artWorkPraiseList.get(j).getUser().getPictureUrl(), new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                        imageView.setLayoutParams(params);
                        linearLayout.addView(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clickHead(temp);
                            }
                        });
                    }
                });

            }
            llinvester.addView(linearLayout);
        }
    }


    private void clickHead(int temp) {
        if (artWorkPraiseList.get(temp).getUser().getMaster() != null) {
            Intent intent = new Intent(getActivity(), UserYsjIndexActivity.class);
            intent.putExtra("userId", artWorkPraiseList.get(temp).getUser().getId());
            intent.putExtra("currentId", AppApplication.gUser.getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), UserPtIndexActivity.class);
            intent.putExtra("userId", artWorkPraiseList.get(temp).getUser().getId());
            intent.putExtra("currentId", AppApplication.gUser.getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        artCommentDatas.clear();
        currentPage = 1;
    }

    @Override
    protected void lazyLoad() {

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
                helper.setText(R.id.iri_tv_date, long2Timestamp(item.getCreateDatetime()));
            }
        };
        iListview.setAdapter(investorRecordCommonAdapter);
    }

    private String long2Timestamp(long time) {
        String sTime = DateUtil.date2String(time, "yyyy-MM-dd  HH:mm:ss");
        Date dt = DateUtil.string2Date(sTime, "yyyy-MM-dd  HH:mm:ss");
        return DateUtil.getTimestampString(dt);
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
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    Map<String, Object> object = (Map<String, Object>) response.get("object");
                    if (flag) {

                        List<ArtworkInvest> investList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestList")), new TypeToken<List<ArtworkInvest>>() {
                        }.getType());

                        if (investList != null) {
                            loadNum5(iListview, investorDatas, investList);
                            investList.clear();
                            if (investList.size() < Constants.pageSize) {

                                loadNum5(iListview, investorDatas, investList);
                                investList.clear();
                            }
                        }
                    } else {
                        List<ArtworkInvest> investList = AppApplication.getSingleGson().fromJson(AppApplication.getSingleGson().toJson(object.get("artworkInvestList")), new TypeToken<List<ArtworkInvest>>() {
                        }.getType());
                        if (investList != null) {
                            loadNum5(iListview, investorDatas, investList);
                            investList.clear();
                        }
                    }
                }
                setInvesterAdapter();
            }
        });
    }


    private static void loadNum5(ListViewForScrollView listView, List dataList, List list) {
        if (list.size() <= 5) {
            dataList.addAll(list);

        } else {
            for (int i = 0; i < 5; i++) {
                dataList.add(list.get(i));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_prise:
                //点赞
                if ("".equals(AppApplication.gUser.getId())) {
                    Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent2);
                } else {
                    if (!isPraise1) {
                        AnimationSet animationSet = new AnimationSet(true);
                        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        scaleAnimation.setDuration(200);
                        animationSet.addAnimation(scaleAnimation);
                        animationSet.setFillAfter(true);
                        animationSet.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                AnimationSet animationSet = new AnimationSet(true);
                                ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f, 1f, 1.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                scaleAnimation.setDuration(200);
                                animationSet.addAnimation(scaleAnimation);
                                animationSet.setFillAfter(true);
                                dianzan.startAnimation(animationSet);
                                dianzan.setEnabled(false);
                                praise(artWorkId, AppApplication.gUser.getId() + "");
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        dianzan.setImageResource(R.mipmap.after_praise);
                        dianzan.startAnimation(animationSet);
                    }
                }
                break;
            case R.id.ib_go:
                break;
            case R.id.iv_is_show:
               /* if (isShow){
                iv_show.setImageResource(R.mipmap.no_show_progress);
                webView.setVisibility(View.INVISIBLE);
                isShow = false;
                } else if(!isShow) {
                    iv_show.setImageResource(R.mipmap.show_progress);
                    webView.setVisibility(View.VISIBLE);
                    isShow = true;
                }*/
                /*isShow=!isShow;
                webView.clearAnimation();  //清除动画
                final int tempHight;
                final int startHight = 0;  //起始高度
                int durationMillis = 200;

                if(isShow){
                    */
                if (mExpandView.isExpand()) {
                    mExpandView.collapse();
                    //  sv.removeView(mExpandView);

                    // mHandler.post(ScrollRunnable2);
                    for (int i = 0; i < mExpandView.getChildCount(); i++) {
                        mExpandView.getChildAt(i).setVisibility(View.GONE);
                    }
                    /*View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.rz_project, null);
                    showHeadNum();
                    loadInvesterData(true, currentPage);
                    loadCommentData(true, currentPage);
                    showOther();
                    getActivity().setContentView(inflate);*/
                   /*EmptyFragment ef = new EmptyFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.expandView, ef);
                    //ft.setTransition(FragmentTransaction.TRANSIT_NONE);
                    ft.commit();*/
                    iv_show.setImageResource(R.mipmap.show_progress);
                    mExpandView.setVisibility(View.VISIBLE);
                } else {
                    mExpandView.expand();

                    for (int i = 0; i < mExpandView.getChildCount(); i++) {
                        mExpandView.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                    mHandler.post(ScrollRunnable);
                    iv_show.setImageResource(R.mipmap.no_show_progress);
                }
                break;
            case R.id.bt_comment:

                AppApplication.getSingleEditTextValidator()
                        .add(new ValidationModel(etComment, new ContentValidation()))
                        .execute();
                //表单没有检验通过直接退出方法
                if (!AppApplication.getSingleEditTextValidator().validate()) {
                    return;
                }
                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("artworkId", artWorkId + "");
                paramsMap.put("currentUserId", AppApplication.gUser.getId());
                paramsMap.put("content", etComment.getText().toString());
                /*if(!"".equals(messageId)){
                    paramsMap.put("messageId", messageId);
                }*/
                /*if ( !"".equals(fatherCommentId)){
                    paramsMap.put("fatherCommentId", fatherCommentId);
                }*/
                paramsMap.put("messageId", "");
                paramsMap.put("fatherCommentId", "");
                paramsMap.put("timestamp", System.currentTimeMillis() + "");
                try {
                    AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
                    paramsMap.put("signmsg", AppApplication.signmsg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NetRequestUtil.post(Constants.BASE_PATH + "artworkComment.do", paramsMap, new RongZiListCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {
                        e.printStackTrace();
                        System.out.println("评论失败");
                    }

                    @Override
                    public void onResponse(Map<String, Object> response) {
                        if (response.get("resultCode").equals("0")) {
                            ToastUtil.show(getActivity(), "评论成功", Toast.LENGTH_SHORT);
                        }
                    }
                });
                break;
            case R.id.ib_attention:
                if (AppApplication.gUser!=null && !"".equals(AppApplication.gUser.getId())){
                Intent intent=new Intent(getActivity(), AttentionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userId", AppApplication.gUser.getId());
                intent.putExtra("otherUserId",artwork.getAuthor().getId());
                if(AppApplication.gUser.getId().equals(artwork.getAuthor().getId())){
                    intent.putExtra("flag","1");
                }else {
                    intent.putExtra("flag","2");
                }
                startActivity(intent);
            }else {
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

                break;
            default:
                break;
        }

    }

    private Runnable ScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int scrollDifference = Utils.dip2px(getActivity(), 160);
            int off = screenHeight;//判断高度
            /*if (off > 0) {
                sv.scrollBy(0, 30);
                if (sv.getScrollY() == off) {
                    Thread.currentThread().interrupt();
                } else {
                    mHandler.postDelayed(this, 1000);
                }
            }*/
            sv.smoothScrollBy(0, screenHeight - scrollDifference);
        }
    };
    private Runnable ScrollRunnable2 = new Runnable() {
        @Override
        public void run() {
            int scrollDifference = Utils.dip2px(getActivity(), 160);
            int svHeight = sv.getHeight();
            int off = screenHeight;//判断高度
            /*if (off > 0) {
                sv.scrollBy(0, 30);
                if (sv.getScrollY() == off) {
                    Thread.currentThread().interrupt();
                } else {
                    mHandler.postDelayed(this, 1000);
                }
            }*/
            sv.smoothScrollTo(0, mExpandView.getHeight() - svHeight);
        }
    };

    private void praise(String artworkId, String s) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("artworkId", artworkId + "");
        paramsMap.put("timestamp", System.currentTimeMillis() + "");
        try {
            AppApplication.signmsg = EncryptUtil.encrypt(paramsMap);
            paramsMap.put("signmsg", AppApplication.signmsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NetRequestUtil.post(Constants.BASE_PATH + "artworkPraise.do", paramsMap, new RongZiListCallBack() {
            @Override
            public void onError(Call call, Exception e) {
                e.printStackTrace();
                System.out.println("失败了");
            }

            @Override
            public void onResponse(Map<String, Object> response) {
                if ("0".equals(response.get("resultCode"))) {
                    ToastUtil.showLong(getActivity(), "点赞成功");
                    Message msg = Message.obtain();
                    msg.what = PRAISE_SUC;
                    handler.sendMessage(msg);
                }
            }
        });
    }


}