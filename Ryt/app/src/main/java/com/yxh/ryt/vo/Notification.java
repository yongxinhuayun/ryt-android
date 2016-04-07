package com.yxh.ryt.vo;

/**
 * Created by Administrator on 2016/4/5.
 */
public class Notification {
    private String content;
    private long createDatetime;
    private int isWatch;
    private Notifaction_user notifaction_user;

    public Notification(String content, Notifaction_user notifaction_user, int isWatch, long createDatetime) {
        this.content = content;
        this.notifaction_user = notifaction_user;
        this.isWatch = isWatch;
        this.createDatetime = createDatetime;
    }

    public String getContent() {
        return content;
    }

    public Notifaction_user getNotifaction_user() {
        return notifaction_user;
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

    public void setNotifaction_user(Notifaction_user notifaction_user) {
        this.notifaction_user = notifaction_user;
    }

    public void setIsWatch(int isWatch) {
        this.isWatch = isWatch;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }
}
