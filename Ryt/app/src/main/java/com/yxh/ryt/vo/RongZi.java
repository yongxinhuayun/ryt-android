package com.yxh.ryt.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 吴洪杰 on 2016/4/5.
 */
public class RongZi {
    private String id;
    private String title;
    private String brief;
    private String description;
    private String status;
    private String type;
    private String step;
    private BigDecimal investGoalMoney;
    private BigDecimal investsMoney;
    private Long investStartDatetime;
    private Long investEndDatetime;
    private Long creationEndDatetime;
    private Long newCreationEmdDatetime;
    private Long auctionStartDatetime;
    private BigDecimal newBidingPrice;
    private Long bewBiddingDate;
    private Long auctionEndDatetime;
    private Long createDatetime;
    private String picture_url;
    private int investorsNum;
    private User author;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public BigDecimal getInvestGoalMoney() {
        return investGoalMoney;
    }

    public void setInvestGoalMoney(BigDecimal investGoalMoney) {
        this.investGoalMoney = investGoalMoney;
    }

    public Long getInvestStartDatetime() {
        return investStartDatetime;
    }

    public void setInvestStartDatetime(Long investStartDatetime) {
        this.investStartDatetime = investStartDatetime;
    }

    public Long getInvestEndDatetime() {
        return investEndDatetime;
    }

    public void setInvestEndDatetime(Long investEndDatetime) {
        this.investEndDatetime = investEndDatetime;
    }

    public Long getCreationEndDatetime() {
        return creationEndDatetime;
    }

    public void setCreationEndDatetime(Long creationEndDatetime) {
        this.creationEndDatetime = creationEndDatetime;
    }

    public Long getNewCreationEmdDatetime() {
        return newCreationEmdDatetime;
    }

    public void setNewCreationEmdDatetime(Long newCreationEmdDatetime) {
        this.newCreationEmdDatetime = newCreationEmdDatetime;
    }

    public Long getAuctionStartDatetime() {
        return auctionStartDatetime;
    }

    public void setAuctionStartDatetime(Long auctionStartDatetime) {
        this.auctionStartDatetime = auctionStartDatetime;
    }

    public BigDecimal getNewBidingPrice() {
        return newBidingPrice;
    }

    public void setNewBidingPrice(BigDecimal newBidingPrice) {
        this.newBidingPrice = newBidingPrice;
    }

    public Long getBewBiddingDate() {
        return bewBiddingDate;
    }

    public void setBewBiddingDate(Long bewBiddingDate) {
        this.bewBiddingDate = bewBiddingDate;
    }

    public Long getAuctionEndDatetime() {
        return auctionEndDatetime;
    }

    public void setAuctionEndDatetime(Long auctionEndDatetime) {
        this.auctionEndDatetime = auctionEndDatetime;
    }

    public Long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public int getInvestorsNum() {
        return investorsNum;
    }

    public void setInvestorsNum(int investorsNum) {
        this.investorsNum = investorsNum;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public BigDecimal getInvestsMoney() {
        return investsMoney;
    }

    public void setInvestsMoney(BigDecimal investsMoney) {
        this.investsMoney = investsMoney;
    }
}
