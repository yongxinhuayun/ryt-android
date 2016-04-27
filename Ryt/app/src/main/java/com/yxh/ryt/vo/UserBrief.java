package com.yxh.ryt.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/21.
 *
 */
public class UserBrief implements Serializable {//用户简介表
    private String id;
    private String status;//0 作废 1 正常
    private String type;// 1 大师 2 普通用户
    private User user;//关联用户
    private String content;//简介内容
    private String signer;//签名
    private Date createDatetime;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }
    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}

