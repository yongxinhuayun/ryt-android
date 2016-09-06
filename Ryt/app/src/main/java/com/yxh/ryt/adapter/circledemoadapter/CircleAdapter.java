package com.yxh.ryt.adapter.circledemoadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yxh.ryt.AppApplication;
import com.yxh.ryt.R;
import com.yxh.ryt.activity.ImagePagerActivity;
import com.yxh.ryt.adapter.circledemoadapter.viewholder.CircleViewHolder;
import com.yxh.ryt.adapter.circledemoadapter.viewholder.ImageViewHolder;
import com.yxh.ryt.adapter.circledemoadapter.viewholder.URLViewHolder;
import com.yxh.ryt.adapter.circledemoadapter.viewholder.VideoViewHolder;
import com.yxh.ryt.bean.ActionItem;
import com.yxh.ryt.mvp.presenter.CirclePresenter;
import com.yxh.ryt.util.DateUtil;
import com.yxh.ryt.util.utils.UrlUtils;
import com.yxh.ryt.vo.ArtWorkPraise;
import com.yxh.ryt.vo.ArtworkComment;
import com.yxh.ryt.vo.ArtworkMessage;
import com.yxh.ryt.vo.CommentConfig;
import com.yxh.ryt.widgets.CircleVideoView;
import com.yxh.ryt.widgets.CommentListView;
import com.yxh.ryt.widgets.MultiImageView;
import com.yxh.ryt.widgets.PraiseListView;
import com.yxh.ryt.widgets.SnsPopupWindow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by yiwei on 16/5/17.
 */
public class CircleAdapter extends BaseRecycleViewAdapter {
    public final static String TYPE_URL = "100";
    public final static String TYPE_IMG = "0";
    public final static String TYPE_VIDEO = "1";
    public final static int TYPE_HEAD = 5;

    private static final int STATE_IDLE = 0;
    private static final int STATE_ACTIVED = 1;
    private static final int STATE_DEACTIVED = 2;
    private String name;
    private String picUrl;
    private int videoState = STATE_IDLE;
    public static final int HEADVIEW_SIZE = 1;

    int curPlayIndex = -1;

    private CirclePresenter presenter;
    private Context context;
    private int hasFavort;

    public void setCirclePresenter(CirclePresenter presenter) {
        this.presenter = presenter;
    }

    public CircleAdapter(Context context, String name, String picUrl) {
        this.picUrl = picUrl;
        this.context = context;
        this.name = name;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        }

