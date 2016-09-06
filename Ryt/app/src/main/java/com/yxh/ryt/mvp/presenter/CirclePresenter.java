package com.yxh.ryt.mvp.presenter;

import android.view.View;

import com.yxh.ryt.listener.IDataRequestListener;
import com.yxh.ryt.mvp.contract.CircleContract;
import com.yxh.ryt.mvp.modle.CircleModel;
import com.yxh.ryt.vo.ArtWorkPraise;
import com.yxh.ryt.vo.ArtworkComment;
import com.yxh.ryt.vo.ArtworkMessage;
import com.yxh.ryt.vo.CommentConfig;

import java.util.List;


/**
 * @author yiw
 * @ClassName: CirclePresenter
 * @Description: 通知model请求服务器和通知view更新
 * @date 2015-12-28 下午4:06:03
 */
public class CirclePresenter implements CircleContract.Presenter {
    private CircleModel circleModel;
    private CircleContract.View view;
    private String str;

    public CirclePresenter(CircleContract.View view, String str) {
        this.str = str;
        circleModel = new CircleModel(str);
        this.view = view;
    }
    public CirclePresenter(CircleContract.View view, String str,String messageId) {
        this.str = str;
        circleModel = new CircleModel(str);
        this.view = view;
    }

    public void loadData(int pageNum, String str, final int loadType) {

        //List<CircleItem> datas = DatasUtil.createCircleDatas(str);

        circleModel.loadData(pageNum, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object circleDatas) {
                /*if (view != null) {
                }*/
                view.update2loadData(loadType, (List<ArtworkMessage>) circleDatas);
            }
        });
        /*if(view!=null){
            view.update2loadData(loadType, datas);
        }*/

    }


    /**
     * @param circleId
     * @return void    返回类型
     * @throws
     * @Title: deleteCircle
     * @Description: 删除动态
     */
    public void deleteCircle(int pageNum, final String circleId) {
        circleModel.deleteCircle(pageNum, new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                if (view != null) {
                    view.update2DeleteCircle(circleId);
                }
            }
        });
    }

    /**
     * @param circlePosition
     * @return void    返回类型
     * @throws
     * @Title: addFavort
     * @Description: 点赞
     */
    public void addFavort(final ArtworkMessage artworkMessage, final int circlePosition) {
        circleModel.addFavort(artworkMessage,circlePosition, new IDataRequestListener() {
            @Override
            public void loadSuccess(Object praiseList) {

                if (view != null) {
                    view.update2AddFavorite(circlePosition, ((List<ArtWorkPraise>)praiseList).get(0));
                }

            }
        });
    }

    /**
     * @param @param circlePosition
     * @param @param favortId
     * @return void    返回类型
     * @throws
     * @Title: deleteFavort
     * @Description: 取消点赞
     */
    public void deleteFavort(final ArtworkMessage artworkMessage, final int circlePosition, final String favortId) {
        circleModel.deleteFavort(artworkMessage, new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                if (view != null) {
                    view.update2DeleteFavort(circlePosition, favortId);
                }
            }
        });
    }

    /**
     * @param content
     * @param config  CommentConfig
     * @return void    返回类型
     * @throws
     * @Title: addComment
     * @Description: 增加评论
     */
    public void addComment(final String content, final ArtworkMessage artworkMessage, final CommentConfig config) {
        if (config == null) {
            return;
        }

        if (config.commentType == CommentConfig.Type.PUBLIC) {
            circleModel.addComment(content, artworkMessage,new IDataRequestListener() {

                @Override
                public void loadSuccess(Object comment) {
              /*  CommentItem newItem = null;
                if (config.commentType == CommentConfig.Type.PUBLIC) {
                    newItem = DatasUtil.createPublicComment(content);
                } else if (config.commentType == CommentConfig.Type.REPLY) {
                    newItem = DatasUtil.createReplyComment(config.replyUser, content);
                } */
                  /*  else if (config.commentType == CommentConfig.Type.REPLY) {
                        newItem = (ArtworkComment) comment;
                    }*/
                    ArtworkComment newItem = (ArtworkComment) comment;
                    if (view != null) {
                        view.update2AddComment(config.circlePosition, (ArtworkComment) comment);
                    }



                }

            });

        }else if (config.commentType == CommentConfig.Type.REPLY) {
            circleModel.addReplyComment(content, artworkMessage, config, new IDataRequestListener() {

                @Override
                public void loadSuccess(Object replyComment) {
                    ArtworkComment newItem = (ArtworkComment) replyComment;
                    if (view != null) {
                        view.update2AddComment(config.circlePosition, (ArtworkComment) replyComment);
                    }



                }

            });
        }

    }

    /**
     * @param @param circlePosition
     * @param @param commentId
     * @return void    返回类型
     * @throws
     * @Title: deleteComment
     * @Description: 删除评论
     */
   /* public void deleteComment(final int circlePosition, final String commentId) {
        circleModel.deleteComment(new IDataRequestListener() {

            @Override
            public void loadSuccess(Object object) {
                if (view != null) {
                    view.update2DeleteComment(circlePosition, commentId);
                }
            }

        });
    }
*/
    /**
     * @param commentConfig
     */
    public void showEditTextBody(CommentConfig commentConfig, ArtworkMessage artworkMessage) {
        if (view != null) {
            view.updateEditTextBodyVisible(View.VISIBLE, commentConfig, artworkMessage);
        }
    }


    /**
     * 清除对外部对象的引用，反正内存泄露。
     */
    public void recycle() {
        this.view = null;
    }
}
