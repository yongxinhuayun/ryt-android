package com.yxh.ryt.vo;

import java.math.BigDecimal;

/**
 * Created by 吴洪杰 on 2016/4/7.
 */
public class Artist {
    private String user_id;
    private String truename;
    private String username;
    private BigDecimal invest_goal_money;
    private BigDecimal turnover;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getInvest_goal_money() {
        return invest_goal_money;
    }

    public void setInvest_goal_money(BigDecimal invest_goal_money) {
        this.invest_goal_money = invest_goal_money;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }
}
