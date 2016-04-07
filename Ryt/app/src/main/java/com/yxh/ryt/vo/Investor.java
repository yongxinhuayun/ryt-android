package com.yxh.ryt.vo;

import java.math.BigDecimal;

/**
 * Created by 吴洪杰 on 2016/4/7.
 */
public class Investor {
    private String user_id;
    private String truename;
    private String username;
    private BigDecimal price;
    private BigDecimal rois;

    public BigDecimal getRois() {
        return rois;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRois(BigDecimal rois) {
        this.rois = rois;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
