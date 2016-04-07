package com.yxh.ryt.vo;

/**
 * Created by Administrator on 2016/4/7.
 */
public class Notifaction_user {
    private String id;
    private int userName;
    private int status;

    public Notifaction_user(String id, int status, int userName) {
        this.id = id;
        this.status = status;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public int getUserName() {
        return userName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(int userName) {
        this.userName = userName;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
