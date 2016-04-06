package com.yxh.ryt.vo;

/**
 * Created by Administrator on 2016/4/5.
 */
public class Notification {
    private String content;
    private long createDatetime;
    private int isWatch;

    public Notification(String content, int isWatch, long createDatetime) {
        this.content = content;
        this.isWatch = isWatch;
        this.createDatetime = createDatetime;
    }

    public String getContent() {
        return content;
    }

    public int getIsWatch() {
        return isWatch;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIsWatch(int isWatch) {
        this.isWatch = isWatch;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }
}
