package com.yxh.ryt.vo;

/**
 * Created by Administrator on 2016/4/5.
 */
public class PrivateLetter {
    private String content;
    private long createDatetime;
    private int isWatch;

    public PrivateLetter(String content, int isWatch, long createDatetime) {
        this.content = content;
        this.isWatch = isWatch;
        this.createDatetime = createDatetime;
    }

    public String getContent() {
        return content;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public int getIsWatch() {
        return isWatch;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public void setIsWatch(int isWatch) {
        this.isWatch = isWatch;
    }
}
