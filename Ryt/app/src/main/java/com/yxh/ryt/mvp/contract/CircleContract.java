package com.yxh.ryt.mvp.contract;


import com.yxh.ryt.vo.ArtWorkPraise;
import com.yxh.ryt.vo.ArtworkComment;
import com.yxh.ryt.vo.ArtworkMessage;
import com.yxh.ryt.vo.CommentConfig;

import java.util.List;

/**
 * Created by suneee on 2016/7/15.
 */
public interface CircleContract {

    interface View extends BaseView{
        void update2DeleteCircle(String circleId);
        void update2AddFavorite(int circlePosition, ArtWorkPraise addItem);
        void update2DeleteFavort(int circlePosition, String favortId);
        void update2AddComment(int circlePosition, ArtworkComment addItem);
        void update2DeleteComment(int circlePosition, String commentId);
        void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig, ArtworkMessage artworkMessage);
        void update2loadData(int loadType, List<ArtworkMessage> datas);
    }

    interface Presenter extends BasePresenter{
        void loadData(String str ,int loadType);
        void deleteCircle(final String circleId);
        void addFavort(final ArtworkMessage artworkMessage, final int circlePosition);
        void deleteFavort(final ArtworkMessage artworkMessage, final int circlePosition, final String favortId);
        void deleteComment(final int circlePosition, final String commentId);

    }
}
