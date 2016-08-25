package com.yxh.ryt.vo;


import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/19.
 */
public class ArtWorkBidding implements Serializable {

    /**
     *
     */

    private String id;
    private String price;
    private String status;
    private User creator;
    private long createDatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }
}
