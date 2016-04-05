package com.yxh.ryt.vo;

/**
 * Created by Administrator on 2016/4/5.
 */
public class PrivateLetter {
    private String imageUrl;
    private String context;
    private String createDatetime;
    private String isWatch;

    public PrivateLetter(String isWatch, String createDatetime, String context, String imageUrl) {
        this.isWatch = isWatch;
        this.createDatetime = createDatetime;
        this.context = context;
        this.imageUrl = imageUrl;
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

    public String getContext() {
        return context;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setIsWatch(String isWatch) {
        this.isWatch = isWatch;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
