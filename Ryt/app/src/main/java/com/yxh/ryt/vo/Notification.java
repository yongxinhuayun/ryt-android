package com.yxh.ryt.vo;

/**
 * Created by Administrator on 2016/4/5.
 */
public class Notification {
    private String imageUrl;
    private String content;
    private String createDatetime;
    private String isWatch;

    public Notification(String imageUrl, String isWatch, String createDatetime, String content) {
        this.imageUrl = imageUrl;
        this.isWatch = isWatch;
        this.createDatetime = createDatetime;
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getIsWatch() {
        return isWatch;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public String getContent() {
        return content;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setIsWatch(String isWatch) {
        this.isWatch = isWatch;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }
}