        int itemType = 0;
        ArtworkMessage item = (ArtworkMessage) datas.get(position - 1);
        if (item.getArtworkMessageAttachments() == null || item.getArtworkMessageAttachments().size() == 0) {
            return CircleViewHolder.TYPE_IMAGE;
        }
        if (TYPE_URL.equals(item.getArtworkMessageAttachments().get(0).getFileType())) {
            itemType = CircleViewHolder.TYPE_URL;
        } else if (TYPE_IMG.equals(item.getArtworkMessageAttachments().get(0).getFileType())) {
            itemType = CircleViewHolder.TYPE_IMAGE;
        } else if (TYPE_VIDEO.equals(item.getArtworkMessageAttachments().get(0).getFileType())) {
            itemType = CircleViewHolder.TYPE_VIDEO;
        }
        return itemType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == TYPE_HEAD) {
            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.head_circle, parent, false);
            viewHolder = new HeaderViewHolder(headView);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_circle_item, parent, false);

            if (viewType == CircleViewHolder.TYPE_URL) {
                viewHolder = new URLViewHolder(view);
            } else if (viewType == CircleViewHolder.TYPE_IMAGE) {
                viewHolder = new ImageViewHolder(view);
            } else if (viewType == CircleViewHolder.TYPE_VIDEO) {
                viewHolder = new VideoViewHolder(view);
            }
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (getItemViewType(position) == TYPE_HEAD) {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            holder.title.setText(name);
            AppApplication.displayImage(picUrl, holder.headPic);

        } else {
            final int circlePosition = position - HEADVIEW_SIZE;
            final CircleViewHolder holder = (CircleViewHolder) viewHolder;
            final ArtworkMessage artworkMessage = (ArtworkMessage) datas.get(circlePosition);
            final String circleId = artworkMessage.getId();
            final String content = artworkMessage.getContent();
            String createTime = DateUtil.millionToNearly(artworkMessage.getCreateDatetime());
            long createDatetime = artworkMessage.getCreateDatetime();
            final List<ArtWorkPraise> favortDatas = artworkMessage.getArtWorkPraiseList();
            final List<ArtworkComment> commentsDatas = artworkMessage.getArtworkCommentList();
            hasFavort = favortDatas.size();
            int hasComment = commentsDatas.size();

            // Glide.with(context).load(headImg).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.bg_no_photo).transform(new GlideCircleTransform(context)).into(holder.headIv);

            //holder.nameTv.setText(name);
            //初始化时间轴
            iniTime(holder.timeToday, holder.timeDay, holder.timeMonth, createDatetime);

            holder.timeTv.setText(createTime);

            if (!TextUtils.isEmpty(content)) {
                holder.contentTv.setText(UrlUtils.formatUrlString(content));
            }
            holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);

            if (AppApplication.gUser.getId().equals(artworkMessage.getCreator().getId())) {
                holder.deleteBtn.setVisibility(View.VISIBLE);
            } else {
                holder.deleteBtn.setVisibility(View.GONE);
            }
          /*  holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除
                    if (presenter != null) {
                        presenter.deleteCircle(circleId);
                    }
                }
            });*/
            if (hasFavort != 0 || hasComment != 0) {
                if (hasFavort != 0) {//处理点赞列表
                    holder.praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
                        @Override
                        public void onClick(int position) {
                            String userName = favortDatas.get(position).getUser().getName();
                            String userId = favortDatas.get(position).getUser().getId();
                            Toast.makeText(AppApplication.getSingleContext(), userName + " &id = " + userId, Toast.LENGTH_SHORT).show();
                        }
                    });
                    holder.praiseListView.setDatas(favortDatas);
                    holder.praiseListView.setVisibility(View.VISIBLE);
                } else {
                    holder.praiseListView.setVisibility(View.GONE);
                }

                if (hasComment != 0) {//处理评论列表
                    holder.commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int commentPosition) {
                            ArtworkComment commentItem = commentsDatas.get(commentPosition);
                           /* if (AppApplication.gUser.getId().equals(commentItem.getCreator().getId())) {//复制或者删除自己的评论

                                CommentDialog dialog = new CommentDialog(context, presenter, commentItem, circlePosition);
                                dialog.show();*/
                           /* } else {//回复别人的评论*/
                            if (presenter != null) {
                                CommentConfig config = new CommentConfig();
                                config.circlePosition = circlePosition;
                                config.commentPosition = commentPosition;
                                config.commentType = CommentConfig.Type.REPLY;
                                if (commentItem.getFatherComment() != null) {
                                    config.replyUser = commentItem.getFatherComment().getCreator();
                                } else {
                                    config.replyUser = commentItem.getCreator();
                                }
                                presenter.showEditTextBody(config, artworkMessage);
//                                }
                            }
                        }
                    });
        /*            holder.commentList.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(int commentPosition) {
                            //长按进行复制或者删除
                            ArtworkComment commentItem = commentsDatas.get(commentPosition);
                            CommentDialog dialog = new CommentDialog(context, presenter, commentItem, circlePosition);
                            dialog.show();
                        }
                    });*/
                    holder.commentList.setDatas(commentsDatas);
                    holder.commentList.setVisibility(View.VISIBLE);

                } else {
                    holder.commentList.setVisibility(View.GONE);
                }
                holder.digCommentBody.setVisibility(View.VISIBLE);
            } else {
                holder.digCommentBody.setVisibility(View.GONE);
            }

            holder.digLine.setVisibility(hasFavort != 0 && hasComment != 0 ? View.VISIBLE : View.GONE);

            final SnsPopupWindow snsPopupWindow = holder.snsPopupWindow;
            //判断是否已点赞
            String curUserFavortId = getCurUserFavortId(artworkMessage.getArtWorkPraiseList(), AppApplication.gUser.getId());
            if (!TextUtils.isEmpty(curUserFavortId)) {
                snsPopupWindow.getmActionItems().get(0).mTitle = "取消";
            } else {
                snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
            }

            snsPopupWindow.update();
            snsPopupWindow.setmItemClickListener(new PopupItemClickListener(circlePosition, artworkMessage,
                    curUserFavortId));
            holder.snsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //弹出popupwindow
                    snsPopupWindow.showPopupWindow(view, artworkMessage);
                }
            });

            holder.urlTipTv.setVisibility(View.GONE);
            switch (holder.viewType) {
         /*       case CircleViewHolder.TYPE_URL:// 处理链接动态的链接内容和和图片
                    if(holder instanceof URLViewHolder){
                        String linkImg = circleItem.getLinkImg();
                        String linkTitle = circleItem.getLinkTitle();
                        Glide.with(context).load(linkImg).into(((URLViewHolder)holder).urlImageIv);
                        ((URLViewHolder)holder).urlContentTv.setText(linkTitle);
                        ((URLViewHolder)holder).urlBody.setVisibility(View.VISIBLE);
                        ((URLViewHolder)holder).urlTipTv.setVisibility(View.VISIBLE);
                    }

                    break;*/
                case CircleViewHolder.TYPE_IMAGE:// 处理图片
                    if (holder instanceof ImageViewHolder) {
                        final List<String> photos = new ArrayList<>();
                        for (int i = 0; i < artworkMessage.getArtworkMessageAttachments().size(); i++) {
                            photos.add(artworkMessage.getArtworkMessageAttachments().get(i).getFileUri());
                        }
                        if (photos != null && photos.size() > 0) {
                            ((ImageViewHolder) holder).multiImageView.setVisibility(View.VISIBLE);
                            ((ImageViewHolder) holder).multiImageView.setList(photos);
                            ((ImageViewHolder) holder).multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    //imagesize是作为loading时的图片size
                                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                                    ImagePagerActivity.startImagePagerActivity(context, photos, position, imageSize);
                                }
                            });
                        } else {
                            ((ImageViewHolder) holder).multiImageView.setVisibility(View.GONE);
                        }
                    }

                    break;
                case CircleViewHolder.TYPE_VIDEO:
                    if (holder instanceof VideoViewHolder) {
                        ((VideoViewHolder) holder).videoView.setVideoUrl(artworkMessage.getArtworkMessageAttachments().get(0).getFileUri());
                        // ((VideoViewHolder)holder).videoView.setVideoImgUrl(circleItem.getVideoImgUrl());//视频封面图片
                        ((VideoViewHolder) holder).videoView.setPostion(position);
                        ((VideoViewHolder) holder).videoView.setOnPlayClickListener(new CircleVideoView.OnPlayClickListener() {
                            @Override
                            public void onPlayClick(int pos) {
                                curPlayIndex = pos;
                            }
                        });
                    }

                    break;
                default:
                    break;
            }
        }
    }

    private void iniTime(TextView to, TextView d, TextView m, long ts) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(ts));
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date(System.currentTimeMillis()));

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int curYear = calendar2.get(Calendar.YEAR);
        int curMonth = calendar2.get(Calendar.MONTH) + 1;
        int curDay = calendar2.get(Calendar.DAY_OF_MONTH);

        if (curYear == year && curMonth == month && curDay == day) {
            to.setVisibility(View.VISIBLE);
            d.setVisibility(View.INVISIBLE);
            m.setVisibility(View.INVISIBLE);
            to.setText("今天");
        }else if (curYear == year && curMonth == month && curDay == (day + 1)) {
            to.setVisibility(View.VISIBLE);
            d.setVisibility(View.INVISIBLE);
            m.setVisibility(View.INVISIBLE);
            to.setText("昨天");
        }else {
            if (0 < day && day < 10) {
                d.setText("0" + day + "");
            } else {
                d.setText(day + "");
            }
            if (0 < month && month < 10) {
                m.setText("0" + month + "");
            } else {
                m.setText(month + "月");
            }
        }

    }

    @Override
    public int getItemCount() {
        if (datas != null) {
            return datas.size() + 1;//有head需要加1
        } else {
            return 1;
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView headPic;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            headPic = (ImageView) itemView.findViewById(R.id.iv_pic);
        }
    }

    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
        private String mFavorId;
        //动态在列表中的位置
        private int mCirclePosition;
        private long mLasttime = 0;
        private ArtworkMessage mArtworkMessage;

        public PopupItemClickListener(int circlePosition, ArtworkMessage artworkMessage, String favorId) {
            this.mFavorId = favorId;
            this.mCirclePosition = circlePosition;
            this.mArtworkMessage = artworkMessage;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position, ArtworkMessage artworkMessage) {
            switch (position) {
                case 0://点赞、取消点赞
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    if (presenter != null) {
                        if ("赞".equals(actionitem.mTitle.toString())) {
                            presenter.addFavort(mArtworkMessage, mCirclePosition);
                        } else {//取消点赞
                            presenter.deleteFavort(mArtworkMessage, mCirclePosition, mFavorId);
                        }
                    }
                    break;
                case 1://发布评论
                    if (presenter != null) {
                        CommentConfig config = new CommentConfig();
                        config.circlePosition = mCirclePosition;
                        config.commentType = CommentConfig.Type.PUBLIC;
                        presenter.showEditTextBody(config, artworkMessage);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public String getCurUserFavortId(List<ArtWorkPraise> artWorkPraise, String curUserId) {
        String favortid = "";
        if (!TextUtils.isEmpty(curUserId) && hasFavort != 0) {
            for (ArtWorkPraise item : artWorkPraise) {
                if (curUserId.equals(item.getUser().getId())) {
                    favortid = item.getId();
                    return favortid;
                }
            }
        }
        return favortid;
    }
}
