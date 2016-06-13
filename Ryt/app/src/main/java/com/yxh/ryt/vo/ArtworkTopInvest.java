package com.yxh.ryt.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/13.
 */
public class ArtworkTopInvest implements Serializable {
    private User user;
    private String money;

    public ArtworkTopInvest() {
    }

    public ArtworkTopInvest(User user, String money) {
        this.user = user;
        this.money = money;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
